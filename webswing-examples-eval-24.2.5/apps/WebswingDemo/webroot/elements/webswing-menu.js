class WebswingMenuElement extends HTMLElement  {

    _text;
    _useArrow;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            div[part=root] {
                position: absolute;
                cursor: pointer;
                padding-left: 0.5rem;
                padding-right: 0.5rem;
                display: flex;
                align-items: center;
                justify-content: space-between;
                box-sizing: border-box;
            }
            span[part=arrow] {
                display: none;
            }
            span[part=arrow].use-arrow {
                display: block;
            }
            div[part=root][disabled],
            div[part=root][disabled] span[part=arrow] {
                color: #CACACA;
            }
            div[part=root][selected],
            div[part=root][selected] span[part=arrow] {
                color: white;
                background-color: #2675BF;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div part="root">
                <span part="slot"><slot></slot></span>
                <span part="arrow">&gt;</span>
            </div>
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
    
    get selected() {
        return this.__getRootElement().selected;
    }
    
    set selected(val) {
        this.__getRootElement().selected = val === "true";
        this.__getRootElement().toggleAttribute("selected", val === "true");
    }
    
    get useArrow() {
        return this._useArrow;
    }
    
    set useArrow(val) {
        this._useArrow = val === "true";
        this.__getRootElement().querySelector("span[part=arrow]").classList.toggle("use-arrow", val === "true");
    }
    
    get text() {
        return this._text;
    }
    
    set text(val) {
        this._text = val;
        this.shadowRoot.querySelector('slot').textContent = this._text;
    }
    
}

customElements.define('webswing-menu', WebswingMenuElement);