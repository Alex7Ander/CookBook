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

	this.addToTable = function(tableId){
		var tbody = document.getElementById(tableId).getElementsByTagName("TBODY")[0];
		var row = document.createElement("TR");	

		var resultCalText = document.createElement('span');
		var volumeInput = document.createElement('input');
		volumeInput.oninput = function() {
			var resultCalValue = this.getCalorificValue() * volumeInput.value;
			resultCalText.innerHTML = "<b>" + resultCalValue + "</b>";
		}
		var deleteBtn = document.createElement('input')
		deleteBtn.type = 'button';
		deleteBtn.value= 'Удалить';
		deleteBtn.setAttribute('onclick', 'this.deleteFromTable()');

		var col1 = document.createElement("TD");
		col1.appendChild(document.createTextNode(this.name));

		var col2 = document.createElement("TD");
		col2.appendChild(document.createTextNode(this.getCalorificValue()));

		var col3 = document.createElement("TD");
		col3.appendChild(volumeInput);

		var col4 = document.createElement("TD");
		col4.appendChild(calText);

		var col5 = document.createElement("TD");		
		col5.appendChild(deleteBtn);	

		row.appendChild(col1);
		row.appendChild(col2);
		row.appendChild(col3);
		row.appendChild(col4);
		row.appendChild(col5);
		tbody.appendChild(row);    
	}

	this.deleteFromTable = function(){
		var ch = event.target.parentNode;
		ch.remove();
	}

	this.getDataFromServer = function(name, type){
		this.name = name;
		this.type = type; 
		var req = new asyncRequest();
		req.onreadystatechange = function() {
			if (req.readyState == 4) {
				if(!req.status == 200) {
					alert("Error: " + req.status);
				}
				let newObject =  JSON.parse(req.responseText);
			}
		}
		var body = "?name=" + this.name + "&type=" + this.type;
		req.open("GET", "/user/addIngrToList"+body);
		req.send();
	}

	this.getCalorificValue = function(){
		var calorificValue = this.fat*9 + this.carbo*4 + this.prot*4;
		return calorificValue;
	}
}
