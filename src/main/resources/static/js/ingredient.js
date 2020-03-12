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
				else{
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

	this.addToTable = function(id){
		var tbody = document.getElementById(id).getElementsByTagName("TBODY")[0];
		var row = document.createElement("TR");		
		var col1 = document.createElement("TD");
		col1.appendChild(document.createTextNode(this.name));
		var col2 = document.createElement("TD");
		col2.appendChild(document.createElement('input'));
		var col3 = document.createElement("TD");
		col3.appendChild(document.createElement('button'));		
		row.appendChild(col1);
		row.appendChild(col2);
		row.appendChild(col3);
		tbody.appendChild(row);    
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
				alert("Data from server: " + req.responseText);
			}
		}
		var body = "?name=" + this.name + "&type=" + this.type;
		req.open("GET", "/user/addIngrToList"+body);
		req.send();
	}

}