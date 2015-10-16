<!DOCTYPE html>
<!--[if IE 8]><html class="no-js lt-ie9" lang="en" ><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js" lang="en" ><!--<![endif]-->

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width">
	<title>Stillwater e-sig</title>
	
	<!--style-->
	<link rel="stylesheet" href="${contextStatic}/foundation/stylesheets/foundation.min.css?t=${launchTimestamp}">
	<link rel="stylesheet" href="${contextStatic}/common/css/foundation-common.css?t=${launchTimestamp}">
	
	<!--Javascript-->
	<script type="text/javascript" src="${contextStatic}/foundation/javascripts/modernizr.foundation.js?t=${launchTimestamp}"></script>
	<script type="text/javascript" src="${contextStatic}/foundation/javascripts/foundation.min.js?t=${launchTimestamp}"></script>
	<script type="text/javascript" src="${contextStatic}/foundation/javascripts/app.js?t=${launchTimestamp}"></script>
	<script type="text/javascript" src="${contextStatic}/common/js/foundation-common.js?t=${launchTimestamp}"></script>
	<script type="text/javascript" src="${contextStatic}/common/js/spin.js?t=${launchTimestamp}"></script>
	<script type="text/javascript" src="${contextStatic}/common/js/jquery.spin.js?t=${launchTimestamp}"></script>
	
	<script type="text/javascript" src="${context}/resources/js/esig.js?t=${launchTimestamp}"></script>
</head>

<body>
	<div class="row">
		<h1>Electronic Signature</h1>
		<h4>Proof of Concept</h4>
	
		<ul>
			<li><a href="${context}/resources/unsigned.pdf" target="_blank">Unsigned PDF</a></li>
			<li><span class="linkStyle" id="signPdf">Sign a PDF</span></li>
			<li><span class="linkStyle" id="listSignedPdfs">List of Signed PDF's</span></li>
		</ul>
	</div>
</body>

</html>