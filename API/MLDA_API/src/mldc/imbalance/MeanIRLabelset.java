package mldc.imbalance;

import java.util.HashMap;
import java.util.Set;

import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Mean of IR per labelset
*
* @author Jose Maria Moyano Murillo
*/
public class MeanIRLabelset extends ImbalanceDataMetric{

	/**
	 * Constructor
	 */
	public MeanIRLabelset() {
		super("Mean of IR per labelset");
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
		
		HashMap<LabelSet,Integer> result = stat.labelCombCount();
        Set<LabelSet> keysets = result.keySet();

        double mean = 0;
        
        int maxCountLabelset = 0;
        
        for(LabelSet labelset : keysets){
            if(result.get(labelset) > maxCountLabelset){
                maxCountLabelset = result.get(labelset);
            }
        }
        
        for(LabelSet labelset : keysets){
        	mean += maxCountLabelset / (result.get(labelset)*1.0);
        }
        mean = mean/keysets.size();
        
		this.value = mean;
		return value;
	}

}
