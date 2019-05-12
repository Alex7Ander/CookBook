//---------------------------------------------------------------------------

#ifndef NewIngredientUnitH
#define NewIngredientUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>

#include "RecipeClasses.h"
//---------------------------------------------------------------------------
class TNewIngredientForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *GroupBox1;
        TLabel *nameLabel;
        TLabel *Label1;
        TLabel *Label2;
        TLabel *Label3;
        TLabel *Label4;
        TEdit *nameEdit;
        TEdit *protEdit;
        TEdit *fatsEdit;
        TEdit *carbEdit;
        TComboBox *typeComboBox;
        TButton *saveButton;
        TLabel *definitionLabel;
        TMemo *descriptionMemo;
        void __fastcall saveButtonClick(TObject *Sender);
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
        void __fastcall FormShow(TObject *Sender);
private:	// User declarations
        
public:		// User declarations
        __fastcall TNewIngredientForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TNewIngredientForm *NewIngredientForm;
//---------------------------------------------------------------------------
#endif
