package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class menu implements Serializable{
	
	private static final long serialVersionUID = 3336427101534731062L;
	private int MenuId;
	private int restaurantId; 
	private int numOfItems;
	private ArrayList<items> itemsInMenu;  
	
	public int getMenuId() {
		return MenuId;
	}
	public void setMenuId(int menuId) {
		MenuId = menuId;
	}
	public ArrayList<items> getItemsInMenu() {
		return itemsInMenu;
	}
	public void setItemsInMenu(ArrayList<items> itemsInMenu) {
		this.itemsInMenu = itemsInMenu;
	}
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public int getNumOfItems() {
		return numOfItems;
	}
	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}
	
	
}
