class WebswingPanelElement extends HTMLElement {

    _wsStyle;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            div[part=root] {
                position: absolute;
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

    get wsStyle() {
        return this._wsStyle;
    }

    set wsStyle(val) {
        this._wsStyle = val;
        this.__getRootElement().style.cssText = val || "";
    }

}

customElements.define('webswing-panel', WebswingPanelElement);