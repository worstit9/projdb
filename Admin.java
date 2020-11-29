import java.sql.*;
import java.util.Scanner;

class Admin{
    Connection con = null;
    Statement stmt = null;
    
    String createDriverTb = "CREATE TABLE driver(id int, name varchar(10), vehicle_id int, driving_years int, Primary Key(id))"; 
    String createVehTb = "CREATE TABLE vehicle(id int, model varchar(20), seats int, Primary Key(id))";
    String createPassengerTb = "CREATE TABLE passenger(id int, name varchar(10), Primary Key(id))";
    String createRequestTb = "CREATE TABLE request(id int, passenger_id int, start_location varchar(20), destination varchar(20), model varchar(20), passengers int, taken boolean, driving_years int, Primary Key(id)) ";
    String createTripTb = "CREATE TABLE trip(id int, driver_id int, passenger_id int, start_location varchar(20), destination varchar(20), start_time datetime, end_time datetime, fee int, Primary Key(id))";
    String createTaxistopTb = "CREATE TABLE taxi_stop(name varchar(20), location_x int, location_y int, Primary Key(name))";

    String dropDriverTb = "DROP TABLE driver";
    String dropVehTb = "DROP TABLE vehicle";
    String dropPassengerTb = "DROP TABLE passenger";
    String dropRequestTb = "DROP TABLE request";
    String dropTripTb = "DROP TABLE trip";
    String dropTaxistopTb = "DROP TABLE taxi_stop";

    public Admin(Connection c){
        int options;
        con = c;
        try{
            stmt = con.createStatement();
            System.out.println(con);
            System.out.println(stmt);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            options = getOptions();
            if(options == 1){
                createTb();
            }
            else if (options == 2){
                dropTb();
            }
            else if (options == 3){
                insertData();
            }
            else if (options == 4){
                showData();
            }
            else if(options == 5){
                break;
            }
        }

    }

    int getOptions(){
        int options;

		adminPageMessage();
        Scanner s = new Scanner(System.in);
            options = s.nextInt(); 

		while(options < 1 || options > 5){
			errorMessage();
			options = s.nextInt();
		}
		return options;
    }

    public void createTb(){
        try{
            stmt.executeUpdate(createDriverTb);
            stmt.executeUpdate(createVehTb);
            stmt.executeUpdate(createPassengerTb);
            stmt.executeUpdate(createRequestTb);
            stmt.executeUpdate(createTripTb);
            stmt.executeUpdate(createTaxistopTb);
            System.out.println("Create all");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public void dropTb(){
        try{
            stmt.executeUpdate(dropDriverTb);
            stmt.executeUpdate(dropVehTb);
            stmt.executeUpdate(dropPassengerTb);
            stmt.executeUpdate(dropRequestTb);
            stmt.executeUpdate(dropTripTb);
            stmt.executeUpdate(dropTaxistopTb);
            System.out.println("Drop all");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    }
    void adminPageMessage(){
        System.out.println("What you want to do, admin");
        System.out.println("1. create table");
        System.out.println("2. delete table");
        System.out.println("3. load data");
        System.out.println("4. check data");
        System.out.println("5. go back");
        System.out.println("please enter 1-5");
    }

    void errorMessage(){
        System.out.println("Error: please enter 1-5");
    }

    void insertData(){
        System.out.println("Data is inserted");
    }
    void showData(){
        System.out.println("Data is showned");
    }
}