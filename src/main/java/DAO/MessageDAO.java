package DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {


    /**
     * Persist a new message into the database.
     * 
     * @param message The message object to be persisted
     * @return the message object with the generated message_id, or null
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.posted_by, message.message_text, message.time_posted_epoch);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
