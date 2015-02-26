var TradeService = (function(){
    var ws = new WebSocket(WEB_SOCKET_URL);

    return {
        openChannel : function(){
            var items = ['EURUSD', 'USDJPY', 'USDCNY', 'GBPUSD', 'XAUUSD', 'XAGUSD', 'AUDUSD', 'USDCHF', 'USDCAD', 'NDZUSD'];
            // Write your code in the same way as for native WebSocket:
            ws.onopen = function () {
                var i = 0;
                ws.send(JSON.stringify({
                    action: 'set_price_ids',
                    data: items
                }));  // Sends a message.
            };
            ws.onclose = function () {
                alert("closed");
            };

        },

        onQuoteData : function(callback) {
            ws.onmessage = function (e) {
                var data = $.parseJSON(e.data);
                if (data.result) {
                    alert(data.result);
                } else {
                    callback(data);
                }
            };
        }

    };
})();