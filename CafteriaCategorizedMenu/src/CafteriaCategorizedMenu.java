
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CafteriaCategorizedMenu extends JFrame implements ActionListener {
    // Maps category -> (item name -> price)
    LinkedHashMap<String, LinkedHashMap<String, Integer>> menuMap = new LinkedHashMap<>();
    // For Drinks subcategories
    LinkedHashMap<String, LinkedHashMap<String, Integer>> drinksSubcategories = new LinkedHashMap<>();
    // To store all checkboxes to check selection easily
    HashMap<JCheckBox, String> checkboxToItem = new HashMap<>();
    HashMap<JCheckBox, String> checkboxToCategory = new HashMap<>();
    HashMap<String, Integer> itemToPrice = new HashMap<>();

    JTextPane outputPane;
    JButton calculateBtn;

    public CafteriaCategorizedMenu() {
        setTitle("Welcome to The Tasty Corner");
        setSize(600, 600);
        setLayout(new BorderLayout(10, 10));

        // Motto panel to ensure it shows up well
        JPanel mottoPanel = new JPanel(new BorderLayout());
        mottoPanel.setBackground(new Color(255, 248, 220)); // light cream color
        mottoPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
        JLabel motto = new JLabel("Delicious Food & Drinks to Brighten Your Day!", SwingConstants.CENTER);
        motto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        motto.setForeground(new Color(59, 89, 182)); // nice blue
        mottoPanel.add(motto, BorderLayout.CENTER);
        add(mottoPanel, BorderLayout.NORTH);

        // Setup Menu Data
        // Food Items
        LinkedHashMap<String, Integer> foodItems = new LinkedHashMap<>();
        foodItems.put("Burger", 5);
        foodItems.put("Pizza", 8);
        foodItems.put("Pasta", 7);
        foodItems.put("Sandwich", 4);
        foodItems.put("Fries", 2);
        foodItems.put("Salad", 6);
        foodItems.put("Steak", 10);

        // Drinks subcategories
        LinkedHashMap<String, Integer> hotDrinks = new LinkedHashMap<>();
        hotDrinks.put("Coffee", 3);
        hotDrinks.put("Tea", 2);
        hotDrinks.put("Hot Chocolate", 4);

        LinkedHashMap<String, Integer> softDrinks = new LinkedHashMap<>();
        softDrinks.put("Soda", 2);
        softDrinks.put("Juice", 3);
        softDrinks.put("Water", 1);

        // Desserts
        LinkedHashMap<String, Integer> desserts = new LinkedHashMap<>();
        desserts.put("Ice Cream", 4);
        desserts.put("Cake", 5);
        desserts.put("Doughnut", 2);

        menuMap.put("Food", foodItems);
        drinksSubcategories.put("Hot Drinks", hotDrinks);
        drinksSubcategories.put("Soft Drinks", softDrinks);
        menuMap.put("Dessert", desserts);

        // Tabs for Food, Drinks, Dessert
        JTabbedPane tabbedPane = new JTabbedPane();

        // Food Tab
        tabbedPane.addTab("Food", createItemPanel(foodItems, "Food"));

        // Drinks Tab with Hot Drinks and Soft Drinks as sub-tabs
        JPanel drinksPanel = new JPanel(new BorderLayout());
        JTabbedPane drinksTabs = new JTabbedPane();
        drinksTabs.addTab("Hot Drinks", createItemPanel(hotDrinks, "Drinks"));
        drinksTabs.addTab("Soft Drinks", createItemPanel(softDrinks, "Drinks"));
        drinksPanel.add(drinksTabs, BorderLayout.CENTER);
        tabbedPane.addTab("Drinks", drinksPanel);

        // Dessert Tab
        tabbedPane.addTab("Dessert", createItemPanel(desserts, "Dessert"));

        add(tabbedPane, BorderLayout.CENTER);

        // Panel at SOUTH to hold button and outputPane (scrollPane)
        JPanel southPanel = new JPanel(new BorderLayout(5,5));
        calculateBtn = new JButton("Calculate Total");
        calculateBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        calculateBtn.setBackground(new Color(59, 89, 182));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFocusPainted(false);
        calculateBtn.addActionListener(this);
        southPanel.add(calculateBtn, BorderLayout.NORTH);


outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        outputPane.setBackground(new Color(245, 245, 245));
        outputPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        outputPane.setPreferredSize(new Dimension(580, 150));

        JScrollPane scrollPane = new JScrollPane(outputPane);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        southPanel.add(scrollPane, BorderLayout.CENTER);

        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createItemPanel(LinkedHashMap<String, Integer> items, String category) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            int price = entry.getValue();
            JCheckBox cb = new JCheckBox(itemName + " - $" + price);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(cb);
            checkboxToItem.put(cb, itemName);
            checkboxToCategory.put(cb, category);
            itemToPrice.put(itemName, price);
        }
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        StyledDocument doc = outputPane.getStyledDocument();
        outputPane.setText("");
        StyleContext sc = new StyleContext();
        Style headerStyle = sc.addStyle("Header", null);
        StyleConstants.setBold(headerStyle, true);
        StyleConstants.setFontSize(headerStyle, 18);
        StyleConstants.setForeground(headerStyle, new Color(59, 89, 182));

        Style itemStyle = sc.addStyle("Item", null);
        StyleConstants.setFontSize(itemStyle, 16);

        Style totalStyle = sc.addStyle("Total", null);
        StyleConstants.setBold(totalStyle, true);
        StyleConstants.setFontSize(totalStyle, 18);
        StyleConstants.setForeground(totalStyle, Color.RED);

        // Collect selected items by category
        Map<String, ArrayList<String>> selectedByCategory = new LinkedHashMap<>();
        int total = 0;

        try {
            for (JCheckBox cb : checkboxToItem.keySet()) {
                if (cb.isSelected()) {
                    String category = checkboxToCategory.get(cb);
                    String itemName = checkboxToItem.get(cb);
                    selectedByCategory.putIfAbsent(category, new ArrayList<>());
                    selectedByCategory.get(category).add(itemName);
                    total += itemToPrice.get(itemName);
                }
            }

            doc.insertString(doc.getLength(), "Selected Items:\n", headerStyle);
            if (selectedByCategory.isEmpty()) {
                doc.insertString(doc.getLength(), "No items selected.\n", itemStyle);
            } else {
                for (String category : selectedByCategory.keySet()) {
                    doc.insertString(doc.getLength(), category + ":\n", headerStyle);
                    for (String item : selectedByCategory.get(category)) {
                        int price = itemToPrice.get(item);
                        doc.insertString(doc.getLength(), "  " + item + " - $" + price + "\n", itemStyle);
                    }
                    doc.insertString(doc.getLength(), "\n", itemStyle);
                }
            }
            doc.insertString(doc.getLength(), "Total Price: $" + total, totalStyle);
        } catch (BadLocationException ex) {
            outputPane.setText("Error displaying result: " + ex.getMessage());
        } catch (Exception ex) {
            outputPane.setText("Error calculating total price: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CafteriaCategorizedMenu());
    }
}