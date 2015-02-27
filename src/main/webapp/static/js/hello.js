var SymboRow = React.createClass({displayName: "SymboRow",
	render : function() {
		return (
            React.createElement("tr", null, 
                React.createElement("td", null, this.props.data.name), 
                React.createElement("td", null, this.props.data.price)
            )
        );
	}
});

// 交易品种列表
var SymbolList = React.createClass({displayName: "SymbolList",

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
                React.createElement(SymboRow, {key: d.name, data: d})
            );
        });

        var style = {
            "float":"left"
        };

        return (
            React.createElement("table", {style: style}, 
                children
            )
        );
    }
});

var SymbolChart = React.createClass({displayName: "SymbolChart",

    componentDidMount: function(){
        TradeChart.init(this.refs.container.getDOMNode());
    },

    render : function() {
        var style = {
            //"float":"right"
        };

        return (
            React.createElement("div", {style: style}, 
                React.createElement("div", {id: "tip"}), 
                React.createElement("p", null, Raphael.svg ? "SVG" : Raphael.vml ? "VML" : "NOT SUPPORT"), 
                React.createElement("div", {id: "chart", ref: "container"})
            )
        );
    }
});

var SymbolChartControl = React.createClass({displayName: "SymbolChartControl",

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
            React.createElement("div", null, 
                React.createElement("button", {onClick: this.onLiveClick}, "go live"), 
                React.createElement("button", {id: "showobj", onClick: this.onShowObjClick}, "show objects"), 
                React.createElement("label", null, React.createElement("input", {type: "checkbox", id: "draw", onClick: this.onDrawClick}), "Draw"), 
                React.createElement("form", {action: BASE_URL + "/svg2", method: "POST", ref: "form"}, 
                    React.createElement("input", {type: "hidden", id: "svg2", name: "svg", value: this.state.svgContent}), 
                    React.createElement("button", {id: "btn2", onClick: this.onExportClick}, "export png")
                ), 
                React.createElement("br", null), 
                React.createElement("textarea", {id: "pagerText", cols: "100", rows: "10", value: this.state.svgContent})
            )
        );    
    }
});

// 交易App
var TradeWorkApp = React.createClass({displayName: "TradeWorkApp",

    componentDidMount: function(){
        TradeService.openChannel();
    },

    render: function () {
        var leftPanelStyle = {
            float: "right"
        };

        return (
        	React.createElement("div", null, 
                React.createElement(SymbolList, null), 
                React.createElement("div", {style: leftPanelStyle}, 
                    React.createElement(SymbolChart, null), 
                    React.createElement(SymbolChartControl, null)
                )
        	)
        );
    }
});

React.render(React.createElement(TradeWorkApp, null), document.body);
