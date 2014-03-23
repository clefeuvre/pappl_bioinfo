
/**
 * This class represent a special kind of Entity : nodes that do not represent
 * a molecule or a gene but en event. In the database, they are in the abstract_node
 * table but not the entity_particpant one.
 * They do not have feature or location, but they can appear in relations.
 * @extends Entity
 * @author Claire
 *
 */

public class SpecialNode extends Entity {
	
	public SpecialNode(String pId, String nId, int patId) {
		participantId = pId;
		nodeId= nId;
		pathwaydbId=patId;
	}

	public SpecialNode(String pId, String nId, int patId, String nam, String typ) {
		participantId = pId;
		nodeId= nId;
		pathwaydbId=patId;
		name = nam;
		type = typ;
	}

	public SpecialNode() {
	}

}
