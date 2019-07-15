package RecipePack;

import DataBase.dataBase;

public class Recipe {
	private String name;
	private String type;
	private String text;
	private int countOfIngredients;
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int getCountOfIngredients() {
		return this.countOfIngredients;
	}

	public int save() {
		String sqlCmd;
		try {
			sqlCmd = "BEGIN TRANSACTION";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd);
			int count = 0;
			sqlCmd="CREATE TABLE ";
			sqlCmd="SELECT COUNT(*) FROM recipes WHERE nameOfRecipe='"+this.name+"' AND typeOfRecipe='"+this.type+"'";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd, count);
			++count;
			String IngrTableName = this.name+"_"+this.type+"_"+Integer.toString(count);
			sqlCmd = "INSERT INTO recipes (nameOfRecipe, typeOfRecipe, textOfrecipe, IngrTable) VALUES ('"+ this.name +"','"+ this.type +"','"+ this.text +"','"+ IngrTableName +"')";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd);
			sqlCmd="CREATE TABLE "+ IngrTableName +" (id INT auto_increment primary key, Ingr VARCHAR(30), Volume VARCHAR(10), MeasUnit VARCHAR(10))";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd);
			sqlCmd = "COMMIT";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd);
			return -1;
		}
		catch(Exception exp) {
			sqlCmd = "ROLLBACK";
			DataBase.dataBase.getInctanceDB().sqlQuery(sqlCmd);
			return 0;
		}
	}
}
