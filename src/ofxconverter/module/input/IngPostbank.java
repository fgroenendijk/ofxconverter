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
public class IngPostbank extends Bank {

    private static final String match = "^\"\\d*\".\".*\"(.\"\\d*\"){2}(.\".*\"){2}.\"\\d+,\\d+\"(.\".*\"){2}$";

    public static String getMatch(){
        return match;
    }

    public static FileType getType(){
        return FileType.CSV_ING;
    }

    protected void parseLine( String line, BankStatement bankStatement ){

        Transaction transaction = new Transaction();

        boolean isDebet = false;

        String strDigits = "\"(\\d+)\"";
        String strChars = "\"(\\w+)\"";
        String strText = "\"(.*)\"";
        String character = "\"(\\w)\"";
        String digits = "(\\d+)";
        String strAmount = "\"(\\d*[,\\.]?\\d+[,\\.]\\d+)\"";
        String strChar = "\"(\\w*)\"";
        String comma = ",";

//"20100601","0170659 V. HEERDT MOTORSERV.>\HI","657400114","550376054","BA","Af","40,00","Betaalautomaat","PASVOLGNR 004     01-06-2010 17:29TRANSACTIENR 3888600       "
//"(\d+)","(.*)","(\w+)","(\w+)","(\w+)","(\w+)","(\d*[,\.]?\d+[,\.]\d+)","(\w+)","(.*)"
        Pattern p = Pattern.compile( strDigits + comma +
                                     strText + comma +
                                     strChars + comma +
                                     strChars + comma +
                                     strChars + comma +
                                     strChars + comma +
                                     strAmount + comma +
                                     strChars + comma +
                                     strText  );

        Matcher m = p.matcher( line );

        if( m.find() ){

            if(  isEmptyString( bankStatement.getCurrency() ) ){
                bankStatement.setCurrency( "EUR" );
            }

            for( int i = 1; i < m.groupCount(); i++ ){
                switch( i ){
                    case 1:
                        transaction.setDate( m.group(i) );
                        if(  isBigger( bankStatement.getDateStart(), transaction.getDate() )  ){
                            bankStatement.setDateStart( m.group(i) );
                        }
                        if(  isSmaller( bankStatement.getDateEnd(), transaction.getDate() )  ){
                            bankStatement.setDateEnd( m.group(i) );
                        }
                        break;

                    case 2:
                        transaction.setName(m.group(i));
                        break;

                    case 3:
                        if(  isEmptyString( bankStatement.getAccount() ) ){
                            bankStatement.setAccount( m.group(i) );
                        }
                        break;

                    case 4:
                        transaction.setAccount(m.group(i));
                        break;

                    case 5:
                        transaction.setType( transactionType( m.group(i), isDebet ) );
                        break;

                    case 6:
                        isDebet = m.group(i).equalsIgnoreCase( "Af" );
                        break;

                    case 7:
                        String amountTemp = m.group(i);
                        if( isDebet ){
                            amountTemp = "-" + amountTemp;
                        }
                        transaction.setAmount(amountTemp);
                        break;
                    case 8:
                        // Is not needed for anything
                        break;

                    case 9:
                        transaction.setMemo( m.group(i) );
                        break;
                }
            }

            bankStatement.addTransaction(transaction);

        }
        else{
            bankStatement.addFailedString(line);
        }
    }
}
