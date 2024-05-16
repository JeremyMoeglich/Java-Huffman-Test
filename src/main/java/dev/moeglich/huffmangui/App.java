package dev.moeglich.huffmangui;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame window = new JFrame("Huffman Coding Demo");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);

        // Create a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the first tab with ConvertPanel
        ConvertPanel convertPanel = new ConvertPanel();
        JScrollPane convertScrollPane = new JScrollPane(convertPanel);
        convertScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        convertScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar vertical = convertScrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(16);
        tabbedPane.addTab("Convert", convertScrollPane);

        // Create a second tab with a simple panel
        NotesPanel notesPanel = new NotesPanel();
        JScrollPane otherScrollPane = new JScrollPane(notesPanel);
        otherScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Notes", otherScrollPane);

        // Add the tabbed pane to the window
        window.add(tabbedPane, BorderLayout.CENTER);

        window.setVisible(true);
    }
}