import java.io.File;
import java.util.Scanner;



/**
 * The main class to run the sokoban AI
 * @author COMP 3211 Group6
 * @since 2017-11-23
 */
public class SokoTest {

	/**
	 * The main method to run the project
	 * @param args main
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String levelpath;
		int searchtype;
		char stats;

		if(args.length < 3){
			//user input
			Scanner input = new Scanner(System.in);
			System.out.println("Enter a sokoban puzzle file [<filepath>]:");
			levelpath = input.nextLine();
			System.out.println("Specify which search to use [1-7]: ");
			System.out.println("1) BFS");
			System.out.println("2) DFS");
			System.out.println("3) UCS");
			System.out.println("4) Greedy Best First Search (with open goals heuristic)");
			System.out.println("5) Greedy Best First Search (with Manhattan distance heuristic)");
			System.out.println("6) A* Search (with open goals heuristic)");
			System.out.println("7) A* Search (with Manhattan distance heuristic)");
			searchtype = input.nextInt();
			System.out.println("Statistics [y/n]:");
			stats = input.next().charAt(0);
		}
		else {
			levelpath = args[0];
			searchtype = Integer.parseInt(args[1]);
			stats = args[2].charAt(0);
		}

		//TREEPREP
		File lvl_src = new File(levelpath);
		LevelLoader ll = new LevelLoader(lvl_src);		
		STree tree = new STree(ll.init());
//		System.out.println(tree.getRoot().toString());
		//ENDTREEPREP
		
		//HEADER
		String searchstring;
		switch(searchtype) {
		case 1:
			searchstring = "BFS";
			break;
		case 2:
			searchstring = "DFS";
			break;
		case 3:
			searchstring = "UCS";
			break;
		case 4:
			searchstring = "Greedy (using OG)";
			break;
		case 5:
			searchstring = "Greedy (using MD)";
			break;
		case 6:
			searchstring = "A* (using OG)";
			break;
		case 7:
			searchstring = "A* (using MD)";
			break;
		default:
			searchstring = "nothing";
		}
		System.out.println("Running " + searchstring + " on,");
		tree.getRoot().printState();
		//ENDHEADER
		
		
		switch(searchtype) {
		case 1:
			if(stats == 'y')
				for(String s : tree.BFS())
					System.out.println(s);
			else
				System.out.println(tree.BFS()[0]);
			break;
		case 2:
			if(stats == 'y')
				for(String s : tree.DFS())
					System.out.println(s);
			else
				System.out.println(tree.DFS()[0]);
			break;
		case 3:
			if(stats == 'y')
				for(String s : tree.UCS())
					System.out.println(s);
			else
				System.out.println(tree.UCS()[0]);
			break;
		case 4:
			if(stats == 'y')
				for(String s : tree.GreedyOG())
					System.out.println(s);
			else
				System.out.println(tree.GreedyOG()[0]);
			break;
		case 5:
			if(stats == 'y')
				for(String s : tree.GreedyMD())
					System.out.println(s);
			else
				System.out.println(tree.GreedyMD()[0]);
			break;
		case 6:
			if(stats == 'y')
				for(String s : tree.AStarOG())
					System.out.println(s);
			else
				System.out.println(tree.AStarOG()[0]);
			break;
		case 7:
			if(stats == 'y')
				for(String s : tree.AStarMD())
					System.out.println(s);
			else
				System.out.println(tree.AStarMD()[0]);
			break;
		}
	
		File src = new File(levelpath);
		LevelLoader LL = new LevelLoader(src);	
		STree tre = new STree(LL.init());
		STree.JUSTKEEPSWIMMING = true;
//		System.out.println(tre.getRoot().toString());
		for(String s : tre.AStarOG()) {
			System.out.print("!");
			System.out.println(s);
		}
			
//		System.out.print("hehe");
	}



}