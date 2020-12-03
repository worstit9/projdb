import java.sql.*;
import java.util.Scanner;

class Main {
	//class deal with start page

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
	int getOptions(Scanner s){		
		int options;

		startUpMessage();
		//Scanner s = new Scanner(System.in);
		options = s.nextInt();

		while(options < 1 || options > 5){
			messageAfterInvalidInput();
			options = s.nextInt();
		}
		return options;
		
	}

    public static void main(String[] args) {		
		Connect dbConnection = new Connect();  //start connection to db
		Connection con = dbConnection.getConnection();
		Scanner s = new Scanner(System.in);

		while (true){
			Main welcomePage = new Main();
			int options = welcomePage.getOptions(s);
			if (options == 1){
				Admin a = new Admin(con,s);
			}
			else if(options == 2){
				System.out.println("Entered passenger mode");
				Passenger p  = new Passenger(con,s);
			}
			else if(options == 3){
				System.out.println("Entered driver mode");
			}
			else if(options == 4){
				System.out.println("Entered manager mode");
				Manager m = new Manager(con,s);
			}
			else if(options == 5){
				System.out.println("bye");
				break;
			}

		}
        

    }
        
}