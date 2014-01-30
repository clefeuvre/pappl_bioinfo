import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
		}
		catch
		(java.lang.ClassNotFoundException e)
		{
		System.err.print("ClassNotFoundException:");
		System.err.println(e.getMessage());
		}
		
		String dbURL="jdbc:mysql://localhost:3306/hipathdb_reformatted_20100309";
		try {
			Connection con=DriverManager.getConnection(dbURL,"root","papplsql");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT reformatted_entity_particpant.participantId, reformatted_entity_particpant.entityId, reformatted_abstract_node.superNodeId, reformatted_entity_particpant.pathwaydbId, reformatted_entity_information.entityType, reformatted_entity_information.entityName "
					+ "FROM reformatted_entity_particpant "
					+ "JOIN reformatted_abstract_node ON reformatted_abstract_node.participantId=reformatted_entity_particpant.participantId "
					+ "JOIN reformatted_entity_information ON reformatted_entity_information.entityId = reformatted_entity_particpant.entityId"
					+ " WHERE reformatted_abstract_node.pathwayDbId='3' AND reformatted_entity_particpant.pathwaydbId='3';");
			DirectedPseudograph<Node,Edge> graph = new DirectedPseudograph<Node,Edge>(Edge.class);
			while (rs.next()) {
				Node n = new Node(rs.getString(1), rs.getString(2),rs.getString(3),rs.getInt(4), rs.getString(5), rs.getString(6));
				graph.addVertex(n);
			}
			rs = stmt.executeQuery("SELECT * FROM reformatted_pathway_relationPair WHERE pathwayDbId='3';");
			while (rs.next()){
				Set<Node> nodes = new HashSet<Node>();
				nodes = graph.vertexSet();
				String nodeAId = rs.getString("nodeA");
				String nodeBId = rs.getString("nodeB");
				String controllerId = rs.getString("controller");
				System.out.println(nodeAId+ " " + nodeBId);
				Node nodeA=new Node();
				Node nodeB=new Node();
				Node controller=new Node();
				Iterator<Node> it = nodes.iterator();
				while(it.hasNext()){
					Node n = (Node)it.next();
					if(n.getNodeID().equals(nodeAId)){
						nodeA=n;
					}
					if(n.getNodeID().equals(nodeBId)){
						nodeB=n;
					}
					if(n.getNodeID().equals(controllerId)){
						controller=n;
					}
				}
				if(nodeA.getNodeID()==null && nodeB.getNodeID()!=null && controller.getNodeID()!=null){ //if there is no nodeA, the edge comes from controller to nodeB
					Edge e = new Edge(rs.getInt("pathwayDbId"), rs.getString("pathwayId"), rs.getString("interactionId"),rs.getString("interactionType"),controller,nodeB);
				}
				else if(nodeB.getNodeID()==null && nodeA.getNodeID()!=null && controller.getNodeID()!=null){ //if there is no nodeB but a nodeA and a controller, edge from controller to nodeA
					Edge e = new Edge(rs.getInt("pathwayDbId"), rs.getString("pathwayId"), rs.getString("interactionId"),rs.getString("interactionType"),controller,nodeA);
				}
				else if (nodeA.getNodeID()!=null && nodeB.getNodeID()!=null){//normal edge from nodeA to nodeB
					Edge e = new Edge(rs.getInt("pathwayDbId"), rs.getString("pathwayId"), rs.getString("interactionId"),rs.getString("interactionType"),nodeA,nodeB);
					graph.addEdge(nodeA, nodeB, e);
				}
				
			}
			stmt.close();
			
			
			con.close();
			Driver theDriver=DriverManager.getDriver(dbURL);
			DriverManager.deregisterDriver(theDriver);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
