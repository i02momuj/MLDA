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

package mlda.attributes;

import java.util.Vector;

import mlda.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import weka.core.Instances;
import weka.core.Utils;

/**
* Class implementing the Average absolute correlation between numeric attributes
*
* @author Jose Maria Moyano Murillo
*/
public class AvgAbsoluteCorrelationBetweenNumericAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public AvgAbsoluteCorrelationBetweenNumericAttributes() {
		super("Average absolute correlation between numeric attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
        Instances instances = mlData.getDataSet();
        
        int numInstances = mlData.getNumInstances();
        
        double res = 0.0;
        int count = 0;
        
        int [] featureIndices = mlData.getFeatureIndices();
        
        Vector<Integer> numericFeatureIndices = new Vector<>();
        for(int fIndex : featureIndices){
            if(instances.attribute(fIndex).isNumeric()){
                numericFeatureIndices.add(fIndex);
            }
        }
        
        if(numericFeatureIndices.size() <= 0){
            return Double.NaN;
        }
        
        double [][] attributesToDoubleArray = new double[numericFeatureIndices.size()][numInstances];
        for(int fIndex : numericFeatureIndices){
            attributesToDoubleArray[fIndex] = instances.attributeToDoubleArray(fIndex);
        }
        
        for(int fIndex1 : numericFeatureIndices){
            for(int fIndex2 = fIndex1+1; fIndex2 < numericFeatureIndices.size(); fIndex2++){
                count++;
                res += Utils.correlation(attributesToDoubleArray[fIndex1], attributesToDoubleArray[fIndex2], numInstances);
            }
        }
        
        if(count > 0){
        	this.value = res/count;
        }
        else{
        	this.value = Double.NaN;
        }
		
		//this.value = res/count;
		return value;
	}

}
