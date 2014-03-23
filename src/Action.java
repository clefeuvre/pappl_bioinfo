
/**
 * This class is used by the PHWriting class to create
 * actions between processes
 * @author Claire
 *
 */

public class Action {
	protected Process source;
	protected Process target;
	protected Process result;
	
	Action(Process s, Process t, Process r){
		source=s;
		target=t;
		result=r;
	}

	/**
	 * @return the source process
	 */
	public Process getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Process source) {
		this.source = source;
	}

	/**
	 * @return the target process
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
	 * @return the result process
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
