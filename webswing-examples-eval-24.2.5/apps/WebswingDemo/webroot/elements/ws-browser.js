class WebswingBrowserElement extends HTMLElement {

    _eventsManager;
    _url;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        
        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
        	div[part=root] {
				display: flex;
				flex-direction: column;
				width: 100%;
				height: 100%;
			}
        
            div[part=address] {
				display: flex;
                padding: 10px 5px 5px 5px;
            }
                
            div[part=address] input {
                flex-grow: 1;
            }
                
            div[part=tools] {
                display: flex;
                flex-direction: row;
                gap: 10px;
                padding: 5px;
            }

            div[part=preset] span {
                font-size: 12px;
            }

            div[part=note] {
				font-size: 12px;
                padding: 5px;
			}

            div[part=content] {
				flex-grow: 1;
                padding: 5px;
			}
			
            div[part=content] iframe {
				width: 100%;
				height: 100%;
				outline: none;
				border: none;
				background: white;
            }

            :host(.webswing-dragging) iframe {
                pointer-events: none;
            }
        `);

        this.shadowRoot.adoptedStyleSheets = [sheet];
        
        this.shadowRoot.innerHTML = `
            <div part="root">
                <div part="address">
                    <input type="text" placeholder="Type a URL..." />
                </div>
                <div part="tools">
                    <div part="preset">
                        <span>Preset:</span>
                        <select>
                            <option value="" disabled selected>Select an option</option>
                            <option value="https://www.youtube.com/embed/oQ2O6K5CUiY">Webswing YouTube video</option>
                            <option value="https://www.openstreetmap.org/export/embed.html?bbox=16.65458679199219%2C47.95452454972844%2C17.56095886230469%2C48.33525610652687&amp;layer=mapnik">Map</option>
                        </select>
                    </div>
                </div>
                <div part="content">
                    <iframe></iframe>
                </div>
                <div part="note">
                    Note that most of websites won't load due to X-Frame-Options header restrictions of the website's server or Content-Security-Policy of Webswing server configuration..
                </div>
            </div>
        `;
    }

    connectedCallback() {
        this.keyDownListener = (evt) => this.keyDownHandler(evt);
        this.presetChangeListener = (evt) => this.presetChangeHandler(evt);

        this.__getAddress().addEventListener('keydown', this.keyDownListener);
        this.__getPreset().addEventListener('change', this.presetChangeListener);
    }
    
    disconnectedCallback() {
        this.__getAddress().removeEventListener('keydown', this.keyDownListener);
        this.__getPreset().removeEventListener('change', this.presetChangeListener);
    }

    keyDownHandler(evt) {
        if (this._eventsManager == null) {
            return;
        }

        if (evt.key !== 'Enter') {
            return;
        }

        let newUrl = this.__getAddress().value;

        if (newUrl != null && !(newUrl.startsWith("http://") || newUrl.startsWith("https://"))) {
            newUrl = "https://" + newUrl;
        }

        this._eventsManager.performAction({
            actionName: 'urlChanged',
            data: newUrl,
            windowId: parseInt(this.getAttribute("data-id") || ""),
        });

        this.url = newUrl;
    }

    presetChangeHandler(_evt) {
        if (this._eventsManager == null) {
            return;
        }

        const newUrl = this.__getPreset().value;

        if (newUrl == null) {
            return;
        }

        this._eventsManager.performAction({
            actionName: 'urlChanged',
            data: newUrl,
            windowId: parseInt(this.getAttribute("data-id") || ""),
        });

        this.url = newUrl;
    }

    focus(options) {
        this.__getIframe().focus(options);
    }

    __getIframe() {
        return this.shadowRoot.querySelector('div[part=content] iframe');
    }

    __getPreset() {
        return this.shadowRoot.querySelector('div[part=preset] select');
    }

    __getAddress() {
        return this.shadowRoot.querySelector('div[part=address] input');
    }

    get eventsManager() {
        return this._eventsManager;
    }

    set eventsManager(val) {
        this._eventsManager = val;
    }

    get url() {
        return this._url;
    }

    set url(url) {
        if (url === this._url) {
            return;
        }
        this._url = url;
        this.__getIframe().src = url;
        this.__getAddress().value = url;
    }

    __preventDisposal() {
        return true;
    }

}
  
customElements.define('ws-browser', WebswingBrowserElement);