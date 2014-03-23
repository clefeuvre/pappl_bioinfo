import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

/**
 * This class is used to translate a graph to PH formalism
 * and write the PH file.
 * @author Claire
 *
 */

public class PHWriting {
	
	protected HashMap<String,Sort> sorts;
	protected ArrayList<Action> actions;
	protected ArrayList<Cooperativity> cooperativities;
	
	public PHWriting(){
		sorts=new HashMap<String,Sort>();
		actions = new ArrayList<Action>();
		cooperativities = new ArrayList<Cooperativity>();
	}
	
	/**
	 * From a name, creates the sort if it does not exist in the sorts attribute (and add it to "sorts")
	 * or return the existing sort with this name
	 * @param name : the name of the sort to get
	 * @return the sort with the researched name
	 */
	public Sort createSortIfNotPresent(String name){
		Sort s=null;
		if(!sorts.containsKey(name)){
			s = new Sort(name);
			sorts.put(s.getName(),s);
		}
		else{
			s = sorts.get(name);
		}
		return s;
	}
	
	/**
	 * Find and translate all simple transformations in the graph
	 * (interaction with no supernode involved)
	 * @param graph : the graph to analyse
	 */
	public void findTransformations(DirectedPseudograph<Node, Edge> graph){		
		Set<Node> allNodes = new HashSet<Node>();
		allNodes = graph.vertexSet();
		Set<Edge> allEdges = new HashSet<Edge>();
		allEdges = graph.edgeSet();
		for(Node node : allNodes){
			//transformations with a controller : use an edge landing on an edge, so there is a PseudoNode involved
			if(node instanceof PseudoNode){
				Set<Edge> edges = new HashSet<Edge>();
				edges = graph.edgesOf(node); //by construction of PseudoNode, all its edges have the same attributes
				boolean done=false;
				Node nodeA=((PseudoNode) node).getNodeA();
				Node nodeB=((PseudoNode) node).getNodeB();
				for(Edge edge : edges){
					Node controller = ((PseudoNode) node).getController();
					if(nodeA instanceof Entity && nodeB instanceof Entity && !done){
						createTransformation((Entity)nodeA, (Entity)nodeB, controller);
						System.out.println("transformation found");
						done = true;
						edge.setTranslated(true);
					}
					else if(done){
						edge.setTranslated(true);
					}
				}
			}
		}
		//transformations without controller : simple arrows A->B
		for(Edge e : allEdges){
			if(e.getNodeA() instanceof Entity && e.getNodeB() instanceof Entity && !e.isTranslated() && !e.getNodeA().equals(e.getNodeB())){
				Sort a=null;
				Sort b=null;
				if(e.getNodeA().getName().equals(e.getNodeB().getName())){
					if(((Entity) e.getNodeA()).getLocation()!=null && ((Entity)e.getNodeB()).getLocation()!=null){
					if(!((Entity) e.getNodeA()).getLocation().equals(((Entity) e.getNodeB()).getLocation())){
						a=createSortIfNotPresent(e.getNodeA().getName()+((Entity) e.getNodeA()).getLocation());
						b=createSortIfNotPresent(e.getNodeA().getName()+((Entity) e.getNodeB()).getLocation());
					}
					else{
						a=createSortIfNotPresent(e.getNodeA().getName()+((Entity) e.getNodeA()).getFeature());
						b=createSortIfNotPresent(e.getNodeA().getName()+((Entity) e.getNodeB()).getFeature());
					}
					}
					else{ //some interactions in base 1 have nodeA and nodeB with exactly the same name and no feature nor location
						a=createSortIfNotPresent(e.getNodeA().getName());
						b=createSortIfNotPresent(e.getNodeB().getName());
					}
				}
				else{
					a=createSortIfNotPresent(e.getNodeA().getName());
					b=createSortIfNotPresent(e.getNodeB().getName());
				}

				Action ac = new Action(a.getProcess(1),b.getProcess(0),b.getProcess(1));
				actions.add(ac);
				e.setTranslated(true);
			}
		}
	}
	
	/**
	 * create sorts and cooperativity for tranformation with controller
	 * in the form : COOPERATIVITY([A,C] -> A+ 0 1,[[1;1]])
	 * @param nodeA
	 * @param nodeB
	 * @param controller
	 */
	public void createTransformation(Entity nodeA, Entity nodeB, Node controller){
		//construct activated/inhibited sort
		Sort a = null;
		Sort b = null;
		if(nodeA.getName().equals(nodeB.getName())){
			if(nodeA.getFeature()!=null && nodeB.getFeature()!=null && nodeA.getLocation()!=null && nodeB.getLocation()!=null){
				if(!nodeA.getFeature().equals(nodeB.getFeature())){
					a = createSortIfNotPresent(nodeA.getName()+"_"+nodeA.getFeature());
					b = createSortIfNotPresent(nodeB.getName()+"_"+nodeB.getFeature());
				}
				else{
					a = createSortIfNotPresent(nodeA.getName()+"_"+nodeA.getLocation());
					b = createSortIfNotPresent(nodeB.getName()+"_"+nodeB.getLocation());
				}
			}
			else{//some interactions in base 1 have nodeA and nodeB with exactly the same name and no feature nor location
				a = createSortIfNotPresent(nodeA.getName());
				b = createSortIfNotPresent(nodeB.getName());
			}
		}
		else{
			a = createSortIfNotPresent(nodeA.getName());
			b = createSortIfNotPresent(nodeB.getName());
		}
		//find or create controller Sort
		Sort control=createSortIfNotPresent(controller.getName());
		//Create and add cooperativity
		ArrayList<Process> sources=new ArrayList<Process>();
		sources.add(control.getProcess(1));
		sources.add(a.getProcess(1));
		Cooperativity coop = new Cooperativity(sources,b.getProcess(0),b.getProcess(1));
		cooperativities.add(coop);
	}
	
	/**
	 * Finds the complex formations (with supernodes) in the graph and creates the appropriates Sorts, Process and Cooperativites
	 * @param graph
	 */
	public void findComplexFormations(DirectedPseudograph<Node, Edge> graph){
		Set<Edge> allEdges = new HashSet<Edge>();
		allEdges = graph.edgeSet();
		Set<Node> allNodes = new HashSet<Node>();
		allNodes = graph.vertexSet();
		//first : complexation with controller : they have a Pseudonode at the center (A and B become AB if C present)
		for(Node n : allNodes){
			if(n instanceof PseudoNode){
				Set<Edge> edges = graph.edgesOf(n);
				boolean done = false; //boolean used to do the translation only once, because the set contains 3 edges with the same attributes
				for(Edge e : edges){
						if(!done){
							//complexation : several elements in a supernode give 1 complex
							if(((PseudoNode) n).getNodeA() instanceof SuperNode && ((PseudoNode) n).getNodeB() instanceof ComplexNode){
								ArrayList<Node> sources;
								SuperNode s = (SuperNode) ((PseudoNode)n).getNodeA();
								sources = s.getSubNodes();
								Node controller = ((PseudoNode)n).getController();
								ComplexNode complex = (ComplexNode) ((PseudoNode)n).getNodeB();
								createComplexFormationWithController(sources, complex, controller);
							}
							//de-complexation : 1 complex gives several elements, gathered in a supernode
							else if(((PseudoNode) n).getNodeA() instanceof ComplexNode && ((PseudoNode) n).getNodeB() instanceof SuperNode){
								ComplexNode complex = (ComplexNode)((PseudoNode) n).getNodeA();
								SuperNode s = (SuperNode)((PseudoNode) n).getNodeB();
								ArrayList<Node> results=s.getSubNodes();
								Node controller = ((PseudoNode) n).getController();
								createDecomplexationWithController(complex,results,controller);
							}
							//other complexation : 1 complex + 1/several other things give complex + other things
							else if(((PseudoNode) n).getNodeA() instanceof SuperNode && ((PseudoNode) n).getNodeB() instanceof SuperNode){
								ArrayList<Node> sources = ((SuperNode)((PseudoNode) n).getNodeA()).getSubNodes();
								ArrayList<Node> results = ((SuperNode)((PseudoNode) n).getNodeB()).getSubNodes();
								Node controller = ((PseudoNode) n).getController();
								createOtherComplexationWithController(sources,results,controller);
							}
						}
					e.setTranslated(true);
					done = true;
				}
			}
		}
		//then simple complexations, without controller
		for(Edge edge : allEdges){
			//complexation (Supernode -> complexNode)
			if(graph.getEdgeTarget(edge) instanceof ComplexNode && graph.getEdgeSource(edge) instanceof SuperNode){
				ComplexNode complex = (ComplexNode)graph.getEdgeTarget(edge);
				ArrayList<Node> subnodes = ((SuperNode)graph.getEdgeSource(edge)).getSubNodes();
				createComplexFormation(subnodes, complex);
				edge.setTranslated(true);
			}
			//de-complexation : Complex -> supernode
			else if(graph.getEdgeSource(edge) instanceof ComplexNode && graph.getEdgeTarget(edge) instanceof SuperNode){
				ComplexNode complex = (ComplexNode) graph.getEdgeSource(edge);
				ArrayList<Node> results = ((SuperNode)graph.getEdgeTarget(edge)).getSubNodes();
				createDecomplexation(complex, results);
			}
			//other complexation relation (superNode->SuperNode with complex involved)
			else if(graph.getEdgeSource(edge) instanceof SuperNode && graph.getEdgeTarget(edge) instanceof SuperNode){
				ArrayList<Node> sources = ((SuperNode)graph.getEdgeSource(edge)).getSubNodes();
				ArrayList<Node> results = ((SuperNode)graph.getEdgeTarget(edge)).getSubNodes();
				createOtherComplexation(sources, results);
			}
		}
	}
	
	/**
	 * Creates the sorts and processes for a complex formation of the form :
	 * Cooperativity([A,B,C...] -> (A+B+C+...-complex) 0 1, [1,1,1,...]
	 * @param sources : arraylist of nodes initiating the reaction
	 * @param c : complexNode result of the reaction
	 */
	public void createComplexFormation(ArrayList<Node> sources, ComplexNode c){
		ArrayList<Process> sources_processes=new ArrayList<Process>();
		for(Node n : sources){
			Sort s=createSortIfNotPresent(n.getName());
			sources_processes.add(s.getProcess(1));
		}
		Sort complex = createSortIfNotPresent(c.getName());
		Cooperativity coop = new Cooperativity(sources_processes,complex.getProcess(0),complex.getProcess(1));
		cooperativities.add(coop);
	}
	
	/**
	 * Creates the sorts and processes for a complex formation of the form :
	 * Cooperativity([A,B,C...+D] -> (A+B+C+...-complex) 0 1, [1,1,1,...]
	 * @param sources : arraylist of nodes at the begining of the reaction
	 * @param c : complexNode result of the reaction
	 * @param controller : controller of the reaction
	 */
	public void createComplexFormationWithController(ArrayList<Node> sources, ComplexNode c, Node controller){
		sources.add(controller);
		createComplexFormation(sources, c);
	}
	
	/**
	 * creates sorts, process and actions for a decomplexation (complex -> supernode containing sub-elements)
	 * Complex 1 -> element 0 1; Complex 1 -> element2 0 1 ....
	 * @param comp : ComplexNode at the beginning of reaction
	 * @param result : arraylist of Nodes : result of the reaction
	 */
	public void createDecomplexation(ComplexNode comp, ArrayList<Node> result){
		//creating sort and processes for the complex
		Sort complex = createSortIfNotPresent(comp.getName());
		//creating Sort and processes and actions for elements created by dissociation
		for(Node n : result){
			Sort s = createSortIfNotPresent(n.getName());
			
			Action action = new Action(complex.getProcess(1),s.getProcess(0),s.getProcess(1));
			actions.add(action);
		}
	}
	
	/**
	 * Creates sorts, process and cooperativities for decomplexation with a controller 
	 * (complex -> several elements if controller is present)
	 * COOPERATVITY([complex,controller]->element[i] 0 1, [1,1])
	 * @param comp : ComplexNode at the beginning of the reaction
	 * @param result : arraylist of Node, results of the reaction
	 * @param controller : controller of the reaction
	 */
	public void createDecomplexationWithController(ComplexNode comp, ArrayList<Node> result, Node controller){
		//creating sort and processes for the complex
		Sort complex = createSortIfNotPresent(comp.getName());
		//create sort and processes for controller
		Sort control = createSortIfNotPresent(controller.getName());
		
		ArrayList<Process> source=new ArrayList<Process>();
		source.add(complex.getProcess(1));
		source.add(control.getProcess(1));
		//creating Sort and processes and cooperativities for elements created by dissociation
		for(Node n : result){
			Sort s = createSortIfNotPresent(n.getName());
			
			Cooperativity coop = new Cooperativity(source, s.getProcess(0), s.getProcess(1));
			cooperativities.add(coop);
		}
	}
	/**
	 * Creates Sorts, Process and cooperativities for reactions with 2 supernodes 
	 * @param sources list of source nodes (sub-elements of the Supernode at the beginning of reaction)
	 * @param results list of results nodes (sub-elements of the Supernode at the end of reaction)
	 */
	public void createOtherComplexation(ArrayList<Node> sources, ArrayList<Node> results){
		ArrayList<Process> source_process = new ArrayList<Process>();
		for(Node n : sources){
			Sort s = createSortIfNotPresent(n.getName());
			source_process.add(s.getProcess(1));
		}
		for(Node n : results){
			Sort s = createSortIfNotPresent(n.getName());

			Cooperativity c = new Cooperativity(source_process, s.getProcess(0), s.getProcess(1));
			cooperativities.add(c);
		}		
	}
	
	/**
	 * Creates Sorts, Process and cooperativities for reactions with 2 supernodes and a controller
	 * @param sources list of source nodes (sub-elements of the Supernode at the beginning of reaction)
	 * @param results list of results nodes (sub-elements of the Supernode at the end of reaction)
	 * @param controller : controller of the reaction
	 */
	public void createOtherComplexationWithController(ArrayList<Node> sources, ArrayList<Node> results, Node controller){
		sources.add(controller);
		createOtherComplexation(sources, results);
	}
	
	/**
	 * Write the PH file with all Sorts, Actions and Cooperativities in the sorts, actions and cooperativities attributes
	 * in the file given as parameter
	 * @param fileName : name of the file to write
	 */
	public void writePHFile(String fileName){
		try{
	      BufferedWriter fichier = new BufferedWriter(new FileWriter(fileName));
	      //Write sorts
	      for(String sortName : sorts.keySet()){
	    	  int taille = sorts.get(sortName).getSize()-1;
	    	  fichier.write("process "+sortName+" "+taille);
	    	  fichier.newLine();
	      }
	      fichier.newLine();
	      //Write actions
	      for(Action a : actions){
	    	  fichier.write(a.getSource().getSort().getName()+" "+a.getSource().getSort().indexOf(a.getSource())+" -> "+a.getTarget().getSort().getName()+" "+a.getTarget().getSort().indexOf(a.getTarget())+" "+a.getResult().getSort().indexOf(a.getResult()));
	    	  fichier.newLine();
	      }
	      fichier.newLine();
	      //Write Cooperativities
	      for(Cooperativity c : cooperativities){
	    	 String coop = "[";
	    	 String states = "[";
	    	 for(Process p : c.getSources()){
	    		 coop += p.getSort().getName();
	    		 coop += ";";
	    		 states += "1;";
	    	 }
	    	 coop=coop.substring(0, coop.length()-1);
	    	 coop += "]";
	    	 states = states.substring(0, states.length()-1);
	    	 states += "]";	    	 
	    	 fichier.write("COOPERATIVITY ("+coop+" -> "+c.getResult().getSort().getName()+" "+c.getTarget().getSort().indexOf(c.getTarget())+" "+c.getResult().getSort().indexOf(c.getResult())+", ["+states+"])");
	    	 fichier.newLine();
	      }
	      
	      fichier.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}


