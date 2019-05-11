#pragma hdrstop
#include "DataBaseUnit.h"
#pragma package(smart_init)
//---------------------------------------------------------------------------
DATA_BASE::DATA_BASE(TForm *anyForm, String dbFileName, String tableNameInDB)
{
  adoConnect = new TADOConnection(anyForm);
  adoTable = new TADOTable(anyForm);
  dataSource = new TDataSource(anyForm);
  adoQuery = new TADOQuery(anyForm);
  adoConnect->Connected = false;
  String cS = "Provider=Microsoft.Jet.OLEDB.4.0;User ID=Admin;Data Source="+GetCurrentDir()+
  "\\files\\"+dbFileName+";Mode=Share Deny None;Persist Security Info=False;Jet OLEDB:System database="";Jet OLEDB:Registry Path="";Jet OLEDB:Database Password="";Jet OLEDB:Engine Type=5;Jet OLEDB:Database Locking Mode=1;Jet OLEDB:Global Partial Bulk Ops=2;Jet OLEDB:Global Bulk Transactions=1;Jet OLEDB:New Database Password="";Jet OLEDB:Create System Database=False;Jet OLEDB:Encrypt Database=False;Jet OLEDB:Don't Copy Locale on Compact=False;Jet OLEDB:Compact Without Replica Repair=False;Jet OLEDB:SFP=False";
  adoConnect->ConnectionString=cS.c_str();
  this->connectionString = cS;
  adoConnect->LoginPrompt = false;
  adoTable->TableName = tableNameInDB;
  this->tableName = tableNameInDB;
  adoTable->Connection = this->adoConnect;
  dataSource->DataSet = this->adoTable;
  adoQuery->Connection = this->adoConnect;
  adoTable->Active = true;
  adoConnect->Connected = true;
}
//---------------------------------------------------------------------------
DATA_BASE::~DATA_BASE()
{
  this->adoConnect->Connected = false;
}
//---------------------------------------------------------------------------
int __fastcall DATA_BASE::ChangeTable(String tableNameInDB)
{
  try
  {
    adoTable->Active = false;
    adoTable->TableName = tableNameInDB;
    adoTable->Active = true;
    this->tableName = tableNameInDB; 
    return 0;
  }
  catch (Exception &exp)
  {
    return 1;
  }
}
//---------------------------------------------------------------------------
void DATA_BASE::sendSqlQuery(String sqlString)
{
  adoQuery->Close();
  adoQuery->SQL->Clear();
  adoQuery->SQL->Add(sqlString);
  adoQuery->ExecSQL();
  adoQuery->Close();
}
//---------------------------------------------------------------------------
void DATA_BASE::sendSqlQuery(String sqlString, String fieldName, int *result)
{
  adoQuery->Close();
  adoQuery->SQL->Clear();
  adoQuery->SQL->Add(sqlString);
    adoQuery->Open();
    *result=adoQuery->FieldByName(fieldName)->AsInteger;
  adoQuery->Close();
}
//---------------------------------------------------------------------------
void DATA_BASE::sendSqlQuery(String sqlString, String fieldName, String *arrayResults)
{
  String result;
  adoQuery->Close();
  adoQuery->SQL->Clear();
  adoQuery->SQL->Add(sqlString);
    adoQuery->Open();
    for (int i=0; i<adoQuery->RecordCount; i++)
      {
         arrayResults[i]=adoQuery->FieldByName(fieldName)->AsString;
         adoQuery->Next();
      }
  adoQuery->Close();
}
//---------------------------------------------------------------------------
void DATA_BASE::sendSqlQuery(String sqlString, String *fieldName, int countOfFields, String **arrayResults)
{
  String result;
  adoQuery->Close();
  adoQuery->SQL->Clear();
  adoQuery->SQL->Add(sqlString);
  adoQuery->Open();
    for (int i=0; i<adoQuery->RecordCount; i++)
      {
         for (int j=0; j<countOfFields; j++)
         {
             arrayResults[j][i]=adoQuery->FieldByName(fieldName[j])->AsString;
         }
         adoQuery->Next();
      }
  adoQuery->Close();
}
