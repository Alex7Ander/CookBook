class Ingredient{ 

	constructor(id, name, type, descr, prot, fat, carbo, common){
		this.id = id;
		this.name=name;
		this.type=type;
		this.descr=descr;
		this.prot=prot;
		this.fat=fat;
		this.carbo=carbo;
		this.common=common;
		this.calorie = Math.floor((this.fat*9 + this.carbo*4 + this.prot*4)*100)/100;
		this.saved=false;
	}

	save() {
		var ingredientData = new FormData();
		ingredientData.append("name", this.name);
		ingredientData.append("type", this.type);
		ingredientData.append("descr", this.descr);
		ingredientData.append("prot", this.prot);
		ingredientData.append("fat", this.fat);
		ingredientData.append("carbo", this.carbo);
		ingredientData.append("common", this.common);
		var doneSuccessfully = false;
		var message = "";
		var tempId = "";
		$.ajax({type: "POST", url: "/ingredient/save", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {		
					doneSuccessfully = true;
					tempId = respond.id;
					message = "Ингредиент сохранен успешно";				
				}
				else {
					doneSuccessfully = false;
					message = respond.error;
				}
			}, 
			error: function(respond, status, jqXHR){
				message = respond.error;
				doneSuccessfully = false;
			}	
		});
		this.id = tempId;
		this.saved = doneSuccessfully;
		return message;  
	}
	
	isSaved() {
		return this.saved;
	}

	getDataFromServer(name, type){
		this.name = name;
		this.type = type; 
		var body = "?name=" + this.name + "&type=" + this.type;
		var response = $.ajax({type: "GET", url: "/ingredient/getProperties" + body, async: false, cache: false, contentType: false, dataType: 'json', 
			error: function(){
				alert("Ошибка при получении данных с сервера:");
			}
		});
		var ingredientInfo = JSON.parse(response.responseText);
		this.id = ingredientInfo.id;
		this.descr = ingredientInfo.descr;
		this.prot = ingredientInfo.prot;
		this.fat = ingredientInfo.fat;
		this.carbo = ingredientInfo.carbo;
		this.calorie = ingredientInfo.calorie;
	}

	loadImage(imageId, successFinishFunction, errorFinishFunction){
		jQuery.ajax({type: "GET", url: "/ingredient/loadImg?ingrId=" + this.id, cache: false,
			xhr:function(){
				var xhr = new XMLHttpRequest();
				xhr.responseType = 'blob';
				return xhr;
			},
			success: function(data){
				var img = document.getElementById(imageId);
				var url = window.URL || window.webkitURL;
				img.src = url.createObjectURL(data);
				successFinishFunction();
			},
			error: function(respond, status, jqXHR) {
				errorFinishFunction();
			}
		});
	}

	edit(name, type, descr, prot, fat, carbo, successFinishFunction, errorFinishFunction){
		var ingrData = new FormData();
		ingrData.append('id', this.id);
		if(typeof name != 'undefined') ingrData.append('name', name);
		if(typeof type != 'undefined') ingrData.append('type', type);
		if(typeof descr != 'undefined') ingrData.append('description', descr);
		if(typeof prot != 'undefined') ingrData.append('protein', prot);
		if(typeof fat != 'undefined') ingrData.append('fat', fat);
		if(typeof carbo != 'undefined') ingrData.append('carbohydrate', carbo);
		$.ajax({type: "POST", url: "/ingredient/edit", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingrData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {		
					successFinishFunction();
				} else{
					errorFinishFunction();
				}
			}, 
			error: function(respond, status, jqXHR){
				errorFinishFunction();
			}	
		});
	}

	delete(successFinishFunction, errorFinishFunction){
		var ingrData = new FormData();
		ingrData.append('id', this.id);
		$.ajax({type: "POST", url: "/ingredient/delete", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingrData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {		
					successFinishFunction();
				} else{
					errorFinishFunction();
				}
			}, 
			error: function(respond, status, jqXHR){
				errorFinishFunction();
			}	
		});
	}

	calculateCalorie(){
		this.calorie = Math.floor((this.fat*9 + this.carbo*4 + this.prot*4)*100)/100;
	}
}
