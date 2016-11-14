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

import mulan.data.MultiLabelInstances;

/**
* Class implementing the Mean of IR intra class
*
* @author Jose Maria Moyano Murillo
*/
public class MeanIRIntraClass extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public MeanIRIntraClass() {
		super("Mean of IR intra class");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		super.calculate(mlData);
		
		double mean = 0;
        int nValues = 0;
        
        for(int i=0; i<imbalancedData.length; i++){
            if(imbalancedData[i].getIRIntraClass() != Double.NaN){
            	mean += imbalancedData[i].getIRIntraClass();
                nValues++;
            }
        }
        
        mean = mean/nValues;
        
		this.value = mean;
		return value;
	}

}
