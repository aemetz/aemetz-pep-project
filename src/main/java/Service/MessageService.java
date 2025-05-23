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



    /**
     * 
     * @param messageId the ID of the message
     * @param messageText the new text to update the message with
     * @return the updated message, or null
     */
    public Message updateMessageById(int messageId, String messageText) {
        // Check if message text is valid
        int messageLen = messageText.length();
        if (messageLen <= 0 || messageLen > 255) {
            return null;
        }

        // Check if the message exists
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }

        // Update the existing message and return the new object
        messageDAO.updateMessageById(messageId, messageText);
        return new Message(
            messageId,
            existingMessage.getPosted_by(),
            messageText,
            existingMessage.getTime_posted_epoch()
        );
    }



    public List<Message> getAllMessagesByAccountId(int accountId) {
        return messageDAO.getAllMessagesByAccountId(accountId);
    }
}
