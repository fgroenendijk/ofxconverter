/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter;

import java.io.File;

/**
 *
 * @author Floris
 */
public class FileHandler {

    private File file = null;
    private FileType type = null;
    private boolean hasHeader = false;

    public FileHandler(){
    }

    public FileHandler( File file, FileType type, boolean hasHeader ){
        this.file = file;
        this.type = type;
        this.hasHeader = hasHeader;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the type
     */
    public FileType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(FileType type) {
        this.type = type;
    }

    /**
     * @return the hasHeader
     */
    public boolean isHasHeader() {
        return hasHeader;
    }

    /**
     * @param hasHeader the hasHeader to set
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }
}
