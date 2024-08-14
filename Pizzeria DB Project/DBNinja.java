package cpsc4620;



import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 *
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 *
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 *
 *
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";




	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}




	public static Order getOrderSpecifics(String orderid) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM orders WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,orderid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int orderID = rs.getInt("OrderID");
				int custID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");

				Order order;
				if(orderType.equals(dine_in)){
					query = "SELECT * FROM dine_in WHERE OrderID = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1,String.valueOf(orderid));
					rs = stmt.executeQuery();
					if(rs.next()) {
						int tableNumber = rs.getInt("DineInTableNumber");

						order = new DineinOrder(orderID, custID, date, cost, price, isComplete, tableNumber);
						getPizzasInOrder(order);
						addDiscountsInOrder(order);
						conn.close();
						return order;
					}
				}else if(orderType.equals(pickup)){
					query = "SELECT * FROM pick_up WHERE OrderID = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1,String.valueOf(orderid));
					rs = stmt.executeQuery();
					if(rs.next()) {
						int isPickedUp = rs.getInt("IsPickedUp");

						order = new PickupOrder(orderID, custID, date, cost, price, isComplete, isPickedUp);
						getPizzasInOrder(order);
						addDiscountsInOrder(order);
						conn.close();
						return order;
					}
				} else{
					query = "SELECT * FROM delivery WHERE OrderID = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1,String.valueOf(orderid));
					rs = stmt.executeQuery();
					if(rs.next()) {
						int houseNumber = rs.getInt("HouseNumber");
						String street = rs.getString("Street");
						String city = rs.getString("City");
						String state = rs.getString("State");
						String zipcode = rs.getString("Zipcode");
						String address = String.valueOf(houseNumber) + street + city + state + zipcode;
						order = new DeliveryOrder(orderID, custID, date, cost, price, isComplete, address);
						getPizzasInOrder(order);
						addDiscountsInOrder(order);
						conn.close();
						return order;
					}
				}

			}
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}


	public static void getPizzasInOrder(Order order) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM pizza WHERE PizzaOrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(order.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int pizzaID = rs.getInt("PizzaID");
				String size = rs.getString("PizzaSize");
				String crust = rs.getString("PizzaCrust");
				double pizzaPrice = rs.getDouble("PizzaPrice");
				double pizzaCost = rs.getDouble("PizzaCost");
				String pizzaDate = rs.getString("PizzaDate");
				String pizzaCompleted = rs.getString("PizzaCompleted");
				Pizza pizza = new Pizza(pizzaID, size, crust, order.getOrderID(), pizzaCompleted, pizzaDate,
						pizzaPrice, pizzaCost);

				addToppingsInPizza(pizza);
				addDiscountsInPizza(pizza);

				order.addPizza(pizza);

			}
		}else{
			System.out.println("ERROR: Connecting to the Database - getPizzasInOrder");
		}
	}

	public static void addToppingsInPizza(Pizza pizza) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM pizza_topping WHERE PizzaID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(pizza.getPizzaID()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int toppingID = rs.getInt("ToppingID");
				boolean isExtra = rs.getInt("Extra_Toppings") == 1;
				Topping t = findToppingByID(toppingID);
				pizza.addToppings(t,isExtra);
				pizza.modifyDoubledArray(toppingID-1,isExtra);

			}
		}else{
			System.out.println("ERROR: Connecting to the Database - getPizzasInOrder");
		}
	}

	public static void addDiscountsInPizza(Pizza pizza) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM pizza_discount WHERE PizzaID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(pizza.getPizzaID()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int discountID = rs.getInt("DiscountID");
				Discount d = findDiscountByID(discountID);
				pizza.addDiscounts(d);
			}
		}else{
			System.out.println("ERROR: Connecting to the Database - getPizzasInOrder");
		}
	}

	public static void addDiscountsInOrder(Order order) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM orders_discount WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(order.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int discountID = rs.getInt("DiscountID");
				Discount d = findDiscountByID(discountID);
				order.addDiscount(d);
			}
		}else{
			System.out.println("ERROR: Connecting to the Database - getPizzasInOrder");
		}
	}







































	public static int getNewOrderID() throws SQLException, IOException{

		if(connect_to_db()){
			String query = "SELECT OrderID FROM orders ORDER BY OrderID DESC LIMIT 1";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				int orderID = rs.getInt("OrderID");
				return ++orderID;
			}

		}else{
			System.out.println("ERROR: Connecting to database - getNewOrderID ");
		}
		return 0;
	}

	public static int getNewPizzaID() throws SQLException, IOException{

		if(connect_to_db()){
			String query = "SELECT PizzaID FROM pizza ORDER BY PizzaID DESC LIMIT 1";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				int pizzaID = rs.getInt("PizzaID");
				return ++pizzaID;
			}

		}else{
			System.out.println("ERROR: Connecting to database - getNewPizzaID");
		}
		return 0;
	}
	public static int getNewCustomerID() throws SQLException, IOException{

		if(connect_to_db()){
			String query = "SELECT CustomerID FROM customer ORDER BY CustomerID DESC LIMIT 1";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				int customerID = rs.getInt("CustomerID");
				return customerID;
			}

		}else{
			System.out.println("ERROR: Connecting to database - getNewCustomerID");
		}
		return 0;
	}

	public static void addOrder(Order o) throws SQLException, IOException
	{
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 *
		 */
		if(connect_to_db()){
			String query =
					"INSERT INTO orders (OrderID,OrderCustomerID,OrderType,Timestamp,OrderTotalCost,OrderTotalPrice,OrderIsComplete)" +
							" VALUES (?,?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(o.getOrderID()));
			stmt.setString(2, String.valueOf(o.getCustID()));
			stmt.setString(3, String.valueOf(o.getOrderType()));
			stmt.setString(4, String.valueOf(o.getDate()));
			stmt.setString(5, String.valueOf(o.getBusPrice()));
			stmt.setString(6, String.valueOf(o.getCustPrice()));
			stmt.setString(7, String.valueOf(o.getIsComplete()));
			stmt.executeUpdate();

			String type = o.getOrderType();
			if(type.equals(dine_in)){
				DineinOrder dineIn = (DineinOrder)o;
				query = "INSERT INTO dine_in (OrderID,DineInTableNumber) Values (?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1,String.valueOf(o.getOrderID()));
				stmt.setString(2,String.valueOf(dineIn.getTableNum()));
				stmt.executeUpdate();
			} else if( type == pickup){
				PickupOrder pickUp = (PickupOrder) o;
				query = "INSERT INTO pick_up (OrderID,IsPickedUp) Values (?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1,String.valueOf(o.getOrderID()));
				stmt.setString(2,String.valueOf(pickUp.getIsPickedUp()));
				stmt.executeUpdate();
			}else if (type == delivery){
				DeliveryOrder delivery = (DeliveryOrder) o;
				query = "INSERT INTO delivery (OrderID,HouseNumber,Street,City,State,Zipcode) " +
						"Values (?,?,?,?,?,?)";
				stmt = conn.prepareStatement(query);
				String address = delivery.getAddress();
				String[] parts = address.split("\n");
				stmt.setString(1,String.valueOf(o.getOrderID()));
				stmt.setString(2,parts[0]);
				stmt.setString(3,parts[1]);
				stmt.setString(4,parts[2]);
				stmt.setString(5,parts[3]);
				stmt.setString(6,parts[4]);
				stmt.executeUpdate();
			}
		/*
				for(Pizza p : o.getPizzaList()){
				addPizza(p);
				updateTotalOrderCosts(o,p);
			}
			for(Discount d : o.getDiscountList()){
				addOrderDiscounts(o.getOrderID(),d.getDiscountID());
			}
		 */




			conn.close();
		}
		else{
			System.out.println("ERROR: Connecting to the database - addOrder");
		}



		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	public static void updateTotalOrderCosts(Order o, Pizza p) throws SQLException, IOException {
		if(connect_to_db()){
			int orderID = o.getOrderID();
			double newCost = o.getBusPrice() + p.getBusPrice();
			double newPrice = o.getCustPrice() + p.getCustPrice();
			o.setBusPrice(newCost);
			o.setCustPrice(newPrice);

			String query = "UPDATE orders \n" +
					"SET OrderTotalCost = ?, OrderTotalPrice = ? \n" +
					"WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(newCost));
			stmt.setString(2, String.valueOf(newPrice));
			stmt.setString(3, String.valueOf(orderID));
			stmt.executeUpdate();
			conn.close();

		}
		else{
			System.out.println("ERROR: Connecting to the database - updateTotalOrderCosts");
		}
	}

	public static void addOrderDiscounts(int orderID,int discountID) throws SQLException,IOException{
		if(connect_to_db()){
			String query = "INSERT INTO orders_discount (OrderID,DiscountID) Values (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(orderID));
			stmt.setString(2, String.valueOf(discountID));
			stmt.executeUpdate();
			conn.close();

		}else{
			System.out.println("ERROR: Connecting to the database - addOrderDiscounts");
		}
	}

	public static void addPizzaDiscounts(int discountID,int pizzaID) throws SQLException,IOException{
		if(connect_to_db()){
			String query = "INSERT INTO pizza_discount (DiscountID,PizzaID) Values (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(discountID));
			stmt.setString(2, String.valueOf(pizzaID));
			stmt.executeUpdate();
			conn.close();

		}else{
			System.out.println("ERROR: Connecting to the database - addOrderDiscounts");
		}
	}
	public static void updatePizza(Pizza p) throws SQLException, IOException{
		if(connect_to_db()){

			String query = 	"UPDATE pizza " +
					"SET PizzaOrderID = ?, " +
					"PizzaSize = ?, " +
					"PizzaCrust = ?, " +
					"PizzaPrice = ?, " +
					"PizzaCost = ?, " +
					"PizzaCompleted = ?, " +
					"PizzaDate = ? " +
					"WHERE PizzaID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(p.getOrderID()));
			stmt.setString(2, String.valueOf(p.getSize()));
			stmt.setString(3, String.valueOf(p.getCrustType()));
			stmt.setString(4, String.valueOf(p.getCustPrice()));
			stmt.setString(5, String.valueOf(p.getBusPrice()));
			stmt.setString(6, String.valueOf(p.getPizzaState()));
			stmt.setString(7, String.valueOf(p.getPizzaDate()));
			stmt.setString(1, String.valueOf(p.getPizzaID()));
			stmt.executeUpdate();
			conn.close();


			for(Topping t : p.getToppings()){
				useTopping(p,t,p.getIsDoubleArray()[t.getTopID()]);
			}
			for(Discount d : p.getDiscounts()){
				addPizzaDiscounts(d.getDiscountID(),p.getPizzaID());
			}
		} else{
			System.out.println("ERROR: Connecting to the database");
		}
	}
	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts and toppings associated with the pizza,
		 * there are other methods below that may help with that process.
		 *
		 */
		if(connect_to_db()){
			String query = "INSERT INTO pizza " +
					"(PizzaID,PizzaOrderID,PizzaSize,PizzaCrust,PizzaPrice,PizzaCost,PizzaCompleted,PizzaDate)" +
					"VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(p.getPizzaID()));
			stmt.setString(2, String.valueOf(p.getOrderID()));
			stmt.setString(3, String.valueOf(p.getSize()));
			stmt.setString(4, String.valueOf(p.getCrustType()));
			stmt.setString(5, String.valueOf(p.getCustPrice()));
			stmt.setString(6, String.valueOf(p.getBusPrice()));
			stmt.setString(7, String.valueOf(p.getPizzaState()));
			stmt.setString(8, String.valueOf(p.getPizzaDate()));
			stmt.executeUpdate();
			conn.close();


			for(Topping t : p.getToppings()){
				useTopping(p,t,p.getIsDoubleArray()[t.getTopID()]);
			}
			for(Discount d : p.getDiscounts()){
				addPizzaDiscounts(d.getDiscountID(),p.getPizzaID());
			}


		}
		else{
			System.out.println("ERROR: Connecting to the database - addPizza");
		}




		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this method will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		/*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 *
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 *
		 */

		if(connect_to_db()){
			String query = "INSERT INTO pizza_topping (ToppingID,PizzaID,Extra_Toppings) VALUES (?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(t.getTopID()));
			stmt.setString(2, String.valueOf(p.getPizzaID()));
			stmt.setInt(3, isDoubled ? 1 : 0);
			stmt.executeUpdate();

			if(isDoubled){
				addToInventory(t,-2);
			}else{
				addToInventory(t,-1);
			}


			conn.close();
		}
		else{
			System.out.println("ERROR: Connecting to the database - useTopping");
		}





		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{

		/*
		 * This method connects a discount with a Pizza in the database.
		 *
		 * What that means will be specific to your implementatinon.
		 */
		p.addDiscounts(d);
		if(connect_to_db()){
			String query = "INSERT INTO pizza_discount (DiscountID,PizzaID) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(d.getDiscountID()));
			stmt.setString(2, String.valueOf(p.getPizzaID()));
			stmt.executeUpdate();
			conn.close();
		}else{
			System.out.println("ERROR: Connecting to the database - usePizzaDiscount");
		}





		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		/*
		 * This method connects a discount with an order in the database
		 *
		 * You might use this, you might not depending on where / how to want to update
		 * this information in the dabast
		 */

		o.addDiscount(d);
		if(connect_to_db()){
			String query = "INSERT INTO orders_discount (DiscountID,OrderID) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(d.getDiscountID()));
			stmt.setString(2, String.valueOf(o.getOrderID()));
			stmt.executeUpdate();
			conn.close();
		}else{
			System.out.println("ERROR: Connecting to the database - useOrderDiscount");
		}




		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void addCustomer(Customer c) throws SQLException, IOException {
		/*
		 * This method adds a new customer to the database.
		 *
		 */
		if (connect_to_db()){
			String query = "INSERT INTO customer (CustomerFirstName, CustomerLastName, CustomerPhoneNumber) VALUES (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, c.getFName());
			stmt.setString(2, c.getLName());
			stmt.setString(3, c.getPhone());
			stmt.executeUpdate();
			conn.close();
		}else{
			System.out.println("ERROR connection to the database!");
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void completeOrder(Order o) throws SQLException, IOException {

		/*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 *
		 */
		if(connect_to_db()){
			String query = "UPDATE orders o " +
					"JOIN pizza p ON o.OrderID = p.PizzaOrderID " +
					"SET " +
					"o.OrderIsComplete = 1, " +
					"p.PizzaCompleted = 1 " +
					"WHERE " +
					"o.OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(o.getOrderID()));
			stmt.executeUpdate();
			conn.close();
		}
		else{
			System.out.println("ERROR: Connecting to the database - completeOrder");
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static Order getOrderByID(String orderid) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM orders WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,orderid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int orderID = rs.getInt("OrderID");
				int custID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");

				Order order = new Order(orderID, custID, orderType, date, cost, price, isComplete);
				conn.close();
				return order;
			}
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}

	public static DineinOrder getOrderDineINOrder(Order o) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM dine_in WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(o.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int tableNumber = rs.getInt("DineInTableNumber");


				DineinOrder order = new DineinOrder(o.getOrderID(), o.getCustID(), o.getDate(), o.getBusPrice(), o.getCustPrice(), o.getIsComplete(),tableNumber);
				conn.close();
				return order;
			}
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}

	public static PickupOrder getPickUpOrder(Order o) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM pick_up WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(o.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int isPickedUp = rs.getInt("DineInTableNumber");


				PickupOrder order = new PickupOrder(o.getOrderID(), o.getCustID(), o.getDate(), o.getBusPrice(), o.getCustPrice(),isPickedUp,o.getIsComplete());
				conn.close();
				return order;
			}
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}

	public static DeliveryOrder getDeliveryOrder(Order o) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM delivery WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(o.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String address = String.valueOf(rs.getInt("HouseNumber")) + rs.getString("Street") + rs.getString("City")
						+ rs.getString("Zipcode");


				DeliveryOrder order = new DeliveryOrder(o.getOrderID(), o.getCustID(), o.getDate(), o.getBusPrice(), o.getCustPrice(),o.getIsComplete(),address);
				conn.close();
				return order;
			}
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}

	public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {

		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 *
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		ArrayList<Order> orders = new ArrayList<>();
		if(connect_to_db()){
			String query = "SELECT * FROM orders WHERE OrderIsComplete = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(openOnly));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int orderID = rs.getInt("OrderID");
				int custID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");

				Order order = new Order(orderID,custID,orderType,date,cost,price,isComplete);
				orders.add(order);



			}
			conn.close();


			return orders;

		} else{
			System.out.println("ERROR: Connecting to the database - getOrders");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;
	}

	public static ArrayList<Order> getAllOrders() throws SQLException, IOException {

		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 *
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		ArrayList<Order> orders = new ArrayList<>();
		if(connect_to_db()){
			String query = "SELECT * FROM orders";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int orderID = rs.getInt("OrderID");
				int custID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");
				Order order = new Order(orderID,custID,orderType,date,cost,price,isComplete);
				orders.add(order);

			}
			conn.close();

			return orders;


		} else{
			System.out.println("ERROR: Connecting to the database - getOrders");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;
	}

	public static Order getLastOrder(){
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */
		try {
			connect_to_db();

			String query = "SELECT * FROM orders ORDER BY OrderID DESC LIMIT 1;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			Order order;

			if (rs.next()) {
				int orderID = rs.getInt("OrderID");
				int orderCustermerID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");

				order = new Order(orderID, orderCustermerID, orderType, date, price, cost, isComplete);
				getPizzasInOrder(order);
				addDiscountsInOrder(order);

				return order;
			} else {
				return null;
			}


		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}




	public static ArrayList<Order> getOrdersByDate(String targetDate) throws SQLException, IOException{
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *
		 */
		ArrayList<Order> orders = new ArrayList<>();
		if(connect_to_db()){
			String query = "SELECT * FROM orders WHERE Timestamp >= ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(targetDate));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int orderID = rs.getInt("OrderID");
				int custID = rs.getInt("OrderCustomerID");
				String orderType = rs.getString("OrderType");
				String date = rs.getString("Timestamp");
				double cost = rs.getDouble("OrderTotalCost");
				double price = rs.getDouble("OrderTotalPrice");
				int isComplete = rs.getInt("OrderIsComplete");

				Order order = new Order(orderID,custID,orderType,date,cost,price,isComplete);
				orders.add(order);
			}
			conn.close();

			return orders;


		} else{
			System.out.println("ERROR: Connecting to the database - getOrders");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;
	}

	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database for all the available discounts and
		 * return them in an arrayList of discounts.
		 *
		 */



		ArrayList<Discount> discountList = new ArrayList<>();
		if(connect_to_db()){

			String query = "SELECT * FROM discount";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int discountID = rs.getInt("DiscountID");
				String discountName = rs.getString("DiscountName");
				double discountDollarOff = rs.getDouble("DiscountDollarsOff");
				double discountPercentageOff = rs.getDouble("DiscountPercentageOff");
				Discount discount;
				if (discountDollarOff > discountPercentageOff){
					discount = new Discount (discountID,discountName,discountDollarOff, false);
				}
				else{
					discount = new Discount (discountID,discountName,discountPercentageOff, true);
				}
				discountList.add(discount);


			}
			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return discountList;

	}

	public static Discount findDiscountByName(String name){
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *
		 */
		try {
			connect_to_db();

			String query = "SELECT * FROM discount WHERE DiscountName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int discountID = rs.getInt("DiscountID");
				String discountName = rs.getString("DiscountName");
				double discountDollarOff = rs.getDouble("DiscountDollarsOff");
				double discountPercentageOff = rs.getDouble("DiscountPercentageOff");

				Discount discount;
				if (discountDollarOff > discountPercentageOff) {
					discount = new Discount(discountID, discountName, discountDollarOff, false);
				} else {
					discount = new Discount(discountID, discountName, discountPercentageOff, true);
				}

				return discount;
			} else {
				return null;
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}

	public static Discount findDiscountByID(int id){
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *
		 */
		try {
			connect_to_db();

			String query = "SELECT * FROM discount WHERE DiscountID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(id));
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int discountID = rs.getInt("DiscountID");
				String discountName = rs.getString("DiscountName");
				double discountDollarOff = rs.getDouble("DiscountDollarsOff");
				double discountPercentageOff = rs.getDouble("DiscountPercentageOff");

				Discount discount;
				if (discountDollarOff > discountPercentageOff) {
					discount = new Discount(discountID, discountName, discountDollarOff, false);
				} else {
					discount = new Discount(discountID, discountName, discountPercentageOff, true);
				}

				return discount;
			} else {
				return null;
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		/*
		 * Query the data for all the customers and return an arrayList of all the customers.
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */

		ArrayList<Customer> customerList = new ArrayList<>();
		if(connect_to_db()){

			String query = "SELECT * FROM customer ORDER BY CustomerID";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int custID = rs.getInt("CustomerID");
				String fName = rs.getString("CustomerFirstName");
				String lName = rs.getString("CustomerLastName");
				String phone = rs.getString("CustomerPhoneNumber");
				Customer customer = new Customer(custID, fName, lName, phone);
				customerList.add(customer);

			}
			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return customerList;
	}

	public static Customer findCustomerByPhone(String phoneNumber){
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *
		 */

		try {
			connect_to_db();

			String query = "SELECT * FROM customer WHERE CustomerPhoneNumber = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, phoneNumber);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int custID = rs.getInt("CustomerID");
				String fName = rs.getString("CustomerFirstName");
				String lName = rs.getString("CustomerLastName");
				String phone = rs.getString("CustomerPhoneNumber");
				Customer customer = new Customer(custID, fName, lName, phone);
				return customer;
			} else {
				return null;
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}

	public static Topping findToppingByID(int toppingID){
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *
		 */

		try {
			connect_to_db();

			String query = "SELECT * FROM topping WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(toppingID));
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int topID = rs.getInt("ToppingID");
				String topName = rs.getString("ToppingName");
				double perAMT = rs.getDouble("ToppingSmall");
				double medAMT = rs.getDouble("ToppingMedium");
				double lgAMT = rs.getDouble("ToppingLarge");
				double xLAMT = rs.getDouble("ToppingXLarge");
				double custPrice = rs.getDouble("ToppingPrice");
				double busPrice = rs.getDouble("ToppingCost");
				int minINVT = rs.getInt("ToppingMinimum");
				int curINVT = rs.getInt("ToppingInventory");

				Topping topping = new Topping(topID, topName, perAMT, medAMT, lgAMT, xLAMT, custPrice, busPrice, minINVT, curINVT);
				return topping;
			} else {
				return null;
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database for the aviable toppings and
		 * return an arrayList of all the available toppings.
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */

		ArrayList<Topping> toppings = new ArrayList<>();
		if(connect_to_db()){

			String query = "SELECT * FROM topping ORDER BY ToppingID";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int topID = rs.getInt("ToppingID");
				String topName = rs.getString("ToppingName");
				double perAMT = rs.getDouble("ToppingSmall");
				double medAMT = rs.getDouble("ToppingMedium");
				double lgAMT = rs.getDouble("ToppingLarge");
				double xLAMT = rs.getDouble("ToppingXLarge");
				double custPrice = rs.getDouble("ToppingPrice");
				double busPrice = rs.getDouble("ToppingCost");
				int minINVT = rs.getInt("ToppingMinimum");
				int curINVT = rs.getInt("ToppingInventory");

				Topping topping = new Topping(topID, topName, perAMT, medAMT, lgAMT, xLAMT, custPrice, busPrice, minINVT, curINVT);
				toppings.add(topping);




			}
			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return toppings;
	}

	public static Topping findToppingByName(String name){
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *
		 */

		try {
			connect_to_db();

			String query = "SELECT * FROM topping WHERE ToppingName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, String.valueOf(name));
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int topID = rs.getInt("ToppingID");
				String topName = rs.getString("ToppingName");
				double perAMT = rs.getDouble("ToppingSmall");
				double medAMT = rs.getDouble("ToppingMedium");
				double lgAMT = rs.getDouble("ToppingLarge");
				double xLAMT = rs.getDouble("ToppingXLarge");
				double custPrice = rs.getDouble("ToppingPrice");
				double busPrice = rs.getDouble("ToppingCost");
				int minINVT = rs.getInt("ToppingMinimum");
				int curINVT = rs.getInt("ToppingInventory");

				Topping topping = new Topping(topID, topName, perAMT, medAMT, lgAMT, xLAMT, custPrice, busPrice, minINVT, curINVT);
				return topping;
			} else {
				return null;
			}
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("ERROR closing the Database connection!");
				e.printStackTrace();
			}
		}
	}


	public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 *
		 * */
		if(connect_to_db()){
			int toppingID = t.getTopID();
			int curInvt = t.getCurINVT();
			int newInvt = curInvt + (int)quantity;

			String query = "UPDATE topping SET ToppingInventory = ? WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, newInvt);
			stmt.setInt(2, toppingID);
			stmt.executeUpdate();


			conn.close();
		}
		else{
			System.out.println("ERROR connecting to the database");
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database fro the base customer price for that size and crust pizza.
		 *
		 */

		double price = 0.0;
		if(connect_to_db()){

			String query = "SELECT BasePrice FROM base_price WHERE BasePriceSize = ? AND BasePriceCrust = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, size);
			stmt.setString(2, crust);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				price = rs.getDouble("BasePrice");
			}
			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return price;
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database fro the base business price for that size and crust pizza.
		 *
		 */
		double cost = 0.0;
		if(connect_to_db()){

			String query = "SELECT BaseCost FROM base_price WHERE BasePriceSize = ? AND BasePriceCrust = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, size);
			stmt.setString(2, crust);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				cost = rs.getDouble("BaseCost");
			}
			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return cost;
	}

	public static void printInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Queries the database and prints the current topping list with quantities.
		 *
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 */

		if(connect_to_db()){

			String query = "SELECT ToppingID,ToppingName, ToppingInventory FROM topping ORDER BY ToppingName";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Topping ID\tTopping Name \t Inventory Level");
			while (rs.next()){
				int toppingID = rs.getInt("ToppingID");
				String toppingName = rs.getString("ToppingName");
				int toppingInventory = rs.getInt("ToppingInventory");
				System.out.println(String.format("%-12d %-20s %d", toppingID, toppingName, toppingInventory));
			}

			conn.close();
		} else{
			System.out.println("ERROR connecting to the Database!");
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void printToppingPopReport() throws SQLException, IOException
	{
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 *
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 */
		if(connect_to_db()){
			String query = "SELECT * FROM ToppingPopularity;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Topping\t\t\t\tTopping Count");
			while(rs.next()){
				String toppingName = rs.getString("Topping");
				int toppingCount = rs.getInt("ToppingCount");
				System.out.printf("%-20s\t%d\n",toppingName,toppingCount);
			}
			conn.close();
		}else{
			System.out.println("ERROR Connecting to the database - printToppingPopReport");
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 *
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 */
		if(connect_to_db()){
			String query = "SELECT * FROM ProfitByPizza;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			System.out.printf("%-10s\t%-10s\t%-10s\t%-10s%n", "Size", "Crust", "Profit", "OrderMonth");
			while(rs.next()){
				String size = rs.getString("Size");
				String crust = rs.getString("Crust");
				double profit = rs.getDouble("Profit");
				String orderMonth = rs.getString("OrderMonth");
				System.out.printf("%-10s\t%-10s\t%-10.2f\t%-10s%n", size, crust, profit, orderMonth);

			}
			conn.close();
		}else{
			System.out.println("ERROR Connecting to the database - printProfitByPizzaReport");
		}




		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void printProfitByOrderType() throws SQLException, IOException
	{

		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 *
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 */

		if(connect_to_db()){
			String query = "SELECT * FROM ProfitByOrderType";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			System.out.printf("%-15s\t%-15s\t%-10s\t%-10s\t%-10s%n", "Customer Type", "Order Month", "Price", "Cost", "Profit");
			while(rs.next()){
				String customerType = rs.getString("customerType");
				String orderMonth = rs.getString("OrderMonth");
				double price = rs.getDouble("TotalOrderPrice");
				double cost = rs.getDouble("TotalOrderCost");
				double profit = rs.getDouble("Profit");
				System.out.printf("%-15s\t%-15s\t%-10.2f\t%-10.2f\t%-10.2f%n",
						customerType, orderMonth, price, cost,profit);

			}
			conn.close();
		}else{
			System.out.println("ERROR Connecting to the database - printProfitByOrderType");
		}




		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static double getSmallUnits(int toppingID) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT ToppingSmall as Units FROM topping WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(toppingID));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				double units = rs.getDouble("Units");
				return units;
			}
			conn.close();

		}{
			System.out.println("ERROR: Connecting to the database");
		}
		return 0.0;
	}
	public static double getMediumUnits(int toppingID) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT ToppingMedium as Units FROM topping WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(toppingID));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				double units = rs.getDouble("Units");
				return units;
			}
			conn.close();

		}{
			System.out.println("ERROR: Connecting to the database");
		}
		return 0.0;
	}
	public static double getLargeUnits(int toppingID) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT ToppingLarge as Units FROM topping WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(toppingID));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				double units = rs.getDouble("Units");
				return units;
			}
			conn.close();

		}{
			System.out.println("ERROR: Connecting to the database");
		}
		return 0.0;
	}
	public static double getXLargeUnits(int toppingID) throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT ToppingXLarge as Units FROM topping WHERE ToppingID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(toppingID));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				double units = rs.getDouble("Units");
				return units;
			}
			conn.close();

		}{
			System.out.println("ERROR: Connecting to the database");
		}
		return 0.0;
	}
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
		/*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 *
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

		connect_to_db();

		/*
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 *

		String cname1 = "";
		String query = "SELECT CustomerFirstName, CustomerLastName FROM customer WHERE CustID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, String.valueOf(CustID));
		ResultSet rset = stmt.executeQuery();
		//ResultSet rset = stmt.executeQuery(query);
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2);
		}
			 */
		/*
		 * an example of the same query using a prepared statement...
		 *
		 */
		String cname2 = "";
		PreparedStatement os;
		ResultSet rset2;
		String query2;
		query2 = "Select CustomerFirstName, CustomerLastName From customer WHERE CustomerID=?;";
		os = conn.prepareStatement(query2);
		os.setInt(1, CustID);
		rset2 = os.executeQuery();
		while(rset2.next())
		{
			cname2 = rset2.getString("CustomerFirstName") + " " + rset2.getString("CustomerLastName"); // note the use of field names in the getSting methods
		}

		conn.close();
		return cname2; // OR cname2
	}

	/*
	 * The next 3 private methods help get the individual components of a SQL datetime object.
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}
/*

	public static ArrayList<Pizza> getPizzas(Order o)throws SQLException, IOException{
		if(connect_to_db()){
			String query = "SELECT * FROM pizza WHERE PizzaOrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(o.getOrderID()));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				int pizzaID = rs.getInt("PizzaID");
				String pizzaSize = rs.getString("PizzaSize");
				String pizzaCrust = rs.getString("PizzaCrust");
				int orderID = o.getOrderID();
				double pizzaPrice = rs.getDouble("pizzaPrice");
				double pizzaCost = rs.getDouble("PizzaCost");
				String pizzaComplete = rs.getString("PizzaComplete");
				String pizzaDate = rs.getString("PizzaDate");
				Pizza p = new Pizza(pizzaID,pizzaSize,pizzaCrust,orderID,pizzaComplete,pizzaDate,pizzaPrice,pizzaCost);
				getToppingsOnPizza(p);
			}
			conn.close();
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}

		return null;
	}
	public static ArrayList<Topping> getToppingsOnPizza(Pizza p)throws SQLException, IOException{

		if(connect_to_db()){
			int pizzaID = p.getPizzaID();
			String query = "SELECT * FROM pizza_topping WHERE PizzaID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,String.valueOf(pizzaID));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){

			}
			conn.close();
		} else{
			System.out.println("ERROR: Connecting to the database - getOrderByID");
		}
		return null;
	}

	public static ArrayList<Discount> getPizzaDiscounts(Pizza p)throws SQLException, IOException{
		return null;
	}

	public static ArrayList<Discount> getOrderDiscounts(Order o) throws SQLException, IOException{}


 */


