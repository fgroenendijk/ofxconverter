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
        String amount = "(\\d*[,\\.]?\\d+[,\\.]\\d+)";
        String strChar = "\"(\\w*)\"";
        String comma = ",";

        Pattern p = Pattern.compile( strChars + comma +
                                     strChars + comma +
                                     digits + comma +
                                     character + comma +
                                     amount + comma +
                                     strChar + comma +
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

            bankStatement.addTransaction(transaction);

        }
        else{
            bankStatement.addFailedString(line);
        }
    }
}
