class WebswingButtonElement extends HTMLElement  {

    _text;
    // _icon;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            button[part=root] {
                position: absolute;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <button part="root"><slot></slot></button>
        `;
    }

    __getRootElement() {
        return this.shadowRoot.querySelector('button[part=root]');
    }

    focus(options) {
        this.__getRootElement().focus(options);
    }
    
    get disabled() {
        return this.__getRootElement().disabled;
    }

    set disabled(val) {
        this.__getRootElement().disabled = val === "true";
    }
    
    get text() {
        return this._text;
    }
    
    set text(val) {
        this._text = val;
        this.shadowRoot.querySelector('slot').textContent = this._text;
    }
    
        // updateIcon() {
        //     let imgIcon = this.querySelector('.img-icon');
    
        //     if (this._wsIcon == null) {
        //         if (imgIcon != null) {
        //             imgIcon.remove();
        //         }
        //         return;
        //     }
    
        //     if (imgIcon == null) {
        //         imgIcon = document.createElement('img');
        //         imgIcon.classList.add('img-icon');
        //         this.prepend(imgIcon);
        //     }
    
        //     imgIcon.src = this._wsIcon;
        // }
    
    // get icon() {
    //     return this._icon;
    // }
    
    // set icon(val) {
    //     if (this._icon == val) {
    //         return;
    //     }
    //     this._icon = val;
    //     // this.updateIcon();
    // }

}

customElements.define('webswing-button', WebswingButtonElement);