package logic;

import java.io.Serializable;
import EnumsAndConstants.*;

/**
*         dishName, ArrayList<Component> components, float price
 */

public class items implements Serializable{

	private static final long serialVersionUID = -3079554028160660848L;
	private int ItemID;
	private String ItemName;
	private float price;
	private int MenuID;
	private TypeOfProduct Type;
	private ProductSize Size;
	private Doneness Doneness;
	private String FoodRequests;
	
	public items(int itemID, String itemName, float price, int menuID, TypeOfProduct type,
			ProductSize size, EnumsAndConstants.Doneness doneness, String FoodRequests) {
		super();
		ItemID = itemID;
		ItemName = itemName;
		this.price = price;
		MenuID = menuID;
		Type = type;
		Size = size;
		Doneness = doneness;
		this.FoodRequests = FoodRequests;
	}
	
	public items() {}
	
	public items(int itemID, String itemName, float price, int menuID, TypeOfProduct type, ProductSize size,
			EnumsAndConstants.Doneness doneness) {
		super();
		ItemID = itemID;
		ItemName = itemName;
		this.price = price;
		MenuID = menuID;
		Type = type;
		Size = size;
		Doneness = doneness;
	}
	
	public items(String string, Double refund, int i) {
		ItemName = string;
		price = Float.valueOf(String.valueOf(refund));
		this.ItemID=i;
	}

	public int getItemID() {
		return ItemID;
	}
	public void setItemID(int itemID) {
		ItemID = itemID;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getMenuID() {
		return MenuID;
	}
	public void setMenuID(int menuID) {
		MenuID = menuID;
	}
	public TypeOfProduct getType() {
		return Type;
	}
	public void setType(TypeOfProduct type) {
		Type = type;
	}

	
	public ProductSize getSize() {
		return Size;
	}
	public void setSize(ProductSize size) {
		Size = size;
	}
	public Doneness getDoneness() {
		return Doneness;
	}
	public void setDoneness(Doneness doneness) {
		Doneness = doneness;
	}
	public String getRestrictions() {
		return FoodRequests;
	}
	public void setRestrictions(String restrictions) {
		FoodRequests = restrictions;
	}
	
	@Override
    public String toString() {
        return "ItemID: " + ItemID +
                ", ItemName: " + ItemName +
                ", Price: " + price +
                ", MenuID: " + MenuID +
                ", Type: " + (Type != null ? Type.toString() : "null") +
                ", Size: " + (Size != null ? Size.toString() : "null") +
                ", Doneness: " + (Doneness != null ? Doneness.toString() : "null") +
                ", Restrictions: " + (FoodRequests != null ? FoodRequests : "null");
    }
	

	
	
	
}
