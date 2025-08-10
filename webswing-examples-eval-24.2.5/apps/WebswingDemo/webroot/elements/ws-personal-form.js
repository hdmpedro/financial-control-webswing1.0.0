class WsPersonalForm extends HTMLElement {

    _eventsManager;
    _formName;
    _formAge;
    _formAgeOptions;
    _formRadioYes;
    _formRadioNo;
    _formAgree;

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
    }

    focus(options) {
        if (document.activeElement != null && this.getRoot().contains(document.activeElement)) {
            // do not re-focus if active element is already inside this component
            return;
        }
        this.shadowRoot.querySelector('#form-name').focus(options);
    }

    connectedCallback() {
        if (!this.rendered) {
            const sheet = new CSSStyleSheet();
            sheet.replaceSync(`
                :host {
                    overflow-y: auto;
                    margin: auto;
                    -webkit-font-smoothing: antialiased;
                    -moz-osx-font-smoothing: grayscale;
                }
                .light-mode {
                    --primary-color: #0097e6;
                    --color-bg: #ebecf0;
                    --color-white: #fff;
                    --color-shadow: rgba(186, 190, 204, 0.9);
                    --main-text-color: #333;
                    --text-field-radius: calc(var(--ruler) * 1);
                    --check-radio-color: var(--primary-color);
                }
                .block-section {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
                    gap: 10px;
                    margin: .6rem 0;
                }
                .sul-text-field {
                    height: 38px;
                    color: #333;
                }
                #form-wrapper {
                    padding: 0.8rem;
                    background-color: white;
                    font-size: 12px;
                    height: 100%;
                    width: 100%;
                    position: absolute;
                }
            `);
            this.getRoot().adoptedStyleSheets = [sheet];

            this.getRoot().innerHTML = `
                <link rel="stylesheet" href="css/neumorphism-ui.css" />
                <div id="form-wrapper" class="container light-mode">
                    <article>
                        <div class="block-section">
                            <input id="form-name" type="text" class="sul-text-field" placeholder="Name" />
                            <label class="sul-select-wrapper" for="form-age">
                                <select id="form-age" name="select" class="sul-text-field"></select>
                            </label>
                        </div>
                        <div class="block-section">
                            <label>Gender</label>
                            <label for="form-gender-yes">
                                <input id="form-gender-yes" type="radio" class="sul-radio" name="gender"> Yes
                            </label>
                            <label for="form-gender-no">
                                <input id="form-gender-no" type="radio" class="sul-radio" name="gender"> No
                            </label>
                        </div>
                        <div class="block-section">
                            <label for="form-agree"><input id="form-agree" type="checkbox" role="switch" class="sul-checkbox-type-1">I agree</label>
                        </div>
                        <div class="block-section">
                            <a id="form-submit" class="sul-btn btn-primary" role="button">Submit</a>
                        </div>
                    </article>
                </div>
            `;
            this.render();
            this.rendered = true;
        }

        this.changeListener = (event) => this.changeHandler(event);
        this.submitClicked = (event) => this.submitHandler(event);

        this.getRoot().querySelector("#form-name").addEventListener('input', this.changeListener);
        this.getRoot().querySelector("#form-age").addEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-gender-yes").addEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-gender-no").addEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-agree").addEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-submit").addEventListener('click', this.submitClicked);
    }
    
    disconnectedCallback() {
        this.getRoot().querySelector("#form-name").removeEventListener('input', this.changeListener);
        this.getRoot().querySelector("#form-age").removeEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-gender-yes").removeEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-gender-no").removeEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-agree").removeEventListener('change', this.changeListener);
        this.getRoot().querySelector("#form-submit").removeEventListener('click', this.submitClicked);
    }

    render() {
        this.getRoot().querySelector("#form-name").value = this._formName || "";
        const formSelect = this.getRoot().querySelector("#form-age");
        while (formSelect.options.length > 0) {
            formSelect.remove(0);
        }
        if (this._formAgeOptions != null && Array.isArray(this._formAgeOptions)) {
            for (const opt of this._formAgeOptions) {
                const option = document.createElement("option");
                option.value = opt;
                option.text = opt;
                option.selected = opt == this._formAge;
                formSelect.appendChild(option);
            }
        }
        this.getRoot().querySelector("#form-gender-yes").checked = this._formRadioYes === true;
        this.getRoot().querySelector("#form-gender-no").checked = this._formRadioNo === true;
        this.getRoot().querySelector("#form-agree").checked = this._formAgree === true;
    }

    changeHandler(evt) {
        if (this._eventsManager == null) {
            return;
        }
        
        const data = {
            formName: this.getRoot().querySelector("#form-name").value,
            formAge: this.getRoot().querySelector("#form-age").value,
            formRadioYes: this.getRoot().querySelector("#form-gender-yes").checked,
            formRadioNo: this.getRoot().querySelector("#form-gender-no").checked,
            formAgree: this.getRoot().querySelector("#form-agree").checked
        }

        this.formName = data.formName;
        this.formAge = data.formAge;
        this.formRadioYes = data.formRadioYes;
        this.formRadioNo = data.formRadioNo;
        this.formAgree = data.formAgree;
        
        this._eventsManager.performAction({
            actionName: 'change',
            data: JSON.stringify(data),
            windowId: parseInt(this.getAttribute("data-id") || ""),
        });
    }

    submitHandler(evt) {
        alert('Form submitted!');
    }

    getRoot() {
        return this.shadowRoot;
        // return this;
    }

    get eventsManager() {
        return this._eventsManager;
    }

    set eventsManager(val) {
        this._eventsManager = val;
    }
    
    get formName() {
        return this._formName;
    }

    set formName(val) {
        this._formName = val;
    }
    
    get formAge() {
        return this._formAge;
    }

    set formAge(val) {
        this._formAge = val;
    }
    
    get formAgeOptions() {
        return this._formAgeOptions;
    }

    set formAgeOptions(val) {
        if (val != null && val.length > 0) {
            this._formAgeOptions = JSON.parse(val);
        }
    }
    
    get formRadioYes() {
        return this._formRadioYes;
    }

    set formRadioYes(val) {
        this._formRadioYes = JSON.parse(val || "false");
    }
    
    get formRadioNo() {
        return this._formRadioNo;
    }

    set formRadioNo(val) {
        this._formRadioNo = JSON.parse(val || "false");
    }
    
    get formAgree() {
        return this._formAgree;
    }

    set formAgree(val) {
        this._formAgree = JSON.parse(val || "false");
    }

    __shouldRender() {
        this.render();
    }

}

customElements.define('ws-personal-form', WsPersonalForm);