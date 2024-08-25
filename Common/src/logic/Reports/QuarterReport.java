package logic.Reports;

import java.io.Serializable;
import java.util.TreeMap;

import EnumsAndConstants.BranchLocation;
import logic.Orders.OrderSummary;

public class QuarterReport extends Report implements Serializable {

    private static final long serialVersionUID = -5532624314759118258L;
    private int fromMonth;
    private int toMonth;
    private int year;
    private TreeMap<String, OrderSummary> ordersAndProfitPerDay;

    public QuarterReport(String title, String date, BranchLocation branchLocation, int fromMonth, int toMonth, int year) {
        super(title, date, branchLocation);
        this.fromMonth = fromMonth;
        this.toMonth = toMonth;
        this.year = year;
        this.ordersAndProfitPerDay = new TreeMap<>();
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public TreeMap<String, OrderSummary> getOrdersAndProfitPerDay() {
        return ordersAndProfitPerDay;
    }

    public void setOrdersAndProfitPerDay(TreeMap<String, OrderSummary> ordersAndProfitPerDay) {
        this.ordersAndProfitPerDay = ordersAndProfitPerDay;
    }
}
