var currentIngredientsList = new Array();

function setIngredientsNames(){
	currentIngredientsList.length = 0;
	var ingrType = $("#ingrType").val();
	getIngrListFromServer(ingrType, currentIngredientsList);
	$('#ingrName').empty();	
	$("#ingrName").append('<option> </option>');
	$.each(currentIngredientsList, function(index, value){
		$("#ingrName").append('<option>'+currentIngredientsList[index].name+'</option>');
	});
}

function setIngredientValues(){	
	var currentIngrIndex = $("#ingrName").prop('selectedIndex') - 1;
	var prot = currentIngredientsList[currentIngrIndex].prot;
	var carbo = currentIngredientsList[currentIngrIndex].carbo;
	var fat = currentIngredientsList[currentIngrIndex].fat;
	var calorie = prot*4 + carbo*4 + fat*9;
	var description = currentIngredientsList[currentIngrIndex].descr;


	$('#ingrTable').find('tr').eq(1).remove();
	$('#ingrTable').append('<tr><td>'+prot+'</td><td>'+fat+'</td><td>'+carbo+'</td><td>'+calorie+' ккал</td></tr>');
	$('#description').empty();	
	$('#description').append(description);
	$('#selectedIngrName').empty();	
	$('#selectedIngrName').append(currentIngredientsList[currentIngrIndex].name);
}