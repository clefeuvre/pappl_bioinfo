public class Edge implements java.io.Serializable
{
	protected int pathwaydbId;
	protected String pathwayId;
	protected String interactionId;
	protected String interactionType;
	protected String controlType;
	protected Node nodeA;
	protected Node nodeB;
	protected String cytoscapeName;
	
	
	public Edge(int dbId, String patId, String intId, String interType, String control, Node na, Node nb)
	{
		pathwaydbId=dbId;
		pathwayId=patId;
		nodeA=na;
		nodeB=nb;
		interactionId=intId;
		interactionType=interType;
		controlType = control;
	}
	
	/**
	 * @return the interactionId
	 */
	public String getInteractionId() {
		return interactionId;
	}

	/**
	 * @param interactionId the interactionId to set
	 */
	public void setInteractionId(String interactionId) {
		this.interactionId = interactionId;
	}

	/**
	 * @return the interactionType
	 */
	public String getInteractionType() {
		return interactionType;
	}

	/**
	 * @param interactionType the interactionType to set
	 */
	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
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
	 * @return the pathwayId
	 */
	public String getPathwayId() {
		return pathwayId;
	}
	/**
	 * @param pathwayId the pathwayId to set
	 */
	public void setPathwayId(String pathwayId) {
		this.pathwayId = pathwayId;
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
	
	
	
	/**
	 * @return the controlType
	 */
	public String getControlType() {
		return controlType;
	}

	/**
	 * @param controlType the controlType to set
	 */
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String attributeForCytoscape(int i)
	{
		String result = "";
		switch(i)
		{
		case 0: result = Integer.toString(this.pathwaydbId);
		break;
		
		case 1: result = this.pathwayId;
		break;
		
		case 2: result = this.interactionId;
		break;
		
		case 3: result = this.interactionType;
		break;
		
		
		// ici changer les nodeA.name et nodeB.name une fois taxonomies correctes adoptées
		case 4: result = this.nodeA.name;
		break;
		
		case 5: result = this.nodeB.name;
		break;
		}
		
		return result;
	}
}
