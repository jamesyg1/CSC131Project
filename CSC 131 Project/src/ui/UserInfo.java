package ui;
import java.util.HashMap;

public class UserInfo {
	
    private static UserInfo instance;
	HashMap<String,String> loginInfo = new HashMap<String,String>();
	
	UserInfo(){
		
	}
	
	/* Singleton Design Pattern */
	public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }
	
	protected HashMap getLoginInfo(){
		return loginInfo;
	}

}
