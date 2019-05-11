#include <vcl.h>
#pragma hdrstop
#include "IngredientsUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TIngredientsForm *IngredientsForm;
//---------------------------------------------------------------------------
__fastcall TIngredientsForm::TIngredientsForm(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void TIngredientsForm::ClearInfo()
{
  this->descriptionMemo->Lines->Clear();
  this->protEdit->Text="";
  this->fatsEdit->Text="";
  this->carbEdit->Text="";
  this->energyEdit->Text="";
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::AddNewButtonClick(TObject *Sender)
{
  Application->CreateForm(__classid(TNewIngredientForm), &NewIngredientForm);
  for (int i=0; i<typeCount; i++) NewIngredientForm->typeComboBox->Items->Add(TypesList[i]);
  NewIngredientForm->Show();       
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeIngredientBranch();
  delete[] TypesList;
  Action = caFree;
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  try{
    String sqlCmd = "SELECT COUNT(type) AS resultSqlInt FROM (SELECT DISTINCT type FROM [Ingredients])";//sqlCmd = "SELECT COUNT(name) FROM [Ingredients] AS resultSqlInt";
    dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &typeCount);
    if (typeCount>0){
      TypesList = new String[typeCount];
      sqlCmd = "SELECT DISTINCT type FROM [Ingredients]";
      dataBase->sendSqlQuery(sqlCmd, "type", TypesList);
      for (int i=0; i<typeCount; i++)
        this->TypeComboBox->Items->Add(TypesList[i]);
      this->TypeComboBox->ItemIndex = 0;
      this->TypeComboBoxChange(Sender);
    }
    else{
      MessageBox(NULL,(LPCTSTR)"Нет зарегистрированный ингредиентов",(LPCTSTR)"Нет ингредиентов", MB_OK | MB_ICONASTERISK | MB_TOPMOST);
    }
  }catch(...){}
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::TypeComboBoxChange(TObject *Sender)
{
  String Type = this->TypeComboBox->Text;
  String sqlCmd = "SELECT COUNT(name) AS resultSqlInt FROM [Ingredients] WHERE type='"+Type+"'";
  int countOfIngredients;
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  try{
      dataBase->sendSqlQuery(sqlCmd, "resultSqlInt", &countOfIngredients);
      if (countOfIngredients>0){
        String *listOfIngredients = new String[countOfIngredients];
        sqlCmd = "SELECT name FROM [Ingredients] WHERE type='"+Type+"'";
        dataBase->sendSqlQuery(sqlCmd, "name", listOfIngredients);
        this->IngredientsListBox->Items->Clear();
        for (int i=0; i<countOfIngredients; i++){
          this->IngredientsListBox->Items->Add(listOfIngredients[i]);
        }
        ClearInfo();
      }
  }catch(...){}
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::IngredientsListBoxClick(TObject *Sender)
{
  int index = this->IngredientsListBox->ItemIndex;
  String Name = this->IngredientsListBox->Items->Strings[index];
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  currentIngr = new Ingredient(Name, dataBase);
  this->protEdit->Text = FloatToStr(currentIngr->getProteins());
  this->fatsEdit->Text = FloatToStr(currentIngr->getFats());
  this->carbEdit->Text = FloatToStr(currentIngr->getCarbohydrates());
  this->energyEdit->Text = FloatToStr(currentIngr->getCalories());
  this->descriptionMemo->Lines->Clear();
  this->descriptionMemo->Lines->Add(currentIngr->getDescription());
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::DeleteButtonClick(TObject *Sender)
{
  if (currentIngr==NULL){
    ShowMessage("Вы не выбрали ингредиент");
  }
  else{
    int res = MessageBox(NULL,(LPCTSTR)"Вы уверены, что хотете удалить этот ингредиент?",(LPCTSTR)"Удалить?",MB_OKCANCEL | MB_ICONQUESTION | MB_TOPMOST);
    if (res == 1){
      DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
      int resOfDeleting = currentIngr->DeleteIngredient(dataBase);
      if (!resOfDeleting){
        ClearInfo();
        int index = this->IngredientsListBox->ItemIndex;
        this->IngredientsListBox->Items->Delete(index);
        MessageBox(NULL,(LPCTSTR)"Успешное удаление!!!",(LPCTSTR)"Успешно",MB_OK | MB_ICONASTERISK | MB_TOPMOST);
      }
    }
  }
}
//---------------------------------------------------------------------------
