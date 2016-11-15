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

package mlda.attributes;

import mlda.base.MLDataMetric;
import mlda.util.Utils;
import mulan.data.MultiLabelInstances;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
* Class implementing the Mean of entropies of nominal attributes
*
* @author Jose Maria Moyano Murillo
*/
public class MeanEntropiesNominalAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanEntropiesNominalAttributes() {
		super("Mean of entropies of nominal attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		double mean = 0.0;
        
		Instances instances = mlData.getDataSet();
        
        int countNominal = 0;
        int [] featureIndices = mlData.getFeatureIndices();
        
        for(int fIndex : featureIndices){
            AttributeStats attStats = instances.attributeStats(fIndex);
            if(attStats.nominalCounts != null){
                countNominal++;
                mean += Utils.entropy(attStats.nominalCounts);
            }
        }
        
        mean = mean/countNominal;
		
		this.value = mean;
		return value;
	}

}
