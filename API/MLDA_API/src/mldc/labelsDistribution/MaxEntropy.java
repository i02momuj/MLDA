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

package mldc.labelsDistribution;

import mldc.base.MLDataMetric;
import mldc.util.Utils;
import mulan.data.MultiLabelInstances;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
* Class implementing the Maximal entropy of labels
*
* @author Jose Maria Moyano Murillo
*/
public class MaxEntropy extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MaxEntropy() {
		super("Maximal entropy of labels");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){        
		Instances instances = mlData.getDataSet();
        
		int nLabels = mlData.getNumLabels();
        int [] labels = mlData.getLabelIndices();
        
        double [] entropies = new double[nLabels];
        
        for(int i=0; i<nLabels; i++){
            AttributeStats attStats = instances.attributeStats(labels[i]);
            
            if(attStats.nominalCounts != null){
                entropies[i] = Utils.entropy(attStats.nominalCounts);
            }
        }
        
        double maxEntropy = Double.MIN_VALUE;
        for(double e : entropies){
            if(e > maxEntropy){
                maxEntropy = e;
            }
        }
        
        this.value = maxEntropy;
        
        return value;
	}

}
