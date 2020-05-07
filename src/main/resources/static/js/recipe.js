$(document).ready(function(){
    //Hide PopUp windows
	PopUpHide("edit_mainInfo_popup");
	PopUpHide("edit_ingr_list_popup");
	PopUpHide("add_photo_popup");
});

/*Prepare for editing*/
function startEditMainInfo(){
	var recipeName = document.getElementById('name').innerText;
	document.getElementById('editName').value = recipeName;
	var recipeType = document.getElementById('type').innerText;
	document.getElementById('editType').value = recipeType;
	var recipeTagline = document.getElementById('tagline').innerText;
	document.getElementById('editTagline').value = recipeTagline;
	var recipeText = document.getElementById('text').innerText;
	document.getElementById('editText').value = recipeText;
	PopUpShow('edit_mainInfo_popup');
}

function startEditIngredientsList(){

}

/*Editing procedures*/
function 

function deleteIngrFromTable(){
	var currentLine = event.target.parentNode.parentNode;
	currentLine.remove();
}


function addNewPhoto(){

}

function deletePhoto(){

}


/*Saving results of editing*/
function editMainInfo(){	
	var id = document.getElementById('id').value;
	var newName = document.getElementById('editName').value;
	var newType = document.getElementById('editType').value;
	var newTagline = document.getElementById('editTagline').value
	var newText = document.getElementById('editText').value;

	var mainInfoData = new FormData();
	mainInfoData.append("id", id);
	mainInfoData.append("name", newName);
	mainInfoData.append("type", newType);
	mainInfoData.append("tagline", newTagline);
	mainInfoData.append("text", newText);

	$.ajax({type: "POST", url: "/recipe/editMainInfo", cache: false, dataType: 'json', contentType: false, processData: false, data: mainInfoData,
		success: function(respond, status, jqXHR) {
			if (typeof respond.error === 'undefined') {	
				document.getElementById('name').innerText = newName;
				document.getElementById('type').innerText = newType;
				document.getElementById('tagline').innerText = newTagline;
				document.getElementById('text').innerText = newText;
				alert('Изменения успешно сохранены');						
			}
			else {
				alert('Произошла ошибка при сохранении: ' + respond.data);
			}
		}, 
		error: function(respond, status, jqXHR) {
			alert('Произошла ошибка при сохранении: ' + status);
		},
		complete: function(){
			PopUpHide('edit_mainInfo_popup');
		}
	});	
}

function editIngredientsList(){

}

function editPhotoList(){

}
