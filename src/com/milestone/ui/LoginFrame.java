package com.milestone.ui;

import com.milestone.dao.UserDAO;
import com.milestone.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginFrame () {
        userDAO = new UserDAO();
        setTitle("Milestone - Productivity System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(AppColors.BACKGROUND);

        //Center card panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 2),
                new EmptyBorder(40, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(450, 420));
        card.setMaximumSize(new Dimension(450, 420));


        //App title
        JLabel titleLabel = new JLabel("Milestone", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(6));


        //Subtitle
        JLabel subtitleLabel = new JLabel("Your productivity companion", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(AppColors.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);

        card.add(Box.createVerticalStrut(30));


        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(AppColors.TEXT_DARK);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailLabel);

        card.add(Box.createVerticalStrut(5));

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setPreferredSize(new Dimension(340, 40));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        emailField.setBackground(Color.WHITE);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailField);

        card.add(Box.createVerticalStrut(15));

        //Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(AppColors.TEXT_DARK);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLabel);

        card.add(Box.createVerticalStrut(5));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setPreferredSize(new Dimension(340, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setBackground(Color.WHITE);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passwordField);

        card.add(Box.createVerticalStrut(25));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(AppColors.BUTTON_PRIMARY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setPreferredSize(new Dimension(340, 42));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginButton);

        card.add(Box.createVerticalStrut(10));

        // Register button
        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(AppColors.BUTTON_DANGER);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(true);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerButton.setPreferredSize(new Dimension(340, 42));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(registerButton);

        // Add card to frame centre
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        add(card, gc);

        // Login action

        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter email and password",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = userDAO.login(email, password);
            if (user != null) {
                new MainDashboard(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password, try again!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });




        registerButton.addActionListener(e ->
                new RegisterDialog(this, userDAO).setVisible(true)
        );
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        field.setBackground(AppColors.BACKGROUND);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(AppColors.BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 38));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
            //Login frame is launched automatically from inside SplashScreen
            // when the progress bar is finished
        });
    }
}
