// expects baseUrl global variable

(function (window, document) {
	var loader = function () {
		var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function () {
			if (xmlhttp.readyState == XMLHttpRequest.DONE) {
				var version = xmlhttp.status == 200 ? xmlhttp.responseText : "undefined";
				var script = document.createElement("script"),
						tag = document.getElementsByTagName("script")[0];
				script.src = baseUrl + "javascript/webswing-embed.js?version=" + version;
				tag.parentNode.insertBefore(script, tag);
			}
		};
		xmlhttp.open("GET", baseUrl + "rest/version", true);
		xmlhttp.send();
	};
	window.addEventListener ? window.addEventListener("load", loader, false) : window.attachEvent("onload", loader);
})(window, document);