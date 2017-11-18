import java.util.ArrayList;

public class State {
	
	//	# (hash) Wall
	//	  (space) open floor
	//	. (period) Empty goal
	//	@ (at) Player on floor
	//	+ (plus) Player on goal 
	//	$ (dollar) Box on floor
	//	* (asterisk) Box on goal
	
	// 1 up
	// 2 down
	// 3 left
	// 4 right
	
	private char[][] level;
	private int x; // x coordinate of the player
	private int y; // y coordinate of the player
	
	public State (char[][] level, int x, int y) {

		//2D deep copy
		this.level = new char[level.length][];
		for(int i = 0; i < level.length; i++){
			this.level[i] = new char[level[i].length];
			for(int j = 0; j < level[i].length; j++)
				this.level[i][j]=level[i][j];
		}

		this.x = x;
		this.y = y;
	}
	
	public State (State state) {

		//2D deep copy
		this.level = new char[state.level.length][];
		for(int i = 0; i < state.level[i].length; i++){
			this.level[i] = new char[state.level[i].length];
			for(int j = 0; j < level[i].length; j++)
				this.level[i][j]=state.level[i][j];
		}

		this.x = state.x;
		this.y = state.y;
	}
	
	private boolean isUpValid() {
		//there's always an up, because I'll check later 
		//to make sure you dont move into a wall
		char up = level[x - 1][y];;
		//if youre below a wall
		if(up == '#') 
			//you cant move up
			return false;
		//if you can move up, set the up variable and check open space
		if(up == ' ' || up == '.') 
			return true;
		//lets check if upup exists: if the row 2 above is
		//long enough to support this column, then carry on checking
		if(level[x - 2].length <= y)
			//if it's not, it's not an open space/goal, and there isn't 
			//a space two rows above to push the box into
			return false;
		//now that we know there is a two rows up in this column,
		char up2 = level[x - 2][y];
		//and check if the space two rows up can accept a box (ie. its an empty space or goal)
		if( (up == '$' || up == '*') && (up2 == ' ' || up2 == '.') )
			return true;
		//backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	private boolean isDownValid() {
		//there's always a down, because I'll check later 
		//to make sure you dont move into a wall					
		char down = level[x + 1][y];;
		//if youre above a wall
		if(down == '#') 
			//you cant move down
			return false;
		//if you can move down, check for open space
		if(down == ' ' || down == '.') 
			return true;
		//lets check if downdown exists: if the row 2 below is
		//long enough to support this column, then carry on checking
		if(level[x + 2].length <= y)
			//if it's not, it's not an open space/goal, and there isn't 
			//a space two rows below to push the box into
			return false;
		//now that we know there is a two rows down in this column,
		char down2 = level[x + 2][y];
		//and check if the space two rows down can accept a box (ie. its an empty space or goal)
		if( (down == '$' || down == '*') && (down2 == ' ' || down2 == '.') )
			return true;
		//backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	private boolean isLeftValid() {
		//there's always a left, because I'll check later 
		//to make sure you dont move into a wall					
		char left = level[x][y - 1];;
		//if youre right of a wall
		if(left == '#') 
			//you cant move left
			return false;
		//if you can move left, check for open space
		if(left == ' ' || left == '.') 
			return true;
		//lets check if leftleft exists: if the column is >1, then carry on checking
		if(y <= 1)
			//if it's not, it's not an open space/goal, and there isn't 
			//a space two rows right to push the box into
			return false;
		//now that we know there is a two rows down in this column,
		char left2 = level[x][y - 2];
		//and check if the space two rows down can accept a box (ie. its an empty space or goal)
		if( (left == '$' || left == '*') && (left2 == ' ' || left2 == '.') )
			return true;
		//backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}

	private boolean isRightValid() {
		//there's always a right, because I'll check later 
		//to make sure you dont move into a wall					
		char right = level[x][y + 1];;
		//if youre left of a wall
		if(right == '#') 
			//you cant move right
			return false;
		//if you can move right, check for open space
		if(right == ' ' || right == '.') 
			return true;
		//lets check if rightright exists: if the row is at least 2 longer, then carry on checking
		if(level[x].length <= y + 2)
			//if it's not, it's not an open space/goal, and there isn't 
			//a space two rows right to push the box into
			return false;
		//now that we know there is a two rows down in this column,
		char right2 = level[x][y + 2];
		//and check if the space two rows down can accept a box (ie. its an empty space or goal)
		if( (right == '$' || right == '*') && (right2 == ' ' || right2 == '.') )
			return true;
		//backup here shouldnt ever be reached or is maybe a really odd deadlock?
		// might as well default to false
		return false;
	}
	
	public State computeState(State par, int dir) {
		char[][] oldlevel = par.getState();
		int x = par.getX();
		int y = par.getY();

		//2D deep copy
		char[][] newlevel = new char[oldlevel.length][];
		for(int i = 0; i < oldlevel.length; i++){
			newlevel[i] = new char[oldlevel[i].length];
			for(int j = 0; j < oldlevel[i].length; j++)
				newlevel[i][j]=oldlevel[i][j];
		}

		switch (dir) {
		//if move is up
		case 1: 
			//the spot youre moving to is open floorspace
			if(newlevel[x - 1][y] == ' ')
				//move the player into the space
				newlevel[x - 1][y] = '@';
			//the spot youre moving to is an empty goal
			if(newlevel[x - 1][y] == '.')
				//move the player onto the goal
				newlevel[x - 1][y] = '+';
			//the spot youre moving to is a box-on-floor
			if(newlevel[x - 1][y] == '$') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x - 2][y] == ' '){
					//make that space a box on floor
					newlevel[x - 2][y] = '$';
					//and move the player into the old box-space
					newlevel[x - 1][y] = '@';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x - 2][y] = '*';
					//and move the player into the old box-space
					newlevel[x - 1][y] = '@';
				}	
			}
			//the spot youre moving to is a box-on-goal
			if(newlevel[x - 1][y] == '*') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x - 2][y] == ' '){
					//make that space a box on floor
					newlevel[x - 2][y] = '$';
					//and move the player into the old box-space
					newlevel[x - 1][y] = '+';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x - 2][y] = '*';
					//and move the player into the old box-space
					newlevel[x - 1][y] = '+';
				}	
			}
			//now that we've moved the player and the box (if there was one),
			//lets remove his tail
			//if he was on empty space,
			if(newlevel[x][y] == '@')
				//leave the space
				newlevel[x][y] = ' ';
			//otherwise he was on a goal (+),
			else
				//keep the goal
				newlevel[x][y] = '.';
			break;	
		case 2: 
			//and the spot youre moving to is open floorspace
			if(newlevel[x + 1][y] == ' ')
				//move the player into the space
				newlevel[x + 1][y] = '@';
			//and the spot youre moving to is an empty goal
			if(newlevel[x + 1][y] == '.')
				//move the player onto the goal
				newlevel[x + 1][y] = '+';
			//and the spot youre moving to is a box-on-floor
			if(newlevel[x + 1][y] == '$') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x + 2][y] == ' '){
					//make that space a box on floor
					newlevel[x + 2][y] = '$';
					//and move the player into the old box-space
					newlevel[x + 1][y] = '@';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x + 2][y] = '*';
					//and move the player into the old box-space
					newlevel[x + 1][y] = '@';
				}	
			}
			//and the spot youre moving to is a box-on-goal
			if(newlevel[x + 1][y] == '*') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x + 2][y] == ' '){
					//make that space a box on floor
					newlevel[x + 2][y] = '$';
					//and move the player into the old box-space
					newlevel[x + 1][y] = '+';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x + 2][y] = '*';
					//and move the player into the old box-space
					newlevel[x + 1][y] = '+';
				}	
			}
			//now that we've moved the player and the box, if there was one
			//lets remove his tail
			//if he was on empty space,
			if(newlevel[x][y] == '@')
				//leave the space
				newlevel[x][y] = ' ';
			//otherwise he was on a goal (+),
			else
				//keep the goal
				newlevel[x][y] = '.';
			break;	
		case 3: 
			//and the spot youre moving to is open floorspace
			if(newlevel[x][y - 1] == ' ')
				//move the player into the space
				newlevel[x][y - 1] = '@';
			//and the spot youre moving to is an empty goal
			if(newlevel[x][y - 1] == '.')
				//move the player onto the goal
				newlevel[x][y - 1] = '+';
			//and the spot youre moving to is a box-on-floor
			if(newlevel[x][y - 1] == '$') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x][y - 2] == ' '){
					//make that space a box on floor
					newlevel[x][y - 2] = '$';
					//and move the player into the old box-space
					newlevel[x][y - 1] = '@';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x][y - 2] = '*';
					//and move the player into the old box-space
					newlevel[x][y - 1] = '@';
				}	
			}
			//and the spot youre moving to is a box-on-goal
			if(newlevel[x][y - 1] == '*') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x][y - 2] == ' '){
					//make that space a box on floor
					newlevel[x][y - 2] = '$';
					//and move the player into the old box-space
					newlevel[x][y - 1] = '+';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x][y - 2] = '*';
					//and move the player into the old box-space
					newlevel[x][y - 1] = '+';
				}	
			}
			//now that we've moved the player and the box, if there was one
			//lets remove his tail
			//if he was on empty space,
			if(newlevel[x][y] == '@')
				//leave the space
				newlevel[x][y] = ' ';
			//otherwise he was on a goal (+),
			else
				//keep the goal
				newlevel[x][y] = '.';
			break;	
		case 4: 
			//and the spot youre moving to is open floorspace
			if(newlevel[x][y + 1] == ' ')
				//move the player into the space
				newlevel[x][y + 1] = '@';
			//and the spot youre moving to is an empty goal
			if(newlevel[x][y + 1] == '.')
				//move the player onto the goal
				newlevel[x][y + 1] = '+';
			//and the spot youre moving to is a box-on-floor
			if(newlevel[x][y + 1] == '$') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x][y + 2] == ' '){
					//make that space a box on floor
					newlevel[x][y + 2] = '$';
					//and move the player into the old box-space
					newlevel[x][y + 1] = '@';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x][y + 2] = '*';
					//and move the player into the old box-space
					newlevel[x][y + 1] = '@';
				}	
			}
			//and the spot youre moving to is a box-on-goal
			if(newlevel[x][y + 1] == '*') {
				// cost++;
				//and there's a space in front of it,
				if(newlevel[x][y + 2] == ' '){
					//make that space a box on floor
					newlevel[x][y + 2] = '$';
					//and move the player into the old box-space
					newlevel[x][y + 1] = '+';
				}
				//if it's a goal ahead of the box
				else{
					//make the goal a box-on-goal
					newlevel[x][y + 2] = '*';
					//and move the player into the old box-space
					newlevel[x][y + 1] = '+';
				}	
			}

			//now that we've moved the player (and the box, if there was one)
			//lets remove his tail
			//if he was on empty space,
			if(newlevel[x][y] == '@')
				//leave the space
				newlevel[x][y] = ' ';
			//otherwise he was on a goal (+),
			else
				//keep the goal
				newlevel[x][y] = '.';
			break;
		}
		int x_new = -1;
		int y_new = -1;
		for(int i = 0; i < newlevel.length; i++) {
			for(int j = 0; j < newlevel[i].length; j++) {
				if(newlevel[i][j] == '@' || newlevel[i][j] == '+')
				{
					x_new = i;
					y_new = j;
				}
			}
		}
		return new State(newlevel,x_new,y_new);
		
		
	}
	
    public int[] possibleActionsFromState() {
        ArrayList<Integer> result = new ArrayList<>();
        if(isUpValid()){
        	result.add(1);
        }
        if(isDownValid()){
        	result.add(2);
        }
        if(isLeftValid()){
        	result.add(3);
        }
        if(isRightValid()){
        	result.add(4);
        }

        return result.stream().mapToInt(i -> i).toArray();
    }
    
    public boolean checkDeadlock(){
    	boolean deadlock = false;
    	return deadlock;
    }
    
	public boolean isGoal() {
		for(int i = 0; i < level.length; i++){
			for(int j = 0; j < level[i].length; j++){
				if(level[i][j]=='.' || level[i][j]=='+'){
					return false;
				}
			}	
		}
		return true;
	}
	
	public int numOfGoals(){
		int num = 0;
		for(int i = 0; i < level.length; i++){
			for(int j = 0; j < level[i].length; j++){
				if(level[i][j]=='*'){
					num++;
				}
			}	
		}
		return num;
	}
	
    
	public char[][] getState() {
		return level;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof State))
        {
            return false;
        }
        State state = (State)o;
        if(x!=state.x || y!=state.y){
        	return false;
        }
        if(level.length!=state.level.length){
        	return false;
        }

		for(int i = 0; i < level.length; i++){
			if (level[i].length!=state.level[i].length){
				return false;
			}
			for(int j = 0; j < level[i].length; j++)
				if(level[i][j]!=state.level[i][j]){
					return false;
				}
		}
		return true;
    }
	
	
}
