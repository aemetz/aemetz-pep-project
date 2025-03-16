package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import Model.Account;
import Service.AccountService;
import Model.Message;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("register", this::createAccountHandler);
        app.post("login", this::loginHandler);
        

        return app;
    }


    /**
     * Persist a new account.
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void createAccountHandler(Context context) throws JsonProcessingException {

        // ObjectMapper will convert JSON from post request into Account object
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // Add the account
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }

    }


    /**
     * Check that an account with the given username/password exists. 200, else 401.
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account loginAccount = accountService.loginAccount(account);
        if (loginAccount != null) {
            context.json(mapper.writeValueAsString(loginAccount));
        } else {
            context.status(401);
        }

    }

}