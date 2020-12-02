import java.sql.*;
import java.util.Scanner;

class Manager{
    Connection con = null;
    Statement stmt = null;
    public Manager(Connection c, Scanner sc){
        int options;
        Scanner s = sc;
        con = c;
        try{
            stmt = con.createStatement();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            options = getOptions(s);
            if(options == 1){
               
            }
            else if (options == 2){
                break;
            }

        }
    }
    
    void managerPageMessage(){
        System.out.println("Manager, what you want to do");
        System.out.println("1.Find trip");
        System.out.println("2.Go back");
        System.out.println("Please enter [1-2]");
    }

    void errorMessage(){
        System.out.println("Error, please enter [1-2]");
    }
    
    int getOptions(Scanner s){
        int options = 0;
        managerPageMessage();
        //Scanner s = new Scanner(System.in);
        if(s.hasNextInt()){
            options = s.nextInt(); 
        }
		while(options < 1 || options > 2){
			errorMessage();
            options = s.nextInt(); 
		}
		return options;
    }

    void findTrips(Scanner s){
        int minDist;
        int maxDist;
        System.out.println("please enter the minimum distance");
        minDist = s.nextInt();
        while(minDist < 0){
            System.out.println("Please enter valid distance");
        }
        System.out.println("please enter the maximum distance");
        maxDist = s.nextInt();
        while(maxDist < minDist){
            System.out.println("Please enter valid distance");
        }   
        
    }

}