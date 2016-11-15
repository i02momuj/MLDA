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

package mlda.labelsRelation;

/**
* Class implementing the Ratio of number of labelsets up to 5 examples
*
* @author Jose Maria Moyano Murillo
*/
public class RatioLabelsetsUpTo5Examples extends RatioLabelsetsUpToNExamples{
	
	/**
	 * Constructor
	 */
	public RatioLabelsetsUpTo5Examples() {
		super(5);
	}
}
