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
void __fastcall TNewRecieptForm::FormClose(TObject *Sender,
      TCloseAction &Action)
{
  StartForm->closeNewReciepBranch();
  Action = caFree;
}
//---------------------------------------------------------------------------
