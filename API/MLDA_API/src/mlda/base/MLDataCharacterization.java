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

package mlda.base;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mlda.metricsTaxonomy.AttributesMetrics;
import mlda.metricsTaxonomy.DimensionalityMetrics;
import mlda.metricsTaxonomy.ImbalanceMetrics;
import mlda.metricsTaxonomy.LabelsDistributionMetrics;
import mlda.metricsTaxonomy.LabelsRelationMetrics;
import mulan.data.MultiLabelInstances;

/**
 * Class for calculating a set of characterization metrics for a multi-label dataset
 *
 * @author Jose Maria Moyano Murillo
 */
public class MLDataCharacterization {

	/**
	 * MultiLabel Dataset
	 */
	protected MultiLabelInstances mlData;
	
	/**
	 * List of metrics
	 */
	protected ArrayList<MLDataMetric> metrics;
	
	/**
	 * List of available metrics
	 */
	protected String[] availableMetrics;
	
	
	
	/**
	 * Constructor with dataset
	 * 
	 * @param mlData Multi-label dataset to which calculate the metrics
	 */
	public MLDataCharacterization(MultiLabelInstances mlData) {
		this.mlData = mlData;
		availableMetrics = searchAvailableMetrics();
		this.metrics = new ArrayList<MLDataMetric>();
	}
	
	/**
	 * Constructor with dataset and metric
	 * 
	 * @param mlData Multi-label dataset to which calculate the metrics
	 * @param metric Metric to be added to the list
	 */
	public MLDataCharacterization(MultiLabelInstances mlData, MLDataMetric metric) {
		this.mlData = mlData;
		availableMetrics = searchAvailableMetrics();
		this.metrics = new ArrayList<MLDataMetric>();
		addMetric(metric);
	}
	
	/**
	 * Constructor with dataset and list of metrics
	 * 
	 * @param mlData Multi-label dataset to which calculate the metrics
	 * @param metrics List of metrics to calculate
	 */
	public MLDataCharacterization(MultiLabelInstances mlData, ArrayList<MLDataMetric> metrics) {
		this.mlData = mlData;
		availableMetrics = searchAvailableMetrics();
		this.metrics = new ArrayList<MLDataMetric>();
		this.metrics.addAll(metrics);
	}
	
	
	/**
	 * return the list of available metrics
	 * 
	 * @return An array with the names of all the available metrics
	 */
	public String[] getAvailableMetrics(){
		return availableMetrics;
	}
	
	
	/**
	 * To String method
	 * 
	 * @return All metrics as a String with name and value, separated by "\n"
	 */
	public String toString(){
		String s = new String();
		
		for(MLDataMetric metric : metrics){
			s += metric.toString() + "\n";
		}

		return s;
	}
	
	/**
	 * Add metric to the list
	 * 
	 * @param metric Metric to add to the list
	 * @return True if successful added and false otherwise
	 */
	public boolean addMetric(MLDataMetric metric){	
		metrics.add(metric);
		return true;
	}
	
	/**
	 * Add metrics to the list
	 * 
	 * @param metrics A list of MLDataMetrics to add to the current list
	 * @return True if all metrics are successfully added and false otherwise
	 */
	public boolean addMetrics(ArrayList<MLDataMetric> metrics){	
		for(MLDataMetric metric : metrics){
			if(!addMetric(metric)){
				return false;
			}
		}
		
		return true;			
	}
	
	
	/**
	 * Clear metrics list
	 */
	public void clear(){
		metrics.clear();
	}
	
	
	
	/**
	 * Get list of metrics
	 * 
	 * @return A list with the metrics of the object
	 */
	public ArrayList<MLDataMetric> getMetrics(){
		return metrics;
	}
	
	/**
	 * Get metric from the list
	 * 
	 * @param metricName Name of the metric to get
	 * @return A MLDataMetric object with the metric required
	 */
	public MLDataMetric getMetric(String metricName){
		for(MLDataMetric metric : metrics){
			if(metric.getName().equals(metricName)){
				return metric;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Calculate values of all the metrics in the list
	 */
	public void calculateMetrics(){
		for(MLDataMetric metric : metrics){
			metric.calculate(mlData);
		}
	}
	
	
	/**
	 * Calculate values of all the metrics in the list for a new mlData
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 */
	public void calculateMetrics(MultiLabelInstances mlData){		
		for(MLDataMetric metric : metrics){
			metric.calculate(mlData);
		}
	}
	
	
	private String[] searchAvailableMetrics(){
		
		String [] sizeMetrics = DimensionalityMetrics.getAvailableMetrics();
		String [] labelsDistributionMetrics = LabelsDistributionMetrics.getAvailableMetrics();
		String [] labelsRelationMetrics = LabelsRelationMetrics.getAvailableMetrics();
		String [] imbalanceMetrics = ImbalanceMetrics.getAvailableMetrics();
		String [] attributesMetrics = AttributesMetrics.getAvailableMetrics();

		String [] metrics = new String[0];
		metrics = concatenate(metrics, sizeMetrics);
		metrics = concatenate(metrics, labelsDistributionMetrics);
		metrics = concatenate(metrics, labelsRelationMetrics);
		metrics = concatenate(metrics, imbalanceMetrics);
		metrics = concatenate(metrics, attributesMetrics);
		
		return metrics;
	}
	
	/**
	 * Know if a metric name is available
	 * 
	 * @param metricName Name of the metric
	 * @return True if it is available and false otherwise
	 */
	public boolean isAvailable(String metricName){
		for(String metric : availableMetrics){
			if(metric.equals(metricName)){
				return true;
			}
		}
		
		return false;
	}
	
	private <T> T[] concatenate (T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
}
