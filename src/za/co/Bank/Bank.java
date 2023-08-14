package za.co.Bank;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;
    
    public Bank(String name){
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }
    
    public String getNewUserUUID(){
        String uuid;
        Random random = new Random();
        int length = 6;
        boolean nonUnique;
        //Continues to loop until we get a unique value
        do {
            //Generate number
            uuid = "";
            for (int i = 0; i < length; i++) 
                uuid += ((Integer)random.nextInt(10)).toString();
            
            //Check to see if its unique
            nonUnique = false;
            for(User user  : this.users){
                if(uuid.compareTo(user.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
                
        } while (nonUnique);
        return uuid;
    }
    
    public String getNewAccountUUID() {
        String uuid;
        Random random = new Random();
        int length = 10;
        boolean nonUnique;
        //Continues to loop until we get a unique value
        do {
            //Generate number
            uuid = "";
            for (int i = 0; i < length; i++) 
                uuid += ((Integer)random.nextInt(10)).toString();
            
            //Check to see if its unique
            nonUnique = false;
            for(Account account : this.accounts){
                if(uuid.compareTo(account.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
                
        } while (nonUnique);
        return uuid;
    }
    
    //Add an Account
    public void addAccount(Account anAccount){
        this.accounts.add(anAccount);
    }
    //Create a new user of the bank
    public User addUser(String firstName, String lastName,
                        String pin){
        //Creates a new User Object
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);
        //Creates a savings account for the user
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);
        
        return newUser;
    }
    
    public User userLogin(String userID, String pin){
    //Searches through list of users
        for(User u : this.users){
        //Checks if user ID is correct
            if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)){
                return u;
            }
        }
        return null;
    }
     
    public String getName(){
        return this.name;
    }
    
}
