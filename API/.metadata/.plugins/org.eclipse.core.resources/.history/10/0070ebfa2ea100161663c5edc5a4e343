package mldc.attributes;

import mldc.base.MLDataMetric;
import mldc.dimensionality.Attributes;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Proportion of binary attributes
*
* @author Jose Maria Moyano Murillo
*/
public class ProportionBinaryAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public ProportionBinaryAttributes() {
		super("Proportion of binary attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
        BinaryAttributes binaries = new BinaryAttributes();
        Attributes att = new Attributes();
		
		this.value = binaries.calculate(mlData) / att.calculate(mlData);
		return value;
	}

}
