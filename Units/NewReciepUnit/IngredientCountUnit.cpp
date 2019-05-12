//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop
#include "IngredientCountUnit.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TIngredientCountForm *IngredientCountForm;
//---------------------------------------------------------------------------
__fastcall TIngredientCountForm::TIngredientCountForm(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TIngredientCountForm::CountEditKeyDown(TObject *Sender,
      WORD &Key, TShiftState Shift)
{
  if (Key == VK_RETURN){
     if (this->CountEdit->Text.Length()==0){
        MessageBox(NULL,(LPCTSTR)"Ошибка! Вы не ввели количество ингредиента",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
     }
     else{
        try{
          double v = StrToFloat(this->CountEdit->Text);
          NewRecieptForm->setIngredientVolume(this->CountEdit->Text);
          this->Close();
        }
        catch(...){
          MessageBox(NULL,(LPCTSTR)"Ошибка! Введенное Вами значение не является числом",(LPCTSTR)"Ошибка", MB_OK | MB_ICONERROR | MB_TOPMOST);
        }
     }
  }
}
//---------------------------------------------------------------------------
