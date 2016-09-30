import prefuse.data.Edge;
/*
 * Class to define Edges with weights. 
 * Also, the edges are made comparable on the basis of their weights.
 */
public class MyEdge implements Comparable
{
	/* ------------------------
	   Data Members
	 * ------------------------ */
	
	// The edge and weight of the comparable Edge
	private Edge e;
	private int w;
	
	/* ------------------------
	   Constructors
	 * ------------------------ */

	   /** Construct an object wiht given edge and weight
	   @param edge    The underlying edge.
	   @param weight  The weight of the edge.
	   */
	public MyEdge(Edge edge, int weight)
	{
		e=edge;
		w=weight;
	}
	
	   /** Making this edge type comparable. 
	   @param 	o  The object with which to compare. Must be of type MyEdge
	   @return the result of the comparison, ascending on edge weights
	   */
	 
	public int compareTo(Object o)
	{
		if(!(o instanceof MyEdge))
		{
			System.err.println("Wrong object comparison");
			System.exit(1);
		}
		MyEdge otherEdge = (MyEdge)(o);
		if(otherEdge.w > w)
			return -1;
		else
			if(otherEdge.w == w)
				return 0;
		return 1;
	}
	
	/** Get the underlying edge.
	   @return  the edge implemented
	   */
	public Edge getEdge()
	{
		return e;
	}
	
	/** Get the weight of the edge.
	@return  the edge's weight
	*/
	public int getWeight()
	{
		return w;
	}
}
