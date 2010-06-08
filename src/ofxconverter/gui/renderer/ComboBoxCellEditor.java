/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.gui.renderer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Floris
 */
public class ComboBoxCellEditor extends DefaultCellEditor{

    public ComboBoxCellEditor(String[] items) {
        super( new JComboBox(items) );
    }
}
