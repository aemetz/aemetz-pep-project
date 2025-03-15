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


    // Uses the AccountDAO to persist an account.
    // Parameters: account (an Account object)
    // Returns: the persisted account if the persistence is successful
    public Account addAccount(Account account) {

        // Check for validity of password (must be greater than 4 characters)
        if (account.getPassword().length() <= 4) {
            return null;
        }


        // TO DO: Check that the account doesn't exist first
        // - Create a method in the DAO layer to get an account by username (unique)
        //       - This method returns the Account object, or null if it doesn't exist
        // - Call that method here
        // - If the result is null, we can create the object
        // - Otherwise, we have return null and the AccountHandler will therefore throw a 400 error



        return accountDAO.insertAccount(account);
    }

}
