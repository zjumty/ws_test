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
        TradeService.onQuoteData(function(data){
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

        return (
            React.createElement("table", null, 
                children
            )
        );
    }
});

var SymbolChart = React.createClass({displayName: "SymbolChart",
    render : function() {
        return (
            React.createElement("div", {id: "chart"})
        );
    }
});

// 交易App
var TradeWorkApp = React.createClass({displayName: "TradeWorkApp",

    componentDidMount: function(){
        TradeService.openChannel();
    },

    render: function () {
        return (
        	React.createElement("table", null, 
        		React.createElement("tr", null, 
                    React.createElement("td", null, 
                        React.createElement(SymbolList, null)
                    ), 
                    React.createElement("td", null, 
                        React.createElement(SymbolChart, null)
                    )
        		)
        	)
        );
    }
});

React.render(React.createElement(TradeWorkApp, null), document.body);
