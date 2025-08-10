class WebswingRadioButtonMenuItemElement extends HTMLElement  {

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
            div[part=root] div[part=check-mark] {
                visibility: hidden;
                height: 100%;
                aspect-ratio: 1;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            div[part=root] div[part=check-mark] .dot {
                height: 40%;
                width: 40%;
                background-color: #bbb;
                border-radius: 50%;
            }
            div[part=root][checked] div[part=check-mark] {
                visibility: visible;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div part="root">
                <div part="check-mark">
                    <div class="dot"></div>
                </div>
                <slot></slot>
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

    get checked() {
        return this.__getRootElement().checked;
    }
    
    set checked(val) {
        this.__getRootElement().checked = val === "true";
        this.__getRootElement().toggleAttribute("checked", val === "true");
    }
    
    get text() {
        return this._text;
    }
    
    set text(val) {
        this._text = val;
        this.shadowRoot.querySelector('slot').textContent = this._text;
    }
    
}

customElements.define('webswing-radiobuttonmenuitem', WebswingRadioButtonMenuItemElement);