import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

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
               findTrips(s);
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
        String headerLine = "id, driver name, passenger name, start location, destination, duration";
        int minDist;
        int maxDist;
        System.out.println("please enter the minimum distance");
        String query = "SELECT T.id, D.name, P.name, T1.name, T1.location_x, T1.location_y, T2.name, T2.location_x, T2.location_y, T.start_time, T.end_time "+
                        "FROM trip T, driver D, passenger P, taxi_stop T1, taxi_stop T2 " +
                        "WHERE T1.name = T.start_location AND T2.name = T.destination AND D.id=T.driver_id AND P.id=T.passenger_id "+
                        "ORDER BY T.id";
        
        ResultSet rs = null;
        minDist = s.nextInt();
        while(minDist < 0){
            System.out.println("ERROR: Please enter valid distance");
            minDist = s.nextInt();
        }
        System.out.println("ERROR: please enter the maximum distance");
        maxDist = s.nextInt();
        while(maxDist < minDist){
            System.out.println("ERROR: Please enter valid distance");
            maxDist = s.nextInt();
        }   
        //grap all necessary info
        try{
            PreparedStatement ps = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            //printTable(rs,"T.id, D.name, P.name, T1.name, T1.location_x, T1.location_y, T2.name, T2.location_x, T2.location_y, T.start_time, T.end_time");
            getNecessaryData(rs,minDist,maxDist,headerLine);
            
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //cal distance
        //if satisify
    } 
    int getManhattanDist(int x1, int y1, int x2, int y2){
        int dist;
        dist = Math.abs(x1-x2) + Math.abs(y1-y2);
        return dist;
    }

    String getDuration(Timestamp tStart, Timestamp tEnd){
        int duration;
        long tmp;
        tmp = (tEnd.getTime() - tStart.getTime()) / 1000 / 60;
        duration = (int) tmp;
        return Integer.toString(duration);
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

    void getNecessaryData(ResultSet r,int minDist, int maxDist, String headerLine){
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int manhattonDist = 0;
        Timestamp tStart = null;
        Timestamp tEnd = null;
        ArrayList<String[]> s = new ArrayList<String[]>();

        //for arraylist
        try{
            ResultSetMetaData rmd = r.getMetaData();
            int colcount = rmd.getColumnCount();
            //try to store everything satisify the condition on the arraylist
            while(r.next()){
                String[] appendToArrayList = new String[6];
                x1 = r.getInt("T1.location_x");
                y1 = r.getInt("T1.location_y");
                x2 = r.getInt("T2.location_x");
                y2 = r.getInt("T2.location_y");
                tStart = r.getTimestamp("T.start_time");
                tEnd = r.getTimestamp("T.end_time");
                for(int i = 1; i <= colcount; i++){
                    //System.out.print(r.getString(i) + " "); print everything
                    if(i < 5){
                        appendToArrayList[i - 1] = r.getString(i);
                    }
                    else if(i == 7){
                        appendToArrayList[4] = r.getString(7);
                    }
                }
                appendToArrayList[5] = getDuration(tStart, tEnd);
                manhattonDist = getManhattanDist(x1, y1, x2, y2);
                if(manhattonDist >= minDist && manhattonDist <= maxDist){
                    s.add(appendToArrayList); 
                }
                      
            }
            //print on arrayList satisify the condition
            System.out.println(headerLine);
            for(String[] i : s){
                for(int j = 0 ; j < 6; j++){             
                    if(j < 5){
                        System.out.print(i[j] + ", ");
                    }
                    else{
                        System.out.print(i[j]);
                    }
                }
                System.out.println();
            }
        }
        catch(Exception e){

        }
    }

}