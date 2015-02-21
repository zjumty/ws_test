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

    <script type="text/javascript">

        $(function () {
            if (Raphael.svg) {
                $("h1").text("SVG");
            } else if (Raphael.vml) {
                $("h1").text("VML");
            }

            // 画布宽
            var CANVAS_WIDTH = 800;
            // 画布高
            var CANVAS_HEIGHT = 400;

            // 网格的留白
            var GRID_MARGIN = {
                TOP: 1,
                LEFT: 1,
                RIGHT: 60,
                BOTTOM: 20
            };

            var GRID_WIDTH = CANVAS_WIDTH - GRID_MARGIN.RIGHT - GRID_MARGIN.LEFT;
            var GRID_HEIGHT = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM - GRID_MARGIN.TOP;

            // X轴刻度数量
            var AXIS_X_COUNT = 21;
            // Y轴刻度数量
            var AXIS_Y_COUNT = 11;

            // 画布
            var PAPER = Raphael("k-chart", CANVAS_WIDTH, CANVAS_HEIGHT);
            // 背景
            var ST_BACKGROUND = PAPER.set();
            // 网格，刻度
            var ST_GRID = PAPER.set();
            // x轴标签
            var ST_AXIS_X = PAPER.set();
            // y轴标签
            var ST_AXIS_Y = PAPER.set();
            // 画图层
            var ST_DRAW = PAPER.set();

            // X轴的点
            var SEQUENCE_X = function () {
                var seq = [];
                var x = GRID_MARGIN.LEFT + 1;
                for (var i = 0; i <= AXIS_X_COUNT; i++) {
                    seq.push(x);
                    x = x + (GRID_WIDTH / AXIS_X_COUNT);
                }
                // 修正
                seq[AXIS_X_COUNT] = CANVAS_WIDTH - GRID_MARGIN.RIGHT + 1;
                return seq;
            }();

            // Y轴的点
            var SEQUENCE_Y = function () {
                var seq = [];
                var y = GRID_MARGIN.TOP + 1;
                for (var i = 0; i <= AXIS_Y_COUNT; i++) {
                    seq.push(y);
                    y = y + (GRID_HEIGHT / AXIS_Y_COUNT);
                }
                // 修正
                seq[AXIS_Y_COUNT] = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM + 1;
                return seq;
            }();

            // 蜡烛的宽度
            var CANDLE_STICK_WIDTH = 15;

            // 时间坐标值
            var sequence_times = [];
            // 坐标能表示的最大价格值
            var max_price;
            // 坐标能表示的最小价格
            var min_price;

            // 画图模式
            var draw_mode = false;
            var draw_panel;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            /**
             * 添加背景
             */
            var addBackground = function () {
                ST_BACKGROUND.push(
                        PAPER.rect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT).attr({
                            "fill": "#000"
                        })
                );
            };

            /**
             * 添加网格
             */
            var addGridBox = function () {
                var el = PAPER.rect(GRID_MARGIN.LEFT + 1, GRID_MARGIN.TOP + 1, GRID_WIDTH, GRID_HEIGHT)
                        .attr({
                            "fill": "#000",
                            "stroke": "#FFF",
                            "stroke-width": "1"
                        });
                ST_GRID.push(el);
            };

            // 横向刻度线
            var addAxisXPoints = function () {
                for (var i = 0; i < SEQUENCE_X.length; i++) {
                    var x = SEQUENCE_X[i];
                    var y1 = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM + 2;
                    var y2 = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM + 6;
                    ST_GRID.push(PAPER.path(Raphael.format("M{0},{1}L{2},{3}", x, y1, x, y2))
                            .attr({
                                "stroke": "#FFF",
                                "stroke-width": "1"
                            }));
                }
            };


            // 纵向刻度线
            var addAxisYPoints = function () {
                for (var i = 0; i < SEQUENCE_Y.length; i++) {
                    var y = SEQUENCE_Y[i];
                    var x1 = CANVAS_WIDTH - GRID_MARGIN.RIGHT + 2;
                    var x2 = CANVAS_WIDTH - GRID_MARGIN.RIGHT + 6;
                    ST_GRID.push(PAPER.path(Raphael.format("M{0},{1}L{2},{3}", x1, y, x2, y))
                            .attr({
                                "stroke": "#FFF",
                                "stroke-width": "1"
                            }));
                }
            };

            // 添加辅助线
            var addReferenceLine = function () {
                for (var i = 1; i < SEQUENCE_Y.length - 1; i++) {
                    var x1 = GRID_MARGIN.LEFT + 2;
                    var x2 = CANVAS_WIDTH - GRID_MARGIN.RIGHT - 1;
                    var y = SEQUENCE_Y[i];
                    ST_GRID.push(PAPER.path(Raphael.format("M{0},{1}L{2},{3}", x1, y, x2, y))
                            .attr({
                                "stroke": "#778899",
                                "stroke-width": "1",
                                "stroke-dasharray": "-"
                            }));
                }
            };

            // 画板层
            var addDrawPanel = function () {
                draw_panel = PAPER.rect(GRID_MARGIN.LEFT + 1, GRID_MARGIN.TOP + 1, GRID_WIDTH, GRID_HEIGHT)
                        .attr({
                            "fill": "#000",
                            "fill-opacity": 0,
                            "stroke-width": 0
                        });
                draw_panel.toFront();
                var drawing_elem;
                var start_point;
                var drawing = false;
                draw_panel.mousedown(function (e) {
                    if (draw_mode) {
                        start_point = {
                            x: e.offsetX,
                            y: e.offsetY
                        };
                        drawing = true;
                        e.preventDefault();
                    }
                });
                $(document).mouseup(function () {
                    if (draw_mode && drawing) {
                        drawing = false;
                        start_point = null;
                        drawing_elem = null;
                    }
                });

                draw_panel.mousemove(function (e) {
                    if (draw_mode && drawing) {
                        var x1 = start_point.x;
                        var y1 = start_point.y;
                        var x2 = e.offsetX;
                        var y2 = e.offsetY;
                        if(!drawing_elem){
                            if(Math.abs(x1-x2) > 10 || Math.abs(y1-y2) > 10){
                                drawing_elem = PAPER.path(Raphael.format("M{0},{1}L{2},{3}", start_point.x, start_point.y, start_point.x, start_point.y)).attr({
                                    "stroke": "#00F",
                                    "stroke-width": "1"
                                });
                                ST_DRAW.push(drawing_elem);
                            } else {
                                return false;
                            }
                        }
                        drawing_elem.attr("path", Raphael.format("M{0},{1}L{2},{3}", x1, y1, x2, y2));
                        e.preventDefault();
                    }
                });
                ST_DRAW.push(draw_panel);
            };

            /**
             * 把报价转换为对应的Y轴坐标
             */
            var transformY = function (price) {
                var y1 = GRID_MARGIN.TOP;
                var y2 = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM;
                return (max_price - price) * (y2 - y1) / (max_price - min_price) + y1;
            };

            // 更新蜡烛图
            var updateCandleSticks = function (datas) {
                for (var i = 1; i < SEQUENCE_X.length - 1; i++) {
                    var x = SEQUENCE_X[i];
                    var data = datas[i - 1];

                    var left_top_x = x - CANDLE_STICK_WIDTH / 2;
                    var left_top_y = transformY(Math.max(data.open, data.close));
                    var right_bottom_x = x + CANDLE_STICK_WIDTH / 2;
                    var right_bottom_y = transformY(Math.min(data.open, data.close));
                    var high_y = transformY(data.high);
                    var low_y = transformY(data.low);

                    var el = PAPER.path(Raphael.format("M{0},{1}L{2},{3}L{4},{5}L{6},{7}L{8},{9}M{10},{11}L{12},{13}M{14},{15}L{16},{17}",
                            left_top_x, left_top_y,
                            right_bottom_x, left_top_y,
                            right_bottom_x, right_bottom_y,
                            left_top_x, right_bottom_y,
                            left_top_x, left_top_y,
                            x, high_y,
                            x, left_top_y,
                            x, low_y,
                            x, right_bottom_y));

                    el.attr({
                        "fill": data.open < data.close ? "#0F0" : "#F00",
                        "stroke": data.open < data.close ? "#0F0" : "#F00",
                        "stroke-width": "1"
                    });

                    el.mouseover((function (d) {
                        return function (e) {
                            if (draw_mode) return;

                            var tip = $("#tip");
                            tip.show();
                            tip.css("left", e.clientX + 20).css("top", e.clientY + 20);
                            tip.html('<table>'
                            + '<tr><th align="right">Open:</th><td>' + d.open.toFixed(5) + '</td></tr>'
                            + '<tr><th align="right">Close:</th><td>' + d.close.toFixed(5) + '</td></tr>'
                            + '<tr><th align="right">High:</th><td>' + d.high.toFixed(5) + '</td></tr>'
                            + '<tr><th align="right">Low:</th><td>' + d.low.toFixed(5) + '</td></tr>'
                            + '<tr><th align="right">Volume:</th><td>' + d.volume + '</td></tr>'
                            + '</tabel>');
                        }
                    })(data));

                    el.mousemove(function (e) {
                        if (draw_mode) return;

                        var tip = $("#tip");
                        tip.css("left", e.clientX + 20).css("top", e.clientY + 20);
                    });

                    el.mouseout(function () {
                        if (draw_mode) return;

                        var tip = $("#tip");
                        tip.hide();
                    });

                    ST_GRID.push(el);
                }
            };

            // 横向的label
            var updateAxisXLabels = function () {
                ST_AXIS_X.clear();
                for (var i = 0; i < sequence_times.length; i++) {
                    var x = SEQUENCE_X[i + 1];
                    var y = CANVAS_HEIGHT - GRID_MARGIN.BOTTOM + 12;
                    ST_AXIS_X.push(PAPER.text(x, y, sequence_times[i])
                            .attr({
                                "fill": "#FFF"
                            }));
                }
            };

            // 纵向的label
            var updateAxisYLabels = function () {
                var labels = [];
                var p = max_price;
                var step = (max_price - min_price) / AXIS_Y_COUNT;
                for (var j = 0; j <= AXIS_X_COUNT; j++) {
                    labels.push(p);
                    p = p - step;
                }
                labels.push(p);

                ST_AXIS_Y.clear();
                for (var i = 1; i < SEQUENCE_Y.length; i++) {
                    var x = CANVAS_WIDTH - GRID_MARGIN.RIGHT + 30;
                    var y = SEQUENCE_Y[i];
                    ST_AXIS_Y.push(PAPER.text(x, y, labels[i].toFixed(5))
                            .attr({
                                "fill": "#FFF"
                            }));
                }
            };

            /**
             * 更新坐标
             * @param datas
             */
            var updateCoordinate = function (datas) {
                if (datas.length == 0) {
                    return;
                }
                sequence_times = [];
                min_price = datas[0].low;
                max_price = datas[0].high;
                for (var i = 0; i < datas.length; i++) {
                    sequence_times.push(datas[i].time);
                    if (min_price > datas[i].low) {
                        min_price = datas[i].low;
                    }
                    if (max_price < datas[i].high) {
                        max_price = datas[i].high;
                    }
                }

                // 调整一下，让最大值和最小值比实际数据中的值宽泛一下。这样图形不会顶在上面或下面。
                var step = (max_price - min_price) / AXIS_Y_COUNT;
                min_price = min_price - step;
                max_price = max_price + step;

                updateAxisXLabels();
                updateAxisYLabels();
            };

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // 画图
            addBackground();
            addGridBox();
            addAxisXPoints();
            addAxisYPoints();
            addReferenceLine();
            addDrawPanel();

            $.ajax({
                url: "${base}/k-chart/data",
                method: 'get',
                type: 'json',
                headers: {
                    Accept: "application/json; charset=utf-8"
                },
                data: {
                    name: 'EURUSD',
                    from: '2015-01-20',
                    to: '2015-01-25'
                }
            }).done(function (datas) {
                updateCoordinate(datas);
                updateCandleSticks(datas);
                console.log(datas);
            });

            $("#live").click(function () {

            });

            var getImageData = function () {
                var data = {};
                var elements = [];
                PAPER.forEach(function (element) {
                    elements.push({type: element.type, attrs: element.attr()});

                });
                data.attrs = {
                    height: PAPER.height,
                    width: PAPER.width
                };
                data.elements = elements;
                return data;
            };

            $("#btn2").click(function () {
                $("#svg2").val(JSON.stringify(getImageData()));
                $(this).closest("form").submit();
            });

            $("#showobj").click(function () {
                $("#pagerText").val(JSON.stringify(getImageData()));
            });

            $("#draw").click(function () {
                draw_mode = $(this).prop("checked");
                if (draw_mode) {
                    draw_panel.toFront();
                }
            });

        });

    </script>
</head>
<body>
<div id="tip"></div>
<h1>SVG</h1>

<div id="k-chart"></div>
<button id="live">go live</button>
<button id="showobj">show objects</button>
<input type="checkbox" id="draw"/><label for="draw">Draw</label>

<form action="${base}/svg2" method="POST">
    <input type="hidden" id="svg2" name="svg"/>
    <button id="btn2">export png</button>
</form>
<br/>
<textarea id="pagerText" cols="100" rows="10"></textarea>
</body>
</html>
