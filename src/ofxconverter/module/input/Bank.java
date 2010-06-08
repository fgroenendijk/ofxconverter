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

    public Bank getBank( FileType fileType ){
        switch( fileType ){
            case CSV_RABOBANK: return new Rabobank();
            case CSV_ING: return new IngPostbank();
        }
        return null;
    }

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

   protected String transactionType( String type, boolean isDebet ){
        String returnType = "OTHER";

        if( type.equalsIgnoreCase( "MA" ) ){ // Machtiging
            returnType = "DIRECTDEBIT";
        }else if( type.equalsIgnoreCase( "TB") ){ // Telebankieren
            returnType = "PAYMENT";
        }else if( type.equalsIgnoreCase( "BA") ){ // Betaalautomaat
            returnType = "POS";
        }else if( type.equalsIgnoreCase( "GA") ){ // Geldautomaat (pin)
            returnType = "ATM";
        }else if( type.equalsIgnoreCase( "OV") ){ // Overschrijving
            if(isDebet)
            {
                returnType = "DEBIT";
            }else{
                returnType = "CREDIT";
            }
        }else if( type.equalsIgnoreCase( "BY") ){ // Bijschrijving
            if(isDebet){
                returnType = "DEBIT";
            }else{
                returnType = "CREDIT";
            }
        } else if( type.equalsIgnoreCase( "DA") ){ // Diversen
            if(isDebet){
                returnType = "DEBIT";
            } else {
                returnType = "CREDIT";
            }
        }

        return returnType;
    }
}
