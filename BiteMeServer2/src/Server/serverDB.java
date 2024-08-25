package Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import EnumsAndConstants.BranchLocation;
import EnumsAndConstants.Doneness;
import EnumsAndConstants.OrderStatus;
import EnumsAndConstants.ProductSize;
import EnumsAndConstants.RestaurantType;
import EnumsAndConstants.TypeOfOrder;
import EnumsAndConstants.TypeOfProduct;
import EnumsAndConstants.UserType;
import logic.CommMessage;
import logic.Restaurant;
import logic.items;
import logic.menu;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Orders.OrderSummary;
import logic.Reports.MonthlyReport;
import logic.Reports.QuarterReport;
import logic.Users.User;

/**
 * The serverDB class handles all database operations for the server, 
 * including connecting to the database, managing user sessions, 
 * retrieving and updating orders, restaurants, and generating reports.
 */
public class serverDB {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static final String TIMEZONE_SETTINGS = "?serverTimezone=IST";
	private static Connection conn;

	/**
     * Connects to the specified database using the given credentials.
     *
     * @param username The username for the database.
     * @param password The password for the database.
     * @param dbName   The name of the database to connect to.
     * @return true if the connection is successful, false otherwise.
     */
	public static boolean connectToDB(String username, String password, String dbName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Definition succeed");
		} catch (Exception e) {
			System.out.println("Driver Definition failed");
		}

		try {
			conn = DriverManager.getConnection(DB_URL + dbName + TIMEZONE_SETTINGS, username, password);
			System.out.println("DataBase Connection succeed");
			return true;
		} catch (SQLException e) {
			System.out.println("Database connection failed: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError:" + e.getErrorCode());
			return false;
		}
	}

	/**
     * Logs out a user by setting their isLoggedIn status to false in the database.
     *
     * @param username The username of the user to log out.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object.
     */
	public static CommMessage Logout(String username, CommMessage msg) {
		String updateQuery = "UPDATE biteme.users SET isLoggedIn = 0 WHERE Username = ?";
		PreparedStatement updateStmt;
		try {
			updateStmt = conn.prepareStatement(updateQuery);
			updateStmt.setString(1, username);
			updateStmt.executeUpdate();
			msg.setSucceeded(true);
			msg.setMsg("User logged in successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}

		return msg;
	}

	/**
     * Approves a user by setting their isApproved status to true in the database.
     *
     * @param username The username of the user to approve.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object.
     */
	public static CommMessage approveUser(String username, CommMessage msg) {
		String updateQuery = "UPDATE biteme.users SET isApproved = 1 WHERE Username = ?";
		PreparedStatement updateStmt;
		try {
			updateStmt = conn.prepareStatement(updateQuery);
			updateStmt.setString(1, username);
			updateStmt.executeUpdate();
			msg.setSucceeded(true);
			msg.setMsg("User approved successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}

		return msg;
	}

	/**
	 * Retrieves a menu and its items from the database using the given MenuID.
	 *
	 * @param menuID The ID of the menu to retrieve.
	 * @param msg    The CommMessage object to store the result and any messages.
	 * @return The updated CommMessage object with the menu and items data.
	 */
	public static CommMessage getMenuAndItemsByMenuID(int menuID, CommMessage msg) {
		String menuQuery = "SELECT * FROM menu WHERE MenuID = ?";
		String itemsQuery = "SELECT * FROM items WHERE MenuID = ?";
		menu menuObj = new menu();
		ArrayList<items> itemList = new ArrayList<>();

		try {
			// Retrieve menu details
			PreparedStatement menuStmt = conn.prepareStatement(menuQuery);
			menuStmt.setInt(1, menuID);
			ResultSet menuRs = menuStmt.executeQuery();
			if (menuRs.next()) {
				menuObj.setMenuId(menuRs.getInt("MenuID"));
				menuObj.setRestaurantId(menuRs.getInt("RestaurantID"));
				menuObj.setNumOfItems(menuRs.getInt("NumOfItems"));
			} else {
				msg.setSucceeded(false);
				msg.setMsg("Menu not found");
				return msg;
			}

			// Retrieve items in the menu
			PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery);
			itemsStmt.setInt(1, menuID);
			ResultSet itemsRs = itemsStmt.executeQuery();
			while (itemsRs.next()) {
				TypeOfProduct type = null;
				ProductSize size = null;
				Doneness doneness = null;

				// Check for null values before calling valueOf
				String typeStr = itemsRs.getString("Type");
				if (typeStr != null) {
					type = TypeOfProduct.getEnum(typeStr);
				}

				String sizeStr = itemsRs.getString("Size");
				if (sizeStr != null) {
					size = ProductSize.getEnum(sizeStr);
				}

				String donenessStr = itemsRs.getString("Doneness");
				if (donenessStr != null) {
					doneness = Doneness.getEnum(donenessStr);
				}
				items item = new items(itemsRs.getInt("ItemID"), itemsRs.getString("ItemName"),
						itemsRs.getFloat("Price"), itemsRs.getInt("MenuID"), type, size, doneness);
				itemList.add(item);
			}

			// Set the items in the menu object
			menuObj.setItemsInMenu(itemList);

			// Update the CommMessage object with the menu
			msg.setSucceeded(true);
			msg.setDataFromServer(menuObj);
			msg.setMsg("Menu retrieved successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}
		return msg;
	}

	// input : orderID || output: ArrayList of all the items linked to that orderID
	/**
     * Retrieves all items in an order from the database using the given OrderID.
     *
     * @param orderID The ID of the order to retrieve items for.
     * @param msg     The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of items.
     */	
	public static CommMessage GetItemsInOrder(int orderID, CommMessage msg) {
			try {
				String query = "SELECT * FROM itemsinorder WHERE OrderID = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setInt(1, orderID);
				ResultSet rs = stmt.executeQuery();
				// Check if result set is empty
				if (!rs.isBeforeFirst()) {
					msg.setSucceeded(false);
					msg.setMsg("Didn't find any Items for the given orderID");
					return msg;
				}
				ArrayList<items> items = new ArrayList<items>();
				while (rs.next()) {
					int itemID = rs.getInt("ItemID");
					String foodRequest = rs.getString("foodRequests");
					String subQuery = "SELECT * FROM items WHERE ItemID = ?";
					PreparedStatement sstmt = conn.prepareStatement(subQuery);
					stmt.setInt(1, itemID);
					ResultSet srs = stmt.executeQuery();
					
					if(!srs.isBeforeFirst()) {
						msg.setSucceeded(false);
						msg.setMsg("Didn't find any Items for the given ItemInOrderID");
						return msg;
					}
					if(srs.next()) {
						String itemName = srs.getString("ItemName");
						float price = srs.getFloat("Price");
						Integer menuID = srs.getInt("MenuID");
						TypeOfProduct type = TypeOfProduct.getEnum(srs.getString("Type"));
						ProductSize size = ProductSize.getEnum(srs.getString("Size"));
						Doneness doneness = Doneness.getEnum(srs.getString("Doneness"));
						items item = new items(itemID, itemName,price,menuID,type,size,doneness,foodRequest);
						items.add(item);
					}
				}
				msg.setSucceeded(true);
				msg.setDataFromServer(items);
				msg.setMsg("ArrayList<items> generated successfully (watch out: the data is ItemID & Restrictions!");
			} catch (SQLException e) {
				e.printStackTrace();
				msg.setSucceeded(false);
				msg.setMsg("Database Error");
			}
			return msg;
		}

	/**
     * Retrieves a restaurant from the database using the given SupplierID.
     *
     * @param supplier The SupplierID of the restaurant to retrieve.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the restaurant data.
     */
	public static CommMessage getRestaurant(String supplier, CommMessage msg) {
		try {
			String query = "SELECT * FROM biteme.restaurants WHERE SupplierID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, supplier);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Restaurant not found for the given supplier");
				return msg;
			}

			// If restaurant exists, process the result
			if (rs.next()) {
				int restaurantId = rs.getInt("ID");
				String restaurantName = rs.getString("RestaurantName");
				String restaurantAddress = rs.getString("Address");
				RestaurantType restaurantType = RestaurantType.valueOf(rs.getString("FoodType"));
				String supplierId = rs.getString("SupplierID");
				BranchLocation branchLocation = BranchLocation.valueOf(rs.getString("BranchLocation"));
				int menuId = rs.getInt("MenuID");

				Restaurant restaurant = new Restaurant(restaurantId, restaurantName, restaurantAddress, restaurantType,
						supplierId, branchLocation);
				restaurant.setMenuId(menuId);

				msg.setSucceeded(true);
				msg.setDataFromServer(restaurant);
				msg.setMsg("Restaurant found successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}

		return msg;
	}

	/**
     * Retrieves the SupplierID of a restaurant from the database using the RestaurantID.
     *
     * @param restaurantID The ID of the restaurant to retrieve the SupplierID for.
     * @param msg          The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the SupplierID.
     */
	public static CommMessage getRestaurantSupplier(String restaurantID, CommMessage msg) {
		try {
			String query = "SELECT SupplierID FROM biteme.restaurants WHERE RestaurantID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantID);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Restaurant supplierId not found for the given RestaurantID");
				return msg;
			}

			// If restaurant exists, process the result
			if (rs.next()) {
				String supplierId = rs.getString("SupplierID");

				msg.setSucceeded(true);
				msg.setDataFromServer(supplierId);
				msg.setMsg("Restaurant supplierId found successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}

		return msg;
	}

	/**
     * Logs in a user by checking their credentials and updating their isLoggedIn status.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the user data if login is successful.
     */
	public static CommMessage Login(String username, String password, CommMessage msg) {
		try {
			String query = "SELECT * FROM biteme.users WHERE Username = ? AND Password = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("User name or password is not correct");
				return msg;
			}

			// If user exists, process the result
			if (rs.next()) {
				if (rs.getInt("isApproved") == 0) {
					msg.setSucceeded(false);
					msg.setMsg("User is not Approved");
//					return msg; 
				} else if (rs.getInt("isLoggedIn") == 1) {
					msg.setSucceeded(false);
					msg.setMsg("User is already logged in");
					return msg;
				} else {
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String id = rs.getString("ID");
					String email = rs.getString("Email");
					String phoneNumber = rs.getString("PhoneNumber");
					UserType role = UserType.valueOf(rs.getString("Role")); // changed from getEnum to valueOf
					BranchLocation branch = BranchLocation.getEnum(rs.getString("HomeBranch"));
					int isLoggedIn = rs.getInt("isLoggedIn");
					// int isApproved = rs.getInt("isApproved");
					User user = new User(username, password, firstName, lastName, email, phoneNumber, role, branch, id,
							1);
					// User user = new User(username, password, firstName, lastName, email,
					// phoneNumber, role, branch, id, isLoggedIn);

					// Update the database to set IsLoggedIn to 1
					String updateQuery = "UPDATE biteme.users SET isLoggedIn = 1 WHERE Username = ? AND Password = ?";
					PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
					updateStmt.setString(1, username);
					updateStmt.setString(2, password);
					updateStmt.executeUpdate(); 

					msg.setSucceeded(true);
					msg.setDataFromServer(user);
					msg.setMsg("User logged in successfully");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		}

		return msg;
	}

	/**
     * Retrieves all restaurants from a specific branch location.
     *
     * @param reqBranch The branch location to filter the restaurants.
     * @return An ArrayList of Restaurant objects belonging to the specified branch location.
     */
	public static ArrayList<Restaurant> getAllRestaurantsFrom(BranchLocation reqBranch) {
		// checked and working !
		ArrayList<Restaurant> rests = new ArrayList<>();
		ResultSet rs = null;
		String query = "SELECT * FROM restaurants WHERE BranchLocation = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, reqBranch.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				int restaurantId = rs.getInt("ID");
				String restaurantName = rs.getString("RestaurantName");
				String restaurantAddress = rs.getString("Address");
				RestaurantType restaurantType = RestaurantType.valueOf(rs.getString("FoodType"));
				String supplierId = rs.getString("SupplierID");
				BranchLocation branchLocation = BranchLocation.valueOf(rs.getString("BranchLocation"));
				int menuId = rs.getInt("MenuID");

				Restaurant restaurant = new Restaurant(restaurantId, restaurantName, restaurantAddress, restaurantType,
						supplierId, branchLocation);
				restaurant.setMenuId(menuId);

				rests.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return rests;
	}

	/**
     * Retrieves all distinct dates on which orders were received by customers.
     *
     * @param msg The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of distinct dates.
     */
	public static CommMessage GetAllOrdersDates(CommMessage msg) {

		try {
			String query = "SELECT DISTINCT RecievedByCustomerDate FROM Orders";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty (there is any completed orders i.e received by
			// the customer)
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("There is no Orders that completed (recieved by some customer) in the database.");
				return msg;
			}

			ArrayList<String> dates = new ArrayList<String>();

			while (rs.next()) {
				dates.add(rs.getString("RecievedByCustomerDate"));
			}

			msg.setSucceeded(true);
			msg.setMsg("Distinct order dates fetched successfully!");
			msg.setDataFromServer(dates);

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}

		return msg;
	}

	/**
	 * Retrieves all restaurants from a specific branch location.
	 *
	 * @param reqBranch The branch location to filter the restaurants.
	 * @param msg       The CommMessage object for sending the response.
	 * @return The updated CommMessage object containing the results of the query.
	 */
	public static CommMessage getAllRestaurantsFrom(BranchLocation reqBranch, CommMessage msg) {
		ArrayList<Restaurant> rests = new ArrayList<>();
		ResultSet rs = null;
		String query = "SELECT * FROM restaurants WHERE BranchLocation = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, reqBranch.toString());
			rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("No restaurants found for the specified branch");
				return msg;
			}

			// Process the result set
			while (rs.next()) {
				int restaurantId = rs.getInt("ID");
				String restaurantName = rs.getString("RestaurantName");
				String restaurantAddress = rs.getString("Address");
				RestaurantType restaurantType = RestaurantType.valueOf(rs.getString("FoodType"));
				String supplierId = rs.getString("SupplierID");
				BranchLocation branchLocation = BranchLocation.valueOf(rs.getString("BranchLocation"));
				int menuId = rs.getInt("MenuID");

				Restaurant restaurant = new Restaurant(restaurantId, restaurantName, restaurantAddress, restaurantType,
						supplierId, branchLocation);
				restaurant.setMenuId(menuId);

				rests.add(restaurant);
			}

			msg.setSucceeded(true);
			msg.setDataFromServer(rests);
			msg.setMsg("Restaurants retrieved successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database error");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return msg;
	}

	/**
     * Retrieves the number of orders and profit for each day for a specific restaurant in a given month and year.
     *
     * @param restaurantId The ID of the restaurant.
     * @param year         The year to filter the orders.
     * @param month        The month to filter the orders.
     * @return A HashMap where the key is the date (as a String) and the value is an OrderSummary object
     *         containing the total number of orders and the total profit for that day.
     */
    public static HashMap<String, OrderSummary> getOrdersAndProfitPerDay(int restaurantId, int year, int month) {
        //checked and working !
    	HashMap<String, OrderSummary> ordersProfitPerDay = new HashMap<>();
        ResultSet rs = null;
        String query = "SELECT DATE(RecievedByCustomerDate) AS OrderDate, COUNT(*) AS OrderCount, SUM(TotalPrice) AS TotalProfit " +
                       "FROM orders " +
                       "WHERE RestaurantID = ? " +
                       "AND YEAR(RecievedByCustomerDate) = ? " +
                       "AND MONTH(RecievedByCustomerDate) = ? " +
                       "GROUP BY OrderDate";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, restaurantId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String orderDate = rs.getString("OrderDate");
                int orderCount = rs.getInt("OrderCount");
                double totalProfit = rs.getDouble("TotalProfit");
                
                OrderSummary summary = new OrderSummary(orderCount, totalProfit);
                ordersProfitPerDay.put(orderDate, summary);
            }

            // If no results were found for this restaurant/month/year, add entries with 0 values
            if (ordersProfitPerDay.isEmpty()) {
                for (int day = 1; day <= 31; day++) {
                    String orderDate = String.format("%d-%02d-%02d", year, month, day);
                    OrderSummary summary = new OrderSummary(0, 0.0);
                    ordersProfitPerDay.put(orderDate, summary);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ordersProfitPerDay;
    }

    /**
     * Retrieves the total number of orders and the total profit for each day within a specified date range
     * for all restaurants in a specific branch.
     *
     * @param reqBranch The branch location to filter the restaurants.
     * @param fromMonth The starting month of the date range (inclusive).
     * @param toMonth   The ending month of the date range (inclusive).
     * @param year      The year to filter the orders.
     * @param msg       The CommMessage object for sending the response.
     * @return A TreeMap where the key is the date (as a String) and the value is an OrderSummary object
     *         containing the total number of orders and the total profit for that day.
     */
    public static TreeMap<String, OrderSummary> getTotalOrdersAndProfitPerDayForBranch(BranchLocation reqBranch, int fromMonth, int toMonth, int year, CommMessage msg) {
        TreeMap<String, OrderSummary> totalOrdersProfitPerDay = new TreeMap<>();

        try {
            // Get all restaurants from the specified branch
            msg = getAllRestaurantsFrom(reqBranch, msg);
            if (!msg.isSucceeded()) {
                return totalOrdersProfitPerDay; // Return empty map if error occurred while retrieving restaurants
            }

            ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) msg.getDataFromServer();
            if (restaurants.isEmpty()) {
                msg.setSucceeded(false);
                msg.setMsg("No restaurants found for the specified branch.");
                return totalOrdersProfitPerDay;
            }

            // Loop through each restaurant and get the orders and profit per day
            for (Restaurant restaurant : restaurants) {
                for (int month = fromMonth; month <= toMonth; month++) {
                    try {
                        HashMap<String, OrderSummary> ordersProfitPerDay = getOrdersAndProfitPerDay(restaurant.getRestaurantId(), year, month);

                        // Aggregate the results
                        for (Map.Entry<String, OrderSummary> entry : ordersProfitPerDay.entrySet()) {
                            String orderDate = entry.getKey();
                            OrderSummary summary = entry.getValue();

                            if (totalOrdersProfitPerDay.containsKey(orderDate)) {
                                OrderSummary existingSummary = totalOrdersProfitPerDay.get(orderDate);
                                existingSummary = new OrderSummary(
                                        existingSummary.getOrderCount() + summary.getOrderCount(),
                                        existingSummary.getTotalProfit() + summary.getTotalProfit()
                                );
                                totalOrdersProfitPerDay.put(orderDate, existingSummary);
                            } else {
                                totalOrdersProfitPerDay.put(orderDate, summary);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.setSucceeded(false);
                        msg.setMsg("An error occurred while processing restaurant ID: " + restaurant.getRestaurantId());
                        return totalOrdersProfitPerDay;
                    }
                }
            }

            // Add entries for all days in the range even if there are no orders
            for (int month = fromMonth; month <= toMonth; month++) {
                int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
                for (int day = 1; day <= daysInMonth; day++) {
                    String date = String.format("%d-%02d-%02d", year, month, day);
                    totalOrdersProfitPerDay.putIfAbsent(date, new OrderSummary(0, 0.0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("An error occurred while retrieving total orders and profit per day.");
        }

        return totalOrdersProfitPerDay;
    }

    /**
     * Retrieves order statistics, including the total number of orders and the number of late orders, for a specific branch and month.
     *
     * @param reqBranch The branch location to filter the orders.
     * @param month     The month to filter the orders.
     * @param year      The year to filter the orders.
     * @param msg       The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the order statistics.
     */
    public static CommMessage getOrderStatsForBranch(BranchLocation reqBranch, int month, int year, CommMessage msg) {
        int totalOrders = 0;
        int lateOrders = 0;

        // First, get all the restaurants from the specified branch
        msg = getAllRestaurantsFrom(reqBranch, msg);
        if (!msg.isSucceeded()) {
            return msg; // Return if there was an error in retrieving restaurants
        }

        @SuppressWarnings("unchecked")
        ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) msg.getDataFromServer();
        if (restaurants.isEmpty()) {
            msg.setSucceeded(false);
            msg.setMsg("No restaurants found for the specified branch.");
            return msg;
        }

        // Prepare the query to get order statistics
        String query = "SELECT COUNT(*) AS TotalOrders, SUM(CASE WHEN IsLate = 1 THEN 1 ELSE 0 END) AS LateOrders " +
                       "FROM Orders " +
                       "WHERE RestaurantID IN (";

        // Dynamically append the restaurant IDs to the query
        for (int i = 0; i < restaurants.size(); i++) {
            query += restaurants.get(i).getRestaurantId();
            if (i < restaurants.size() - 1) {
                query += ", ";
            }
        }
        query += ") AND MONTH(RecievedByCustomerDate) = ? AND YEAR(RecievedByCustomerDate) = ?";

        // Execute the query to get order statistics
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalOrders = rs.getInt("TotalOrders");
                lateOrders = rs.getInt("LateOrders");
            }

            // Create a result object to store the statistics
            HashMap<String, Integer> result = new HashMap<>();
            result.put("TotalOrders", totalOrders);
            result.put("LateOrders", lateOrders);

            msg.setSucceeded(true);
            msg.setDataFromServer(result);
            msg.setMsg("Order statistics retrieved successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("Database error");
        }

        return msg;
    }
    
    
    /**
     * Generates a monthly performance report for the specified branch.
     *
     * @param reqBranch The branch location for which the report is generated.
     * @param month     The month for the report.
     * @param year      The year for the report.
     * @param msg       The CommMessage object for sending the response.
     * @return The updated CommMessage object containing the monthly performance report.
     */
    public static CommMessage getMonthlyPerformanceReport(BranchLocation reqBranch, int month, int year, CommMessage msg) {
        try {
            // Retrieve order statistics for the specified branch
            msg = getOrderStatsForBranch(reqBranch,month,year, msg);

            // Check if an error occurred during data retrieval
            if (!msg.isSucceeded()) {
                return msg;
            }

            // Retrieve the order statistics from the CommMessage object
            @SuppressWarnings("unchecked")
            HashMap<String, Integer> orderStats = (HashMap<String, Integer>) msg.getDataFromServer();
            int totalOrders = orderStats.get("TotalOrders");
            int lateOrders = orderStats.get("LateOrders");

            // Create MonthlyReport object
            String date = String.format("%d-%02d", year, month);
            MonthlyReport monthlyReport = new MonthlyReport(date, reqBranch, "Performance");
            monthlyReport.setTotalOrders(totalOrders);
            monthlyReport.setLateOrders(lateOrders);

            // Update CommMessage object
            msg.setSucceeded(true);
            msg.setDataFromServer(monthlyReport);
            msg.setMsg("Monthly performance report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("An error occurred while generating the monthly performance report.");
        }

        return msg;
    }

    /**
     * Generates a monthly revenue report for each restaurant in the specified branch.
     *
     * @param reqBranch The branch location for which the report is generated.
     * @param month     The month for the report.
     * @param year      The year for the report.
     * @param msg       The CommMessage object for sending the response.
     * @return The updated CommMessage object containing the monthly revenue report.
     */
    public static CommMessage getMonthlyRevenueReport(BranchLocation reqBranch, int month, int year, CommMessage msg) {
        try {
            // Fetch all restaurants from the specified branch
            msg = getAllRestaurantsFrom(reqBranch, msg);
            if (!msg.isSucceeded()) {
                return msg;
            }

            ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) msg.getDataFromServer();
            HashMap<String, Double> revenuePerRestaurant = new HashMap<>();

            // Calculate revenue for each restaurant
            for (Restaurant restaurant : restaurants) {
                double totalRevenue = 0.0;
                String query = "SELECT SUM(TotalPrice) AS Revenue FROM orders WHERE RestaurantID = ? AND YEAR(RecievedByCustomerDate) = ? AND MONTH(RecievedByCustomerDate) = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, restaurant.getRestaurantId());
                    stmt.setInt(2, year);
                    stmt.setInt(3, month);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        totalRevenue = rs.getDouble("Revenue");
                    }
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.setSucceeded(false);
                    msg.setMsg("Database error while calculating revenue.");
                    return msg;
                }
                revenuePerRestaurant.put(restaurant.getRestaurantName(), totalRevenue);
            }

            // Create and return the report
            String date = String.format("%d-%02d", year, month);
            MonthlyReport revenueReport = new MonthlyReport(date, reqBranch, "Revenue");
            revenueReport.setData(revenuePerRestaurant);

            msg.setSucceeded(true);
            msg.setDataFromServer(revenueReport);
            msg.setMsg("Monthly revenue report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("An error occurred while generating the monthly revenue report.");
        }
        return msg;
    }

    /**
     * Generates a monthly orders report for each restaurant in the specified branch, filtered by individual items.
     *
     * @param reqBranch The branch location for which the report is generated.
     * @param month     The month for the report.
     * @param year      The year for the report.
     * @param msg       The CommMessage object for sending the response.
     * @return The updated CommMessage object containing the monthly orders report.
     */
    public static CommMessage getMonthlyOrdersReport(BranchLocation reqBranch, int month, int year, CommMessage msg) {
        try {
            // Fetch all restaurants from the specified branch
            msg = getAllRestaurantsFrom(reqBranch, msg);
            if (!msg.isSucceeded()) {
                return msg;
            }

            ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) msg.getDataFromServer();
            HashMap<String, HashMap<String, Integer>> ordersPerRestaurant = new HashMap<>();

            // Calculate orders for each restaurant, filtered by individual items
            for (Restaurant restaurant : restaurants) {
                HashMap<String, Integer> ordersByItem = new HashMap<>();
                String query = "SELECT i.ItemName, COUNT(*) AS OrderCount " +
                               "FROM orders o " +
                               "JOIN itemsinorder iio ON o.OrderID = iio.OrderID " +
                               "JOIN items i ON iio.ItemID = i.ItemID " +
                               "WHERE o.RestaurantID = ? AND YEAR(o.RecievedByCustomerDate) = ? AND MONTH(o.RecievedByCustomerDate) = ? " +
                               "GROUP BY i.ItemName";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, restaurant.getRestaurantId());
                    stmt.setInt(2, year);
                    stmt.setInt(3, month);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String itemName = rs.getString("ItemName");
                        int orderCount = rs.getInt("OrderCount");
                        ordersByItem.put(itemName, orderCount);
                    }
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.setSucceeded(false);
                    msg.setMsg("Database error while calculating orders by item.");
                    return msg;
                }
                ordersPerRestaurant.put(restaurant.getRestaurantName(), ordersByItem);
            }

            // Create and return the report
            String date = String.format("%d-%02d", year, month);
            MonthlyReport ordersReport = new MonthlyReport(date, reqBranch, "Orders");
            ordersReport.setData(ordersPerRestaurant);

            msg.setSucceeded(true);
            msg.setDataFromServer(ordersReport);
            msg.setMsg("Monthly orders report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("An error occurred while generating the monthly orders report.");
        }
        return msg;
    }

    /**
     * Generates a quarter report for the specified branch and date range.
     *
     * @param reqBranch The branch location for which the report is generated.
     * @param fromMonth The starting month of the quarter.
     * @param toMonth   The ending month of the quarter.
     * @param year      The year for the report.
     * @param msg       The CommMessage object for sending the response.
     * @return The updated CommMessage object containing the quarter report.
     */
    public static CommMessage getQuarterReport(BranchLocation reqBranch, int fromMonth, int toMonth, int year, CommMessage msg) {
        try {
            // Retrieve total orders and profit per day for the specified branch and date range
            TreeMap<String, OrderSummary> totalOrdersProfitPerDay = getTotalOrdersAndProfitPerDayForBranch(reqBranch, fromMonth, toMonth, year, msg);

            // Check if an error occurred during data retrieval
            if (!msg.isSucceeded()) {
                return msg;
            }

            // Create QuarterReport object with a relevant date string, such as "Q1 2024"
            String date = String.format("Q%d %d", (fromMonth + 2) / 3, year);
            QuarterReport quarterReport = new QuarterReport("Quarterly Report", date, reqBranch, fromMonth, toMonth, year);
            quarterReport.setOrdersAndProfitPerDay(totalOrdersProfitPerDay);

            // Update CommMessage object
            msg.setSucceeded(true);
            msg.setDataFromServer(quarterReport);
            msg.setMsg("Quarter report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setSucceeded(false);
            msg.setMsg("An error occurred while generating the quarter report.");
        }

        return msg;
    }

    /**
     * Retrieves order statistics, including the total number of orders and the number of late orders, for a specific branch and month.
     *
     * @param reqBranch The branch location to filter the orders.
     * @param month     The month to filter the orders.
     * @param year      The year to filter the orders.
     * @param msg       The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the order statistics.
     */
	public static CommMessage AllUsersNotLoggedIn(CommMessage msg) {
		try {
			// preparing for the query execution
			String query = "SELECT * FROM Users";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty (order not found by the given ID)
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("There is no Users in the database.");
				return msg;
			}

			query = "UPDATE Users SET isLoggedIn = ?";
			stmt = conn.prepareStatement(query);
			stmt.setBoolean(1, false);

			int rowsAffected = stmt.executeUpdate();
			// Message the number of Users that updated (usually all the rows in the
			// database).
			msg.setMsg("The number of users that is updated: " + rowsAffected);
			msg.setSucceeded(true);

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Retrieves the refund balance for a specific user from the database.
     *
     * @param username The username of the user.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the refund balance.
     */
	public static CommMessage GetRefund(String username, CommMessage msg) {
		try {
			String query = "SELECT * FROM Refunds WHERE Username = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("No refunds for this user");
				return msg;
			}

			if (rs.next()) {
				// Extracting data from ResultSet
				Double Balance = rs.getDouble("Balance");

				msg.setSucceeded(true);
				msg.setDataFromServer(Balance);
				msg.setMsg("Refund fetched successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Updates the refund balance for a specific user in the database.
     *
     * @param username The username of the user.
     * @param Balance  The new balance to update.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object.
     */
	public static CommMessage UpdateRefund(String username, Double Balance, CommMessage msg) {
		try {
			// preparing for the query execution
			String query = "SELECT * FROM Refunds WHERE Username = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty (order not found by the given ID)
			// So creates a new Refund
			if (!rs.isBeforeFirst()) {
				query = "INSERT INTO Refunds (Username, Balance) VALUES (?, ?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, username);
				stmt.setDouble(2, Balance);
				stmt.executeUpdate();
				msg.setSucceeded(true);
				msg.setMsg("The Refund of the User" + " " + username + " created successfully");
				return msg;
			}

			// Refund is found -> update the Refund by the given username + Balance
			if (rs.next()) {
				query = "UPDATE Refunds SET Balance = ? WHERE Username = ?";
				stmt = conn.prepareStatement(query);
				// Set parameters according to the schema
				stmt.setDouble(1, Balance);
				stmt.setString(2, username);
				stmt.executeUpdate();

				msg.setSucceeded(true); // Update succeeded therefore we update the success message
				msg.setMsg("The Refund of the User" + " " + username + " updated successfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}

		return msg;
	}

	/**
     * Updates an order in the database using the provided OrderID and CommMessage object.
     *
     * @param orderID The ID of the order to update.
     * @param msg     The CommMessage object containing the order data to update.
     * @return The updated CommMessage object with the result of the update operation.
     */
	public static CommMessage UpdateOrder(int orderID, CommMessage msg) {
		try {
			// preparing for the query execution
			String strOrderID = ((Integer) orderID).toString();
			String query = "SELECT * FROM biteme.Orders WHERE OrderID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, strOrderID);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty (order not found by the given ID)
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Order not found");
				return msg;
			}

			// Order is found -> update the Order by the given fields of the Order object
			if (rs.next()) {
				Order orderSent = (Order) (msg.dataFromServer);
				query = "UPDATE biteme.Orders SET NumOfItems = ?, Status = ?, TotalPrice = ?, IsLate = ?, "
						+ "ApprovedByResDate = ?, ApprovedByResTime = ?, RecievedByCustomerDate = ?, "
						+ "RecievedByCustomerTime = ?, AskedByCustomerDate = ?, AskedByCustomerTime = ? WHERE OrderID = ?";
				stmt = conn.prepareStatement(query);

				// Set parameters according to the schema
				stmt.setInt(1, orderSent.getNumOfItems());
				stmt.setString(2, orderSent.getStatus().toString());
				stmt.setDouble(3, orderSent.getTotal_price());
				stmt.setBoolean(4, orderSent.isIsLate());

				// Convert LocalDate to java.sql.Date
				stmt.setDate(5,orderSent.getApprovedByResDate());

				// Convert LocalTime to java.sql.Time
				stmt.setTime(6,orderSent.getApprovedByResTime());

				// Convert LocalDate to java.sql.Date
				if (orderSent.getRecievedByCustomerDate() == null) {
					stmt.setDate(7, null);
				} else {
					stmt.setDate(7,orderSent.getRecievedByCustomerDate());
				}
				// Convert LocalTime to java.sql.Time
				if(orderSent.getRecievedByCustomerTime() == null) {
					stmt.setDate(8, null);
				} else {
					stmt.setTime(8, java.sql.Time.valueOf(orderSent.getRecievedByCustomerTime().toString()));
				}
				// Convert LocalDate to java.sql.Date for AskedByCustomerDate
				stmt.setDate(9,orderSent.getAskedByCustomerDate());

				// Convert LocalTime to java.sql.Time for AskedByCustomerTime
				stmt.setTime(10,orderSent.getAskedByCustomerTime());

				stmt.setInt(11, Integer.parseInt(strOrderID)); // Assuming OrderID is stored as an integer

				stmt.executeUpdate();

				msg.setSucceeded(true); // Update succeeded therefore we update the success message
				msg.setDataFromServer(orderSent); // its the same Order object as sent by the client
				msg.setMsg("The Order updated successfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}

		return msg;
	}

	/**
     * Retrieves all orders for a specific user from the database using their username.
     *
     * @param username The username of the user.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of orders.
     */
	public static CommMessage GetOrders(String username, CommMessage msg) {
		try {
			String query = "SELECT * FROM Orders WHERE Username = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Didn't find any Orders for the given username");
				return msg;
			}

			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				// Extracting data from ResultSet
				int orderId = rs.getInt("OrderID");
				int numOfItems = rs.getInt("NumOfItems");
				OrderStatus status = OrderStatus.valueOf(rs.getString("Status"));
				double total_price = rs.getDouble("TotalPrice");
				String restaurantId = (rs.getInt("RestaurantID") + "");
				boolean isLate = rs.getBoolean("IsLate");
				int deliveryID = rs.getInt("DeliveryID");
				Date askedByCustomerDate = rs.getDate("AskedByCustomerDate");
				Time askedByCustomerTime = rs.getTime("AskedByCustomerTime");
				Date approvedByResDate = rs.getDate("ApprovedByResDate");
				Time approvedByResTime = rs.getTime("ApprovedByResTime");
				Date recievedByCustomerDate = rs.getDate("RecievedByCustomerDate");
				Time recievedByCustomerTime = rs.getTime("RecievedByCustomerTime");
				TypeOfOrder type = TypeOfOrder.valueOf(rs.getString("Type"));

				Order order = new Order(orderId, numOfItems, status, total_price, restaurantId, isLate, username,
						deliveryID, askedByCustomerDate, askedByCustomerTime, approvedByResDate, approvedByResTime,
						recievedByCustomerDate, recievedByCustomerTime, type, new ArrayList<items>());
				orders.add(order);
			}
			msg.setSucceeded(true);
			msg.setDataFromServer(orders);
			msg.setMsg("Orders ArrayList<Order> fetched successfully!");

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Retrieves all orders for a specific restaurant from the database using the RestaurantID.
     *
     * @param restaurantID The ID of the restaurant.
     * @param msg          The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of orders.
     */
	public static CommMessage GetOrdersForRestaurant(String restaurantID, CommMessage msg) {
		try {
			String query = "SELECT * FROM Orders WHERE RestaurantID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantID);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Didn't find any Orders for the given username");
				return msg;
			}

			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				// Extracting data from ResultSet
				int orderId = rs.getInt("OrderID");
				int numOfItems = rs.getInt("NumOfItems");
				OrderStatus status = OrderStatus.valueOf(rs.getString("Status"));
				double total_price = rs.getDouble("TotalPrice");
				String username = (rs.getString("Username"));
				boolean isLate = rs.getBoolean("IsLate");
				int deliveryID = rs.getInt("DeliveryID");
				Date askedByCustomerDate = rs.getDate("AskedByCustomerDate");
				Time askedByCustomerTime = rs.getTime("AskedByCustomerTime");
				Date approvedByResDate = rs.getDate("ApprovedByResDate");
				Time approvedByResTime = rs.getTime("ApprovedByResTime");
				Date recievedByCustomerDate = rs.getDate("RecievedByCustomerDate");
				Time recievedByCustomerTime = rs.getTime("RecievedByCustomerTime");
				TypeOfOrder type = TypeOfOrder.valueOf(rs.getString("Type"));

				Order order = new Order(orderId, numOfItems, status, total_price, restaurantID, isLate, username,
						deliveryID, askedByCustomerDate, askedByCustomerTime, approvedByResDate, approvedByResTime,
						recievedByCustomerDate, recievedByCustomerTime, type, new ArrayList<items>());
				orders.add(order);
			}
			msg.setSucceeded(true);
			msg.setDataFromServer(orders);
			msg.setMsg("Orders ArrayList<Order> fetched successfully!");

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Retrieves personal data for a specific user from the database using their username.
     *
     * @param username The username of the user.
     * @param msg      The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the user's personal data.
     */
	public static CommMessage GetPersonalData(String username, CommMessage msg) {
		try {
			String query = "SELECT * FROM Users WHERE Username = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("User not found");
				return msg;
			}

			if (rs.next()) {
				// Extracting data from ResultSet
				String password = rs.getString("Password");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				String email = rs.getString("Email");
				String id = rs.getString("ID");
				String phoneNumber = rs.getString("PhoneNumber");
				UserType role = UserType.valueOf(rs.getString("Role"));
				BranchLocation branch = BranchLocation.valueOf(rs.getString("HomeBranch"));
				int isLoggedIn = rs.getInt("IsLoggedIn");
				int isApproved = rs.getInt("isApproved");
				User user = new User(username, password, firstName, lastName, email, phoneNumber, role, branch, id,
						isLoggedIn, isApproved);

				msg.setSucceeded(true);
				msg.setDataFromServer(user);
				msg.setMsg("User fetched successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Creates a new order in the database and its corresponding delivery details.
     *
     * @param msg The CommMessage object containing the order and delivery data to insert.
     * @return The updated CommMessage object with the created order and delivery data.
     */
	public static CommMessage CreateOrder(CommMessage msg) {
		try {

			Order orderSent = (Order) (msg.dataFromServer);
			String query = "INSERT INTO Orders (NumOfItems, Status, TotalPrice, RestaurantID, IsLate, Username, "
					+ "AskedByCustomerDate, AskedByCustomerTime, Type)"
					+ " VALUES (?, 'Pending', ?, ?, FALSE, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			// Set parameters according to the schema
			stmt.setInt(1, orderSent.getNumOfItems());
			stmt.setDouble(2, orderSent.getTotal_price());
			stmt.setString(3, orderSent.getRestaurantId());
			stmt.setString(4, orderSent.getUsername());
			// Convert LocalDate to java.sql.Date
			stmt.setDate(5, java.sql.Date.valueOf(orderSent.getAskedByCustomerDate().toString()));
			// Convert LocalTime to java.sql.Time
			stmt.setTime(6, java.sql.Time.valueOf(orderSent.getAskedByCustomerTime().toString()));
			stmt.setString(7, orderSent.getType().toString());
			stmt.executeUpdate();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			generatedKeys.next();
			int orderID = generatedKeys.getInt(1); // Retrieves the first column (i.e the orderID)

			// Now we create Delivery with that orderID
			Delivery delSent = (Delivery) (msg.objectForServer);
			query = "INSERT INTO Delivery (Address,Name,PhoneNumber,Type,DeliveryFee,OrderID,UsernamesOfParticipants,NumOfParticipants)"
					+ " VALUES(?,?,?,?,?,?,?,?)";
			stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, delSent.getAddress());
			stmt.setString(2, delSent.getName());
			stmt.setString(3, delSent.getPhoneNumber());
			stmt.setString(4, delSent.getType().toString());
			stmt.setDouble(5, (Double) delSent.getDeliveryFee());
			stmt.setInt(6, orderID);
			if (delSent.getNumOfParticipants() > 1)
				stmt.setString(7, delSent.getUsernamesOfParticipants().toString());
			else
				stmt.setString(7, orderSent.getUsername());
			stmt.setInt(8, delSent.getNumOfParticipants());
			stmt.executeUpdate();
			generatedKeys = stmt.getGeneratedKeys();
			generatedKeys.next();
			int delID = generatedKeys.getInt(1);

			// Now we can update the Order with the field of DeliveryID
			query = "UPDATE Orders SET DeliveryID = ? WHERE OrderID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, delID);
			stmt.setInt(2, orderID);
			stmt.executeUpdate();

			// Fetch ItemsInOrder
			ArrayList<items> items = orderSent.getItemsInOrder();
			query = "INSERT INTO ItemsInOrder (OrderID, ItemID, foodRequests) VALUES (?,?,?)";
			for (items i : items) {
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, orderID);
				stmt.setInt(2, i.getItemID());
				stmt.setString(3, i.getRestrictions());
				stmt.executeUpdate();
			}

			// Update Delivery & Order objects!
			orderSent.setDeliveryID(delID);
			orderSent.setOrderId(orderID);

			delSent.setDeliveryId(delID);
			delSent.setOrderId(orderID);

			msg.setSucceeded(true); // Update succeeded therefore we update the success message
			msg.setDataFromServer(orderSent); // Updated Order object to client
			msg.objectForServer = delSent; // Updated Delivery object to client
			msg.setMsg("The Order Created successfully OrderID:" + orderID + " DeliveryID: " + delID);

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}

	/**
     * Retrieves all full orders for a specific restaurant from the database using the RestaurantID.
     *
     * @param restaurantID The ID of the restaurant.
     * @param msg          The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of full orders.
     */
	public static CommMessage GetFullOrdersForRestaurant(String restaurantID, CommMessage msg) {
		try {
			String query = "SELECT * FROM orders WHERE RestaurantID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantID);
			ResultSet rs = stmt.executeQuery();

			// Check if result set is empty
			if (!rs.isBeforeFirst()) {
				msg.setSucceeded(false);
				msg.setMsg("Didn't find any Orders for the given restaurant ID");
				return msg;
			}

			List<Order> orders = new ArrayList<>();
			while (rs.next()) {
				// Extracting data from ResultSet
				int orderId = rs.getInt("OrderID");
				int numOfItems = rs.getInt("NumOfItems");

				OrderStatus status = null;
				String statusStr = rs.getString("Status");
				if (statusStr != null) {
					status = OrderStatus.getEnum(statusStr);
				}

				double total_price = rs.getDouble("TotalPrice");
				String username = rs.getString("Username");
				boolean isLate = rs.getBoolean("IsLate");
				int deliveryID = rs.getInt("DeliveryID");
				Date askedByCustomerDate = rs.getDate("AskedByCustomerDate");
				Time askedByCustomerTime = rs.getTime("AskedByCustomerTime");
				Date approvedByResDate = rs.getDate("ApprovedByResDate");
				Time approvedByResTime = rs.getTime("ApprovedByResTime");
				Date recievedByCustomerDate = rs.getDate("RecievedByCustomerDate");
				Time recievedByCustomerTime = rs.getTime("RecievedByCustomerTime");

				TypeOfOrder type = null;
				String typeStr = rs.getString("Type");
				if (typeStr != null) {
					type = TypeOfOrder.getEnum(typeStr);
				}

				Order order = new Order(orderId, numOfItems, status, total_price, restaurantID, isLate, username,
						deliveryID, askedByCustomerDate, askedByCustomerTime, approvedByResDate, approvedByResTime,
						recievedByCustomerDate, recievedByCustomerTime, type, new ArrayList<items>());

				// Fetching items in order
				String itemQuery = "SELECT iio.*, i.ItemName, i.Price, i.MenuID, i.Type, i.Size, i.Doneness FROM itemsinorder iio JOIN items i ON iio.ItemID = i.ItemID WHERE iio.OrderID = ?";
				PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
				itemStmt.setInt(1, orderId);
				ResultSet itemRs = itemStmt.executeQuery();
				while (itemRs.next()) {
					int itemID = itemRs.getInt("ItemID");
					String itemName = itemRs.getString("ItemName");
					float price = itemRs.getFloat("Price");
					int menuID = itemRs.getInt("MenuID");

					TypeOfProduct typeOfProduct = null;
					String typeOfProductStr = itemRs.getString("Type");
					if (typeOfProductStr != null) {
						typeOfProduct = TypeOfProduct.getEnum(typeOfProductStr);
					}

					ProductSize size = null;
					String sizeStr = itemRs.getString("Size");
					if (sizeStr != null) {
						size = ProductSize.getEnum(sizeStr);
					}

					Doneness doneness = null;
					String donenessStr = itemRs.getString("Doneness");
					if (donenessStr != null) {
						doneness = Doneness.getEnum(donenessStr);
					}

					String foodRequests = itemRs.getString("foodRequests");

					items item = new items(itemID, itemName, price, menuID, typeOfProduct, size, doneness,
							foodRequests);
					order.getItemsInOrder().add(item);
				}

				orders.add(order);
			}
			msg.setSucceeded(true);
			msg.setDataFromServer(orders);
			msg.setMsg("Orders ArrayList<Order> fetched successfully!");

		} catch (SQLException e) {
			e.printStackTrace();
			msg.setSucceeded(false);
			msg.setMsg("Database Error");
		}
		return msg;
	}
	
	/**
     * Retrieves all unapproved users from a specific branch.
     *
     * @param fromBranch The branch location to filter the users.
     * @param msg        The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the list of unapproved users.
     */ 
		public static CommMessage GetUnApprovedUsers(String fromBranch ,CommMessage msg) {
			try {
				String query = "SELECT * FROM biteme.users WHERE isApproved = 0 AND HomeBranch = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, fromBranch);
				ResultSet rs = stmt.executeQuery();
				// Check if result set is empty
				if (!rs.isBeforeFirst()) {
					msg.setSucceeded(false);
					msg.setMsg("Didn't find any UnApproved users");
					return msg;
				}
				ArrayList<User> users = new ArrayList<User>();
				while (rs.next()) {
					String userName = rs.getString("Username");
					String password = rs.getString("Password");
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					String email = rs.getString("Email");
					String phoneNumber = rs.getString("PhoneNumber");
					UserType type = UserType.getEnum(rs.getString("Role"));
					BranchLocation mainBranch = BranchLocation.getEnum(rs.getString("HomeBranch"));
					Integer id = rs.getInt("ID");
					int isLoggedIn = rs.getInt("isLoggedIn");
					User user = new User(userName, password, firstName, lastName, email, phoneNumber, type, mainBranch, id.toString(), isLoggedIn);

					users.add(user);
				}
				msg.setSucceeded(true);
				msg.setDataFromServer(users);
				msg.setMsg("ArrayList<user> generated successfully");
			} catch (SQLException e) {
				e.printStackTrace();
				msg.setSucceeded(false);
				msg.setMsg("Database Error");
			}
			return msg;
		}

		/**
	     * Removes an item from the menu by setting its MenuID to a non-existing value.
	     *
	     * @param itemID The ID of the item to remove.
	     * @param msg    The CommMessage object to store the result and any messages.
	     * @return The updated CommMessage object with the result of the removal operation.
	     */
	public static CommMessage RemoveItemFromMenu(int itemID, CommMessage msg) {
	        try {
	            // Step 1: Check if the item exists
	            String query = "SELECT * FROM items WHERE ItemID = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setInt(1, itemID);
	            ResultSet rs = stmt.executeQuery();

	            if (!rs.isBeforeFirst()) { // Check if result set is empty
	                msg.setSucceeded(false);
	                msg.setMsg("Failed to find item with ID: " + itemID + ". Removal from menu aborted.");
	                return msg;
	            }

	            // Step 2: Update the MenuID to 999 (non-existing menu)
	            query = "UPDATE items SET MenuID = ? WHERE ItemID = ?";
	            stmt = conn.prepareStatement(query);
	            stmt.setInt(1, 999); // Non-existing menu ID
	            stmt.setInt(2, itemID);
	            int affectedRows = stmt.executeUpdate();

	            if (affectedRows > 0) {
	                msg.setSucceeded(true);
	                msg.setMsg("Item with ID: " + itemID + " removed from menu successfully.");
	            } else {
	                msg.setSucceeded(false);
	                msg.setMsg("Failed to remove item with ID: " + itemID + " from menu.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            msg.setSucceeded(false);
	            msg.setMsg("Database Error: " + e.getMessage());
	        }
	        return msg;
	    }
	    
	/**
     * Updates the specifications of an item in the database.
     *
     * @param updatedItem The item object containing the updated specifications.
     * @param msg         The CommMessage object to store the result and any messages.
     * @return The updated CommMessage object with the result of the update operation.
     */
	    public static CommMessage updateItemSpecifications(items updatedItem, CommMessage msg) {
	        try {
	            // Step 1: Verify the item exists and retrieve the current record
	            String query = "SELECT * FROM items WHERE ItemID = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setInt(1, updatedItem.getItemID());
	            ResultSet rs = stmt.executeQuery();

	            if (!rs.isBeforeFirst()) { // Check if the result set is empty
	                msg.setSucceeded(false);
	                msg.setMsg("Failed to find item with ID: " + updatedItem.getItemID() + ". Update aborted.");
	                return msg;
	            }

	            // Step 2: Update the item with the provided specifications
	            query = "UPDATE items SET Price = ?, Type = ?, Size = ?, Doneness = ? WHERE ItemID = ?";
	            stmt = conn.prepareStatement(query);

	            stmt.setFloat(1, updatedItem.getPrice());

	            // Handle null values for enums
	            if (updatedItem.getType() != null) {
	                stmt.setString(2, updatedItem.getType().toString());
	            } else {
	                stmt.setNull(2, java.sql.Types.VARCHAR);
	            }

	            if (updatedItem.getSize() != null) {
	                stmt.setString(3, updatedItem.getSize().toString());
	            } else {
	                stmt.setNull(3, java.sql.Types.VARCHAR);
	            }

	            if (updatedItem.getDoneness() != null) {
	                stmt.setString(4, updatedItem.getDoneness().toString());
	            } else {
	                stmt.setNull(4, java.sql.Types.VARCHAR);
	            }

	            stmt.setInt(5, updatedItem.getItemID());

	            int affectedRows = stmt.executeUpdate();

	            if (affectedRows > 0) {
	                msg.setSucceeded(true);
	                msg.setMsg("Item with ID: " + updatedItem.getItemID() + " updated successfully.");
	            } else {
	                msg.setSucceeded(false);
	                msg.setMsg("Failed to update item with ID: " + updatedItem.getItemID() + ".");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            msg.setSucceeded(false);
	            msg.setMsg("Database Error: " + e.getMessage());
	        }
	        return msg;
	    }
	    
	    /**
	     * Adds a new item to the menu in the database.
	     *
	     * @param newItem The item object containing the data to insert.
	     * @param msg     The CommMessage object to store the result and any messages.
	     * @return The updated CommMessage object with the result of the insertion operation.
	     */
	    public static CommMessage addItemToMenu(items newItem, CommMessage msg) {
	        try {
	            String query = "INSERT INTO items (ItemName, Price, MenuID, Type, Size, Doneness) VALUES (?, ?, ?, ?, ?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            
	            stmt.setString(1, newItem.getItemName());
	            stmt.setFloat(2, newItem.getPrice());
	            stmt.setInt(3, newItem.getMenuID());
	            
	            // Handle null values for enums
	            if (newItem.getType() != null) {
	                stmt.setString(4, newItem.getType().toString());
	            } else {
	                stmt.setNull(4, java.sql.Types.VARCHAR);
	            }
	            
	            if (newItem.getSize() != null) {
	                stmt.setString(5, newItem.getSize().toString());
	            } else {
	                stmt.setNull(5, java.sql.Types.VARCHAR);
	            }
	            
	            if (newItem.getDoneness() != null) {
	                stmt.setString(6, newItem.getDoneness().toString());
	            } else {
	                stmt.setNull(6, java.sql.Types.VARCHAR);
	            }

	            // Execute the insertion
	            int affectedRows = stmt.executeUpdate();
	            
	            if (affectedRows > 0) {
	                msg.setSucceeded(true);
	                msg.setMsg("Item added to the database successfully.");
	            } else {
	                msg.setSucceeded(false);
	                msg.setMsg("Failed to add the item to the database.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            msg.setSucceeded(false);
	            msg.setMsg("Database error: " + e.getMessage());
	        }
	        
	        return msg;
	    }

	    /**
	     * Logs out all users by setting their isLoggedIn status to false in the database.
	     *
	     * @param msg The CommMessage object to store the result and any messages.
	     * @return The updated CommMessage object.
	     */
	    public static CommMessage LogoutAll(CommMessage msg) {
	    	String updateQuery = "UPDATE biteme.users SET isLoggedIn = 0";
	        PreparedStatement updateStmt;
			try {
				updateStmt = conn.prepareStatement(updateQuery);
				updateStmt.executeUpdate();
				msg.setSucceeded(true);
				msg.setMsg("All Users logged in successfully");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.setSucceeded(false);
	            msg.setMsg("Database error");
			}

	    	return msg;
	    }
	    
	    /**
	     * Retrieves all full orders for a specific user from the database using their username.
	     *
	     * @param username The username of the user.
	     * @param msg      The CommMessage object to store the result and any messages.
	     * @return The updated CommMessage object with the list of full orders.
	     */
	    public static CommMessage GetFullOrdersForUser(String username, CommMessage msg) {
	        try {
	            String query = "SELECT * FROM orders WHERE Username = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, username);
	            ResultSet rs = stmt.executeQuery();

	            // Check if result set is empty
	            if (!rs.isBeforeFirst()) {
	                msg.setSucceeded(false);
	                msg.setMsg("Didn't find any Orders for the given username");
	                return msg;
	            }

	            List<Order> orders = new ArrayList<>();
	            while (rs.next()) {
	                // Extracting data from ResultSet
	                int orderId = rs.getInt("OrderID");
	                int numOfItems = rs.getInt("NumOfItems");

	                OrderStatus status = null;
	                String statusStr = rs.getString("Status");
	                if (statusStr != null) {
	                    status = OrderStatus.getEnum(statusStr);
	                }

	                double total_price = rs.getDouble("TotalPrice");
	                String restaurantID = rs.getString("RestaurantID");
	                boolean isLate = rs.getBoolean("IsLate");
	                int deliveryID = rs.getInt("DeliveryID");
	                Date askedByCustomerDate = rs.getDate("AskedByCustomerDate");
	                Time askedByCustomerTime = rs.getTime("AskedByCustomerTime");
	                Date approvedByResDate = rs.getDate("ApprovedByResDate");
	                Time approvedByResTime = rs.getTime("ApprovedByResTime");
	                Date recievedByCustomerDate = rs.getDate("RecievedByCustomerDate");
	                Time recievedByCustomerTime = rs.getTime("RecievedByCustomerTime");

	                TypeOfOrder type = null;
	                String typeStr = rs.getString("Type");
	                if (typeStr != null) {
	                    type = TypeOfOrder.getEnum(typeStr);
	                }

	                Order order = new Order(orderId, numOfItems, status, total_price, restaurantID, isLate,
	                        username, deliveryID, askedByCustomerDate, askedByCustomerTime, approvedByResDate, 
	                        approvedByResTime, recievedByCustomerDate, recievedByCustomerTime, type, new ArrayList<items>());

	                // Fetching items in order
	                String itemQuery = "SELECT iio.*, i.ItemName, i.Price, i.MenuID, i.Type, i.Size, i.Doneness FROM itemsinorder iio JOIN items i ON iio.ItemID = i.ItemID WHERE iio.OrderID = ?";
	                PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
	                itemStmt.setInt(1, orderId);
	                ResultSet itemRs = itemStmt.executeQuery();
	                while (itemRs.next()) {
	                    int itemID = itemRs.getInt("ItemID");
	                    String itemName = itemRs.getString("ItemName");
	                    float price = itemRs.getFloat("Price");
	                    int menuID = itemRs.getInt("MenuID");

	                    TypeOfProduct typeOfProduct = null;
	                    String typeOfProductStr = itemRs.getString("Type");
	                    if (typeOfProductStr != null) {
	                        typeOfProduct = TypeOfProduct.getEnum(typeOfProductStr);
	                    }

	                    ProductSize size = null;
	                    String sizeStr = itemRs.getString("Size");
	                    if (sizeStr != null) {
	                        size = ProductSize.getEnum(sizeStr);
	                    }

	                    Doneness doneness = null;
	                    String donenessStr = itemRs.getString("Doneness");
	                    if (donenessStr != null) {
	                        doneness = Doneness.getEnum(donenessStr);
	                    }

	                    String foodRequests = itemRs.getString("foodRequests");

	                    items item = new items(itemID, itemName, price, menuID, typeOfProduct, size, doneness, foodRequests);
	                    order.getItemsInOrder().add(item);
	                }

	                orders.add(order);
	            }
	            msg.setSucceeded(true);
	            msg.setDataFromServer(orders);
	            msg.setMsg("Orders ArrayList<Order> fetched successfully!");

	        } catch (SQLException e) {
	            e.printStackTrace();
	            msg.setSucceeded(false);
	            msg.setMsg("Database Error");
	        }
	        return msg;
	    }
	    
	    /**
	     * Imports users from the importedusers table to the main users table, avoiding duplicates.
	     *
	     * @param msg The CommMessage object to store the result and any messages.
	     * @return The updated CommMessage object with the result of the import operation.
	     */
	    public static CommMessage importUsersFromImportedUsers(CommMessage msg) {
	        try {
	            // Step 1: Check if the importedusers table is empty
	            String checkIfEmptyQuery = "SELECT COUNT(*) FROM importedusers";
	            PreparedStatement stmt = conn.prepareStatement(checkIfEmptyQuery);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next() && rs.getInt(1) == 0) { // If the count is 0, the table is empty
	                msg.setSucceeded(false);
	                msg.setMsg("No users to import.");
	                return msg;
	            }

	            // Step 2: Get all users from importedusers
	            String getUsersQuery = "SELECT * FROM importedusers";
	            stmt = conn.prepareStatement(getUsersQuery);
	            rs = stmt.executeQuery();

	            ArrayList<String> existingUsernames = new ArrayList<>();
	            ArrayList<String> importedUsernames = new ArrayList<>();
	            ArrayList<String> failedImports = new ArrayList<>();
	            
	            while (rs.next()) {
	                String username = rs.getString("Username");

	                // Step 3: Check if the username already exists in the users table
	                String checkUsernameQuery = "SELECT * FROM users WHERE Username = ?";
	                PreparedStatement checkStmt = conn.prepareStatement(checkUsernameQuery);
	                checkStmt.setString(1, username);
	                ResultSet checkRs = checkStmt.executeQuery();
	                if (checkRs.next()) {
	                    // Username already exists
	                    existingUsernames.add(username);
	                    failedImports.add(username);
	                    continue;
	                }

	                // Step 4: Move the user to the users table
	                String insertUserQuery = "INSERT INTO users (Username, Password, FirstName, LastName, Email, PhoneNumber, Role, HomeBranch, ID, isLoggedIn, isApproved) " +
	                                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	                PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery);
	                insertStmt.setString(1, rs.getString("Username"));
	                insertStmt.setString(2, rs.getString("Password"));
	                insertStmt.setString(3, rs.getString("FirstName"));
	                insertStmt.setString(4, rs.getString("LastName"));
	                insertStmt.setString(5, rs.getString("Email"));
	                insertStmt.setString(6, rs.getString("PhoneNumber"));
	                insertStmt.setString(7, rs.getString("Role"));
	                insertStmt.setString(8, rs.getString("HomeBranch"));
	                insertStmt.setInt(9, rs.getInt("ID"));
	                insertStmt.setInt(10, rs.getInt("isLoggedIn"));
	                insertStmt.setInt(11, rs.getInt("isApproved"));

	                int affectedRows = insertStmt.executeUpdate();
	                if (affectedRows > 0) {
	                    importedUsernames.add(username);
	                } else {
	                    failedImports.add(username);
	                }
	            }

	            // Step 5: Delete successfully imported users from importedusers table
	            if (!importedUsernames.isEmpty()) {
	                String deleteUsersQuery = "DELETE FROM importedusers WHERE Username IN (" +
	                                          String.join(",", importedUsernames.stream().map(u -> "'" + u + "'").toArray(String[]::new)) +
	                                          ")";
	                stmt = conn.prepareStatement(deleteUsersQuery);
	                stmt.executeUpdate();
	            }

	            // Step 6: Set the message for CommMessage
	            if (importedUsernames.isEmpty() && failedImports.isEmpty()) {
	                msg.setSucceeded(false);
	                msg.setMsg("No users were imported.");
	            } else if (!failedImports.isEmpty()) {
	                msg.setSucceeded(false);
	                msg.setMsg("Some users were not imported due to existing usernames: " + String.join(", ", failedImports));
	            } else {
	                msg.setSucceeded(true);
	                msg.setMsg("All users were successfully imported.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            msg.setSucceeded(false);
	            msg.setMsg("Database Error: " + e.getMessage());
	        }
	        return msg;
	    }
}
