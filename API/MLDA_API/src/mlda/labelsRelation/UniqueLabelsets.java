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

import java.util.HashMap;

import mlda.base.MLDataMetric;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Number of unique labelsets
*
* @author Jose Maria Moyano Murillo
*/
public class UniqueLabelsets extends MLDataMetric{

	/**
	 * Constructor
	 */
	public UniqueLabelsets() {
		super("Number of unique labelsets");
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
		
		HashMap<LabelSet,Integer> labelsets = stat.labelCombCount();
        
        int uniq = 0;
        
        for(int n : labelsets.values()){
            if(n == 1){
                uniq++;
            }
        }
		
		this.value = uniq;
		return value;
	}

}
