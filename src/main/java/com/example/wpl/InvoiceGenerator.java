package com.example.wpl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.io.FileWriter;
import java.io.IOException;

public class InvoiceGenerator {

    static private String url = "jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=WP;user=lol;password=1234;trustServerCertificate=true";

    public static void generateInvoice(int transactionId) {
        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "SELECT *, Customer.CustomerID as CustomerID FROM TransactionCustomer " +
                    "INNER JOIN Deal ON TransactionCustomer.DealID = Deal.DealID " +
                    "INNER JOIN Customer ON Deal.CustomerID = Customer.CustomerID " +
                    "WHERE TID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transactionId);
            ResultSet resultSet = statement.executeQuery();

            // Initialize HTML content
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<!DOCTYPE html>");
            htmlContent.append("<html lang=\"en\">");
            htmlContent.append("<head>");
            htmlContent.append("<meta charset=\"UTF-8\">");
            htmlContent.append("<title>Invoice</title>");
            htmlContent.append("<style>");
            // CSS styles for the invoice
            htmlContent.append("body { font-family: Arial, sans-serif; }");
            htmlContent.append(".invoice { width: 80%; margin: 0 auto; padding: 20px; background: #f9f9f9; }");
            htmlContent.append(".invoice-header { text-align: center; margin-bottom: 20px; }");
            htmlContent.append(".invoice-details { margin-bottom: 20px; }");
            htmlContent.append(".invoice-items { width: 100%; border-collapse: collapse; }");
            htmlContent.append(".invoice-items th, .invoice-items td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            htmlContent.append(".customer-details { float: right; margin-right: 20px; }"); // Align customer details to the right
            htmlContent.append("</style>");
            htmlContent.append("</head>");
            htmlContent.append("<body>");

            // Iterate through the result set and add invoice details
            while (resultSet.next()) {
                // Add transaction details
                int dealId = resultSet.getInt("DealID");
                double transactionAmount = resultSet.getDouble("TransactionAmount");
                String dateAndTime = resultSet.getString("DateAndTime");
                String paymentMethod = resultSet.getString("PaymentMethod");
                int customerId = resultSet.getInt("CustomerID");

                // Add customer details
                String customerDetails = getCustomerDetails(connection, customerId);

                // Invoice header with company logo
                htmlContent.append("<div class=\"invoice\">");
                htmlContent.append("<div class=\"invoice-header\">");
                // Add company logo
                htmlContent.append("<img src=\"../target/classes/Assets/WP_logo.png\" alt=\"Company Logo\" style=\"max-width: 200px;\" />\n");
                htmlContent.append("<h1>Invoice</h1>");
                htmlContent.append("</div>");

                // Add customer details
                htmlContent.append("<div class=\"customer-details\">");
                htmlContent.append(customerDetails);
                htmlContent.append("</div>");

                // Add transaction details to the invoice
                htmlContent.append("<div class=\"invoice-details\">");
                htmlContent.append("<p><strong>Transaction ID:</strong> ").append(transactionId).append("</p>");
                htmlContent.append("<p><strong>Deal ID:</strong> ").append(dealId).append("</p>");
                htmlContent.append("<p><strong>Transaction Amount:</strong> $").append(formatAmount(transactionAmount)).append("</p>");
                htmlContent.append("<p><strong>Date and Time:</strong> ").append(dateAndTime).append("</p>");
                htmlContent.append("<p><strong>Payment Method:</strong> ").append(paymentMethod).append("</p>");
                htmlContent.append("</div>");

                // Retrieve order details based on dealId
                String orderDetails = getCargoDetails(connection, dealId);
                // Add order details to the invoice
                htmlContent.append("<p><strong>Order Details:</strong></p>");
                htmlContent.append("<table class=\"invoice-items\">");
                htmlContent.append("<tr><th>Cargo ID</th><th>Item Name</th><th>Weight</th><th>Quantity</th></tr>");
                htmlContent.append(orderDetails);
                htmlContent.append("</table>");

                // Close invoice div
                htmlContent.append("</div>");
            }

            // Close HTML content
            htmlContent.append("</body>");
            htmlContent.append("</html>");

            // Write HTML content to a file with the name "Invoice_transactionID.html"
            String fileName = "Invoice_" + transactionId + ".html";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(htmlContent.toString());
            fileWriter.close();

            // Introduce a brief delay before opening the invoice in the browser
            Thread.sleep(1000); // 1 second delay

            openInvoiceInBrowser(transactionId);

            connection.close();
        } catch (SQLException | IOException e) {
            System.out.println("Error generating invoice: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getCustomerDetails(Connection connection, int customerId) throws SQLException {
        StringBuilder customerDetails = new StringBuilder();
        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, customerId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            customerDetails.append("<p><strong>Customer ID:</strong> ").append(resultSet.getInt("CustomerID")).append("</p>");
            customerDetails.append("<p><strong>Name:</strong> ").append(resultSet.getString("CustomerName")).append("</p>");
            customerDetails.append("<p><strong>Email:</strong> ").append(resultSet.getString("Email")).append("</p>");
            customerDetails.append("<p><strong>Contact No:</strong> ").append(resultSet.getString("ContactNo")).append("</p>");
            customerDetails.append("<p><strong>Company:</strong> ").append(resultSet.getString("Company")).append("</p>");
        }
        return customerDetails.toString();
    }
    private static String getCargoDetails(Connection connection, int dealId) throws SQLException {
        StringBuilder cargoDetails = new StringBuilder();
        String sql = "SELECT * FROM Goods_bilty WHERE DealID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, dealId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int biltyId = resultSet.getInt("BiltyID");
            cargoDetails.append(getCargoDetailsForBilty(connection, biltyId));
        }
        return cargoDetails.toString();
    }

    private static String getCargoDetailsForBilty(Connection connection, int biltyId) throws SQLException {
        StringBuilder cargoDetailsForBilty = new StringBuilder();
        String sql = "SELECT Cargo.CargoID, Cargo.ItemID, Cargo.Quantity, Item.ItemName, Item.Weigh " +
                "FROM Cargo " +
                "INNER JOIN Items as Item ON Cargo.ItemID = Item.ItemID " +
                "WHERE Cargo.BiltyID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, biltyId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int cargoId = resultSet.getInt("CargoID");
            String itemName = resultSet.getString("ItemName");
            double weigh = resultSet.getDouble("Weigh");
            int quantity = resultSet.getInt("Quantity");
            cargoDetailsForBilty.append("<tr><td>").append(cargoId).append("</td><td>").append(itemName)
                    .append("</td><td>").append(weigh).append("</td><td>").append(quantity).append("</td></tr>");
        }
        return cargoDetailsForBilty.toString();
    }

    private static String formatAmount(double amount) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }
    private static void openInvoiceInBrowser(int transactionId) {
        String fileName = "Invoice_" + transactionId + ".html";
        File file = new File(fileName);

        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported.");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!file.exists()) {
            System.out.println("Invoice file does not exist.");
            return;
        }

        try {
            desktop.open(file);
        } catch (IOException e) {
            System.out.println("Error opening invoice file: " + e.getMessage());
        }
    }
}