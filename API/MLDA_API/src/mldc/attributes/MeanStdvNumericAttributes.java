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

package mldc.attributes;

import java.util.Set;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instances;

/**
* Class implementing the Mean of standard deviation of numeric attributes
*
* @author Jose Maria Moyano Murillo
*/
public class MeanStdvNumericAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanStdvNumericAttributes() {
		super("Mean of standard deviation of numeric attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		double mean = 0;
        int nNumeric = 0;
        
        Instances instances = mlData.getDataSet();
        
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        for(Attribute att : attributeSet){
            if(att.isNumeric()){
                nNumeric++;
                mean += Math.sqrt(instances.variance(att));
            }
        }
        
        if(nNumeric > 0){
        	this.value = mean / nNumeric;
        }
        else{
        	this.value = Double.NaN;
        }
		
		//this.value = mean;
		return value;
	}

}
