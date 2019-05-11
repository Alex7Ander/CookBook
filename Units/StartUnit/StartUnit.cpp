//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "StartUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
//---------------------------------------------------------------------------
TStartForm *StartForm;
__fastcall TStartForm::TStartForm(TComponent* Owner)
        : TForm(Owner)
{
  reciepBranch = false;
  newReciepBranch = false;
  ingredientBranch = false;
  shopListBranch = false;
}
//---------------------------------------------------------------------------
bool TStartForm::isBusy()
{
  if (reciepBranch == true || newReciepBranch == true ||
  ingredientBranch == true || shopListBranch == true) return true;
  else return false;
}
//---------------------------------------------------------------------------
void __fastcall TStartForm::ReciepToolButtonClick(TObject *Sender)
{
  if (isBusy())
  {
     String BtOk[]={"Продолжить"};
  }
  else
  {
    if (this->reciepBranch == false){
      Application->CreateForm(__classid(TReciepForm), &ReciepForm);
      ReciepForm->Show();
      this->reciepBranch = true;
    }
  }
}
//---------------------------------------------------------------------------
void __fastcall TStartForm::NewReciepToolButtonClick(TObject *Sender)
{
  if (isBusy())
  {
     String BtOk[]={"Продолжить"};
  }
  else
  {
    if (this->newReciepBranch == false){
      Application->CreateForm(__classid(TNewRecieptForm), &NewRecieptForm);
      NewRecieptForm->Show();
      this->newReciepBranch = true;
    }
  }
}
//---------------------------------------------------------------------------
void __fastcall TStartForm::IngridientToolButtonClick(TObject *Sender)
{
  if (isBusy()){
     String BtOk[]={"Продолжить"};
  }
  else{
    if (this->ingredientBranch == false){
       Application->CreateForm(__classid(TIngredientsForm), &IngredientsForm);
       IngredientsForm->Show();
       this->ingredientBranch = true;
    }
  }
}
//---------------------------------------------------------------------------
void __fastcall TStartForm::ShopListToolButtonClick(TObject *Sender)
{
  if (isBusy())
  {
     String BtOk[]={"Продолжить"};
  }
  else
  {
    if (this->shopListBranch == false){
      Application->CreateForm(__classid(TShopListForm), &ShopListForm);
      ShopListForm->Show();
      this->shopListBranch = true;
    }
  }
}
//---------------------------------------------------------------------------
void __fastcall TStartForm::ExitBitBtnClick(TObject *Sender)
{
  this->Close();
}
//---------------------------------------------------------------------------

