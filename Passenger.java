import java.sql.*;
import java.util.Scanner;
import java.util.regex.*;

import javax.naming.spi.DirStateFactory.Result;

import java.util.ArrayList;

class Passenger{
    Connection con = null;
    Statement stmt = null;
    public Passenger(Connection c, Scanner sc){
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
                rideRequest(s);
            }
            else if (options == 2){
                chkRecords(s);
            }
            else if (options == 3){
                break;
            }

        }

    }
    int getOptions(Scanner s){
        int options = 0;

		passengerPageMessage();
        //Scanner s = new Scanner(System.in);
        if(s.hasNextInt()){
            options = s.nextInt(); 
        }
		while(options < 1 || options > 3){
			errorMessage();
            options = s.nextInt(); 
		}
		return options;
    }
    void printTable(ResultSet r, String headerLine){
        try{
            ResultSetMetaData rmd = r.getMetaData();
            int colcount = rmd.getColumnCount();
            System.out.println(headerLine);
            while(r.next()){
                for(int i = 1; i <= colcount; i++)
                    System.out.print(r.getString(i) + " ");
                System.out.println();          
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    ArrayList<String> getAllTaxiStops(){
        ArrayList<String> allStopsArray = new ArrayList<String>();
        String allStops = "SELECT name from taxi_stop";
        ResultSet r = null;

        try{
            r = stmt.executeQuery(allStops);
            while(r.next()){
                String s = r.getString("name");
                allStopsArray.add(s);
            }
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return allStopsArray;
    }
    ArrayList<String> getAllCarModels(){
        ArrayList<String> result = new ArrayList<String>();
        String query = "SELECT DISTINCT model FROM vehicle";
        ResultSet r = null;
        try{    
            r = stmt.executeQuery(query);
            while(r.next()){
                String s = r.getString("model");
                result.add(s);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    boolean isinArrayList(ArrayList<String> al, String s){
        for(String i: al){
            if(i.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    String getUniqueInArrayList(ArrayList<String> al, String s){
        //assume s in array list
        for(String i: al){
            if(i.equalsIgnoreCase(s)){
                return i;
            }
        }
        return "";
    }
    ArrayList<String> getAllInArrayList(ArrayList<String> al, String s){
        //deal with partial match 
        //s = toyota, return Toyota Prius, Toyota corolla...
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(s+"*",Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        boolean matchFound;
        for(String i: al){
            matcher = pattern.matcher(i);
            matchFound = matcher.find();
            if(matchFound) {
                result.add(i);
            }
        }
        return result;
    }


    void rideRequest(Scanner s){

        boolean isModelConstriant;
        int pid;
        int noOfPassenger;
        String startLoc;
        String endLoc;
        String model;
        int drivYears;
        int count = 0;

        String noModelConstriantQuery = "SELECT * FROM  driver D, vehicle V WHERE V.seats >= ? AND D.driving_years >= ? AND V.id = D.vehicle_id";
        String modelConstriantQuery = "SELECT * FROM  driver D, vehicle V WHERE V.seats >= ? AND D.driving_years >= ? AND V.model = ? AND V.id = D.vehicle_id";
        

        ResultSet rs;
        try{
            ArrayList<String> allTaxiStops = getAllTaxiStops();
            ArrayList<String> allCarModels = getAllCarModels();
            ArrayList<String> allCarCanChoice = null;

            PreparedStatement noModelConstriant = con.prepareStatement(noModelConstriantQuery);
            PreparedStatement modelConstriant = con.prepareStatement(modelConstriantQuery);

            
            System.out.println("Please enter your ID");
            pid = s.nextInt();
            System.out.println("Please enter number of passenger");
            noOfPassenger = s.nextInt();
            while(noOfPassenger > 7){
                System.out.println("ERROR: invalid value of passenger");
                noOfPassenger = s.nextInt();
            }
            System.out.println("Please enter start location");
            startLoc = s.nextLine();
            startLoc = s.nextLine();
            while(!isinArrayList(allTaxiStops, startLoc)){
                System.out.println("ERROR: Not a valid stop");
                startLoc = s.nextLine();
            }
            System.out.println("Please enter destination");
            endLoc = s.nextLine();
            while(!isinArrayList(allTaxiStops, endLoc) || startLoc.equalsIgnoreCase(endLoc)){
                if(!isinArrayList(allTaxiStops, endLoc)){
                    System.out.println("ERROR: Not a valid stop");
                }
                else{
                    System.out.println("same stop");
                }
                endLoc = s.nextLine();
            }
            System.out.println("Please enter model(enter to skip)");
            model = s.nextLine();
            if(model.isEmpty()){
                System.out.println("nothing input");
                isModelConstriant = false;
            }
            else{
                allCarCanChoice = getAllInArrayList(allCarModels, model);
                isModelConstriant = true;
                if(allCarCanChoice.isEmpty()){
                    System.out.println("ERROR: no car satisify");
                    isModelConstriant = false;
                }
            }
            System.out.println("Please enter min driving year(enter to skip)");
            try{
                String tmp = s.nextLine();
                drivYears = Integer.parseInt(tmp);
            }
            catch(Exception e){
                //receive no input -> can't parse int
                drivYears = -1;
            }
            if(isModelConstriant){
                for(String i : allCarCanChoice){
                    modelConstriant.setInt(1, noOfPassenger);
                    modelConstriant.setInt(2, drivYears);
                    modelConstriant.setString(3, i);
                    rs = modelConstriant.executeQuery();
                    while(rs.next()){
                        count++;
                    }
                }
            }
            else{
                noModelConstriant.setInt(1, noOfPassenger);
                noModelConstriant.setInt(2, drivYears);
                rs = noModelConstriant.executeQuery();
                while(rs.next()){
                    count++;
                }
            }
            insertIntoRequest(pid, startLoc, endLoc, model, noOfPassenger, drivYears);
            System.out.printf("Your order is placed, %d drivers are able to take your request.",count);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void checkCarModelConstriant(){

    }

    void chkRecords(Scanner s){
        int id;
        String startDate;
        String endDate;
        String destination;

        String driverCar = "SELECT D.id, D.name, V.id, V.model FROM driver D, vehicle V WHERE D.vehicle_id = V.id";
        String headerLine = "Trip_id, Driver name, Vehicle ID, Vehicle Model, Start, End, Fee, Start Location, Destination";

        System.out.println("Enter your ID");
        id = s.nextInt();
        System.out.println("Enter start date");
        startDate = s.nextLine();
        startDate = s.nextLine();
        System.out.println("Enter end date");
        endDate = s.nextLine();
        System.out.println("Enter destination");
        destination = s.nextLine();
        try{
            ResultSet r;
            ResultSetMetaData rmd;

            //tripid, driver name, vid, vmodel, start, end, fee, startloc, endloc
            //table: trip, driver, vehicle
            
            PreparedStatement p = con.prepareStatement("SELECT T.id, D.name, V.id, V.model, T.start_time, T.end_time, T.fee, T.start_location, T.destination "+
                                                    "FROM trip T, driver D, vehicle V "+
                                                    "WHERE T.passenger_id = ? AND T.destination = ? AND T.start_time > ? AND T.end_time < ? AND D.vehicle_id= V.id AND T.driver_id = D.id");
            p.setInt(1, id);
            p.setString(2, destination);
            p.setString(3, startDate);
            p.setString(4, endDate);
            r = p.executeQuery();
            printTable(r,headerLine);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    void passengerPageMessage(){
        System.out.println("Bad passenger, what are you going to do");
        System.out.println("1. Request a ride");
        System.out.println("2. Check trip record");
        System.out.println("3. Go back");
        System.out.println("Please enter 1-3");
    
    }
    void errorMessage(){
        System.out.println("Error, choose 1-3");
    }


    void insertIntoRequest(int pid, String startLoc, String endLoc, String model, int noOfPassenger, int drivYears){
        String insertIntoRequest = "INSERT INTO request values(?,?,?,?,?,?,0,?)";
        String rowRequest = "SELECT COUNT(*) AS rc FROM request";
        ResultSet r2 = null;
        try{
            PreparedStatement p1 = con.prepareStatement(insertIntoRequest);
            r2 = stmt.executeQuery(rowRequest);
            r2.next();
            int count = r2.getInt("rc") + 1;
            p1.setInt(1, count);
            p1.setInt(2, pid);
            p1.setString(3, startLoc);
            p1.setString(4, endLoc);
            p1.setString(5, model);
            p1.setInt(6, noOfPassenger);
            p1.setInt(7, drivYears);
            p1.execute();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}