package mldc.labelsRelation;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Average examples per labelset
*
* @author Jose Maria Moyano Murillo
*/
public class AvgExamplesPerLabelset extends MLDataMetric{

	/**
	 * Constructor
	 */
	public AvgExamplesPerLabelset() {
		super("Average examples per labelset");
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
		
		this.value = ((double)mlData.getNumInstances()) / stat.labelSets().size();
		return value;
	}

}
