/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.structure;

import java.util.ArrayList;
import java.util.List;
import ofxconverter.Currency;
import ofxconverter.Language;

/**
 *
 * @author Floris
 */
public class BankStatement {

    // This is the creation date of the ofx
    private String dateTime = "20100602210441";

    // First transaction
    private long dateStart = 99999999;

    // Last transaction
    private long dateEnd = 0;

    private Language language = Language.ENG;
    private Currency currency = Currency.EUR;
    private String account = "304635065";

    private List<Transaction> transactions = new ArrayList<Transaction>();

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
    public String getDateStart() {
        return Long.toString( dateStart );
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
    public String getDateEnd() {
        return Long.toString(dateEnd);
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
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
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

}
