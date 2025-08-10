class WsAnimationPanel extends HTMLElement {

    _running;
    _speed;

    constructor() {
        super();
	    this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            .panel-wrapper {
                display: flex;
                flex-direction: column;
                row-gap: 1rem;
                height: 100%;
                width: 100%;
                position: absolute;
            }
            .image-wrapper {
                display: flex;
                flex-direction: column;
                justify-content: center;
                flex-grow: 1;
            }
            #anim-logo {
                align-self: center;
            }
            #anim-logo.running {
                animation-duration: 3s;
                animation-name: rotate;
                animation-iteration-count: infinite;
                animation-timing-function: linear;
                transition: transform 0.5s;
            }
            @keyframes rotate {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div class="panel-wrapper">
                <div class="image-wrapper">
                    <img src="images/logo.png" id="anim-logo" class="running" />
                </div>
            </div>
        `;
    }

    connectedCallback() {
        if (!this.rendered) {
            this.render();
            this.rendered = true;
        }
    }
    
    disconnectedCallback() {
    }

    render() {
        this.shadowRoot.querySelector("#anim-logo").classList.toggle("running", this._running);
        if (this._speed != null) {
            this.shadowRoot.querySelector("#anim-logo").style.animationDuration = (100 / this._speed) + "s";
        }
    }

    get running() {
        return this._running;
    }

    set running(val) {
        this._running = val == "true";
        this.render();
    }

    get speed() {
        return this._speed;
    }

    set speed(val) {
        this._speed = val == null ? null : parseInt(val, 10);
        this.render();
    }

}

customElements.define('ws-animation-panel', WsAnimationPanel);