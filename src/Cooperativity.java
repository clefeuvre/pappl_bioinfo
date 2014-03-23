import java.util.ArrayList;

/**
 * This class is used by the PHWriting class to create Cooperativities between processes
 * @author Claire
 *
 */

public class Cooperativity {
	protected ArrayList<Process> sources;
	protected Process target;
	protected Process result;
	
	public Cooperativity(ArrayList<Process> s, Process t, Process r){
		sources = s;
		target = t;
		result = r;
	}

	/**
	 * @return the sources
	 */
	public ArrayList<Process> getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(ArrayList<Process> sources) {
		this.sources = sources;
	}

	/**
	 * @return the target
	 */
	public Process getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Process target) {
		this.target = target;
	}

	/**
	 * @return the result
	 */
	public Process getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Process result) {
		this.result = result;
	}
	
	
}
