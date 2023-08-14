package za.co.Bank;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        //Stores the pin's MD5 hash, rather than the original value for security reasons
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Error! caught, NoSuchAlgorithmException.");
            ex.printStackTrace();
            System.exit(1);
        }
        //Get a new unique universal ID for the user
        this.uuid = theBank.getNewUserUUID();
        //Empty list of accounts
        this.accounts = new ArrayList<Account>();
        //Print log message
        System.out.printf("New user %s %s with ID %s created.\n", 
                           lastName, firstName, this.uuid);
    }
    
    public void addAccount(Account anAccount){
        this.accounts.add(anAccount);
    }
    
    public String getUUID(){
        return this.uuid;
    }
    
    public boolean validatePin(String aPin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()),
                   this.pinHash);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Error! caught, NoSuchAlgorithmException.");
            ex.printStackTrace();
            System.exit(1);
        } 
        return false;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
    
    public void printAccountsSummary(){
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("  %d) %s\n", i + 1,
                              this.accounts.get(i).getSummaryLine());
        }
        System.out.println("");
    }
    
    public int numAccounts(){
        return this.accounts.size();
    }
    
    public void printAccTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory();
    }
    
    public double getAccountBalance(int accIdx) {
        return this.accounts.get(accIdx).getBalance();
    }
    
    public String getAcctUUID(int accIdx){
        return this.accounts.get(accIdx).getUUID();
    }
    
    public void addAccountTransaction(int acctIdx, double amount,
                                      String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
