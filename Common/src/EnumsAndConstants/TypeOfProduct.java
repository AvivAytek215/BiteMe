package EnumsAndConstants;

public enum TypeOfProduct {
    MainCourse, Drink, Side, Soup;

    public static TypeOfProduct getEnum(String name) {
        switch (name) {
            case "MainCourse":
                return MainCourse;
            case "Drink":
                return Drink;
            case "Side":
                return Side;
            case "Soup":
            	return Soup;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case MainCourse:
                return "MainCourse";
            case Drink:
                return "Drink";
            case Side:
                return "Side";
            case Soup:
            	return "Soup";
            default:
                return null;
        }
    }
}
