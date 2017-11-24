import java.util.Comparator;


/**
 * The class is to compare the open goal between two state
 * @author COMP 3211 Group6
 * @since 2017-11-23
 */
public class OpenGoals implements Comparator<State> {

 	/**
 	 * compare the open goals of two input state
 	 * @param o1 input State1
 	 * @param o2 input State2
 	 * @return result return an integer of the comparision
 	 */
	@Override
	public int compare(State o1, State o2) {
		if(o1.openGoals() == o2.openGoals())
			return 0;
		return (o1.openGoals() < o2.openGoals() ? -1 : 1);
	}

}
