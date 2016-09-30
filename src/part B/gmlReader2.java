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

class gmlReader2
{
	/**
	 * The main function for the class.
	 @return  The graph read.
	 */

	public static Graph main(String[] args) throws FileNotFoundException, IOException
	{ //Reading the file
		File f = new File("polblogs.gml");
		Scanner in = new Scanner(f);
		//Creating a new graph
		Graph g = new Graph();
		//Assigning attributes to each node
		g.addColumn("value",Integer.class);
		g.addColumn("label",String.class);
		g.addColumn("id",Integer.class);
		g.addColumn("sources",String.class);
		String line; 
		line = in.nextLine();
		//System.out.println("Read line "+line);

		while( in.hasNextLine())
		{ //line with the node
			if (line.equalsIgnoreCase("  node [") )
			{//adds a node in the graph
				Node n1 = g.addNode();

				//reads id
				in.next("id");
				//reads int representing id
				int soo = in.nextInt();
				//assigning id to the node
				n1.set("id",soo-1); 




				//label
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
				in.next("value");
				//reading the int value
				int s = in.nextInt();
				//completing the line
				in.nextLine();
				//assigning value of the node	
				n1.set("value",s);

				line=in.nextLine();

				//new line containing source		

				//source
				// reading the string after source
				char [] By= line.toCharArray();
				char [] Cy= new char [line.length()-13];
				for (int i = 0; i < (line.length()-13); i++)
				{Cy[i]=By[i+11];
				}
				String ghy = new String(Cy);






				//assigning source to the node 
				n1.set("sources",ghy);



				//reading bracket
				in.nextLine();



			}




			else
				if (line.equalsIgnoreCase("  edge [") )
				{      //if it reads edge



					in.next("source");
					//reading int representing source
					int s = in.nextInt();
					in.nextLine();

					in.next("target");
					//reading int representing target
					int t = in.nextInt();


					//adding an edge

					g.addEdge(s-1, t-1);



					line = in.nextLine();



				}

				else
					line=in.nextLine();


		}
		//obtaining a GRAPH
		return g;

	}
}