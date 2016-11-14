package mldc.imbalance;

import mulan.data.MultiLabelInstances;

/**
* Class implementing the Max IR intra class
*
* @author Jose Maria Moyano Murillo
*/
public class MaxIRIntraClass extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public MaxIRIntraClass() {
		super("Max IR intra class");
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
            if(imbalancedData[i].getIRIntraClass() != Double.NaN){
                if(imbalancedData[i].getIRIntraClass() > max){
                    max = imbalancedData[i].getIRIntraClass();
                }
            }
        }
        
		this.value = max;
		return value;
	}

}
