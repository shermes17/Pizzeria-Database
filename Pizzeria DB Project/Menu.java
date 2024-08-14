package cpsc4620;

import javax.swing.text.AttributeSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the methods for each of the menu options.
 * 
 * This file should not need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove methods as you see necessary. But you MUST have all of the menu methods (including exit!)
 * 
 * Simply removing menu methods because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 */

public class Menu {

	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public int CurrentPizzaID = 0;

	public static void main(String[] args) throws SQLException, IOException {

		System.out.println("Welcome to Pizzas-R-Us!");
		
		int menu_option = 0;

		// present a menu of options and take their selection
		
		PrintMenu();
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{

		/*
		 * EnterOrder should do the following:
		 * 
		 * Ask if the order is delivery, pickup, or dinein
		 *   if dine in....ask for table number
		 *   if pickup...
		 *   if delivery...
		 * 
		 * Then, build the pizza(s) for the order (there's a method for this)
		 *  until there are no more pizzas for the order
		 *  add the pizzas to the order
		 *
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * return to menu
		 * 
		 * make sure you use the prompts below in the correct order!
		 */

		 // User Input Prompts...
		System.out.println("Is this order for: \n1.) Dine-in\n2.) Pick-up\n3.) Delivery\nEnter the number of your choice:");
		int orderInput = Integer.parseInt(reader.readLine());

		Order order;
		// get current timestamp as a string
		Date day = Calendar.getInstance().getTime();
		DateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		String date = format.format(day);

		// get auto_incremented orderID
		int orderID = DBNinja.getNewOrderID();
		int customerID = 1; // 1 should be initialized as a placeholder customer for dinein orders
		if(orderInput == 1){ // Dine In
			System.out.println("What is the table number for this order?");
			int tableNumber = Integer.parseInt(reader.readLine());
			order = new DineinOrder(orderID,customerID, date,0, 0, 0, tableNumber);
		}
		else if (orderInput == 2){ // Pick Up

			System.out.println("Is this order for an existing customer? Answer y/n: ");
			char pu = reader.readLine().charAt(0);
			Character.toLowerCase(pu);
			if(pu == 'y'){
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
				System.out.println("Which customer is this order for? Enter ID Number:");
				customerID = Integer.parseInt(reader.readLine());
			}else if (pu == 'n'){
				EnterCustomer();
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
				System.out.println("Which customer is this order for? Enter ID Number:");
				customerID = Integer.parseInt(reader.readLine());
			}else{
				System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
				return;
			}
			order = new PickupOrder(orderID, customerID,date, 0,0,0, 0);



		}
		else if (orderInput == 3){ // Delivery
			System.out.println("Is this order for an existing customer? Answer y/n: ");
			char d = reader.readLine().charAt(0);
			Character.toLowerCase(d);
			if(d == 'y'){
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
				System.out.println("Which customer is this order for? Enter ID Number:");
				customerID = Integer.parseInt(reader.readLine());
			}else if (d == 'n'){
				EnterCustomer();
				customerID = DBNinja.getNewCustomerID();
			}else{
				System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
				return;
			}


			System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
			int houseNumber = Integer.parseInt(reader.readLine());
			System.out.println("What is the Street for this order? (e.g., Smile Street)");
			String street = reader.readLine();
			System.out.println("What is the City for this order? (e.g., Greenville)");
			String city = reader.readLine();
			System.out.println("What is the State for this order? (e.g., SC)");
			String state = reader.readLine();
			System.out.println("What is the Zip Code for this order? (e.g., 20605)");
			String zipcode = reader.readLine();

			String address = String.valueOf(houseNumber) + "\n" + street + "\n" + city + "\n" + state + "\n" + zipcode;
			order = new DeliveryOrder(orderID, customerID,date, 0, 0, 0,address);
		}else{
			System.out.println("ERROR: Invalid Input!");
			return;
		}

		DBNinja.addOrder(order);
		String pizzaInput = "";
		System.out.println("Let's build a pizza!");
		do {
			if(!pizzaInput.equals("-1")){
				Pizza p = buildPizza(orderID);
				order.addPizza(p);
			}
			System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
			pizzaInput = reader.readLine();
		} while (!pizzaInput.equals("-1"));


		char discountInput = 'y';
		String discountLoopInput = "";
		System.out.println("Do you want to add discounts to this order? Enter y/n?");
		discountInput = Character.toLowerCase(reader.readLine().charAt(0));
			if(discountInput == 'y'){
				do {
					for(Discount d : DBNinja.getDiscountList()){
						System.out.println(d.toString());
					}
					System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
					discountLoopInput = reader.readLine();
					if(!discountLoopInput.equals("-1")){
						order.addDiscount((DBNinja.findDiscountByID(Integer.parseInt(discountLoopInput))));
					}
				} while(!discountLoopInput.equals("-1"));
			}

		for(Pizza p : order.getPizzaList()){
			DBNinja.updateTotalOrderCosts(order,p);
		}
		for(Discount d : order.getDiscountList()){
			DBNinja.addOrderDiscounts(order.getOrderID(),d.getDiscountID());
		}
		

		System.out.println("Finished adding order...Returning to menu...");
	}
	
	
	public static void viewCustomers() throws SQLException, IOException 
	{
		/*
		 * Simply print out all of the customers from the database. 
		 */
		
		ArrayList<Customer> customerList = DBNinja.getCustomerList();

		for (Customer customer : customerList){
			System.out.println("CustId="+customer.getCustID()
				+" | Name= " + customer.getFName() + " " + customer.getLName()
				+ ", Phone= " + customer.getPhone());
		}
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask for the name of the customer:
		 *   First Name <space> Last Name
		 * 
		 * Ask for the  phone number.
		 *   (##########) (No dash/space)
		 * 
		 * Once you get the name and phone number, add it to the DB
		 */

		// User Input Prompts...
		 System.out.println("What is this customer's name (first <space> last)");
		 String name = reader.readLine();
		 System.out.println("What is this customer's phone number (##########) (No dash/space)");
		 String phone = reader.readLine();
		 // parse Name
		 String[] names = name.split(" ");
		 String fName = names[0];
		 String lName = names[1];

		Customer customer = new Customer(0,fName,lName,phone);
		DBNinja.addCustomer(customer);

	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
		/*  
		* This method allows the user to select between three different views of the Order history:
		* The program must display:
		* a.	all open orders
		* b.	all completed orders 
		* c.	all the orders (open and completed) since a specific date (inclusive)
		* 
		* After displaying the list of orders (in a condensed format) must allow the user to select a specific order for viewing its details.  
		* The details include the full order type information, the pizza information (including pizza discounts), and the order discounts.
		* 
		*/ 
			
		
		// User Input Prompts...
		System.out.println("Would you like to:\n(a) display all orders [open or closed]\n(b) display all open orders\n(c) display all completed [closed] orders\n(d) display orders since a specific date");
		char input = Character.toLowerCase(reader.readLine().charAt(0));
		if(input == 'a'){
			ArrayList<Order> orders = DBNinja.getAllOrders();
			for(Order o : orders){
				System.out.println(o.toSimplePrint());
			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
			String input2 = reader.readLine();
			if(input2.equals("-1")){
				return;
			}

			Order o = DBNinja.getOrderSpecifics(input2);
			System.out.println(o.toString());
			if(o.getDiscountList().isEmpty() == true){
				System.out.println("NO ORDER DISCOUNTS");
			}else{
				for(Discount d : o.getDiscountList()){
					System.out.println("ORDER DISCOUNTS: " + d.getDiscountName());
				}
			}
			if(o.getPizzaList().isEmpty() == true){
				System.out.println("NO PIZZAS");
			}else{
				for(Pizza p : o.getPizzaList()){
					System.out.println(p.toString());
					if(p.getDiscounts().isEmpty() == true){
						System.out.println("NO PIZZA DISCOUNTS");
					}else{
						for(Discount d : p.getDiscounts()){
							System.out.println("PIZZA DISCOUNTS: " + d.getDiscountName());
						}
					}
				}
			}





		}
		else if (input == 'b'){
			ArrayList<Order> orders = DBNinja.getOrders(true);
			for(Order o : orders){
				System.out.println(o.toSimplePrint());
			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
			String input2 = reader.readLine();
			if(input2.equals("-1")){
				return;
			}

			Order o = DBNinja.getOrderSpecifics(input2);
			System.out.println(o.toString());
			if(o.getDiscountList().isEmpty() == true){
				System.out.println("NO ORDER DISCOUNTS");
			}else{
				for(Discount d : o.getDiscountList()){
					System.out.println("ORDER DISCOUNTS: " + d.getDiscountName());
				}
			}
			if(o.getPizzaList().isEmpty() == true){
				System.out.println("NO PIZZAS");
			}else{
				for(Pizza p : o.getPizzaList()){
					System.out.println(p.toString());
					if(p.getDiscounts().isEmpty() == true){
						System.out.println("NO PIZZA DISCOUNTS");
					}else{
						for(Discount d : p.getDiscounts()){
							System.out.println("PIZZA DISCOUNTS: " + d.getDiscountName());
						}
					}
				}
			}

		}
		else if (input == 'c'){
			ArrayList<Order> orders = DBNinja.getOrders(false);
			for(Order o : orders){
				System.out.println(o.toSimplePrint());
			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
			String input2 = reader.readLine();
			if(input2.equals("-1")){
				return;
			}

			Order o = DBNinja.getOrderSpecifics(input2);
			System.out.println(o.toString());
			if(o.getDiscountList().isEmpty() == true){
				System.out.println("NO ORDER DISCOUNTS");
			}else{
				for(Discount d : o.getDiscountList()){
					System.out.println("ORDER DISCOUNTS: " + d.getDiscountName());
				}
			}
			if(o.getPizzaList().isEmpty() == true){
				System.out.println("NO PIZZAS");
			}else{
				for(Pizza p : o.getPizzaList()){
					System.out.println(p.toString());
					if(p.getDiscounts().isEmpty() == true){
						System.out.println("NO PIZZA DISCOUNTS");
					}else{
						for(Discount d : p.getDiscounts()){
							System.out.println("PIZZA DISCOUNTS: " + d.getDiscountName());
						}
					}
				}
			}

		}
		else if (input =='d'){
			System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
			String date = reader.readLine();
			ArrayList<Order> orders = DBNinja.getOrdersByDate(date);
			for(Order o : orders){
				System.out.println(o.toSimplePrint());
			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
			String input2 = reader.readLine();
			if(input2.equals("-1")){
				return;
			}

			Order o = DBNinja.getOrderSpecifics(input2);
			System.out.println(o.toString());
			if(o.getDiscountList().isEmpty() == true){
				System.out.println("NO ORDER DISCOUNTS");
			}else{
				for(Discount d : o.getDiscountList()){
					System.out.println("ORDER DISCOUNTS: " + d.getDiscountName());
				}
			}
			if(o.getPizzaList().isEmpty() == true){
				System.out.println("NO PIZZAS");
			}else{
				for(Pizza p : o.getPizzaList()){
					System.out.println(p.toString());
					if(p.getDiscounts().isEmpty() == true){
						System.out.println("NO PIZZA DISCOUNTS");
					}else{
						for(Discount d : p.getDiscounts()){
							System.out.println("PIZZA DISCOUNTS: " + d.getDiscountName());
						}
					}
				}
			}

		}
		else{
			System.out.println("Incorrect entry, returning to menu.");
		}

	/*
	System.out.println("I don't understand that input, returning to menu");
		System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
		System.out.println("Incorrect entry, returning to menu.");
		System.out.println("No orders to display, returning to menu.");
	 */




	}




	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/*
		 * All orders that are created through java (part 3, not the orders from part 2) should start as incomplete
		 * 
		 * When this method is called, you should print all of the "opoen" orders marked
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */

		ArrayList<Order> orders = DBNinja.getOrders(true);
		if(orders.isEmpty()){
			System.out.println("There are no open orders currently... returning to menu...");
			return;
		}
		ArrayList<Integer> validOrders = new ArrayList<>();
		for(Order o : orders){
			System.out.println(o.toSimplePrint());
			validOrders.add(o.getOrderID());
		}
		System.out.println("Which order would you like mark as complete? Enter the OrderID: ");
		int input = Integer.parseInt(reader.readLine());
		if(!validOrders.contains(input)){
			System.out.println("Incorrect entry, not an option");
			return;
		}
		Order o = DBNinja.getOrderByID(String.valueOf(input));
		DBNinja.completeOrder(o);


	}

	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		/*
		 * Print the inventory. Display the topping ID, name, and current inventory
		*/
		
		DBNinja.printInventory();
		
		
	}


	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping (by ID) they want to add more to and how much to add
		 */

		DBNinja.printInventory();
		// User Input Prompts...
		System.out.println("Which topping do you want to add inventory to? Enter the number: ");
		int toppingID = Integer.parseInt(reader.readLine());
		if (toppingID <= 0 || toppingID > 17){
			System.out.println("Incorrect entry, not an option");
			return;
		}
		System.out.println("How many units would you like to add? ");
		double quantity = Double.parseDouble(reader.readLine());

		Topping t = DBNinja.findToppingByID(toppingID);
		DBNinja.addToInventory(t,quantity);

	}

	// A method that builds a pizza. Used in our add new order method
	public static Pizza buildPizza(int orderID) throws SQLException, IOException 
	{
		
		/*
		 * This is a helper method for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */

		 Pizza pizza = null;




			System.out.println("What size is the pizza?");
			System.out.println("1."+DBNinja.size_s);
			System.out.println("2."+DBNinja.size_m);
			System.out.println("3."+DBNinja.size_l);
			System.out.println("4."+DBNinja.size_xl);
			System.out.println("Enter the corresponding number: ");
			int size = Integer.parseInt(reader.readLine());
			if(size < 1 || size > 4){
				System.out.println("ERROR: Invalid Input");
				return null;
			}
			String crustSize = "";
			switch(size){
				case 1:
					crustSize = DBNinja.size_s;
					break;
				case 2:
					crustSize = DBNinja.size_m;
					break;
				case 3:
					crustSize = DBNinja.size_l;
					break;
				case 4:
					crustSize = DBNinja.size_xl;
					break;
				default:
					break;
			};

			System.out.println("What crust for this pizza?");
			System.out.println("1."+DBNinja.crust_thin);
			System.out.println("2."+DBNinja.crust_orig);
			System.out.println("3."+DBNinja.crust_pan);
			System.out.println("4."+DBNinja.crust_gf);
			System.out.println("Enter the corresponding number: ");
			int crust = Integer.parseInt(reader.readLine());
			if(crust < 1 || crust > 4){
				System.out.println("ERROR: Invalid Input");
				return null;
			}
			String crustType = "";
			switch(crust){
				case 1:
					crustType = DBNinja.crust_thin;
					break;
				case 2:
					crustType = DBNinja.crust_orig;
					break;
				case 3:
					crustType = DBNinja.crust_pan;
					break;
				case 4:
					crustType = DBNinja.crust_gf;
					break;
				default:
					break;
			};
			int pizzaID = DBNinja.getNewPizzaID();
			double price = DBNinja.getBaseCustPrice(crustSize,crustType);
			double cost = DBNinja.getBaseBusPrice(crustSize, crustType);

			Date day = Calendar.getInstance().getTime();
			DateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			String date = format.format(day);

			pizza = new Pizza(pizzaID,crustSize,crustType,orderID,"0",date,price,cost);
			DBNinja.addPizza(pizza);

			ArrayList<Topping> toppings = DBNinja.getToppingList();

			String toppingInput = "";
			do {
				System.out.println("Available Toppings:");

				System.out.printf("%-3s\t%-20s\t%s%n", "ID", "Name", "CurINVT");

				for (Topping t : toppings) {
					System.out.printf("%-3d\t%-20s\t%d%n", t.getTopID(), t.getTopName(), t.getCurINVT());
				}
				System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
				toppingInput = reader.readLine();
				if(!toppingInput.equals("-1")){
					int toppingID = Integer.parseInt(toppingInput);
					if(toppingID < 1 || toppingID > 17){
						System.out.println("ERROR: Invalid Topping ID");
					}
					else{
						Topping topping = DBNinja.findToppingByID(toppingID);
						double units = 0.0;
						double toppingCost = 0.0;
						double toppingPrice = 0.0;
						switch(crustSize){
							case DBNinja.size_s:
								units = DBNinja.getSmallUnits(toppingID);
								break;
							case DBNinja.size_m:
								units = DBNinja.getMediumUnits(toppingID);
								break;
							case DBNinja.size_l:
								units = DBNinja.getLargeUnits(toppingID);
								break;
							case DBNinja.size_xl:
								units = DBNinja.getXLargeUnits(toppingID);
								break;
							default:
								break;
						};
						System.out.println("Do you want to add extra topping? Enter y/n");
						char extraInput = Character.toLowerCase(reader.readLine().charAt(0));
						if (extraInput == 'y'){
							if(topping.getCurINVT() < topping.getMinINVT() +2){
								System.out.println("We don't have enough of that topping to add it...");
							}
							else{
								toppingCost = topping.getBusPrice()* 2 * units;
								toppingPrice = topping.getCustPrice() *2 * units;
								double pizzaCost = pizza.getBusPrice();
								double pizzaPrice = pizza.getCustPrice();
								pizza.addToppings(topping,true);
								pizza.setBusPrice(toppingCost+pizzaCost);
								pizza.setCustPrice(toppingPrice + pizzaPrice);

							}
						}else{
							if(topping.getCurINVT() < topping.getMinINVT() + 1){
								System.out.println("We don't have enough of that topping to add it...");
							}
							else{

								toppingCost = topping.getBusPrice() * units;
								toppingPrice = topping.getCustPrice()  * units;
								double pizzaCost = pizza.getBusPrice();
								double pizzaPrice = pizza.getCustPrice();
								pizza.addToppings(topping,false);
								pizza.setBusPrice(toppingCost+pizzaCost);
								pizza.setCustPrice(toppingPrice + pizzaPrice);

							}
						}


					}
				}
			}while (!toppingInput.equals("-1"));



		System.out.println("Do you want to add discounts to this Pizza? Enter y/n?");
		char discountInput = Character.toLowerCase(reader.readLine().charAt(0));

		if (discountInput == 'y'){
			String discountLoopInput = "";
			do{
				for(Discount d : DBNinja.getDiscountList()){
					System.out.println(d.toString());
				}
				System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
				discountLoopInput = reader.readLine();
				if(!discountLoopInput.equals("-1" )){
					pizza.addDiscounts(DBNinja.findDiscountByID(Integer.parseInt(discountLoopInput)));
				}

			} while (!discountLoopInput.equals("-1"));


		}



		return pizza;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This method asks the use which report they want to see and calls the DBNinja method to print the appropriate report.
		 * 
		 */

		// User Input Prompts...
		System.out.println("Which report do you wish to print? Enter\n(a) ToppingPopularity\n(b) ProfitByPizza\n(c) ProfitByOrderType:");
		char input = reader.readLine().charAt(0);
		Character.toLowerCase(input);
		if(input == 'a'){
			DBNinja.printToppingPopReport();
		}else if (input == 'b'){
			DBNinja.printProfitByPizzaReport();
		}else if (input == 'c'){
			DBNinja.printProfitByOrderType();
		}
		else{
			System.out.println("I don't understand that input... returning to menu...");
		}



	}

	//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
	// DO NOT EDIT ANYTHING BELOW HERE, THIS IS NEEDED TESTING.
	// IF YOU EDIT SOMETHING BELOW, IT BREAKS THE AUTOGRADER WHICH MEANS YOUR GRADE WILL BE A 0 (zero)!!

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	/*
	 * autograder controls....do not modiify!
	 */

	public final static String autograder_seed = "6f1b7ea9aac470402d48f7916ea6a010";

	
	private static void autograder_compilation_check() {

		try {
			Order o = null;
			Pizza p = null;
			Topping t = null;
			Discount d = null;
			Customer c = null;
			ArrayList<Order> alo = null;
			ArrayList<Discount> ald = null;
			ArrayList<Customer> alc = null;
			ArrayList<Topping> alt = null;
			double v = 0.0;
			String s = "";

			DBNinja.addOrder(o);
			DBNinja.addPizza(p);
			DBNinja.useTopping(p, t, false);
			DBNinja.usePizzaDiscount(p, d);
			DBNinja.useOrderDiscount(o, d);
			DBNinja.addCustomer(c);
			DBNinja.completeOrder(o);
			alo = DBNinja.getOrders(false);
			o = DBNinja.getLastOrder();
			alo = DBNinja.getOrdersByDate("01/01/1999");
			ald = DBNinja.getDiscountList();
			d = DBNinja.findDiscountByName("Discount");
			alc = DBNinja.getCustomerList();
			c = DBNinja.findCustomerByPhone("0000000000");
			alt = DBNinja.getToppingList();
			t = DBNinja.findToppingByName("Topping");
			DBNinja.addToInventory(t, 1000.0);
			v = DBNinja.getBaseCustPrice("size", "crust");
			v = DBNinja.getBaseBusPrice("size", "crust");
			DBNinja.printInventory();
			DBNinja.printToppingPopReport();
			DBNinja.printProfitByPizzaReport();
			DBNinja.printProfitByOrderType();
			s = DBNinja.getCustomerName(0);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}


}


/*
				Order o = DBNinja.getOrderByID(input2);
			String type = o.getOrderType();
			if(type.equals(DBNinja.dine_in)){
				DineinOrder d = DBNinja.getOrderDineINOrder(o);
				System.out.println(d.toString());
			} else if(type.equals(DBNinja.pickup)){
				PickupOrder p = DBNinja.getPickUpOrder(o);
				System.out.println(p.toString());
			}else{
				DeliveryOrder de = DBNinja.getDeliveryOrder(o);
				System.out.println(de.toString());
			}
			*/