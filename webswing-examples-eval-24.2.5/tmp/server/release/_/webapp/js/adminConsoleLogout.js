var baseUrl = getBaseUrl();
    
var webswingInstance0 = {
    options: {
        autoStart: false,
        connectionUrl: baseUrl,
        securityToken: getParam('securityToken'),
        realm: getParam('realm'),
        onReady: function(api) {
        	api.logout()
        }
    }
}

function testIfAuthenticated() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function () {
		if (xmlhttp.readyState == XMLHttpRequest.DONE) {
			var data;
			if (xmlhttp.status == 401 || xmlhttp.status == 403) {
				// not authorized redirect to baseurl
				window.location.href = baseUrl
			}
		}
	};
	xmlhttp.open("POST", baseUrl + "rest/refreshToken", true);
	xmlhttp.withCredentials = true;
	xmlhttp.send();
}

function getBaseUrl() {
    var baseUrl = document.location.origin + document.location.pathname;
    baseUrl = baseUrl.indexOf('adminConsoleLogout.html') > -1 ? baseUrl.split('adminConsoleLogout.html').join('') : baseUrl;
    return baseUrl;
}

function getParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
    return results == null ? null : decodeURIComponent(results[1]);
}

testIfAuthenticated();