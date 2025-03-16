package Service;

import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    // Constructor for the service when an authorDAO is provided.
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }



    public Message createMessage(Message message) {

        // Ensure message_text is not blank, and is not over 255 characters
        if (message.message_text.length() <= 0 || message.message_text.length() > 255) {
            return null;
        }

        // Ensure posted_by refers to a real, existing user
        
        if (accountDAO.getAccountById(message.posted_by) == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    /**
     * 
     * @return List of all messages retrieved by the DAO
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    /**
     * @param messageId
     * @return The message if it exists.
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }


    /**
     * 
     * @param messageId
     * @return The deleted message if it existed, otherwise null
     */
    public Message deleteMessageById(int messageId) {
        // Check if the message exists and store for returning
        Message message = messageDAO.getMessageById(messageId);
        if (message == null) {
            return null;
        }

        // Delete the message and return the object
        messageDAO.deleteMessageById(messageId);
        return message;
    }


}
