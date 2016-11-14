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

package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Default render
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DefaultRender extends DefaultTableCellRenderer
{
    /**
     * Constructor without parameters
     */
    public DefaultRender(){}
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
        this.setForeground(Color.black);
        this.setOpaque(true);
       
        return this;
    }
    
}
