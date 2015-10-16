
(function ($) {
	"use strict";
	
	var _ES = {};
	
	$(document).ready(function() {
		
		$('#signPdf').click(_ES.signPdfClick);
		$('#listSignedPdfs').click(_ES.listSignedPdfsClick);
		
	});
	
	_ES.signPdfClick = function() {
		alert('TODO');
	};
	
	_ES.listSignedPdfsClick = function() {
		alert('TODO');
	};
	
	//Make this module globally accessible
	if(!window.EsGlobal) {
		window.EsGlobal = _ES;
	}
	
}(window.jQuery));
