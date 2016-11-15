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

import java.util.Set;

import mlda.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
* Class implementing the Mean of skewness of numeric attributes
*
* @author Jose Maria Moyano Murillo
*/
public class MeanSkewnessNumericAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanSkewnessNumericAttributes() {
		super("Mean of skewness of numeric attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
        Instances instances = mlData.getDataSet();
        int nInstances = mlData.getNumInstances();
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        
        int nNumeric = 0;
        double mean = 0;
        double avg;
        double var;
        double stdev;
        
        for(Attribute att : attributesSet){
            if(att.isNumeric()){
                nNumeric++;
                avg = instances.meanOrMode(att);
                var = 0;
                for(Instance inst : instances){
                    var += Math.pow(inst.value(att) - avg, 3);
                }
                stdev = Math.sqrt(instances.variance(att));
                mean += nInstances*var / ((nInstances-1)*(nInstances-2)*Math.pow(stdev, 3));
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
