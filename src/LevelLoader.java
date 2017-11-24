import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * The class is to load the .txt file
 * @author COMP 3211 Group6
 * @since 2017-11-23
 */
public class LevelLoader {



	//	# (hash) Wall
	//	_ (space) empty space
	//	. (period) Empty goal
	//	@ (at) Player on floor
	//	+ (plus) Player on goal 
	//  $ (dollar) Box on floor
	//	* (asterisk) Box on goal


	/**
	 * a 2D array to store the levelmap
	 */
	public char[][] levelmap;

	/**
	 * The constructor of the levelloader
	 * @param levelsource The input file
	 */
	public LevelLoader (File levelsource) {
		parseRowList(loadRowList(levelsource));
		//printLevel();
	}

	/**
	 * construct a new State depend on the level map
	 * @return State a new State
	 */
	public State init() {
		return new State(levelmap, getX(), getY());
	}

	/**
	 * load the txt file by row
	 * @param levelsource the source .txt file
	 * @return rowlist an arraylist of input levelmap
	 */
	private ArrayList<String> loadRowList(File levelsource) {
		Scanner input;
		ArrayList<String> rowlist = new ArrayList<String>();
		try {
			input = new Scanner(levelsource);
			//file should start with a puzzle height
			if(input.hasNextInt()){
				int height = Integer.parseInt(input.nextLine());
				//System.out.println(height);
				//iterate over that full height/depth
				for(int i = 0; i < height; i++) {
					//check for validity (is height as specified)
					if(input.hasNextLine()){
						//add the row to the stringmap
						rowlist.add(input.nextLine());
					}
					//invalid puzzle; bail
					else {
						System.out.println("That is not a valid puzzle file");
					}
				}
			}
			//close the scanner
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("The specified file could not be located");
			e.printStackTrace();
		}
		return rowlist;
	}

	/**
	 * change the signal of some specific char
	 * @param rowlist The arraylist after loading
	 */
	private void parseRowList(ArrayList<String> rowlist) {
		int height = rowlist.size();
		levelmap = new char[height][];
		for(int i = 0; i < height; i++) {
			levelmap[i] = rowlist.get(i).toCharArray();
//			NOTE: this is for taking the player off the board
//			for(char c : levelmap[i]) {
//				if(c == '@') {
//					c = ' ';
//				}
//				else if(c == '+')
//					c = '.';
//			}
		}
	}

	/**
	 * print the level map
	 */
	private void printLevel() {
		System.out.println("The level char[][] read in by the LevelLoader is:");
		System.out.println("=================================================");
		for(char[] row : levelmap){
			for(char c : row){
				System.out.print(c);
			}
			System.out.println();
		}
		System.out.println("=================================================");
	}
	
	/**
	 * get the player's location
	 * @return result an integer array store the location of player
	 */
	private int[] getPlayerLocation() {
		for(int i = 0; i < levelmap.length; i++) {
			for(int j = 0; j < levelmap[i].length; j++) {
				if(levelmap[i][j] == '@' || levelmap[i][j] == '+')
					return new int[] {i, j};
			}
		}
		//never reached, ignoring playerless game entry for now.
		System.out.println("something went wrong...");
		return new int[2];
	}
	
	/**
	 * get the x-coordinate of player and print out
	 * @return getPlayerLocation the x-coordinate of the player
	 */
	private int getX() {
//		System.out.println("getting x: " + getPlayerLocation()[0]);
		return getPlayerLocation()[0];
	}
	
	/**
	 * get the y-coordinate of player and print out
	 * @return getPlayerLocation the y-coordinate of the player
	 */
	private int getY() {
//		System.out.println("getting y: " + getPlayerLocation()[1]);
		return getPlayerLocation()[1];
	}

}
