import java.util.HashSet;

/**
 * This class is the mother class for all nodes in the graph.
 * @author Claire
 *
 */

public class Node {
	
	protected int pathwaydbId; //n° of the database 
	protected HashSet<String> pathways = new HashSet<String>(); //pathways the node belongs to
	
	protected String nodeId; //id of the node (superNodeId in the DB)
	protected String name; 
	protected String type;
	protected String cytoscapeName;

	public Node(String nId, int patId){
		nodeId= nId;
		pathwaydbId=patId;
	}
	
	
 

	public Node(){
	}
	/**
	 * @return the nodeID
	 */
	public String getNodeID() {
		return nodeId;
	}

	/**
	 * @param nodeID the nodeID to set
	 */
	public void setNodeID(String nodeID) {
		this.nodeId = nodeID;
	}

	/**
	 * @return the pathwaydbId
	 */
	public int getPathwaydbId() {
		return pathwaydbId;
	}

	/**
	 * @param pathwaydbId the pathwaydbId to set
	 */
	public void setPathwaydbId(int pathwaydbId) {
		this.pathwaydbId = pathwaydbId;
	}
	
	


	/**
	 * @return the pathways
	 */
	public HashSet<String> getPathways() {
		return pathways;
	}

	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(HashSet<String> pathways) {
		this.pathways = pathways;
	}
	
	/**
	 * @param pathway the pathway to add to the list
	 */
	public void addPathway(String pathway){
		pathways.add(pathway);
	}
	
	public void addSeveralPathways(HashSet<String> pathways){
		pathways.addAll(pathways);
	}




	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCytoscapeName(){
		return cytoscapeName;
	}
	
	public void setCytoscapeName(String inputCytoscapeName)
	{
		this.cytoscapeName = inputCytoscapeName;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String attributeForCytoscape(int i)
	{
		String result = "";
		switch (i)
		{
		case 0: result = null;
		break;

		case 1: result = null;
		break;
		
		case 2: result = Integer.toString(this.pathwaydbId);
		break;
		
		case 3: result = null;
		break;
		
		case 4: result = null;
		break;
		
		case 5: result = this.nodeId;
		break;
		
		case 6: result = this.name;
		break;
		
		case 7: result = this.type;
		break;
		}
		
		return result;
		
	}

}
