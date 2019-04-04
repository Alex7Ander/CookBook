using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.OleDb;

namespace CookBook
{
    class AccessConnector
    {
        private OleDbConnection odConnection;
        private OleDbCommand aCommand;
        private string connectionString;
        private string filePath;


        public AccessConnector(string path)
        {
            this.filePath = path;
            this.connectionString = "Provider=Microsoft.Jet.OLEDB.4.0; Data Source="+path;
        }

        public int connect()
        {
            this.odConnection = new OleDbConnection(this.connectionString);           
            return 0;
        }
        public int disconnect()
        {
            this.odConnection.Close();
            return 0;
        }

        public int sendQuerry(string querry)
        {
            int res;
            try
            {
                this.odConnection.Open();
                aCommand = new OleDbCommand(querry, odConnection);
                aCommand.ExecuteNonQuery();
                this.odConnection.Close();
                res = 0;
            }
            catch (OleDbException exp)
            {
                res = -1;
            }
            return res;
        }

        public int getScalarInfo(string querry, ref object info)
        {
            try
            {
                this.odConnection.Open();
                aCommand = new OleDbCommand(querry, odConnection);
                info = aCommand.ExecuteScalar();
                this.odConnection.Close();
                return 0;
            }
            catch (OleDbException exp)
            {
                return -1;
            }
            
        }

        public int getStringInfo(string querry, string[] info)
        {
            int res;
            try
            {
                this.odConnection.Open();
                aCommand = new OleDbCommand(querry, odConnection);
                OleDbDataReader Reader = aCommand.ExecuteReader();
                int i = 0;
                while (Reader.Read())
                {
                    string s = Reader.GetString(0);
                    info[i] = s; 
                    i++;
                }
                Reader.Close();
                this.odConnection.Close();
                res = 0;
            }
            catch (OleDbException exp)
            {
                res = -1;
            }
            return res;
        }

        public int getTable(string tableName, string[,] table, string[] fields, string filters)
        {
            try
            {
                int countOfFields = 0;
                countOfFields = fields.Length;
                String querry = "SELECT ";
                foreach (string field in fields) querry += field + ", ";
                querry = querry.Remove(querry.Length - 2, 2);
                querry +=" FROM [" + tableName + "] ";
                if (filters != null) querry += filters;
                aCommand = new OleDbCommand(querry, odConnection);
                this.odConnection.Open();               
                OleDbDataReader Reader = aCommand.ExecuteReader();
                int counter = 0;
                while (Reader.Read())
                {
                    for (int i = 0; i < countOfFields; i++)
                        table[counter, i] = Reader.GetString(i);
                    counter++;
                }
                Reader.Close();
                this.odConnection.Close();
                return 0;
            }
            catch (OleDbException exp)
            {
                return -1;
            }
        }

        public int selectCount(string tableName, string fieldName, string filters)
        {
            int count = 0;
            String querry = "SELECT COUNT("+fieldName+") AS resInt FROM ["+tableName+"]";
            if (filters != null) querry += filters;
            this.odConnection.Open();
            aCommand = new OleDbCommand(querry, odConnection);
            Object r = aCommand.ExecuteScalar();
            count = (int)r;
            this.odConnection.Close();
            return count;
        }
    }
}
