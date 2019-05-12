//---------------------------------------------------------------------------

#ifndef IngredientCountUnitH
#define IngredientCountUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>

#include "NewReciepUnit.h"
//---------------------------------------------------------------------------
class TIngredientCountForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *CountGroupBox;
        TEdit *CountEdit;
        void __fastcall CountEditKeyDown(TObject *Sender, WORD &Key,
          TShiftState Shift);
private:	// User declarations
public:		// User declarations
        __fastcall TIngredientCountForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TIngredientCountForm *IngredientCountForm;
//---------------------------------------------------------------------------
#endif
