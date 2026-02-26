package edu.ntnu.idi.idatt2003;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Exchange {
    private final String name;
    private int week;
    private final Map<String, Stock> stocks;
    private final Random random;

    public Exchange(String name) {
        validateName(name);

        this.name = name;
        this.week = 1;
        this.stocks = new HashMap<>();
        this.random = new Random();
    }

    public String getName() {
        return name;
    }

    public int getWeek() {
        return week;
    }

    public boolean addStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        stocks.put(stock.getSymbol(), stock);
        return true;
    }

    public Stock getStock(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        return stocks.get(symbol);
    }

    public List<Stock> getAllStocks() {
        return List.copyOf(stocks.values());
    }

    public List<Stock> findStocks(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        String searchName = companyName.toLowerCase();
        return stocks.values().stream()
                .filter(stock -> stock.getCompany().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public Sale sell(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }

        Stock stock = getStock(share.getStock().getSymbol());
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found in exchange: " + share.getStock().getSymbol());
        }

        SaleCalculator calculator = new SaleCalculator(share);

        return new Sale(share, week, calculator);
    }
}
