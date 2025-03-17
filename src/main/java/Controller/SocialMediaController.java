package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.*;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {
    /**
     * Write the endpoints in the startAPI() method, as the test suite must receive a Javalin object from this method.
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

        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageById);
        app.delete("messages/{message_id}", this::deleteMessageById);
        app.patch("messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::getAllMessagesForUserHandler);

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



    /**
     * Create a new message. Status code of 200 if successful, otherwise 400.
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            context.json(mapper.writeValueAsString(newMessage));
        } else {
            context.status(400);
        }

    }



    /**
     * Respond with a JSON representation of a list containing all messages retrieved from the database
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        context.json(mapper.writeValueAsString(messages));
    }



    /**
     * Get a message object with a specific message_id. Empty response if message doesn't exist.
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void getMessageById(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String messageIdParam = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdParam);

        // If message doesn't exist, response will be empty (200)
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(200);
        }
    }


    /**
     * Deletes a message given an ID. If message didn't exist, response is empty, status 200.
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void deleteMessageById(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String messageIdParam = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdParam);

        Message message = messageService.deleteMessageById(messageId);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(200);
        }
    }


    /**
     * Given an ID and text, update the message with the new text.
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void updateMessage(Context context) throws JsonProcessingException {
        // Get message id
        String messageIdParam = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdParam);

        // Get message text
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(context.body());
        String messageText = jsonNode.get("message_text").asText();

        Message message = messageService.updateMessageById(messageId, messageText);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(400);
        }
    }



    /**
     * Retrieve all messages posted by the account with the given account ID
     * 
     * @param context
     * @throws JsonProcessingException
     */
    private void getAllMessagesForUserHandler(Context context) throws JsonProcessingException {
        // Get Account ID
        String accountIdParam = context.pathParam("account_id");
        int accountId = Integer.parseInt(accountIdParam);

        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        context.json(mapper.writeValueAsString(messages));
    }


}