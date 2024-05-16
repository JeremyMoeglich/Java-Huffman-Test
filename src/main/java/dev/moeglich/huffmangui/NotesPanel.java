package dev.moeglich.huffmangui;

import javax.swing.*;
import dev.moeglich.db_bindings.Database;
import dev.moeglich.db_bindings.Note;
import dev.moeglich.huffmanlib.Huffman;
import java.awt.*;
import java.awt.event.ActionListener;

public class NotesPanel extends JPanel {
    private JButton saveExistingButton;
    private ActionListener saveExistingListener;
    private JButton saveNewButton;
    private ActionListener saveNewListener;
    private JButton deleteButton;
    private ActionListener deleteListener;
    private Integer currentNoteId;
    private UndoableTextArea currentNote;
    private NotesOverviewPanel overviewPanel;

    public NotesPanel() {
        this.setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridBagLayout());
        this.add(grid, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel title = new JLabel("Notes");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        grid.add(title, gbc);

        JPanel bottomGrid = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        grid.add(bottomGrid, gbc);

        JPanel leftBottomGrid = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        bottomGrid.add(leftBottomGrid, gbc);

        currentNote = new UndoableTextArea();
        currentNote.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        currentNote.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        leftBottomGrid.add(currentNote, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftBottomGrid.add(buttonPanel, gbc);

        this.refreshButtonPanel(buttonPanel);

        Note[] notes;
        try {
            notes = Database.getAll().toArray(new Note[0]);
        } catch (Exception e) {
            notes = new Note[0];
            e.printStackTrace();
        }

        overviewPanel = new NotesOverviewPanel(note -> {
            try {
                this.currentNoteId = note.id;
                this.currentNote.setText(Huffman.decode_from_bytes(note.content));
                this.refreshButtonPanel(buttonPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, notes);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        bottomGrid.add(overviewPanel, gbc);
    }

    void refreshButtonPanel(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        if (saveExistingListener != null) {
            saveExistingButton.removeActionListener(saveExistingListener);
        }
        if (saveNewListener != null) {
            saveNewButton.removeActionListener(saveNewListener);
        }
        if (deleteListener != null) {
            deleteButton.removeActionListener(deleteListener);
        }
        panel.removeAll();

        if (this.currentNoteId != null) {
            saveExistingButton = new JButton("Save changes");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(saveExistingButton, gbc);

            deleteButton = new JButton("Delete note");
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(deleteButton, gbc);

            saveExistingListener = e -> {
                try {
                    Note updatedNote = Database.update(this.currentNoteId, Huffman.encode_to_bytes(this.currentNote.getText()));
                    overviewPanel.saveNotePanel(updatedNote);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            saveExistingButton.addActionListener(saveExistingListener);

            deleteListener = e -> {
                try {
                    Database.deleteById(this.currentNoteId);
                    overviewPanel.removeNotePanel(this.currentNoteId);

                    this.currentNoteId = null;
                    this.currentNote.setText("");
                    this.refreshButtonPanel(panel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            deleteButton.addActionListener(deleteListener);
        }

        saveNewButton = new JButton("Save new note");
        gbc.gridx = this.currentNoteId != null ? 2 : 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(saveNewButton, gbc);

        saveNewListener = e -> {
            try {
                Note created = Database.create(Huffman.encode_to_bytes(this.currentNote.getText()));
                this.currentNoteId = created.id;

                overviewPanel.saveNotePanel(created);
                this.refreshButtonPanel(panel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        saveNewButton.addActionListener(saveNewListener);

        panel.revalidate();
        panel.repaint();
    }
}
