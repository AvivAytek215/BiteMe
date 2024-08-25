package logic;

import java.io.Serializable;
import java.util.ArrayList;

import EnumsAndConstants.*;

/**
 * Supplier class, extends user and stores user+ : String restaurantName,
 * ArrayList<Product> Menu, int monthlyComissionPercent,Branch relatedBranch
 */
public class Restaurant implements Serializable{

	private static final long serialVersionUID = 987654321L;
	private int restaurantId; 
	private String restaurantName;
	private String restaurantAddress; 
	private RestaurantType restaurantType;
	private String SupplierId;
	private BranchLocation BranchLocation;
	private int MenuId;
	private ArrayList<items> Menu; 

	public Restaurant(int restaurantId, String restaurantName, String restaurantAddress, RestaurantType restaurantType,
			String supplierId, BranchLocation branchLocation) {
		super();
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.restaurantAddress = restaurantAddress;
		this.restaurantType = restaurantType;
		SupplierId = supplierId;
		BranchLocation = branchLocation;
	}

	public Restaurant(String restaurantName, RestaurantType restaurantType) { // MyFile restaurantLogo
		super();
		this.restaurantName = restaurantName;
		this.restaurantType = restaurantType;
	}

	public Restaurant() {
		super();
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public ArrayList<items> getMenu() {
		return Menu;
	}

	public String getRestaurantAddress() {
		return restaurantAddress;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public void setMenu(ArrayList<items> menu) {
		Menu = menu;
	}

	public void setRestaurantAddress(String restaurantAddress) {
		this.restaurantAddress = restaurantAddress;
	}

	public RestaurantType getRestaurantType() {
		return restaurantType;
	}

	public void setRestaurantType(RestaurantType restaurantType) {
		this.restaurantType = restaurantType;
	}

	@Override
	public String toString() {
	    return "Restaurant [restaurantId=" + restaurantId + 
	           ", restaurantName=" + (restaurantName != null ? restaurantName : "null") + 
	           ", restaurantAddress=" + (restaurantAddress != null ? restaurantAddress : "null") + 
	           ", restaurantType=" + (restaurantType != null ? restaurantType : "null") + 
	           ", SupplierId=" + (SupplierId != null ? SupplierId : "null") + 
	           ", BranchLocation=" + (BranchLocation != null ? BranchLocation : "null") + 
	           ", MenuId=" + MenuId + "]";
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(String supplierId) {
		SupplierId = supplierId;
	}

	public BranchLocation getBranchLocation() {
		return BranchLocation;
	}

	public void setBranchLocation(BranchLocation branchLocation) {
		BranchLocation = branchLocation;
	}

	public int getMenuId() {
		return MenuId;
	}

	public void setMenuId(int menuId) {
		MenuId = menuId;
	}

}
