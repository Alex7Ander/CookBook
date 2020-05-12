class ingredient{ 

	constructor(name, type, descr, prot, fat, carbo){
		this.name=name;
		this.type=type;
		this.descr=descr;
		this.prot=prot;
		this.fat=fat;
		this.carbo=carbo;
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
		/*
		//var req = new asyncRequest();
		req.onreadystatechange = function() {
			if (req.readyState == 4) {
				if(req.responseText == '0'){
					saved = new Boolean(false);
				}
				else {
					saved = new Boolean(true);
				}			
				if(!req.status == 200) {
					alert("Error: " + req.status);
				}
			}
		}
		var body = "name=" + this.name + "&type=" + this.type + "&descr=" + this.descr + "&prot=" + this.prot + "&fat=" + this.fat + "&carbo=" + this.carbo;			
		req.open("POST", "/ingredient/saveIngredient", false);
		req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		req.send(body);
		*/
	}
	
	isSaved() {
		return this.saved;
	}

	getDataFromServer(name, type){
		this.name = name;
		this.type = type; 
		//var req = new asyncRequest();
		var newObject;
		var ingredientData = new FormData();
		ingredientData.append("type", this.type);
		ingredientData.append("name", this.name);	
		$.ajax({type: "GET", url: "/ingredient/getProperties", cache: false, contentType: false, processData : false, data: ingredientData,
			success: function(respond, status, jqXHR) {
				if (typeof respond.error === 'undefined') {	
					newObject =  JSON.parse(req.responseText);
					this.descr = newObject.descr;
					this.prot = newObject.prot;
					this.fat = newObject.fat;
					this.carbo = newObject.carbo;
				}
				else {
					alert("Error: " + status);
				}
			}, 
			error: function(respond, status, jqXHR) {
				//
			}
		});

/*
		req.onreadystatechange = function() {
			if (req.readyState == 4) {
				if(!req.status == 200) {
					alert("Error: " + req.status);
				}
				newObject =  JSON.parse(req.responseText);
			}
		}
		var body = "?name=" + this.name + "&type=" + this.type;
		req.open("GET", "/user/addIngrToList"+body, false);
		req.send();
*/
	}

	getCalorificValue(){
		var calorificValue = this.fat*9 + this.carbo*4 + this.prot*4;
		return calorificValue;
	}
}
