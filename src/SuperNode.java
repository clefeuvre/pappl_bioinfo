import java.util.ArrayList;


public class SuperNode extends Node{
	
	private static int instanceCount = 0;
	protected ArrayList<Node> sub_nodes=new ArrayList<Node>();
	
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
	
	public void addSubNode(Node n){
		sub_nodes.add(n);
	}
	
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
