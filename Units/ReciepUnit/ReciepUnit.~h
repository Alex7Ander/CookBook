//---------------------------------------------------------------------------

#ifndef ReciepUnitH
#define ReciepUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <Grids.hpp>

#include "StartUnit.h"
//---------------------------------------------------------------------------
class TReciepForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *ReciepTypeGroupBox;
        TComboBox *RecipeTypeComboBox;
        TGroupBox *GroupBox1;
        TListBox *RecipesListBox;
        TGroupBox *GroupBox2;
        TStringGrid *RecipeTable;
        TMemo *RecipeTextMemo;
        TGroupBox *GroupBox3;
        TButton *AddReciepButton;
        TButton *DeleteReciepButton;
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
        void __fastcall AddReciepButtonClick(TObject *Sender);
        void __fastcall FormShow(TObject *Sender);
        void __fastcall RecipeTypeComboBoxChange(TObject *Sender);
        void __fastcall RecipesListBoxClick(TObject *Sender);
private:	// User declarations
        int typeCount;
        String *TypesList;
        Recipe *currentRecipe;
public:		// User declarations
        __fastcall TReciepForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TReciepForm *ReciepForm;
//---------------------------------------------------------------------------
#endif
