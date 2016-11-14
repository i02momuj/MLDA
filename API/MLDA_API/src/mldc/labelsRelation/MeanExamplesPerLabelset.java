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

import java.util.HashMap;

import mldc.base.MLDataMetric;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Mean examples per labelset
*
* @author Jose Maria Moyano Murillo
*/
public class MeanExamplesPerLabelset extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanExamplesPerLabelset() {
		super("Mean examples per labelset");
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
        int nValues = labelsets.values().size();
        double mean = 0;
        
        for(int n : labelsets.values()){
            mean += n;
        }
        mean = mean/nValues;

		this.value = mean;
		return value;
	}

}
