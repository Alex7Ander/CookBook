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
int Ingredient::EditIngredient(DATA_BASE *dataBase, int editedParam, String newValue)
{
  String sqlCmd;
  switch(editedParam) {
    case(0): sqlCmd= "UPDATE [Ingredients] SET name='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    case(1): sqlCmd= "UPDATE [Ingredients] SET type='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    case(2): sqlCmd= "UPDATE [Ingredients] SET prot='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    case(3): sqlCmd= "UPDATE [Ingredients] SET fats='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    case(4): sqlCmd= "UPDATE [Ingredients] SET carb='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    case(5): sqlCmd= "UPDATE [Ingredients] SET description='"+newValue+"' WHERE name='"+this->name+"' AND type='"+this->type+"'"; break;
    default: return 2;
  }
  try{
    dataBase->sendSqlQuery(sqlCmd);
    return 0;
  }catch(...){return 1;}
}
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
Recipe::Recipe (String Name, String Type, String Text, int CountOfIngredients, String *ingrNames, double *ingrVolumes, DATA_BASE *dataBase)
{
  this->name = Name;
  this->type = Type;

  if (Text.Length()==0)this->text = "А как готовить то и не известно";
  else this->text = Text;
  this->countOfIngredients = CountOfIngredients;
  this->ingrTable = this->name+"_"+this->type;
  dataBase->ChangeTable("Ingredients");
  for (int i=0; i<this->countOfIngredients; i++){
     this->volumeOfIngr.push_back(ingrVolumes[i]);
     ingredients.push_back(new Ingredient(ingrNames[i], dataBase));
  }
}
//---------------------------------------------------------------------------
Recipe::Recipe (String Name, DATA_BASE *dataBase)
{
   this->name = Name;
   String sqlCmd;
   String values[3] = {"type", "recipeText", "ingrTable"};
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
   String *tIngrVolume = new String[this->countOfIngredients];
   sqlCmd = "SELECT vol FROM ["+this->ingrTable+"]";
   dataBase->sendSqlQuery(sqlCmd, "vol", tIngrVolume);
   dataBase->ChangeTable("Ingredients");
   for (int i=0; i<this->countOfIngredients; i++){
     this->volumeOfIngr.push_back(StrToFloat(tIngrVolume[i]));
     String ingrName;
     sqlCmd = "SELECT name FROM [Ingredients] WHERE Код="+ingrID[i]+"";
     dataBase->sendSqlQuery(sqlCmd, "name", &ingrName);
     ingredients.push_back(new Ingredient(ingrName, dataBase));
   }
}
//---------------------------------------------------------------------------
int Recipe::SaveRecipe(DATA_BASE *dataBase)
{
  String sqlCmd;
  try{
    dataBase->ChangeTable("Recipes");
    sqlCmd = "BEGIN TRANSACTION";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "INSERT INTO Recipes (name, type, recipeText, ingrTable) values ('"+this->name+"','"+this->type+"','"+this->text+"','"+this->ingrTable+"')";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "CREATE TABLE ["+this->ingrTable+"] (Код COUNTER, ingrId TEXT, vol TEXT, PRIMARY KEY (Код))";
    dataBase->sendSqlQuery(sqlCmd);
    for (int i=0; i<this->countOfIngredients; i++){
      int code;
      sqlCmd = "SELECT Код FROM [Ingredients] WHERE name='"+this->ingredients[i]->getName()+"' AND type='"+this->ingredients[i]->getType()+"'";
      dataBase->sendSqlQuery(sqlCmd, "Код", &code);
      sqlCmd = "INSERT INTO ["+this->ingrTable+"] (ingrId, vol) values ('"+IntToStr(code)+"','"+FloatToStr(this->volumeOfIngr[i])+"')";
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
    dataBase->ChangeTable("Recipes");
    sqlCmd = "BEGIN TRANSACTION";
    dataBase->sendSqlQuery(sqlCmd);
    sqlCmd = "DELETE FROM [Recipes] WHERE name='"+this->name+"' AND type='"+this->type+"'";
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
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
typeList::typeList(DATA_BASE *dataBase, String Table)
{
  try{
    String sqlCmd = "SELECT COUNT(type) AS resultSqlInt FROM (SELECT DISTINCT type FROM ["+Table+"])";
    dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &this->itemsCount);
    if (this->itemsCount>0){
      items = new String[this->itemsCount];
      sqlCmd = "SELECT DISTINCT type FROM ["+Table+"]";
      dataBase->sendSqlQuery(sqlCmd, "type", items);
    }
  }catch(...){}
}
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
objectsList::objectsList(DATA_BASE *dataBase, String Table, String Type)
{
  String sqlCmd = "SELECT COUNT(name) AS resultSqlInt FROM ["+Table+"] WHERE type='"+Type+"'";
  try{
      dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &this->itemsCount);
      if (this->itemsCount>0){
        this->items = new String[this->itemsCount];
        sqlCmd = "SELECT name FROM ["+Table+"] WHERE type='"+Type+"'";
        dataBase->sendSqlQuery(sqlCmd, "name", this->items);
      }
  }catch(...){}
}

