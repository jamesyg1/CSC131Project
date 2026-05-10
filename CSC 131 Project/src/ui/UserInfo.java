package ui;

import java.util.HashMap;
import model.User;
import service.DatabaseService;

public class UserInfo {

	private static UserInfo instance;
	private HashMap<String, User> loginInfo = new HashMap<>();

	private UserInfo() {
		loginInfo = DatabaseService.getInstance().loadAllUsers();
		System.out.println("UserInfo loaded " + loginInfo.size() + " user(s) from database.");
	}

	public static UserInfo getInstance() {
		if (instance == null) {
			instance = new UserInfo();
		}
		return instance;
	}

	public HashMap<String, User> getLoginInfo() {
		return loginInfo;
	}
}