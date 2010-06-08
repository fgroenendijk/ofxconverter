/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ofxconverter.structure;

/**
 *
 * @author Floris
 */
public class Transaction {

    private long date = 0;
    private long interestDate = 0;
    private String amount = "820.97";
    private String name = "TEST Transaction name";
    private String account = "122577876";
    private String memo = "TEST Transaction name but with memo's";
    private String type = "OTHER";

    /**
     * @return the date
     */
    public long getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = new Long(date);
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        if( amount.matches( "-?\\d*\\.?\\d+,\\d+$" ) ){
            amount = amount.replace(",", ":");
            amount = amount.replace(".", ",");
            amount = amount.replace(":", ".");
        }
        this.amount = amount;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        // Remove all double spaces from the memo field
        memo = memo.replaceAll( "\\s{2,}", " " ).trim();
        this.memo = memo;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the interestDate
     */
    public long getInterestDate() {
        return interestDate;
    }

    /**
     * @param interestDate the interestDate to set
     */
    public void setInterestDate(String interestDate) {
        this.interestDate = new Long(interestDate);
    }
}
