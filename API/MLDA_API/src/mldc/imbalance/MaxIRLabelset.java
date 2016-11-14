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
import java.util.Set;

import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Max IR per labelset
*
* @author Jose Maria Moyano Murillo
*/
public class MaxIRLabelset extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public MaxIRLabelset() {
		super("Max IR per labelset");
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
		
		HashMap<LabelSet,Integer> result = stat.labelCombCount();
        Set<LabelSet> keysets = result.keySet();
        
        double IR;
        
        int maxCountLabelset = 0;
        double maxIR = 0;
        
        for(LabelSet labelset : keysets){
            if(result.get(labelset) > maxCountLabelset){
                maxCountLabelset = result.get(labelset);
            }
        }

        for(LabelSet labelset : keysets){
        	IR = maxCountLabelset / (result.get(labelset)*1.0);
            if(IR > maxIR){
                maxIR = IR;
            }
        }
        
		this.value = maxIR;
		return value;
	}

}
