package EnumsAndConstants;

public enum OrderStatus {
	Pending, Approved, Delivered, Received, Ready;
	
	public static OrderStatus getEnum(String name) {
		switch(name) {
		case "Pending":
			return Pending;
		case "Approved":
			return Approved;
		case "Delivered":
			return Delivered;
		case "Received":
			return Received;
		case "Ready":
			return Ready;
		default:
			return null;
		}
	}
	
	public String toString() {
		switch (this) { 
		case Pending:
			return "Pending";
		case Approved:
			return "Approved";
		case Delivered:
			return "Delivered";
		case Received:
			return "Received";
		case Ready:
			return "Ready";
		default:
			return null;
		}
	}
}
