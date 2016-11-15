/*
 * This file is part of the MLDA.
 *
 * (c)  Jose Maria Moyano Murillo
 *      Eva Lucrecia Gibaja Galindo
 *      Sebastian Ventura Soto <sventura@uco.es>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package mlda.imbalance;

import mlda.base.MLDataMetric;
import mlda.util.ImbalancedFeature;
import mlda.util.Utils;
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
