import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelLoader {



	//	# (hash) Wall
	//	_ (space) empty space
	//	. (period) Empty goal
	//	@ (at) Player on floor
	//	+ (plus) Player on goal 
	//  $ (dollar) Box on floor
	//	* (asterisk) Box on goal


	private char[][] levelmap;

	public LevelLoader (File levelsource) {
		parseRowList(loadRowList(levelsource));
		//printLevel();
	}

	public State init() {
		return new State(levelmap, getX(), getY());
	}

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
	
	private int getX() {
//		System.out.println("getting x: " + getPlayerLocation()[0]);
		return getPlayerLocation()[0];
	}
	
	private int getY() {
//		System.out.println("getting y: " + getPlayerLocation()[1]);
		return getPlayerLocation()[1];
	}

}
