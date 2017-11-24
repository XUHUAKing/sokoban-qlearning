package reinforcement_learning;
/**
 * This class is a container for QNode object which stores a pair of state and action
 * @author COMP3211 Group 6
 */
public class QNode {
	/**
	 * This is the state
	 */
	State state;
	
	/**
	 * This is the action
	 * 1 stands for up
	 * 2 stands for down
	 * 3 stands for left
	 * 4 stands for right
	 */
	int action;

	
    /**
     * This method is the constructor of a QNode object
     * @param  state the State object to be set
     * @param  action the action to be set
     */
	QNode(State state, int action) {
		this.state = state;
		this.action = action;
	}

    /**
     * This method is the overriding function of equals for QNode class
     * @param o the object to be compared with
     * @return the result is true if o is equal to this QNode object, otherwise the result if false
     */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QNode)) {
			return false;
		}
		QNode qnode = (QNode) o;
		if (state.equals(qnode.state) && action == qnode.action) {
			return true;
		} else {
			return false;
		}
	}

    /**
     * This method is the overriding function of hashCode for QNode class
     * @return the result is hash code for this QNode object
     */
	@Override
	public int hashCode() {

		String result = "";
		for (int i = 0; i < state.getState().length; i++) {
			for (int j = 0; j < state.getState()[i].length; j++) {
				result += state.getState()[i][j];
			}
		}
		int hash = 1;
		hash = hash * 17 + action;
		hash = hash * 31 + result.hashCode();
		return hash;
	}

}
