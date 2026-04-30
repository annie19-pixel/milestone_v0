package com.milestone.ui;

import com.milestone.dao.GoalDAO;
import com.milestone.dao.TaskDAO;
import com.milestone.model.Goal;
import com.milestone.model.Task;
import com.milestone.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private User currentUser;
    private TaskDAO taskDAO;
    private GoalDAO goalDAO;
    private JPanel statsPanel;
    private JLabel greetingLabel;

    public DashboardPanel(User user) {
        this.currentUser = user;
        this.taskDAO = new TaskDAO();
        this.goalDAO = new GoalDAO();
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        buildUI();
    }

    private void buildUI() {
        // Top greeting
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppColors.BACKGROUND);

        greetingLabel = new JLabel("Welcome back, " +
                currentUser.getUsername() + "! 👋");
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        greetingLabel.setForeground(AppColors.TEXT_PRIMARY);
        topPanel.add(greetingLabel, BorderLayout.WEST);

        JLabel subtitleLabel = new JLabel(
                "Here's your productivity overview today.");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(AppColors.TEXT_SECONDARY);

        JPanel topTextPanel = new JPanel();
        topTextPanel.setLayout(new BoxLayout(topTextPanel, BoxLayout.Y_AXIS));
        topTextPanel.setBackground(AppColors.BACKGROUND);
        topTextPanel.add(greetingLabel);
        topTextPanel.add(Box.createVerticalStrut(5));
        topTextPanel.add(subtitleLabel);
        topPanel.add(topTextPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Stats cards
        statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(AppColors.BACKGROUND);
        statsPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        add(statsPanel, BorderLayout.CENTER);

        // Progress section
        JPanel progressSection = new JPanel();
        progressSection.setLayout(new BoxLayout(progressSection, BoxLayout.Y_AXIS));
        progressSection.setBackground(AppColors.BACKGROUND);

        JLabel progressTitle = new JLabel("Overall Task Completion");
        progressTitle.setFont(new Font("Arial", Font.BOLD, 16));
        progressTitle.setForeground(AppColors.TEXT_PRIMARY);
        progressTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressSection.add(progressTitle);
        progressSection.add(Box.createVerticalStrut(10));

        add(progressSection, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        statsPanel.removeAll();

        List<Task> tasks = taskDAO.getTasksByUser(currentUser.getUserId());
        List<Goal> goals = goalDAO.getGoalsByUser(currentUser.getUserId());

        int totalTasks     = tasks.size();
        int completedTasks = (int) tasks.stream()
                .filter(Task::isCompleted).count();
        int totalGoals     = goals.size();
        int activeGoals    = (int) goals.stream()
                .filter(g -> g.getStatus().equals("active")).count();

        int percentage = totalTasks == 0 ? 0 :
                (completedTasks * 100) / totalTasks;

        statsPanel.add(createStatCard("📋 Total Tasks",
                String.valueOf(totalTasks), AppColors.CARD));
        statsPanel.add(createStatCard("✅ Completed",
                String.valueOf(completedTasks), AppColors.CARD));
        statsPanel.add(createStatCard("🎯 Total Goals",
                String.valueOf(totalGoals), AppColors.CARD));
        statsPanel.add(createStatCard("🔥 Active Goals",
                String.valueOf(activeGoals), AppColors.CARD));

        // Update progress bar
        Component south = ((BorderLayout) getLayout())
                .getLayoutComponent(BorderLayout.SOUTH);
        if (south instanceof JPanel) {
            JPanel progressSection = (JPanel) south;
            progressSection.removeAll();

            JLabel progressTitle = new JLabel("Overall Task Completion");
            progressTitle.setFont(new Font("Arial", Font.BOLD, 16));
            progressTitle.setForeground(AppColors.TEXT_PRIMARY);
            progressTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressSection.add(progressTitle);
            progressSection.add(Box.createVerticalStrut(10));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(percentage);
            progressBar.setStringPainted(true);
            progressBar.setString(percentage + "% Complete");
            progressBar.setFont(new Font("Arial", Font.BOLD, 13));
            progressBar.setForeground(AppColors.ACCENT);
            progressBar.setBackground(AppColors.CARD_BORDER);
            progressBar.setPreferredSize(new Dimension(0, 30));
            progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressSection.add(progressBar);

            progressSection.revalidate();
            progressSection.repaint();
        }

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color bg) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 2),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 42));
        valueLabel.setForeground(AppColors.TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(AppColors.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        return card;
    }
}