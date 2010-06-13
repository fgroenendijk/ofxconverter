/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Floris
 */
public class ComboBoxCellEditor extends DefaultCellEditor{

    private JComboBox combo = null;

    public void setEnabled( boolean enabled ){
        combo.setEnabled(enabled);
    }

    public boolean isEnabled(){
        return combo.isEnabled();
    }

    public ComboBoxCellEditor(String[] items) {
        super( new JComboBox(items) );
        if( super.getComponent() instanceof JComboBox ){
            combo = (JComboBox) super.getComponent();
        }
    }
}
