var baseUrl = getBaseUrl();

var webswingInstance0 = {
	options: {
		autoStart: false,
		connectionUrl: baseUrl,
		securityToken: getParam('securityToken'),
		realm: getParam('realm'),
		adminConsoleLogin: true,
		useWindowTop: false,
		onReady: function(api) {
			var targetWindow = window.opener || window.top;
			api.readUnlockToken().then(function(unlockToken){
				targetWindow.postMessage({type:'webswing-unlock-token',value:unlockToken},baseUrl);
			}).catch(function(e){
				targetWindow.postMessage({type:'webswing-unlock-token',value:'"unlockTokenError:'+ e+'"'},baseUrl);
			})
		},
		customization: function(injector) {
			injector.services.webswing.configureFetchHeaders = function(headers){
				headers["X-webswing-challenge"] = getParam("challenge");
				return headers;
			}
		}
	}
}

function getBaseUrl() {
	var baseUrl = document.location.origin + document.location.pathname;
    baseUrl = baseUrl.indexOf('unlockSessionLogin.html') > -1 ? baseUrl.split('unlockSessionLogin.html').join('') : baseUrl;
    return baseUrl;
}

function getParam(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
	return results == null ? null : decodeURIComponent(results[1]);
}