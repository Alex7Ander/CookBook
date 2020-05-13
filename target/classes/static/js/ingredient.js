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
		$.ajax({type: "POST", url: "/ingredient/save", async:false, cache: false, dataType: 'json', contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {	
					this.saved = true;
				}
				else {
					this.saved = false;
				}
			}, 
			error: function(respond, status, jqXHR) {
				this.saved = false;
			}
		});
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
		var recipeInfo = JSON.parse(response.responseText);
		this.id = recipeInfo.id;
		this.descr = recipeInfo.descr;
		this.prot = recipeInfo.prot;
		this.fat = recipeInfo.fat;
		this.carbo = recipeInfo.carbo;
		this.calorie = recipeInfo.calorie;
	}
}
