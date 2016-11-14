package mldc.labelsRelation;

import java.util.HashMap;

import mldc.base.MLDataMetric;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Standard deviation of examples per labelset
*
* @author Jose Maria Moyano Murillo
*/
public class StdvExamplesPerLabelset extends MLDataMetric{

	/**
	 * Constructor
	 */
	public StdvExamplesPerLabelset() {
		super("Standard deviation of examples per labelset");
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
		
		HashMap<LabelSet,Integer> labelsets = stat.labelCombCount();
        int nValues = labelsets.values().size();
        double media = 0;
        
        for(int n : labelsets.values()){
            media += n;
        }
        media = media/nValues;
        
        double varianza = 0;
        
        for(int n : labelsets.values()){
            varianza += Math.pow(n-media, 2);
        }
        varianza = varianza/nValues;

		this.value = Math.sqrt(varianza);
		return value;
	}

}
