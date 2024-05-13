package com.example.wpl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProfitLossChartController extends Application {


    public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days Ago");
        yAxis.setLabel("Amount");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Profit and Loss");

        List<Transaction> profitTransactions = getProfitTransactions();
        List<Transaction> lossTransactions = getLossTransactions();

        // Filter transactions for the last 30 days
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(29); // 30 days ago
        profitTransactions = filterTransactionsWithinLastThirtyDays(profitTransactions, thirtyDaysAgo);
        lossTransactions = filterTransactionsWithinLastThirtyDays(lossTransactions, thirtyDaysAgo);

        // Assign consecutive numbers for each day within the last 30 days
        Map<LocalDate, Integer> dateToIndexMap = new HashMap<>();
        for (LocalDate date = today; !date.isBefore(thirtyDaysAgo); date = date.minusDays(1)) {
            dateToIndexMap.put(date, (int) ChronoUnit.DAYS.between(date, today));
        }

        XYChart.Series<Number, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");
        for (Transaction transaction : profitTransactions) {
            int daysSinceTransaction = dateToIndexMap.get(transaction.getDate().toLocalDate());
            profitSeries.getData().add(new XYChart.Data<>(daysSinceTransaction, transaction.getAmount()));
        }

        XYChart.Series<Number, Number> lossSeries = new XYChart.Series<>();
        lossSeries.setName("Loss");
        for (Transaction transaction : lossTransactions) {
            int daysSinceTransaction = dateToIndexMap.get(transaction.getDate().toLocalDate());
            lossSeries.getData().add(new XYChart.Data<>(daysSinceTransaction, transaction.getAmount()));
        }

        lineChart.getData().addAll(profitSeries, lossSeries);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Profit and Loss Visualization");
        stage.show();
    }



    public List<Transaction> getProfitTransactions() {
        return DB_Admin.getProfitTransactions();
    }

    public List<Transaction> getLossTransactions() {
        return DB_Admin.getLossTransactions();
    }

    private List<Transaction> filterTransactionsWithinLastThirtyDays(List<Transaction> transactions, LocalDate thirtyDaysAgo) {
        return transactions.stream()
                .filter(transaction -> transaction.getDate().toLocalDate().isAfter(thirtyDaysAgo))
                .collect(Collectors.toList());
    }

    public static class Transaction {
        private LocalDateTime dateAndTime;
        private double amount;

        public Transaction(LocalDateTime dateAndTime, double amount) {
            this.dateAndTime = dateAndTime;
            this.amount = amount;
        }

        public LocalDateTime getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(LocalDateTime dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public LocalDateTime getDate() {
            return dateAndTime;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
