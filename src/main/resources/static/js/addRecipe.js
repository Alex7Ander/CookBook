function getIngrListFromServer(){
    req = new asyncRequest();
    element = document.getElementById('ingrList');
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
            element.innerHTML = req.responseText;
            if(!req.status == 200) {
                alert("Error: " + req.status);
            }
        }
    }
    var select = document.getElementById("ingrType");
    var selectedValue = select.value;
    var body = "?type=" + selectedValue;			
    req.open("GET", "/user/getIngrList" + body);
    req.send();
    element.innerHTML = "Список ингредиентов загружается...";
}

function saveNewIngredient(){
    var name = document.getElementById("newIngrName").value;
    var type = document.getElementById("newIngrType").value; 
    var descr = document.getElementById("newIngrDesription").value;
    var prot = document.getElementById("newIngrProt").value;
    var fat = document.getElementById("newIngrFat").value;
    var carbo = document.getElementById("newIngrCarbo").value;
    var ingr = new ingredient(name, type, descr, prot, fat, carbo);
    ingr.save();
    if (ingr.isSaved() == true) {
        ingr.addToTable('ingrTable');
        PopUpHide('add_ingr_popup');
    }
}

function addToTableExistingIngr(){
    var ingr = new ingredient();
    var selectType = document.getElementById("ingrType");
    var cType = selectType.value;
    var selectName = document.getElementById("ingrName");
    var cName = selectName.value;
    ingr.getDataFromServer(cName, cType);
    ingr.addToTable('ingrTable');
    PopUpHide('add_ingr_popup');		
}

function sendPhoto(){
    var req = new asyncRequest();
    req.onreadystatechange = function() {
        if (req.readyState == 4) {			
            if(req.status != 200) {
                alert("Error: " + req.status);
            }
        }
    }
    var inputFile = document.getElementById('photoLoader');
    var body = "photo=" + inputFile.value;	
    //var boundary = String(Math.random()).slice(2);
    //var boundaryMiddle = '--' + boundary + '\r\n';
    //var boundaryLast = '--' + boundary + '--\r\n';

    //var formData = new FormData();
    //formData.append("photo", inputFile.value);
    req.open("Get", "/user/addRecipePhoto?"+body, false);
    //req.setRequestHeader('Content-Type', 'multipart/form-data boundary=' + boundary); //  application/x-www-form-urlencoded
    req.send();
    //element.innerHTML = "Список ингредиентов загружается...";
}

function uploadFileToPage(event) {
    var output  = document.getElementById('uploadedPhoto');
    output.src = URL.createObjectURL(event.target.files[0]);
  };