function ingredient(name, type, descr, prot, fat, carbo){

	this.name=name;
	this.type=type;
	this.descr=descr;
	this.prot=prot;
	this.fat=fat;
	this.carbo=carbo;

	this.save = function() {
		var req = new asyncRequest();
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
		req.open("POST", "/user/saveIngredient", false);
		req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		req.send(body);
	}
	
	this.isSaved = function() {
		return saved;
	}

	this.getDataFromServer = function(name, type){
		this.name = name;
		this.type = type; 
		var req = new asyncRequest();
		let newObject;
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

		this.descr = newObject.descr;
		this.prot = newObject.prot;
		this.fat = newObject.fat;
		this.carbo = newObject.carbo;
	}

	this.getCalorificValue = function(){
		var calorificValue = this.fat*9 + this.carbo*4 + this.prot*4;
		return calorificValue;
	}
}
