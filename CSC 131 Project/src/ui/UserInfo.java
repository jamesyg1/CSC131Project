package ui;
import java.util.HashMap;

public class UserInfo {
	
	HashMap<String,String> loginInfo = new HashMap<String,String>();
	
	UserInfo(){
		
	}
	
	protected HashMap getLoginInfo(){
		return loginInfo;
	}

}
