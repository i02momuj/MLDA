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

package mldc.imbalance;

import java.util.HashMap;

import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Proportion of maxim label combination (PMax)
*
* @author Jose Maria Moyano Murillo
*/
public class PMax extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public PMax() {
		super("Proportion of maxim label combination (PMax)");
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
		
		HashMap<LabelSet, Integer> combCount = stat.labelCombCount();
        
        int max = 0;
        
        for(LabelSet key : combCount.keySet()){
            if(combCount.get(key) > max){
                max = combCount.get(key);
            }
        }

		this.value = ((double) max)/mlData.getNumInstances();
		return value;
	}

}
