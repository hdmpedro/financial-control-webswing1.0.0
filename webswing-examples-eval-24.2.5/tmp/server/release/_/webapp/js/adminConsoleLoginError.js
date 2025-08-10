var baseUrl = getBaseUrl();

function getErrorMsg() {
	var msg = "An unexpected error occurred.";
	var error = getParam("error");
	if (error == "unauthorized") {
		msg = "You are not authorized to access this area.";
	} else if (error == "not_found") {
		msg = "Could not find Admin Console URL, please update your configuration.";
	} else if (error == "referrer") {
		msg = "You are trying to access Admin Console from URL that is not configured, please update your configuration.";
	}
}

function getBaseUrl() {
	//fix for ie
    if (!window.location.origin) {
        window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }
    var baseUrl = document.location.origin + document.location.pathname;
    baseUrl = baseUrl.indexOf('adminConsoleLoginError.html') > -1 ? baseUrl.split('adminConsoleLoginError.html').join('') : baseUrl;
    return baseUrl;
}

function getParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var results = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(location.href);
    return results == null ? null : decodeURIComponent(results[1]);
}

document.querySelector("#adminConsoleErrorDialog-description").innerHTML = getErrorMsg();
	
document.querySelector("#logoutButton").addEventListener("click", function() {
	window.location.href = getBaseUrl() + "logout";
});