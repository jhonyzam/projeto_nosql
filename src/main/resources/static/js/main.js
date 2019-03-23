$(document).ready(function() {
	
	var id = getLastId();
	var idEdit = null;
	var indexEdit = null;
	var objEditSecao = null;
	
	// Start verification
	checkedUncheckedSecao($("#chkSecao").is(':checked'));
	
	$("#chkSecao").change(function() {
		checkedUncheckedSecao(this.checked);
	});
		
	$("#btnAddSecao").click(function() {
		listSecaoOrdenado = [];
		// VALIDATE
		var title = $("#secaoTitle").val();
		var body = $("#secaoBody").val();
		

		validateFielsSecao(title, body);
		if(title == "" || body == ""){
			return false;
		}
						
		if(objEditSecao == null){
			id = id + 1;
			var parent = Number($("#secaoParent").val());
			var seq = componentCountParentSeq(parent);			
		}else{
			id = objEditSecao.id;
			var seq = objEditSecao.seq;
			var parent = objEditSecao.parent;
		}
						
		var secao = {"id": id, "paragrafo": "", seq, parent, title, body};
		
		if(objEditSecao == null){
			listSecao.push(secao);
		}else{
			listSecao[indexEdit] = secao;
			objEditSecao = null;
			indexEdit = null;
			idEdit = null;
		}				
				
		clearInputSecao();
		componentArvoreSecao1(0);
		componentComboboxSecao();
	});
	
	$(document).on('click', '.btnEditSecao', function(){	
		idEdit = $(this).attr("idSecao");
		
		for (var i = 0; i < listSecao.length; i++) {
			if(idEdit == listSecao[i].id){
				objEditSecao = listSecao[i];
				indexEdit = i;
				break;
			}
		}
		
		$("#secaoTitle").val(objEditSecao.title);
		$("#secaoBody").val(objEditSecao.body);
		$("#secaoParent").val(objEditSecao.parent).attr("disabled", true);
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
		$("#secaoParent").val("0").removeAttr("disabled");
	}
	
	function componentCountParentSeq(parent){
		var count = 0;
		for (var i = 0; i < listSecao.length; i++) {
			if(parent == listSecao[i].parent){
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
			if(listSecao[i].parent == 0){
				listSecao[i].paragrafo = paragrafoPai = listSecao[i].seq;
				paragrafoFilhos = "";
				body += componentLinhaSecao(listSecao[i]);
				componentArvoreSecao2(listSecao[i].id);
			}		
		}
		
		var myJSON = JSON.stringify(listSecao);	
		if(myJSON == "[]")
			myJSON = "";
		
		$("#secao").val(myJSON);		
		$("#bodyTree").html(body);
		
		body = "";
		paragrafoPai = "";
		paragrafoFilhos = "";
	}
	
	function componentArvoreSecao2(parent) {		
		var paragrafo = paragrafoFilhos;
		
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent == parent){						
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
			if(listSecao[i].id == idRemove){
				if(listSecao[i].parent == 0){
					ajusteSeq =listSecao[i].seq; 
				}
				
				listSecao.splice(i, 1);
				break;
			}			
		}
		
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent == idRemove){
				componentRemoveSecao(listSecao[i].id)
			}
		}		
		
		listSecaoOrdenado = [];
		componentAjustaSeq(ajusteSeq);
	}
	
	function componentAjustaSeq(seq){
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent == 0){
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
		localBody += "<button placeholder=\"Remover seção\"  idSecao=\"" + secao.id + "\" type=\"button\" class=\"btn btn-danger btn-sm btnRemoveSecao\">X</button>";
		localBody += "<button placeholder=\"Editar seção\"  idSecao=\"" + secao.id + "\" type=\"button\" class=\"btn btn-info btn-sm btnEditSecao\">E</button>";
		localBody += "</h4></div>";
		
		// ORDERNA ARRAY
		listSecaoOrdenado.push(secao);
		
		return localBody;
	}	
	
	function validateFielsSecao(title, body){
		if(title == ""){
			$("#secaoTitleError").html("Campo não pode ser vazio").css("display","block");
		}else{
			$("#secaoTitleError").html("").css("display","none");
		}
		
		if(body == ""){
			$("#secaoBodyError").html("Campo não pode ser vazio").css("display","block");
		}else{
			$("#secaoBodyError").html("").css("display","none");
		}				
	}
	
});