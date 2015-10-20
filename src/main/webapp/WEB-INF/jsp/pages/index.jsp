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
	
	<div id="footer" class="row">
		<div class="footer-panel">
			<div class="row collapse top">
				<div class="twelve columns">
					<ul class="footer-links no-bullet">
						<li>
							<a href="/fidelity_manuals/sales_manuals/policy/FNPRINE1.pdf" target="_blank">Privacy Policy</a>
						</li>
						<li>
							<a href="/fidelity_manuals/sales_manuals/policy/Security_Policy.pdf" target="_blank">Security Policy</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="row collapse bottom">
				<div class="two columns hide-for-small">
					<img src="${contextStatic}/stillwater/img/logo-footer.png" />
				</div>
				<div class="ten columns text-right copyright">
					<span class="small-font">&copy; ${year} Stillwater Insurance Group, Inc. All rights reserved.</span>
				</div>
			</div>
		</div>
	</div>
	
	<div id="pageLoadingModal" class="reveal-modal coverScreen">
		<div id="pageLoadingSpinner" class="loading-spinner"></div>
		<div class="text" id="loadingModalText">
		Loading, please wait...
		</div>
	</div>
	
	<div id="signatureModal" class="reveal-modal">
		<h5>Sign PDFs</h5>
		<div class="userInputForm">
			<ul class="no-bullet">
				<li>
					<input type="checkbox"/>
					<a href="${context}/resources/unsigned.pdf" target="_blank">PDF A</a>
				</li>
				<li>
					<input type="checkbox"/>
					<a href="${context}/resources/unsigned.pdf" target="_blank">PDF B</a>
				</li>
			</ul>
			<div class="row">
				<div class="two columns">
					<label class="inline">Name</label>
				</div>
				<div class="ten columns">
					<input type="text" id="signature" />
				</div>
			</div>
			<div class="row">
				<input type="button" class="button" value="Sign" onclick="EsGlobal.signPdf()"/>
				<input type="button" class="button close-reveal-modal" value="Cancel" />
			</div>
		</div>
	</div>

</body>

</html>