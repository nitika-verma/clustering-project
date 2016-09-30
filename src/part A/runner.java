import java.io.*;
import prefuse.data.Graph;

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
	private static final String OUTPUT_FILE = "stats.data";
	private static final String TYPE = "value";
	
	
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
			g = gmlReader.main(null);
		}
		catch (Exception e )
		{
			e.printStackTrace();
			System.err.println("Error loading graph. Exiting...");
			System.exit(1);
		}
		
		//System.out.println(g.getNodeCount()+" "+g.getEdgeCount());
		randomAnalyze trial = new randomAnalyze(g,100,TYPE);
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
		initial_vis.main(null);
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
