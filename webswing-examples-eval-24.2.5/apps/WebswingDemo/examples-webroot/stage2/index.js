//fix for ie
if (!window.location.origin) {
    window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
}

const getServerUrl = () => {
    const href = window.location.href;
    return href.substring(0, href.indexOf('/migration-examples')) + '/migration-examples';
}

var baseUrl = getServerUrl() + "/";

var webswingInstance0 = {
    options: {
        autoStart: true,
        connectionUrl: baseUrl,
        debugPort: getParam('debugPort'),
        customization: function(injector){
            // disable welcome back bar
			injector.services.dialog.content.continueOldSessionDialog = null;

            injector.services.base.handleActionEvent=function(actionName){
                if(actionName==='initialize'){
                    webswingInstance0.performAction({actionName:"switchStage",data:"2"})
                }
            }
        },
        compositingWindowsListener: {
            windowOpened: function(win) {
                if (win.htmlWindow && win.name == "map" && win.element.children.length == 0) {
                    var map = document.createElement("ws-map");
                    map.style.backgroundColor = "#dff6ff";
                    map.style.border = "1px solid #00a0db";
                    map.style.display = "block";
                    map.style.width = "100%";
                    map.style.height = "100%";
                    win.element.style.padding = "0.5rem";
                    win.element.appendChild(map);
                    map.addEventListener('click', (e)=>{//'regionClick', (e)=>{
                    	const country = e.composedPath()[0].getAttribute('data-name');
                        if (country) {
                            win.performAction({actionName:"filterString",data:country})
                        }
                    });
                    win.handleActionEvent = (evt,data)=>{
                        if(evt==='updateMapSelection'){
                            document.querySelector('ws-map').setAttribute("active", data)
                        }
                    }
                } else if (win.htmlWindow && win.name == "youtubeIframe" && win.element.children.length == 0) {
                    var container = document.createElement("div");
                    container.style.maxWidth = '45rem';

                    var wrapper = document.createElement("div");
                    wrapper.style.position = "relative";
                    wrapper.style.paddingBottom = "56.25%";

                    var iframe = document.createElement("iframe");
                    iframe.src = "https://www.youtube.com/embed/rBHBlICJMcU";
                    iframe.style.width = "100%";
                    iframe.style.height = "100%";
                    iframe.style.top = "0";
                    iframe.style.left = "0";
                    iframe.style.position = "absolute";
                    iframe.setAttribute("frameborder", 0);
                    iframe.setAttribute("allow", "fullscreen, accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture");
                    iframe.setAttribute("allowfullscreen", "allowfullscreen");
                    
                    win.element.style.overflow = "hidden";
                    win.element.style.backgroundColor = "#dff6ff";
                    win.element.style.border = "1px solid #00a0db";
                    win.element.style.padding = "0.5rem";

                    container.appendChild(wrapper);
                    wrapper.appendChild(iframe);
                    win.element.appendChild(container);
                }
            },
            windowOpening: function(win) {
            },
            windowClosing: function(win, evt) {
                if (win.name == "youtubeIframe") {
                    evt.preventDefault();
                }
                if (win.name == "map") {
                    evt.preventDefault();
                }
            },
            windowClosed: function(win) {
            },
        },
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