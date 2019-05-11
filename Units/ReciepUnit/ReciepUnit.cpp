//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "ReciepUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TReciepForm *ReciepForm;
//---------------------------------------------------------------------------
__fastcall TReciepForm::TReciepForm(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  try{
    String sqlCmd = "SELECT COUNT(type) AS resultSqlInt FROM (SELECT DISTINCT type FROM [Recipes])";
    dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &typeCount);
    if (typeCount>0){
      TypesList = new String[typeCount];
      sqlCmd = "SELECT DISTINCT type FROM [Recipes]";
      dataBase->sendSqlQuery(sqlCmd, "type", TypesList);
      for (int i=0; i<typeCount; i++)
        this->RecipeTypeComboBox->Items->Add(TypesList[i]);
      this->RecipeTypeComboBox->ItemIndex = 0;
      this->RecipeTypeComboBoxChange(Sender);
    }
    else{
      MessageBox(NULL,(LPCTSTR)"Книга рецептов пока пуста.\nНо Вы можете начать её заполнять прямо сейчас",(LPCTSTR)"Нет рецептов", MB_OK | MB_ICONASTERISK | MB_TOPMOST);
    }
  }catch(...){}
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::RecipeTypeComboBoxChange(TObject *Sender)
{
  String Type = this->RecipeTypeComboBox->Text;
  String sqlCmd = "SELECT COUNT(name) AS resultSqlInt FROM [Recipes] WHERE type='"+Type+"'";
  int countOfRecipes;
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  try{
      dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &countOfRecipes);
      if (countOfRecipes>0){
        String *listOfRecipes = new String[countOfRecipes];
        sqlCmd = "SELECT name FROM [Recipes] WHERE type='"+Type+"'";
        dataBase->sendSqlQuery(sqlCmd, "name", listOfRecipes);
        this->RecipesListBox->Items->Clear();
        for (int i=0; i<countOfRecipes; i++){
          this->RecipesListBox->Items->Add(listOfRecipes[i]);
        }
        //ClearInfo();
      }
  }catch(...){}
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::RecipesListBoxClick(TObject *Sender)
{
  int index = this->RecipesListBox->ItemIndex;
  String Name = this->RecipesListBox->Items->Strings[index];
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  currentRecipe = new Recipe(Name, dataBase);
  this->RecipeTable->RowCount = currentRecipe->getCountOfIngredients();
  for (int i=0; i<currentRecipe->getCountOfIngredients(); i++){
    this->RecipeTable->Cells[0][i+1] = currentRecipe->getIngredient(i)->getName();
    //this->RecipeTable->Cells[1][i+1] = currentRecipe->getIngrVolume(i);
    //this->RecipeTable->Cells[2][i] = currentRecipe->getIngredient(i)->
  }
  this->RecipeTextMemo->Lines->Clear();
  this->RecipeTextMemo->Lines->Add(currentRecipe->getText());
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::AddReciepButtonClick(TObject *Sender)
{
  Application->CreateForm(__classid(TNewRecieptForm), &NewRecieptForm);
  NewRecieptForm->Show();          
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeReciepBranch();
  Action = caFree;
}
//---------------------------------------------------------------------------

