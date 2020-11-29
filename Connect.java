import java.sql.*;

class Connect{

    Connection con = null;
    Statement stmt = null;
    public Connect(){
        newConnection();
    }

    public void newConnection() {
		
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
    
    public Connection getConnection(){
        return con;
    }

    public Statement getStatement(){
        return stmt;
    }

}