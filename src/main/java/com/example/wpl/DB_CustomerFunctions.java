
package com.example.wpl;

import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DB_CustomerFunctions {
    static private String url = "jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";

    public static void AddOrder(ObservableList<CustomerPlaceOrderController.Item> items, String email) {
        //String url = "jdbc:sqlserver://192.168.1.25\\SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";
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

        public DealInfo(int dealId, String orderDate, String dealStatus, String dealDescription) {
            this.dealId = dealId;
            this.orderDate = orderDate;
            this.dealStatus = dealStatus;
            this.dealDescription = dealDescription;
        }

        // Getters
        public int getDealId() {
            return dealId;
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

    public static ArrayList<DealInfo> viewOrders(String email) {
        //System.out.print('\n' + "we inside view order" + email+'\n');
        int CustomerID = getCustomerIDByEmail(email);
       // System.out.print("Customer ID " + CustomerID);

        ArrayList<DealInfo> DealInf = new ArrayList<DealInfo>();
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM Deal where CustomerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, CustomerID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while(resultSet.next()){
                        int dealId = resultSet.getInt("DealID");
                        String orderDate = resultSet.getString("DealDate");
                        String dealStatus = resultSet.getString("Order_Status");
                        String dealDescription = resultSet.getString("DealDescription");
                        DealInf.add(new DealInfo(dealId,orderDate,dealStatus,dealDescription));
                        //System.out.print('\n' + dealId + "\n" + orderDate + "\n" + dealStatus + "\n" + dealDescription+'\n');
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DealInf;
    }

}