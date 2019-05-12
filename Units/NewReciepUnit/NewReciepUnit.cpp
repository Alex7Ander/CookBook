//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "NewReciepUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TNewRecieptForm *NewRecieptForm;
//---------------------------------------------------------------------------
__fastcall TNewRecieptForm::TNewRecieptForm(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void TNewRecieptForm::setIngredientVolume(String ingrVolume)
{
  int index = this->IngredientsListBox->ItemIndex;
  String ingrName = this->IngredientsListBox->Items->Strings[index];
  this->NecessaryIngredientsListEditor->InsertRow(ingrName, ingrVolume, true);
}
//---------------------------------------------------------------------------
void __fastcall TNewRecieptForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeNewReciepBranch();
  Action = caFree;
}
//---------------------------------------------------------------------------
void __fastcall TNewRecieptForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  typeList *RecipesTypes = new typeList(dataBase, "Recipes");
  if (RecipesTypes->getItemsCount()>0){
    for (int i=0; i<RecipesTypes->getItemsCount(); i++)
      this->TypeComboBox->Items->Add(RecipesTypes->getItem(i));
  }
  typeList *IngrTypes = new typeList(dataBase, "Ingredients");
  if (IngrTypes->getItemsCount()>0){
    for (int i=0; i<IngrTypes->getItemsCount(); i++)
      this->IngredientsTypeComboBox->Items->Add(IngrTypes->getItem(i));
    this->IngredientsTypeComboBox->ItemIndex = 0;
    this->IngredientsTypeComboBoxChange(Sender);
  }
}
//---------------------------------------------------------------------------
void __fastcall TNewRecieptForm::IngredientsTypeComboBoxChange(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
  String Type = this->IngredientsTypeComboBox->Text;
  objectsList *Ingredients = new objectsList(dataBase, "Ingredients", Type);
  this->IngredientsListBox->Items->Clear();
  for (int i=0; i<Ingredients->getItemsCount(); i++){
    this->IngredientsListBox->Items->Add(Ingredients->getItem(i));
  }
}
//---------------------------------------------------------------------------

void __fastcall TNewRecieptForm::AddButtonClick(TObject *Sender)
{
  Application->CreateForm(__classid(TIngredientCountForm), &IngredientCountForm);
  IngredientCountForm->ShowModal();
}
//---------------------------------------------------------------------------

void __fastcall TNewRecieptForm::DeleteButtonClick(TObject *Sender)
{
  int index = this->NecessaryIngredientsListEditor->Row;
  if (index>0){
     this->NecessaryIngredientsListEditor->DeleteRow(index);
  }
  else{
    for (int i=0; i<NecessaryIngredientsListEditor->RowCount; i++)
     this->NecessaryIngredientsListEditor->DeleteRow(i);
  }
}
//---------------------------------------------------------------------------

void __fastcall TNewRecieptForm::SaveButtonClick(TObject *Sender)
{
  String Name = this->NameEdit->Text;
  if (Name.Length()==0){
     MessageBox(NULL,(LPCTSTR)"Ошибка! Вы не ввели имя рецепта",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
  }
  else{
    String Type = this->TypeComboBox->Text;
    if (Type.Length()==0) Type = "unknown";
    String Text;
    for (int i=0; i<this->DescriptionMemo->Lines->Count; i++) Text += this->DescriptionMemo->Lines->Strings[i];
    int CountOfIngredients = this->NecessaryIngredientsListEditor->Strings->Count;
    String *ingrNames = new String[CountOfIngredients];
    double *ingrVolumes = new double[CountOfIngredients];
    for (int i=0; i<CountOfIngredients; i++){
      ingrNames[i] = this->NecessaryIngredientsListEditor->Cells[0][i+1];
      ingrVolumes[i] = StrToFloat(this->NecessaryIngredientsListEditor->Cells[1][i+1]);
    }
    DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Ingredients");
    newRecipe = new Recipe(Name, Type, Text, CountOfIngredients, ingrNames, ingrVolumes, dataBase);
    newRecipe->SaveRecipe(dataBase);
  }
}
//---------------------------------------------------------------------------
void __fastcall TNewRecieptForm::IngredientsListBoxDblClick(TObject *Sender)
{
  Application->CreateForm(__classid(TIngredientCountForm), &IngredientCountForm);
  IngredientCountForm->ShowModal();
}
//---------------------------------------------------------------------------

