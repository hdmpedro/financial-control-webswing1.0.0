class WebswingSliderElement extends HTMLElement  {

    _value;
    _orientation;
    _min;
    _max;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            input[part=root] {
                position: absolute;
            }
            input[part=root][orient=vertical] {
                appearance: slider-vertical;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <input type="range" part="root">
        `;
    }
    
    __getRootElement() {
        return this.shadowRoot.querySelector('input[part=root]');
    }

    focus(options) {
        this.__getRootElement().focus(options);
    }

    render() {
        this.__getRootElement().setAttribute("value", this._value != null ? this._value : "");
        this.__getRootElement().setAttribute("min", this._min != null ? this._min : "");
        this.__getRootElement().setAttribute("max", this._max != null ? this._max : "");
        if (this._orientation === "vertical") {
            this.__getRootElement().setAttribute("orient", "vertical");
        } else {
            this.__getRootElement().removeAttribute("orient", "vertical");
        }
    }

    get value() {
        return this._value;
    }
    
    set value(val) {
        this._value = val == null ? "" : parseInt(val, 10);
    }
    
    get min() {
        return this._min;
    }
    
    set min(val) {
        this._min = val == null ? "" : parseInt(val, 10);
    }
    
    get max() {
        return this._max;
    }
    
    set max(val) {
        this._max = val == null ? "" : parseInt(val, 10);
    }

    get disabled() {
        return this.__getRootElement().disabled;
    }

    set disabled(val) {
        this.__getRootElement().disabled = val === "true";
    }
    
    get orientation() {
        return this._orientation;
    }

    set orientation(val) {
        this._orientation = val;
    }

    __shouldRender() {
        this.render();
    }

}

customElements.define('webswing-slider', WebswingSliderElement);