package logic;

import java.io.Serializable;
import EnumsAndConstants.BranchLocation;
import logic.Users.User;

public class Branch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7545747177565237611L;
	private BranchLocation branchLocation;
	private int NumOfRestaurants;
	private User branchManager;

	
	public Branch(BranchLocation branchLocation, User branchManager, int NumOfRestaurants) {
		super();
		this.branchLocation = branchLocation;
		this.branchManager = branchManager;
		this.setNumOfRestaurants(NumOfRestaurants);
	}
		
	
	/**
	 * @return the BranchLocation
	 */
	public BranchLocation getBranchLocation() {
		return branchLocation;
	}
	
	/**
	 * @return the branchManager
	 */
	public User getBranchManager() {
		return branchManager;
	}
	
	/**
	 * @param BranchLocation the branchName to set
	 */
	public void setBranchLocation(BranchLocation branchLocation) {
		this.branchLocation = branchLocation;
	}
	
	/**
	 * @param branchManager the branchManager to set
	 */
	public void setBranchManager(User branchManager) {
		this.branchManager = branchManager;
	}


	public int getNumOfRestaurants() {
		return NumOfRestaurants;
	}


	public void setNumOfRestaurants(int numOfRestaurants) {
		NumOfRestaurants = numOfRestaurants;
	}
		
}
