package renders;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DefaultRender extends DefaultTableCellRenderer
{
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
