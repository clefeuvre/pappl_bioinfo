import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.jgrapht.graph.DirectedPseudograph;

public class Util 
{
	public static DirectedPseudograph<Node, Edge> deserializeGraph(String fileName)
	{
		DirectedPseudograph<Node,Edge> returnGraph = new DirectedPseudograph<Node,Edge>(Edge.class);
		try 
		{
			FileInputStream fI = new FileInputStream(fileName);
			ObjectInputStream oI = new ObjectInputStream(fI);
			SerializableGraph outputGraph = (SerializableGraph) oI.readObject();
			returnGraph = outputGraph.graph;
			fI.close();
			oI.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return returnGraph;
	}
}
