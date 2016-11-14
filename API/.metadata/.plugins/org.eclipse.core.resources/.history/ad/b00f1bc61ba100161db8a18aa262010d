package mldc.base;

import mulan.data.MultiLabelInstances;

/**
* Class implementing a metric for multi-label data characterization
*
* @author Jose Maria Moyano Murillo
*/
public class MLDataMetric implements Comparable<MLDataMetric>{

	/**
	 * Metric name
	 */
	protected String name;
	
	/**
	 * Metric value
	 */
	protected double value;

	
	/**
	 * Constructor
	 * 
	 * @param name Name of the metric
	 */
	public MLDataMetric(String name) {
		this.name = name;
		this.value = Double.NaN;
	}
	
	
	/**
	 * Get metric name
	 * 
	 * @return Name of the metric
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get metric value
	 * 
	 * @return Value of the metric
	 */
	public double getValue(){
		return value;
	}
	
	/**
	 * To String method
	 * 
	 * @return MLDataMetric as String, including name and value
	 */
	public String toString(){
		String s = new String();
		
		s += name + ": " + value;
		
		return s;
	}
	
	
	
	/**
	 * Calculate metric value.
	 * To be implemented in each metric
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Calculated value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		return Double.NaN;
	}
	
	
	@Override
	/**
	 * Compares MLDataMetric objects by name
	 * 
	 * @param other MLDataMetric object
	 * @return Comparison by name between them.
	 */
	public int compareTo(MLDataMetric other) {
		return this.name.compareTo(other.getName());
	}

}
