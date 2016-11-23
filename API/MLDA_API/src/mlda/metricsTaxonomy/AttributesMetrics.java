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

package mlda.metricsTaxonomy;

import java.util.Arrays;

/**
* Class storing the attribute metrics names
*
* @author Jose Maria Moyano Murillo
*/
public class AttributesMetrics {
	
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
		metrics = new String[12];
		
		metrics[0] = "Average absolute correlation between numeric attributes";
		metrics[1] = "Average gain ratio";
		metrics[2] = "Mean of entropies of nominal attributes";
		metrics[3] = "Mean of mean of numeric attributes";
		metrics[4] = "Mean of standard deviation of numeric attributes";
		metrics[5] = "Number of binary attributes";
		metrics[6] = "Number of nominal attributes";
		metrics[7] = "Number of numeric attributes";
		metrics[8] = "Proportion of binary attributes";
		metrics[9] = "Proportion of nominal attributes";
		metrics[10] = "Proportion of numeric attributes";
		metrics[11] = "Proportion of numeric attributes with outliers";
	}

}
