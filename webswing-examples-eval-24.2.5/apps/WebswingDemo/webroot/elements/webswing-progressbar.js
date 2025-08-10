class WebswingProgressBarElement extends HTMLElement  {

    _indeterminate;
    _value;
    _max;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            progress[part=root] {
                position: absolute;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <progress part="root" />
        `;
    }
    
    __getRootElement() {
        return this.shadowRoot.querySelector('progress[part=root]');
    }

    render() {
        if (this._indeterminate) {
            this.__getRootElement().removeAttribute("value");
        } else {
            this.__getRootElement().setAttribute("value", this._value || "");
            this.__getRootElement().setAttribute("max", this._max || "");
        }
    }

    get indeterminate() {
        return this._indeterminate;
    }

    set indeterminate(val) {
        this._indeterminate = val === 'true';
    }
    
    get value() {
        return this._value;
    }
    
    set value(val) {
        this._value = val == null ? "" : parseInt(val, 10);
    }
    
    get max() {
        return this._max;
    }
    
    set max(val) {
        this._max = val == null ? "" : parseInt(val, 10);
    }

    __shouldRender() {
        this.render();
    }

}

customElements.define('webswing-progressbar', WebswingProgressBarElement);