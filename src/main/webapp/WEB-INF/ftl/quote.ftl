<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>WebSocket</title>
    <script type="text/javascript">
        WEB_SOCKET_FORCE_FLASH = true;
    </script>
    <script type="text/javascript" src="${base}/static/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="${base}/static/js/swfobject.js"></script>
    <script type="text/javascript"
            src="${base}/static/js/web_socket.js?${.now?datetime?string('yyyyMMddHHmmss')}"></script>

    <script type="text/javascript">

        // Let the library know where WebSocketMain.swf is:
        WEB_SOCKET_SWF_LOCATION = "${base}/static/js/WebSocketMain.swf";
        var items = ['EURUSD', 'USDJPY', 'USDCNY', 'GBPUSD', 'XAUUSD', 'XAGUSD', 'AUDUSD', 'USDCHF', 'USDCAD', 'NDZUSD'];
        // Write your code in the same way as for native WebSocket:
        var ws = new WebSocket("ws://${request.getServerName()}:${request.getServerPort()?string("#####")}/ws_test/quote");
        ws.onopen = function () {
            var i = 0;
            ws.send(JSON.stringify({
                action: 'set_price_ids',
                data: items
            }));  // Sends a message.
        };
        ws.onmessage = function (e) {
            var data = $.parseJSON(e.data);
            if (data.result) {
                $("#msg").text(data.result);
            } else {
                for (var k in data) {
                    if (data.hasOwnProperty(k)) {
                        $("#" + k).text(data[k].toFixed(5));
                    }
                }
            }
        };
        ws.onclose = function () {
            alert("closed");
        };

        $(function () {
            for (var i = 0; i < items.length; i++) {
                $("#prices").append("<tr><th>" + items[i] + "</th><td id='" + items[i] + "'></td></tr>")
            }
        });

    </script>
</head>
<body>
WebSocket<br/>
Message: <span id="msg"></span>
<table id="prices">
</table>
</body>
</html>
