class WebswingToolBarSeparatorElement extends HTMLElement {

    _orientation;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            div[part=root] {
                position: absolute;
                margin-top: 1px;
                margin-left: 0;
                border-top: 1px solid #CECECE;
                border-left: 0;
                box-sizing: border-box;
            }
            div[part=root].vertical {
                position: absolute;
                margin-top: 0;
                margin-left: 1px;
                border-left: 1px solid #CECECE;
                border-top: 0;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div part="root"></div>
        `;
    }

    __getRootElement() {
        return this.shadowRoot.querySelector('div[part=root]');
    }

    get orientation() {
        return this._orientation;
    }

    set orientation(val) {
        this._orientation = val;
        this.__getRootElement().classList.toggle("vertical", this._orientation === "vertical");
    }

}

customElements.define('webswing-toolbarseparator', WebswingToolBarSeparatorElement);