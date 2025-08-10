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
        autoReconnect: 5000,
        syncClipboard: true,
        args: getParam('args'),
        recording: getParam('recording'),
        binarySocket: getParam('binarySocket'),
        debugPort: getParam('debugPort'),
        recordingPlayback: getParam('recordingPlayback'),
        securityToken: getParam('securityToken'),
        realm: getParam('realm'),
        debugLog: getParam('debugLog'),
        pingParams:{count : 6, interval : 1, maxLatency : 1000, notifyIf : 3},
        onStart: function() {
			webswingInstance0.performAction({actionName: "requestFileList", data: null, binaryData: null})
        },
        customization: function(injector) {
        	injector.services.base.handleActionEvent = function(actionName, data, binaryData) {
				if (actionName == 'fileList') {
					const nav = document.querySelector("#nav");
					nav.replaceChildren();
					if (data) {
						const files = data.split(";");
						for (let i=0; i<files.length; i++) {
							const file = document.createElement("div");
							file.textContent = files[i];
							file.style.cursor = "pointer";
							file.style.wordBreak = "break-all";
							file.addEventListener("click", (e) => {
								webswingInstance0.performAction({actionName: "fileSelected", data: files[i], binaryData: null})
							});
    						nav.appendChild(file);
						}
					}
				}
  			}
        },
        compositingWindowsListener: {
        	windowOpened: function(win) {
        		console.log("window " + win.id + " (" + win.name + ") opened");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        		
        		//if (win.name == "viewer-frame") {
        		if (win.name != "NbMainWindow") {
        			const winEl = win.detach();
        			win.attach(document.querySelector("#container #attach"));
        		} else {
        			const winEl = win.detach();
        			win.attach(document.querySelector("#hidden-container"));
        		}
        	},
        	windowOpening: function(win) {
        		console.log("window " + win.id + " opening");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        	},
        	windowClosing: function(win, evt) {
        		console.log("window " + win.id + " closing");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        		if (win.name == "internalIframe") {
        			evt.preventDefault();
        		}
        		if (win.name == "test123") {
        			evt.preventDefault();
        		}
        	},
        	windowClosed: function(win) {
        		console.log("window " + win.id + " closed");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        	},
        	windowDocked: function(win) {
        		console.log("window " + win.id + " docked");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        	},
        	windowUndocked: function(win) {
        		console.log("window " + win.id + " undocked");
        		console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
        	}
        }
    }
}

function focusWindow(winId) {
	var win = window.webswingInstance0.getWindowById(winId);
	if (!win) {
		return;
	}
	
	win.performAction({actionName: "focus"});
}

function findHtmlIframeDocument(id) {
	return document.querySelector("iframe#" + id).contentDocument;
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