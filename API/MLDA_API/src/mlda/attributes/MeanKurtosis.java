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
* Class implementing the Mean of kurtosis
*
* @author Jose Maria Moyano Murillo
*/
public class MeanKurtosis extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanKurtosis() {
		super("Mean of kurtosis");
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
        
        double avg;
        double var2;
        double var4;
        double val;
        int nNumeric = 0;
        double mean = 0;
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        
        for(Attribute att : attributesSet){
            if(att.isNumeric()){
                nNumeric++;
                avg = instances.meanOrMode(att);
                var2 = 0;
                var4 = 0;
                
                for(Instance inst : instances){
                    val = inst.value(att);
                    var2 += Math.pow(val-avg, 2);
                    var4 += Math.pow(val-avg, 4);
                }
                
                double kurtosis = (nInstances*var4/Math.pow(var2,2))-3;
                double sampleKurtosis = (kurtosis*(nInstances+1) + 6) * (nInstances-1)/((nInstances-2)*(nInstances-3));
                mean += sampleKurtosis;
            }
        }
        if(nNumeric > 0){
        	mean = mean/nNumeric;
        }
        else{
        	mean = Double.NaN;
        }

		this.value = mean;
		return value;
	}

}
