import java.util.Comparator;



/**
 * A class to compare two State
 * @author COMP 3211 Group6
 * @since 2017-11-23
 */
public class StarOG implements Comparator<State> {

	/**
	 * Compare two States' sum of opengoal and cost
	 * @param o1 input State1
	 * @param o2 input State2
	 * @return result return an integer to give the result of comparision
	 */
	@Override
	public int compare(State o1, State o2) {
		if((o1.openGoals() + o1.getCost()) == (o2.openGoals() + o2.getCost()))
			return 0;
		return (((o1.openGoals() + o1.getCost())  < (o2.openGoals() + o2.getCost())) ? -1 : 1);
	}


}
