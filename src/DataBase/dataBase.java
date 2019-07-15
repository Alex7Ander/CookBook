package DataBase;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class dataBase {
	private Connection dbConnection;
	private Statement dbStatement;
	private String url = "jdbc:mysql://localhost/my_airsoft_team?serverTimezone=Europe/Moscow&useSSL=false";
	private String username = "root";
	private String password = "89151032839ôûâ";
	
	private static dataBase instanceDB;
	private dataBase() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			this.dbConnection = DriverManager.getConnection(url, username, password);
			this.dbStatement = this.dbConnection.createStatement(); 
		}
		catch(SQLException exp) {
			
		}
	}
//*********************************	
	public int sqlQuery(String queryText) throws SQLException {
		return this.dbStatement.executeUpdate(queryText);
	}

	public int sqlQuery(String queryText, int result) throws SQLException {
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		if(rSet.next()) {
			result = rSet.getInt(0);
			return 0;
		}
		else {
			return -1;
		}
	}
//**********************************	
	public int sqlQuery(String queryText, String[] data) throws SQLException {
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		int i = 0;
		while(rSet.next()) {
			data[i] = rSet.getString(0);
			i++;
		}
		return i;
	}
	
	public int sqlQuery(String queryText, ArrayList<String> data) throws SQLException{
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		int i = 0;
		while(rSet.next()) {
			data.add(rSet.getString(0));
			i++;
		}
		return i;
	}
	
	public int sqlQuery(String queryText, int[] data) throws SQLException {
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		int i = 0;
		while(rSet.next()) {
			data[i] = rSet.getInt(0);
			i++;
		}
		return i;
	}
//************************************	
	public int sqlQuery(String queryText, String[][] data, int countOfFields) throws SQLException{
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		int i=0;
		while(rSet.next()) {
			for (int j=0; j<countOfFields; j++) {
				data[i][j] = rSet.getString(j);
			}
		}
		return i;
	}
//*******************************************	
	public static dataBase getInctanceDB() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		if (instanceDB == null) {
			dataBase.instanceDB = new dataBase();
		}
		return dataBase.instanceDB;
	}
	
	public int close(){
		try {
			dbConnection.close();
			return 0;
		}
		catch(SQLException exp) {
			return -1;
		}
	}
}