
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
	
	_ES.signPdf = function() {
		//This should be one ajax call that passes a list of docs, but I'm being lazy
		if($('#pdfAcheck').is(':checked')) {
			signPdfAjax('testsigA.pdf', 'sigField');
		}
		if($('#pdfBcheck').is(':checked')) {
			signPdfAjax('testsigB.pdf', 'sigField');
		}
	};
	
	function signPdfAjax(pdfName, fieldName) {
		return $.ajax({
			type: 'POST',
			url: _CONTEXT + "/index/signPdf",
			data: {
				document: pdfName,
				field: fieldName,
				signature: $('#signature').val(),
				email: $('#email').val()
			},
			beforeSend: function() {},
			complete: function() {
				$('#signatureModal').trigger('reveal:close');
			}
		});
	}
	
	//Make this module globally accessible
	if(!window.EsGlobal) {
		window.EsGlobal = _ES;
	}
	
}(window.jQuery));
