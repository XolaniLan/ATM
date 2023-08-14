import java.util.Scanner;
import za.co.Bank.Account;
import za.co.Bank.Bank;
import za.co.Bank.User;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank theBank = new Bank("Bank of Ekangala");
        
        //Adds user which also creates a savings account
        User aUser = theBank.addUser("John", "Doe", "1234");
        
        //Adds checking account for the user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);
        
        User curUser;
        while(true){
            // Stays in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);
            
            // Stays in the main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }
    
    public static User mainMenuPrompt(Bank theBank, Scanner sc){
        String userID;
        String pin;
        User authUser;
        
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.printf("Enter the ID: ");
            userID = sc.nextLine();
            System.out.printf("Enter pin: ");
            pin = sc.nextLine();
            
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.printf("Incorrect user ID/pin combination. " + 
                                  "Please try again.");
            }
        } while (authUser == null); //Continues to loop till successful login
        
        return authUser;
    }
    
    public static void printUserMenu(User theUser, Scanner sc){
        theUser.printAccountsSummary(); //Print a summary of the user's accounts
        
        int choice;
        
        do {
            System.out.printf("Welcome %s, what would you like to do?\n",
                               theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdrawal");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            
            if(choice < 1 || choice > 5)
                System.out.println("Invalid choice. Please choose 1-5");
            
        } while (choice < 1 || choice > 5);
        
        // Processes the choice
        switch(choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawlFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }
        
        // Redisplaye menu
        if (choice != 5 ) 
            ATM.printUserMenu(theUser, sc);
    }
    
    public static void showTransHistory(User theUser, Scanner sc){
        int theAccount;
        // Get's account whose transaction historyto look at
        do {
            System.out.printf("Enter the number (1-%d) of the account " +
                              "whose transactions you want to see: ",
                               theUser.numAccounts());
            theAccount = sc.nextInt() - 1;
            if (theAccount < 0 || theAccount >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again.");
            
        } while (theAccount < 0 || theAccount >= theUser.numAccounts());
        
        //Prints the transactio history
        theUser.printAccTransHistory(theAccount);
    }
    
    public static void transferFunds(User theUser, Scanner sc){
        int fromAccount;
        int toAccount;
        double amount;
        double accountBal;
        
        do {
            System.out.printf("Enter the number (1 - %d) of the account " +
                              "to transfer from: ", theUser.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts())
               System.out.println("Invalid account. Please try again.");
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        
        accountBal = theUser.getAccountBalance(fromAccount);
        
        //Gets the account to transfer to
        do {
            System.out.printf("Enter the number (1 - %d) of the account " +
                              "to transfer to: ", theUser.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts())
               System.out.println("Invalid account. Please try again.");
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());
        
        //Gets the amount to transfer to
        do {
            System.out.printf("Enter the amount to transfer (max R%.02f): R",
                               accountBal);
            amount = sc.nextDouble();
            
            if(amount < 0)
                System.out.println("Amount must be greater than zero.");
            else
                if(amount > accountBal)
                    System.out.printf("Amount must not be greater than\n" +
                                      "balance of R%.02f.\n", accountBal);
        } while (amount < 0 || amount > accountBal);
        
        //Transfer
        theUser.addAccountTransaction(fromAccount, -1 * amount, String.format(
                                      "Transfer to account %s", 
                                      theUser.getAcctUUID(toAccount)));
        
        theUser.addAccountTransaction(toAccount, amount, String.format(
                                      "Transfer to account %s", 
                                      theUser.getAcctUUID(fromAccount)));
    }
    
    public static void withdrawlFunds(User theUser, Scanner sc){
        int fromAccount;
        String memo;
        double amount;
        double accountBal = 0.0;
        
        do {
            System.out.printf("Enter the number (1 - %d) of the account " +
                              "to withdraw from: ",theUser.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts())
               System.out.println("Invalid account. Please try again.");
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        
        do {
            System.out.printf("Enter the amount to withdraw (max R%.02f): R",accountBal);
            amount = sc.nextDouble();
            
            if(amount < 0)
                System.out.println("Amount must be greater than zero.");
            else
                if(amount > accountBal)
                    System.out.printf("Amount must not be greater than " +
                                      "balance of R%.02f.\n", accountBal);
        } while (amount < 0 || amount > accountBal);
        
        //Gobble up rest of previous input
        sc.nextLine();
        //Gets a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
        //Withdrawal
        theUser.addAccountTransaction(fromAccount, -1 * amount, memo);
    }
    
    public static void depositFunds(User theUser, Scanner sc){
        int toAccount;
        String memo;
        double amount;
        double accountBal = 0.0;
        
        do {
            System.out.printf("Enter the number (1 - %d) of the account to deposit in: ",
                              theUser.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts())
               System.out.println("Invalid account. Please try again.");
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());
        
        do {
            System.out.printf("Enter the amount to transfer (max R%.02f): R",accountBal);
            amount = sc.nextDouble();
            
            if(amount < 0)
                System.out.println("Amount must be greater than zero.");
       
        } while (amount < 0);
        
        //Gobble up rest of previous input
        sc.nextLine();
        //Gets a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
        //Withdrawal
        theUser.addAccountTransaction(toAccount, amount, memo);
        
    }
    
}
