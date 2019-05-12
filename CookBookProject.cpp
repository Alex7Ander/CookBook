//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop
//---------------------------------------------------------------------------
USEFORM("Units\StartUnit\StartUnit.cpp", StartForm);
USEFORM("Units\NewIngredientUnit\NewIngredientUnit.cpp", NewIngredientForm);
USEFORM("Units\IngredientsUnit\IngredientsUnit.cpp", IngredientsForm);
USEFORM("Units\ReciepUnit\ReciepUnit.cpp", ReciepForm);
USEFORM("Units\NewReciepUnit\NewReciepUnit.cpp", NewRecieptForm);
USEFORM("Units\ShopListUnit\ShopListUnit.cpp", ShopListForm);
USEFORM("Units\NewReciepUnit\IngredientCountUnit.cpp", IngredientCountForm);
//---------------------------------------------------------------------------
WINAPI WinMain(HINSTANCE, HINSTANCE, LPSTR, int)
{
        try
        {
                 Application->Initialize();
                 Application->CreateForm(__classid(TStartForm), &StartForm);
                 Application->Run();
        }
        catch (Exception &exception)
        {
                 Application->ShowException(&exception);
        }
        catch (...)
        {
                 try
                 {
                         throw Exception("");
                 }
                 catch (Exception &exception)
                 {
                         Application->ShowException(&exception);
                 }
        }
        return 0;
}
//---------------------------------------------------------------------------
