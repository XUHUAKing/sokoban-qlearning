import java.util.Comparator;


/**
 * This class is to compare the manhanttan distance between two state
 * @author COMP 3211 Group
 * @since 2017-11-23
 */
public class ManhDist implements Comparator<State> {

	/**
	 * compare two states' manhanttan distance
	 * @param o1 input State1
	 * @param o2 input State2
	 * @return result an integer to show the result of comparision
	 */
	@Override
	public int compare(State o1, State o2) {
		if(o1.manhDist() == o2.manhDist())
			return 0;
		return (o1.manhDist() < o2.manhDist() ? -1 : 1);
	}

}
