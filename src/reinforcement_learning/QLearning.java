package reinforcement_learning;

import java.io.File;
//import java.util.Scanner;
import java.util.*;
/**
 * This class is a container for QLearning object which is the main part of reinforcement learning
 * @author COMP3211 Group 6
 */
public class QLearning {
	/**
	 * This is the learning rate for Q learning
	 */
	private final double alpha = 0.3; // Learning rate
	
	/**
	 * This is the reward decay for Q learning
	 */
	private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
	
	/**
	 * This is the reward when one of boxes reaches a goal
	 */
	private final double reward = 1000; // one of boxes reaches a goal
	
	/**
	 * This is the decay parameter for randomization
	 */
	private final double decay = 0.999; // decay for randomization
	
	/**
	 * This is the parameter for epsilon greedy
	 */
	private final int k = 100; // parameter for epsilon greedy
	
	/**
	 * This is the penalty when one of boxes reaches a deadlock
	 */
	private final double penalty = 0; // one of boxes reaches a deadlock
	
	/**
	 * This is the reward when the player pushes a box
	 */
	private final double push_reward = 0;// encourage the player to push box
	
	/**
	 * This is the living penalty
	 */
	private final double timePenalty = 0;// living penalty for training in order to optimize move steps
	
	/**
	 * This is the number of training epochs
	 */
	private final int trainingEpoch = 10000;// num of epochs to be trained
	
	/**
	 * This is the parameter for exploration function
	 */
	private final double trialTimesParam = 0; // parameter for exploration function
	
	/**
	 * This is the number of steps to move for print strategy
	 */
	private final int numSteps = 100; // num of steps to move;
	
	/**
	 * This is the Q table
	 */
	private HashMap<QNode, Double> table; // Q table
	
	/**
	 * This is the variable to record number of success
	 */
	private int numOfSuccess = 0;
	
	/**
	 * This is the initial state for Q learning
	 */
	private State initState;
	
	/**
	 * This is the current state for Q learning
	 */
	private State state;
    
	/**
     * This method is used to execute Q learning
     * @param  filePath the file to be loaded
     * @return strategy for Q learning
     */
	public static String execute(String filePath) {
		QLearning ql = new QLearning();

		ql.init(filePath);
		ql.calculateQ();
		ql.printStrategy();
		// ql.printQ();
		// ql.printPolicy();
		return ql.getStrategyFromTable();
		}

	/**
     * This method is to do initialization for Q learning
     * @param  filePath the file to be loaded
     */
	public void init(String filePath) {
		File lvl_src = new File(filePath);
		LevelLoader ll = new LevelLoader(lvl_src);
		initState = ll.init();
		state = ll.init();

		// initialize Q table
		table = new HashMap<QNode, Double>();
	}

	/**
     * This method is the main part of learning process
     */
	void calculateQ() {
		Random rand = new Random();
		for (int i = 0; i < trainingEpoch; i++) { // Train cycles
			// Select random initial state
			int counter = 0;
			while (!state.isGoal()&&counter <= 10000) {
				//System.out.println(counter + " steps of move");
				counter++;
				int[] actionsFromCurrentState = state.possibleActionsFromState();

				// Pick a random action from the ones possible
				int index = rand.nextInt(actionsFromCurrentState.length);
				int randomAction = actionsFromCurrentState[index];
				int suboptimalAction = getPolicyFromState(state);
				int action = 0;
				if (epsilonGreedy(i, decay, k)) {
					action = randomAction;
				} else {
					action = suboptimalAction;
				}
				if (!table.containsKey(new QNode(state, action))) {
					table.put(new QNode(state, action), 0.0);
				}
				double q = table.get(new QNode(state, action));
				State nextState = state.computeState(state, action);
				nextState.update_explore();// add exploration num - visit count n, at least one to avoid numeric problem
				// double maxQ = maxQ(nextState);
				double maxQ = modified_maxQ(nextState);

				if (nextState.checkDeadlock()) {
					double r = penalty;
					double value = q + alpha * (r + gamma * maxQ - q);
					table.remove(new QNode(state, action));
					table.put(new QNode(state, action), value);

					state = new State(initState);
					System.out.println("Deadlock happens!");
					break;
				} else {
					double r = (nextState.numOfGoals() - state.numOfGoals()) * reward + timePenalty;
					if (nextState.stickToBox() && state.stickToBox()) {
						r += push_reward;
					}
					double value = q + alpha * (r + gamma * maxQ - q);// (1-alpha)*Q(s,a) + alpha[sample]
					table.remove(new QNode(state, action));
					table.put(new QNode(state, action), value);
					if (nextState.isGoal()) {
						System.out.println("Reaches goal!");
						numOfSuccess++;
						// if (table.containsKey(new QNode(state, 2))) {
						// System.out.println("Value down: " + table.get(new QNode(state, 2)));
						// }
						// if (table.containsKey(new QNode(state, 1))) {
						// System.out.println("Value up: " + table.get(new QNode(state, 1)));
						// }
						state = new State(initState);
						break;
					}
					state = nextState;
				}
			}
		}

		System.out.println("Size of table:" + table.size());
	}

	/**
     * This method is used to do calculate the maximum Q value got the given state
     * @param  nextState the input state
     * @return the Q value of the given state
     */
	double maxQ(State nextState) {
		int[] actionsFromState = nextState.possibleActionsFromState();
		double maxValue = -Double.MAX_VALUE;
		for (int nextAction : actionsFromState) {
			if (!table.containsKey(new QNode(nextState, nextAction))) {
				table.put(new QNode(nextState, nextAction), 0.0);
			}
			double value = table.get(new QNode(nextState, nextAction));
			if (value > maxValue)
				maxValue = value;
		}
		return maxValue;
	}

	/**
     * This method is used to do calculate the maximum Q with exploration function implemented
     * @param  nextState the input state
     * @return the Q value of the given state
     */
	double modified_maxQ(State nextState) {
		int[] actionsFromState = nextState.possibleActionsFromState();
		double maxValue = -Double.MAX_VALUE;
		for (int nextAction : actionsFromState) {
			if (!table.containsKey(new QNode(nextState, nextAction))) {
				table.put(new QNode(nextState, nextAction), 0.0);
			}
			double value = table.get(new QNode(nextState, nextAction));
			if (value + this.trialTimesParam / nextState.num_explore() > maxValue)
				maxValue = value + this.trialTimesParam / nextState.num_explore();
		}
		return maxValue;
	}

	/**
     * This method is used to extract policy from the given state
     * @param  state the input state
     * @return the policy of the given state
     */
	int getPolicyFromState(State state) {
		int[] actionsFromState = state.possibleActionsFromState();
		double maxValue = -Double.MAX_VALUE;
		int action = 0;
		for (int nextAction : actionsFromState) {
			if (!table.containsKey(new QNode(state, nextAction))) {
				table.put(new QNode(state, nextAction), 0.0);
			}
			double value = table.get(new QNode(state, nextAction));
			if (value > maxValue) {
				maxValue = value;
				action = nextAction;
			}
		}
		return action;
	}

	/**
     * This method is used to extract policy from the Q table
     * @return the policy extracted from Q table
     */
	String getStrategyFromTable() {
		String result = "";
		State test = new State(initState);
		for (int i = 0; i < numSteps; i++) {
			int action = getPolicyFromState(test);
			switch (action) {
			case 1:
				result += "u";
				break;
			case 2:
				result += "d";
				break;
			case 3:
				result += "l";
				break;
			case 4:
				result += "r";
				break;
			default:
				break;
			}
			test = test.computeState(test, action);
			if (test.isGoal()) {
				System.out.println("Reaches goal");
				break;
			}
		}
		return result;
	}

	/**
     * This method is used to decide whether to adopt a random action or based on policy
     * @param numOfEpoches the number of current epochs
     * @param decay the decay parameter for randomization
     * @param k the parameter for epsilon greedy
     * @return the result is true if it decides to move randomly and the result is false if it decides to move based on policy
     */
	boolean epsilonGreedy(int numOfEpoches, double decay, int k) {
		Random rand = new Random();
		int n = rand.nextInt(100) + 1;
		if (n <= k * Math.pow((double) decay, (double) numOfEpoches)) {
			return true; // randomize
		} else {
			return false; // policy
		}
	}

	/**
     * This method is used to print the policy
     */
	void printStrategy() {
		System.out.println("Moving strategy:");
		String result = getStrategyFromTable();
		System.out.println(result);
		System.out.println("Number of success:" + numOfSuccess);
	}

}
