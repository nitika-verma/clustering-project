import java.util.*;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Edge;

/**
 * This is a class to find clusters in the given unweighted graph. Maximizing the 
 * density of the clusters is favored. In order to form the clusters greedy algorithm for k-clustering is followed.
 * Each node is considered as a cluster and no edges are drawn. The, the edge with maximum weight is drawn while avoiding 
 * cycles. The process continues till the required number of clusters is obtained.
 * Depending on the graph, there might be single node clusters. All these are thrown away as the interest is primarily in dense clusters.
 */

public class clusters
{
	/* ------------------------
	   Data Members
	 * ------------------------ */
	
	// Arrays with weights
	private int weights[][];
	private int weightsS[][];
	
	// Data of graph made
	private Graph clusters;
	private int intNodes;
	int[] cluster;
	int clusterCount;

	// Graph internal fields
	public static final String ID="id";
	public static final String TYPE="value";
	 
	/* ------------------------
	   Public Methods

	 * ------------------------ */

	/**
     * Generate the random graphs using nodes of given graph.
     * Edges are added at random, independent of each other.
     * Edge ratio is calculated for each graph. It is defined as:
     * Ratio of number of edges between nodes of same type to the total number of edges.
     @param pq 		PriorityQueue containing the edges of the weighted graph
     @param g  		The graph in which clusters are to be made
     @param k 		Number of clusters to be formed
     */
	
	public void makeClusters(PriorityQueue<MyEdge> pq, Graph g,int k)
	{
		int n=g.getNodeCount();	
		intNodes=0;
		cluster=new int[n];
		for(int i=0;i<n;i++)
			cluster[i]=-1;
		clusterCount=0;
		for(int i=0;i<n-k;i++)
		{
			Edge e = pq.poll().getEdge();
			int n1=Integer.parseInt(e.getSourceNode().get(ID).toString());
			int n2=Integer.parseInt(e.getTargetNode().get(ID).toString());
			if(cluster[n1]==-1)
				if(cluster[n2]==-1)
				{
					cluster[n1]=clusterCount;
					cluster[n2]=clusterCount;
					clusterCount++;
				}
				else
				{
					cluster[n1]=cluster[n2];
				}
			else
				if(cluster[n2]==-1)
				{
					cluster[n2]=cluster[n1];
				}
				else
					if(cluster[n1]==cluster[n2])
					{
						i--;
					}
					else
					{
						int x=cluster[n2];
						for(int j=0;j<n;j++)
							if(cluster[j]==x)
							{
								cluster[j]=cluster[n1];
							}
					}
		}
		clusters = new Graph();
		clusters.addColumn("id",Integer.class);
		clusters.addColumn("red",Integer.class);
		clusters.addColumn("blue",Integer.class);

		int[] red=new int[clusterCount];
		int[] blue=new int[clusterCount];
			for(int i=0;i<n;i++)
		{
			if(cluster[i]!=-1)
				if( g.getNode(i).get(TYPE).toString().equals("1"))
					blue[cluster[i]]++;
				else
					red[cluster[i]]++;
		}

		for(int i=0;i<clusterCount;i++)
		{
			Node n1 = clusters.addNode();
			n1.set("id",i);
			n1.set("red", red[i]);
			n1.set("blue",blue[i]);
			intNodes+=(red[i]+blue[i]);
		}
		weights = new int[clusterCount][clusterCount];
		weightsS = new int[clusterCount][clusterCount];
		Iterator<Edge> iter= g.edges();
		while(iter.hasNext())
		{
			Edge e = iter.next();
			int n1 = Integer.parseInt(e.getSourceNode().get(ID).toString());
			int n2 = Integer.parseInt(e.getTargetNode().get(ID).toString());
			if(cluster[n1]!=-1 && cluster[n2]!=-1)
			{

				clusters.addEdge(cluster[n1],cluster[n2]);
				weights[cluster[n1]][cluster[n2]]=weights[cluster[n1]][cluster[n2]]+1;
				weights[cluster[n2]][cluster[n1]]=weights[cluster[n2]][cluster[n1]]+1;
				if(g.getNode(n1).get(TYPE).equals(g.getNode(n2).get(TYPE)))
				{	
					weightsS[cluster[n1]][cluster[n2]]=weightsS[cluster[n1]][cluster[n2]]+1;
					weightsS[cluster[n2]][cluster[n1]]=weightsS[cluster[n2]][cluster[n1]]+1;
				}
			}	
		}	
	}
	  /** Edge Ratios found earlier are returned
	   @return Array containing the weights
	   */

	public int[][] getWeights()
	{
		return weights;
	}
	
	  /** Edge Ratios found earlier are returned
	   @return Array containing the weights for same linking edges
	   */

	public int[][] getWeightsS()
	{
		return weightsS;
	}
	
	  /** Edge Ratios found earlier are returned
	   @return graph of clusters
	   */

	public Graph getClusters()
	{
		return clusters;
	}
	
	  /** Edge Ratios found earlier are returned
	   @return no of nodes in the clustered graph
	   */

	public int getInternalNodes()
	{
		return intNodes;
	}
	
	  /** Edge Ratios found earlier are returned
	   @return Number of valid clusters formed.
	   */
	public int getClusterCount()
	{
		return clusterCount;
	}
}