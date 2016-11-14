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

import mldc.labelsDistribution.Cardinality;
import mldc.util.Utils;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Kurtosis cardinality
*
* @author Jose Maria Moyano Murillo
*/
public class KurtosisCardinality extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public KurtosisCardinality() {
		super("Kurtosis cardinality");
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
		
		Cardinality card = new Cardinality();
		double cardinality = card.calculate(mlData);
        
        int [] labelsForInstance = Utils.labelsForInstance(mlData);
        
        int nInstances = mlData.getNumInstances();
        
        double v;
        double sum2 = 0;
        double sum4 = 0;
        
        for(int i=0; i<nInstances; i++){
            v = labelsForInstance[i] - cardinality;
            sum2 += Math.pow(v, 2);
            sum4 += Math.pow(v, 4);
        }
        
        double kurtosis = (nInstances*sum4/Math.pow(sum2,2))-3;
        double sampleKurtosis = (kurtosis*(nInstances+1) + 6) * (nInstances-1)/((nInstances-2)*(nInstances-3));
		
		this.value = sampleKurtosis;
		return value;
	}

}
