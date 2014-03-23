import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// PARAMETERS
		int databaseId=1;
		String dbURL=null;
		String database_login=null;
		String database_password=null;
		String pathway_id=null;
 		boolean searching_by_patwhay=false;
		 		
 		if(args.length<4){
			System.out.println("Not enough parameters (minimum 4, --help to get help)");
			return;
		}
 		else if(args[0].equals("--help") || args[0].equals("-h")){
			System.out.println("PAPPL BIOINFORMATIQUE");
			System.out.println("This program needs 4 or 5 parameters when called :");
			System.out.println("1. address of the HipathDB database (on the form \"jdbc:mysql://localhost:3306/hipathdb_reformatted_20100309\"");
			System.out.println("2. login used to connect to the database");
			System.out.println("3. password used to connect to the database");
			System.out.println("4. number of the database used for this query (reminder : 1=KEGG, 2=BioCarta, 3=Nci-Nature, 4=Reactome)");
			System.out.println("5. -optional- id of the wanted pathway");
			
			return;
		}
		else if(args.length>5){
			System.out.println("Too many parameters (maximum 5, --help to get help)");
			return;
		}
		else{
			dbURL=args[0];
			database_login=args[1];
			database_password=args[2];
			databaseId=Integer.parseInt(args[3]);
			if(args.length==4){
				searching_by_patwhay=false;
			}
			else if(args.length==5){
				searching_by_patwhay=true;
				pathway_id=args[4];
			}
		}
		long time = System.currentTimeMillis();
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



		Connection con;
 		try 
 		{
			con=DriverManager.getConnection(dbURL,database_login,database_password);
		}
		catch(SQLException e){
			System.out.println("Could not connect to the database. Check address, login and password.");
			return;
		}
		try{
			Statement stmt = con.createStatement();

			///////////////////////////////////////////////////////////////////////////////////////
			//         Creating a HashMap linking supernodeIds with pathwayIds
			///////////////////////////////////////////////////////////////////////////////////////
			HashMap<String, HashSet<String>> nodeIds_pathways = new HashMap<String, HashSet<String>>();

			ResultSet path_res = stmt.executeQuery("SELECT DISTINCT reformatted_abstract_node.superNodeId, reformatted_pathway_abstractnode.pathwayId"
			+" FROM reformatted_abstract_node, reformatted_pathway_abstractnode"
			+" WHERE reformatted_abstract_node.superNodeId=reformatted_pathway_abstractnode.superNodeId"
			+" AND  reformatted_abstract_node.pathwayDbId='"+databaseId+"'"
			+" AND reformatted_pathway_abstractnode.pathwayDbId='"+databaseId+"'"
			+" ORDER BY reformatted_abstract_node.superNodeId;");
			while(path_res.next()){
				String nodeId = path_res.getString("superNodeId");
				String pathway = path_res.getString("pathwayId");
				if(nodeIds_pathways.containsKey(nodeId)){
					nodeIds_pathways.get(nodeId).add(pathway);
				}
				else{
					HashSet<String> set = new HashSet<String>();
					set.add(pathway);
					nodeIds_pathways.put(nodeId,set);
				}
			}

			///////////////////////////////////////////////////////////////////////////////////////
			//          get all the "normal" nodes
			///////////////////////////////////////////////////////////////////////////////////////


			ResultSet rs;
			if(searching_by_patwhay){
				rs = stmt.executeQuery("SELECT DISTINCT reformatted_entity_particpant.*, reformatted_abstract_node.superNodeId, reformatted_entity_information.entityType, reformatted_entity_information.entityName "
						+"FROM reformatted_entity_particpant "
						+"JOIN reformatted_abstract_node ON reformatted_abstract_node.participantId=reformatted_entity_particpant.participantId "
						+"JOIN reformatted_entity_information ON reformatted_entity_information.entityId = reformatted_entity_particpant.entityId "
						+"JOIN reformatted_pathway_abstractnode ON reformatted_pathway_abstractnode.superNodeId=reformatted_abstract_node.superNodeId "
						+"WHERE reformatted_abstract_node.pathwayDbId='"+databaseId+"' "
						+"AND reformatted_entity_particpant.pathwaydbId='"+databaseId+"' "
						+"AND reformatted_pathway_abstractnode.pathwayDbId='"+databaseId+"' "
						+"AND reformatted_pathway_abstractnode.pathwayId='"+pathway_id+"' "
						+"ORDER BY supernodeId;");
			}
			else{
				rs= stmt.executeQuery("SELECT DISTINCT reformatted_entity_particpant.*, reformatted_abstract_node.superNodeId, reformatted_entity_information.entityType, reformatted_entity_information.entityName "
					+ "FROM reformatted_entity_particpant "
					+ "JOIN reformatted_abstract_node ON reformatted_abstract_node.participantId=reformatted_entity_particpant.participantId "
					+ "JOIN reformatted_entity_information ON reformatted_entity_information.entityId = reformatted_entity_particpant.entityId "
					+ "WHERE reformatted_abstract_node.pathwayDbId='"+databaseId+"' AND reformatted_entity_particpant.pathwaydbId='"+databaseId+"' ORDER BY supernodeId;");
			}

			DirectedPseudograph<Node,Edge> graph = new DirectedPseudograph<Node,Edge>(Edge.class);

			SuperNode s=new SuperNode("init", databaseId);
			String current_superNodeId="init";
			int compteur = 0;
			Entity previous=new Entity();

			while (rs.next()) 
			{

				Entity n;
				if(rs.getString("entityType").equals("complex")){
					n = new ComplexNode(rs.getString("participantId"), rs.getString("entityId"), rs.getString("supernodeId"), rs.getInt("pathwayDbId"),rs.getString("entityName"), rs.getString("entityType"));		
				}
				else{
					n = new Entity(rs.getString("participantId"), rs.getString("entityId"),rs.getString("superNodeId"),rs.getInt("pathwaydbId"), rs.getString("entityName"), rs.getString("entityType"));
				}
				n.setFeature(rs.getString("feature"));
				n.setLocation(rs.getString("location"));

				//special for supernodes : we have to manage entities linked to 1 or several superNodes 
				if((rs.getInt("pathwaydbId")!=1 && rs.getString("superNodeId").startsWith("C")) || (rs.getInt("pathwaydbId")==1 && rs.getString("superNodeId").startsWith("E")) || (rs.getInt("pathwaydbId")==1 && rs.getString("superNodeId").startsWith("M"))){
					//if it is a different superNodeId, reinitialize the counter counting the number of entity with the same sueprNodeId (we need to create a superNode only if there are several entities with the same superNodeId)
					if(!current_superNodeId.equals(rs.getString("superNodeId"))){
						compteur = 0;
						current_superNodeId=rs.getString("superNodeId");
					}
					//if this is the second entity with the same superNodeId, we create a superNode and we add them both to this superNode
					else if(compteur==1){
						s = new SuperNode(rs.getString("superNodeId"),databaseId);
						graph.addVertex(s);
						s.addSubNode(n);
						s.addSubNode(previous);
						n.addSuperNode(s);
						previous.addSuperNode(s);
					}
					//if there is more than 2 entities we just add them to the superNode
					else if(compteur>1){
						s.addSubNode(n);
						n.addSuperNode(s);
					}
					//in all cases increment counter
					compteur++;
				}


				//We browse the graph to check if the node n is not already in the graph 
				//(We cannot  use the containsVertex method of the graph because it does not behave as explained in the doc)

				Set<Node> nodes = new HashSet<Node>();
				nodes = graph.vertexSet();
				boolean trouve = false;
				Iterator<Node> it = nodes.iterator();
				Node node=new Node();

				Entity alreadyInGraph = new Entity();
				while(it.hasNext() && !trouve){
					node = (Node)it.next();
					if(node instanceof Entity){
						if(((Entity)node).similar(n)){
							trouve=true;
							alreadyInGraph = (Entity)node;
						}
					}
				}
				//if the node is already in the graph, we check the node-already-in-the-graph's nodeId 
				if(trouve){
					//if the nodeId is a "supernode-type" nodeId (beginning with C for bases 2 3 and 4, with E or M for base 1) we replace it with the nodeId of the current node n
					if( (alreadyInGraph.getPathwaydbId()!=1 && alreadyInGraph.getNodeID().startsWith("C")) || (alreadyInGraph.getPathwaydbId()==1 &&  alreadyInGraph.getNodeID().startsWith("M")) || (alreadyInGraph.getPathwaydbId()==1 &&  alreadyInGraph.getNodeID().startsWith("E")) ){
						alreadyInGraph.setNodeID(n.getNodeID());
						if(nodeIds_pathways.containsKey(n.getNodeID())){
							alreadyInGraph.addSeveralPathways(nodeIds_pathways.get(n.getNodeID()));
						}
					}
				}
				//if it isn't we add it (and its sub_entities if it's a complex)
				else
				{
					graph.addVertex(n);
					if(nodeIds_pathways.containsKey(n.getNodeID())){
						n.addSeveralPathways(nodeIds_pathways.get(n.getNodeID()));
					}
					if(n.getType().equals("complex"))
					{
						ArrayList<Entity> sub_entities = ((ComplexNode)n).getSub_entities();
						for (Entity e : sub_entities) 
						{
							graph.addVertex(e);
						}
					}
				}
				previous = n;

			}

			stmt.close(); 

			///////////////////////////////////////////////////////////////////////////////////////
			//          get all special nodes (which do not appear in the "entity_particpant" table)
			///////////////////////////////////////////////////////////////////////////////////////

			Statement stmt_spe = con.createStatement();
			ResultSet rs_spe=null;
			if(searching_by_patwhay){
				rs_spe = stmt_spe.executeQuery("SELECT reformatted_abstract_node.*, reformatted_pathway_abstractnode.pathwayId FROM reformatted_abstract_node, reformatted_pathway_abstractnode "
						+"WHERE reformatted_abstract_node.pathwayDbId='"+databaseId+"' "
						+"AND reformatted_pathway_abstractnode.superNodeId= reformatted_abstract_node.superNodeId "
						+"AND reformatted_pathway_abstractnode.pathwayId='"+pathway_id+"' "
						+"AND reformatted_pathway_abstractnode.pathwayDbId='"+databaseId+"' "
						+"AND reformatted_abstract_node.participantId NOT IN "
						+"(SELECT participantId FROM reformatted_entity_particpant WHERE pathwayDbId='"+databaseId+"');");
			}
			else{
				rs_spe = stmt_spe.executeQuery("SELECT * FROM reformatted_abstract_node"
						+" WHERE reformatted_abstract_node.pathwayDbId='"+databaseId+"' "
						+"AND reformatted_abstract_node.participantId NOT IN "
						+"(SELECT participantId FROM reformatted_entity_particpant WHERE pathwayDbId='"+databaseId+"');");
			}
			while (rs_spe.next()){
				Entity n;
				if(rs_spe.getInt("reformatted_abstract_node.pathwayDbId")==1 && rs_spe.getString("superNodeId").startsWith("S")){
					//special nodes in DB1 with nodeId beginning by S are complex which have information associated to them in the entity_information table
					//special nodes beginning by M or E cannot be found in the database, so we do not create them

					Statement stmt_compl=con.createStatement();
					String requete = "SELECT * FROM reformatted_entity_information WHERE entityId='"+rs_spe.getString("participantId")+"' AND pathwayDbId='1'";
					ResultSet rs_compl=stmt_compl.executeQuery(requete);

					if(rs_compl.next()){
						n = new ComplexNode(rs_spe.getString("participantId"), rs_spe.getString("supernodeId"), rs_spe.getInt("reformatted_abstract_node.pathwayDbId"),	rs_compl.getString("entityName"),rs_compl.getString("entityType"));
					}
					else
					{
						n = new ComplexNode(rs_spe.getString("participantId"), rs_spe.getString("supernodeId"), rs_spe.getInt("reformatted_abstract_node.pathwayDbId"));
					}
					ArrayList<Entity> sub_entities = ((ComplexNode)n).createSubEntities(con);
					for (Entity e : sub_entities) 
					{
						graph.addVertex(e);
					}
					stmt_compl.close();
				}
				else
				{
					n = new SpecialNode(rs_spe.getString("participantId"), rs_spe.getString("supernodeId"), rs_spe.getInt("reformatted_abstract_node.pathwayDbId"));
					n.setName(rs_spe.getString("participantId"));
				}
				graph.addVertex(n);
			}
			stmt_spe.close();		

			Statement stmt2 = con.createStatement();
			ResultSet rs2 ;

			if(searching_by_patwhay){
				rs2 = stmt2.executeQuery("SELECT * FROM reformatted_pathway_relationPair WHERE pathwayDbId='"+databaseId+"' AND pathwayId ='"+pathway_id+"';");
			}
			else{
				rs2 = stmt2.executeQuery("SELECT * FROM reformatted_pathway_relationPair WHERE pathwayDbId='"+databaseId+"';");
			}

			while (rs2.next())
			{
				Set<Node> nodes = new HashSet<Node>();
				nodes = graph.vertexSet();
				String nodeAId = rs2.getString("nodeA");
				String nodeBId = rs2.getString("nodeB");
				String controllerId = rs2.getString("controller");
				Node nodeA=null;
				Node nodeB=null;
				Node controller=null;
				Iterator<Node> it = nodes.iterator();
				boolean nodeA_supernode=false;
				boolean nodeB_supernode=false;
				boolean controller_supernode=false;
				while(it.hasNext()){
					Node n = (Node)it.next();
					if(n.getNodeID()!=null){ //subentities have no nodeid, so we have to check first if there is one nodeId to prevent crash
						if(n.getNodeID().equals(nodeAId) && nodeA_supernode==false){
							nodeA=n;
							//if the nodeAId matches with an existent superNode we want the interaction to use the superNode 
							//and not one of its subnodes which may have the same nodeId
							if(n instanceof SuperNode){
								nodeA_supernode=true;
							}
						}
						if(n.getNodeID().equals(nodeBId) && nodeB_supernode==false){
							nodeB=n;
							if(n instanceof SuperNode){
								nodeB_supernode=true;
							}
						}

						if(n.getNodeID().equals(controllerId) && controller_supernode==false){
							controller=n;
							if(n instanceof SuperNode){
								controller_supernode=true;
							}
						}
					}
				}
				if(nodeA==null && nodeB!=null && controller!=null)
				{ 
					//if there is no nodeA, the edge comes from controller to nodeB
					Edge e = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),controller,nodeB);
					graph.addEdge(controller, nodeB, e);
				}
				else if(nodeB==null && nodeA!=null && controller!=null)
				{ 
					//if there is no nodeB but a nodeA and a controller, edge from controller to nodeA
					Edge e = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),controller,nodeA);
					graph.addEdge(controller, nodeA, e);
				}
				else if(nodeA!=null && nodeB!=null && controller!=null){ //if there is a nodeA, a nodeB and a controller, since we can't do an edge terminating on another edge we have to create a node
					PseudoNode pn = new PseudoNode(rs2.getString("interactionId"),databaseId, controller, nodeA, nodeB);
					graph.addVertex(pn);
					//edge from node A to PseudoNode
					Edge e1 = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),nodeA,pn);
					graph.addEdge(nodeA, pn, e1);
					//edge from controller to PseudoNode
					Edge e2 = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),controller,pn);
					graph.addEdge(controller, pn, e2);
					//edge from PseudoNode to nodeB
					Edge e3 = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),pn,nodeB);
					graph.addEdge(pn, nodeB, e3);
				}
				else if (nodeA!=null && nodeB!=null){//normal edge from nodeA to nodeB
					Edge e = new Edge(rs2.getInt("pathwayDbId"), rs2.getString("pathwayId"), rs2.getString("interactionId"),rs2.getString("interactionType"),rs2.getString("controlType"),nodeA,nodeB);
					graph.addEdge(nodeA, nodeB, e);
				}
				else
				{
					//No Edge created (relation with 0 nodes)
				}
			}
			stmt2.close();


			con.close();
			Driver theDriver=DriverManager.getDriver(dbURL);
			DriverManager.deregisterDriver(theDriver);
			time = System.currentTimeMillis() - time;
			System.out.println("time : "+time+ "ms");
			
			/* NOT USED FOR THE MOMENT 
			 * possibility to serialize the graph
			 * or to use a serialized graph (deserializing it)

			//to serialize graph 
			SerializableGraph ser_graph = new SerializableGraph(graph);
			ser_graph.serialize("graphe_serialise");
			
			//deserialize graph
			FileInputStream fileIn;
			try {
				fileIn = new FileInputStream("chemin_acces/graphe_serialise.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				SerializableGraph sg = (SerializableGraph) in.readObject();
				in.close();
				fileIn.close();
			} catch (FileNotFoundException e) {
				System.out.println("file not found");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/


			CytoscapeWriting writingTest = new CytoscapeWriting();
			writingTest.writeNodes(graph);
			writingTest.writeEdges(graph);
			writingTest.writeLinks(graph);
			System.out.println("Cytoscape files written");

			PHWriting ph = new PHWriting();
 			ph.findTransformations(graph);
 			ph.findComplexFormations(graph);
			String filename=null;
 			if(searching_by_patwhay){
				ph.writePHFile(pathway_id+".ph");
				filename = pathway_id+".ph";
				
 			}
 			else{
				ph.writePHFile("base_"+databaseId+".ph");
				filename="base_"+databaseId+".ph";
 			}
			ph.writePHFile(filename);
			System.out.println("PH file written ("+filename+")");

		} 

		catch (SQLException e) 
		{
			e.printStackTrace();
		}

	}

}