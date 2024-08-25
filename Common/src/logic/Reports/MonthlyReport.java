package logic.Reports;

import java.io.Serializable;
import java.util.HashMap;

import EnumsAndConstants.BranchLocation;

public class MonthlyReport extends Report implements Serializable {

    private static final long serialVersionUID = -1307826367537589027L;
    private String reportType;
    private int totalOrders; //for perfomence report
    private int lateOrders;//for perfomence report
    private HashMap<String, Double> revenuePerRestaurant; //for revenue report divided by restaurants
    private HashMap<String, HashMap<String, Integer>> ordersPerRestaurant; //for sales report 

    public MonthlyReport(String date, BranchLocation branchLocation, String reportType) {
        super("Monthly", date, branchLocation);
        this.reportType = reportType;
    }

    /**
     * @return the reportType
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * @param reportType the reportType to set
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * @return the totalOrders
     */
    public int getTotalOrders() {
        return totalOrders;
    }

    /**
     * @param totalOrders the totalOrders to set
     */
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    /**
     * @return the lateOrders
     */
    public int getLateOrders() {
        return lateOrders;
    }

    /**
     * @param lateOrders the lateOrders to set
     */
    public void setLateOrders(int lateOrders) {
        this.lateOrders = lateOrders;
    }

    /**
     * @return the revenuePerRestaurant
     */
    public HashMap<String, Double> getRevenuePerRestaurant() {
        return revenuePerRestaurant;
    }

    /**
     * @param revenuePerRestaurant the revenuePerRestaurant to set
     */
    public void setRevenuePerRestaurant(HashMap<String, Double> revenuePerRestaurant) {
        this.revenuePerRestaurant = revenuePerRestaurant;
    }

    /**
     * @return the ordersPerRestaurant
     */
    public HashMap<String, HashMap<String, Integer>> getOrdersPerRestaurant() {
        return ordersPerRestaurant;
    }

    /**
     * @param ordersPerRestaurant the ordersPerRestaurant to set
     */
    public void setOrdersPerRestaurant(HashMap<String, HashMap<String, Integer>> ordersPerRestaurant) {
        this.ordersPerRestaurant = ordersPerRestaurant;
    }

    /**
     * Sets the data for the monthly report based on the report type.
     *
     * @param data The data to set.
     */
    @SuppressWarnings("unchecked")
	public void setData(Object data) {
        switch (reportType) {
            case "Revenue":
                this.revenuePerRestaurant = (HashMap<String, Double>) data;
                break;
            case "Orders":
                this.ordersPerRestaurant = (HashMap<String, HashMap<String, Integer>>) data;
                break;
            case "Performance":
                // Not used, as Performance report sets totalOrders and lateOrders directly.
                break;
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
    }
}
