using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CookBook
{
    class Book
    {
        public static List<string> ListOfTypes;
        public static List<string>[] ListOfRecipes;
        public static int countOfTypes = 0;

        public static void openBook()
        {
            ListOfTypes = new List<string>();
            string dbPath = System.IO.Directory.GetCurrentDirectory() + "\\data\\data.mdb";
            AccessConnector connector = new AccessConnector(dbPath);
            int countOfValues = 0;
            object o = "";
            connector.connect();
            string sqlString = "SELECT COUNT([Type]) FROM [Recieps]";
            connector.getScalarInfo(sqlString, ref o);
            countOfValues = (int)o;
            sqlString = "SELECT [Type] FROM [Recieps]";
            string[] tempArray = new string[countOfValues];
            connector.getStringInfo(sqlString, tempArray);
            //Поиск неповторяющихся значений
            ListOfTypes.Add(tempArray[0]);
            foreach (string str in tempArray)
            {
                bool exist = false;
                for (int i=0; i< ListOfTypes.Count; i++)
                {
                    if (str == ListOfTypes[i])
                    {
                        exist = true;
                        break;
                    }
                }
                if (exist == false)
                {
                    ListOfTypes.Add(str);
                    countOfTypes++;
                }
            }

            ListOfRecipes = new List<string>[countOfTypes];
            for (int i=0; i<countOfTypes; i++)
            {
                ListOfRecipes[i] = new List<string>();
                sqlString = "SELECT COUNT(Type) FROM [Recieps] WHERE Type='"+ListOfTypes[i]+"'";
                object ob = "";
                connector.getScalarInfo(sqlString, ref ob);
                int countOfRec = (int)ob;

                sqlString = "SELECT ReciepName FROM [Recieps] WHERE Type='" + ListOfTypes[i] + "'";
                string[] tempRecArray = new string[countOfRec];
                connector.getStringInfo(sqlString, tempRecArray);
                foreach (string str in tempRecArray) ListOfRecipes[i].Add(str);
            }
            
        }
    }
}
