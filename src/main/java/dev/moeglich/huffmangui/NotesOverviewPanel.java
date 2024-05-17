package dev.moeglich.huffmangui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import dev.moeglich.db_bindings.Database;
import dev.moeglich.db_bindings.Note;
import dev.moeglich.huffmanlib.Huffman;

class NotePanel extends JPanel {
    private MouseAdapter viewNoteListener;
    private ActionListener deleteNoteListener;
    private JButton deleteButton;
    private Consumer<Note> onNoteSelected;

    public NotePanel(Note note, Consumer<Note> onNoteSelected) {
        this.setLayout(new GridBagLayout());

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                this.getBorder()));

        this.onNoteSelected = onNoteSelected;
        this.load_note(note);
    }

    public void removeListeners() throws IllegalStateException {
        deleteButton.removeActionListener(deleteNoteListener);
        this.removeMouseListener(viewNoteListener);
    }

    void load_note(Note note) {
        GridBagConstraints gbc = new GridBagConstraints();

        String decoded;
        try {
            decoded = Huffman.decode_from_bytes(note.content);
        } catch (Exception e) {
            decoded = "Error decoding note";
        }

        int trimLength = 20;
        String trimmed = decoded;
        if (trimmed.length() > trimLength) {
            trimmed = trimmed.substring(0, trimLength) + "...";
        }
        if (trimmed.length() == 0) {
            trimmed = "<Empty note>";
        }

        JLabel contentLabel = new JLabel(trimmed);
        JLabel createdAtLabel = new JLabel(note.createdAt.toString());
        deleteButton = new JButton("Delete");

        contentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        createdAtLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        this.add(contentLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;
        this.add(createdAtLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        this.add(deleteButton, gbc);

        deleteNoteListener = e -> {
            try {
                Database.deleteById(note.id);
                onNoteSelected.accept(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        deleteButton.addActionListener(deleteNoteListener);

        viewNoteListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                onNoteSelected.accept(note);
            }
        };
        this.addMouseListener(viewNoteListener);
    }

    public void replaceNote(Note note) {
        if (deleteNoteListener != null) {
            this.removeAll();
            this.removeListeners();
        }
        this.load_note(note);
        this.revalidate();
        this.repaint();
    }
}

public class NotesOverviewPanel extends JPanel {
    private Consumer<Note> onNoteSelected;
    private JPanel boxes;
    private LinkedHashMap<Integer, NotePanel> notePanels;

    public NotesOverviewPanel(Consumer<Note> onNoteSelected, Note[] initialNotes) {
        this.onNoteSelected = onNoteSelected;
        this.setLayout(new BorderLayout());

        this.boxes = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension preferredSize = super.getPreferredSize();
                preferredSize.width = 300; // Set the fixed width
                return preferredSize;
            }
        };
        this.boxes.setLayout(new BoxLayout(boxes, BoxLayout.Y_AXIS));

        this.add(new JScrollPane(boxes), BorderLayout.CENTER);

        this.notePanels = new LinkedHashMap<>();
        for (Note note : initialNotes) {
            NotePanel panel = createNotePanel(note);
            this.notePanels.put(note.id, panel);
            this.boxes.add(panel, 0);
        }

        this.refresh();
    }

    public void saveNotePanel(Note note) {
        NotePanel currentPanel = this.notePanels.get(note.id);
        if (currentPanel != null) {
            currentPanel.replaceNote(note);
            return;
        }

        NotePanel panel = createNotePanel(note);
        this.notePanels.put(note.id, panel);
        this.boxes.add(panel, 0);
        this.refresh();
    }

    private NotePanel createNotePanel(Note note) {
        return new NotePanel(note, e -> {
            if (e == null) {
                onNoteSelected.accept(null);
                this.removeNotePanel(note.id);
                return;
            }
            onNoteSelected.accept(e);
        });
    }

    public void removeNotePanel(int noteId) {
        NotePanel panel = this.notePanels.get(noteId);
        if (panel == null) {
            return;
        }
        panel.removeListeners();
        this.boxes.remove(panel);
        this.notePanels.remove(noteId);
        this.refresh();

        onNoteSelected.accept(null);
    }

    private void refresh() {
        this.boxes.revalidate();
        this.boxes.repaint();
    }
}
