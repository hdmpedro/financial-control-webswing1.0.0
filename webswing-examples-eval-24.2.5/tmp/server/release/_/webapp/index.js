//fix for ie
if (!window.location.origin) {
    window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
}

var baseUrl = document.location.origin + document.location.pathname;
baseUrl = baseUrl.indexOf("/", baseUrl.length - 1) !== -1 ? baseUrl : (baseUrl + "/");

var webswingInstance0 = {
    options: {
        autoStart: true,
        connectionUrl: baseUrl,
        args: getParam('args'),
        recording: getParam('recording'),
        debugPort: getParam('debugPort'),
        recordingPlayback: getParam('recordingPlayback'),
        securityToken: getParam('securityToken'),
        realm: getParam('realm'),
        debugLog: getParam('debugLog')
    }
}

function getParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
    return results == null ? null : decodeURIComponent(results[1]);
}

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