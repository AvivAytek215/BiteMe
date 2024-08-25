package logic.Users;


import java.io.Serializable;

import EnumsAndConstants.*;


/**
 * Abstract class to describe a User.
 * 
 */
public class User implements Serializable{
	

	private static final long serialVersionUID = -8749032741240943517L;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private UserType role;
	private BranchLocation mainBranch;
	private String id;
	private int isLoggedIn;
	private int isApproved;
	
	public int getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @param id
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param userType
	 * @param mainBranch
	 */
	public User(String userName, String password, String firstName, String lastName, String email, String phoneNumber,
			UserType role, BranchLocation mainBranch, String id, int isLoggedIn) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.mainBranch = mainBranch;
		this.id = id;
		this.isLoggedIn = isLoggedIn;
	}

	public User(String userName, String password, String firstName, String lastName, String email, String phoneNumber,
			UserType role, BranchLocation mainBranch, String id, int isLoggedIn, int isApproved) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.mainBranch = mainBranch;
		this.id = id;
		this.isLoggedIn = isLoggedIn;
		this.isApproved = isApproved;
	}
	public User() {
	}	

	public void setIsLoggedIn(int isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public int getIsLoggedIn() {
		return isLoggedIn;
	}

	public String getPassword() {
		return password;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id=id;
	}


	public String getEmail() {
		return email;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public UserType getUserType() {
		return role;
	}


	public BranchLocation getMainBranch() {
		return mainBranch;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public void setUserType(UserType role) {
		this.role = role;
	}



	public void setMainBranch(BranchLocation mainBranch) {
		this.mainBranch = mainBranch;
	}




	public String getUserName() {
		return userName;
	}

	public String toString() {
		return id + " " + firstName + " " + lastName;
	}


	
	
}
