package ui;
import java.util.HashMap;

public class UserInfo {
	
	HashMap<String,String> loginInfo = new HashMap<String,String>();
	
	UserInfo(){
		
		loginInfo.put("James", "CSC131");
	}
	
	protected HashMap getLoginInfo(){
		return loginInfo;
	}

}
