package EnumsAndConstants;

public enum PaymentMethod {
    CreditCard, Cash;

    public static PaymentMethod getEnum(String name) {
        switch (name) {
            case "Credit Card":
                return CreditCard;
            case "Cash":
                return Cash;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case CreditCard:
                return "Credit Card";
            case Cash:
                return "Cash";
            default:
                return null;
        }
    }
}
