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

function setIngredientValues(){	
	var currentIngrIndex = $("#ingrName").prop('selectedIndex') - 1;
	var prot = currentIngredientsList[currentIngrIndex].prot;
	var carbo = currentIngredientsList[currentIngrIndex].carbo;
	var fat = currentIngredientsList[currentIngrIndex].fat;
	var calorie = prot*4 + carbo*4 + fat*9;
	var description = currentIngredientsList[currentIngrIndex].descr;

	$('#ingrId').val(currentIngredientsList[currentIngrIndex].id);
	$('#ingrTable').find('tr').eq(1).remove();
	$('#ingrTable').append('<tr><td>'+prot+'</td><td>'+fat+'</td><td>'+carbo+'</td><td>'+calorie+' ккал</td></tr>');
	$('#description').empty();	
	$('#description').append(description);
	$('#selectedIngrName').empty();	
	$('#selectedIngrName').append(currentIngredientsList[currentIngrIndex].name);

	//$('#imageUploader').show();	
	loadImg();
	if(currentIngredientsList[currentIngrIndex].common != true){
		$('#imageUploader').show();
	}
	
}

function loadImg(){
	var currentIngrId = $('#ingrId').val();
	$("#spinner_image").show();
	$("#image").hide();
    jQuery.ajax({	
        type: "GET",
        url: "/ingredient/loadImg?ingrId=" + currentIngrId,
        cache: false,
        xhr:function(){
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        },
        success: function(data){
            var img = document.getElementById('image');
            var url = window.URL || window.webkitURL;
			img.src = url.createObjectURL(data);
			$("#image").show();
			$("#spinner_image").hide();
        },
        error: function(respond, status, jqXHR) {
            alert(respond.statusText);
        }
    });
}

function sendIngrImage(){
	var ingredientData = new FormData();
	var currentIngrId = $('#ingrId').val(); 		
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