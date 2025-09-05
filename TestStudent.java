package com.ranga.grade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestStudent {

    // inner class for StudentRecord
    static class StudentRecord {
        String id;
        String name;
        List<Integer> grades = new ArrayList<>();

        StudentRecord(String id, String name) {
            this.id = id;
            this.name = name;
        }

        void addGrade(int g) {
            grades.add(g);
        }

        double getAverage() {
            if (grades.isEmpty()) return 0;
            int sum = 0;
            for (int g : grades) sum += g;
            return (double) sum / grades.size();
        }

        int getHighest() {
            if (grades.isEmpty()) return 0;
            int max = grades.get(0);
            for (int g : grades) if (g > max) max = g;
            return max;
        }

        int getLowest() {
            if (grades.isEmpty()) return 0;
            int min = grades.get(0);
            for (int g : grades) if (g < min) min = g;
            return min;
        }

        String getReport() {
            return "\n--- Report for Student ---\n" +
                    "ID: " + id + " | Name: " + name + "\n" +
                    "Marks/Grades: " + grades + "\n" +
                    "Average grade: " + getAverage() + "\n" +
                    "Highest marks: " + getHighest() + "\n" +
                    "Lowest marks: " + getLowest() + "\n";
        }
    }

    // method to add placeholder
    private static void addPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    // GUI main
    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Grade Tracker");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Input fields with placeholders
        JTextField idField = new JTextField(8);
        JTextField nameField = new JTextField(8);
        JTextField gradesField = new JTextField(15);

        addPlaceholder(idField, "Enter Student ID");
        addPlaceholder(nameField, "Enter Name");
        addPlaceholder(gradesField, "Enter comma separated grades");

        JButton addBtn = new JButton("Add Student");
        JButton showBtn = new JButton("Show Report");
        JTextArea output = new JTextArea(15, 40);
        output.setEditable(false);

        // Add components
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Grades (comma separated):"));
        panel.add(gradesField);

        // Button panel for alignment
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(addBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(10, 0))); // gap
        btnPanel.add(showBtn);

        panel.add(btnPanel);
        panel.add(new JScrollPane(output));

        frame.add(panel);

        // student list
        List<StudentRecord> students = new ArrayList<>();

        // Enter key navigation
        idField.addActionListener(e -> {
            if (!idField.getText().trim().isEmpty() &&
                !idField.getText().equals("Enter Student ID")) {
                nameField.requestFocus();
            }
        });

        nameField.addActionListener(e -> {
            String text = nameField.getText().trim();
            if (text.matches("[A-Za-z]+")) {
                gradesField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Name! Letters only.");
            }
        });

        gradesField.addActionListener(e -> addBtn.doClick()); // press enter = add student

        // Add student action
        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String gradesText = gradesField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || gradesText.isEmpty() ||
                id.equals("Enter Student ID") ||
                name.equals("Enter Name") ||
                gradesText.equals("Enter comma separated grades")) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            if (!name.matches("[A-Za-z]+")) {
                JOptionPane.showMessageDialog(frame, "Invalid Name! Letters only.");
                return;
            }

            StudentRecord sr = new StudentRecord(id, name);

            try {
                String[] parts = gradesText.split(",");
                if (parts.length < 1 || parts.length > 10) {
                    JOptionPane.showMessageDialog(frame, "Enter 1–10 grades only!");
                    return;
                }
                for (String p : parts) {
                    int g = Integer.parseInt(p.trim());
                    if (g < 0 || g > 100) {
                        JOptionPane.showMessageDialog(frame, "Grades must be 0–100 only!");
                        return;
                    }
                    sr.addGrade(g);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid grades format!");
                return;
            }

            students.add(sr);
            JOptionPane.showMessageDialog(frame, "Student Added Successfully!");

            idField.setText("");
            nameField.setText("");
            gradesField.setText("");

            // reset placeholders
            addPlaceholder(idField, "Enter Student ID");
            addPlaceholder(nameField, "Enter Name");
            addPlaceholder(gradesField, "Enter comma separated grades");

            idField.requestFocus();
        });

        // Show report action
        showBtn.addActionListener(e -> {
            StringBuilder report = new StringBuilder("===== SUMMARY REPORT OF ALL STUDENTS =====\n");
            for (StudentRecord s : students) {
                report.append(s.getReport());
            }
            output.setText(report.toString());
        });

        frame.setVisible(true);
    }
}
