$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// Set CSRF token in AJAX request header
// 	var token = $("meta[name='_csrf']").attr("content");
//    	var header = $("meta[name='_csrf_header']").attr("content");
//    $(document).ajaxSend(function(e, xhr, options){
//        xhr.setRequestHeader(header, token);
//    });

	// Get title and content
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// Send async request
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function(data) {
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);
			$("#hintModal").modal("show");
			setTimeout(function() {
				$("#hintModal").modal("hide");
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	)

	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}