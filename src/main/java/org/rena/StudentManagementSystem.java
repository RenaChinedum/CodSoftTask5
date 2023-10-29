package org.rena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class StudentManagementSystem {
    private ArrayList<Student> students = new ArrayList<>();
    private JList<Student> studentList;
    
    public void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultListModel<Student> studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        JScrollPane scrollPane = new JScrollPane(studentList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All Students");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentDialog();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    removeStudent();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentDialog();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllStudents();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
    }


    private void addStudentDialog() {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField rollNumberField = new JTextField();
        JTextField gradeField = new JTextField();

        Object[] message = {
                "First Name:", firstNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Roll Number:", rollNumberField,
                "Grade:", gradeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String rollNumberText = rollNumberField.getText();
            String grade = gradeField.getText();

            try {
                int rollNumber = Integer.parseInt(rollNumberText);
                Student student = new Student(firstName,lastName,email,rollNumber,grade);
                students.add(student);
                refreshStudentList();
                saveStudentsToFile();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Roll Number must be an integer.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void removeStudent() throws IOException {
        int selectedIndex = studentList.getSelectedIndex();
        if (selectedIndex != -1) {
            students.remove(selectedIndex);
            refreshStudentList();
            saveStudentsToFile();
        } else {
            JOptionPane.showMessageDialog(null, "No student selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchStudentDialog() {
        String input = JOptionPane.showInputDialog(null, "Enter the roll number of the student:");
        if (input != null) {
            int rollToSearch = Integer.parseInt(input);
            Student foundStudent = null;

            for (Student student : students) {
                if (student.getRollNumber() == rollToSearch) {
                    foundStudent = student;
                    break;
                }
            }

            if (foundStudent != null) {
                JOptionPane.showMessageDialog(null, "Student found:\n" + foundStudent.toString(), "Student Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.", "Student Not Found", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    private void displayAllStudents() {
        StringBuilder studentdetails = new StringBuilder();
        for (Student student : students) {
            studentdetails.append(student.toString()).append("\n");
        }
        if (studentdetails.length() == 0) {
            JOptionPane.showMessageDialog(null, "No students to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "All Students:\n" + studentdetails.toString(), "All Students", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshStudentList() {
        DefaultListModel<Student> studentListModel = (DefaultListModel<Student>) studentList.getModel();
        studentListModel.clear();
        for (Student student : students) {
            studentListModel.addElement(student);
        }
    }

    private void saveStudentsToFile() throws IOException {
        String filePath = "/Users/decagon/Desktop/CV/student.txt";

        try(PrintWriter printWriter = new PrintWriter(new FileWriter(filePath))){
            for (Student student: students
                 ) {
                printWriter.println(student.getFirstName() + "," + student.getLastName() + "," + student.getEmail() + "," + student.getRollNumber() + "," + student.getGrade());
            }
            JOptionPane.showConfirmDialog(null, "Student data saved to file.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }catch (IOException e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Error saving student data to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadStudentsFromFile() {
        String filePath = "/Users/decagon/Desktop/CV/student.txt";

        File file = new File(filePath);
        if (file.exists()){
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
                students.clear();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    String[] parts = line.split(", ");
                    if(parts.length == 5){
                        String firstName = parts[0];
                        String lastName = parts[1];
                        String email = parts[2];
                        int rollNumber = Integer.parseInt(parts[3]);
                        String grade = parts[4];
                        students.add(new Student(firstName,lastName,email,rollNumber,grade));
                    }
                }
                refreshStudentList();
                JOptionPane.showMessageDialog(null, "Student data loaded from file.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error loading student data from file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            students = new ArrayList<>();
        }
    }

}
