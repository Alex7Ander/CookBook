//---------------------------------------------------------------------------

#ifndef NewReciepUnitH
#define NewReciepUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <Buttons.hpp>
#include <Grids.hpp>
#include <ValEdit.hpp>

#include "StartUnit.h"
#include "IngredientCountUnit.h"
//---------------------------------------------------------------------------
class TNewRecieptForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *MainInfoGroupBox;
        TLabel *NameLabel;
        TLabel *TypeLabel;
        TEdit *NameEdit;
        TComboBox *TypeComboBox;
        TGroupBox *GroupBox1;
        TMemo *DescriptionMemo;
        TButton *SaveButton;
        TGroupBox *IngredientsGroupBox;
        TComboBox *IngredientsTypeComboBox;
        TListBox *IngredientsListBox;
        TLabel *Label1;
        TButton *DeleteButton;
        TValueListEditor *NecessaryIngredientsListEditor;
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
        void __fastcall FormShow(TObject *Sender);
        void __fastcall IngredientsTypeComboBoxChange(TObject *Sender);
        void __fastcall AddButtonClick(TObject *Sender);
        void __fastcall DeleteButtonClick(TObject *Sender);
        void __fastcall SaveButtonClick(TObject *Sender);
        void __fastcall IngredientsListBoxDblClick(TObject *Sender);
private:	// User declarations
        Recipe *newRecipe;
public:		// User declarations
        __fastcall TNewRecieptForm(TComponent* Owner);
        void setIngredientVolume(String volume);
};
//---------------------------------------------------------------------------
extern PACKAGE TNewRecieptForm *NewRecieptForm;
//---------------------------------------------------------------------------
#endif
