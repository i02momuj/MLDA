package mldc.labelsDistribution;

import mldc.base.MLDataMetric;
import mldc.util.Utils;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Standard deviation of label cardinality
*
* @author Jose Maria Moyano Murillo
*/
public class StdvCardinality extends MLDataMetric{

	public StdvCardinality() {
		super("Standard deviation of label cardinality");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		double stdv = 0;
		
		Cardinality card = new Cardinality();
		double avg = card.calculate(mlData);
        
        try{
            int [] labelsForInstance = Utils.labelsForInstance(mlData);
            double sum = 0;
            
            for(int i=0; i<labelsForInstance.length; i++){
                sum += Math.pow((double)labelsForInstance[i] - avg, 2);
            }
            
            stdv = Math.sqrt(sum / (labelsForInstance.length - 1));
        }
        catch(Exception e){
        	stdv = 0;
            e.printStackTrace();
        }
        
        this.value = stdv;
        return value;
	}
	
	

}
