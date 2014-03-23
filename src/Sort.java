import java.util.ArrayList;

/**
 * This class is used by the PHWriting class to create the Sorts
 * of the PH formalism.
 * @author Claire
 *
 */

public class Sort {
	protected String name; //name of the sort
	protected ArrayList<Process> process; //list of the processes contained in the sort
	
	public Sort(String n){
		process = new ArrayList<Process>();
		n=n.replace(" ", "_");
		n=n.replace("/", "_");
		name = n;
		this.addProcess("0");
		this.addProcess("1");
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

	/**
	 * @return the process
	 */
	public ArrayList<Process> getProcesses() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcesses(ArrayList<Process> process) {
		this.process = process;
	}

	/**
	 * Add a process to the Sort
	 * @param name : name of the process to add
	 */
	private void addProcess(String name){
		Process p = new Process(this, name);
		process.add(p);
	}
	
	/**
	 * @return the number of processes in the Sort
	 */
	public int getSize(){
		return process.size();
	}
	
	/**
	 * Get the index of a process in the arraylist
	 * @param p : th process to find
	 * @return index of a process in the arraylist processes
	 */
	public int indexOf(Process p){
		return process.indexOf(p);
	}
	
	/**
	 * Get a process by its index in the arraylist processes
	 * @param i : the index of the process
	 * @return the process to get
	 */
	public Process getProcess(int i){
		return process.get(i);
	}
}
