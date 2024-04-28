package com.example.wpl;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

 public class DB_Admin {
    static protected String url = "jdbc:sqlserver://192.168.1.25\\SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";

    public static void AddOrder(ObservableList<CustomerPlaceOrderController.Item> items, String email) {
        String url = "jdbc:sqlserver://192.168.1.25\\SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";
        System.out.print("this is : " + email);


        int Cust_id = getCustomerIDByEmail(email);
        int DealId = RandomGenerator.generateRandomNumber();

        AddDeal(DealId, Cust_id);
        int biltyID = AddGoodsBilty(DealId);
        System.out.print("\nbilty add hogai? " + biltyID + '\n');

        int size = items.size();

        // Print the size of the list
        System.out.println("\nSize of the list: " + size + '\n');

        for (CustomerPlaceOrderController.Item item : items) {

            String name = item.getItemName();
            int itemID = getItemIdByName(name);
            System.out.print("Did Item exist = " + itemID + '\n');
            if (itemID == -1) {
                itemID = RandomGenerator.generateRandomNumber();
                AddItem(itemID, item.getItemName(), item.getItemWeight());
                System.out.print("if not = " + itemID + '\n');
            }
            int cargoID = RandomGenerator.generateRandomNumber();
            AddCargo(cargoID, itemID, biltyID, item.getItemQuantity());
        }

    }

    public static void AddCargo(int cargoId, int itemId, int biltyId, int quantity) {

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "{CALL AddCargo(?, ?, ?, ?)}";
            try (PreparedStatement statement = connection.prepareCall(sql)) {
                statement.setInt(1, cargoId);
                statement.setInt(2, itemId);
                statement.setInt(3, biltyId);
                statement.setInt(4, quantity);
                statement.execute();
                System.out.println("Cargo added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getItemIdByName(String itemName) {
        int itemId = -1;
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT ItemId FROM Items WHERE ItemName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, itemName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        itemId = resultSet.getInt("ItemId");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemId;
    }

    public static int getCustomerIDByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "EXEC GetCustomerIDByEmail ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("CustomerID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if customer ID is not found or an error occurs
    }

    public static void AddDeal(int dealID, int customerID) {
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "{CALL AddDeal(?, ?)}";
            try (PreparedStatement statement = connection.prepareCall(sql)) {
                statement.setInt(1, dealID);
                statement.setInt(2, customerID);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int AddGoodsBilty(int dealID) {
        int biltyId = -1;
        try (Connection connection = DriverManager.getConnection(url)) {
            // Define the SQL query with the output parameter
            String sql = "{CALL AddBilty(?, ?)}";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                // Set the input parameter
                statement.setInt(1, dealID);

                // Register the output parameter and its SQL type
                statement.registerOutParameter(2, Types.INTEGER);

                // Execute the statement
                statement.execute();

                // Retrieve the output parameter value
                biltyId = statement.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return biltyId;
    }

    public static void AddItem(int itemID, String itemName, double weight) {
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "{CALL AddItems(?, ?, ?)}";
            try (PreparedStatement statement = connection.prepareCall(sql)) {
                statement.setInt(1, itemID);
                statement.setString(2, itemName);
                statement.setDouble(3, weight);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public static class DealInfo {
         private int dealId;
         private String orderDate;
         private String dealStatus;
         private String dealDescription;
         private int customerID;
         private String customerName; // New field

         public DealInfo(int customerID, String customerName, int dealId, String orderDate, String dealStatus, String dealDescription) {
             this.customerID = customerID;
             this.customerName = customerName; // Initialize new field
             this.dealId = dealId;
             this.orderDate = orderDate;
             this.dealStatus = dealStatus;
             this.dealDescription = dealDescription;
         }

         // Getters
         public int getDealId() {
             return dealId;
         }
         public int getCustomerId() {
             return customerID;
         }
         public String getCustomerName() {
             return customerName;
         }
         public String getOrderDate() {
             return orderDate;
         }
         public String getDealStatus() {
             return dealStatus;
         }
         public String getDealDescription() {
             return dealDescription;
         }
     }
    public static ArrayList<DB_Admin.DealInfo> viewOrders() {

        ArrayList<DB_Admin.DealInfo> DealInf = new ArrayList<DB_Admin.DealInfo>();
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT Customer.CustomerName,Deal.* FROM Deal inner join Customer on Customer.CustomerID = Deal.customerId";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while(resultSet.next()){
                        String CustomerName = resultSet.getString("CustomerName");
                        int CustomerId = resultSet.getInt("CustomerID");
                        int dealId = resultSet.getInt("DealID");
                        String orderDate = resultSet.getString("DealDate");
                        String dealStatus = resultSet.getString("Order_Status");
                        String dealDescription = resultSet.getString("DealDescription");
                        DealInf.add(new DB_Admin.DealInfo(CustomerId,CustomerName,dealId,orderDate,dealStatus,dealDescription));
                        //System.out.print('\n' + dealId + "\n" + orderDate + "\n" + dealStatus + "\n" + dealDescription+'\n');
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DealInf;
    }
     public static ArrayList<AdminOrderDetailsController.Item> getOrderItemDetails(int dealId) {
         ArrayList<AdminOrderDetailsController.Item> items = new ArrayList<>();
         String sql = "SELECT I.ItemName, C.Quantity, I.Weigh, CT.NumberPlate " +
                 "FROM Items I " +
                 "JOIN Cargo C ON I.ItemID = C.ItemID " +
                 "JOIN Goods_bilty GB ON C.BiltyID = GB.BiltyID " +
                 "JOIN Deal D ON GB.DealID = D.DealID " +
                 "LEFT JOIN Cargo_Transport CT ON C.CargoID = CT.CargoID " +
                 "WHERE D.DealID = ?";

         try (Connection connection = DriverManager.getConnection(DB_CustomerFunctions.url);
              PreparedStatement statement = connection.prepareStatement(sql)) {

             statement.setInt(1, dealId);
             ResultSet resultSet = statement.executeQuery();

             while(resultSet.next()) {
                 String itemName = resultSet.getString("ItemName");
                 int quantity = resultSet.getInt("Quantity");
                 double weight = resultSet.getDouble("Weigh");
                 String vehicleAssignment = resultSet.getString("NumberPlate"); // This can be null if LEFT JOIN doesn't find a match

                 items.add(new AdminOrderDetailsController.Item(itemName, quantity, weight));
                 //System.out.print("item name " + itemName + '\n' + vehicleAssignment + '\n'  + quantity + '\n' + weight + '\n');
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }

         return items;
     }
}
