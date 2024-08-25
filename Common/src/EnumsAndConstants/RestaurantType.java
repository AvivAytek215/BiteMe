package EnumsAndConstants;

public enum RestaurantType {
    ITALIAN, ASIAN, FASTFOOD, MEAT;

    public static RestaurantType getEnum(String name) {
        switch (name) {
            case "ITALIAN":
                return ITALIAN;
            case "ASIAN":
                return ASIAN;
            case "FASTFOOD":
                return FASTFOOD;
            case "MEAT":
                return MEAT;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case ITALIAN:
                return "ITALIAN";
            case ASIAN:
                return "ASIAN";
            case FASTFOOD:
                return "FASTFOOD";
            case MEAT:
                return "MEAT";
            default:
                return null;
        }
    }
}
