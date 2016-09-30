import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * The input class for the project. Reads the gml file containing the graph data and makes a graph out of the data by parsing.
 * The generated graph is returned to the caller.
 * */

class gmlReader
{
	/**
	 * The main function for the class.
	 @return  The graph read.
	 */
	public static Graph main(String[] args) throws FileNotFoundException, IOException
	{ //Reading the file
		File f = new File("polbooks.gml");
		Scanner in = new Scanner(f);

		//Creating a new graph
		Graph g = new Graph();
		//Assigning attributes to each node
		g.addColumn("value",Character.class);
		g.addColumn("label",String.class);
		g.addColumn("id",Integer.class);
		String line; 
		line = in.nextLine();


		while( in.hasNextLine())
		{ //line with the node
			if (line.equalsIgnoreCase("  node") )
			{Node n1 = g.addNode();
			//reads bracket
			in.nextLine();
			//reads id		
			in.next("id");
			//reads int representing id
			int soo = in.nextInt();
			//assigning id to the node
			n1.set("id",soo);  

			//completes previous line
			line=in.nextLine();
			//new line containing label
			line=in.nextLine();

			// reading the string after label
			char [] B= line.toCharArray();
			char [] C= new char [line.length()-12];
			for (int i = 0; i < (line.length()-12); i++)
			{C[i]=B[i+11];
			}
			String gh = new String(C);
			//assigning label to the node 

			n1.set("label", gh);

			//reading value 
			line =in.nextLine();
			//reading the character value
			char [] Bi= line.toCharArray();
			//assigning value of the node	
			n1.set("value",Bi[11]);

			//reading bracket

			in.nextLine();
			}
			else
				if (line.equalsIgnoreCase("  edge"))
				{      //if it reads edge
					//reading bracket
					in.nextLine();    
					in.next("source");
					//reading int representing source
					int s = in.nextInt();
					in.nextLine();
					//reading int representing target
					in.next("target");

					int t = in.nextInt();

					//adding an edge
					g.addEdge(s, t);

					//reading the bracket
					line = in.nextLine();
				}

				else
					line=in.nextLine();
		}
		//returning graph
		return g;
	}
}