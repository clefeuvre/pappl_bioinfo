import java.util.ArrayList;

/**
 * This class represents superNodes : nodes that are used to represent the gathering of several elements
 * They are different from ComplexNodes because they do not physycally gather all their components,
 * theyr are just here to make interactions start or arrive from/to several nodes at the same time.
 * subnodes of a SuperNode can be Entities (including complexes and specialnodes), but not supernodes or pseudonodes.
 * A Node can belong to several SuperNodes at the same time.
 * @author Claire
 *
 */

public class SuperNode extends Node{
	
	private static int instanceCount = 0; //number of instance created (used to give a different name to each supernode)
	protected ArrayList<Node> sub_nodes=new ArrayList<Node>(); //list of the element in the supernode
	
	public SuperNode(){
		type="SuperNode";
		instanceCount ++;
		name = "SuperNode_"+instanceCount;
	}
	
	public SuperNode(String supernodeId, int patid){
		nodeId=supernodeId;
		pathwaydbId=patid;
		type="SuperNode";
		name = "SuperNode_"+instanceCount;
		instanceCount++;
	}
	
	/**
	 * Add a subnode to the list
	 * @param n : the Node to add
	 */
	public void addSubNode(Node n){
		sub_nodes.add(n);
	}
	
	/**
	 * @return the subnodes list
	 */
	public ArrayList<Node> getSubNodes(){
		return sub_nodes;
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
