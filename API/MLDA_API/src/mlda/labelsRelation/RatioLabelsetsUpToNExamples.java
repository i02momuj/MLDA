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

package mlda.labelsRelation;

import mlda.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Ratio of number of labelsets up to N examples
*
* @author Jose Maria Moyano Murillo
*/
public class RatioLabelsetsUpToNExamples extends MLDataMetric{

	protected int n = 0;
	
	/**
	 * Constructor
	 * 
	 * @param n Number of examples
	 */
	public RatioLabelsetsUpToNExamples(int n) {
		super("Ratio of number of labelsets up to " + n + " examples");
		this.n = n;
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
		
		LabelsetsUpToNExamples upToN = new LabelsetsUpToNExamples(n);
		double nUpToN = upToN.calculate(mlData);
		
		this.value = nUpToN / stat.labelCombCount().values().size();
		return value;
	}

}
