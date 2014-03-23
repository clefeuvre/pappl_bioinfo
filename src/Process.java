
/**
 * This class is used by the Sort and PHWriting classes
 * to create Process contained in Sorts for the translation of the
 * graph to PH file
 * @author Claire
 *
 */
public class Process {
	protected String name;
	protected Sort sort;
	
	public Process(Sort s, String n){
		name = n;
		sort = s;
	}

	/**
	 * @return the name of the process
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
	 * @return the sort
	 */
	public Sort getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
	
}
