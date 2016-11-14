package mldc.labelsRelation;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Ratio of labelsets with number of examples less than half of the attributes
*
* @author Jose Maria Moyano Murillo
*/
public class RatioLabelsetsWithExamplesLessThanHalfAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public RatioLabelsetsWithExamplesLessThanHalfAttributes() {
		super("Ratio of labelsets with number of examples < half of the attributes");
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
		
		LabelsetsUpToNExamples upToN = new LabelsetsUpToNExamples(mlData.getFeatureIndices().length / 2);
		double n = upToN.calculate(mlData);
		
		this.value = n / stat.labelCombCount().values().size();
		return value;
	}

}
