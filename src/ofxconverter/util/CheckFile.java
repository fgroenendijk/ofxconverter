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

/**
 *
 * @author Floris
 */
public class CheckFile {

    private final long MAX_FILE_SIZE = 2097152L;
    private FileType fileType = FileType.CSV;

    public FileType getFileType(){
        return fileType;
    };

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
                                break;
                            }
                            else{
                                
                            }
                        }
                    default: str.append( (char)character );
                             break;
                }
            }
        }
        catch( FileNotFoundException e ){
            // report error
        }
        catch( IOException e ){
            // report error
            System.out.println("Error!!!");
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
            if( str.toString().matches("^\"\\d*\".\".*\"(.\"\\d*\"){2}(.\".*\"){2}.\"\\d+,\\d+\"(.\".*\"){2}$") ){
                this.fileType = FileType.CSV_ING;
            }
            else if( str.toString().matches("^\"\\d*\",\".*\",\\d+,\".*\",\\d+\\.\\d+(,\".*\"){2},\\d+(,\".*\"){6}$") )
            {
                this.fileType = FileType.CSV_RABOBANK;
            }
            return true;
        }
        return false;
    }
}
