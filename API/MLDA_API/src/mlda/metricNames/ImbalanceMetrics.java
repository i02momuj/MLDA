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

package mlda.metricNames;

import java.util.Arrays;

/**
* Class storing the imbalance metrics names
*
* @author Jose Maria Moyano Murillo
*/
public class ImbalanceMetrics {
	
	static String [] metrics = null;
	
	/**
	 * Get the names of the available attributes metrics
	 * 
	 * @return An array with the names
	 */
	public static String[] getAvailableMetrics(){
		if(metrics == null){
			defaultMetrics();
		}
		
		return metrics;
	}
	
	/**
	 * Add a metric to the list
	 * 
	 * @param newMetric Name of the metric to add
	 */
	public static void addMetric(String newMetric){
		if(metrics == null){
			defaultMetrics();
		}
		
		metrics = Arrays.copyOf(metrics, metrics.length+1);
		metrics[metrics.length - 1] = newMetric;
	}
	
	/**
	 * Fill the array with the default metrics
	 */
	private static void defaultMetrics(){
		String [] metrics = new String[14];
		
		metrics[0] = "CVIR inter class";
		metrics[1] = "Kurtosis cardinality";
		metrics[2] = "Max IR inter class";
		metrics[3] = "Max IR intra class";
		metrics[4] = "Max IR per labelset";
		metrics[5] = "Mean of IR inter class";
		metrics[6] = "Mean of IR intra class";
		metrics[7] = "Mean of IR per labelset";
		metrics[8] = "Mean of kurtosis";
		metrics[9] = "Mean of skewness of numeric attributes";
		metrics[10] = "Mean of standard deviation of IR intra class";
		metrics[11] = "Proportion of maxim label combination (PMax)";
		metrics[12] = "Proportion of unique label combination (PUniq)";
		metrics[13] = "Skewness cardinality";
	}

}
