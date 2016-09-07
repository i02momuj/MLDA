package mldc.imbalance;

import mldc.base.MLDataMetric;
import mldc.util.ImbalancedFeature;
import mldc.util.Utils;
import mulan.data.MultiLabelInstances;

/**
* Class for all Imbalance Metrics including characteristics for imbalanced data
*
* @author Jose Maria Moyano Murillo
*/
public class ImbalanceDataMetric extends MLDataMetric{

	protected ImbalancedFeature [] imbalancedData;
	
	/**
	 * Constructor
	 * 
	 * @param name Metric name
	 */
	public ImbalanceDataMetric(String name) {
		super(name);
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		imbalancedData =  Utils.getImbalancedWithIR(mlData, Utils.getSortedByFrequency(Utils.getAppearancesPerLabel(mlData)));
		
		return Double.NaN;
	}

}
