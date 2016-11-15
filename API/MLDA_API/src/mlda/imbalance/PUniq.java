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


import mlda.labelsRelation.UniqueLabelsets;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Proportion of unique label combination (PUniq)
*
* @author Jose Maria Moyano Murillo
*/
public class PUniq extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public PUniq() {
		super("Proportion of unique label combination (PUniq)");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		super.calculate(mlData);
		
		Statistics stat = new Statistics();
		stat.calculateStats(mlData);
		
		UniqueLabelsets uniqueLabelsets = new UniqueLabelsets();		
		double uniq = uniqueLabelsets.calculate(mlData);		 

		this.value = uniq / mlData.getNumInstances();
		return value;
	}

}
