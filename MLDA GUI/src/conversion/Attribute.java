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

package conversion;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class Attribute {
    String name;
    String type;
    
    Attribute()
    {
        name = new String();
	type = new String();
    }
	
    Attribute(String name, String type)
    {
        this.name = name;
	this.type = type;
    }
}
