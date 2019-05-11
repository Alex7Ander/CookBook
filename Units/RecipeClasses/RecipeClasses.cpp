//---------------------------------------------------------------------------
#pragma hdrstop
#include "RecipeClasses.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
Ingredient::Ingredient()
{
  this->name = "unknown";
  this->type = "unknown";
  this->description = "unknown";
}
//---------------------------------------------------------------------------
Ingredient::Ingredient(String Name, String Type, String Description, double Proteins, double Fats, double Carbohydrates)
{
  this->name = Name;
  this->type = Type;
  this->description = Description;
  this->proteins = Proteins;
  this->fats = Fats;
  this->carbohydrates = Carbohydrates;
  setCalories();
}
//---------------------------------------------------------------------------
Ingredient::Ingredient(String Name, DATA_BASE *dataBase)
{
   this->name = Name;
   String values[5] = {"prot", "fats", "carb", "type", "description"};
   dataBase->ChangeTable("Ingredients");
   for (int i=0; i<5; i++){
     String sqlCmd = "SELECT "+values[i]+" FROM [Ingredients] WHERE name='"+Name+"'";
     String tVal;
     try{
       dataBase->sendSqlQuery(sqlCmd, values[i], &tVal);
       switch(i){
         case (0): this->proteins = StrToFloat(tVal); break;
         case (1): this->fats = StrToFloat(tVal); break;
         case (2): this->carbohydrates = StrToFloat(tVal); break;
         case (3): this->type = tVal; break;
         case (4): this->description = tVal; break;
       }
       setCalories();
     }catch(...){}
   }
}
//---------------------------------------------------------------------------
void Ingredient::setCalories()
{
  this->calories = this->proteins*4 + this->fats*9 + this->carbohydrates*4;
}
//---------------------------------------------------------------------------
int Ingredient::SaveNewIngredient(DATA_BASE *dataBase)
{
  dataBase->ChangeTable("Ingredients");
  int count;
  String sqlCmd = "SELECT COUNT(name) AS resultSqlInt FROM [Ingredients] WHERE name='"+this->name+"' AND type='"+this->type+"'";
  try{
    dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &count);
    if (count){
      return 1;
    }
    else{
      sqlCmd = "INSERT INTO [Ingredients] (name, prot, fats, carb, type, description) values ('"+this->name+"','"+FloatToStr(this->proteins)+"','"+FloatToStr(this->fats)+"','"+FloatToStr(this->carbohydrates)+"','"+this->type+"','"+this->description+"')";
      try{
        dataBase->sendSqlQuery(sqlCmd);
        return 0;
      }
      catch(...){return 3;}
    }
  }catch(...){return 2;}
}
//---------------------------------------------------------------------------
int Ingredient::DeleteIngredient(DATA_BASE *dataBase)
{
  dataBase->ChangeTable("Ingredients");
  String sqlCmd = "SELECT COUNT(name) AS resultSqlInt FROM [Ingredients] WHERE name='"+this->name+"' AND type='"+this->type+"'";
  try{
    int count;
    dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &count);
    if (count){
        sqlCmd = "DELETE FROM [Ingredients] WHERE name='"+this->name+"' AND type='"+this->type+"'";
        dataBase->sendSqlQuery(sqlCmd);
        return 0;
    }
    else{
     return 1;
    }
  }
  catch(...){return 2;}
}
//---------------------------------------------------------------------------
int Ingredient::EditIngredient()
{
//
return 0;
}
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
Recipe::Recipe (String Name, String Type, String Text, int CountOfIngredients)
{
  this->name = Name;
  this->type = Type;
  this->text = Text;
  this->countOfIngredients = CountOfIngredients;
  this->ingrTable = "Ingredients_of_"+this->name+"_"+this->type;
}
//---------------------------------------------------------------------------
Recipe::Recipe (String Name, DATA_BASE *dataBase)
{
   this->name = Name;
   String sqlCmd;
   String values[3] = {"type", "text", "ingrTable"};
   dataBase->ChangeTable("Recipes");
   for (int i=0; i<3; i++){
     sqlCmd = "SELECT "+values[i]+" FROM [Recipes] WHERE name='"+Name+"'";
     String tVal;
     try{
       dataBase->sendSqlQuery(sqlCmd, values[i], &tVal);
       switch(i){
         case (0): this->type = tVal; break;
         case (1): this->text = tVal; break;
         case (2): this->ingrTable = tVal; break;
       }
     }catch(...){}
   }
   dataBase->ChangeTable(this->ingrTable);
   sqlCmd = "SELECT COUNT(ingrId) AS resultSqlInt FROM ["+this->ingrTable+"]";
   dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &this->countOfIngredients);
   String *ingrID = new String[this->countOfIngredients];
   sqlCmd = "SELECT ingrId FROM ["+this->ingrTable+"]";
   dataBase->sendSqlQuery(sqlCmd, "ingrId", ingrID);
   for (int i=0; i<this->countOfIngredients; i++){
     String ingrName;
     sqlCmd = "SELECT name FROM [Ingredients] WHERE ���="+ingrID[i]+"";
     dataBase->sendSqlQuery(sqlCmd, "name", &ingrName);
     ingredients.push_back(new Ingredient(ingrName, dataBase));
   }
}
//---------------------------------------------------------------------------
int Recipe::SaveRecipe(DATA_BASE *dataBase)
{
  String sqlCmd;
  try{
    dataBase->ChangeTable("Recieps");
    sqlCmd = "BEGIN TRANSACTION";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "INSERT INTO [Recieps] (name, type, text, ingrTable) values ('"+this->name+"','"+this->type+"','"+this->text+"','"+this->ingrTable+"')";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "CREATE TABLE ["+this->ingrTable+"] (��� COUNTER, ingrId TEXT, volume TEXT, PRIMARY KEY (���))";
    dataBase->sendSqlQuery(sqlCmd);
    for (int i=0; i<this->countOfIngredients; i++){
      sqlCmd = "INSERT INTO ["+this->ingrTable+"] (ingrId, volume) values ('(SELECT ��� FROM [Ingredients] WHERE name='"+this->ingredients[i]->getName()+"' AND type='"+this->ingredients[i]->getType()+"')','"+FloatToStr(this->volumeOfIngr[i])+"')";
      dataBase->sendSqlQuery(sqlCmd);
    }
    sqlCmd = "COMMIT";
    dataBase->sendSqlQuery(sqlCmd);
    return 0;
  }
  catch(...){
    sqlCmd = "ROLLBACK";
    dataBase->sendSqlQuery(sqlCmd);
    return 1;
  }
}
//---------------------------------------------------------------------------
int Recipe::DeleteRecipe(DATA_BASE *dataBase)
{
  String sqlCmd;
  try{
    dataBase->ChangeTable("Recieps");
    sqlCmd = "BEGIN TRANSACTION";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "DELETE FROM [Recieps] WHERE name='"+this->name+"' AND type='"+this->type+"'";
    dataBase->sendSqlQuery(sqlCmd);
    dataBase->ChangeTable(this->ingrTable);
    sqlCmd = "DROP TABLE ["+this->ingrTable+"]";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "COMMIT";
    dataBase->sendSqlQuery(sqlCmd);
    return 0;
  }
  catch(...){
    sqlCmd = "ROLLBACK";
    dataBase->sendSqlQuery(sqlCmd);
    return 1;
  }
}
//---------------------------------------------------------------------------
int Recipe::EditRecipe(DATA_BASE *dataBase)
{
//
return 0;
}
//---------------------------------------------------------------------------