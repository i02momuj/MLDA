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

import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the CVIR inter class
*
* @author Jose Maria Moyano Murillo
*/
public class CVIRInterClass extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public CVIRInterClass() {
		super("CVIR inter class");
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
		
		double CV = 0;
		MeanIRInterClass meanIRinter = new MeanIRInterClass();
        double meanIR = meanIRinter.calculate(mlData);
        int nValues = 0;
            
        for(int i=0; i<imbalancedData.length; i++){
            if(imbalancedData[i].getIRInterClass() >= 0){
            	CV += Math.pow(imbalancedData[i].getIRInterClass() - meanIR, 2);
                nValues++;
            }
        }
        
        CV = CV/(nValues-1);
        CV = Math.sqrt(CV);
        CV = CV/meanIR;
		
		this.value = CV;
		return value;
	}

}
