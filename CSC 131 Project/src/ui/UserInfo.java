package ui;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import model.Receipt;

public class UserInfo {
	
    private static UserInfo instance;
	HashMap<String,String> loginInfo = new HashMap<String,String>();
	private List<Receipt> allReceipts = new ArrayList<>();

	
	UserInfo(){
		
	}
	
	/* Singleton Design Pattern */
	
	public static UserInfo getInstance(){
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }
	
	protected HashMap getLoginInfo(){
		return loginInfo;
	}
	
	public List<Receipt> getAllReceipts() {
	    return allReceipts;
	}

}
