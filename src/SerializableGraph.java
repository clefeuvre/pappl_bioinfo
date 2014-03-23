import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.jgrapht.graph.DirectedPseudograph;

public class SerializableGraph implements java.io.Serializable {

	protected DirectedPseudograph<Node, Edge> graph;
	
	public SerializableGraph(DirectedPseudograph<Node, Edge> inputGraph) 
	{
		this.graph = inputGraph;
	}
	
	public void serialize(String fileName)
	{
		try 
		{
			FileOutputStream fO = new FileOutputStream(fileName + ".ser");
			ObjectOutputStream oO = new ObjectOutputStream(fO);
			oO.writeObject(this);
			oO.flush();
			oO.close();
			fO.close();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	} 

}
