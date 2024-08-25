package logic.Users;

import EnumsAndConstants.*;

public class Supplier extends User {

	private static final long serialVersionUID = 1L;
	private String RestauarantID;

    public Supplier(String userName, String password, String firstName, String lastName, String email,
                    String phoneNumber, UserType userType, BranchLocation mainBranch, String id, int isLoggedIn) {
        super(userName, password, firstName, lastName, email, phoneNumber, userType, mainBranch, id, isLoggedIn);
        this.setUserType(UserType.Supplier);
    }

	public String getRestauarantID() {
		return RestauarantID;
	}

	public void setRestauarantID(String restauarantID) {
		RestauarantID = restauarantID;
	}

}