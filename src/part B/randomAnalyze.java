import prefuse.data.Graph;
import prefuse.data.Node;
import java.util.Iterator;
import prefuse.data.Edge;

/**
 * This is a class to analyze any given graph by comparing it with generated random graphs having 
 * same number of nodes.Extended constructor support along with functions to
 * analyze by producing various stats are provided. Highly xustomizable by changing parameters.
 */

public class randomAnalyze
{
	/* ------------------------
	   Data Members
	 * ------------------------ */

	// Graph internal fields
	private String TYPE="value";
	private String ID="id";

	// Graph to be analyzed
	private Graph g;
	private int n_nodes;

	//Random graphs generated
	private Graph[] rGraph;
	private int n_rGraph;

	//Statistics generated
	private double ratio[];
	private double ccAll[];
	private double ccSame[];

	/* ------------------------
	   Constructors
	 * ------------------------ */


	/**
	 * Creates a new empty Graph and sets number of random graphs to be used to 30.
	 */
	public randomAnalyze()
	{
		g = new Graph();
		n_rGraph=30;
		rGraph= new Graph[31];
		ratio = new double[31];
		ccAll= new double[31];
		ccSame = new double[31];
		n_nodes=0;
	}
	/**
	 * Create a new Graph using the provided graph.
	 * @param Graph to be analyzed.
	 * @param Number of random graphs to use
	 * @param Graph field containing the attribute
	 */
	public randomAnalyze(Graph graph,int n, String type)
	{
		g = graph;
		n_rGraph=n;
		rGraph= new Graph[n+1];
		ratio = new double[n+1];
		ccAll = new double[n+1];
		ccSame = new double[n+1];
		TYPE=type;
		n_nodes=g.getNodeCount();
	}
	/**
	 * Create a new Graph using the provided graph.
	 * Number of random graphs to use set to 30
	 * @param Graph to be analyzed.
	 */
	public randomAnalyze(Graph graph)
	{
		this(graph,30);
	}
	/**
    Create a new Graph using the provided graph.
    @param Graph to be analyzed.
    @param Number of random graphs to use
	 */
	public randomAnalyze(Graph graph, int n)
	{
		this(graph,n,"type");
	}
	/**
	 * Create a new Graph using the provided graph.
     @param Graph to be analyzed.
     @param Graph field containing the attribute
	 */
	public randomAnalyze(Graph graph, String type)
	{
		this(graph,30,type);
	}

	/* ------------------------
	   Public Methods

	 * ------------------------ */

	/**
	 * Generate the random graphs using nodes of given graph.
	 * Edges are added at random, independent of each other.
	 * Edge ratio is calculated for each graph. It is defined as:
	 * Ratio of number of edges between nodes of same type to the total number of edges.
	 */

	public void generateRandoms()
	{
		int n_edges = g.getEdgeCount();
		for(int i=0;i<n_rGraph;i++)
		{
			rGraph[i]=new Graph(g.getNodeTable(),false);
			ratio[i]=0;
			int edgeCount = n_edges;	//Other two choices are in comments.
			//int edgeCount= (int)(n_nodes*(n_nodes-1)*Math.random());
			//int edgeCount= (int)(n_edges*Math.random()) +n_edges/2;
			//while(edgeCount >= n_nodes*(n_nodes-1))
			//	edgeCount= (int)(n_nodes*(n_nodes-1)*Math.random());
			for(int j=0;j<edgeCount;j++)			
			{
				int n1 = (int)(Math.random()*n_nodes);
				int n2 = (int)(Math.random()*n_nodes);
				while(n1==n2)
				{
					n2 = (int)(Math.random()*n_nodes);
				}
				Edge e = rGraph[i].getEdge(rGraph[i].getEdge(n1,n2));
				if(e == null)
				{	
					rGraph[i].addEdge(n1, n2);
					Node node1 = g.getNode(n1);
					Node node2 = g.getNode(n2);
					if(node1.get(TYPE).equals(node2.get(TYPE)))
						ratio[i]++;
				}
				else
				{
					j--;
				}
			}
			ratio[i]/=edgeCount;
		}

		ratio[n_rGraph] = findRatio();
		rGraph[n_rGraph] = g;
	}

	/** Edge ratio for the default graph to be analyzed is found.
    @return     Edge ratio
	 */
	public double findRatio()
	{
		return findRatio(g);
	}

	/** Edge ratio for the given graph is found.
	   @param graph		Graph to work upon
	   @return			Edge Ratio
	 */
	public double findRatio(Graph graph)
	{
		Iterator<Edge> iter = graph.edges();
		int sameEdge = 0;
		int edgeCount = 0;
		while(iter.hasNext())
		{
			Edge e = iter.next();
			if(e.getSourceNode().get(TYPE).equals(e.getTargetNode().get(TYPE)))
				sameEdge++;
			edgeCount++;
		}
		return (double)sameEdge/edgeCount;
	}
	/** Edge Ratios found earlier are returned
	   @param graph	[]	 Array containing the ratios
	 */
	public double[] getRatios()
	{
		return ratio;
	}
	/** Clustering coefficients are found for all the graphs.
	 * CC for a graph is defined as Average of CC of each node.
	 * Two types of CC are found for each graph:
	 * One considering all edges, other considering only edges between same type nodes.
	 */
	public void findCC()
	{ 
		for(int i=0;i<=n_rGraph;i++)
		{	
			//System.out.println("findCC for "+i);
			Matrix M1 = new Matrix(n_nodes,n_nodes);
			Matrix MS1 = new Matrix(n_nodes,n_nodes);
			Iterator<Edge> iter = rGraph[i].edges();
			while(iter.hasNext())
			{
				Edge e = iter.next();
				Node n1=e.getSourceNode();
				Node n2=e.getTargetNode();
				int j = Integer.parseInt(n1.get(ID).toString());
				int k = Integer.parseInt(n2.get(ID).toString());
				M1.set(j,k,1);
				M1.set(k,j,1);
				if(n1.get(TYPE).equals(n2.get(TYPE)))
				{
					MS1.set(j,k,1);
					MS1.set(k,j,1);
				}
			}

			ccAll[i] = findACC(M1,n_nodes);
			ccSame[i] = findACC(MS1,n_nodes);
		}
	}

	/** Returns the previously found values of Clustering coefficients on all edges.
	   @return Array of CC values
	 */
	public double[] getCCAll()
	{
		return ccAll;
	}
	  /** Returns the previously found values of Clustering coefficients on same type edges.
	   @return 	Array of CC values
	   */
	public double[] getCCSame()
	{
		return ccSame;
	}
	/* ------------------------
	   Private Methods

	 * ------------------------ */
	
	 /** Does the actual calculation of the Average Clustering Coefficient of the graph.
	   @param  The Adjacency Matrix of the graph
	   @param  The number of nodes in the graph	
	   @return 	CC value found
	   */
	private float findACC(Matrix M1, int n)
	{
		float sum=0;
		int triads=0;
		int neighbours=0;
		Matrix M2 = new Matrix(n,n);
		M2=M1.times(M1);

		for(int i=0;i<n;i++)
		{
			triads=0;
			neighbours=0;
			for(int k=0;k<n;k++)
			{
				triads+=M1.get(i,k)*M2.get(k,i);
				neighbours+=M1.get(i,k);
			}
			if(neighbours != 0 && neighbours != 1)
				sum+=(float)triads/(neighbours*(neighbours-1));
		}

		float ans = sum/n_nodes;
		return ans;
	}

}