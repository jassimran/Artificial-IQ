package project4;

public class PointPair<S> implements Comparable<PointPair<S>>{

	
	    private Float x;
	    private S y;
	    
	    public PointPair(Float x, S y)
	    {
	        this.x = x;
	        this.y = y;
	    }
	    
	    public int compareTo(PointPair<S> comparePair)
	    {
	        if(this.x > comparePair.x )
	        {
	            return 1;
	        }
	        else if (this.x == comparePair.x )
	        {
	            return 0;
	        }
	        return -1;
	    }
	    
	    public S getY()
	    {
	        return y;
	    }
	
}
