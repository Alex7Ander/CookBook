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
//---------------------------------------------------------------------------
void __fastcall TShopListForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeShopListBranch();
  Action = caFree;        
}
//---------------------------------------------------------------------------