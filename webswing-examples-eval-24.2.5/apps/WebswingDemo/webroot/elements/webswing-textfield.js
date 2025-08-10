class WebswingTextFieldElement extends HTMLElement {

    _eventsManager;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            input[part=root] {
                position: absolute;
                box-sizing: border-box;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <input part="root">
        `;
    }

    __getRootElement() {
        return this.shadowRoot.querySelector('input[part=root]');
    }

    focus(options) {
        this.__getRootElement().focus(options);
    }

    connectedCallback() {
        this.inputListener = (evt) => this.inputHandler(evt);
        this.__getRootElement().addEventListener('input', this.inputListener);
    }
    
    disconnectedCallback() {
        this.__getRootElement().removeEventListener('input', this.inputListener);
    }

    inputHandler(_evt) {
        if (this._eventsManager == null) {
            return;
        }

        this._eventsManager.performAction({
            actionName: 'input',
            data: this.__getRootElement().value,
            windowId: parseInt(this.getAttribute("data-id") || ""),
        });
    }
    
    get eventsManager() {
        return this._eventsManager;
    }

    set eventsManager(val) {
        this._eventsManager = val;
    }

    get value() {
        return this.__getRootElement().value;
    }

    set value(val) {
        this.__getRootElement().value = val == null ? "" : val;
    }

    get disabled() {
        return this.__getRootElement().disabled;
    }

    set disabled(val) {
        this.__getRootElement().disabled = val === "true";
    }

    get readOnly() {
        return this.__getRootElement().readOnly;
    }

    set readOnly(val) {
        this.__getRootElement().readOnly = val === "true";
    }
    
}

customElements.define('webswing-textfield', WebswingTextFieldElement);