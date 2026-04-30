package com.milestone.dao;

import com.milestone.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserDAO {

    //Checking if an email is already registered
    public boolean emailExists(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try(Connection c= DatabaseConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
    s.setString(1, email);
    ResultSet rs = s.executeQuery();
    return rs.next();
        }catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

//Register a new user- hashes password with BCrypt before saving
    public boolean register(String username, String email, String plainPassword) {
       if (emailExists(email)) return false;

       String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
       String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

       try(Connection c = DatabaseConnection.getConnection();
       PreparedStatement s = c.prepareStatement(sql)){
           s.setString(1, username);
           s.setString(2, email);
           s.setString(3, hashedPassword);
           return s.executeUpdate() > 0;
       }catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
    }

    //Login- finds user by email, then BCrypt verifies password
    public User login(String email, String plainPassword) {
        String  sql = "SELECT * FROM users WHERE email = ?";
        try(Connection c = DatabaseConnection.getConnection();
        PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1,email);
            ResultSet rs= s.executeQuery();
            if(rs.next()) {
                String storedHash = rs.getString("password_hash");
                //BCrypt.checkpw compares the plain password to the stored hash
                if (BCrypt.checkpw(plainPassword, storedHash)) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
    }
        return null;
}
}