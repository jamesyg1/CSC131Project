package ui;

import model.User;
import model.Item;
import model.Receipt;
import service.ReceiptService;
import service.DatabaseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ReceiptPage implements ActionListener {

    JFrame frame = new JFrame();
    JTextField searchField = new JTextField();
    JButton searchButton = new JButton("Search");
    JButton addButton = new JButton("+ Add Receipt");
    JButton logoutButton = new JButton("Logout");
    JPanel listPanel = new JPanel();
    ReceiptService receiptService = new ReceiptService();
    List<Receipt> allReceipts = new ArrayList<>();
    User currentUser;

    ReceiptPage(User user) {
        this.currentUser = user;

        allReceipts = DatabaseService.getInstance().loadReceiptsForUser(user);

        searchButton.addActionListener(this);
        addButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
        top.add(logoutButton, BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchButton, BorderLayout.EAST);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JPanel bottom = new JPanel();
        bottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        addButton.setPreferredSize(new Dimension(460, 40));
        bottom.add(addButton);

        frame.setTitle("Receipts - " + user.getName());
        frame.setSize(500, 650);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setVisible(true);

        refreshList(allReceipts);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addButton) {
            showAddDialog();
        }

        if (e.getSource() == searchButton) {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                refreshList(allReceipts);
                return;
            }
            List<Receipt> filtered = new ArrayList<>();
            for (Receipt r : allReceipts) {
                if (r.getName().toLowerCase().contains(query) ||
                        String.valueOf(r.getReceiptID()).contains(query)) {
                    filtered.add(r);
                }
            }
            refreshList(filtered);
        }

        if (e.getSource() == logoutButton) {
            frame.dispose();
            new LoginPage(UserInfo.getInstance().getLoginInfo());
        }
    }

    void showAddDialog() {

        JDialog dialog = new JDialog(frame, "New Receipt", true);
        dialog.setSize(380, 420);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(frame);

        JLabel nameLabel = new JLabel("Receipt Name:");
        JTextField nameField = new JTextField();
        JLabel dateLabel = new JLabel("Date (MM/DD/YYYY):");
        JTextField dateField = new JTextField();
        JLabel itemsLabel = new JLabel("Items (Name, Count, Price):");
        JTextArea itemsArea = new JTextArea();
        JLabel messageLabel = new JLabel();
        JButton submitButton = new JButton("Create Receipt");

        nameLabel.setBounds(20, 20, 200, 25);
        nameField.setBounds(20, 45, 330, 25);
        dateLabel.setBounds(20, 80, 200, 25);
        dateField.setBounds(20, 105, 330, 25);
        itemsLabel.setBounds(20, 140, 200, 25);
        itemsArea.setBounds(20, 165, 330, 120);
        messageLabel.setBounds(20, 295, 330, 25);
        submitButton.setBounds(115, 330, 150, 30);

        messageLabel.setForeground(Color.RED);
        itemsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(itemsLabel);
        dialog.add(itemsArea);
        dialog.add(messageLabel);
        dialog.add(submitButton);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String itemsText = itemsArea.getText().trim();

            if (name.isEmpty() || date.isEmpty() || itemsText.isEmpty()) {
                messageLabel.setText("All fields are required.");
                return;
            }

            List<Item> itemList = new ArrayList<>();
            for (String line : itemsText.split("\\r?\\n")) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    messageLabel.setText("Invalid format: " + line.trim());
                    return;
                }
                try {
                    itemList.add(new Item(
                            parts[0].trim(),
                            Integer.parseInt(parts[1].trim()),
                            Double.parseDouble(parts[2].trim())
                    ));
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Invalid number in: " + line.trim());
                    return;
                }
            }

            int code = DatabaseService.getInstance().insertReceipt(currentUser, name, date, itemList);
            if (code == -1) {
                messageLabel.setText("Database error. Receipt not saved.");
                return;
            }

            Receipt receipt = receiptService.createReceipt(currentUser, new ArrayList<>(), name, date);
            receipt.setReceiptID(code);
            for (Item item : itemList) {
                receiptService.addItemToReceipt(receipt, item);
            }

            allReceipts.add(0, receipt);
            refreshList(allReceipts);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    void refreshList(List<Receipt> receipts) {

        listPanel.removeAll();

        for (Receipt r : receipts) {

            int cardHeight = 50 + (r.getItems().size() * 18);

            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
            card.setPreferredSize(new Dimension(460, cardHeight));
            card.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel(r.getName());
            titleLabel.setBounds(10, 5, 400, 20);
            titleLabel.setFont(new Font(null, Font.BOLD, 14));

            JLabel subLabel = new JLabel(
                    "ID: " + r.getReceiptID() +
                            "  |  " + r.getDate() +
                            "  |  Total: $" + String.format("%.2f", r.itemSum())
            );
            subLabel.setBounds(10, 26, 450, 18);
            subLabel.setFont(new Font(null, Font.PLAIN, 11));
            subLabel.setForeground(Color.GRAY);

            card.add(titleLabel);
            card.add(subLabel);

            int yPos = 46;
            for (Item item : r.getItems()) {
                JLabel itemLabel = new JLabel(
                        "  • " + item.getName() +
                                "   x" + item.getCount() +
                                "   $" + String.format("%.2f", item.getPrice())
                );
                itemLabel.setBounds(10, yPos, 440, 16);
                itemLabel.setFont(new Font(null, Font.PLAIN, 11));
                itemLabel.setForeground(new Color(80, 80, 80));
                card.add(itemLabel);
                yPos += 18;
            }

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(6));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}