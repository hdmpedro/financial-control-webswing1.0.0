class WebswingMenuItemElement extends HTMLElement  {

    _text;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            div[part=root] {
                position: absolute;
                cursor: pointer;
                padding-left: 0.5rem;
                display: flex;
                align-items: center;
            }
            div[part=root][disabled] {
                color: #CACACA;
            }
            div[part=root][selected] {
                color: white;
                background-color: #2675BF;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div part="root"><slot></slot></div>
        `;
    }

    __getRootElement() {
        return this.shadowRoot.querySelector('div[part=root]');
    }

    focus(options) {
        this.__getRootElement().focus(options);
    }
    
    get disabled() {
        return this.__getRootElement().disabled;
    }

    set disabled(val) {
        this.__getRootElement().disabled = val === "true";
        this.__getRootElement().toggleAttribute("disabled", val === "true");
    }
    
    get selected() {
        return this.__getRootElement().selected;
    }
    
    set selected(val) {
        this.__getRootElement().selected = val === "true";
        this.__getRootElement().toggleAttribute("selected", val === "true");
    }
    
    get text() {
        return this._text;
    }
    
    set text(val) {
        this._text = val;
        this.shadowRoot.querySelector('slot').textContent = this._text;
    }
    
}

customElements.define('webswing-menuitem', WebswingMenuItemElement);