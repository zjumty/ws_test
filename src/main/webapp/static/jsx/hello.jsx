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
                <SymboRow key={d.name} data={d}/>
            );
        });

        return (
            <table>
                {children}
            </table>
        );
    }
});

var SymbolChart = React.createClass({
    render : function() {
        return (
            <div id="chart"></div>
        );
    }
});

// 交易App
var TradeWorkApp = React.createClass({

    componentDidMount: function(){
        TradeService.openChannel();
    },

    render: function () {
        return (
        	<table>
        		<tr>
                    <td>
                        <SymbolList/>
                    </td>
                    <td>
                        <SymbolChart/>
                    </td>
        		</tr>
        	</table>
        );
    }
});

React.render(<TradeWorkApp/>, document.body);
