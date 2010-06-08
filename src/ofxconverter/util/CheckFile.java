/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import ofxconverter.FileType;
import ofxconverter.module.input.IngPostbank;
import ofxconverter.module.input.Rabobank;

/**
 *
 * @author Floris
 */
public class CheckFile {

    private final long MAX_FILE_SIZE = 2097152L;
    private FileType fileType = FileType.CSV;
    private boolean hasHeader = false;

    public FileType getFileType(){
        return fileType;
    };

    public boolean hasHeader(){
        return hasHeader;
    }

    public boolean isValid( File file ){
        if( file.length() <= 0 || file.length() > MAX_FILE_SIZE ){
            return false;
        }

        FileReader fileReader = null;
        BufferedReader reader = null;

        try{
            fileReader = new FileReader( file );
            reader = new BufferedReader( fileReader );
            int character;
            StringBuilder str = new StringBuilder();
            while( ( character = reader.read() ) != -1 ){
                switch( character ){
                    case 0: return false;
                    case 13: break; // Do not append windows line break to str
                    case 10: 
                        if( checkLine( str ) ){
                            if( getFileType().isGeneral() ){
                                // Assume the file has a header
                                hasHeader = true;
                                break;
                            }
                            else{
                                return true;
                            }
                        }
                    default: str.append( (char)character );
                             break;
                }
            }
        }
        catch( FileNotFoundException e ){
            return false;
        }
        catch( IOException e ){
            // report error, show error icon?
            System.out.println("Error!!!");
            return false;
        }
        finally{
            try{
                if( reader != null ){
                    reader.close();
                }
                if( fileReader != null ){
                    fileReader.close();
                }
            }
            catch( IOException e ){
                // Send to oblivian???
            }
        }
        return false;
    }

    private boolean checkLine( StringBuilder str ){

        // This is a match for csv file
        if( str.toString().matches("^\".*\".(\".*\")*$") ){
            this.fileType = FileType.CSV;
            if( str.toString().matches( IngPostbank.getMatch() ) ){
                this.fileType = IngPostbank.getType();
            }
            else if( str.toString().matches( Rabobank.getMatch() ) ) {
                this.fileType = Rabobank.getType();
            }
            return true;
        }
        return false;
    }
}
