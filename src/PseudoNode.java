/*
 * This class is used only because we sometimes need to create an edge
 * landing on another edge. Since we cannot do this with a graph, we create a
 * PseudoNode at the intersection of the 2 edges (and thus 3 edges : 2 arriving to 
 * the node and another leaving). These nodes do not represent
 * anything, they are just here for construction purposes. Their nodeId is the
 * interactionId of the edges.
 */


public class PseudoNode extends Node {
	
	private static int instanceCount = 0; //number of instances created, used to give a different name to each PseudoNode
	protected Node controller; //controller of the reaction
	protected Node nodeA; //nodeA of the reaction
	protected Node nodeB; //nodeB of the reaction
	
	
	public PseudoNode(String id, int patid,Node contr, Node nA, Node nB){
		nodeId=id;
		pathwaydbId= patid;
		controller = contr;
		nodeA=nA;
		nodeB=nB;
		instanceCount ++;
		name = "PseudoNode_"+instanceCount;
	}

	public PseudoNode() {
		instanceCount ++;
		name = "PseudoNode_"+instanceCount;
	}
	
	
	
	/**
	 * @return the controller
	 */
	public Node getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Node controller) {
		this.controller = controller;
	}

	/**
	 * @return the nodeA
	 */
	public Node getNodeA() {
		return nodeA;
	}

	/**
	 * @param nodeA the nodeA to set
	 */
	public void setNodeA(Node nodeA) {
		this.nodeA = nodeA;
	}

	/**
	 * @return the nodeB
	 */
	public Node getNodeB() {
		return nodeB;
	}

	/**
	 * @param nodeB the nodeB to set
	 */
	public void setNodeB(Node nodeB) {
		this.nodeB = nodeB;
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
