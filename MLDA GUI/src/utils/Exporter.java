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

package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;

/**
 * This class allows to export a JTable to a file
 * 
 * @author Jose Maria Moyano Murillo
 */
public class Exporter {
    private File file;
    private JTable table;
    private String tableName;

    /**
     * Constructor specifying file, table and table name
     * 
     * @param file File
     * @param table Table
     * @param tableName Name of the table
     */
    public Exporter(File file, JTable table, String tableName) {
        this.file = file;
        this.table = table;
        this.tableName = tableName;
    }
}
