class WebswingLabelElement extends HTMLElement  {

    _text;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            span[part=root] {
                position: absolute;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <span part="root"><slot></slot></span>
        `;
    }

    __getRootElement() {
        return this.shadowRoot.querySelector('span[part=root]');
    }

    get text() {
        return this._text;
    }
    
    set text(val) {
        this._text = val;
        this.shadowRoot.querySelector('slot').textContent = this._text;
    }

}

customElements.define('webswing-label', WebswingLabelElement);