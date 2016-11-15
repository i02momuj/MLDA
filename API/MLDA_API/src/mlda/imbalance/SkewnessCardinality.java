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

import mlda.util.Utils;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Skewness cardinality
*
* @author Jose Maria Moyano Murillo
*/
public class SkewnessCardinality extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public SkewnessCardinality() {
		super("Skewness cardinality");
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
		
		double card = 0;
        
        try{
            int [] labelsForInstance = Utils.labelsForInstance(mlData);
            double avg = stat.cardinality();
            double sum = 0;
            int n = labelsForInstance.length;
            double cardStdev;
            
            for(int i=0; i<n; i++){
                sum += Math.pow(labelsForInstance[i] - avg, 3);
            }
            
            cardStdev = Math.sqrt(sum / (n-1));
            card =  n * sum / ((n - 1)*(n-2) * Math.pow(cardStdev, 3));
            
        }
        catch(Exception e){
        	card = 0;
            e.printStackTrace();
        }

		this.value = card;
		return value;
	}

}
