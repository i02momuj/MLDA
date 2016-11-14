package mldc.labelsDistribution;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Cardinality
*
* @author Jose Maria Moyano Murillo
*/
public class Cardinality extends MLDataMetric{

	/**
	 * Constructor
	 */
	public Cardinality() {
		super("Cardinality");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		Statistics stat = new Statistics();
		stat.calculateStats(mlData);
		
		this.value = stat.cardinality();
		return value;
	}

}
