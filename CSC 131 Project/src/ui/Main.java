package ui;

public class Main {
	
	public static void main(String[] args) {
		
		UserInfo userInfo = UserInfo.getInstance();
        new LoginPage(userInfo.getLoginInfo());		
        
	}

}
