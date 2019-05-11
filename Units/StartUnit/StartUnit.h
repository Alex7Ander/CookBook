//---------------------------------------------------------------------------
#ifndef StartUnitH
#define StartUnitH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <ComCtrls.hpp>
#include <ImgList.hpp>
#include <ToolWin.hpp>
#include <ExtCtrls.hpp>
#include <ADODB.hpp>
#include <DB.hpp>
#include <Buttons.hpp>

#include "IngredientsUnit.h"
#include "NewReciepUnit.h"
#include "ReciepUnit.h"
#include "ShopListUnit.h"
//---------------------------------------------------------------------------
class TStartForm : public TForm
{
__published:	// IDE-managed Components
        TToolBar *ToolBar1;
        TToolButton *ReciepToolButton;
        TToolButton *NewReciepToolButton;
        TImageList *ImageList1;
        TToolButton *IngridientToolButton;
        TToolButton *ShopListToolButton;
        TGroupBox *HeadGroupBox;
        TLabel *MainLabel;
        TPanel *Panel1;
        TBitBtn *ExitBitBtn;
        void __fastcall NewReciepToolButtonClick(TObject *Sender);
        void __fastcall IngridientToolButtonClick(TObject *Sender);
        void __fastcall ShopListToolButtonClick(TObject *Sender);
        void __fastcall ReciepToolButtonClick(TObject *Sender);
        void __fastcall ExitBitBtnClick(TObject *Sender);
private:	// User declarations
        bool isBusy();
        bool reciepBranch;
        bool newReciepBranch;
        bool ingredientBranch;
        bool shopListBranch;
public:		// User declarations
        __fastcall TStartForm(TComponent* Owner);
        void closeReciepBranch(){this->reciepBranch = false;}
        void closeNewReciepBranch(){this->newReciepBranch = false;}
        void closeIngredientBranch() {this->ingredientBranch = false;}
        void closeShopListBranch() {this->shopListBranch = false;}
};
//---------------------------------------------------------------------------
extern PACKAGE TStartForm *StartForm;
//---------------------------------------------------------------------------
#endif
