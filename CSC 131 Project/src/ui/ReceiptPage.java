package ui;

import model.User;
import model.Item;
import model.Receipt;
import service.ReceiptService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ReceiptPage implements ActionListener{

    JFrame frame = new JFrame();
    JTextField searchField = new JTextField();
    JButton searchButton = new JButton("Search");
    JButton addButton = new JButton("+ Add Receipt");
    JPanel listPanel = new JPanel();
    ReceiptService receiptService = new ReceiptService();
    List<Receipt> allReceipts = new ArrayList<>();
    User currentUser;

    ReceiptPage(User user){
        this.currentUser = user;

        searchButton.addActionListener(this);
        addButton.addActionListener(this);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchButton, BorderLayout.EAST);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JPanel bottom = new JPanel();
        bottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        addButton.setPreferredSize(new Dimension(460, 40));
        bottom.add(addButton);

        frame.setTitle("Receipts - " + user.getName());
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
    	
    	if (e.getSource() == addButton){
            showAddDialog();
        }
    	
    	if (e.getSource() == searchButton){
    	    String query = searchField.getText().trim().toLowerCase();
    	    if (query.isEmpty()){
    	        refreshList(allReceipts);
    	        return;
    	    }
    	    List<Receipt> filtered = new ArrayList<>();
    	    for (Receipt r : allReceipts) {
    	        if (r.getName().toLowerCase().contains(query) ||
    	            String.valueOf(r.getReceiptID()).contains(query)){
    	            filtered.add(r);
    	        }
    	    }
    	    refreshList(filtered);
    	}
    	
    }
    
    void showAddDialog(){

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

        //Layouts
        nameLabel.setBounds(20, 20, 200, 25);
        nameField.setBounds(20, 45, 330, 25);
        dateLabel.setBounds(20, 80, 200, 25);
        dateField.setBounds(20, 105, 330, 25);
        itemsLabel.setBounds(20, 140, 200, 25);
        itemsArea.setBounds(20, 165, 330, 120);
        messageLabel.setBounds(20, 295, 330, 25);
        submitButton.setBounds(120, 330, 150, 30);

        messageLabel.setForeground(Color.RED);
        itemsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //Add components to dialog
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(itemsLabel);
        dialog.add(itemsArea);
        dialog.add(messageLabel);
        dialog.add(submitButton);
        
        //When creating receipting button is clicked
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String itemsText = itemsArea.getText().trim();

            //Checks for empty fields
            if (name.isEmpty() || date.isEmpty() || itemsText.isEmpty()){
                messageLabel.setText("All fields are required.");
                return;
            }

            //Parse items and validate each line
            List<Item> itemList = new ArrayList<>();
            for (String line : itemsText.split("\n")) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    messageLabel.setText("Invalid format " + line.trim());
                    return;
                }
                
                //Converts the values to proper type
                try{
                    itemList.add(new Item(parts[0].trim(), Integer.parseInt(parts[1].trim()), Double.parseDouble(parts[2].trim())));
                //Error message if conversion fails
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Invalid number in " + line.trim());
                    return;
                }
            }

            //Created the receipt
            Receipt receipt = receiptService.createReceipt(currentUser, new ArrayList<>(), name, date);
            
            //Add items to receipt
            for (Item item : itemList){
                receiptService.addItemToReceipt(receipt, item);
            }
            
            //Adds new receipt on top
            allReceipts.add(0, receipt);
            refreshList(allReceipts);
            dialog.dispose();
        });
        
        dialog.setVisible(true);

    }
    
    
    /* Refreshes page when searching or adding receipts */
    void refreshList(List<Receipt> receipts){

        listPanel.removeAll();

        for (Receipt r : receipts){

        	//Creates a card for each receipt
            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            card.setBackground(Color.WHITE);

            //Labels for receipt
            JLabel titleLabel = new JLabel(r.getName());
            JLabel subLabel = new JLabel("ID: " + r.getReceiptID() + "  |  " + r.getDate() + "  |  $" + String.format("%.2f", r.itemSum()));

            //Add item summary to receipt
            String itemNames = "";
            for (Item item : r.getItems()){
                itemNames += item.getName() + " x" + item.getCount() + "  ";
            }
            JLabel itemsLabel = new JLabel("Items: " + itemNames);

            //Layout
            titleLabel.setBounds(10, 5, 400, 20);
            subLabel.setBounds(10, 28, 400, 20);
            itemsLabel.setBounds(10, 51, 400, 20);

            titleLabel.setFont(new Font(null, Font.BOLD, 14));
            subLabel.setFont(new Font(null, Font.PLAIN, 11));
            subLabel.setForeground(Color.GRAY);
            itemsLabel.setFont(new Font(null, Font.PLAIN, 11));
            itemsLabel.setForeground(Color.GRAY);

            //Add components to card
            card.add(titleLabel);
            card.add(subLabel);
            card.add(itemsLabel);
            //Add card to list panel
            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(5));
        }

        //Updates the UI
        listPanel.revalidate();
        listPanel.repaint();
    }
}
