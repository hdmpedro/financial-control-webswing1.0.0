class WsChart extends HTMLElement {

    _eventsManager;
    _data;

    constructor() {
        super();
	    this.attachShadow({ mode: 'open' });

        const sheet = new CSSStyleSheet();
        sheet.replaceSync(`
            .chart-wrapper {
                display: flex;
                height: 100%;
                width: 100%;
                position: absolute;
            }
            #chartdiv {
                width: 100%;
                height: 100%;
            }
        `);
        this.shadowRoot.adoptedStyleSheets = [sheet];

        this.shadowRoot.innerHTML = `
            <div class="chart-wrapper">
                <div id="chartdiv">
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
        const chartRoot = this.shadowRoot.querySelector("#chartdiv");
        if (this._data == null || this._data.data == null || this._data.data.length == 0) {
            chartRoot.innerHTML = "";
            return;
        }

        const chartData = this._data;
        am4core.ready(function() {
            am4core.useTheme(am4themes_animated);
            am4core.useTheme(am4themes_material);

            var chart = am4core.create(chartRoot, am4charts.PieChart3D);
            chart.hiddenState.properties.opacity = 0;

            chart.legend = new am4charts.Legend();
            chart.data = chartData.data;
            
            var series = chart.series.push(new am4charts.PieSeries3D());
            series.dataFields.value = chartData.value;
            series.dataFields.category = chartData.category;
        });
    }

    get eventsManager() {
        return this._eventsManager;
    }

    set eventsManager(val) {
        this._eventsManager = val;
    }
    
    get data() {
        return this._data;
    }

    set data(val) {
        this._data = val == null ? null : JSON.parse(val);
        this.render();
    }

}

customElements.define('ws-chart', WsChart);