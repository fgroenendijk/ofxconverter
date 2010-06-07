/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.module.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ofxconverter.FileType;
import ofxconverter.structure.BankStatement;
import ofxconverter.structure.Transaction;

/**
 *
 * @author Floris
 */
public class Rabobank extends Bank {

    private static final String match = "^\"\\d*\",\".*\",\\d+,\".*\",\\d+\\.\\d+(,\".*\"){2},\\d+(,\".*\"){6}$";

    public static String getMatch(){
        return match;
    }

    public static FileType getType(){
        return FileType.CSV_RABOBANK;
    }

    protected void parseLine( String line, BankStatement bankStatement ){

        Transaction transaction = new Transaction();

        boolean isDebet = false;

        String strDigits = "\"(\\d+)\"";
        String strChars = "\"(\\w+)\"";
        String strText = "\"(.*)\"";
        String character = "\"(\\w)\"";
        String digits = "(\\d+)";
        String amount = "(\\d+\\.\\d+)";
        String strChar = "\"(\\w*)\"";
        String comma = ",";

        //"0304635065","EUR",20100512,"D",30.88,"0000000000","V. HEERDT MOTORSERV. HIL",20100512,"ba","","Pinautomaat 16:02 pasnr. 005","","","","",""

        Pattern p = Pattern.compile( strChars + comma +
                                     strChars + comma +
                                     digits + comma +
                                     character + comma +
                                     amount + comma +
                                     strChars + comma +
                                     strText + comma +
                                     digits + comma +
                                     strChars + comma +
                                     strChar + comma +
                                     strText + comma +
                                     strText + comma +
                                     strText + comma +
                                     strText + comma +
                                     strText + comma +
                                     strText );

        Matcher m = p.matcher( line );

        if( m.find() ){

            for( int i = 1; i < m.groupCount(); i++ ){
                switch( i ){
                    case 1:
                        if(  isEmptyString( bankStatement.getAccount() ) ){
                            bankStatement.setAccount( m.group(i) );
                        }
                        break;

                    case 2:
                        if(  isEmptyString( bankStatement.getCurrency() ) ){
                            bankStatement.setCurrency( m.group(i) );
                        }
                        break;

                    case 3:
                        transaction.setInterestDate( m.group(i) );
                        if(  isBigger( bankStatement.getDateStart(), transaction.getInterestDate() )  ){
                            bankStatement.setDateStart( m.group(i) );
                        }
                        if(  isSmaller( bankStatement.getDateEnd(), transaction.getInterestDate() )  ){
                            bankStatement.setDateEnd( m.group(i) );
                        }
                        break;
                    case 4:
                        isDebet = m.group(i).equalsIgnoreCase( "d" );
                        break;
                    case 5:
                        String amountTemp = m.group(i);
                        if( isDebet ){
                            amountTemp = "-" + amountTemp;
                        }
                        transaction.setAmount(amountTemp);
                        break;
                    case 6:
                        transaction.setAccount(m.group(i));
                        break;
                    case 7:
                        transaction.setName(m.group(i));
                        break;
                    case 8:
                        transaction.setDate( m.group(i) );
                        break;
                    case 9:
                        transaction.setType( transactionType( m.group(i), isDebet ) );
                        break;
                    case 10:
                        transaction.setMemo( m.group(i) + " " );
                        break;
                    case 11:
                        transaction.setMemo( transaction.getMemo() + m.group(i) + " " );
                        break;
                    case 12:
                        transaction.setMemo( transaction.getMemo() + m.group(i) + " " );
                        break;
                    case 13:
                        transaction.setMemo( transaction.getMemo() + m.group(i) + " " );
                        break;
                    case 14:
                        transaction.setMemo( transaction.getMemo() + m.group(i) + " " );
                        break;
                    case 15:
                        transaction.setMemo( transaction.getMemo() + m.group(i) + " " );
                        break;
                    case 16:
                        transaction.setMemo( transaction.getMemo() + m.group(i) );
                        break;
                }
            }

            // Remove all double spaces from the memo field
            transaction.setMemo( transaction.getMemo().replaceAll( "\\s{2,}", " " ).trim() );

            bankStatement.addTransaction(transaction);

        }
        else{
            bankStatement.addFailedString(line);
        }
    }

    private String transactionType( String type, boolean isDebet ){
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
