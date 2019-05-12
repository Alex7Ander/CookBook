//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "ShopListUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TShopListForm *ShopListForm;
//---------------------------------------------------------------------------
__fastcall TShopListForm::TShopListForm(TComponent* Owner)
        : TForm(Owner)
{
}
void TShopListForm::AddIngredient(Recipe *anyRecipe)
{   
  for (int i=0; i<anyRecipe->getCountOfIngredients(); i++){
     bool notAdded = true;
     String item = anyRecipe->getIngredient(i)->getName();
     for (int j=0; j<this->ShopListBox->Items->Count; j++){
        if (this->ShopListBox->Items->Strings[j]==item) notAdded = false;
     }
     if (notAdded){
       ShopListBox->Items->Add(item);
     }
  }

}
//---------------------------------------------------------------------------
void __fastcall TShopListForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeShopListBranch();
  Action = caFree;        
}
//---------------------------------------------------------------------------
void __fastcall TShopListForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  typeList *RecipesTypes = new typeList(dataBase, "Recipes");
  if (RecipesTypes->getItemsCount()>0){
    for (int i=0; i<RecipesTypes->getItemsCount(); i++)
      this->TypesComboBox->Items->Add(RecipesTypes->getItem(i));
    this->TypesComboBox->ItemIndex = 0;
    this->TypesComboBoxChange(Sender);
  }
}
//---------------------------------------------------------------------------
void __fastcall TShopListForm::TypesComboBoxChange(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  String Type = this->TypesComboBox->Text;
  objectsList *Recipes = new objectsList(dataBase, "Recipes", Type);
  this->RecipesListBox->Items->Clear();
  for (int i=0; i<Recipes->getItemsCount(); i++){
    this->RecipesListBox->Items->Add(Recipes->getItem(i));
  }
}
//---------------------------------------------------------------------------
void __fastcall TShopListForm::RecipesListBoxDblClick(TObject *Sender)
{
  int index = this->RecipesListBox->ItemIndex;
  String item = this->RecipesListBox->Items->Strings[index];
  checkedRecipesListBox->Items->Add(item);
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  AddIngredient(new Recipe(item, dataBase));
}
//---------------------------------------------------------------------------
void __fastcall TShopListForm::SaveButtonClick(TObject *Sender)
{
TSaveDialog *sd = new TSaveDialog(this);
sd->Filter = "Text files (*.txt)";
if (sd->Execute()){
  Path = sd->FileName;
  int pos = Path.LastDelimiter(".");
  String extention = Path;
  extention.Delete(1,pos);
  if (extention!="txt") Path+=".txt";
  ShopListBox->Items->SaveToFile(Path);
  this->Close();
}
}
//---------------------------------------------------------------------------

