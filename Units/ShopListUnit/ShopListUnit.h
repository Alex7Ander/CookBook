//---------------------------------------------------------------------------

#ifndef ShopListUnitH
#define ShopListUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <vector.h>

#include "RecipeClasses.h"
#include "StartUnit.h"
//---------------------------------------------------------------------------
class TShopListForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *RecipesListGroupBox;
        TComboBox *TypesComboBox;
        TListBox *RecipesListBox;
        TGroupBox *CheckedRecipesGroupBox;
        TGroupBox *GroupBox1;
        TListBox *checkedRecipesListBox;
        TListBox *ShopListBox;
        TButton *SaveButton;
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
        void __fastcall FormShow(TObject *Sender);
        void __fastcall TypesComboBoxChange(TObject *Sender);
        void __fastcall RecipesListBoxDblClick(TObject *Sender);
        void __fastcall SaveButtonClick(TObject *Sender);
private:	// User declarations
        String Path;
        void AddIngredient(Recipe *anyRecipe);
public:		// User declarations
        __fastcall TShopListForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TShopListForm *ShopListForm;
//---------------------------------------------------------------------------
#endif
