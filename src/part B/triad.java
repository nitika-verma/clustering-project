import prefuse.data.Graph;
import java.util.*;
import prefuse.data.Edge;

/**
 * This is a class to find the the weights of the edges in an undirected graph.
 * The weight of an edge is defined as the number of triads of which its is part.
 * A priority Queue on the weighted edges is returned.
 */

public class triad
{
	
	/**
	 *  The function implementing the required functionality.
	 @param graph	The graph in which triads are to be found.
	 @return A priority Queue of weighted edges of the graph.
	 */

	public static PriorityQueue<MyEdge> findTriads(Graph graph) 
	{
		int n=graph.getNodeCount();
		int[]source = new int[n];
		int[]target = new int[n];
		int[][] weights = new int[n][n];
		int[][] weightsS = new int[n][n];
		
		Matrix M = new Matrix(n, n, 0);

		final String TYPE="value";

		for(int i=0; i<n;i++)
		{
			source[i]=999999;         //any huge integer outside limit of the no. of graph nodes
			target[i]=999999;
			for(int j=0;j<n;j++)
				if(graph.getEdge(i,j)>=0)
				{
					M.set(i, j, 1);
					M.set(j,i,1);
				}
		}
			
		for (int node=0;node<n;node++)
		{ 
			for(int i=0;i<n;i++)
			{
				source[i]=999999;         //any huge integer outside limit of the no. of graph nodes
				target[i]=999999;

			}
			int k = 0;
			for(int i=0;i<n;i++)
			{
				if(M.get(node,i)==1)
				{
					source[k]=i;
					k+=1;
				}
			}
			int g = 0;
			for(int  i=0; i<n; i++)
			{
				if (M.get(i,node)==1)
				{
					target[g]=i;
					g+=1;
				}
			}
			int go =0;
			int goo =0;
			while(source[go]!=999999)
			{
				go+=1;
			}
			while(target[goo]!=999999)
			{
				goo+=1;
			}
			for (int i=0;i<go;i++)     
			{
				for (int j=0;j<goo;j++)      
				{	
					if (M.get(source[i],target[j])==1)
					{
						weights[node][source[i]] = weights[node][source[i]]+1;
						if(graph.getNode(i).get(TYPE).equals(graph.getNode(j).get(TYPE)))
							weightsS[node][source[i]] = weightsS[node][source[i]]+1;

					}
				}
			}	
		}
		

		PriorityQueue<MyEdge> pq= new PriorityQueue();
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				if(weights[i][j]!=0)
				{
					int x= graph.getEdge(i,j);
					if(x<0)
						x=graph.getEdge(j,i);
					Edge me=graph.getEdge(x);
					MyEdge e=new MyEdge(me,weights[i][j]);
					pq.offer(e);
				}
		return pq;
	}
}
