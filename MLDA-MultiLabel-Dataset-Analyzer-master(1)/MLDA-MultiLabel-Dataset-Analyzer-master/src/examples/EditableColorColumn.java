/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author oscglezm
 */
public class EditableColorColumn {

  public static void main(String args[]) {
    JFrame frame = new JFrame("Editable Color Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    TableModel model = new ColorTableModel();
    
    JTable table = new JTable(model);
    // TableColumn column = table.getColumnModel().getColumn(3);
    // column.setCellRenderer(renderer);
    // column.setCellEditor(editor);

    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.setSize(400, 150);
    frame.setVisible(true);
  }
}