package com.milestone.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(500, 320);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.SIDEBAR);
        setLayout(new BorderLayout());

        //Main content panel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppColors.SIDEBAR);
        content.setBorder(new EmptyBorder(50,60,30,60));

        //Add emoju/icon
        JLabel emojiLabel = new JLabel(" ", JLabel.CENTER);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(emojiLabel);

        content.add(Box.createVerticalStrut(15));

        //App name
        JLabel nameLabel = new JLabel("MILESTONE", JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 36));
        nameLabel.setForeground(AppColors.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(nameLabel);

        content.add(Box.createVerticalStrut(8));

        //Tagline
        JLabel tagLabel = new JLabel(
                "Your Productivity Companion", JLabel.CENTER);
        tagLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        tagLabel.setForeground(AppColors.TEXT_SECONDARY);
        tagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(tagLabel);

        content.add(Box.createVerticalStrut(8));


        //Progress Bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(AppColors.ACCENT);
        progressBar.setBackground(AppColors.CARD_BORDER);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE,8));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(progressBar);

        content.add(Box.createVerticalStrut(10));

        //Loading Label
        JLabel loadingLabel = new JLabel("Loading...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loadingLabel.setForeground(AppColors.TEXT_SECONDARY);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(loadingLabel);

        content.add(Box.createVerticalStrut(20));

        //Version Label
        JLabel versionLabel = new JLabel("v1.0",  JLabel.CENTER);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        versionLabel.setForeground(AppColors.TEXT_SECONDARY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(versionLabel);

        add(content, BorderLayout.CENTER);

        //The accent border around the whole splash
        getRootPane().setBorder(BorderFactory.createLineBorder(
                AppColors.ACCENT, 3
        ));

        //Animating the progress bar using a background thread
        //then launching the login screen when done
        Thread splashThread = new Thread(() -> {
            try{
                for(int i = 0; i<= 100; i +=2){
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);
                        if(progress <40)
                            loadingLabel.setText("Initialising...");
                        else if(progress < 75)
                            loadingLabel.setText("Connecting to database...");
                        else loadingLabel.setText("Almost ready...");
                    });
                    Thread.sleep(30); //30ms x 50 steps = t.5 secs total
                }
                Thread.sleep(200); // brief pause at 100%
                SwingUtilities.invokeLater(() -> {
                    dispose(); //Close splash
                    new LoginFrame().setVisible(true); //open login
                });
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        splashThread.start();
    }
}
