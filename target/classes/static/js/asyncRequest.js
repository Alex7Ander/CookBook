function asyncRequest(){
	try{
		var request = new XMLHttpRequest()
	}
	catch(exp1){
		try{
			request = new ActiveXObject("Msxml2.XMLHTTP")
		}
		catch(exp2){
			try{
				request = new ActiveXObject("Microsoft.XMLHTTP")
			}
			catch(exp3){
				request = false;
			}			
		}
	}
	return request
}