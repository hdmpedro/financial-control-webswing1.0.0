class WebswingHtmlPanelBrowserElement extends HTMLElement {

    _webswingWindow;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        this.iframe = document.createElement('iframe');
        this.iframe.style.width = '100%';
        this.iframe.style.height = '100%';
        this.iframe.style.outline = 'none';
        this.iframe.style.border = 'none';
        this.iframe.style.background = 'white';
        
        this.shadowRoot.appendChild(this.iframe);
    }

    static get observedAttributes() {
        return ['src'];
    }

    connectedCallback() {
        if (this.hasAttribute('src')) {
            this.url = this.getAttribute('src');
        }
    }
    
    attributeChangedCallback(name, oldValue, newValue) {
        if (name === 'src') {
            this.url = newValue;
        }
    }
    
    initWebswingWindow() {
        if (this._webswingWindow == null) {
            return;
        }

        this.iframe.onload = () => {
            this.iframeOnload();
        };

        this._webswingWindow.handleActionEvent = (name, data, binaryData) => {
            if (name == "changeSrc") {
                this.url = data;
            } else if (name == "injectHtml") {
                this.source = data;
            }
        }
    }

    iframeOnload() {
        try {
            this.iframe.contentWindow.webswingWindow = this._webswingWindow;
        } catch (e) {
            // ignore
            // if we get exception here then the loaded URL is not a same origin, in that case we don't need to set webswingWindow
        }
    }

    get url() {
        return this.iframe.src;
    }

    set url(url) {
        this.iframe.removeAttribute('srcdoc');
        this.iframe.src = url;
    }

    get source() {
        return this.iframe.srcdoc;
    }

    set source(source) {
        this.iframe.removeAttribute('src');
        this.iframe.srcdoc = source;
    }

    get webswingWindow() {
        return this._webswingWindow;
    }

    set webswingWindow(val) {
        this._webswingWindow = val;
        this.initWebswingWindow();
    }

}
  
customElements.define('ws-html-panel-browser', WebswingHtmlPanelBrowserElement);