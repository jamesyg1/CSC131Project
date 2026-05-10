package ui;

import model.Item;
import model.Receipt;
import service.DatabaseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPage implements ActionListener {

    JFrame frame = new JFrame();
    JTextField idField = new JTextField();
    JButton searchButton = new JButton("Find Receipt");
    JButton backButton = new JButton("Back to Login");
    JPanel resultPanel = new JPanel();

    SearchPage() {
        searchButton.addActionListener(this);
        backButton.addActionListener(this);

        JLabel title = new JLabel("Receipt Lookup");
        title.setFont(new Font(null, Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel hint = new JLabel("Enter a 5-digit Receipt ID:");
        hint.setFont(new Font(null, Font.PLAIN, 13));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(20, 30, 10, 30));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        idField.setMaximumSize(new Dimension(300, 35));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(title);
        top.add(Box.createVerticalStrut(15));
        top.add(hint);
        top.add(Box.createVerticalStrut(8));
        top.add(idField);
        top.add(Box.createVerticalStrut(10));
        top.add(searchButton);
        top.add(Box.createVerticalStrut(6));
        top.add(backButton);

        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        frame.setTitle("Receipt Lookup");
        frame.setSize(480, 550);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backButton) {
            frame.dispose();
            new LoginPage(UserInfo.getInstance().getLoginInfo());
        }

        if (e.getSource() == searchButton) {
            String input = idField.getText().trim();

            if (input.isEmpty()) {
                showMessage("Please enter a Receipt ID.", Color.RED);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                showMessage("Receipt ID must be a number.", Color.RED);
                return;
            }

            Receipt found = DatabaseService.getInstance().loadReceiptByID(id);
            if (found == null) {
                showMessage("No receipt found with ID: " + String.format("%05d", id), Color.RED);
                return;
            }

            displayReceipt(found);
        }
    }

    private void showMessage(String msg, Color color) {
        resultPanel.removeAll();
        JLabel lbl = new JLabel(msg);
        lbl.setForeground(color);
        lbl.setFont(new Font(null, Font.BOLD, 13));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(lbl);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void displayReceipt(Receipt r) {
        resultPanel.removeAll();

        JLabel nameLabel = new JLabel(r.getName());
        nameLabel.setFont(new Font(null, Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel idLabel = new JLabel("ID: " + String.format("%05d", r.getReceiptID()));
        idLabel.setFont(new Font(null, Font.PLAIN, 12));
        idLabel.setForeground(Color.GRAY);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + r.getDate());
        dateLabel.setFont(new Font(null, Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(nameLabel);
        resultPanel.add(Box.createVerticalStrut(4));
        resultPanel.add(idLabel);
        resultPanel.add(dateLabel);
        resultPanel.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        resultPanel.add(sep);
        resultPanel.add(Box.createVerticalStrut(8));

        JLabel header = new JLabel(String.format("%-25s %8s %10s", "Item", "Qty", "Price"));
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        resultPanel.add(header);
        resultPanel.add(Box.createVerticalStrut(4));

        for (Item item : r.getItems()) {
            JLabel itemLabel = new JLabel(String.format(
                    "%-25s %8s %10s",
                    item.getName(),
                    "x" + item.getCount(),
                    "$" + String.format("%.2f", item.getPrice())
            ));
            itemLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            resultPanel.add(itemLabel);
            resultPanel.add(Box.createVerticalStrut(2));
        }

        resultPanel.add(Box.createVerticalStrut(8));
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        resultPanel.add(sep2);
        resultPanel.add(Box.createVerticalStrut(6));

        JLabel totalLabel = new JLabel("Total:  $" + String.format("%.2f", r.itemSum()));
        totalLabel.setFont(new Font(null, Font.BOLD, 14));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(totalLabel);

        resultPanel.revalidate();
        resultPanel.repaint();
    }
}