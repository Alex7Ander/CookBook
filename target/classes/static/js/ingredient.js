class ingredient{ 

	constructor(id, name, type, descr, prot, fat, carbo){
		this.id = id;
		this.name=name;
		this.type=type;
		this.descr=descr;
		this.prot=prot;
		this.fat=fat;
		this.carbo=carbo;
		this.calorie = this.fat*9 + this.carbo*4 + this.prot*4;
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

		var doneSuccessfully = false;
		var message = "";
		var tempId = "";
		$.ajax({type: "POST", url: "/ingredient/save", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {		
					doneSuccessfully = true;
					tempId = respond.id;				
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
				alert("Ошабка при получении данных с сервера:");
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
}
