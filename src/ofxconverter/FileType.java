/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter;

/**
 *
 * @author Floris
 */
public enum FileType {
    CSV(true), CSV_ING, CSV_RABOBANK;

    private boolean general = true;

    public boolean isGeneral(){
        return general;
    }

    FileType(){
        this.general = false;
    }

    FileType( boolean general ){
        this.general = general;
    }
}
