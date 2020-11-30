import java.sql.*;
import java.util.Scanner;

class Main {
	//class deal with start page
    Connection con = null;
	Statement stmt = null;
	
	

	void startUpMessage() {
        //The welcome page
        System.out.println("Welcome! Who are you?");
        System.out.println("1. Admin");
        System.out.println("2. Passenger");
        System.out.println("3. Driver");
        System.out.println("4. Manager");
        System.out.println("5. None");
        System.out.println("Choose Input 1-4");
		
	}
	
	void messageAfterInvalidInput(){
		System.out.println("Invalid input");
		System.out.println("Choose Input 1-4");
	}
	int getOptions(){		
		int options;


		startUpMessage();
		Scanner s = new Scanner(System.in);
		options = s.nextInt();

		while(options < 1 || options > 5){
			messageAfterInvalidInput();
			options = s.nextInt();
		}
		return options;
		
	}
    
    void newConnection() {
		//unused
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
		Connect dbConnection = new Connect();  //start connection to db
		Connection con = dbConnection.getConnection();

		//Main welcomePage = new Main();
		//int options = welcomePage.getOptions();
		while (true){
			Main welcomePage = new Main();
			int options = welcomePage.getOptions();
			if (options == 1){
				Admin a = new Admin(con);
			}
			else if(options == 2){
				System.out.println("Entered passenger mode");
			}
			else if(options == 3){
				System.out.println("Entered driver mode");
			}
			else if(options == 4){
				System.out.println("Entered manager mode");
			}
			else if(options == 5){
				System.out.println("bye");
				break;
			}

		}
        

    }
        
}