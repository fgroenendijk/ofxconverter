/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.module.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import ofxconverter.FileType;
import ofxconverter.structure.BankStatement;

/**
 *
 * @author Floris
 */
public abstract class Bank {

    public static String getMatch(){
        throw new UnsupportedOperationException("Bank must implement getMatch() function");
    }

    public static FileType getType(){
        throw new UnsupportedOperationException("Bank must implement getType() function");
    }

    protected abstract void parseLine( String line, BankStatement bankStatement );

    public BankStatement readFile(File file) {
        BankStatement bankStatement = new BankStatement();
        bankStatement.createDateTime();

        FileReader fileReader = null;
        BufferedReader reader = null;

        try {

            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            
            String line = null;

            while ((line = reader.readLine()) != null) {
                parseLine(line, bankStatement);
            }

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            // report error, show error icon?
            System.out.println("Error!!!");
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                // Send to oblivian???
            }
        }
        return bankStatement;
    }

    protected boolean isEmptyString( String string ){
        if( string == null || string.isEmpty() ){
            return true;
        }
        return false;
    }

    protected boolean isSmaller( long firstDate, long secondDate){
        if( firstDate < secondDate ){
            return true;
        }
        return false;
    }

    protected boolean isBigger( long firstDate, long secondDate){
        if( firstDate > secondDate ){
            return true;
        }
        return false;
    }

}
