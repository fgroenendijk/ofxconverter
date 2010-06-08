/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui;

import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceManager;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Floris
 */
public class FileTableModel extends DefaultTableModel{

    private static ResourceMap resourceMap = null;
    private DefaultTableModel tableModel = null;

    private Class[] types = new Class [] {
        java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
    };
    
    public FileTableModel( ApplicationContext context, String[] names ){
        ResourceManager manager = context.getResourceManager();
        resourceMap = manager.getResourceMap();

        for( String name: names ){
            this.addColumn( name );
        }

    }

    private void createModel(){
        tableModel = new DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Process", "Title 2", "Title 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        };

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
