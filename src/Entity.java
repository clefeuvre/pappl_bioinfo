import java.util.ArrayList;

/**
 * This class represent "real" nodes of the graph
 * (since SuperNodes and Pseudonodes do not represent anything, they are just used
 * to create interaction more easily)
 * @extends Node
 * @author Claire
 *
 */

public class Entity extends Node 
{
	
	protected String participantId;
	protected String entityId;
	protected String feature;
	protected String location;
	protected ComplexNode complex; //ComplexNode the entity belongs to, if it is part of  complex
	protected ArrayList<SuperNode> supernodes = new ArrayList<SuperNode>(); //list of superNodes the entity belongs to
	
	public Entity(){}
	
	public Entity(String entId, String nId, int patId, ComplexNode n){
		entityId=entId;
		pathwaydbId=patId;
		complex=n;
		nodeId=nId;
	}
	
	public Entity(String pId, String entId, String nId, int patId){
		participantId=pId;
		entityId=entId;
		nodeId=nId;
		pathwaydbId=patId;
	}
	
	public Entity(String pId, String entId, String nId, int patId, ComplexNode c){
		participantId=pId;
		entityId=entId;
		nodeId=nId;
		pathwaydbId=patId;
		complex=c;
	}
	
	public Entity(String pId, String entId, String nId, int patId, String nam, String typ){
		participantId=pId;
		entityId=entId;
		nodeId=nId;
		pathwaydbId=patId;
		name=nam;
		type=typ;
	}
	
	public String attributeForCytoscape(int i)
	{
		String result = "";
		switch (i)
		{
		case 0: result = this.participantId;
		break;

		case 1: result = this.entityId;
		break;
		
		case 2: result = Integer.toString(this.pathwaydbId);
		break;
		
		case 3: result = this.feature;
		break;
		
		case 4: result = this.location;
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


	/**
	 * @return the complex
	 */
	public ComplexNode getComplex() {
		return complex;
	}

	/**
	 * @param complex the complex to set
	 */
	public void setComplex(ComplexNode complex) {
		this.complex = complex;
	}
	
	

	/**
	 * @return the participantId
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the feature
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * @param feature the feature to set
	 */
	public void setFeature(String feature) {
		this.feature = feature;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the supernodes
	 */
	public ArrayList<SuperNode> getSupernodes() {
		return supernodes;
	}

	/**
	 * @param supernodes the supernodes to set
	 */
	public void setSupernodes(ArrayList<SuperNode> supernodes) {
		this.supernodes = supernodes;
	}
	
	public void addSuperNode(SuperNode s){
		this.supernodes.add(s);
	}

	/*
	 * We use this function to know if we have to add an entity in the graph
	 * 2 entities are considered similar if they have the same entityId, pathwayDbId, location and feature
	 */
	public boolean similar(Entity obj) {
        if (obj==this) {
            return true;
        }
         
        else {
            Entity other = obj;
            
            if(this.pathwaydbId != other.pathwaydbId){
            	return false;
            }

            if (entityId == null) {
            	if (other.entityId != null)
            		return false;
            } else if (!entityId.equals(other.entityId))
            	return false;
            
            if (feature == null) {
            	if (other.feature != null)
            		return false;
            } else if (!feature.equals(other.feature))
            	return false;
            
            
            if (location == null) {
            	if (other.location != null)
            		return false;
            } else if (!location.equals(other.location))
            	return false;
            
            
            
            return true;
        }
	}

	
}
