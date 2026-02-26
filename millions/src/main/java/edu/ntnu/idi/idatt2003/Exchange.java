package edu.ntnu.idi.idatt2003;

import java.math.BigDecimal;
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

    public Exchange(String name, List<Stock> stocks) {
        validateName(name);

        this.name = name;
        this.week = 1;
        this.stocks = new HashMap<>();
        this.random = new Random();

        if(stocks != null) {
            for (Stock stock : stocks) {
                addStock(stock);
            }
        }
    }

    public Exchange(String name) {
        this(name, null);
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

    public boolean hasStock(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }
        return stocks.containsKey(symbol);
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

    public List<Stock> findStocks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        String searchName = searchTerm.toLowerCase();
        return stocks.values().stream()
                .filter(stock ->
                        stock.getCompany().toLowerCase().contains(searchName) ||
                        stock.getSymbol().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public Transaction sell(Share share, Player player) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        Stock stock = getStock(share.getStock().getSymbol());
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found in exchange: " + share.getStock().getSymbol());
        }

        SaleCalculator calculator = new SaleCalculator(share);
        Sale sale = new Sale(share, week, calculator);
        sale.commit(player);

        return sale;
    }

    public Transaction buy(String symbol, BigDecimal quantity, Player player) {
        Stock stock = getStock(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found in exchange: " + symbol);
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        BigDecimal currentPrice = stock.getSalesPrice();
        Share share = new Share(stock, quantity, currentPrice);

        Purchase purchase = new Purchase(share, week);
        purchase.commit(player);

        return purchase;
    }

    public void advance() {
        week++;

        // Update each stock's price randomly
        for (Stock stock : stocks.values()) {
            BigDecimal currentPrice = stock.getSalesPrice();

            // Random change between -7% and +7%
            double percentageChange = (random.nextDouble() * 0.10) - 0.07; // -0.07 to +0.07
            BigDecimal change = currentPrice.multiply(new BigDecimal(percentageChange));
            BigDecimal newPrice = currentPrice.add(change);

            // Ensure price doesn't go below $1
            if (newPrice.compareTo(BigDecimal.ONE) < 0) {
                newPrice = BigDecimal.ONE;
            }
            stock.addNewSalesPrice(newPrice);
        }
    }


}
