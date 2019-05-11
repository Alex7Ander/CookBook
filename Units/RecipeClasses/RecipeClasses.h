//---------------------------------------------------------------------------
#ifndef RecipeClassesH
#define RecipeClassesH
#include <Classes.hpp>
#include <vector.h>

#include "DataBaseUnit.h"
//---------------------------------------------------------------------------
class Ingredient
{
private:
        String name;
        String type;
        String description;
        double proteins;
        double fats;
        double carbohydrates;
        double calories;

public:
        Ingredient();
        Ingredient(String Name, String Type, String Description, double Proteins, double Fats, double Carbohydrates);
        Ingredient(String Name, DATA_BASE *dataBase);
        void setCalories();
        double getCalories(){return calories;}
        double getProteins(){return proteins;}
        double getFats(){return fats;}
        double getCarbohydrates(){return carbohydrates;}
        String getDescription(){return description;}
        String getName(){return name;}
        String getType(){return type;}
        int SaveNewIngredient(DATA_BASE *dataBase);
        int DeleteIngredient(DATA_BASE *dataBase);
        int EditIngredient();
};

class Recipe
{
private:
        String name;
        String type;
        String text;
        String ingrTable;
        String photoFolder;
        int countOfIngredients;
        vector<Ingredient*> ingredients;
        vector<double> volumeOfIngr;
public:
        Recipe (String Name, String Type, String text, int countOfIngredients);
        Recipe (String Name, DATA_BASE *dataBase);
        String getName(){return name;}
        String getType(){return type;}
        String getText(){return text;}
        int getCountOfIngredients(){return countOfIngredients;}
        Ingredient* getIngredient(int at){return ingredients[at];}
        double getIngrVolume(int at){return volumeOfIngr[at];}
        int SaveRecipe(DATA_BASE *dataBase);
        int DeleteRecipe(DATA_BASE *dataBase);
        int EditRecipe(DATA_BASE *dataBase);
};
#endif