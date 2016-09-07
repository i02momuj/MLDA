package mldc.labelsRelation;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Proportion of distinct labelsets
*
* @author Jose Maria Moyano Murillo
*/
public class ProportionDistinctLabelsets extends MLDataMetric{

	/**
	 * Constructor
	 */
	public ProportionDistinctLabelsets() {
		super("Proportion of distinct labelsets");
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
		
		this.value = ((double)stat.labelCombCount().size()) / mlData.getNumInstances();
		return value;
	}

}
