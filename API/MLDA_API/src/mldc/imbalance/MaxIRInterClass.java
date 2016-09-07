package mldc.imbalance;

import mulan.data.MultiLabelInstances;

/**
* Class implementing the Max IR inter class
*
* @author Jose Maria Moyano Murillo
*/
public class MaxIRInterClass extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public MaxIRInterClass() {
		super("Max IR inter class");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		super.calculate(mlData);
		
		double max = 0;
        
        for(int i=0; i<imbalancedData.length; i++){
            if(imbalancedData[i].getIRInterClass()!= Double.NaN && 
                    imbalancedData[i].getIRInterClass() >= 0){
                if(imbalancedData[i].getIRInterClass() > max){
                    max = imbalancedData[i].getIRInterClass();
                }
            }
        }
        
		this.value = max;
		return value;
	}

}
