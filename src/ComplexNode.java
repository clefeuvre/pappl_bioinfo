import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ComplexNode extends Node {
	
	protected ArrayList<Entity> sub_entities=new ArrayList<Entity>();

	public ComplexNode(String pId, String entId, String nId, int patId) {
		super(pId, entId, nId, patId);
	}
	
	public ComplexNode(String pId, String nId, int patId) {
		participantId = pId;
		nodeId= nId;
		pathwaydbId=patId;
	}

	public ComplexNode(String pId, String entId, String nId, int patId,	String nam, String typ) {
		super(pId, entId, nId, patId, nam, typ);
	}
	
	public ComplexNode(String pId, String nId, int patId,	String nam, String typ) {
		participantId = pId;
		nodeId= nId;
		pathwaydbId=patId;
		name = nam;
		type = typ;
	}

	public ComplexNode() {
	}
	
	/*
	 * Creates the Entity corresponding to the sub_entities of this Complex,
	 * and returns them as a list (and updates the sub_entities member).
	 * The complex has to create the sub_entities because they are not in the result
	 * of the SQL request interrogating only the abstract_node and entity_particpant
	 * tables.
	 * 
	 * @param con Connection object to the database to allow to make requests
	 * @return sub_entities An ArrayList of the subentities
	 * 
	 */
	public ArrayList<Entity> createSubEntities(Connection con) throws SQLException{
		if(sub_entities.isEmpty()){
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			//getting all the subentities associated to this complex
			if(pathwaydbId==1){
				 rs = stmt.executeQuery("SELECT * FROM reformatted_sub_entity WHERE entityId='"+participantId+"';");
			}
			if(pathwaydbId==2 || pathwaydbId==3){
				String requete = "SELECT * FROM reformatted_sub_entity WHERE entityId='"+entityId+"';";
				rs = stmt.executeQuery(requete);
				System.out.println(requete);
			}
			
			while(rs.next()){
				//for each subentity, create an Entity, and get the information about it
				Entity n = new Entity(rs.getString("subcomponentId"),rs.getInt("pathwaydbId"),this);
				String id=rs.getString("subcomponentId");
				if(pathwaydbId==2 || pathwaydbId==3){ //for DB2 and 3, the subcomponentId has to be modified to be used for a query in the entity_information table				
					id=id.replace("x", "m");
					int index = id.indexOf('_', 7);
					if(index>0){
						id = id.substring(0, index);
					}
				}
				Statement stmt2 = con.createStatement();
				ResultSet r = stmt2.executeQuery("SELECT * FROM reformatted_entity_information WHERE entityId='"+id+"';");
				System.out.println("subcomponentId : "+n.entityId);
				if(r.next()){
				n.setType(r.getString("entityType"));
				n.setName(r.getString("entityName"));
				}
				sub_entities.add(n);
				System.out.println("sub_entity "+n.getEntityId()+" added.");
				stmt2.close();
			}
			stmt.close();
		}
		return sub_entities;
	}

	/**
	 * @return the sub_entities
	 */
	public ArrayList<Entity> getSub_entities() {
		return sub_entities;
	}

	/**
	 * @param sub_entities the sub_entities to set
	 */
	public void setSub_entities(ArrayList<Entity> sub_entities) {
		this.sub_entities = sub_entities;
	}
	
	public void addSub_entity(Entity e){
		sub_entities.add(e);
	}

}