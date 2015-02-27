<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>WebSocket</title>
    <script type="text/javascript">
        WEB_SOCKET_FORCE_FLASH = true;
    </script>
    <style>
        #tip {
            position: absolute;
            border: 1px solid gray;
            background-color: #efefef;
            padding: 3px;
            z-index: 1000;
            /* set this to create word wrap */
            max-width: 200px;
        }
    </style>
    <script type="text/javascript" src="${base}/static/js/raphael.js"></script>
    <script type="text/javascript" src="${base}/static/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="${base}/static/js/swfobject.js"></script>
    <script type="text/javascript" src="${base}/static/js/web_socket.js"></script>
    <script type="text/javascript" src="${base}/static/js/moment.js"></script>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${base}/static/js/es5-shim.min.js"></script>
    <script type="text/javascript" src="${base}/static/js/es5-sham.min.js"></script>
    <script type="text/javascript" src="${base}/static/js/console-polyfill-0.2.0.js"></script>
    <script type="text/javascript" src="${base}/static/js/html5shiv.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="${base}/static/js/react-with-addons.js"></script>
    <script type="text/javascript" src="${base}/static/js/microevent.js"></script>

    <script type="text/javascript">
        // Let the library know where WebSocketMain.swf is:
        var WEB_SOCKET_SWF_LOCATION = "${base}/static/js/WebSocketMain.swf";
        var WEB_SOCKET_URL = "ws://${request.getServerName()}:${request.getServerPort()?string("#####")}/ws_test/quote";
        var BASE_URL = "${base}";
        Raphael.st.removeAll = function () {
            this.forEach(function (el) {
                el.remove();
            });
        };
    </script>
</head>
<body>
</body>
<script type="text/javascript" src="${base}/static/js/trade_service.js?_t=${.now}"></script>
<script type="text/javascript" src="${base}/static/js/trade_chart.js?_t=${.now}"></script>
<script type="text/javascript" src="${base}/static/js/hello.js?_t=${.now}"></script>
</html>
