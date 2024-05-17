package dev.moeglich.huffmangui;

import java.awt.*;
import java.util.Base64;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import dev.moeglich.huffmanlib.Huffman;

public class ConvertPanel extends JPanel {
    private UndoableTextArea decodedText;
    private UndoableTextArea encodedText;
    private JLabel decodedLabel;
    private JLabel encodedLabel;

    public ConvertPanel() {
        this.setLayout(new BorderLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel grid = new JPanel(new GridBagLayout());
        this.add(grid, BorderLayout.CENTER);


        JLabel title = new JLabel("Convert Text to Huffman Code");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        grid.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 1;

        this.decodedLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        grid.add(this.decodedLabel, gbc);

        this.decodedText = new UndoableTextArea();
        this.decodedText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.decodedText.setLineWrap(true);
        this.decodedText.setWrapStyleWord(true);
        JScrollPane decodedScrollPane = new JScrollPane(this.decodedText);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        grid.add(decodedScrollPane, gbc);

        this.encodedLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        grid.add(this.encodedLabel, gbc);

        this.encodedText = new UndoableTextArea();
        this.encodedText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.encodedText.setLineWrap(true);
        this.encodedText.setWrapStyleWord(true);
        JScrollPane encodedScrollPane = new JScrollPane(this.encodedText);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        grid.add(encodedScrollPane, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        grid.add(buttonPanel, gbc);

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 5, 5, 5);

        JButton encodeButton = new JButton("Encode");
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonPanel.add(encodeButton, buttonGbc);

        JButton decodeButton = new JButton("Decode");
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonPanel.add(decodeButton, buttonGbc);

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