/*
Sean Hermes
shermes@clemson.edu
CPSC 4620 Part 2
CreateTables.SQL
*/

CREATE schema Pizzeria;
USE Pizzeria;

CREATE TABLE 
    customer (
        CustomerID INT AUTO_INCREMENT NOT NULL,
        PRIMARY KEY (CustomerID),
        CustomerFirstName VARCHAR(50) NOT NULL,
        CustomerLastName VARCHAR(50) NOT NULL,
        CustomerPhoneNumber VARCHAR(100) NOT NULL
    );

CREATE TABLE 
    orders (
        OrderID INT AUTO_INCREMENT NOT NULL,
        PRIMARY KEY (OrderID),
        OrderCustomerID INT NOT NULL,
        OrderType ENUM ('dinein', 'delivery','pickup') NOT NULL,
        FOREIGN KEY (OrderCustomerID) REFERENCES customer (CustomerID),
        Timestamp DATETIME NOT NULL,
        OrderTotalCost DECIMAL(10,2) NOT NULL,
        OrderTotalPrice DECIMAL(10,2) NOT NULL,
        OrderIsComplete BOOLEAN DEFAULT FALSE NOT NULL
    );
    CREATE TABLE
    dine_in (
        OrderID INT NOT NULL,
        FOREIGN KEY (OrderID) REFERENCES orders (OrderID),
        PRIMARY KEY (OrderID),
        DineInTableNumber INT NOT NULL
    );

CREATE TABLE
    pick_up (
        OrderID INT NOT NULL,
        FOREIGN KEY (OrderID) REFERENCES orders (OrderID),
        PRIMARY KEY (OrderID),
        IsPickedUp BOOLEAN DEFAULT FALSE NOT NULL
    );

CREATE TABLE
    delivery (
        OrderID INT NOT NULL,
        FOREIGN KEY (OrderID) REFERENCES orders (OrderID),
        PRIMARY KEY (OrderID),
        HouseNumber INT NOT NULL,
        Street VARCHAR(100) NOT NULL,
        City VARCHAR(100) NOT NULL,
        State VARCHAR(2) NOT NULL,
        Zipcode VARCHAR(5) NOT NULL
    );

CREATE TABLE
    base_price (
        BasePriceSize VARCHAR(30) NOT NULL,
        BasePriceCrust VARCHAR(30) NOT NULL,
        PRIMARY KEY (BasePriceSize, BasePriceCrust),
        BasePrice DECIMAL(10,2) NOT NULL,
        BaseCost DECIMAL(10,2) NOT NULL
    );

CREATE TABLE
    pizza (
        PizzaID INT AUTO_INCREMENT NOT NULL,
        PRIMARY KEY (PizzaID),
        PizzaOrderID INT NOT NULL,
        FOREIGN KEY (PizzaOrderID) REFERENCES orders (OrderID),
        PizzaSize VARCHAR(30) NOT NULL,
        PizzaCrust VARCHAR(30) NOT NULL,
        FOREIGN KEY (PizzaSize, PizzaCrust) REFERENCES base_price (BasePriceSize, BasePriceCrust),
        PizzaPrice DECIMAL(10,2) NOT NULL,
        PizzaCost DECIMAL(10,2) NOT NULL,
        PizzaCompleted BOOLEAN DEFAULT FALSE,
        PizzaDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE
    topping (
		ToppingID INT AUTO_INCREMENT NOT NULL,
        ToppingName VARCHAR(100) NOT NULL,
        PRIMARY KEY (ToppingID),
        ToppingPrice DECIMAL(10,2) NOT NULL,
        ToppingCost DECIMAL(10,2) NOT NULL,
        ToppingInventory INT NOT NULL,
        ToppingMinimum INT NOT NULL,
        ToppingSmall DECIMAL(10,2) NOT NULL,
        ToppingMedium DECIMAL(10,2) NOT NULL,
        ToppingLarge DECIMAL(10,2) NOT NULL,
        ToppingXLarge DECIMAL(10,2) NOT NULL
    );

CREATE TABLE
    discount (
		DiscountID INT AUTO_INCREMENT NOT NULL,
        DiscountName VARCHAR(25) NOT NULL,
        PRIMARY KEY (DiscountID),
        DiscountDollarsOff DECIMAL(10,2) ,
        DiscountPercentageOff DECIMAL(10,2) 
    );



CREATE TABLE
    pizza_topping (
        ToppingID INT NOT NULL,
        PizzaID INT NOT NULL,
        FOREIGN KEY (ToppingID) REFERENCES topping (ToppingID),
        FOREIGN KEY (PizzaID) REFERENCES pizza (PizzaID),
        PRIMARY KEY (ToppingID, PizzaID),
        Extra_Toppings BOOLEAN DEFAULT FALSE NOT NULL
    );

CREATE TABLE
    pizza_discount (
        DiscountID INT  NOT NULL,
        PizzaID INT NOT NULL,
        FOREIGN KEY (DiscountID) REFERENCES discount (DiscountID),
        FOREIGN KEY (PizzaID) REFERENCES pizza (PizzaID),
        PRIMARY KEY (DiscountID, PizzaID)
    );

CREATE TABLE
    orders_discount (
        OrderID INT NOT NULL,
		DiscountID INT  NOT NULL,
        FOREIGN KEY (OrderID) REFERENCES orders (OrderID),
        FOREIGN KEY (DiscountID) REFERENCES discount (DiscountID),
        PRIMARY KEY (OrderID, DiscountID)
    );
    



