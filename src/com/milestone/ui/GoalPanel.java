package com.milestone.ui;

import com.milestone.dao.GoalDAO;
import com.milestone.model.Goal;
import com.milestone.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GoalPanel extends JPanel {
    private User currentUser;
    private GoalDAO goalDAO;
    private JTable goalTable;
    private DefaultTableModel tableModel;
    private List<Goal> goalList;

    public GoalPanel(User user) {
        this.currentUser = user;
        this.goalDAO = new GoalDAO();
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("🎯 My Goals");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Title", "Description", "Deadline", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        goalTable = new JTable(tableModel);
        styleTable(goalTable);
        goalTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        goalTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        goalTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        goalTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(goalTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                AppColors.CARD_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        JButton addBtn    = createButton("+ Add Goal",    AppColors.BUTTON_PRIMARY);
        JButton deleteBtn = createButton("🗑 Delete Goal", AppColors.BUTTON_DANGER);

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openAddGoalDialog());
        deleteBtn.addActionListener(e -> deleteGoal());

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        goalList = goalDAO.getGoalsByUser(currentUser.getUserId());

        if (goalList.isEmpty()) {
            tableModel.addRow(new Object[]{
                    "No goals yet — click Add Goal to start!", "", "", ""});
        } else {
            for (Goal g : goalList) {
                tableModel.addRow(new Object[]{
                        g.getTitle(),
                        g.getDescription() != null ? g.getDescription() : "",
                        g.getDeadline() != null ? g.getDeadline().toString() : "No deadline",
                        g.getStatus().equals("active") ? "🔥 Active" : "✅ Completed"
                });
            }
        }
    }

    private void openAddGoalDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Add Goal", true);
        dialog.setSize(500, 340);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 0, 8));
        formPanel.setBackground(AppColors.BACKGROUND);
        formPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

        JLabel titleLbl = new JLabel("Title *:");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 13));
        titleLbl.setForeground(AppColors.TEXT_DARK);

        JTextField titleField = new JTextField();
        styleField(titleField);

        JLabel descLbl = new JLabel("Description:");
        descLbl.setFont(new Font("Arial", Font.BOLD, 13));
        descLbl.setForeground(AppColors.TEXT_DARK);

        JTextField descField = new JTextField();
        styleField(descField);

        JLabel deadlineLbl = new JLabel("Deadline (YYYY-MM-DD):");
        deadlineLbl.setFont(new Font("Arial", Font.BOLD, 13));
        deadlineLbl.setForeground(AppColors.TEXT_DARK);

        JTextField deadlineField = new JTextField();
        deadlineField.setToolTipText("Format: YYYY-MM-DD  e.g. 2026-12-31");
        styleField(deadlineField);

        formPanel.add(titleLbl);
        formPanel.add(titleField);
        formPanel.add(descLbl);
        formPanel.add(descField);
        formPanel.add(deadlineLbl);
        formPanel.add(deadlineField);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(AppColors.BACKGROUND);
        JButton saveBtn   = createButton("Save",   AppColors.BUTTON_PRIMARY);
        JButton cancelBtn = createButton("Cancel", AppColors.TEXT_SECONDARY);
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Title is required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.sql.Date deadline = null;
            String dateText = deadlineField.getText().trim();
            if (!dateText.isEmpty()) {
                try {
                    deadline = java.sql.Date.valueOf(dateText);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Use YYYY-MM-DD",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String desc = descField.getText().trim();
            Goal newGoal = new Goal(currentUser.getUserId(), title,
                    desc.isEmpty() ? null : desc, deadline);
            if (goalDAO.addGoal(newGoal)) {
                JOptionPane.showMessageDialog(dialog,
                        "Goal added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Failed to add goal. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deleteGoal() {
        int row = goalTable.getSelectedRow();
        if (row < 0 || goalList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a goal first.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this goal?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Goal selected = goalList.get(row);
            if (goalDAO.deleteGoal(selected.getGoalId())) refresh();
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setGridColor(AppColors.CARD_BORDER);
        table.setSelectionBackground(AppColors.TABLE_SELECTED);
        table.setSelectionForeground(AppColors.TEXT_DARK);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.TABLE_HEADER);
        table.getTableHeader().setForeground(AppColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(
                AppColors.CARD_BORDER));
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 36));
        return btn;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.CARD_BORDER, 1),
                new EmptyBorder(5, 7, 5, 7)
        ));
        field.setBackground(Color.WHITE);
    }
}
