package harry.io.test;

import java.io.Console;
import java.util.Arrays;

/**
 * 
 * @author Harry
 *
 */
public class ConsoleTest {
	public static void main(String[] args) {
		if(login()){
			System.out.println("Thanks for logging in!");
		}else{
			System.out.println("Login failed!");
		}
	}
	
	private static boolean login(){
		Console console = System.console();
		boolean isAuthenticated = false;
		if(console != null){
			int count = 0;
			do{
				char[] pwd = console.readPassword("[%s]", "Password:");
				isAuthenticated = authenticate(pwd);
				Arrays.fill(pwd, ' ');
				console.writer().write("\n");
			}while(!isAuthenticated && ++count < 3);
		}
		
		return isAuthenticated;
	}
	
	private static boolean authenticate(char[] passwd){
		char[] secret = {'a','d','m','i','n'};
		if(Arrays.equals(passwd, secret)){
			Arrays.fill(passwd, ' ');
			System.out.println("Authenticated\n");
			
			return true;
		}else{
			System.out.println("Authentication failed\n");
		}
		
		return false;
	}
}
