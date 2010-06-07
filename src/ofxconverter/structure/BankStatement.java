/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.structure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ofxconverter.Language;

/**
 *
 * @author Floris
 */
public class BankStatement {

    private final String eol = System.getProperty("line.separator");

    // This is the creation date of the ofx
    private String dateTime = "";

    private StringBuilder failedStrings = new StringBuilder();

    // First transaction
    private long dateStart = 99999999;

    // Last transaction
    private long dateEnd = 0;

    private Language language = Language.ENG;
    private String currency = "";
    private String account = "";

    private List<Transaction> transactions = new ArrayList<Transaction>();

    public BankStatement(){
        createDateTime();
    }

    /**
     *  This function will set the current date of this bank statement
     */
    public final void createDateTime(){
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");

        this.dateTime = formatter.format(today);
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the dateStart
     */
    public long getDateStart() {
        return dateStart;
    }

    /**
     * @param dateStart the dateStart to set
     */
    public void setDateStart(String dateStart) {
        this.dateStart = new Long(dateStart);
    }

    /**
     * @return the dateEnd
     */
    public long getDateEnd() {
        return dateEnd;
    }

    /**
     * @param dateEnd the dateEnd to set
     */
    public void setDateEnd(String dateEnd) {
        this.dateEnd = new Long(dateEnd);
    }

    /**
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean addTransaction( Transaction transaction ){

        Long transactionDate = new Long( transaction.getDate() );

        if( dateStart > transactionDate ){
            dateStart = transactionDate;
        }

        if( dateEnd < transactionDate ){
            dateEnd = transactionDate;
        }

        return transactions.add(transaction);
    }

    /**
     * @return the failedStrings
     */
    public StringBuilder getFailedStrings() {
        return failedStrings;
    }

    /**
     * @param failedStrings the failedStrings to set
     */
    public void setFailedStrings( String failedString ) {
        this.failedStrings.append( failedString ).append( account ).append(eol);
    }

    /**
     * @param failedStrings the failedStrings to set
     */
    public void addFailedString( String failedString ) {
        this.failedStrings.append( failedString ).append( account ).append(eol);
    }

}
