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
  NewIngredientForm->Show();       
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeIngredientBranch();
  Action = caFree;
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  typeList *IngrTypes = new typeList(dataBase, "Ingredients");
  if (IngrTypes->getItemsCount()>0){
    for (int i=0; i<IngrTypes->getItemsCount(); i++)
      this->TypeComboBox->Items->Add(IngrTypes->getItem(i));
    this->TypeComboBox->ItemIndex = 0;
    this->TypeComboBoxChange(Sender);
  }
  else{
    MessageBox(NULL,(LPCTSTR)"��� ������������������ ������������",(LPCTSTR)"��� ������������", MB_OK | MB_ICONASTERISK | MB_TOPMOST);
  }
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::TypeComboBoxChange(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  String Type = this->TypeComboBox->Text;
  objectsList *Ingredients = new objectsList(dataBase, "Ingredients", Type);
  this->IngredientsListBox->Items->Clear();
  for (int i=0; i<Ingredients->getItemsCount(); i++){
    this->IngredientsListBox->Items->Add(Ingredients->getItem(i));
  }
  ClearInfo();
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
    MessageBox(NULL,(LPCTSTR)"�� �� ������� ����������!",(LPCTSTR)"����������?",MB_OK| MB_ICONQUESTION | MB_TOPMOST);
  }
  else{
    int res = MessageBox(NULL,(LPCTSTR)"�� �������, ��� ������ ������� ���� ����������?",(LPCTSTR)"�������?",MB_OKCANCEL | MB_ICONQUESTION | MB_TOPMOST);
    if (res == 1){
      DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
      int resOfDeleting = currentIngr->DeleteIngredient(dataBase);
      if (!resOfDeleting){
        ClearInfo();
        int index = this->IngredientsListBox->ItemIndex;
        this->IngredientsListBox->Items->Delete(index);
        MessageBox(NULL,(LPCTSTR)"�������� ��������!!!",(LPCTSTR)"�������",MB_OK | MB_ICONASTERISK | MB_TOPMOST);
      }
    }
  }
}
//---------------------------------------------------------------------------
void __fastcall TIngredientsForm::EditButtonClick(TObject *Sender)
{
  if (this->protEdit->Text!=currentIngr->getProteins()){
     DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
     currentIngr->EditIngredient(dataBase, 2, this->protEdit->Text);
  }
  if (this->fatsEdit->Text!=currentIngr->getFats()){
     DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
     currentIngr->EditIngredient(dataBase, 3, this->fatsEdit->Text);
  }
  if (this->carbEdit->Text!=currentIngr->getCarbohydrates()){
     DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
     currentIngr->EditIngredient(dataBase, 4, this->carbEdit->Text);
  }
  String newDescr;
  for (int i=0; i<descriptionMemo->Lines->Count; i++) newDescr += descriptionMemo->Lines->Strings[i];
  if (newDescr!=currentIngr->getDescription()){
     DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
     currentIngr->EditIngredient(dataBase, 5, newDescr);
  }
}
//---------------------------------------------------------------------------

