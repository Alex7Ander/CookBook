package DataBase;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	private Connection dbConnection;
	private Statement dbStatement;
	private String url = "jdbc:mysql://localhost/my_airsoft_team?serverTimezone=Europe/Moscow&useSSL=false";
	private String username = "root";
	private String password = "89151032839ôûâ";
	
	private static DataBase instanceDB;
	private DataBase() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			this.dbConnection = DriverManager.getConnection(url, username, password);
			this.dbStatement = this.dbConnection.createStatement(); 
		}
		catch(SQLException exp) {
			
		}
	}
	
	public void sqlQuery(String queryText) throws SQLException {
		this.dbStatement.executeUpdate(queryText);
	}
	
	public int sqlQuery(String queryText, String[] data) throws SQLException {
		ResultSet rSet = this.dbStatement.executeQuery(queryText);
		int i = 0;
		while(rSet.next()) {
			data[i] = rSet.getString(0);
			i++;
		}
		return i;
	}
	
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
	
	public DataBase getInctanceDB() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		if (instanceDB == null) {
			DataBase.instanceDB = new DataBase();
		}
		return DataBase.instanceDB;
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