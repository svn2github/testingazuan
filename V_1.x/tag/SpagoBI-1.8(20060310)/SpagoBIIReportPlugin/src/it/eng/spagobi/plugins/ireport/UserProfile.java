package it.eng.spagobi.plugins.ireport;

public class UserProfile {

	private static String username = "";
	private static String password = "";
	
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		UserProfile.password = password;
	}
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		UserProfile.username = username;
	}
	
}
