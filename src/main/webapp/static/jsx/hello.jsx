var SymboRow = React.createClass({
	render : function() {
		return (
            <tr>
                <td>{this.props.data.name}</td>
                <td>{this.props.data.price}</td>
            </tr>
        );
	}
});

// 交易品种列表
var SymbolList = React.createClass({

    getInitialState: function() {
        return {
            symbols : []
        };
    },

    componentDidMount: function(){
        TradeService.bind("quote", function(data){
            var symbols = [];
            for (var k in data) {
                if (data.hasOwnProperty(k)) {
                    symbols.push({
                        name: k,
                        price : data[k].toFixed(5)
                    });
                }
            }
            this.setState({symbols: symbols}, null);
        }.bind(this));
    },

    render : function() {
        var children = this.state.symbols.map(function(d){
            return (
                <SymboRow key={d.name} data={d}/>
            );
        });

        var style = {
            "float":"left"
        };

        return (
            <table style={style}>
                {children}
            </table>
        );
    }
});

var SymbolChart = React.createClass({

    componentDidMount: function(){
        TradeChart.init(this.refs.container.getDOMNode());
    },

    render : function() {
        var style = {
            //"float":"right"
        };

        return (
            <div style={style}>
                <div id="tip"></div>
                <p>{Raphael.svg ? "SVG" : Raphael.vml ? "VML" : "NOT SUPPORT"}</p>
                <div id="chart" ref="container"></div>
            </div>
        );
    }
});

var SymbolChartControl = React.createClass({

    getInitialState: function() {
        return {svgContent : ""};
    },

    onLiveClick : function(event){
        TradeChart.livingOn();
        event.target.disabled = "disabled";
    },

    onShowObjClick : function(event) {
        this.setState({svgContent : JSON.stringify(TradeChart.getImageData())});
    },

    onDrawClick: function(event) {
        TradeChart.beginDraw($(event.target).prop("checked"));
    },

    onExportClick: function(event) {
        event.preventDefault();
        this.setState({svgContent : JSON.stringify(TradeChart.getImageData())}, function(){
            this.refs.form.getDOMNode().submit();
        }.bind(this));
    }, 

    render : function() {
        return (
            <div>
                <button onClick={this.onLiveClick}>go live</button>
                <button id="showobj" onClick={this.onShowObjClick}>show objects</button>
                <label><input type="checkbox" id="draw" onClick={this.onDrawClick}/>Draw</label>
                <form action={BASE_URL + "/svg2"} method="POST" ref="form">
                    <input type="hidden" id="svg2" name="svg" value={this.state.svgContent}/>
                    <button id="btn2" onClick={this.onExportClick}>export png</button>
                </form>
                <br/>
                <textarea id="pagerText" cols="100" rows="10" value={this.state.svgContent}/>
            </div>
        );    
    }
});

// 交易App
var TradeWorkApp = React.createClass({

    componentDidMount: function(){
        TradeService.openChannel();
    },

    render: function () {
        var leftPanelStyle = {
            float: "right"
        };

        return (
        	<div>
                <SymbolList/>
                <div style={leftPanelStyle}>
                    <SymbolChart/>
                    <SymbolChartControl/>
                </div>
        	</div>
        );
    }
});

React.render(<TradeWorkApp/>, document.body);
