/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import ofxconverter.FileHandler;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceManager;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Floris
 */
public class FileTableModel extends DefaultTableModel{

    private ResourceMap resourceMap = null;
    private List<FileHandler> fileHandlers = new ArrayList<FileHandler>();

    private Class[] types = new Class [] {
        java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
    };

    @Override
    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if( column == 2 ){
            return fileHandlers.get(row);
        }
        else{
            return super.getValueAt(row, column);
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if( column == 2 && aValue instanceof FileHandler ){
            FileHandler fileHandler = (FileHandler) aValue;
            fileHandlers.add( (FileHandler) aValue );
            aValue = fileHandler.getType().toString();
        }
        super.setValueAt(aValue, row, column);
    }

    @Override
    public void addRow(Object[] rowData) {
        FileHandler fileHandler = null;
        if( rowData.length > 2 && rowData[2] instanceof FileHandler ){
            fileHandler = (FileHandler) rowData[2];
            rowData[2] = fileHandler.getType().toString();            
        }
        else{
            fileHandler = new FileHandler();
        }
        fileHandlers.add( fileHandler );
        super.addRow(rowData);
    }

    public FileTableModel( ResourceMap resourceMap ){
        super( new Object [] { "Process", "File", "Bank" }, 0 );
        this.resourceMap = resourceMap;
    }

    /*fileTable.setName("fileTable"); // NOI18N



    //jScrollPane1.setViewportView(fileTable);

    fileTable.getColumnModel().getColumn(0).setMinWidth(30);
    fileTable.getColumnModel().getColumn(0).setMaxWidth(30);
    fileTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("fileTable.columnModel.title0")); // NOI18N
    fileTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("fileTable.columnModel.title1")); // NOI18N
    fileTable.getColumnModel().getColumn(2).setMinWidth(140);
    fileTable.getColumnModel().getColumn(2).setMaxWidth(140);
    fileTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("fileTable.columnModel.title2")); // NOI18N*/


}
