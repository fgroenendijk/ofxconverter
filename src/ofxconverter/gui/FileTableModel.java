/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import ofxconverter.FileHandler;

/**
 *
 * @author Floris
 */
public class FileTableModel extends DefaultTableModel implements TableModelListener{

    public enum Column{ CHECKBOX, TEXT, COMBOBOX }

    private List<FileHandler> fileHandlers = new ArrayList<FileHandler>();
    private List<List<Boolean>> selectable = new ArrayList<List<Boolean>>();

    private Class[] types = new Class [] {
        java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
    };

    public FileTableModel( Object [] header ){
        super( header, 0 );
    }

    // This is called after construction of the new FileTableModel has completed
    {
        this.addTableModelListener(this);
    }

    @Override
    public void removeRow(int row) {
        fileHandlers.remove(row);
        selectable.remove(row);
        super.removeRow(row);
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

    public Object getObjectAt( int row, int column ){
        if( row < fileHandlers.size() ){
            if( column == Column.COMBOBOX.ordinal() ){
                return fileHandlers.get(row);
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if( column == Column.COMBOBOX.ordinal() && aValue instanceof FileHandler ){
            FileHandler fileHandler = (FileHandler) aValue;
            fileHandlers.add( (FileHandler) aValue );
            aValue = fileHandler.getType().toString();
        }
        super.setValueAt(aValue, row, column);
    }

    @Override
    public void addRow(Object[] rowData) {
        FileHandler fileHandler = null;
        if( rowData.length > 2 && rowData[Column.COMBOBOX.ordinal()] instanceof FileHandler ){
            fileHandler = (FileHandler) rowData[Column.COMBOBOX.ordinal()];
            rowData[Column.COMBOBOX.ordinal()] = fileHandler.getType().toString();
        }
        else{
            fileHandler = new FileHandler();
        }
        
        ArrayList<Boolean> newSelectableArray = new ArrayList();
        newSelectableArray.add(true);
        newSelectableArray.add(false);
        newSelectableArray.add(true);

        // This is used to define whether a field can be edited or not
        selectable.add( newSelectableArray );

        fileHandlers.add( fileHandler );
        super.addRow(rowData);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if( column == Column.TEXT.ordinal() ){
            return false;
        }
        else{
            List<Boolean> rowList =  selectable.get(row);
            if( rowList != null ){
                return rowList.get(column);
            }
            return super.isCellEditable(row, column);
        }
    }

    public void setCellEditable(boolean editable, int row, int column) {
        if( column != Column.TEXT.ordinal() ){
            List<Boolean> rowList =  selectable.get(row);
            if( rowList != null ){
                rowList.set(column, editable);
            }
        }
    }

    public void tableChanged(TableModelEvent e) {
        if( e.getColumn() == Column.COMBOBOX.ordinal() ){
            if( e.getSource() instanceof FileTableModel ){
                FileTableModel tableModel = (FileTableModel) e.getSource();

                for( int i = e.getFirstRow(); i <= e.getLastRow(); i++ ){
                    if( tableModel.getValueAt(i, Column.COMBOBOX.ordinal()).equals("") ){
                        tableModel.setValueAt( false, i, Column.CHECKBOX.ordinal() );
                        tableModel.setCellEditable(false, i, Column.CHECKBOX.ordinal());
                        this.fireTableCellUpdated(i, Column.CHECKBOX.ordinal());
                    }
                    else{
                        tableModel.setValueAt( true, i, Column.CHECKBOX.ordinal() );
                        tableModel.setCellEditable(true, i, Column.CHECKBOX.ordinal());
                        this.fireTableCellUpdated(i, Column.CHECKBOX.ordinal());
                    }

                }
            }
        }
    }

}
