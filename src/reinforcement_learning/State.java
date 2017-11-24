package reinforcement_learning;

import java.util.ArrayList;

public class State {

	// # (hash) Wall
	// (space) open floor
	// . (period) Empty goal
	// @ (at) Player on floor
	// + (plus) Player on goal
	// $ (dollar) Box on floor
	// * (asterisk) Box on goal

	// 1 up
	// 2 down
	// 3 left
	// 4 right

	private char[][] level;
	/**
	 * x coordinate of the player
	 */
	private int x; 
	/**
	 * y coordinate of the player
	 */
	private int y; 
	/**
	 * number of exploration
	 */
	private int num_exploration;

	// private int[] x_boxs;//x coordinate of the boxes
	// private int[] y_boxs;// y coordinate of the boxes

	/**
	 * constructor of state
	 * @param level a 2D char array storing the level
	 * @param x int x coordinate of player
	 * @param y int y coordinate of player
	 */
	public State(char[][] level, int x, int y/*, int[] x_boxs, int[] y_boxs*/) {

		// 2D deep copy
		this.level = new char[level.length][];
		for (int i = 0; i < level.length; i++) {
			this.level[i] = new char[level[i].length];
			for (int j = 0; j < level[i].length; j++)
				this.level[i][j] = level[i][j];
		}

		this.x = x;
		this.y = y;
		this.num_exploration = 0;

		// //initialization for x_boxs and y_boxs
		// if (x_boxs.length != y_boxs.length){
		// //exception case - wrong cor_input for boxes
		// this.x_boxs = {x_boxs[0]};
		// this.y_boxs = {y_boxs[0]};
		// }
		// else{
		// this.x_boxs = new int[x_boxs.length]
		// this.y_boxs = new int[y_boxs.length]
		// for (int i = 0; i < x_boxs.length; i++){
		// this.x_boxs[i] = x_boxs[i];
		// this.y_boxs[i] = y_boxs[i];
		// }
		// }
	}
	/**
	* copy constructor
	* @param state state to copy
	*/
	public State(State state) {

		// 2D deep copy
		this.level = new char[state.level.length][];
		for (int i = 0; i < this.level.length; i++) {
			this.level[i] = new char[state.level[i].length];
			for (int j = 0; j < this.level[i].length; j++)
				this.level[i][j] = state.level[i][j];
		}

		this.x = state.x;
		this.y = state.y;
		this.num_exploration = state.num_exploration;

		// this.x_boxs = new int[state.x_boxs.length]
		// this.y_boxs = new int[state.y_boxs.length]
		// for (int i = 0; i < x_boxs.length; i++){
		// this.x_boxs[i] = state.x_boxs[i];
		// this.y_boxs[i] = state.y_boxs[i];
		// }
	}

	/**
	 * see whether going up is a valid move
	 * @return boolean whether going up is valid
	 */
	private boolean isUpValid() {
		// there's always an up, because I'll check later
		// to make sure you dont move into a wall
		char up = level[x - 1][y];
		;
		// if youre below a wall
		if (up == '#')
			// you cant move up
			return false;
		// if you can move up, set the up variable and check open space
		if (up == ' ' || up == '.')
			return true;
		// lets check if upup exists: if the row 2 above is
		// long enough to support this column, then carry on checking
		if (level[x - 2].length <= y)
			// if it's not, it's not an open space/goal, and there isn't
			// a space two rows above to push the box into
			return false;
		// now that we know there is a two rows up in this column,
		char up2 = level[x - 2][y];
		// and check if the space two rows up can accept a box (ie. its an empty space
		// or goal)
		if ((up == '$' || up == '*') && (up2 == ' ' || up2 == '.'))
			return true;
		// backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	/**
	 * see whether going down is a valid move
	 * @return boolean whether going down is valid
	 */
	private boolean isDownValid() {
		// there's always a down, because I'll check later
		// to make sure you dont move into a wall
		char down = level[x + 1][y];
		;
		// if youre above a wall
		if (down == '#')
			// you cant move down
			return false;
		// if you can move down, check for open space
		if (down == ' ' || down == '.')
			return true;
		// lets check if downdown exists: if the row 2 below is
		// long enough to support this column, then carry on checking
		if (level[x + 2].length <= y)
			// if it's not, it's not an open space/goal, and there isn't
			// a space two rows below to push the box into
			return false;
		// now that we know there is a two rows down in this column,
		char down2 = level[x + 2][y];
		// and check if the space two rows down can accept a box (ie. its an empty space
		// or goal)
		if ((down == '$' || down == '*') && (down2 == ' ' || down2 == '.'))
			return true;
		// backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	/**
	 * see whether going left is a valid move
	 * @return boolean whether going left is valid
	 */
	private boolean isLeftValid() {
		// there's always a left, because I'll check later
		// to make sure you dont move into a wall
		char left = level[x][y - 1];
		;
		// if youre right of a wall
		if (left == '#')
			// you cant move left
			return false;
		// if you can move left, check for open space
		if (left == ' ' || left == '.')
			return true;
		// lets check if leftleft exists: if the column is >1, then carry on checking
		if (y <= 1)
			// if it's not, it's not an open space/goal, and there isn't
			// a space two rows right to push the box into
			return false;
		// now that we know there is a two rows down in this column,
		char left2 = level[x][y - 2];
		// and check if the space two rows down can accept a box (ie. its an empty space
		// or goal)
		if ((left == '$' || left == '*') && (left2 == ' ' || left2 == '.'))
			return true;
		// backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	/**
	 * see whether going right is a valid move
	 * @return boolean whether going right is valid
	 */
	private boolean isRightValid() {
		// there's always a right, because I'll check later
		// to make sure you dont move into a wall
		char right = level[x][y + 1];
		// if youre left of a wall
		if (right == '#')
			// you cant move right
			return false;
		// if you can move right, check for open space
		if (right == ' ' || right == '.')
			return true;
		// lets check if rightright exists: if the row is at least 2 longer, then carry
		// on checking
		if (level[x].length <= y + 2)
			// if it's not, it's not an open space/goal, and there isn't
			// a space two rows right to push the box into
			return false;
		// now that we know there is a two rows down in this column,
		char right2 = level[x][y + 2];
		// and check if the space two rows down can accept a box (ie. its an empty space
		// or goal)
		if ((right == '$' || right == '*') && (right2 == ' ' || right2 == '.'))
			return true;
		// backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	/**
	 * compute the state from previous state and a direction
	 * @param par previous state
	 * @param dir direction to move
	 * @return boolean whether going up is valid
	 */
	public State computeState(State par, int dir) {
		char[][] oldlevel = par.getState();
		int x = par.getX();
		int y = par.getY();

		// 2D deep copy
		char[][] newlevel = new char[oldlevel.length][];
		for (int i = 0; i < oldlevel.length; i++) {
			newlevel[i] = new char[oldlevel[i].length];
			for (int j = 0; j < oldlevel[i].length; j++)
				newlevel[i][j] = oldlevel[i][j];
		}

		switch (dir) {
		// if move is up
		case 1:
			// the spot youre moving to is open floorspace
			if (newlevel[x - 1][y] == ' ')
				// move the player into the space
				newlevel[x - 1][y] = '@';
			// the spot youre moving to is an empty goal
			if (newlevel[x - 1][y] == '.')
				// move the player onto the goal
				newlevel[x - 1][y] = '+';
			// the spot youre moving to is a box-on-floor
			if (newlevel[x - 1][y] == '$') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x - 2][y] == ' ') {
					// make that space a box on floor
					newlevel[x - 2][y] = '$';
					// and move the player into the old box-space
					newlevel[x - 1][y] = '@';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x - 2][y] = '*';
					// and move the player into the old box-space
					newlevel[x - 1][y] = '@';
				}
			}
			// the spot youre moving to is a box-on-goal
			if (newlevel[x - 1][y] == '*') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x - 2][y] == ' ') {
					// make that space a box on floor
					newlevel[x - 2][y] = '$';
					// and move the player into the old box-space
					newlevel[x - 1][y] = '+';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x - 2][y] = '*';
					// and move the player into the old box-space
					newlevel[x - 1][y] = '+';
				}
			}
			// now that we've moved the player and the box (if there was one),
			// lets remove his tail
			// if he was on empty space,
			if (newlevel[x][y] == '@')
				// leave the space
				newlevel[x][y] = ' ';
			// otherwise he was on a goal (+),
			else
				// keep the goal
				newlevel[x][y] = '.';
			break;
		case 2:
			// and the spot youre moving to is open floorspace
			if (newlevel[x + 1][y] == ' ')
				// move the player into the space
				newlevel[x + 1][y] = '@';
			// and the spot youre moving to is an empty goal
			if (newlevel[x + 1][y] == '.')
				// move the player onto the goal
				newlevel[x + 1][y] = '+';
			// and the spot youre moving to is a box-on-floor
			if (newlevel[x + 1][y] == '$') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x + 2][y] == ' ') {
					// make that space a box on floor
					newlevel[x + 2][y] = '$';
					// and move the player into the old box-space
					newlevel[x + 1][y] = '@';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x + 2][y] = '*';
					// and move the player into the old box-space
					newlevel[x + 1][y] = '@';
				}
			}
			// and the spot youre moving to is a box-on-goal
			if (newlevel[x + 1][y] == '*') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x + 2][y] == ' ') {
					// make that space a box on floor
					newlevel[x + 2][y] = '$';
					// and move the player into the old box-space
					newlevel[x + 1][y] = '+';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x + 2][y] = '*';
					// and move the player into the old box-space
					newlevel[x + 1][y] = '+';
				}
			}
			// now that we've moved the player and the box, if there was one
			// lets remove his tail
			// if he was on empty space,
			if (newlevel[x][y] == '@')
				// leave the space
				newlevel[x][y] = ' ';
			// otherwise he was on a goal (+),
			else
				// keep the goal
				newlevel[x][y] = '.';
			break;
		case 3:
			// and the spot youre moving to is open floorspace
			if (newlevel[x][y - 1] == ' ')
				// move the player into the space
				newlevel[x][y - 1] = '@';
			// and the spot youre moving to is an empty goal
			if (newlevel[x][y - 1] == '.')
				// move the player onto the goal
				newlevel[x][y - 1] = '+';
			// and the spot youre moving to is a box-on-floor
			if (newlevel[x][y - 1] == '$') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x][y - 2] == ' ') {
					// make that space a box on floor
					newlevel[x][y - 2] = '$';
					// and move the player into the old box-space
					newlevel[x][y - 1] = '@';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x][y - 2] = '*';
					// and move the player into the old box-space
					newlevel[x][y - 1] = '@';
				}
			}
			// and the spot youre moving to is a box-on-goal
			if (newlevel[x][y - 1] == '*') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x][y - 2] == ' ') {
					// make that space a box on floor
					newlevel[x][y - 2] = '$';
					// and move the player into the old box-space
					newlevel[x][y - 1] = '+';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x][y - 2] = '*';
					// and move the player into the old box-space
					newlevel[x][y - 1] = '+';
				}
			}
			// now that we've moved the player and the box, if there was one
			// lets remove his tail
			// if he was on empty space,
			if (newlevel[x][y] == '@')
				// leave the space
				newlevel[x][y] = ' ';
			// otherwise he was on a goal (+),
			else
				// keep the goal
				newlevel[x][y] = '.';
			break;
		case 4:
			// and the spot youre moving to is open floorspace
			if (newlevel[x][y + 1] == ' ')
				// move the player into the space
				newlevel[x][y + 1] = '@';
			// and the spot youre moving to is an empty goal
			if (newlevel[x][y + 1] == '.')
				// move the player onto the goal
				newlevel[x][y + 1] = '+';
			// and the spot youre moving to is a box-on-floor
			if (newlevel[x][y + 1] == '$') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x][y + 2] == ' ') {
					// make that space a box on floor
					newlevel[x][y + 2] = '$';
					// and move the player into the old box-space
					newlevel[x][y + 1] = '@';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x][y + 2] = '*';
					// and move the player into the old box-space
					newlevel[x][y + 1] = '@';
				}
			}
			// and the spot youre moving to is a box-on-goal
			if (newlevel[x][y + 1] == '*') {
				// cost++;
				// and there's a space in front of it,
				if (newlevel[x][y + 2] == ' ') {
					// make that space a box on floor
					newlevel[x][y + 2] = '$';
					// and move the player into the old box-space
					newlevel[x][y + 1] = '+';
				}
				// if it's a goal ahead of the box
				else {
					// make the goal a box-on-goal
					newlevel[x][y + 2] = '*';
					// and move the player into the old box-space
					newlevel[x][y + 1] = '+';
				}
			}

			// now that we've moved the player (and the box, if there was one)
			// lets remove his tail
			// if he was on empty space,
			if (newlevel[x][y] == '@')
				// leave the space
				newlevel[x][y] = ' ';
			// otherwise he was on a goal (+),
			else
				// keep the goal
				newlevel[x][y] = '.';
			break;
		}
		int x_new = -1;
		int y_new = -1;
		for (int i = 0; i < newlevel.length; i++) {
			for (int j = 0; j < newlevel[i].length; j++) {
				if (newlevel[i][j] == '@' || newlevel[i][j] == '+') {
					x_new = i;
					y_new = j;
				}
			}
		}
		return new State(newlevel, x_new, y_new);

	}
	/**
	 * get possible action from current state
	 * @return int array storing the actions
	 */
	public int[] possibleActionsFromState() {
		ArrayList<Integer> result = new ArrayList<>();
		if (isUpValid()) {
			result.add(1);
		}
		if (isDownValid()) {
			result.add(2);
		}
		if (isLeftValid()) {
			result.add(3);
		}
		if (isRightValid()) {
			result.add(4);
		}

		return result.stream().mapToInt(i -> i).toArray();
	}

	// check whether the player is stick to a box
	// if this state and next_state are both stick to the box, it means it push the box
	
	/**
	 * see whether the player is sticking to a box
	 * @return boolean whether the player is sticking to a box
	 */
	public boolean stickToBox() {
		if(level[x-1][y] == '$' || level[x+1][y] == '$' || level[x][y+1] == '$' || level[x][y-1] == '$') {
			return true;
		}	
		return false;
	}
	
	// update the num_exploration
	/**
	 * update number of exploration
	 */
	public void update_explore() {
		this.num_exploration++;
	}
	// return the num_exploration
	/**
	 * return number of exploration
	 * @return number of exploration
	 */
	public int num_explore() {
		return this.num_exploration;
	}

	/**
	 * check whether any box is in deadlock
	 * @return boolean indicating deadlock or not
	 */
	public boolean checkDeadlock() {
		boolean deadlock = false;
		// checking the position of all box could be consuming
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				// box on the floor
				if (level[i][j] == '$') {
					// store the four-direction situation for a box #/*
					boolean right = (level[i][j + 1] == '#' || level[i][j + 1] == '*');
					boolean left = (level[i][j - 1] == '#' || level[i][j - 1] == '*');
					boolean up = (level[i + 1][j] == '#' || level[i + 1][j] == '*');
					boolean down = (level[i - 1][j] == '#' || level[i - 1][j] == '*');
					// case 1: corner
					if (right && (up || down) || left && (up || down)) {
						// deadlock = true;
						return true;
					}
					// case 2: stand against wall in the case where it is impossible to move away
					// from the wall again
					/* right wall */
					if (right) {
						boolean down_dead = false;// flag for deadlock when searching downward
						boolean up_dead = false;// flag for deadlock when searching upward
						// check downward
						for (int r = i; r < level.length; r++) {
							// check corner in advance
							if (level[r][j] == '*' || level[r][j] == '#') {
								down_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[r][j] == '.' || level[r][j + 1] != '#') {
								down_dead = false;
								break;
							}
						}
						// check upward
						for (int r = i; r >= 0; r--) {
							// check corner in advance
							if (level[r][j] == '*' || level[r][j] == '#') {
								up_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[r][j] == '.' || level[r][j + 1] != '#') {
								up_dead = false;
								break;
							}
						}
						if (up_dead && down_dead) {
							return true;
						}
					}
					/* left wall */
					else if (left) {
						boolean down_dead = false;// flag for deadlock when searching downward
						boolean up_dead = false;// flag for deadlock when searching upward
						// check downward
						for (int r = i; r < level.length; r++) {
							// check corner in advance
							if (level[r][j] == '*' || level[r][j] == '#') {
								down_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[r][j] == '.' || level[r][j - 1] != '#') {
								down_dead = false;
								break;
							}
						}
						// check upward
						for (int r = i; r >= 0; r--) {
							// check corner in advance
							if (level[r][j] == '*' || level[r][j] == '#') {
								up_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[r][j] == '.' || level[r][j - 1] != '#') {
								up_dead = false;
								break;
							}
						}
						if (up_dead && down_dead) {
							return true;
						}
					} else if (up) {
						boolean right_dead = false;// flag for deadlock when searching rightward
						boolean left_dead = false;// flag for deadlock when searching leftward
						// check rightward
						for (int u = j; u < level[i].length; u++) {
							// check corner in advance
							if (level[i][u] == '*' || level[i][u] == '#') {
								right_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[i][u] == '.' || level[i - 1][u] != '#') {
								right_dead = false;
								break;// break as long as find goal or find escape way
							}
						}
						// check leftward
						for (int u = j; u >= 0; u--) {
							// check corner in advance
							if (level[i][u] == '*' || level[i][u] == '#') {
								left_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[i][u] == '.' || level[i - 1][u] != '#') {
								left_dead = false;
								break;// break as long as find goal or find escape way
							}
						}
						if (right_dead && left_dead) {
							return true;
						}
					} else if (down) {
						boolean right_dead = false;// flag for deadlock when searching rightward
						boolean left_dead = false;// flag for deadlock when searching leftward
						// check rightward
						for (int u = j; u < level[i].length; u++) {
							// check corner in advance
							if (level[i][u] == '*' || level[i][u] == '#') {
								right_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[i][u] == '.' || level[i + 1][u] != '#') {
								right_dead = false;
								break;// break as long as find goal or find escape way
							}
						}
						// check leftward
						for (int u = j; u >= 0; u--) {
							// check corner in advance
							if (level[i][u] == '*' || level[i][u] == '#') {
								left_dead = true;
								break;
							}
							// check goal or space for escaping on right right
							if (level[i][u] == '.' || level[i + 1][u] != '#') {
								left_dead = false;
								break;// break as long as find goal or find escape way
							}
						}
						if (right_dead && left_dead) {
							return true;
						}
					}

				}
			}
		}
		return deadlock;
	}
	
	/**
	 * check whether goal state is reached
	 * @return boolean indicating whether goal state is reached
	 */
	public boolean isGoal() {
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				if (level[i][j] == '.' || level[i][j] == '+') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check number of goals that has been reached
	 * @return int number of goals that has been reached
	 */
	public int numOfGoals() {
		int num = 0;
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				if (level[i][j] == '*') {
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * get the current state
	 * @return char[][] staring the map
	 */
	public char[][] getState() {
		return level;
	}

	/**
	 * get the x coordinate of player
	 * @return int the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * get the y coordinate of player
	 * @return int the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * compare the current state to target state
	 * 
	 * @return boolean indicating whether the current state s the same as target state
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof State)) {
			return false;
		}
		State state = (State) o;
		if (x != state.x || y != state.y) {
			return false;
		}
		if (level.length != state.level.length) {
			return false;
		}

		for (int i = 0; i < level.length; i++) {
			if (level[i].length != state.level[i].length) {
				return false;
			}
			for (int j = 0; j < level[i].length; j++)
				if (level[i][j] != state.level[i][j]) {
					return false;
				}
		}
		return true;
	}

}
