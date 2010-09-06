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
public class Raiffeisen extends Bank {
    private static final String match = "^(\\d+\\.){2}\\d+;.*;-*(\\d*\\.\\d+;){2}(\\d+\\.){2}\\d+$";

    public static String getMatch(){
        return match;
    }

    public static FileType getType(){
        return FileType.CSV_RAIIFFEISEN;
    }

    protected void parseLine( String line, BankStatement bankStatement ){

        String strDate = "(\\d+\\.\\d+\\.\\d+)";
        String strText = "(.*)";
        String amount = "(-*\\d*\\.\\d+)";
        String colon = ";";

        Pattern p = Pattern.compile( strDate + colon +
                                     strText + colon +
                                     amount + colon +
                                     amount + colon +
                                     strDate );

        Matcher m = p.matcher( line );

        if( m.find() ){

            Transaction transaction = processLine( m, bankStatement );

            bankStatement.addTransaction(transaction);

        }
        else{

            bankStatement.addFailedString(line);

        }
    }

//Booked At;Text;Credit/Debit Amount;Balance;Valuta Date
//17.7.10;Texttexttexttext;-100.0;10000.0;19.7.10

    private Transaction processLine( Matcher m, BankStatement bankStatement ){

        Transaction transaction = new Transaction();

        bankStatement.setCurrency("CHF");

        for( int i = 1; i <= m.groupCount(); i++ ){
            switch( i ){
                case 1:
                    String date = toDate( m.group(i) );
                    transaction.setInterestDate( date );
                    if(  isBigger( bankStatement.getDateStart(), transaction.getInterestDate() )  ){
                        bankStatement.setDateStart( date );
                    }
                    if(  isSmaller( bankStatement.getDateEnd(), transaction.getInterestDate() )  ){
                        bankStatement.setDateEnd( date );
                    }
                    break;

                case 2:
                    transaction.setMemo( m.group(i) );
                    break;

                case 3:
                    transaction.setAmount(m.group(i));
                    break;

                case 4:
                    // Do nothing with balance
                    break;
                case 5:
                    transaction.setDate( toDate(m.group(i)) );
                    break;

            }
        }

        return transaction;
    }

    private String toDate( String date ){
        if( date == null ){
            return date;
        }

        String[] oldDate = date.split("\\.");

        //This will work until the year 2099
        String newDate = "20";

        if( oldDate.length > 2 ){
            // Year
            newDate += oldDate[2];
            // Month
            newDate += addDigit(oldDate[1]);
            // Day
            newDate += addDigit(oldDate[0]);
        }

        return newDate;
    }

    private String addDigit( String digit ){
        if( digit.length() < 2 ){
            digit = "0" + digit;
        }
        return digit;
    }

}
