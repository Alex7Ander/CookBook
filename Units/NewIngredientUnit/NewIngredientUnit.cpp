//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "NewIngredientUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TNewIngredientForm *NewIngredientForm;
//---------------------------------------------------------------------------
__fastcall TNewIngredientForm::TNewIngredientForm(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TNewIngredientForm::saveButtonClick(TObject *Sender)
{
String Name = this->nameEdit->Text;
if (Name.Length()==0){
  MessageBox(NULL,(LPCTSTR)"Ошибка! Вы не ввели имя ингредиента",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
}
else{
  try{
    String Type = this->typeComboBox->Text;
    if (Type.Length()==0) Type = "unknown";
    String Descr = "";
    for (int i=0; i<this->descriptionMemo->Lines->Count; i++) Descr += this->descriptionMemo->Lines->Strings[i];
    double Prot = StrToFloat(this->protEdit->Text);
    double Fats = StrToFloat(this->fatsEdit->Text);
    double Carb = StrToFloat(this->carbEdit->Text);
    Ingredient *newIngredient = new Ingredient(Name, Type, Descr, Prot, Fats, Carb);
    DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
    int resOfSaveing = newIngredient->SaveNewIngredient(dataBase);
    if (!resOfSaveing){
       MessageBox(NULL,(LPCTSTR)"Успешное сохранение",(LPCTSTR)"Успешно", MB_OK | MB_ICONASTERISK | MB_TOPMOST);
       this->Close();
    }
    else{
      MessageBox(NULL,(LPCTSTR)"Ошибка сохранения",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
    }
  }catch(Exception &exp){
    MessageBox(NULL,(LPCTSTR)"Ошибка сохранения",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
    ShowMessage("Ошибка! Проверьте правильность введеных вами значений!");
  }
}
}
//---------------------------------------------------------------------------
void __fastcall TNewIngredientForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  Action = caFree;        
}
//---------------------------------------------------------------------------
void __fastcall TNewIngredientForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  typeList *IngrTypes = new typeList(dataBase, "Ingredients");
  if (IngrTypes->getItemsCount()>0){
    for (int i=0; i<IngrTypes->getItemsCount(); i++)
      this->typeComboBox->Items->Add(IngrTypes->getItem(i));
  }        
}
//---------------------------------------------------------------------------

