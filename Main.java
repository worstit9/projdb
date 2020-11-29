import java.sql.*;
import java.util.Scanner;

class Main {
    Connection con = null;
	Statement stmt = null;
	
	String create_new_tb = "CREATE TABLE name_age(name varchar(10), age int)"; 

	void startUpMessage() {
        //The welcome page
        System.out.println("Welcome! Who are you?");
        System.out.println("1. Admin");
        System.out.println("2. Passenger");
        System.out.println("3. Driver");
        System.out.println("4. Manager");
        System.out.println("5. None");
        System.out.println("Choose Input");
		
    }
    
    void newConnection() {
		
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
        Main db = new Main();
        db.startUpMessage();
        db.newConnection();
    	try{
    		db.stmt.executeUpdate(db.create_new_tb);
    		db.stmt.executeUpdate("INSERT INTO name_age values('Alice',1)");
    		db.stmt.executeUpdate("INSERT INTO name_age values('Bob',2)");
    		db.stmt.executeUpdate("INSERT INTO name_age values('Charlie',3)");
    		db.stmt.executeUpdate("INSERT INTO name_age values('Eve',5)");
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
        
}