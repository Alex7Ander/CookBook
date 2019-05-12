//---------------------------------------------------------------------------

#ifndef IngredientsUnitH
#define IngredientsUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <vector.h>
#include "NewIngredientUnit.h"
#include "StartUnit.h"
//---------------------------------------------------------------------------
class TIngredientsForm : public TForm
{
__published:	// IDE-managed Components
        TGroupBox *TypeGroupBox;
        TComboBox *TypeComboBox;
        TGroupBox *IngredientsGroupBox;
        TListBox *IngredientsListBox;
        TGroupBox *GroupBox1;
        TButton *DeleteButton;
        TButton *EditButton;
        TButton *AddNewButton;
        TLabel *Label1;
        TLabel *Label2;
        TLabel *Label3;
        TLabel *Label4;
        TLabel *Label5;
        TEdit *energyEdit;
        TEdit *protEdit;
        TEdit *fatsEdit;
        TEdit *carbEdit;
        TLabel *Label6;
        TMemo *descriptionMemo;
        void __fastcall AddNewButtonClick(TObject *Sender);
        void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
        void __fastcall FormShow(TObject *Sender);
        void __fastcall TypeComboBoxChange(TObject *Sender);
        void __fastcall IngredientsListBoxClick(TObject *Sender);
        void __fastcall DeleteButtonClick(TObject *Sender);
        void __fastcall EditButtonClick(TObject *Sender);
private:	// User declarations
        /*int typeCount;
        String *TypesList;  */
        typeList *IngrTypes;
        objectsList *Ingredients;
        Ingredient *currentIngr;
        void ClearInfo();
public:		// User declarations
        __fastcall TIngredientsForm(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TIngredientsForm *IngredientsForm;
//---------------------------------------------------------------------------
#endif
