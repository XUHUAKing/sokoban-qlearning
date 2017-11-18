import java.util.Objects;

public class QNode {
	State state;
	int action;
	
	QNode(State state, int action){
		this.state = state;
		this.action = action;
	}
	
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof QNode))
        {
            return false;
        }
        QNode qnode = (QNode)o;
        if(state.equals(qnode.state)&&action==qnode.action){
        	return true;
        }
        else{
        	return false;
        }
    }
    
    @Override
    public int hashCode()
    {

    	String result = ""; 
		for(int i = 0; i < state.getState().length; i++){
			for(int j = 0; j < state.getState()[i].length; j++){
				result += state.getState()[i][j];
			}
		}
		int hash = 1;
        hash = hash * 17 + action;
        hash = hash * 31 + result.hashCode();
        return hash;
    }
	
}
