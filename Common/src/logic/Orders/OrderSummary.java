package logic.Orders;

import java.io.Serializable;

public class OrderSummary implements Serializable{
	private static final long serialVersionUID = 112233445566778899L;
	private int orderCount;
    private double totalProfit;

    public OrderSummary(int orderCount, double totalProfit) {
        this.orderCount = orderCount;
        this.totalProfit = totalProfit;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "orderCount=" + orderCount +
                ", totalProfit=" + totalProfit +
                '}';
    }
}
