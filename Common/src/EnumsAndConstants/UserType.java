package EnumsAndConstants;

public enum UserType {
    Customer, BusinessCustomer, BranchManager, CEO, Supplier;

    public static UserType getEnum(String name) {
        switch (name) {
            case "Customer":
                return Customer;
            case "BusinessCustomer":
                return BusinessCustomer;
            case "BranchManager":
                return BranchManager;
            case "CEO":
                return CEO;
            case "Supplier":
                return Supplier;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case Customer:
                return "Customer";
            case BusinessCustomer:
                return "BusinessCustomer";
            case BranchManager:
                return "BranchManager";
            case CEO:
                return "CEO";
            case Supplier:
                return "Supplier";
            default:
                return null;
        }
    }
}
