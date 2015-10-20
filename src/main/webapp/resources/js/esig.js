
(function ($) {
	"use strict";
	
	var _ES = {};
	
	var _CONTEXT = "/electronic-signature";
	
	$(document).ready(function() {
		
		$('#signPdf').click(_ES.signPdfClick);
		$('#listSignedPdfs').click(_ES.listSignedPdfsClick);
		
	});
	
	_ES.signPdfClick = function() {
		$('#signatureModal').reveal({});
	};
	
	_ES.listSignedPdfsClick = function() {
		alert('TODO');
	};
	
	_ES.signPdf = function() {
		return $.ajax({
			type: 'POST',
			url: _CONTEXT + "/index/signPdf",
			data: {
				document: 'testsigA.pdf',
				field: 'sigField',
				signature: $('#signature').val()
			},
			beforeSend: function() {},
			complete: function() {
				$('#signatureModal').trigger('reveal:close');
			}
		});
	};
	
	//Make this module globally accessible
	if(!window.EsGlobal) {
		window.EsGlobal = _ES;
	}
	
}(window.jQuery));
