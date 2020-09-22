var currentIngredientsList = new Array();

$(document).ready(function(){
	$('#imageUploader').hide();
	$("#image").hide();
	$("#spinner_image").hide();
});

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
		ingredient.id = requestedIngredientsList[index].id;
		ingredient.name = requestedIngredientsList[index].name;
		ingredient.type = requestedIngredientsList[index].type;
		ingredient.descr = requestedIngredientsList[index].descr;
		ingredient.prot = requestedIngredientsList[index].prot;
		ingredient.fat = requestedIngredientsList[index].fat;
		ingredient.carbo = requestedIngredientsList[index].carbo;
		ingredient.common = requestedIngredientsList[index].common;
		currentIngredientsList.push(ingredient);
	});
}

function setIngredientValues(){	
	var currentIngrIndex = $("#ingrName").prop('selectedIndex') - 1;
	var prot = currentIngredientsList[currentIngrIndex].prot;
	var carbo = currentIngredientsList[currentIngrIndex].carbo;
	var fat = currentIngredientsList[currentIngrIndex].fat;
	var calorie = prot*4 + carbo*4 + fat*9;
	var description = currentIngredientsList[currentIngrIndex].descr;

	$('#currentIngrId').val(currentIngredientsList[currentIngrIndex].id);
	$('#ingrTable').find('tr').eq(1).remove();
	$('#ingrTable').append('<tr><td>'+prot+'</td><td>'+fat+'</td><td>'+carbo+'</td><td>'+calorie+' ккал</td></tr>');
	$('#description').empty();	
	$('#description').append(description);
	$('#selectedIngrName').empty();	
	$('#selectedIngrName').append(currentIngredientsList[currentIngrIndex].name);	
	loadImg();
	if(currentIngredientsList[currentIngrIndex].common != true){
		$('#imageUploader').show();
	}
	else{
		$('#imageUploader').hide();
	}	
}

function loadImg(){
	var currentIngrIndex = $("#ingrName").prop('selectedIndex') - 1;
	$("#spinner_image").show();
	currentIngredientsList[currentIngrIndex].loadImage("image", finishImageLoading);
}

function finishImageLoading(){
	$("#image").show();
	$("#spinner_image").hide();
}

function showChosenPhoto(){
	let photoFileLoader = document.getElementById('imageLoader');
	currentlyUploadedPhoto = photoFileLoader.files[0];
	let output  = document.getElementById('image');
	output.src = URL.createObjectURL(currentlyUploadedPhoto); 
	/*
	var chosenPhoto = ('#imageLoader').files[0];	
	var chosenPhotoSrc = URL.createObjectURL(chosenPhoto);
	$("#image").attr("src", chosenPhotoSrc);
	*/
}

function sendIngrImage(){
	var ingredientData = new FormData();
	var currentIngrId = $('#currentIngrId').val(); 		
	var currentlyUploadedPhoto = $('#imageLoader').prop('files')[0];
	ingredientData.append('ingrImage', currentlyUploadedPhoto);
	ingredientData.append('ingrId', currentIngrId);

	$.ajax({type: "POST", url: "/ingredient/saveImage", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {		
				var img = document.getElementById('image');
				var url = window.URL || window.webkitURL;
				img.src = url.createObjectURL(currentlyUploadedPhoto);
			}
			else {
				alert("Ошибка при загрузке изображения: " + respond.error);
			}
		}, 
		error: function(respond, status, jqXHR){
			alert(respond.statusText);
		}	
	});
	
}