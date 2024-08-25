package EnumsAndConstants;

public enum ProductSize {
    Small, Medium, Large;

    public static ProductSize getEnum(String name) {
        switch (name) {
            case "Small":
                return Small;
            case "Medium":
            	return Medium;
            case "Large":
                return Large;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case Small:
                return "Small";
            case Medium:
            	return "Medium";
            case Large:
                return "Large";
            default:
                return null;
        }
    }
}
