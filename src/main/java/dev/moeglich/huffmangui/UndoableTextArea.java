package dev.moeglich.huffmangui;

import javax.swing.*;
import javax.swing.undo.*;
import java.awt.event.*;

public class UndoableTextArea extends JTextArea {
    private UndoManager undoManager;

    public UndoableTextArea() {
        super();
        undoManager = new UndoManager();
        getDocument().addUndoableEditListener(undoManager);
        initKeyBindings();
    }

    private void initKeyBindings() {
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_FOCUSED).put(undoKeyStroke, "Undo");
        getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_FOCUSED).put(redoKeyStroke, "Redo");
        getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
}