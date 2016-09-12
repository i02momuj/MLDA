package mldc.attributes;

import mldc.base.MLDataMetric;
import mldc.dimensionality.Attributes;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Proportion of nominal attributes
*
* @author Jose Maria Moyano Murillo
*/
public class ProportionNominalAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public ProportionNominalAttributes() {
		super("Proportion of nominal attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
        NominalAttributes nominal = new NominalAttributes();
        Attributes att = new Attributes();
		
		this.value = nominal.calculate(mlData) / att.calculate(mlData);
		return value;
	}

}