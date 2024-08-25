package EnumsAndConstants;

public enum TypeOfOrder {
    PRE, Regular, Shared, Robot, PickUp;

    public static TypeOfOrder getEnum(String name) {
        switch (name) {
            case "PRE":
                return PRE;
            case "Regular":
                return Regular;
            case "Shared":
                return Shared;
            case "Robot":
                return Robot;
            case "PickUp":
                return PickUp;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case PRE:
                return "PRE";
            case Regular:
                return "Regular";
            case Shared:
                return "Shared";
            case Robot:
                return "Robot";
            case PickUp:
                return "PickUp";
            default:
                return null;
        }
    }
}
