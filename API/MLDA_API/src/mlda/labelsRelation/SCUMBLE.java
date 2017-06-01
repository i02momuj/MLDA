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

package mlda.labelsRelation;

import mlda.base.MLDataMetric;
import mlda.util.ImbalancedFeature;
import mlda.util.Utils;
import mulan.data.MultiLabelInstances;
import weka.core.Instance;
import weka.core.Instances;

/**
* Class implementing the SCUMBLE
*
* @author Jose Maria Moyano Murillo
*/
public class SCUMBLE extends MLDataMetric{

	/**
	 * Constructor
	 */
	public SCUMBLE() {
		super("SCUMBLE");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){

        double SCUMBLE = 0.0;
        
        ImbalancedFeature [] imbalanced_data =  Utils.getImbalancedWithIR(mlData, Utils.getSortedByFrequency(Utils.getAppearancesPerLabel(mlData)));
        
        ImbalancedFeature[] new_imbalanced_data = new ImbalancedFeature[imbalanced_data.length];
        
        for(int i=0; i<imbalanced_data.length; i++){
            for(int j=0; j<imbalanced_data.length; j++){
                if(mlData.getLabelNames()[i].equals(imbalanced_data[j].getName())){
                    new_imbalanced_data[i] = imbalanced_data[j];
                }
            }
        }
        
        int nLabels = mlData.getNumLabels();
        Instances instances = mlData.getDataSet();
        double IRLmean = 0;
        int nActive = 0;
        double prod = 1;
        double sum = 0;
        
        int [] labelIndices = mlData.getLabelIndices();
        
        for(Instance inst : instances){
        	IRLmean = 0;
        	prod = 1;
        	nActive = 0;
        	
        	for(int l=0; l<nLabels; l++){
        		if(inst.value(labelIndices[l]) == 1){
        			prod *= new_imbalanced_data[l].getIRInterClass();
        			IRLmean += new_imbalanced_data[l].getIRInterClass();
        			nActive++;
        		}
        		else{
        			//prod *= 0;
        		}        		
        	}
        	
        	if(nActive == 0){
        		sum += 1;
        	}
        	else{
        		IRLmean /= nActive;
            	
            	System.out.println(IRLmean);
            	
            	sum += 1 - (Math.pow(prod, 1.0/nLabels) / IRLmean);
        	}        	
        	
        }
        
        SCUMBLE = sum / mlData.getNumInstances();
        
		this.value = SCUMBLE;
		return value;
	}

}
