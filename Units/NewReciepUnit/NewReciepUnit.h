//---------------------------------------------------------------------------

#ifndef NewReciepUnitH
#define NewReciepUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>

#include "StartUnit.h"
//---------------------------------------------------------------------------
class TNewRecieptForm : public TForm
{
__published:	// IDE-managed Components
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
private:	// User declarations
public:		// User declarations
        __fastcall TNewRecieptForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TNewRecieptForm *NewRecieptForm;
//---------------------------------------------------------------------------
#endif
