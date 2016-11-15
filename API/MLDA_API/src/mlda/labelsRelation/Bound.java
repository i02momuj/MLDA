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

/**
* Class implementing the Bound
*
* @author Jose Maria Moyano Murillo
*/
public class Bound extends MLDataMetric{

	/**
	 * Constructor
	 */
	public Bound() {
		super("Bound");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){	
		this.value = Math.pow(2, mlData.getNumLabels());
		return value;
	}

}
