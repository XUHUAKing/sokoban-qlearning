import java.util.Comparator;

/**
 * This class is to compare two state depend on manhanttan distance
 * @author COMP 3211 Group6
 * @since 2017-11-23
 */
public class StarMD implements Comparator<State> {

	/**
	 * compare two states' sum of manhanttan distance and cost
	 * @param o1 input State1
	 * @param o2 input State2
	 * @return result return an integer to show the result of comparision
	 */
	@Override
	public int compare(State o1, State o2) {
		if((o1.manhDist() + o1.getCost()) == (o2.manhDist() + o2.getCost()))
			return 0;
		return (((o1.manhDist() + o1.getCost())  < (o2.manhDist() + o2.getCost())) ? -1 : 1);
	}

}
