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

package mldc.attributes;

import mldc.base.MLDataMetric;
import mldc.dimensionality.Attributes;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Proportion of numeric attributes
*
* @author Jose Maria Moyano Murillo
*/
public class ProportionNumericAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public ProportionNumericAttributes() {
		super("Proportion of numeric attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
        NumericAttributes numeric = new NumericAttributes();
        Attributes att = new Attributes();
		
		this.value = numeric.calculate(mlData) / att.calculate(mlData);
		return value;
	}

}
