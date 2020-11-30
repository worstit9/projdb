import java.sql.*;
import java.io.*;
import java.util.Scanner;

class Admin{
    Boolean istableInserted = false;
    Connection con = null;
    Statement stmt = null;

    String dataPath = "./test_data/";
    
    //all queries to be executed
    String createDriverTb = "CREATE TABLE driver(id int, name varchar(30), vehicle_id varchar(6), driving_years int, Primary Key(id))"; 
    String createVehTb = "CREATE TABLE vehicle(id varchar(6), model varchar(30), seats int, Primary Key(id))";
    String createPassengerTb = "CREATE TABLE passenger(id int, name varchar(30), Primary Key(id))";
    String createRequestTb = "CREATE TABLE request(id int, passenger_id int, start_location varchar(20), destination varchar(20), model varchar(30), passengers int, taken boolean, driving_years int, Primary Key(id)) ";
    String createTripTb = "CREATE TABLE trip(id int, driver_id int, passenger_id int, start_location varchar(20), destination varchar(20), start_time datetime, end_time datetime, fee int, Primary Key(id))";
    String createTaxistopTb = "CREATE TABLE taxi_stop(name varchar(20), location_x int, location_y int, Primary Key(name))";

    String dropDriverTb = "DROP TABLE driver";
    String dropVehTb = "DROP TABLE vehicle";
    String dropPassengerTb = "DROP TABLE passenger";
    String dropRequestTb = "DROP TABLE request";
    String dropTripTb = "DROP TABLE trip";
    String dropTaxistopTb = "DROP TABLE taxi_stop";

    String rowDriver = "SELECT COUNT(*) AS rc FROM driver";
    String rowVeh = "SELECT COUNT(*) AS rc FROM vehicle";
    String rowPassenger = "SELECT COUNT(*) AS rc FROM passenger";
    String rowRequest = "SELECT COUNT(*) AS rc FROM request";
    String rowTrip = "SELECT COUNT(*) AS rc FROM trip";
    String rowTaxistop = "SELECT COUNT(*) AS rc FROM taxi_stop";


    public Admin(Connection c){
        int options;
        con = c;
        try{
            stmt = con.createStatement();
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
            System.out.println("no table dropped!");
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
    void loadData(){
        String line = null;
        try{
           BufferedReader vehicleReader = new BufferedReader(new FileReader(dataPath + "vehicles.csv"));
           BufferedReader passengerReader = new BufferedReader(new FileReader(dataPath + "passengers.csv"));
           BufferedReader driverReader = new BufferedReader(new FileReader(dataPath + "drivers.csv"));
           BufferedReader tripReader = new BufferedReader(new FileReader(dataPath + "trips.csv"));
           BufferedReader taxistopReader = new BufferedReader(new FileReader(dataPath + "taxi_stops.csv"));

           insertVeh(vehicleReader);
           insertPassenger(passengerReader);
           insertDriver(driverReader);
           insertTrip(tripReader);
           insertTaxistop(taxistopReader);
        }  
        catch (Exception e){
            System.out.println(e);
        }



    }
    void insertVeh(BufferedReader vehicleReader){
        String line = null;
        try{
            PreparedStatement vehicle = con.prepareStatement("insert into vehicle values(?,?,?)");
            while((line = vehicleReader.readLine())!= null){
                String[] data = line.split(",");
                String id = data[0];
                String model = data[1];
                int seats = Integer.parseInt(data[2]);

                vehicle.setString(1, id);
                vehicle.setString(2, model);
                vehicle.setInt(3, seats);
                vehicle.execute();
            }

        }
        catch (Exception e){
            System.out.println("error in insert veh");
            System.out.println(e);
        }

    }
    void insertPassenger(BufferedReader passengerReader){
        String line = null;
        try{
            PreparedStatement passenger = con.prepareStatement("insert into passenger values(?,?)");
            while((line = passengerReader.readLine())!= null){
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];

                passenger.setInt(1, id);
                passenger.setString(2, name);
                passenger.execute();
            }
        }
        catch (Exception e){
            System.out.println("error in insert passenger");
            System.out.println(e);
        }

    }
    void insertDriver(BufferedReader driverReader){
        String line = null;
        try{
            PreparedStatement driver= con.prepareStatement("insert into driver values(?,?,?,?)");
            while((line = driverReader.readLine())!= null){
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String vid = data[2];
                int years = Integer.parseInt(data[3]);

                driver.setInt(1, id);
                driver.setString(2, name);
                driver.setString(3, vid);
                driver.setInt(4, years);
                driver.execute();
            }
        }
        catch (Exception e){
            System.out.println("error in insert driver");
            System.out.println(e);
        }

    }
    void insertTrip(BufferedReader tripReader){
        //CREATE TABLE trip
        //(id int, driver_id int, passenger_id int, start_location varchar(20), destination varchar(20),
        // start_time datetime, end_time datetime, fee int, Primary Key(id))
        String line = null;
        try{
            PreparedStatement trip = con.prepareStatement("insert into trip values(?,?,?,?,?,?,?,?)");
            while((line = tripReader.readLine())!= null){
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                int driverId = Integer.parseInt(data[1]);
                int passengerId = Integer.parseInt(data[2]);
                String startLoc = data[5];
                String endLoc = data[6];
                String startTime = data[3];
                String endTime = data[4];
                int fee  = Integer.parseInt(data[7]);

                trip.setInt(1, id);
                trip.setInt(2, driverId);
                trip.setInt(3, passengerId);
                trip.setString(4, startLoc);
                trip.setString(5, endLoc);
                trip.setTimestamp(6, Timestamp.valueOf(startTime));
                trip.setTimestamp(7, Timestamp.valueOf(endTime));
                trip.setInt(8, fee);
                trip.execute();
            }
            
        }
        catch (Exception e){
            System.out.println("error in insert trip");
            System.out.println(e);
        }

    }
    void insertTaxistop(BufferedReader taxistopReader){
        String line = null;
        try{
            PreparedStatement taxiStop = con.prepareStatement("insert into taxi_stop values(?,?,?)");
            while((line = taxistopReader.readLine())!= null){
                String[] data = line.split(",");
                String location = data[0];
                int xloc = Integer.parseInt(data[1]);
                int yloc = Integer.parseInt(data[2]);

                taxiStop.setString(1, location);
                taxiStop.setInt(2, xloc);
                taxiStop.setInt(3, yloc);
                taxiStop.execute();
            }
            
        }
        catch (Exception e){
            System.out.println("error in insert taxistop");
            System.out.println(e);
        }

    }

    void insertData(){
        //should I assume that admin MUST insert data?
        String filePath = null;
        String actualPath = "test_data";
        Scanner s = new Scanner(System.in);
        while(! actualPath.equals(filePath)){
            System.out.println("Please enter the path");
            filePath = s.nextLine();
        }
        loadData();
        System.out.println("Data is inserted");

        
    }

    void printResult(String tbName){
        try{
            ResultSet r = null;
            int count;
            if(tbName == "Driver"){r = stmt.executeQuery(rowDriver);}
            else if(tbName == "Vehicle"){r = stmt.executeQuery(rowVeh);}
            else if(tbName == "Passenger"){r = stmt.executeQuery(rowPassenger);}
            else if(tbName == "Request"){r = stmt.executeQuery(rowRequest);}
            else if(tbName == "Trip"){r = stmt.executeQuery(rowTrip);}
            else if(tbName == "Taxi_stop"){r = stmt.executeQuery(rowTaxistop);}
            r.next();
            count = r.getInt("rc");
            r.close();
            System.out.println(tbName + ": " + count);

        }
        catch (Exception e){
            System.out.println("error in printResult");
            System.out.println(e);
        }

    }
    void showData(){
        System.out.println("Data is showned");
        printResult("Vehicle");
        printResult("Passenger");
        printResult("Driver");
        printResult("Trip");
        printResult("Request");
        printResult("Taxi_stop");
    }


}