/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

/**
 *
 * @author Floris
 */
public class CheckBoxCellEditor extends DefaultCellEditor{

    private JCheckBox combo = null;

    public void setEnabled( boolean enabled ){
        combo.setEnabled(enabled);
    }

    public boolean isEnabled(){
        return combo.isEnabled();
    }

    public CheckBoxCellEditor() {
        super( new JCheckBox() );
        if( super.getComponent() instanceof JCheckBox ){
            combo = (JCheckBox) super.getComponent();
        }
    }
}
