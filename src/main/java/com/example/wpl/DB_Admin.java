package com.example.wpl;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DB_Admin {
     static private String url = "jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";

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

         try (Connection connection = DriverManager.getConnection(url);
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

     public static boolean AddVehicle(String numberPlate, int rentedBy) {
         try (Connection connection = DriverManager.getConnection(url)) {
             // Check if rentedBy exists in the database
             if (!checkTransportCompanyExists(connection, rentedBy)) {
                 System.out.println("Transport company with ID " + rentedBy + " does not exist.");
                 return false;
             }

             // Check if numberPlate exists in the Transport table
             if (!checkTransportExists(connection, numberPlate)) {
                 System.out.println("Transport with number plate " + numberPlate + "already exist.");
                 return false;
             }

             // Proceed with adding the vehicle
             String sql = "{CALL AddVehicle(?, ?)}";
             try (PreparedStatement statement = connection.prepareCall(sql)) {
                 statement.setString(1, numberPlate);
                 statement.setInt(2, rentedBy);
                 int affectedRows = statement.executeUpdate();
                 if (affectedRows > 0) {
                     System.out.println("Vehicle added successfully.");
                     return true; // Operation was successful
                 } else {
                     System.out.println("Failed to add vehicle.");
                     return false; // Operation failed
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
             return false; // Operation failed due to exception
         }
     }

     // Method to check if rentedBy exists in the database
     private static boolean checkTransportCompanyExists(Connection connection, int rentedBy) throws SQLException {
         String sql = "SELECT COUNT(*) FROM TransportCompany WHERE CompanyID = ?";
         try (PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setInt(1, rentedBy);
             try (ResultSet resultSet = statement.executeQuery()) {
                 if (resultSet.next()) {
                     if(resultSet.getInt(1) > 0)
                         return true;
                     else
                        return false ; // Return true if count > 0, indicating existence
                 }
             }
         }
         return false; // Return false if an error occurs
     }

     public static ArrayList<AdminTransportCompany.TransportCompany> getExistingTransportCompanies() {
         ArrayList<AdminTransportCompany.TransportCompany> existingCompanies = new ArrayList<>();
         String sql = "SELECT CompanyID, CompanyName, ContactNo FROM TransportCompany WHERE CompanyID != 0";

         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement(sql);
              ResultSet resultSet = statement.executeQuery()) {

             while (resultSet.next()) {
                 int companyId = resultSet.getInt("CompanyID");
                 String companyName = resultSet.getString("CompanyName");
                 String contactNumber = resultSet.getString("ContactNo");
                 existingCompanies.add(new AdminTransportCompany.TransportCompany(companyId, companyName, contactNumber));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return existingCompanies;
     }

     // Method to check if numberPlate exists in the Transport table
     private static boolean checkTransportExists(Connection connection, String numberPlate) throws SQLException {
         String sql = "SELECT COUNT(*) FROM Transport WHERE NumberPlate = ?";
         try (PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setString(1, numberPlate);
             try (ResultSet resultSet = statement.executeQuery()) {
                 if (resultSet.next()) {
                     if(resultSet.getInt(1) > 0)
                        return false;  // Return true if count > 0, indicating existence
                     else
                         return true;
                 }
             }
         }
         return false; // Return false if an error occurs
     }

     public static ArrayList<TransportController.Vehicle> getExistingVehicles() {
         ArrayList<TransportController.Vehicle> existingVehicles = new ArrayList<>();
         try (Connection connection = DriverManager.getConnection(url)) {
             String sql = "SELECT T.NumberPlate, T.RentedBy, Cargo_Transport.CargoID " +
                     "FROM Transport as T left join Cargo_Transport on Cargo_Transport.NumberPlate = T.NumberPlate";

             try (PreparedStatement statement = connection.prepareStatement(sql)) {
                 try (ResultSet resultSet = statement.executeQuery()) {
                     while (resultSet.next()) {
                         String numberPlate = resultSet.getString("NumberPlate");
                         int rentedBy = resultSet.getInt("RentedBy");
                         int cargoID = resultSet.getInt("CargoID"); // Assuming cargoID is an integer in the database
                         existingVehicles.add(new TransportController.Vehicle(numberPlate, rentedBy, cargoID));
                     }
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return existingVehicles;
     }
     public static boolean addTransportCompany(AdminTransportCompany.TransportCompany newCompany) {
         // Check if the phone number already exists
         if (phoneNumberExists(newCompany.getContactNumber())) {
             // If the phone number already exists, display an error message
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setHeaderText(null);
             alert.setContentText("Phone number already exists. Please use a different phone number.");
             alert.showAndWait();
             return false;
         }

         // Generate a new company ID by getting the last company ID and incrementing it by 1
         int newCompanyId = getLastCompanyId() + 1;

         // Insert the new company into the database
         String sql = "INSERT INTO TransportCompany (CompanyID, CompanyName, ContactNo) VALUES (?, ?, ?)";
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setInt(1, newCompanyId);
             statement.setString(2, newCompany.getCompanyName());
             statement.setString(3, newCompany.getContactNumber());
             statement.executeUpdate();
             newCompany.setCompanyID(newCompanyId);
             return true;
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         }
     }

     private static int getLastCompanyId() {
         int lastCompanyId = 0;
         String sql = "SELECT MAX(CompanyID) AS LastCompanyID FROM TransportCompany";
         try (Connection connection = DriverManager.getConnection(url);
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(sql)) {
             if (resultSet.next()) {
                 lastCompanyId = resultSet.getInt("LastCompanyID");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return lastCompanyId;
     }

     private static boolean phoneNumberExists(String phoneNumber) {
         String sql = "SELECT * FROM TransportCompany WHERE ContactNo = ?";
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setString(1, phoneNumber);
             try (ResultSet resultSet = statement.executeQuery()) {
                 return resultSet.next(); // Returns true if phone number exists
             }
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         }
     }
     public static boolean addCompanyTransaction(AdminTransactionCompanyController.TransactionCompany transaction) throws SQLException {
         // Check if the dealId exists in the Deal table

         if (!dealExists(transaction.getDealId())) {
             System.out.println("Deal ID does not exist.");
             return false;
         }

         // Check if the companyId exists in the TransportCompany table
         if (!companyExists(transaction.getCompanyId())) {
             System.out.println("Company ID does not exist.");
             return false;
         }

         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("INSERT INTO TransactionCompany (TID, DealID, CompanyID, TransactionAmount, DateAndTime, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?)"))
         {
             statement.setInt(1, transaction.getTransactionId());
             statement.setInt(2, transaction.getDealId());
             statement.setInt(3, transaction.getCompanyId());
             statement.setString(4, transaction.getTransactionAmount());
             statement.setString(5, transaction.getDateAndTime());
             statement.setString(6, transaction.getPaymentMethod());

             // Execute the insert query
             int rowsAffected = statement.executeUpdate();
             return rowsAffected > 0;
         }
         catch (SQLException e) {
             System.out.println("Error inserting transaction: " + e.getMessage());
             return false;
         }
     }

     // Method to retrieve the last transaction ID from the database
     public static int getLastTransactionId() throws SQLException {
         int lastTransactionId = 0;
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT MAX(TID) AS LastTransactionId FROM TransactionCompany");
              ResultSet resultSet = statement.executeQuery()) {
             if (resultSet.next()) {
                 lastTransactionId = resultSet.getInt("LastTransactionId");
             }
         }
         return lastTransactionId;
     }
     private static boolean dealExists(int dealId) throws SQLException {
         // Check if the dealId exists in the Deal table
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT COUNT(DealID) AS dealCount FROM Deal WHERE DealID = ?")) {
             statement.setInt(1, dealId);
             try (ResultSet resultSet = statement.executeQuery()) {
                 if (resultSet.next()) {
                     int dealCount = resultSet.getInt("dealCount");
                     return dealCount > 0;
                 }
             }
         }
         return false; // Return false if an error occurs or if no result is found
     }



     private static boolean companyExists(int companyId) throws SQLException {
         // Check if the companyId exists in the TransportCompany table
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT * FROM TransportCompany WHERE CompanyID = ?")) {
             statement.setInt(1, companyId);
             try (ResultSet resultSet = statement.executeQuery()) {
                 return resultSet.next(); // Return true if companyId exists
             }
         }
     }
     public static ArrayList<AdminTransactionCompanyController.TransactionCompany> getExistingCompanyTransactions() {
         ArrayList<AdminTransactionCompanyController.TransactionCompany> existingTransactions = new ArrayList<>();

         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT * FROM TransactionCompany");
              ResultSet resultSet = statement.executeQuery()) {
             while (resultSet.next()) {
                 // Retrieve transaction details from the result set
                 int transactionId = resultSet.getInt("TID");
                 int dealId = resultSet.getInt("dealId");
                 int companyId = resultSet.getInt("companyId");
                 String transactionAmount = resultSet.getString("transactionAmount");
                 String dateAndTime = resultSet.getString("dateAndTime");
                 String paymentMethod = resultSet.getString("paymentMethod");

                 // Create a TransactionCompany object and add it to the list

                 AdminTransactionCompanyController.TransactionCompany transaction = new AdminTransactionCompanyController.TransactionCompany(transactionId, dealId, companyId, transactionAmount, dateAndTime, paymentMethod);

                 existingTransactions.add(transaction);
             }
         } catch (SQLException e) {
             System.out.println("Error retrieving existing transactions: " + e.getMessage());
         }

         return existingTransactions;
     }



     public static ArrayList<AdminTransactionCustomerController.TransactionCompany> getExistingCustomerTransactions() {
         ArrayList<AdminTransactionCustomerController.TransactionCompany> existingTransactions = new ArrayList<>();

         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT * FROM TransactionCustomer");
              ResultSet resultSet = statement.executeQuery()) {
             while (resultSet.next()) {
                 // Retrieve transaction details from the result set
                 int transactionId = resultSet.getInt("TID");
                 int dealId = resultSet.getInt("DealID");
                 String transactionAmount = resultSet.getString("TransactionAmount");
                 String dateAndTime = resultSet.getString("DateAndTime");
                 String paymentMethod = resultSet.getString("PaymentMethod");

                 // Create a TransactionCompany object and add it to the list
                 AdminTransactionCustomerController.TransactionCompany transaction = new AdminTransactionCustomerController.TransactionCompany(transactionId, dealId, transactionAmount, dateAndTime, paymentMethod);
                 existingTransactions.add(transaction);
             }
         } catch (SQLException e) {
             System.out.println("Error retrieving existing customer transactions: " + e.getMessage());
         }

         return existingTransactions;
     }
     public static boolean addCustomerTransaction(AdminTransactionCustomerController.TransactionCompany transaction) throws SQLException {
         // Check if the dealId exists
         if (!dealExists(transaction.getDealId())) {
             System.out.println("Deal ID does not exist.");
             return false;
         }

         // Insert the transaction into the database
         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement(
                      "INSERT INTO TransactionCustomer (TID, DealID, TransactionAmount, DateAndTime, PaymentMethod) VALUES (?, ?, ?, ?, ?)")) {
             statement.setInt(1, transaction.getTransactionId());
             statement.setInt(2, transaction.getDealId());
             statement.setString(3, transaction.getTransactionAmount());
             statement.setString(4, transaction.getDateAndTime());
             statement.setString(5, transaction.getPaymentMethod());

             // Execute the insert query
             int rowsAffected = statement.executeUpdate();
             return rowsAffected > 0;
         } catch (SQLException e) {
             System.out.println("Error inserting customer transaction: " + e.getMessage());
             return false;
         }
     }

     public static int getLastCustomerTransactionId() {
         int lastTransactionId = 0;

         try (Connection connection = DriverManager.getConnection(url);
              PreparedStatement statement = connection.prepareStatement("SELECT MAX(TID) AS LastID FROM TransactionCustomer");
              ResultSet resultSet = statement.executeQuery()) {
             if (resultSet.next()) {
                 lastTransactionId = resultSet.getInt("LastID");
             }
         } catch (SQLException e) {
             System.out.println("Error retrieving last customer transaction ID: " + e.getMessage());
         }

         return lastTransactionId;
     }
     public static ArrayList<AdminBiltyController.BiltyDealPair> getBiltyDealPairsFromDatabase() {
         ArrayList<AdminBiltyController.BiltyDealPair> biltyDealPairs = new ArrayList<>();

         try (Connection connection = DriverManager.getConnection(url)) {
             String sql = "SELECT BiltyID, DealID FROM Goods_bilty";
             try (PreparedStatement statement = connection.prepareStatement(sql)) {
                 try (ResultSet resultSet = statement.executeQuery()) {
                     while (resultSet.next()) {
                         int biltyID = resultSet.getInt("BiltyID");
                         int dealID = resultSet.getInt("DealID");
                         biltyDealPairs.add(new AdminBiltyController.BiltyDealPair(biltyID, dealID));
                     }
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return biltyDealPairs;
     }
    public static List<AdminCargoController.Item> getCargoByBiltyID(int biltyID) {
        List<AdminCargoController.Item> cargoList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT Cargo.CargoID, Items.ItemName, Cargo.quantity, Items.Weigh, COALESCE(Cargo_Transport.numberPlate, 'None') AS vehicleAssigned " +
                    "FROM Cargo " +
                    "INNER JOIN Items ON Cargo.ItemId = Items.ItemId " +
                    "LEFT JOIN Cargo_Transport ON Cargo.CargoID = Cargo_Transport.CargoID " +
                    "WHERE Cargo.BiltyID = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, biltyID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int cargoID = resultSet.getInt("CargoID");
                        String itemName = resultSet.getString("itemName");
                        int quantity = resultSet.getInt("quantity");
                        double weight = resultSet.getDouble("weigh");
                        String vehicleAssigned = resultSet.getString("vehicleAssigned");

                        AdminCargoController.Item cargo = new AdminCargoController.Item(cargoID, itemName, quantity, weight, vehicleAssigned);
                        cargoList.add(cargo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions or connection errors
        }

        return cargoList;
    }

    public static List<AdminCargoController.Vehicle> getAvailableVehicles(int biltyID) {
        List<AdminCargoController.Vehicle> availableVehicles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT Transport.NumberPlate, Transport.RentedBy, Cargo_Transport.CargoID " +
                    "FROM Transport " +
                    "LEFT JOIN Cargo_Transport ON Transport.NumberPlate = Cargo_Transport.NumberPlate " +
                    "WHERE Transport.NumberPlate NOT IN (" +
                    "SELECT NumberPlate FROM Cargo_Transport WHERE CargoID IN (" +
                    "SELECT CargoID FROM Cargo WHERE BiltyID = ?" +
                    ")" +
                    ")";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, biltyID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Assuming your Vehicles table has columns named numberPlate, rentedBy, cargoAssigned
                        String numberPlate = resultSet.getString("NumberPlate");
                        String rentedBy = resultSet.getString("RentedBy");
                        int cargoAssigned = resultSet.getInt("CargoID");

                        AdminCargoController.Vehicle vehicle = new AdminCargoController.Vehicle(numberPlate, rentedBy, cargoAssigned);
                        availableVehicles.add(vehicle);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions or connection errors
        }

        return availableVehicles;
    }
    public static void linkVehicleAndCargo(AdminCargoController.Vehicle vehicle, AdminCargoController.Item cargo) {


        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "INSERT INTO Cargo_Transport (NumberPlate, CargoID, DepartTime, ArrivalTime) VALUES (?, ?, GETDATE(), DATEADD(DAY, 10, GETDATE()))";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, vehicle.getNumberPlate());
                statement.setInt(2, cargo.getCargoID());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions or connection errors
        }
    }

    public static List<ProfitLossChartController.Transaction> getLossTransactions() {
        List<ProfitLossChartController.Transaction> lossTransactions = new ArrayList<>();

        String sql = "SELECT * FROM TransactionCompany";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Retrieve data from the result set and create Transaction objects
                LocalDateTime dateAndTime = rs.getTimestamp("dateAndTime").toLocalDateTime();
                double amount = rs.getDouble("TransactionAmount");

                // Create a new Transaction object and add it to the list
                ProfitLossChartController.Transaction transaction = new ProfitLossChartController.Transaction(dateAndTime, amount);
                lossTransactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception here
        }

        return lossTransactions;
    }
    public static List<ProfitLossChartController.Transaction> getProfitTransactions() {
        List<ProfitLossChartController.Transaction> profitTransactions = new ArrayList<>();

        String sql = "SELECT * FROM TransactionCustomer";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Retrieve data from the result set and create Transaction objects
                LocalDateTime dateAndTime = rs.getTimestamp("dateAndTime").toLocalDateTime();
                double amount = rs.getDouble("TransactionAmount");

                // Create a new Transaction object and add it to the list
                ProfitLossChartController.Transaction transaction = new ProfitLossChartController.Transaction(dateAndTime, amount);
                profitTransactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception here
        }

        return profitTransactions;
    }
}
