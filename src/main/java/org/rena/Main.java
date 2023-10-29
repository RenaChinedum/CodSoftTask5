package org.rena;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management System");
            StudentManagementSystem managementSystem = new StudentManagementSystem();
            managementSystem.createUI(frame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
            managementSystem.loadStudentsFromFile();
        });
    }
}