package com.Encoder_Decoder;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FileUtils;


public class Window extends javax.swing.JFrame {

    private javax.swing.JButton run;
    private javax.swing.JButton file;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JComboBox algorithm;
    private javax.swing.JComboBox function;
    private javax.swing.JTextArea inputJson;
    private javax.swing.JScrollPane scrollFileChooser;
    private javax.swing.JScrollPane scrollFile;
    private javax.swing.JTextField k;

    private Writer writer;
    private Algorithms algorithms;

    public Window() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        run = new javax.swing.JButton();
        scrollFileChooser = new javax.swing.JScrollPane();
        inputJson = new javax.swing.JTextArea();
        scrollFile = new javax.swing.JScrollPane();
        file = new javax.swing.JButton();
        k = new javax.swing.JTextField();

        String s1 [] = {
                algorithms.GOLOMB.getName(),
                algorithms.ELIASGAMMA.getName(),
                algorithms.FIBONACCI.getName(),
                algorithms.UNARIA.getName(),
                algorithms.DELTA.getName()
        };
        algorithm = new javax.swing.JComboBox(s1);
        s1 = new String[] {"Encoder","Decoder"};
        function = new javax.swing.JComboBox(s1);

        fileChooser.setPreferredSize(new java.awt.Dimension(814, 597));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Encoder_Decoder");
        setLocation(new java.awt.Point(500, 50));

        run.setText("Run");
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               runActionPerformed(evt);
            }
        });

        function.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionActionPerformed(evt);
            }
        });

        k.setText("2");
        algorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithmActionPerformed(evt);
            }
        });

        inputJson.setColumns(20);
        inputJson.setRows(5);
        scrollFileChooser.setViewportView(inputJson);

        file.setText("Choose file");
        file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readFile(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(file)
                                                .addComponent(algorithm)
                                                .addComponent(k)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(function)
                                                .addComponent(run))
                                        .addComponent(scrollFileChooser)
                                        .addComponent(scrollFile, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollFileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scrollFile, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(run)
                                        .addComponent(function)
                                        .addComponent(k)
                                        .addComponent(algorithm)
                                        .addComponent(file))
                                .addContainerGap())
        );

        pack();
    }

    private void functionActionPerformed(java.awt.event.ActionEvent evt) {
        if(function.getItemAt(function.getSelectedIndex()) == "Encoder"){
            algorithm.setEnabled(true);
            run.setEnabled(true);
            if(algorithm.getItemAt(algorithm.getSelectedIndex()) == "Golomb") k.setEnabled(true);
            else k.setEnabled(false);
        }
        else {
            algorithm.setEnabled(false);
            k.setEnabled(false);
        }
    }

    private void algorithmActionPerformed(java.awt.event.ActionEvent evt) {
        if(algorithm.getItemAt(algorithm.getSelectedIndex()) == "Golomb") k.setEnabled(true);
        else k.setEnabled(false);
    }

    private void runActionPerformed(java.awt.event.ActionEvent evt) {
        algorithm.setEnabled(false);
        k.setEnabled(false);
        file.setEnabled(false);
        function.setEnabled(false);
        run.setEnabled(false);
        try {
            if(inputJson.getText().isEmpty()) throw new Exception();
            if(function.getItemAt(function.getSelectedIndex()) == "Encoder") {
                writer = new Writer(fileChooser.getSelectedFile().getPath(), inputJson.getText(),
                        algorithm.getItemAt(algorithm.getSelectedIndex()).toString(),
                        k.getText());
            }
            else{
                writer = new Writer(fileChooser.getSelectedFile().getPath(), inputJson.getText(),
                        "","");
            }
        } catch (Exception e) {
            System.out.println("Verifique se a entrada está correta!");
        }
        String newFilePath;
        if(function.getItemAt(function.getSelectedIndex()) == "Encoder") {
            newFilePath = writer.encode();
            file.setEnabled(true);
            function.setSelectedIndex(1);
            function.setEnabled(true);
            run.setEnabled(true);
            readFileFromEncoding(newFilePath);
        }
        else {
            newFilePath = writer.decode();
            algorithm.setEnabled(false);
            k.setEnabled(false);
            file.setEnabled(false);
            function.setEnabled(false);
            run.setEnabled(false);
            readFileFromEncoding(newFilePath);
        }
    }
    private void readFile(java.awt.event.ActionEvent evt) {
        int val = fileChooser.showOpenDialog(this);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                inputJson.setText(FileUtils.readFileToString(file));
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void readFileFromEncoding(String path) {
        File file = new File(path);
        try {
            inputJson.setText(FileUtils.readFileToString(file));
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }
}