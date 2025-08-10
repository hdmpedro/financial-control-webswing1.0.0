//fix for ie
if (!window.location.origin) {
    window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
}

var baseUrl = document.location.origin + document.location.pathname;
baseUrl = baseUrl.indexOf("/", baseUrl.length - 1) !== -1 ? baseUrl : (baseUrl + "/");

var startTimestamp = new Date().getTime();

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
        customization: function(injector) {
            var taskbar = injector.services.taskbar;
            taskbar.config.enabled = true;
            taskbar.config.autoHide = true;
            //injector.services.canvas.canvasConfig.disposeOnDisconnect = false;

            injector.services.files.manager.getAcceptFilter = function(fileChooserName, filter) {
                if (fileChooserName === 'uploadFC') {
                    return "image/png";
                }
                return filter;
            };

            injector.services.files.manager.validateUploadedFile = function(fileChooserName, file) {
                if (fileChooserName === 'uploadFC') {
                    if (file.name.endsWith(".png") || file.type === 'image/png') {
                        return { valid: true };
                    }
                    return { valid: false, error: "Cannot upload file - " + file.name };
                }
                return { valid: true };
            };

            injector.services.base.handleActionEvent = function(actionName, data, binaryData) {
                if (actionName == "ApiDemoActionEventTest") {
                    console.log("sendActionEvent - " + data);
                    alert("sendActionEvent - " + data);
                }
            }
        },
        compositingWindowsListener: {
            windowOpened: function(win) {
            	if (startTimestamp > 0) {
            		console.warn('Time to first render: ' + (new Date().getTime() - startTimestamp) + ' ms');
            		startTimestamp = 0;
            	}
            	
                console.log("window " + win.id + " opened");
                console.log("Rendering " + window.webswingInstance0.getRenderedWindows().length + " windows");
                if (getParam('loadtest') === 'true') {
                    location.reload();
                }

                if (win.htmlWindow && win.name == "apiDemoHtmlPanelTest") {
                    if (win.element.getAttribute("ws-initialized") == "true") {
                        return;
                    }
                    win.element.setAttribute("ws-initialized", "true");

                    let apiDemoButton = document.createElement("button");
                    apiDemoButton.appendChild(document.createTextNode("This is HTML button. Click me!"));
                    apiDemoButton.style.margin = "25px";
                    apiDemoButton.addEventListener("click", function() {
                        win.performAction({actionName: "openApiDemoConfirmDialog"});
                    });

                    let apiDemoResult = document.createElement("div");
                    apiDemoResult.style.margin = "25px";
                    win.element.appendChild(apiDemoButton);
                    win.element.appendChild(apiDemoResult);

                    win.handleActionEvent = function(name, data, binaryData) {
                        if (name == "confirmApiDemoDialogResult") {
                            apiDemoResult.innerHTML = 'You have selected: "' + data + '"';
                        } else if (name == "sendActionEvent") {
                            console.log('Your input text: "' + data + '"');
                            apiDemoResult.innerHTML = 'Your input text: "' + data + '"';
                        }
                    }
                } else if (win.htmlWindow && win.name == "test123") {
                    if (win.element.getAttribute("ws-initialized") == "true") {
                        return;
                    }
                    win.element.setAttribute("ws-initialized", "true");

                    var button = document.createElement("button");
                    var btnValue = document.createTextNode("This is HTML button. Click me!");
                    button.appendChild(btnValue);
                    button.style.margin = "10px";
                    button.classList.add("focusable");
                    button.addEventListener("click", function() {
                        win.performAction({actionName: "openConfirmDialog"});
                        // win.callAction({ actionName: "openConfirmDialog" }).then(res => {
                        //     const data = res.data;

                        //     var resE = win.element.querySelector("#confirmDialogResult");
                        //     if (resE && resE.parentNode) {
                        //         resE.parentNode.removeChild(resE);
                        //     }

                        //     resE = document.createElement("div");
                        //     resE.id = "confirmDialogResult";
                        //     resE.appendChild(document.createTextNode('You have selected: "' + data + '"'));
                        //     win.element.appendChild(resE);
                        // });
                    });
                    win.handleActionEvent = function(name, data, binaryData) {
                        if (name == "confirmDialogResult") {
                            var resE = win.element.querySelector("#confirmDialogResult");
                            if (resE && resE.parentNode) {
                                resE.parentNode.removeChild(resE);
                            }

                            resE = document.createElement("div");
                            resE.id = "confirmDialogResult";
                            resE.appendChild(document.createTextNode('You have selected: "' + data + '"'));
                            win.element.appendChild(resE);
                        }
                    };
                    // win.handleActionCall = function(event) {
                    //     if (event.actionName == 'clearResult') {
                    //         win.element.querySelector("#confirmDialogResult").remove();
                    //         return "finished!";
                    //     }
                    // };

                    var input = document.createElement("input");
                    input.value = "this is a text";
                    input.style.margin = "10px";
                    input.classList.add("focusable");

                    var button2 = document.createElement("button");
                    var btnValue2 = document.createTextNode("This is another button. Click me!");
                    button2.appendChild(btnValue2);
                    button2.style.margin = "10px";
                    button2.classList.add("focusable");
                    /* button2.addEventListener("click", function() {
                        win.performAction({actionName: "openConfirmDialog"});
                    }); */

                    var name = document.createElement("span");
                    name.textContent = "test123";
                    name.style.margin = "10px";

                    win.element.appendChild(name);
                    win.element.appendChild(button);
                    win.element.appendChild(input);
                    win.element.appendChild(button2);

                    win.element.style.background = "white";
                    win.element.style.textAlign = "center";
                    win.element.style.overflow = "hidden";

                } else if (win.htmlWindow && win.name == "test456") {
                    var input = document.createElement("input");
                    input.value = "another text";
                    input.style.margin = "10px";
                    input.classList.add("focusable");

                    var name = document.createElement("span");
                    name.textContent = "test456";
                    name.style.margin = "10px";

                    win.element.appendChild(name);
                    //win.element.appendChild(input);

                    win.element.style.background = "white";
                    win.element.style.textAlign = "center";
                    win.element.style.overflow = "hidden";
                    win.element.style.border = "1px dotted black";
                } else if (win.htmlWindow && win.name == "testIframe") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "iframe.html";
                    iframe.width = "100%";
                    iframe.height = "100%";
                    win.element.appendChild(iframe);
                    win.element.style.background = "white";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "internalIframe_1") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "iframe_internal_1.html";
                    iframe.width = "100%";
                    iframe.height = "100%";
                    iframe.setAttribute("data-id", win.id);
                    win.element.appendChild(iframe);
                    win.element.style.background = "#00FF0099";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "internalIframe_2") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "iframe_internal_2.html";
                    iframe.width = "100%";
                    iframe.height = "100%";
                    iframe.setAttribute("data-id", win.id);
                    win.element.appendChild(iframe);
                    win.element.style.background = "#FF000099";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "window-internalIframe") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "iframe_internal.html";
                    iframe.width = "100%";
                    iframe.height = "100%";
                    iframe.setAttribute("data-id", win.id);
                    win.element.appendChild(iframe);
                    win.element.style.background = "white";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "fc-panel") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "file_chooser.html";
                    iframe.id = 'fc-panel';
                    iframe.width = "100%";
                    iframe.height = "100%";
                    iframe.style.border = "0";
                    win.element.appendChild(iframe);
                    win.element.style.background = "white";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "fe-panel") {
                    var iframe = document.createElement("iframe");
                    iframe.src = baseUrl + "file_explorer.html";
                    iframe.id = 'fe-panel';
                    iframe.width = "100%";
                    iframe.height = "100%";
                    iframe.style.border = "0";
                    win.element.appendChild(iframe);
                    win.element.style.background = "white";
                    win.element.style.overflow = "hidden";
                } else if (win.htmlWindow && win.name == "selected-demo-readme") {
                    win.element.appendChild(document.querySelector('#template-readme').content.cloneNode(true));
                    win.handleActionEvent = function(name, data, binaryData) {
                        if (name == "setCurrentDemoReadme") {
                            document.querySelector('#selected-demo-readme').innerHTML = marked.parse(data);
                        }
                    }
                    win.element.style.overflow = 'auto';
                    win.element.style.outline = 'none';
                } else if (win.htmlWindow && win.name == "selected-demo-source") {
                    win.element.appendChild(document.querySelector('#template-source').content.cloneNode(true));
                    win.handleActionEvent = function(name, data, binaryData) {
                        if (name == "setCurrentDemoSource") {
                            document.querySelector('#selected-demo-source').textContent = data;
                            hljs.highlightAll();
                        }
                    }
                    win.element.style.background = 'white';
                    win.element.style.overflow = 'auto';
                    win.element.style.outline = 'none';
                } else if (win.htmlWindow && win.name == "ws-html-panel-browser") {
                    const wsBrowser = document.createElement("ws-html-panel-browser");
                    win.element.appendChild(wsBrowser);
                    wsBrowser.webswingWindow = win;
                } else if (win.name == "callActionExample2") {
                    win.handleActionCall = function(options) {
                        if (options.actionName == "confirm") {
                            const res = confirm("Choose your answer");
                            setTimeout(function() {
                                win.callAction({actionName: "clear"}).then(clearRes => {
                                    console.log("Result from clear action:", clearRes.data);
                                });
                            }, 5000);
                            return res + "";
                        }
                    }
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
        },
        filesListener: {
            directTransferDownloadProgressAll: (downloadsCount, loadedAll, totalAll) => {
                var progress = findFileIframe().querySelector("#fc-progress");
                progress.style.display = 'block';
                progress.value = loadedAll;
                progress.max = totalAll;
            },
            directTransferDownloadAllDone: () => {
                var progress = findFileIframe().querySelector("#fc-progress");
                progress.value = 0;
                progress.style.display = 'none';
            },
            directTransferDownloadFailed: (reason) => {
                var errorDiv = findFileIframe().querySelector("#fc-errors");
                errorDiv.insertAdjacentHTML('beforeend', "<div>" + reason + "</div>");
            },
            fileUploadProgressAll: (loadedAll, totalAll) => {
                var progress = findFileIframe().querySelector("#fc-progress");
                progress.style.display = 'block';
                progress.value = loadedAll;
                progress.max = totalAll;
            },
            fileUploadDone: (fileName) => {
                var progress = findFileIframe().querySelector("#fc-progress");
                progress.value = 0;
                progress.style.display = 'none';
                
                webswingInstance0.getFilesManager().filesSelected([fileName]);

                const externalPanel = document.querySelector("#fcPanel");
                externalPanel.classList.add('hidden');
            },
            fileUploadFailed: (isAbort, fileNames, responseText) => {
                var errorDiv = findFileIframe().querySelector("#fc-errors");
                errorDiv.insertAdjacentHTML('beforeend', "<div>" + (responseText || "") + "</div>");
            },
            handleError: (msg) => {
                var errorDiv = findFileIframe().querySelector("#fc-errors");
                errorDiv.insertAdjacentHTML('beforeend', "<div>" + (msg || "") + "</div>");
            }
        },
		actions: {
			testActionString: (webActionEvent) => {
				console.log('testActionString');
				return new Promise((resolve, reject) => {
					setTimeout(() => {
						resolve(webActionEvent.data);
					}, 5000);
				});
			},
			testActionBinary: (webActionEvent) => {
				console.log('testActionBinary');
				return webActionEvent.binaryData;
			},
			testActionEmpty: () => {
				console.log('testActionEmpty');
			},
			testCallAction: (webActionEvent) => {
				console.log('calling testActionStringJS defined in app, the result should be received in 5 seconds');
				webswingInstance0.callAction({ actionName: "testActionStringJS", data: "testDataJS" }).then((webActionEvent) => {
					console.log('testActionStringJS -> result: ' + webActionEvent.data);
					console.log('calling testActionBinaryJS defined in app, the result should be received immediately');
					webswingInstance0.callAction({ actionName: "testActionBinaryJS", binaryData: new Uint8Array([1, 2, 3]) }).then((webActionEvent) => {
						console.log('testActionBinaryJS -> result: ' + webActionEvent.binaryData);
						console.log('calling testActionEmptyJS defined in app, the result should be received immediately');
						webswingInstance0.callAction({ actionName: "testActionEmptyJS" }).then((webActionEvent) => {
							console.log('testActionEmptyJS -> result: ' + ((webActionEvent.data == null && webActionEvent.binaryData == null) ? "<empty>" : "not empty!"));
						}).catch((e) => console.error(e));
					}).catch((e) => console.error(e));
				}).catch((e) => console.error(e));
			},
		}
    }
}

function findFileIframe() {
    const externalPanel = document.querySelector("#fcPanel");
    let panel = findHtmlIframeDocument("fc-panel") || findHtmlIframeDocument("fe-panel");
    if (panel != null) {
        externalPanel.classList.add('hidden');
        return panel;
    }
    externalPanel.classList.remove('hidden');
    return externalPanel;
}

function focusWindow(winId) {
    var win = window.webswingInstance0.getWindowById(winId);
    if (!win) {
        return;
    }

    win.performAction({actionName: "focus"});
}

function findHtmlIframeDocument(id) {
    const ifr = document.querySelector("iframe#" + id);
    return ifr ? ifr.contentDocument : null;
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