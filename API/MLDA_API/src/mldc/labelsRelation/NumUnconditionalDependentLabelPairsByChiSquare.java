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

package mldc.labelsRelation;

import mldc.base.MLDataMetric;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.UnconditionalChiSquareIdentifier;

/**
* Class implementing the Number of unconditionally dependent label pairs by chi-square test
*
* @author Jose Maria Moyano Murillo
*/
public class NumUnconditionalDependentLabelPairsByChiSquare extends MLDataMetric{

	/**
	 * Constructor
	 */
	public NumUnconditionalDependentLabelPairsByChiSquare() {
		super("Number of unconditionally dependent label pairs by chi-square test");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		UnconditionalChiSquareIdentifier chi2Identifier = new UnconditionalChiSquareIdentifier();
        LabelsPair[] pairs = chi2Identifier.calculateDependence(mlData);
        
        int dep = 0;
        double score;
        
        for (LabelsPair pair : pairs) {
            score = pair.getScore();
            if(score > 6.635){
                dep++;
            }
            else{
                break;
            }
        }
		
		this.value = dep;
		return value;
	}

}
