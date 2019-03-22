$(document).ready(function() {
	
	var id = getLastId();
	
	// start verification
	checkedUncheckedSecao($("#chkSecao").is(':checked'));
	
	$("#chkSecao").change(function() {
		checkedUncheckedSecao(this.checked);
	});
		
	$("#btnAddSecao").click(function() {
		id = id + 1;	
		listSecaoOrdenado = [];
		// VALIDATE
		var title = $("#secaoTitle").val();
		var body = $("#secaoBody").val();
		

		validateFielsSecao(title, body);
		if(title === "" || body === ""){
			return false;
		}
		
		var parent = Number($("#secaoParent").val());
		var seq = componentCountParentSeq(parent);		
		var secao = {"id": id, "paragrafo": "", seq, parent, title, body};
		
		listSecao.push(secao);		
				
		clearInputSecao();
		componentArvoreSecao1(0);
		componentComboboxSecao();
	});
	
	
	$(document).on('click', '.btnRemoveSecao', function(){	
		var idRemove = $(this).attr("idSecao");
		
		// Remove a secao e suas subsecoes
		componentRemoveSecao(idRemove);
		// Recria a arvore
		componentArvoreSecao1(0);
		componentComboboxSecao();
	});	
	
	function checkedUncheckedSecao(checked) {
		if (checked) {
			$("#divBody").css("display", "none");
			$("#divSecao").css("display", "block");
			$("#secaoTitle").focus();
		} else {
			$("#divSecao").css("display", "none");
			$("#divBody").css("display", "block");
			$("#title").focus();
		}
	}
	
	function componentComboboxSecao(){			
		var options = "<option value=\"0\" ></option>";
		for (var i = 0; i < listSecaoOrdenado.length; i++) {
			options += "<option value=\""+ listSecaoOrdenado[i].id +"\">" + listSecaoOrdenado[i].paragrafo +" " + listSecaoOrdenado[i].title +"</option>"
		}						
		$("#secaoParent").html(options);
	}	
	
	function clearInputSecao(){
		$("#secaoTitle").val("");
		$("#secaoBody").val("");
		$("#sesaoParent").val("0");
	}
	
	function componentCountParentSeq(parent){
		var count = 0;
		for (var i = 0; i < listSecao.length; i++) {
			if(parent === listSecao[i].parent){
				count++;
			}
		}
		
		return count + 1;
	}
	
	function getLastId(){
		if(listSecao == null){
			listSecao = [];
			return 0;
		}else{
			var myJSON = JSON.stringify(listSecao);		
			$("#secao").val(myJSON);

			componentComboboxSecao();
			var lastId = 0;
			for (var i = 0; i < listSecao.length; i++) {
			   if (Number(listSecao[i].id) > lastId ) {
				   lastId = Number(listSecao[i].id);
			   }
			}			
			
			return lastId;			
		}
	}

	
	var body = "";
	var paragrafoPai = "";
	var paragrafoFilhos = "";
	function componentArvoreSecao1(parent) {
		body = "<label>Seções:</label>";
		for (var i = 0; i < listSecao.length; i++) {						
			if(listSecao[i].parent === 0){
				listSecao[i].paragrafo = paragrafoPai = listSecao[i].seq;
				paragrafoFilhos = "";
				body += componentLinhaSecao(listSecao[i]);
				componentArvoreSecao2(listSecao[i].id);
			}		
		}
		
		var myJSON = JSON.stringify(listSecao);	
		if(myJSON === "[]")
			myJSON = "";
		
		$("#secao").val(myJSON);		
		$("#bodyTree").html(body);
		
		body = "";
		paragrafoPai = "";
		paragrafoFilhos = "";
	}
	
	function componentArvoreSecao2(parent) {		
		var paragrafo = paragrafoFilhos;
		parent = Number(parent);
		
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent === parent){						
				paragrafoFilhos += "." + listSecao[i].seq;
				listSecao[i].paragrafo = paragrafoPai + paragrafoFilhos;
				body += componentLinhaSecao(listSecao[i]);					
				componentArvoreSecao2(listSecao[i].id);
			}	
			
			paragrafoFilhos = paragrafo;
		}
	}	
	
	function componentRemoveSecao(idRemove) {		
		var ajusteSeq;
		
		for (var i = 0; i < listSecao.length; i++) {
			if(Number(listSecao[i].id) === Number(idRemove)){
				if(listSecao[i].parent === 0){
					ajusteSeq =listSecao[i].seq; 
				}
				
				listSecao.splice(i, 1);
				break;
			}			
		}
		
		for (var i = 0; i < listSecao.length; i++) {
			if(Number(listSecao[i].parent) === Number(idRemove)){
				componentRemoveSecao(listSecao[i].id)
			}
		}		
		
		componentAjustaSeq(ajusteSeq);
	}
	
	function componentAjustaSeq(seq){
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent === 0){
				if(listSecao[i].seq > seq){
					listSecao[i].seq = listSecao[i].seq - 1;
				}
			}
		}
	}
	
	function componentLinhaSecao(secao){
		var localBody = "";
		
		localBody += "<div class=\"col-sm-12 secaoPostForm\">";						
		localBody += "<h4><span>" + secao.paragrafo + ". </span>";
		localBody += "<span>" + secao.title + "</span>";		
		localBody += "<button idSecao=\"" + secao.id + "\" type=\"button\" class=\"btn btn-danger btn-sm btnRemoveSecao\">X</button>";
		localBody += "</h4></div>";
		
		// ORDERNA ARRAY
		listSecaoOrdenado.push(secao);
		
		return localBody;
	}
	
	function validateFielsSecao(title, body){				
		if(title === ""){
			$("#secaoTitleError").html("Campo não pode ser vazio").css("display","block");
		}else{
			$("#secaoTitleError").html("").css("display","none");
		}
		
		if(body === ""){
			$("#secaoBodyError").html("Campo não pode ser vazio").css("display","block");
		}else{
			$("#secaoBodyError").html("").css("display","none");
		}				
	}
	
});