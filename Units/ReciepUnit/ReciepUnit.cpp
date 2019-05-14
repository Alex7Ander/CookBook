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
  this->RecipeTable->ColWidths[0] = 150;
  this->RecipeTable->ColWidths[1] = 140;
  this->RecipeTable->ColWidths[2] = 150;
  this->RecipeTable->Cells[0][0] = "Ингредиент";
  this->RecipeTable->Cells[1][0] = "Количество, гр/мл";
  this->RecipeTable->Cells[2][0] = "Калорийность";
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::FormShow(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  typeList *RecipesTypes = new typeList(dataBase, "Recipes");
  if (RecipesTypes->getItemsCount()>0){
    for (int i=0; i<RecipesTypes->getItemsCount(); i++)
      this->RecipeTypeComboBox->Items->Add(RecipesTypes->getItem(i));
    this->RecipeTypeComboBox->ItemIndex = 0;
    this->RecipeTypeComboBoxChange(Sender);
  }
  else{
    MessageBox(NULL,(LPCTSTR)"Книга рецептов пока пуста.\nНо Вы можете начать её заполнять прямо сейчас",(LPCTSTR)"Нет рецептов", MB_OK | MB_ICONASTERISK | MB_TOPMOST);
  }
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::RecipeTypeComboBoxChange(TObject *Sender)
{
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  String Type = this->RecipeTypeComboBox->Text;
  objectsList *Recipes = new objectsList(dataBase, "Recipes", Type);
  this->RecipesListBox->Items->Clear();
  for (int i=0; i<Recipes->getItemsCount(); i++){
    this->RecipesListBox->Items->Add(Recipes->getItem(i));
  }
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::RecipesListBoxClick(TObject *Sender)
{
  int index = this->RecipesListBox->ItemIndex;
  String Name = this->RecipesListBox->Items->Strings[index];
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  currentRecipe = new Recipe(Name, dataBase);
  this->RecipeTable->RowCount = currentRecipe->getCountOfIngredients()+1;
  for (int i=0; i<currentRecipe->getCountOfIngredients(); i++){
    this->RecipeTable->Cells[0][i+1] = currentRecipe->getIngredient(i)->getName();
    this->RecipeTable->Cells[1][i+1] = currentRecipe->getIngrVolume(i);
    double currentEnergy = currentRecipe->getIngredient(i)->getCalories()/100*currentRecipe->getIngrVolume(i);
    this->RecipeTable->Cells[2][i+1] = FloatToStr(currentEnergy);
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
void __fastcall TReciepForm::FormClose(TObject *Sender, TCloseAction &Action)
{
  StartForm->closeReciepBranch();
  Action = caFree;
}
//---------------------------------------------------------------------------
void __fastcall TReciepForm::DeleteReciepButtonClick(TObject *Sender)
{
  int index = this->RecipesListBox->ItemIndex;
  String Name = this->RecipesListBox->Items->Strings[index];
  DATA_BASE *dataBase = new DATA_BASE(this, "data.mdb", "Recipes");
  currentRecipe = new Recipe(Name, dataBase);
  if (MessageBox(NULL,(LPCTSTR)"Вы уверены, что хотете удалить этот рецепт?",(LPCTSTR)"Удалить?",MB_OKCANCEL | MB_ICONQUESTION | MB_TOPMOST)==1){
    int resOfDeliting = currentRecipe->DeleteRecipe(dataBase);
    if (resOfDeliting==0){
      this->RecipesListBox->Items->Delete(index);
      for (int j=0; j<3; j++){
        for (int i=1; i<RecipeTable->RowCount; i++){
          RecipeTable->Cells[j][i]="";
        }
      }
      RecipeTable->RowCount = 2;
      this->RecipeTextMemo->Lines->Clear();
    }
    else{

    }
  }
}
//---------------------------------------------------------------------------

