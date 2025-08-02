package edu.bbte.idde.scim2304.desktop;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.service.ServiceFactory;
import edu.bbte.idde.scim2304.backend.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.backend.service.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DealershipApp extends JFrame {
    private static final Logger LOG = LoggerFactory.getLogger(DealershipApp.class);

    private final List<Advert> adverts;
    private DefaultTableModel tableModel;
    private JTable table;

    public DealershipApp() {
        super();

        LOG.info("Starting app");

        adverts = new ArrayList<>();
        setTitle("Dealership Management");
        setSize(680, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        var buttons = createCUDButtonPanel();
        var scrollPane = new JScrollPane(createAdvertsTable());

        add(scrollPane, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        loadAllAdverts();

        LOG.info("App initialized");
    }

    private void loadAllAdverts() {
        adverts.clear();
        tableModel.setRowCount(0);
        ServiceFactory.getAdvertService().findAllAdverts().forEach(advert -> {
            adverts.add(advert);
            tableModel.addRow(new Object[]{advert.getId(), advert.getName(), advert.getBrand(),
                    advert.getYear(), advert.getPrice(), advert.getUploadDate()});
        });
    }

    private JTable createAdvertsTable() {
        String[] columnNames = {"ID", "Name", "Brand", "Year", "Price", "Upload Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        return table;
    }

    private JPanel createCUDButtonPanel() {
        var panel = new JPanel(new GridLayout(1, 3));

        var createButton = new JButton("Create");
        panel.add(createButton);
        createButton.addActionListener(new CreateAction(this));

        var editButton = new JButton("Edit");
        panel.add(editButton);
        editButton.addActionListener(new EditAction(this));

        var deleteButton = new JButton("Delete");
        panel.add(deleteButton);
        deleteButton.addActionListener(new DeleteAction(this));

        return panel;
    }

    private class CreateAction implements ActionListener {
        final JFrame parentComponent;

        public CreateAction(JFrame parentComponent) {
            this.parentComponent = parentComponent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var dialog = new AdvertDialog(parentComponent, null, "Create Advert");
            if (dialog.isAccepted()) {
                Advert newAdvert;
                try {
                    newAdvert = ServiceFactory.getAdvertService().createAdvert(new Advert(dialog.getName(),
                            dialog.getBrand(), dialog.getYear(), dialog.getPrice(), new Date()));
                } catch (FailedCreateException ex) {
                    LOG.error("Data access error", ex);
                    JOptionPane.showMessageDialog(parentComponent, "Data access error", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                adverts.add(newAdvert);
                tableModel.addRow(new Object[]{newAdvert.getId(), newAdvert.getName(), newAdvert.getBrand(),
                        newAdvert.getYear(), newAdvert.getPrice(), newAdvert.getUploadDate()});
            }
        }
    }

    private class EditAction implements ActionListener {
        final JFrame parentComponent;

        public EditAction(JFrame parentComponent) {
            this.parentComponent = parentComponent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentComponent, "No row selected for editing");
                return;
            }
            var advert = adverts.get(selectedRow);
            var dialog = new AdvertDialog(parentComponent, advert, "Edit Advert");
            if (dialog.isAccepted()) {
                var newAdvert = new Advert(dialog.getName(), dialog.getBrand(), dialog.getYear(), dialog.getPrice(),
                        new Date());
                newAdvert.setId(advert.getId());  // needed to avoid hashCode error
                try {
                    ServiceFactory.getAdvertService().updateAdvert(newAdvert.getId(), newAdvert); // update data source
                } catch (NotFoundException exception) {
                    LOG.error("Data access error", exception);
                    JOptionPane.showMessageDialog(parentComponent, "Data access error", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                adverts.set(selectedRow, newAdvert); // update list for UI
                // update table visually
                tableModel.setValueAt(newAdvert.getName(), selectedRow, 1);
                tableModel.setValueAt(newAdvert.getBrand(), selectedRow, 2);
                tableModel.setValueAt(newAdvert.getYear(), selectedRow, 3);
                tableModel.setValueAt(newAdvert.getPrice(), selectedRow, 4);
            }
        }
    }

    private class DeleteAction implements ActionListener {
        final JFrame parentComponent;

        public DeleteAction(JFrame parentComponent) {
            this.parentComponent = parentComponent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentComponent, "No row selected for deletion");
                return;
            }
            var advert = adverts.get(selectedRow);
            int response = JOptionPane.showConfirmDialog(
                    parentComponent,
                    String.format("Are you sure you want to delete row %d", selectedRow + 1),
                    "Delete Advert",
                    JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION) {
                try {
                    ServiceFactory.getAdvertService().deleteAdvert(advert.getId()); // delete from data source
                } catch (NotFoundException exception) {
                    LOG.error("Data access error", exception);
                    JOptionPane.showMessageDialog(parentComponent, "Data access error", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                adverts.remove(selectedRow); // delete from UI list
                tableModel.removeRow(selectedRow); // remove from table visually
            }
        }
    }

    public static void main(String[] args) {
        var app = new DealershipApp();
        app.setVisible(true);
    }
}
