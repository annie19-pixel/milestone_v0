package com.milestone.ui;

import com.milestone.dao.TaskDAO;
import com.milestone.model.Task;
import com.milestone.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class TaskPanel extends JPanel {
    private User currentUser;
    private TaskDAO taskDAO;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private List<Task> taskList;

    public TaskPanel(User user) {
        this.currentUser = user;
        this.taskDAO = new TaskDAO();
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

        JLabel titleLabel = new JLabel("✅ My Tasks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Title", "Description", "Due Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        taskTable = new JTable(tableModel);
        styleTable(taskTable);
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                AppColors.CARD_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        JButton addBtn      = createButton("+ Add Task",       AppColors.BUTTON_PRIMARY);
        JButton completeBtn = createButton("✔ Mark Complete",  AppColors.BUTTON_SUCCESS);
        JButton incompleteBtn = createButton("↩ Mark Incomplete",AppColors.SIDEBAR_HOVER);
        JButton deleteBtn   = createButton("🗑 Delete Task",    AppColors.BUTTON_DANGER);

        buttonPanel.add(addBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(incompleteBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> openAddTaskDialog());
        completeBtn.addActionListener(e -> markTask(true));
        incompleteBtn.addActionListener(e -> markTask(false));
        deleteBtn.addActionListener(e -> deleteTask());

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        taskList = taskDAO.getTasksByUser(currentUser.getUserId());

        if (taskList.isEmpty()) {
            tableModel.addRow(new Object[]{
                    "No tasks yet — click Add Task to get started!",
                    "", "", ""});
        } else {
            for (Task t : taskList) {
                tableModel.addRow(new Object[]{
                        t.getTitle(),
                        t.getDescription() != null ? t.getDescription() : "",
                        t.getDueDate() != null ? t.getDueDate().toString() : "No date",
                        t.isCompleted() ? "✅ Complete" : "⏳ Incomplete"
                });
            }
        }
    }

    private void openAddTaskDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Add Task", true);
        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppColors.BACKGROUND);

        // Form panel using GridLayout — guarantees all fields are full width
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

        JLabel dateLbl = new JLabel("Due Date (YYYY-MM-DD):");
        dateLbl.setFont(new Font("Arial", Font.BOLD, 13));
        dateLbl.setForeground(AppColors.TEXT_DARK);

        JTextField dateField = new JTextField();
        dateField.setToolTipText("Format: YYYY-MM-DD  e.g. 2026-12-31");
        styleField(dateField);

        formPanel.add(titleLbl);
        formPanel.add(titleField);
        formPanel.add(descLbl);
        formPanel.add(descField);
        formPanel.add(dateLbl);
        formPanel.add(dateField);

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
            java.sql.Date dueDate = null;
            String dateText = dateField.getText().trim();
            if (!dateText.isEmpty()) {
                try {
                    dueDate = java.sql.Date.valueOf(dateText);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Use YYYY-MM-DD",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String desc = descField.getText().trim();
            Task newTask = new Task(currentUser.getUserId(), null,
                    title, desc.isEmpty() ? null : desc, dueDate);
            if (taskDAO.addTask(newTask)) {
                JOptionPane.showMessageDialog(dialog,
                        "Task added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refresh();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Failed to add task. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void markTask(boolean complete) {
        int row = taskTable.getSelectedRow();
        if (row < 0 || taskList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a task first.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task selected = taskList.get(row);
        boolean success = complete
                ? taskDAO.markComplete(selected.getTaskId())
                : taskDAO.markIncomplete(selected.getTaskId());
        if (success) refresh();
    }

    private void deleteTask() {
        int row = taskTable.getSelectedRow();
        if (row < 0 || taskList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a task first.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this task?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Task selected = taskList.get(row);
            if (taskDAO.deleteTask(selected.getTaskId())) refresh();
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