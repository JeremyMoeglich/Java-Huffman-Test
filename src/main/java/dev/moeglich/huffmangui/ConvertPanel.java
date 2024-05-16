package dev.moeglich.huffmangui;

import java.awt.*;
import java.util.Base64;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import dev.moeglich.huffmanlib.Huffman;
import net.miginfocom.swing.MigLayout;

public class ConvertPanel extends JPanel {
    private UndoableTextArea decodedText;
    private UndoableTextArea encodedText;
    private JLabel decodedLabel;
    private JLabel encodedLabel;

    public ConvertPanel() {
        // Use MigLayout instead of BorderLayout and GridBagLayout
        this.setLayout(new BorderLayout());

        JPanel grid = new JPanel(new MigLayout("fillx", "[right]rel[grow, fill]", "[]10[]10[]10[]"));
        this.add(grid, BorderLayout.CENTER);

        JLabel title = new JLabel("Convert Text to Huffman Code");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        grid.add(title, "span, center, wrap");

        this.decodedLabel = new JLabel();
        grid.add(this.decodedLabel, "right");

        this.decodedText = new UndoableTextArea();
        this.decodedText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.decodedText.setLineWrap(true);
        this.decodedText.setWrapStyleWord(true);
        JScrollPane decodedScrollPane = new JScrollPane(this.decodedText);
        grid.add(decodedScrollPane, "grow, wrap");

        this.encodedLabel = new JLabel();
        grid.add(this.encodedLabel, "right");

        this.encodedText = new UndoableTextArea();
        this.encodedText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.encodedText.setLineWrap(true);
        this.encodedText.setWrapStyleWord(true);
        JScrollPane encodedScrollPane = new JScrollPane(this.encodedText);
        grid.add(encodedScrollPane, "grow, wrap");

        JPanel buttonPanel = new JPanel(new MigLayout("", "[][]", "[]"));
        grid.add(buttonPanel, "span, center, wrap");

        JButton encodeButton = new JButton("Encode");
        buttonPanel.add(encodeButton, "cell 0 0");

        JButton decodeButton = new JButton("Decode");
        buttonPanel.add(decodeButton, "cell 1 0");

        encodeButton.addActionListener(e -> {
            String decoded = decodedText.getText();
            String encoded = Huffman.encode_to_base64(decoded);
            encodedText.setText(encoded);
            this.updateLabels();
        });

        decodeButton.addActionListener(e -> {
            String encoded = encodedText.getText();
            String decoded = "";
            try {
                decoded = Huffman.decode_from_base64(encoded);
            } catch (Exception ex) {
                decoded = "Error";
            }
            decodedText.setText(decoded);
            this.updateLabels();
        });

        this.decodedText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }
        });

        this.encodedText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateLabels();
            }
        });

        String initialText = "This is a long test sentence to see how the Huffman encoding works.";
        decodedText.setText(initialText);
        encodedText.setText(Huffman.encode_to_base64(initialText));

        this.updateLabels();
    }

    static String createLabel(String labelText, Integer size) {
        if (size == null) {
            return "<html>" + labelText + "<br>Not valid base64</html>";
        }
        return "<html>" + labelText + "<br>" + size + " bytes</html>";
    }

    void updateLabels() {
        String decoded = decodedText.getText();
        String encoded = encodedText.getText();
        decodedLabel.setText(createLabel("Decoded Text", decoded.length()));

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encoded);
            encodedLabel.setText(createLabel("Encoded Text", decodedBytes.length));
        } catch (Exception e) {
            encodedLabel.setText(createLabel("Encoded Text", null));
        }
    }
}
