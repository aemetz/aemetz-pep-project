package Service;

import Model.Account;
import DAO.AccountDAO;


/*
 * The Service class contains "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). It performs tasks that aren't done through the web or SQL.
 * Programming tasks like checking that the input is valid, conducting additional security checks,
 * or saving the actions undertaken by the API to a logging file.
 */

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Constructor for the service when an authorDAO is provided.
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }


    /**
     * Uses the AccountDAO to persist an account.
     * Checks for valid username and password.
     * Checks that the account doesn't already exist.
     * 
     * @param account The object of the account to be created
     * @return the Account object created if successful, or null
     */
    public Account addAccount(Account account) {

        // Check for validity of password (must be greater than 4 characters)
        if (account.getPassword().length() <= 4) {
            return null;
        }

        // Check for valid username (not blank)
        if (account.getUsername().length() == 0) {
            return null;
        }

        // Check if the account w/ given username already exists.
        // If we get an Account back, we return null which triggers the Controller to throw a 400.
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        // Checks have passed so we create the account.
        return accountDAO.insertAccount(account);
    }




    /**
     * Uses the AccountDAO to check if the account credentials map to an existing account.
     * 
     * @param account Account object containing username and password to be checked
     * @return the Account object with account_id if it exists, or null
     */
    public Account loginAccount(Account account) {
        return accountDAO.getAccountByUsernamePassword(account.getUsername(), account.getPassword());
    }




}
