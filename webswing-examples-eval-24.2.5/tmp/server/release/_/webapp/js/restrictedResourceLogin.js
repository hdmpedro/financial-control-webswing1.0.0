var baseUrl = getBaseUrl();
var successUrl = getSuccessUrl();

var webswingInstance0 = {
    options: {
        autoStart: false,
        connectionUrl: baseUrl,
        securityToken: getParam('securityToken'),
        realm: getParam('realm'),
        adminConsoleLogin: true,
        onReady: function(api) {
        	if (successUrl.startsWith(baseUrl)) {
        		window.location.href = successUrl;
        	}
        }
    }
}

function getSuccessUrl() {
	try {
        var successUrl = getParam("successUrl");
        if (successUrl.startsWith("http://") || successUrl.startsWith("https://")) {
        	return "";
        }
        return successUrl;
	} catch (e) {
		console.error(e);
	}
}

function getBaseUrl() {
	try {
        var baseUrl = getParam("baseUrl");
        if (baseUrl.startsWith("http://") || baseUrl.startsWith("https://")) {
        	return "";
        }
        if (!baseUrl.endsWith("/")) {
        	baseUrl += "/";
        }
        return document.location.origin + baseUrl;
	} catch (e) {
		console.error(e);
	}
}

function getParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
    return results == null ? null : decodeURIComponent(results[1]);
}