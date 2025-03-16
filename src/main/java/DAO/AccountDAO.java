package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    

    // Get an account from the Account table given an account_id
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.executeUpdate();

            // If the account exists, return the account
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return account;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // If no account with the given username exists, return null
        return null;


    }


    // Insert an account into the Account table
    // The account_id is automatically generated, this method needs a username and password
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            // Create prepared statement with RETURN_GENERATED_KEYS flag in order to retrieve auto-generated account ID
            String sql = "INSERT INTO account(username, password) VALUES(?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet pkResultSet = ps.getGeneratedKeys();
            if(pkResultSet.next()){
                int generated_account_id = (int) pkResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }










}
