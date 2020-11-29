import java.sql.*;

class Main {
    static Connection con = null;
	static Statement stmt = null;
	
	static String create_new_tb = "CREATE TABLE name_age(name varchar(10), age int)"; 
    
	static void startUpMessage() {
        //The welcome page
        System.out.println("Welcome! Who are you?");
        System.out.println("1. Admin");
        System.out.println("2. Passenger");
        System.out.println("3. Driver");
        System.out.println("4. Manager");
        System.out.println("5. None");
        System.out.println("Choose Input");
		
    }
    
    static void newConnection() {
		
		String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/group77";
		String dbUserName = "Group77";
		String dbPassword = "3170group77";
		try {
			Class.forName("com.mysql.jdbc.Driver");
        	con = DriverManager.getConnection(dbAddress,dbUserName,dbPassword);
        	stmt = con.createStatement();
            
        } catch(Exception e) {
        	System.out.println(e);
        }
	}
    public static void main(String[] args) {
        newConnection();
    	try{
    		stmt.executeUpdate(create_new_tb);
    		stmt.executeUpdate("INSERT INTO name_age values('Alice',1)");
    		stmt.executeUpdate("INSERT INTO name_age values('Bob',2)");
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
        
}