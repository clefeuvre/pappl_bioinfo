import java.util.ArrayList;


public class Sort {
	protected String name;
	protected ArrayList<Process> process;
	
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

	
	private void addProcess(String name){
		Process p = new Process(this, name);
		process.add(p);
	}
	
	
	public int getSize(){
		return process.size();
	}
	
	public int indexOf(Process p){
		return process.indexOf(p);
	}
	
	public Process getProcess(int i){
		return process.get(i);
	}
}
