import java.io.*;
import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * The main class of the project. Handles I/O of data and its flow between various classes
 * Also contains a method to find deviation of a data value as compared to a large set.
 */

public class runner
{

	/* ------------------------
	   Data Members
	 * ------------------------ */
	//I/O constants
	private static int k=700;
	private static String OUTPUT_FILE = "stats2.data";
	private static String TYPE = "value";


	/* ------------------------
	   Public Methods

	 * ------------------------ */

	/**
	 * The main function which is executed automatically when the project is run.
	 */

	public static void main(String[] argv)
	{
		Graph g = new Graph();
		try
		{
			g = gmlReader2.main(null);
		}
		catch (Exception e )
		{
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}
		//System.out.println("node"+g.getNodeCount()+"edge"+g.getEdgeCount());
		clusters cl = new clusters();
		cl.makeClusters(triad.findTriads(g), g, k);
		k=cl.getClusterCount();
		
		Graph clFound = cl.getClusters();
		Graph clNew = new Graph();
		clNew.addColumn("label", String.class);
		clNew.addColumn("id",Integer.class);
		clNew.addColumn("red",Integer.class);
		clNew.addColumn("blue",Integer.class);
		clNew.addColumn("value", Integer.class);
		int nodeCount=0;
		int validNodes[] = new int[k];
		for(int i=0;i<k;i++)
		{
			Node n=clFound.getNode(i);
			int red=Integer.parseInt(n.get("red").toString());
			int blue=Integer.parseInt(n.get("blue").toString());
			if(red+blue != 0)
			{
				Node n1 = clNew.addNode();
				n1.set("id",nodeCount);
				n1.set("red", red);
				n1.set("blue",blue);
				n1.set("label","Red Nodes: "+n.get("red").toString()+"     Blue nodes: "+n.get("blue").toString());
				if(red>blue)
					n1.set("value", 0);
				else
					n1.set("value",1);
				validNodes[nodeCount]=i;
				nodeCount++;
			}
		}
		
		int[][] weights = cl.getWeights();
		int[][] weightsS = cl.getWeightsS();
		int[][] newWeights = new int[nodeCount][nodeCount];
		int[][] newWeightsS = new int[nodeCount][nodeCount];
		for(int i=0;i<nodeCount;i++)
		{
			int nodeWeight=0;
			int nodeWeightS=0;
			for(int j=0;j<nodeCount;j++)
			{
				newWeights[i][j]=weights[validNodes[i]][validNodes[j]];
				newWeightsS[i][j]=weights[validNodes[i]][validNodes[j]];
				if(newWeights[i][j] >0)
				{
					clNew.addEdge(i, j);
					nodeWeight+=newWeights[i][j];
				}
				if(newWeightsS[i][j] >0)
				{
					nodeWeightS+=newWeightsS[i][j];
				}
			}
			Node n1=clNew.getNode(i);
			String label=n1.get("label").toString();
			label=label+"     All Edges: "+nodeWeight+"     Same Edges: "+nodeWeightS;
			n1.set("label",label);
		}
		visdatablog.main(clNew, newWeights, newWeightsS, cl.getInternalNodes());
		
		//Analysis starts
		randomAnalyze trial = new randomAnalyze(g,10,TYPE);
		trial.generateRandoms();

		PrintWriter pw = null;
		try
		{
			pw=new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE,false)));
		}
		catch (Exception e )
		{
			e.printStackTrace();
			System.err.println("Error writing file. Exiting...");
			System.exit(1);
		}
		
		double[] result;
		result = trial.getRatios();
		display(result,pw);
		
		double[] ccAll;
		trial.findCC();
		ccAll = trial.getCCAll();
		display(ccAll,pw);
		
		double[] ccSame;
		ccSame = trial.getCCSame();
		display(ccSame,pw);
		pw.close();	
	}
	/** Print an array to a PrintWriter.
    @param Array to be printed
    @param PrinterWriter to which output is to be sent
	 */
	private static void display(double[] data, PrintWriter pw)
	{

		for(int i=0;i<data.length-1;i++)
		{
			pw.print(data[i]+" ");
		}
		pw.println("\n"+data[data.length-1]);
		pw.println(statify(data));
	}
	/** Finds deviation of an element from rest of its set.
	 * The last element of the array is taken to be X. The mean m and
	 * standard deviation of rest of the set is found.
	 * The value (x-m)/SD is returned.
	@param 		Array with data to be analyzed
	@return 	Deviation coefficient
	 */

	private static double statify(double[] data)
	{
		int N=data.length-1;
		double sum=0;
		double sqSum=0;
		for(int i=0;i<N;i++)
		{
			sum+=data[i];
			sqSum+=(data[i]*data[i]);
		}
		double mean=sum/N;
		double SD=Math.sqrt(sqSum/N - mean*mean);
		double coeff = (data[N]-mean)/SD;
		return coeff;
	}
}