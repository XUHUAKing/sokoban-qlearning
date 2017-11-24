import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * @author COMP3211 Group 6
 * @since 2017-11-23
 */

/**
 * The constructor
 */
public class STree {

	/**
 	 * the initial state
	 */
	private State root;
	/**
 	 * used in the while loop to keep the search running
	 */
	static boolean JUSTKEEPSWIMMING = true;

	public STree(State r){
		root = r;
//		cleanLog();
	}

	private void cleanLog() {
		try {
			FileWriter fw = new FileWriter("log.txt");
			fw.write(new Date().toString() + '\n');
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
 	 * return the root
 	 * @return root The initial state 
	 */
	public State getRoot() {
		return root;
	}

	//for simple checking later
	/**
 	 * for simple checking later
 	 * @param solution the final state
 	 * @return String The solution 
	 */
	private String[] solution(State solution) {
		JUSTKEEPSWIMMING = false;
		//LOG
		//System.out.println("Solution Found");
		//ENDLOG
		String[] result = {solution.getPath()};
		return result;
	}

	/**
 	 * for simple checking later
 	 * @param solution final state
 	 * @param generated integer indicating number of node generated during the search
 	 * @param repeated integer indicating number of state repeated during the search
 	 * @param fringe integer indicating number of node inside the fringe after the search
 	 * @param seen integer indicating number of node explored during the search
 	 * @param start long starting time
 	 * @return String The solution 
	 */
	private String[] solution(State solution, int generated, int repeated, int fringe, int seen, long start) {
		JUSTKEEPSWIMMING = false;
		//LOG
		//System.out.println("Solution Found");
		//ENDLOG

		//give in millis
		double time = (System.nanoTime() - start) / 1000000000.0;
		String[] data = {
				solution.getPath(), 
				"\n",
				"Generated: " + Integer.toString(generated), 
				"Repeated: " + Integer.toString(repeated), 
				"Fringe: " + Integer.toString(fringe), 
				"Explored: " + Integer.toString(seen), 
				"Duration: " + String.valueOf((time)) 
		};
		return data;
	}

	/**
 	 * failed to find solution
 	 * @return String telling you that it failed to find solution
	 */
	private String[] failure() {
		String[] fail = {"Search Completed and No Solution Found"};
		return fail;
	}

	//BFS
	/**
	 * This is Breadth first Search
	 * @return A string containing the solution, u d l r corresponding to up down left right 
	 */
	public String[] BFS() {
		JUSTKEEPSWIMMING = true;
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		HashSet<String> explored = new HashSet<String>();
		Queue<State> frontier = new LinkedList<State>();
		frontier.add(node);

		if(node.isGoal())
			return solution(node, gen, rep, frontier.size(), explored.size(), start);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			explored.add(node.getStateString());

			//LOG
			//node.log("===========================================================\nSTATE: " + node.getPath() + "\n");
			//node.logState();
			//node.log("HAS UNEXPLORED CHILDREN: \n");
			//ENDLOG

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				//LOG
				//assert child.equals(new State(new State(new State(node, 'u'), 'd'), c)) : "CHILD EQUALITY BROKEN";
				//ENDLOG
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					//LOG
					//child.log(c + "\n");
					//child.logState();
					//child.log(child.getStateString() + '\n');
					//for(char mv : child.getValidMoves())
					//	child.log(mv + ", ");
					//child.log("\n\n");
					//ENDLOG

					if(child.isGoal())
						return solution(child, gen, rep, frontier.size(), explored.size(), start);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	//ENDBFS

	//DFS
	/**
	 * This is Depth first Search
	 * @return A string containing the solution 
	 */
	public String[] DFS() {
		JUSTKEEPSWIMMING = true;
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		HashSet<String> explored = new HashSet<String>();
		Stack<State> frontier = new Stack<State>();
		
		frontier.push(node);
		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.pop();
			if( !explored.contains(node.getStateString()) ) {
				explored.add(node.getStateString());
				if(node.isGoal())
					return solution(node, gen, rep, frontier.size(), explored.size(), start);
				for(char c : node.getValidMoves()) {
					State child = new State(node, c);
					gen++;
					if( !explored.contains(child.getStateString()) ) {
						frontier.add(child);
					}
					else
						rep++;
				}
			}
			else
				rep++;
		}
		return new String[0];
	}
	//ENDDFS
	
	/**
	 * Get the cost from priority queue
	 * @param pq PriorityQueue of state 
	 * @param comp target State  
	 * @return A integer indicating the cost
	 */
	private int getCostFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).getCost();
		}
		return -1;
	}

	/**
	 * This is Uniform Cost Search 
	 * @return A string containing the solution 
	 */
	public String[] UCS() {
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>();
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getCostFromPQ(frontier, child) > child.getCost()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	//ENDUCS

	//GREEDYOG
	/**
	 * Get the number of open goal from priority queue
	 * @param pq PriorityQueue of state 
	 * @param comp target State  
	 * @return A integer indicating the open goal
	 */
	private int getOGFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).openGoals();
		}
		return -1;
	}

	/**
	 * This is greedy search using open goal as heuristic
	 * @return A string containing the solution 
	 */
	public String[] GreedyOG() {
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new OpenGoals());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getOGFromPQ(frontier, child) > child.openGoals()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	//ENDGREEDYOG
	
	//GREEDYMD


	/**
	 * Get the Manhattan distance from priority queue
	 * @param pq PriorityQueue of state 
	 * @param comp target State  
	 * @return A integer indicating the Manhattan distance
	 */
	private int getMDFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).manhDist();
		}
		return -1;
	}

	/**
	 * This is greedy search using Manhattan Distance as heuristic
	 * @return A string containing the solution 
	 */
	public String[] GreedyMD() {
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new ManhDist());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getMDFromPQ(frontier, child) > child.manhDist()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	//ENDGREEDYMD
	
	//ASTARMD
	/**
	 * Get the A* Manhattan distance (cost + MD) from priority queue
	 * @param pq PriorityQueue of state
	 * @param comp target State  
	 * @return int A integer indicating the Manhattan distance
	 */
	private int getStarMDFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).manhDist() + ((State) orig).getCost();
		}
		return -1;
	}

	/**
	 * This is A* search using Manhattan Distance as heuristic
	 * @return A string containing the solution 
	 */
	public String[] AStarMD() {
		//data and results
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		//here we go
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new StarMD());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getStarMDFromPQ(frontier, child) > (child.manhDist() + child.getCost())){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	//ENDASTARMD
	
	//ASTAROG
	/**
	 * Get the A* open goal (cost + OG) from priority queue
	 * @param pq PriorityQueue of state
	 * @param comp target State 
	 * @return int A integer indicating the Manhattan distance
	 */
		private int getStarOGFromPQ(PriorityQueue<State> pq, State comp) {
			for(Object orig : pq.toArray()) {
				if( ((State) orig).equals(comp) )
					return ((State) orig).openGoals() + ((State) orig).getCost();
			}
			return -1;
		}
	/**
	 * This is A* search using open goal as heuristic
	 * @return A string containing the solution 
	 */
		public String[] AStarOG() {
			//data and results
			long start = System.nanoTime();
			int rep = 0;
			int gen = 1;

			//here we go
			State node = root;
			PriorityQueue<State> frontier = new PriorityQueue<State>(11, new StarOG());
			HashSet<String> explored = new HashSet<String>();
			frontier.add(node);

			while(JUSTKEEPSWIMMING) {
				if(frontier.peek() == null)
					return failure();
				node = frontier.poll();
				if(node.isGoal())
					return solution(node, gen, rep, frontier.size(), explored.size(), start);
				explored.add(node.getStateString());

				for(char c : node.getValidMoves()) {
					State child = new State(node, c);
					gen++;
					if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
						frontier.add(child);
					}
					else if(frontier.contains(child) && getStarOGFromPQ(frontier, child) > (child.openGoals() + child.getCost())){
						frontier.remove(child);
						frontier.add(child);
					}
					else if(explored.contains(child.getStateString()))
						rep++;
				}
			}
			return new String[0];
		}
		//ENDASTAROG



}



