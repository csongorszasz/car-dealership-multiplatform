package edu.bbte.idde.scim2304.desktop;

import edu.bbte.idde.scim2304.backend.model.Advert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvertDialog extends JDialog {
    private final JTextField nameField;
    private final JTextField brandField;
    private final JTextField yearField;
    private final JTextField priceField;
    private boolean accepted;

    public AdvertDialog(Component parentComponent, Advert advert, String title) {
        super();
        setTitle(title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(200, 200);
        setLocationRelativeTo(parentComponent);
        setLayout(new GridLayout(5, 2));

        nameField = new JTextField();
        brandField = new JTextField();
        yearField = new JTextField();
        priceField = new JTextField();

        if (advert != null) {
            nameField.setText(advert.getName());
            brandField.setText(advert.getBrand());
            yearField.setText(Integer.toString(advert.getYear()));
            priceField.setText(Float.toString(advert.getPrice()));
        }

        add(new JLabel("Name"));
        add(nameField);
        add(new JLabel("Brand"));
        add(brandField);
        add(new JLabel("Year"));
        add(yearField);
        add(new JLabel("Price"));
        add(priceField);

        var okButton = new JButton("OK");
        okButton.addActionListener(new OkButtonListener());

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());

        add(cancelButton);
        add(okButton);

        setVisible(true);
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    public String getBrand() {
        return brandField.getText();
    }

    public int getYear() {
        return Integer.parseInt(yearField.getText());
    }

    public float getPrice() {
        return Float.parseFloat(priceField.getText());
    }

    private void validateName() {
        if (nameField.getText().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    private void validateBrand() {
        if (brandField.getText().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
    }

    private void validateYear() {
        if (yearField.getText().isEmpty()) {
            throw new IllegalArgumentException("Year cannot be empty");
        }
        // is an integer
        try {
            Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year must be a number");
        }
    }

    private void validatePrice() {
        if (priceField.getText().isEmpty()) {
            throw new IllegalArgumentException("Price cannot be empty");
        }
        // is a float
        try {
            Float.parseFloat(priceField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price must be a number");
        }
    }

    private final class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // input validation
                validateName();
                validateBrand();
                validateYear();
                validatePrice();

                // close dialog
                setVisible(false);
                dispose();

                accepted = true;
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(AdvertDialog.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                accepted = false;
            }
        }
    }

    private final class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }
}

