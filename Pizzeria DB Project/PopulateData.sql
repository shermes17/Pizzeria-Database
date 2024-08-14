/*
Sean Hermes
shermes@clemson.edu
CPSC 4620 Part 2
PopulateData.SQL
*/
USE Pizzeria;

/********** Discounts ***********/
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff) VALUES ('Employee', 15, NULL); 
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff) VALUES ('Lunch Special Medium', NULL, 1.00); 
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff) VALUES ('Lunch Special Large', NULL, 2.00 ); 
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff) VALUES ('Specialty Pizza', NULL,1.50); 
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff) VALUES ('Happy Hour', 10, NULL); 
INSERT INTO discount (DiscountName, DiscountPercentageOff, DiscountDollarsOff ) VALUES ('Gameday Special', 20, NULL);

/********** Base Price **********/
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Small', 'Thin', 3, .5);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Small', 'Original', 3, .75);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Small', 'Pan', 3.5, 1);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Small', 'Gluten-Free', 4, 2);

INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Medium', 'Thin', 5, 1);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Medium', 'Original', 5, 1.5);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Medium', 'Pan', 6, 2.25);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Medium', 'Gluten-Free', 6.25, 4);

INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Large', 'Thin', 8, 1.25);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Large', 'Original', 8, 2);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Large', 'Pan', 9, 3);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('Large', 'Gluten-Free', 9.5, 4);

INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('XLarge', 'Thin', 10, 2);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('XLarge', 'Original', 10, 3);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('XLarge', 'Pan', 11.5, 4.5);
INSERT INTO base_price (BasePriceSize, BasePriceCrust, BasePrice, BaseCost ) VALUES ('XLarge', 'Gluten-Free', 12.5, 6);



/********** Toppings ***********/
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Pepperoni', 1.25, 0.2, 100, 50, 2, 2.75, 3.5, 4.5);
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Sausage',1.25, 0.15, 100, 50,2.5, 3, 3.5, 4.25);
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Ham',1.5, 0.15, 78, 25, 2, 2.5, 3.25, 4 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Chicken',1.75 ,0.25 ,56 ,25 ,1.5 ,2 ,2.25 ,3 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Green Pepper',0.5, 0.02, 79, 25, 1, 1.5, 2, 2.5);
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Onion',0.5 ,0.02 ,85 ,25 ,1 ,1.5 ,2 ,2.75 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Roma Tomato', 0.75, 0.03, 86, 10, 2, 3, 3.5, 4.5 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Mushrooms',0.75 ,0.1 ,52 ,50 ,1.5 ,2 ,2.5 ,3 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Black Olives', 0.6, 0.1, 39, 25, 0.75, 1, 1.5, 2 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Pineapple',1 ,0.25 ,15 ,0 ,1 ,1.25 ,1.75 ,2 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Jalapenos',0.5, 0.05, 64, 0, 0.5, 0.75, 1.25, 1.75);
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Banana Peppers',0.5 ,0.05 ,36 ,0 ,0.6 ,1 ,1.3 ,1.75);
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Regular Cheese',0.5, 0.12, 250, 50, 2, 3.5, 5, 7 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Four Cheese Blend', 1 ,0.15 ,150 ,25 ,2 ,3.5 ,5 ,7 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Feta Cheese',1.5, 0.18, 75, 0, 1.75, 3, 4, 5.5 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Goat Cheese', 1.5 ,0.2 ,54 ,0 ,1.6 ,2.75 ,4 ,5.5 );
INSERT INTO topping (ToppingName, ToppingPrice, ToppingCost, ToppingInventory, ToppingMinimum, ToppingSmall, ToppingMedium, ToppingLarge, ToppingXLarge)
	VALUES ('Bacon',1.5, 0.25, 89, 0, 1, 1.5, 2, 3 );

/********** ORDERS **********/

-- ORDER 1
/*
On March 5th at 12:03 pm there was a dine-in order (at table 21) for a large thin crust pizza with Regular
Cheese (extra), Pepperoni, and Sausage (Price: $19.75, Cost: $3.68). They used the “Lunch Special Large”
discount for the pizza.
*/
INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNumber) VALUES ('DINE','IN','0000000000');
	SET @CUSTOMER_ID := LAST_INSERT_ID();
INSERT INTO orders (OrderCustomerID,OrderType, Timestamp,OrderTotalPrice,OrderTotalCost) VALUES (@CUSTOMER_ID,'dinein', '2024-03-05 12:03:00',19.75,3.68);
SET @ORDER_ID = LAST_INSERT_ID();
INSERT INTO dine_in (OrderID, DineInTableNumber) VALUES (@ORDER_ID, 21);
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost) VALUES (@ORDER_ID,TRUE,'Large','Thin',19.75,3.68);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,13,TRUE);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,2);
	INSERT INTO pizza_discount (PizzaID,DiscountID) VALUES (@PIZZA_ID,2);
    

-- ORDER 2
/*
On April 3rd at 12:05 pm there was a dine-in order (at table 4). They ordered a medium pan pizza with
Feta Cheese, Black Olives, Roma Tomatoes, Mushrooms and Banana Peppers (Price: $12.85, Cost: $3.23).
They used the “Lunch Special Medium” and the “Specialty Pizza” discounts for the pizza. They also ordered
a small original crust pizza with Regular Cheese, Chicken and Banana Peppers (Price: $6.93, Cost: $1.40).
*/

INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) VALUES (1,'dinein', '2024-04-03 12:05:00',19.78,4.63);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO dine_in (OrderID, DineInTableNumber) VALUES (@ORDER_ID, 4);
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Medium','Pan',12.85,3.23);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,15);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,9);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,7);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,8);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,12);
	INSERT INTO pizza_discount (PizzaID,DiscountID)VALUES (@PIZZA_ID,2);
	INSERT INTO pizza_discount (PizzaID,DiscountID)VALUES (@PIZZA_ID,4);
-- pizza 2
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Small','Original',6.93,1.40);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,4);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,12);


-- ORDER 3
/*
On March 3rd at 9:30 pm Andrew Wilkes-Krier placed an order for pickup of 6 large original crust pizzas
with Regular Cheese and Pepperoni (Price: $14.88, Cost: $3.30 each). Andrew’s phone number is 864-254-
5861.
*/

INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNumber) VALUES ('Andrew','Wilkes-Krier','8642545861');
	SET @CUSTOMER_ID := LAST_INSERT_ID();
-- order
INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) VALUES (@CUSTOMER_ID,'pickup', '2024-03-03 21:30:00',89.28,19.8);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO pick_up (OrderID,IsPickedUp) VALUES (@ORDER_ID,TRUE);
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
-- pizza 2
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
-- pizza 3
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
-- pizza 4
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
-- pizza 5
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
-- pizza 6
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Original',14.88,3.30);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);


-- ORDER 4
/*
On April 20th at 7:11 pm there was a delivery order made by Andrew Wilkes-Krier for 1 xlarge pepperoni
and Sausage pizza (Price: $27.94, Cost: $9.19), one xlarge pizza with Ham (extra) and Pineapple (extra)
pizza (Price: $31.50, Cost: $6.25), and one xlarge Chicken and Bacon pizza (Price: $26.75, Cost: $8.18). All
the pizzas have the Four Cheese Blend on it and are original crust. The order has the “Gameday Special”
discount applied to it, and the ham and pineapple pizza has the “Specialty Pizza” discount applied to it. The
pizzas were delivered to 115 Party Blvd, Anderson SC 29621. His phone number is the same as before.
*/

INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) 
	VALUES (2,'delivery', '2024-04-20 19:11:00',86.19,23.62);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO delivery (OrderID, HouseNumber,Street,City,State,Zipcode) VALUES (@ORDER_ID, 115, 'Party Blvd', 'Anderson','SC','29621');
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'XLarge','Original',27.94,9.19);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,14);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,1);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,2);
-- pizza 2
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'XLarge','Original',31.50,6.25);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,14);
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,3,TRUE);
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,10,TRUE);
-- pizza 3
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'XLarge','Original',26.75,8.18);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,14);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,4);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,17);
	INSERT INTO pizza_discount (DiscountID, PizzaID) VALUES (4, @PIZZA_ID);
INSERT INTO orders_discount (DiscountID, OrderID) VALUES (6, @ORDER_ID);


-- ORDER 5
/*
On March 2nd at 5:30 pm Matt Engers placed an order for pickup for an xlarge pizza with Green Pepper,
Onion, Roma Tomatoes, Mushrooms, and Black Olives on it. He wants the Goat Cheese on it, and a Gluten
Free Crust (Price: $27.45, Cost: $7.88). The “Specialty Pizza” discount is applied to the pizza. Matt’s phone
number is 864-474-9953.
*/
INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNumber) VALUES ('Matt','Engers','8644749953');
	SET @CUSTOMER_ID := LAST_INSERT_ID();
INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) VALUES (@CUSTOMER_ID,'pickup', '2024-03-02 17:30:00',27.45,7.88);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO pick_up (OrderID,IsPickedUp) VALUES (@ORDER_ID, TRUE);
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'XLarge','Gluten-Free',27.45,7.88);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,5);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,6);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,7);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,8);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,9);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,16);
	INSERT INTO pizza_discount (DiscountID, PizzaID) VALUES (4, @PIZZA_ID);


-- ORDER 6
/*
On March 2nd at 6:17 pm Frank Turner places an order for delivery of one large pizza with Chicken, Green
Peppers, Onions, and Mushrooms. He wants the Four Cheese Blend (extra) and thin crust (Price: $25.81,
Cost: $4.24). The pizza was delivered to 6745 Wessex St Anderson SC 29621. Frank’s phone number is 864-
232-8944.
*/

INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNumber) VALUES ('Frank', 'Turner','8642328944');
	SET @CUSTOMER_ID := LAST_INSERT_ID();
INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) 
	VALUES (@CUSTOMER_ID,'delivery', '2024-03-02 18:17:00',25.81,4.24);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO delivery (OrderID, HouseNumber,Street,City,State,Zipcode) VALUES (@ORDER_ID, 6745, 'Wessex St', 'Anderson','SC','29621');
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)VALUES (@ORDER_ID,TRUE,'Large','Thin',25.81,4.24);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,5);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,6);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,4);
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,8);
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,14, TRUE);
    

-- ORDER 7
/*
On April 13th at 8:32 pm Milo Auckerman ordered two large thin crust pizzas. One had the Four Cheese
Blend on it (extra) (Price: $18.00, Cost: $2.75), the other was Regular Cheese and Pepperoni (extra) (Price:
$19.25, Cost: $3.25). He used the “Employee” discount on his order. He had them delivered to 8879
Suburban Home, Anderson, SC 29621. His phone number is 864-878-5679.
*/
INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNumber) VALUES ('Milo', 'Auckerman','8648785679');
	SET @CUSTOMER_ID := LAST_INSERT_ID();
INSERT INTO orders (OrderCustomerID,OrderType, Timestamp, OrderTotalPrice,OrderTotalCost) 
	VALUES (@CUSTOMER_ID,'delivery', '2024-04-13 20:32:00',37.25,6);
SET @ORDER_ID := LAST_INSERT_ID();
INSERT INTO delivery (OrderID, HouseNumber,Street,City,State,Zipcode) VALUES (@ORDER_ID, 8879, 'Suburban Home', 'Anderson','SC','29621');
-- pizza 1
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)
		VALUES (@ORDER_ID,TRUE,'Large','Thin',18.00,2.75);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,14, TRUE);
-- pizza 2
	INSERT INTO pizza (PizzaOrderID,PizzaCompleted,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)
		VALUES (@ORDER_ID,TRUE,'Large','Thin',19.25,3.25);
	SET @PIZZA_ID := LAST_INSERT_ID();
	INSERT INTO pizza_topping (PizzaID,ToppingID) VALUES (@PIZZA_ID,13);
	INSERT INTO pizza_topping (PizzaID,ToppingID,Extra_Toppings) VALUES (@PIZZA_ID,1, TRUE);
INSERT INTO orders_discount (OrderID, DiscountID) VALUES (@ORDER_ID, 1);





