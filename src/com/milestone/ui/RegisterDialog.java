package com.milestone.ui;

import com.milestone.dao.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserDAO userDAO;

    public RegisterDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Milestone - Create Account", true);
        this.userDAO = userDAO;
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(AppColors.SIDEBAR);
        titlePanel.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel titleLabel = new JLabel("Create Your Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8,1,0,5));
        formPanel.setBackground(AppColors.BACKGROUND);
        formPanel.setBorder(new EmptyBorder(20, 35, 20, 35));


        String[] labelTexts = {"Username:", "Email:", "Password:", "Confirm Password:"};

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JTextField[] fields = {
                usernameField, emailField,
                passwordField, confirmPasswordField
        };

        for (int i = 0; i < labelTexts.length; i++) {
            JLabel lbl = new JLabel(labelTexts[i]);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            lbl.setForeground(AppColors.TEXT_DARK);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            formPanel.add(lbl);

            formPanel.add(Box.createVerticalStrut(4));

            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            fields[i].setMaximumSize(new Dimension(400, 38));
            fields[i].setMinimumSize(new Dimension(380, 38));
            fields[i].setPreferredSize(new Dimension(380, 38));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppColors.CARD_BORDER, 1),
                    new EmptyBorder(5, 10, 5, 10)
            ));
            fields[i].setBackground(Color.WHITE);
            fields[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            formPanel.add(fields[i]);

            formPanel.add(Box.createVerticalStrut(12));
        }

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        JButton registerBtn = makeButton("Register",
                AppColors.BUTTON_PRIMARY);
        JButton cancelBtn = makeButton("Cancel",
                AppColors.TEXT_SECONDARY);


        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);


        // Register action
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirm.isEmpty()) {
                showError("All fields required");
                return;
            }
            if (!email.contains("@")) {
                showError("Please enter a valid email");
                return;
            }
            if (password.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }
            if (!password.equals(confirm)) {
                showError("Passwords do not match");
                return;
            }
            if (userDAO.register(username, email, password)) {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully! You can now log in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("An account with this email already exists");
            }
        });

        cancelBtn.addActionListener(e -> dispose());
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 38));
        return btn;
    }


    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}