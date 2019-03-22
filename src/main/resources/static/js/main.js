$(document).ready(function() {
	
	var id = getLastId();
// var listSecao = [];
	
	// start verification
	checkedUncheckedSecao($("#chkSecao").is(':checked'));
	
	$("#chkSecao").change(function() {
		checkedUncheckedSecao(this.checked);
	});
		
	$("#btnAddSecao").click(function() {
		id = id + 1;	
		
		var parent = Number($("#secaoParent").val());
		var seq = componentCountParentSeq(parent);
		var title = $("#secaoTitle").val();
		var body = $("#secaoBody").val();
		var sessao = {"id": id, "paragrafo": "", seq, parent, title, body};
		
		listSecao.push(sessao);		
		
		componentComboboxSecao();
		clearInputSessao();
		componentArvoreSessao1(0);		
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
		for (var i = 0; i < listSecao.length; i++) {
			options += "<option value=\""+ listSecao[i].id +"\">" + listSecao[i].title +"</option>"
		}
						
		$("#secaoParent").html(options);
	}	
	
	function clearInputSessao(){
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
			return Number(listSecao[listSecao.length -1].id);			
		}
	}

	
	var body = "";
	var paragrafoPai = "";
	var paragrafoFilhos = "";
	function componentArvoreSessao1(parent) {
		body = "<label>Seções:</label>";
		for (var i = 0; i < listSecao.length; i++) {						
			if(listSecao[i].parent === 0){
				listSecao[i].paragrafo = paragrafoPai = listSecao[i].seq;
				paragrafoFilhos = "";
				body += componentLinhaSecao(listSecao[i].paragrafo, listSecao[i].title);
				componentArvoreSessao2(listSecao[i].id);
			}		
		}
		
		var myJSON = JSON.stringify(listSecao);		
		$("#secao").val(myJSON);
		$("#bodyTree").html(body);
	}
	
	function componentArvoreSessao2(parent) {		
		var paragrafo = paragrafoFilhos;
		parent = Number(parent);
		
		for (var i = 0; i < listSecao.length; i++) {
			if(listSecao[i].parent === parent){						
				paragrafoFilhos += "." + listSecao[i].seq;
				listSecao[i].paragrafo = paragrafoPai + paragrafoFilhos;
				body += componentLinhaSecao(listSecao[i].paragrafo, listSecao[i].title);					
				componentArvoreSessao2(listSecao[i].id);
			}	
			
			paragrafoFilhos = paragrafo;
		}
	}	
	
	function componentLinhaSecao(paragrafo, title){
		var localBody = "";
		
		localBody += "<div class=\"col-sm-12 secaoPostForm\">";						
		localBody += "<h4><span>" + paragrafo + ". </span>";
		localBody += "<span>" + title + "</span></h4></div>";
		
		return localBody;
	}
});



