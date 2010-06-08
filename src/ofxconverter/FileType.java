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
    CSV(true,"csv file"), CSV_ING("Ing bank csv"), CSV_RABOBANK("Rabobank csv");

    private boolean general = true;
    private String friendlyName = "";

    public boolean isGeneral(){
        return general;
    }

    public String getName(){
        return friendlyName;
    }

    FileType(){
        this.general = false;
    }

    FileType( String name ){
        this( false, name );
    }

    FileType( boolean general, String name ){
        this.general = general;
        this.friendlyName = name;
    }
}
