var currentIngredientsList = new Array();

function setIngredientsNames(){
	currentIngredientsList.length = 0;
	var ingrType = $("#ingrType").val();
	getIngrListFromServer(ingrType, currentIngredientsList);
	$('#ingrName').empty();	
	$.each(currentIngredientsList, function(index, value){
		$("#ingrName").append('<option>'+currentIngredientsList[index].name+'</option>');
	});
}

function setIngredientValues(){
	/*
	var currentIngrIndex = $("#ingrName").val();
	var prot = currentIngredientsList[currentIngrIndex].prot;
	var carbo = currentIngredientsList[currentIngrIndex].carbo;
	var fat = currentIngredientsList[currentIngrIndex].fat;
	var calorie = prot*4 + carbo*4 + fat*9;
	var description = currentIngredientsList[currentIngrIndex].descr;
	$('#ingrTable > tbody:last-child').append('<tr>'+prot+'</tr><tr>'+fat+'</tr><tr>'+carbo+'</tr><tr>'+calorie+'</tr>);	
	$('#description').prop(text, description);
	*/
}