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

    public NotePanel(Note note, Consumer<Note> onNoteSelected, Consumer<Note> onNoteDeleted) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        String decoded;
        try {
            decoded = Huffman.decode_from_bytes(note.content);
        } catch (Exception e) {
            decoded = "Error decoding note";
        }

        int trimLength = 15;
        String trimmed = (decoded.length() > trimLength) ? decoded.substring(0, trimLength) + "..." : decoded;

        JLabel contentLabel = new JLabel(trimmed);
        JLabel createdAtLabel = new JLabel(note.createdAt.toString());
        deleteButton = new JButton("Delete");

        contentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        createdAtLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        this.add(contentLabel);
        this.add(createdAtLabel);
        this.add(deleteButton);

        deleteNoteListener = e -> {
            try {
                Database.deleteById(note.id);
                onNoteDeleted.accept(note);
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

    public void removeListeners() {
        this.deleteButton.removeActionListener(deleteNoteListener);
        this.removeMouseListener(viewNoteListener);
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
            currentPanel.removeListeners();
            this.boxes.remove(currentPanel);
        }

        NotePanel panel = createNotePanel(note);
        this.notePanels.put(note.id, panel);
        this.boxes.add(panel, 0);
        this.refresh();
    }

    private NotePanel createNotePanel(Note note) {
        return new NotePanel(note, this.onNoteSelected, removedNote -> {
            this.removeNotePanel(removedNote.id);
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
    }

    private void refresh() {
        this.boxes.revalidate();
        this.boxes.repaint();
    }
}
