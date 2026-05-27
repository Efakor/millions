# Millions-Stock Trading Game
A real time desktop stock market simulation game built  with Java  21 and JavaFX 23.Developed as a part of IDATT2003 Programmering 2 at NTNU.

## Authors
- Archi Makwana
- Vanessa Efakor Akosua Amedi

## Requirements
- Java 21 or higher
- Maven

## How to run

1. Clone the repository and navigate to the project root:
```bash
git clone <repository-url>
cd Millions
```
2.Run the application

```bash
mvn javafx:run

```

## Getting started
1. Launch the application with ' mvn javafx:run'
2. Enter your player and starting capital
3. Select a CSV stock data file
4. Click **START Trading**

## Stock data format
In order the application reads the CSV files,the file must follow this format
```bash
AAPL,Apple Inc., 150.00
TSLA, Tesla Inc.,300.00
GOOGL,Alphabet Inc., 250.00
```

- Each stock line format must be: 'symbol, company, name, price'
- Lines starting with '#' are treated as comments and ignored
- Blank lines are ignored
- Note for Testing: You can use any CSV file that follows this format. However, for easy testing, we have provided a ready-to-use file included in this repository. Please select this file when launching the game: stocks.csv

## Features
- Buy and sell shares with cost breakdown
- Price history line chart per stock (to see the change in graph, need to click the stock)
-  Personal watchlist with price alerts
-  Random Market events news feed
-  Final summary screen with net worth and status

## Software Architecture & Patterns
This project was designed with a strict focus on clean code and structural principles:
- **MVC (Model-View-Controller):** Strict separation between business logic, UI, and mediation.
- **Observer Pattern:** Used to automatically and asynchronously refresh JavaFX components.
- **Factory Pattern:** Centralizes transaction creation and system component wiring.
- **Strategy Pattern:** Encapsulates the specific commission and tax calculation algorithms.
  


## Testing & Code Quality
The project includes comprehensive unit tests and implements strict Google CheckStyle formatting.

To run the unit tests:

```bash
mvn test
```

to run the CheckStyle validation:

``` bash
mvn checkstyle:check
```



## Project Structure
```bash
src/main/java/edu/ntnu/idi/idatt2003/
├── controller/    — ExchangeController, PlayerController
├── factory/       — AppFactory, TransactionFactory
├── model/         — Exchange, Player, Portfolio, Stock, Share, Transaction
├── observer/      — GameEvent, GameObserver, Observable
├── repository/    — CsvStockReader, CsvStockWriter
├── util/          — CurrencyUtil
└── view/          
   ├── components/ — NewsFeedPanel,GainersLoserPanel,StatusBarView,StockDetailPanel
   └──...          - MainView,StartView,SummaryView,BuyDialog,SellDialog,SellAllDialog

```


