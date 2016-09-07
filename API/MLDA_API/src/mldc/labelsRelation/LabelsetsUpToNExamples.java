package mldc.labelsRelation;

import java.util.Arrays;
import java.util.Collection;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Number of labelsets up to N examples
*
* @author Jose Maria Moyano Murillo
*/
public class LabelsetsUpToNExamples extends MLDataMetric{

	protected int n = 0;

	/**
	 * Constructor
	 * 
	 * @param n Number of examples
	 */
	public LabelsetsUpToNExamples(int n) {
		super("Number of labelsets up to " + n + " examples");
		this.n = n;
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		Statistics stat = new Statistics();
		stat.calculateStats(mlData);
		
		Collection<Integer> counts = stat.labelCombCount().values();
        Integer[] combCounts = new Integer[counts.size()];
        counts.toArray(combCounts);
        Arrays.sort(combCounts);
        
        int count = 0;
        
        for(int i=0; i<combCounts.length; i++){
            if(combCounts[i] <= n){
                count++;
            }
            else{
                break;
            }
        }
		
		this.value = count;
		return value;
	}

}
