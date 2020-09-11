function ipws_saveNewIngredient(){
    var name = $('#newIngrName').val();
    var type = $('#newIngrType').val();
    var descr = $('#newIngrDesription').val();
    var prot = $('#newIngrProt').val();
    var fat = $('#newIngrFat').val();
    var carbo = $('#newIngrCarbo').val();
    if(isNaN(prot) || isNaN(fat) || isNaN(carbo)){
        alert("Одно из введенных Вами значений БЖУ не является числом")
    }
    else{
        var ingr = new Ingredient("-", name, type, descr, prot, fat, carbo);
        ingr.save();
        if (ingr.isSaved()) {
            return 0;
        } else {
            return 1;
        }
    }
}