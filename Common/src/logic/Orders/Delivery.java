package logic.Orders;

import java.io.Serializable;
import java.util.ArrayList;

import EnumsAndConstants.TypeOfOrder;


public class Delivery implements Serializable  {


	private static final long serialVersionUID = 1L;
	private int deliveryId;
	private String Name;
	private String phone;
	private String Address;
	private TypeOfOrder Type;
	private double DeliveryFee;
	private int orderId;
	private ArrayList<String> UsernamesOfParticipants;
	private int NumOfParticipants;

	public Delivery() {}
	
	public Delivery(int deliveryId, String address, TypeOfOrder type, float deliveryFee, int orderId,
			ArrayList<String> usernamesOfParticipants, int numOfParticipants) {
		super();
		this.deliveryId = deliveryId;
		Address = address;
		Type = type;
		DeliveryFee = deliveryFee;
		this.orderId = orderId;
		UsernamesOfParticipants = usernamesOfParticipants;
		NumOfParticipants = numOfParticipants;
	}
	
	
	public Delivery(int orderId2, int NumOfParticipants, TypeOfOrder type, int DeliveryFee) {
		this.orderId = orderId2;
		this.NumOfParticipants = NumOfParticipants;
		this.Type = type;
		this.DeliveryFee = DeliveryFee;
		UsernamesOfParticipants = new ArrayList<String>();
	}
	
	
	public Delivery(int orderId, String firstName, String phoneNumber, int NumOfParticipants, int deliveryfee) {
		this.orderId = orderId;
		this.NumOfParticipants = NumOfParticipants;
		this.DeliveryFee = deliveryfee;
		UsernamesOfParticipants = new ArrayList<String>();
	}

	public String toString() {
		StringBuilder b = new StringBuilder("Delivery Details:\n");
		if (Address != null) {
			b.append("Address: " + Address);
			return b.toString();
		}
		return "Take Away";
	}

	public TypeOfOrder getType() {
		return Type;
	}

	public void setType(TypeOfOrder type) {
		Type = type;
	}

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public double getDeliveryFee() {
		return DeliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		DeliveryFee = deliveryFee;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getNumOfParticipants() {
		return NumOfParticipants;
	}

	public void setNumOfParticipants(int numOfParticipants) {
		NumOfParticipants = numOfParticipants;
	}

	public ArrayList<String> getUsernamesOfParticipants() {
		return UsernamesOfParticipants;
	}

	public void setUsernamesOfParticipants(ArrayList<String> usernamesOfParticipants) {
		UsernamesOfParticipants = usernamesOfParticipants;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getPhoneNumber() {
		return phone;
	}

	public void setPhoneNumber(String phone) {
		this.phone = phone;
	}


}
