package com.milestone.ui;

import com.milestone.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private JButton activeNavButton;

    //panels
    private DashboardPanel dashboardPanel;
    private TaskPanel taskPanel;
    private GoalPanel goalPanel;

    public MainDashboard(User user) {
        this.currentUser = user;
        setTitle("Milestone -- " + user.getUsername());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AppColors.BACKGROUND);

        //Confirm before closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        MainDashboard.this,
                        "Are you sure you want to exit Milestone?",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) System.exit(0);
            }
        });

        buildSidebar();
        buildContentArea();
        buildStatusBar();

        // Show dashboard by default
        showDashboard();
    }


    private void buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // App logo/name in sidebar
        JLabel appName = new JLabel("🎯 Milestone", JLabel.CENTER);
        appName.setFont(new Font("Arial", Font.BOLD, 20));
        appName.setForeground(AppColors.TEXT_PRIMARY);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        appName.setBorder(new EmptyBorder(0, 0, 30, 0));
        sidebar.add(appName);

        // Nav buttons
        JButton dashBtn  = createNavButton("📊  Dashboard");
        JButton taskBtn  = createNavButton("✅  Tasks");
        JButton goalBtn  = createNavButton("🎯  Goals");

        sidebar.add(dashBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(taskBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(goalBtn);
        sidebar.add(Box.createVerticalGlue());

        // Logout button at bottom of sidebar
        JButton logoutBtn = createNavButton("🚪  Logout");
        logoutBtn.setBackground(AppColors.BUTTON_DANGER);
        sidebar.add(logoutBtn);

        dashBtn.addActionListener(e  -> { showDashboard(); setActive(dashBtn); });
        taskBtn.addActionListener(e  -> { showTasks();     setActive(taskBtn); });
        goalBtn.addActionListener(e  -> { showGoals();     setActive(goalBtn); });
        logoutBtn.addActionListener(e -> logout());

        setActive(dashBtn);
        add(sidebar, BorderLayout.WEST);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(AppColors.SIDEBAR);
        btn.setForeground(AppColors.TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 42));
        btn.setPreferredSize(new Dimension(180, 42));
        return btn;
    }

    private void setActive(JButton btn) {
        if (activeNavButton != null) {
            activeNavButton.setBackground(AppColors.SIDEBAR);
            activeNavButton.setForeground(AppColors.TEXT_PRIMARY);
        }
        activeNavButton = btn;
        btn.setBackground(AppColors.ACCENT);
        btn.setForeground(Color.WHITE);
    }

    private void buildContentArea() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(AppColors.BACKGROUND);

        dashboardPanel = new DashboardPanel(currentUser);
        taskPanel      = new TaskPanel(currentUser);
        goalPanel      = new GoalPanel(currentUser);

        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(taskPanel,      "TASKS");
        contentPanel.add(goalPanel,      "GOALS");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void buildStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        statusBar.setBackground(AppColors.STATUS_BAR);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                AppColors.CARD_BORDER));

        JLabel userLabel = new JLabel("👤 Logged in as: " +
                currentUser.getUsername() + "  |  " + currentUser.getEmail());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(AppColors.TEXT_SECONDARY);
        statusBar.add(userLabel);

        add(statusBar, BorderLayout.SOUTH);
    }

    private void showDashboard() {
        dashboardPanel.refresh();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "DASHBOARD");
    }

    private void showTasks() {
        taskPanel.refresh();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "TASKS");
    }

    private void showGoals() {
        goalPanel.refresh();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "GOALS");
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
