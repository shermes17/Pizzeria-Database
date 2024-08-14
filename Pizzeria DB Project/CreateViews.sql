/*
Sean Hermes
shermes@clemson.edu
CPSC 4620 Part 2
CreateViews.SQL
*/
USE Pizzeria; 


-- ToppingPopularity
CREATE VIEW ToppingPopularity AS
	SELECT 
		t.ToppingName AS Topping,
		 COUNT(pt.ToppingID) + SUM(CASE WHEN pt.Extra_Toppings = TRUE THEN 1 ELSE 0 END) AS ToppingCount
	FROM 
		topping t
	LEFT JOIN 
		pizza_topping pt ON t.ToppingID = pt.ToppingID
	GROUP BY 
		t.ToppingName
	ORDER BY 
	   ToppingCount DESC,  Topping;



-- ProfitByPizza
CREATE VIEW ProfitByPizza AS
	SELECT 
		p.PizzaSize AS Size,
		p.PizzaCrust AS Crust,
		SUM(p.PizzaPrice) - SUM(p.PizzaCost) AS Profit,
		DATE_FORMAT(o.Timestamp, '%m/%Y') AS OrderMonth
	FROM
		base_price bp
	JOIN
		pizza p ON bp.BasePriceSize = p.PizzaSize AND bp.BasePriceCrust = p.PizzaCrust
	JOIN
		orders o ON p.PizzaOrderID = o.OrderID
	GROUP BY
		Size, Crust, OrderMonth
	ORDER BY
		Profit ASC;


-- ProfitByOrderType
CREATE VIEW ProfitByOrderType AS
	SELECT 
		o.orderType AS customerType,
		DATE_FORMAT(o.Timestamp, '%m/%Y') AS OrderMonth,
		ROUND(SUM(o.OrderTotalPrice * (1 - IFNULL(d.DiscountPercentageOff,0)/100) - IFNULL(d.DiscountDollarsOff, 0)), 2) AS TotalOrderPrice,
		ROUND(SUM(o.OrderTotalCost), 2) AS TotalOrderCost,
		ROUND(SUM(o.OrderTotalPrice * (1 - IFNULL(d.DiscountPercentageOff,0)/100) - IFNULL(d.DiscountDollarsOff, 0)) - SUM(o.OrderTotalCost), 2) AS Profit
	FROM
		orders o
	LEFT JOIN
		orders_discount od ON o.OrderID = od.OrderID
	LEFT JOIN 
		discount d ON od.DiscountID = d.DiscountID
	GROUP BY
		customerType, OrderMonth
	UNION ALL
	SELECT
		'',
		'Grand Total',
		ROUND(SUM(o.OrderTotalPrice * (1 - IFNULL(d.DiscountPercentageOff,0)/100) - IFNULL(d.DiscountDollarsOff, 0)), 2),
		ROUND(SUM(o.OrderTotalCost), 2),
		ROUND(SUM(o.OrderTotalPrice * (1 - IFNULL(d.DiscountPercentageOff,0)/100) - IFNULL(d.DiscountDollarsOff, 0)) - SUM(o.OrderTotalCost), 2)
	FROM
		orders o
	LEFT JOIN
		orders_discount od ON o.OrderID = od.OrderID
	LEFT JOIN 
		discount d ON od.DiscountID = d.DiscountID
	ORDER BY
		CASE 
			WHEN customerType = 'dinein' THEN 1
			WHEN customerType = 'pickup' THEN 2
			WHEN customerType = 'delivery' THEN 3
			WHEN customerType = 'Grand Total' THEN 4
			ELSE 5
		END
;


SELECT * FROM ToppingPopularity;
SELECT * FROM ProfitByPizza;
SELECT * FROM ProfitByOrderType;