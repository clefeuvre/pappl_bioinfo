//package pappl_bioinformatique;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

public class CytoscapeWriting {

	public CytoscapeWriting() 

	{
	}

	public void writeNodes(DirectedPseudograph<Node, Edge> graph)
	{
		//Creating all .NA files, from all the attributes of the nodes

		int countCas1 = 0;
		int countCas2 = 0;
		try
		{

			ArrayList<BufferedWriter> BufferedWriters = new ArrayList<BufferedWriter>();
			ArrayList<FileWriter> FileWriters = new ArrayList<FileWriter>();
			ArrayList<String> Attributes = new ArrayList<String>();
			Attributes.add("participantId");
			Attributes.add("entityId");
			Attributes.add("pathwaydbId");
			Attributes.add("feature");
			Attributes.add("location");
			Attributes.add("nodeId");
			Attributes.add("name");
			Attributes.add("type");

			//ParticipantID file
			File participantFile = new File("ParticipantIDs.na");
			participantFile.createNewFile();
			FileWriter participantFw = new FileWriter(participantFile);
			BufferedWriter participantBw = new BufferedWriter(participantFw);
			participantBw.write("ParticipantID");
			BufferedWriters.add(participantBw); 
			FileWriters.add(participantFw);


			//EntityID file
			File entityFile = new File("EntityIDs.na");
			entityFile.createNewFile();
			FileWriter entityFw = new FileWriter(entityFile);
			BufferedWriter entityBw = new BufferedWriter(entityFw);
			entityBw.write("EntityID");
			BufferedWriters.add(entityBw); 
			FileWriters.add(entityFw);

			//PathwayDbID file
			File pathwayDbFile = new File("pathwayDbIDs.na");
			pathwayDbFile.createNewFile();
			FileWriter pathwayDbFw = new FileWriter(pathwayDbFile);
			BufferedWriter pathwayDbBw = new BufferedWriter(pathwayDbFw);
			pathwayDbBw.write("pathwayDbID");
			BufferedWriters.add(pathwayDbBw);
			FileWriters.add(pathwayDbFw);

			//feature file
			File featureFile = new File("feature.na");
			featureFile.createNewFile();
			FileWriter featureFw = new FileWriter(featureFile);
			BufferedWriter featureBw = new BufferedWriter(featureFw);
			featureBw.write("feature");
			BufferedWriters.add(featureBw);
			FileWriters.add(featureFw);

			//location file
			File locationFile = new File("location.na");
			locationFile.createNewFile();
			FileWriter locationFw = new FileWriter(locationFile);
			BufferedWriter locationBw = new BufferedWriter(locationFw);
			locationBw.write("location");
			BufferedWriters.add(locationBw);
			FileWriters.add(locationFw);

			//nodeID file
			File nodeFile = new File("nodeIDs.na");
			nodeFile.createNewFile();
			FileWriter nodeFw = new FileWriter(nodeFile);
			BufferedWriter nodeBw = new BufferedWriter(nodeFw);
			nodeBw.write("nodeID");
			BufferedWriters.add(nodeBw);
			FileWriters.add(nodeFw);

			//name file
			File nameFile = new File("name.na");
			nameFile.createNewFile();
			FileWriter nameFw = new FileWriter(nameFile);
			BufferedWriter nameBw = new BufferedWriter(nameFw);
			nameBw.write("name");
			BufferedWriters.add(nameBw);
			FileWriters.add(nameFw);

			//type file
			File typeFile = new File("type.na");
			typeFile.createNewFile();
			FileWriter typeFw = new FileWriter(typeFile);
			BufferedWriter typeBw = new BufferedWriter(typeFw);
			typeBw.write("type");
			BufferedWriters.add(typeBw);
			FileWriters.add(typeFw);

			Set<Node> allNodes = new HashSet<Node>();
			allNodes = graph.vertexSet();
			Hashtable<String, Integer> cytoscapeNames = new Hashtable<String, Integer>();


			for (Node node : allNodes)
			{
				String tempName = node.getName();

				if (!cytoscapeNames.containsKey(tempName))
				{
					node.cytoscapeName = tempName;
					cytoscapeNames.put(tempName, 1);
					countCas1++;
				}

				if (cytoscapeNames.containsKey(tempName))
				{
					node.cytoscapeName = tempName + Integer.toString(cytoscapeNames.get(tempName) + 1);
					cytoscapeNames.put(tempName, cytoscapeNames.get(tempName) + 1);
					countCas2++;
				}
				int i = 0;
				for (BufferedWriter writer : BufferedWriters)
				{
					if (node.attributeForCytoscape(i) != null)
					{
						writer.write("\n"+ node.cytoscapeName + " = "+ node.attributeForCytoscape(i));
						writer.flush();
						System.out.println("writing node : " + node.getCytoscapeName());
					}
					i++;
				}
			}



		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void writeEdges(DirectedPseudograph<Node,Edge> graph)
	{
		try
		{
			ArrayList<BufferedWriter> BufferedWriters = new ArrayList<BufferedWriter>();
			ArrayList<FileWriter> FileWriters = new ArrayList<FileWriter>();
			ArrayList<String> Attributes = new ArrayList<String>();
			Attributes.add("pathwaydbId");
			Attributes.add("pathwayId");
			Attributes.add("interactionId");
			Attributes.add("interactionType");
			Attributes.add("nodeA");
			Attributes.add("nodeB");


			//pathwaydb file
			File pathwaydbFile = new File("pathwaydb.ea");
			pathwaydbFile.createNewFile();
			FileWriter pathwaydbFw = new FileWriter(pathwaydbFile);
			BufferedWriter pathwaydbBw = new BufferedWriter(pathwaydbFw);
			pathwaydbBw.write("pathwaydbId");
			BufferedWriters.add(pathwaydbBw); 
			FileWriters.add(pathwaydbFw);

			//pathway file
			File pathwayFile = new File("pathway.ea");
			pathwayFile.createNewFile();
			FileWriter pathwayFw = new FileWriter(pathwayFile);
			BufferedWriter pathwayBw = new BufferedWriter(pathwayFw);
			pathwayBw.write("pathwayId");
			BufferedWriters.add(pathwayBw); 
			FileWriters.add(pathwayFw);

			//interaction file
			File interactionFile = new File("interaction.ea");
			interactionFile.createNewFile();
			FileWriter interactionFw = new FileWriter(interactionFile);
			BufferedWriter interactionBw = new BufferedWriter(interactionFw);
			interactionBw.write("interactionId");
			BufferedWriters.add(interactionBw); 
			FileWriters.add(interactionFw);

			//interactionType file
			File interactionTypeFile = new File("interactionType.ea");
			interactionTypeFile.createNewFile();
			FileWriter interactionTypeFw = new FileWriter(interactionTypeFile);
			BufferedWriter interactionTypeBw = new BufferedWriter(interactionTypeFw);
			interactionTypeBw.write("interactionType");
			BufferedWriters.add(interactionTypeBw); 
			FileWriters.add(interactionTypeFw);

			//nodeA file
			File nodeAFile = new File("nodeA.ea");
			nodeAFile.createNewFile();
			FileWriter nodeAFw = new FileWriter(nodeAFile);
			BufferedWriter nodeABw = new BufferedWriter(nodeAFw);
			nodeABw.write("nodeA");
			BufferedWriters.add(nodeABw); 
			FileWriters.add(nodeAFw);

			//nodeB file
			File nodeBFile = new File("nodeB.ea");
			nodeBFile.createNewFile();
			FileWriter nodeBFw = new FileWriter(nodeBFile);
			BufferedWriter nodeBBw = new BufferedWriter(nodeBFw);
			nodeBBw.write("nodeB");
			BufferedWriters.add(nodeBBw); 
			FileWriters.add(nodeBFw);

			Set<Edge> AllEdges = new HashSet<Edge>();
			AllEdges = graph.edgeSet();
			Hashtable<String, Integer> cytoscapeNames = new Hashtable<String, Integer>();

			for (Edge edge : AllEdges)
			{
				String tempId = edge.interactionId;
				if (!cytoscapeNames.containsKey(tempId))
				{
					edge.cytoscapeName = tempId;
					cytoscapeNames.put(tempId, 1);
				}

				if (cytoscapeNames.containsKey(tempId))
				{
					edge.cytoscapeName = tempId + Integer.toString(cytoscapeNames.get(tempId) + 1);
					cytoscapeNames.put(tempId, cytoscapeNames.get(tempId) + 1);
				}

				int i = 0;
				for (BufferedWriter writer : BufferedWriters)
				{
					if (edge.attributeForCytoscape(i) != null)
					{
						writer.write("\n"+ edge.interactionId + " = "+ edge.attributeForCytoscape(i));
						writer.flush();
						i++;
					}
				}
			}


		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void writeLinks(DirectedPseudograph<Node, Edge> graph)
	{
		try
		{
			File linksFile = new File("links.sif");
			linksFile.createNewFile();
			FileWriter linksFw = new FileWriter(linksFile);
			BufferedWriter linksBw = new BufferedWriter(linksFw);

			Set<Edge> allEdges = new HashSet<Edge>();
			allEdges = graph.edgeSet();
			for (Edge edge: allEdges)
			{
				if (edge.nodeA != null && edge.nodeB != null)
				{
					linksBw.write("\n" +  edge.nodeA.cytoscapeName + " " +  edge.cytoscapeName + " " + edge.nodeB.cytoscapeName);
					linksBw.flush();
				}
			}
			linksBw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
