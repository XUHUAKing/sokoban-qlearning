import java.io.File;
//import java.util.Scanner;
import java.util.*;  

public class QLearning {
    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
    private final double reward = 100; // one of box reaches a goal
    private final double penalty = -100; // one of box reaches a deadlock
    private HashMap<QNode,Double> table;
    private State init_state;
    private State state;
    
    public static void main(String args[]) {
        QLearning ql = new QLearning();

        ql.init();
        ql.calculateQ();
        //ql.printQ();
        //ql.printPolicy();
    }
    
    public void init(){
    	
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a sokoban puzzle file [<filepath>]:");
		String levelpath = input.nextLine();
		File lvl_src = new File(levelpath);
		LevelLoader ll = new LevelLoader(lvl_src);	
		init_state = ll.init();
		state = ll.init();
		
		// initialize Q table
		table = new HashMap<QNode,Double>();
    }
    
    void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) { // Train cycles
            // Select random initial state
        	int counter = 0;
            while (!state.isGoal()) {
            	System.out.println(counter+" steps of move");
            	counter++;
            	int[] actionsFromCurrentState = state.possibleActionsFromState();
            	
                // Pick a random action from the ones possible
                int index = rand.nextInt(actionsFromCurrentState.length);
                int action = actionsFromCurrentState[index];
                if(!table.containsKey(new QNode(state,action))){
            		table.put(new QNode(state,action), 0.0);
            	}
                double q = table.get(new QNode(state,action));
                State nextState = state.computeState(state, action);
                
                double maxQ = maxQ(nextState);
                
                if(nextState.checkDeadlock()){
                	double r = penalty;
                	double value = q + alpha * (r + gamma * maxQ - q);
                    table.remove(new QNode(state,action));
                    table.put(new QNode(state,action), value);

                    state = new State(init_state);
                    continue;
                }
                else{
                	double r = (nextState.numOfGoals()-state.numOfGoals())*reward;
                	double value = q + alpha * (r + gamma * maxQ - q);
                    table.remove(new QNode(state,action));
                    table.put(new QNode(state,action), value);

                    state = nextState;
                }
            }
        }
    }
    
    double maxQ(State nextState) {
        int[] actionsFromState = nextState.possibleActionsFromState();
        double maxValue = Double.MIN_VALUE;
        for (int nextAction : actionsFromState) {
        	if(!table.containsKey(new QNode(nextState,nextAction))){
        		table.put(new QNode(nextState,nextAction), 0.0);
        	}
        	double value = table.get(new QNode(nextState,nextAction));
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }
    
    int getPolicyFromState(State state) {
        int[] actionsFromState = state.possibleActionsFromState();

        double maxValue = Double.MIN_VALUE;
        int policyGotoState = 0;

        // Pick to move to the state that has the maximum Q value
        for (int action : actionsFromState) {
        	double value = maxQ(state.computeState(state, action));

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = action;
            }
        }
        return policyGotoState;
    }
    
    
}
