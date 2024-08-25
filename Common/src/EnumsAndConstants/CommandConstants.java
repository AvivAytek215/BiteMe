package EnumsAndConstants;



public enum CommandConstants {

		/**
		 * Login for user;
		 **   
		 * if Succeeded - get user in the dataFromServer;
		 **   
		 * if not - see msg;
		 * @param messageForServer add userName
		 * @param messageForServer add password
		 * @returns isSucceeded true/false
		 */
		Login,
		
		/**
		 * Log out the user.
		 * 
		 * If succeeded - user session is terminated.
		 * 
		 * If not - see msg;
		 * @param messageForServer add user name
		 * @returns isSucceeded true/false
		 */
		LogOut,
		
		Disconnect,
		
		/**
		 * Aprrove user by BranchManager ;
		 **   
		 * if Succeeded - get MSg;
		 **   
		 * if not - see msg;
		 * @param messageForServer add userNameto be aprroved
		 * @returns isSucceeded true/false
		 */
		ApproveUser, 
		

		/**
		 * Get the list of restaurants with given brachLocation.
		 * 
		 * If succeeded - list of restaurants in the dataFromServer.
		 * 
		 * If not - see msg;
		 * @param messageForServer add Branch location
		 * @returns isSucceeded true/false
		 * @returns arrayList of restaurants in data froms erver
		 */
		GetRestaurants,
		
		/**
		 * Get the restaurant given a supplierID.
		 * 
		 * If succeeded - the req restaurants in the dataFromServer.
		 * 
		 * If not - see msg;
		 * @param messageForServer add supplierID
		 * @returns isSucceeded true/false
		 */
		GetRestaurant,
		
		/**
		 * Get the menu given a MenuID.
		 * 
		 * If succeeded - the req menu in the dataFromServer.
		 * 
		 * If not - see msg;
		 * @param messageForServer add supplierID
		 * @returns isSucceeded true/false
		 */
		GetMenuAndItems,


		/**
		 * Update an order given an order.
		 * 
		 * If succeeded - see msg accordingly.
		 * 
		 * If not - see msg;
		 * @param ObjectForServer add a requested order
		 * @returns isSucceeded true/false
		 */
		UpdateOrder,


		/**
		 * Get the Monthly Performence report.
		 * 
		 * If succeeded - Monthly Revenue report details in the dataFromServer.
		 * 
		 * If not - see msg; 
		 * @param messageForServer add string of required branch
		 * @param messageForServer add string of required month (1-12)
		 * @param messageForServer add string of required year (2024 for example)
		 * @returns isSucceeded true/false
		 * @returns isSucceeded true -> you will get MothlyReport in the dataFromServer
		 * @returns isSucceeded true -> get an HashMap<String, Double> revenuePerRestaurant;
		 * in the hash map there is keys of each restaurant (by names) and the value is double of the specific revenue 
		 */
		GetMonthlyRevenueReport,
		
		/**
		 * Get the Monthly Performence report.
		 * 
		 * If succeeded - Monthly Orders report details in the dataFromServer.
		 * 
		 * If not - see msg; 
		 * @param messageForServer add string of required branch
		 * @param messageForServer add string of required month (1-12)
		 * @param messageForServer add string of required year (2024 for example)
		 * @returns isSucceeded true/false
		 * @returns isSucceeded true -> you will get MothlyReport in the dataFromServer
		 * @returns isSucceeded true -> get an HashMap<String, HashMap<String, Integer>> ordersPerRestaurant;
		 * in the first hash map there is keys of each restaurant (by names) and the value
		 * is another hash map with keys according to the items that were ordered
		 * and the value of the last hashmap it an int for amount of each item in specific order
		 */
		GetMonthlyOrdersReport,
		
		/**
		 * Get the Monthly Performence report.
		 * 
		 * If succeeded - Monthly Performence report details in the dataFromServer.
		 * 
		 * If not - see msg; 
		 * @param messageForServer add string of required branch
		 * @param messageForServer add string of required month (1-12)
		 * @param messageForServer add string of required year (2024 for example)
		 * @returns isSucceeded true/false
		 * @returns isSucceeded true -> you will get MothlyReport in the dataFromServer
		 * @returns isSucceeded true -> get the num of total orders in totalOrders (if there is no orders its 0)
		 * @returns isSucceeded true -> get the num of late orders in lateOrders
		 */
		GetMonthlyPerformenceReport,

		/**
		 * Get the quarterly report.
		 * 
		 * If succeeded - quarterly report details in the dataFromServer.
		 * 
		 * If not - see msg;
		 * @param messageForServer add quarter
		 * @param messageForServer add String branch name (location)
		 * @param messageForServer add String of what month to start (1<=i<=12)
		 * @param messageForServer add String of what month to end (i<j<=12)
		 * @param messageForServer add String of what year (2024)
		 * @returns isSucceeded true/false
		 * * @returns isSucceeded true -> you will get QuarterReport in the dataFromServer
		 * @returns isSucceeded true -> get the TreeMap<String, OrderSummary> ordersAndProfitPerDay;
		 * in the treemap there is keys of full dates 
		 * and their values (for each day) its an object of OrderSummary
		 * in the OrderSummary you have int orderCount for how much order in the specific day
		 * and in the double totalProfit there is the profit of that specific day
		 * take note : if there is no orders in a specific day you will have 0 orders and 0 profit 
		 * please keep const quarters by months (for example first quarter is months 1-3 and second is 4-6 and so on)
		 */
		GetQuarterReport,

		
		/**
		 * Get the personal data of user.
		 * 
		 * If succeeded - user details in dataFromServer.
		 * 
		 * If not - see msg;
		 * @param messageForServer add userName
		 * @param messageForServer add password
		 * @returns isSucceeded true/false
		 */
		GetPersonalData,
	
		/**
		 * get the orders data to given username.
		 * 
		 * If succeeded - arraylist of orders in the dataFromServer
		 * 
		 * If not - see message;
		 * @returns isSucceeded true/false
		 */
		GetOrders,
		
		
		/**
		 * not yet works , alex working on that 
		 * creates an order with order,delivery and items in the order
		 * make sure to send full object of the order
		 * make sure to send full object of the delivery
		 * make sure to send full object of the items in order
		 * its a very sensetive method be carefull
		 * If succeeded - order and delivery and items in order.
		 * 
		 * If not - see msg; 
		 * @param dataFromServe give me a full order 
		 * @param objectForServer give me a full delivery (its a combina!)
		 * @returns isSucceeded true/false
		 * @returns isSucceeded true -> see msg
		 * @returns isSucceeded false -> see msg
		 */
		CreateOrder,
		
		/**
		 *gets all items of order by giving OrderId 
		 * If not - see msg; 
		 * @param messageForServer add string of OrderID
		 * @returns isSucceeded true -> you have arrayList of items in DataFromServer
		 * @returns isSucceeded false -> see msg
		 */
		GetItemsInOrder,
		
		/**
		 * get the orders data to given RestaurantID.
		 * 
		 * If succeeded - arraylist of orders in the dataFromServer
		 * 
		 * If not - see message;
		 * @returns isSucceeded true/false
		 */
		GetOrdersForRestaurant,
		
		
		/**
		 * Server is power shut down.
		 * 
		 * you have 5 seconds to show popUp and close entirely your apps
		 */
		TerminateServer,
	
		GetRefund,
		
		getRestaurantIdFromSupplierId,
		
		GetFullOrdersForRestaurant,
		GetUnApprovedUsers,
		RemoveItemFromMenu,
		UpdateItemSpecifications,
		AddItemToMenu,
		PowerLogOut,
		GetFullOrdersForUser,
		UpdateRefund;

	}


