using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CookBook
{
    class Ingridient
    {
        public Ingridient()
        {
            this.size = "";
            this.count = "";
            this.ingr = "";
        }

        public string ingr;
        public string count;
        public string size;

        public string Ingr { get { return ingr; } set{ ingr = value; } }
        public string Count { get { return count; } set { count = value; } }
        public string Size { get { return size; } set { size = value; } }
    }

    class Recipe
    {
    //private:       
        public string Name; //Название рецепта
        public string typeGroup;  //Название группы типа рецепта
        public int countOfIngr;
    //public:
        public string text;
        public Ingridient[] Ingrs;
        //Конструктор
        public Recipe(string name, string type){
          Name = name;
          this.typeGroup = type;
        }

        public void getRecipe()
        {          
            string filePath = System.IO.Directory.GetCurrentDirectory()+"\\data\\data.mdb";
            AccessConnector connector = new AccessConnector(filePath);
            connector.connect();
            string sqlString = "SELECT TEXT FROM [Recieps] WHERE ReciepName='" + this.Name + "' AND Type='" + this.typeGroup + "'";
            object oText = "";
            connector.getScalarInfo(sqlString, ref oText);
            this.text = oText.ToString();
            sqlString = "SELECT IngrTableName FROM [Recieps] WHERE ReciepName='"+this.Name+"' AND Type='"+this.typeGroup+"'";            
            object info = "";
            connector.getScalarInfo(sqlString, ref info);
            string tableName = info.ToString();
            this.countOfIngr = connector.selectCount(tableName, "Ingr", null);
            Ingrs = new Ingridient[this.countOfIngr];
            for (int i=0; i< this.countOfIngr; i++)
            {
                Ingridient tempIngr = new Ingridient();
                Ingrs[i] = tempIngr;
            }
            string[,] tI = new string[this.countOfIngr, 3];
            string[] fields = {"Ingr", "ValueOfIngr", "Unit"};
            connector.getTable(tableName, tI, fields, null);
            for (int i = 0; i < this.countOfIngr; i++){
                this.Ingrs[i].Ingr = tI[i, 0];
                this.Ingrs[i].Count = tI[i,1];
                this.Ingrs[i].Size = tI[i, 2];
            }
            connector.disconnect();
            return;
        }

        public int deleteRecipe()
        {
            int resOfQuerry = 0;
            string filePath = System.IO.Directory.GetCurrentDirectory() + "\\data\\data.mdb";
            AccessConnector connector = new AccessConnector(filePath);
            string sqlString = "BEGIN TRANSACTION";
            string finalSqlString = "COMMIT";
            connector.connect();
            connector.sendQuerry(sqlString);
            sqlString = "SELECT IngrTableName FROM [Recieps] WHERE ReciepName='" + this.Name + "' AND Type='" + this.typeGroup + "'";
            object o = "";
            resOfQuerry = connector.getScalarInfo(sqlString, ref o);
            if (resOfQuerry == 0)
            {
                string tableName = o.ToString();
                sqlString = "DROP TABLE [" + tableName + "]";
                resOfQuerry = connector.sendQuerry(sqlString);
                if (resOfQuerry == 0)
                {
                    sqlString = "DELETE FROM [Recieps] WHERE ReciepName='" + this.Name + "' AND Type='" + this.typeGroup + "'";
                    resOfQuerry = connector.sendQuerry(sqlString);
                    if (resOfQuerry == 0)
                    {
                        this.typeGroup = null;
                        this.Name = null;
                    }
                    else
                    {
                        finalSqlString = "ROLLBACK";
                    }
                }
                else
                {
                    finalSqlString = "ROLLBACK";
                }
            }
            else
            {
                finalSqlString = "ROLLBACK";
            }
            connector.sendQuerry(sqlString);
            return resOfQuerry;
        }

        public void saveRecipe()
        {

            return;
        }

        public int editText(string newText)
        {
            int resOfQuerry = 0;
            string filePath = System.IO.Directory.GetCurrentDirectory() + "\\data\\data.mdb";
            AccessConnector connector = new AccessConnector(filePath);
            string sqlString = "UPDATE [Recieps] SET Text='"+newText+ "' WHERE ReciepName='"+this.Name+"' AND Type='"+this.typeGroup+"'";
            resOfQuerry = connector.sendQuerry(sqlString);
            return resOfQuerry;
        }

        public int editName(String newName)
        {
            int resOfQuerry = 0;
            string filePath = System.IO.Directory.GetCurrentDirectory() + "\\data\\data.mdb";
            AccessConnector connector = new AccessConnector(filePath);
            string sqlString = "UPDATE [Recipes] SET ReciepName='"+newName+"' WHERE  ReciepName='"+this.Name+"' AND Type='" + this.typeGroup + "'";
            resOfQuerry = connector.sendQuerry(sqlString);
            if (resOfQuerry == 0)
            {
                this.Name = newName;
            }
            return resOfQuerry;
        }

        public void editType(String type)
        {

            return;
        }
    }
}
