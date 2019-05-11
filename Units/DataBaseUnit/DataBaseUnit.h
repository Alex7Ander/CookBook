//---------------------------------------------------------------------------
#ifndef DataBaseUnitH
#define DataBaseUnitH
#include <Forms.hpp>
#include <Classes.hpp>
#include <ADODB.hpp>
#include <DB.hpp>
#include "PVA_serv.h"

class DATA_BASE
{
private:
        String connectionString;
        String dbFilePath;
        TADOConnection *adoConnect;
        TADOTable *adoTable;
        TDataSource *dataSource;
        TADOQuery *adoQuery;
        String tableName;

public:
       DATA_BASE(TForm *anyForm, String dbFileName, String tableNameInDB);
       ~DATA_BASE();
       String getTableName(){return tableName;}
       int ChangeTable(String tableNameInDB);
       void sendSqlQuery(String sqlString); //отправка запроса без получения result set; подходит для Update, Insert, etc.
       void sendSqlQuery(String sqlString, String fieldName, int *result);  //отправка запроса с получением result set типа int - подходит для запроса Select count(*)
       void sendSqlQuery(String sqlString, String fieldName, String *arrayResults);  //отправка запроса с получением result set в виде массива String - подходит для запроса Select
       void sendSqlQuery(String sqlString, String *fieldName, int countOfFields, String **arrayResults);  //отправка запроса по нескольким полям
};
//---------------------------------------------------------------------------
#endif
