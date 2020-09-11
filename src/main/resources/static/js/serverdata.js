//Функция отправлят запрос для получения списка ингредиентов типа ingrType
function getIngrListFromServer(ingrType, currentIngredientsList){
	var typeData = new FormData();
	var requestedIngredientsList = new Array();
    typeData.append("ingrType", ingrType);
    $.ajax({type: "GET", url: "/ingredient/getIngredients?ingrType=" + ingrType, async: false, cache: false, dataType: 'json', contentType: false, processData: false,
        success: function(respond, status, jqXHR){
            if (typeof respond.error === 'undefined') {
				requestedIngredientsList = respond;
            }
        }
    });
	$.each(requestedIngredientsList, function(index,value){
        var ingredient = new Ingredient();
        ingredient.id = requestedIngredientsList[index].id,
        ingredient.name = requestedIngredientsList[index].name,
        ingredient.type = requestedIngredientsList[index].type,
        ingredient.descr = requestedIngredientsList[index].descr,
        ingredient.prot = requestedIngredientsList[index].prot,
        ingredient.fat = requestedIngredientsList[index].fat,
        ingredient.carbo = requestedIngredientsList[index].carbo;
        currentIngredientsList.push(ingredient);
		//currentIngredientsList.push(requestedIngredientsList[index]);
	});
}