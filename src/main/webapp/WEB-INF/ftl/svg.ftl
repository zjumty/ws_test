<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>WebSocket</title>
    <script type="text/javascript">
        WEB_SOCKET_FORCE_FLASH = true;
    </script>
    <script type="text/javascript" src="${base}/static/js/raphael.js"></script>
    <script type="text/javascript" src="${base}/static/js/jquery-1.11.2.js"></script>

    <script type="text/javascript">

        $(function () {
            if (Raphael.svg) {
                $("h1").text("SVG");
            } else if (Raphael.vml) {
                $("h1").text("VML");
            }

            var paper = Raphael("image", 320, 200);
            paper.circle(100, 100, 50).attr({
                "stroke": "#F0F",
                "stroke-width": 5
            });
            paper.circle(150, 60, 40).attr({
                "stroke": "#0F0",
                "stroke-width": 5,
                "stroke-dasharray": "-.."
            });
            paper.path("M100,100L150,60").attr({
                "stroke": "#00F",
                "stroke-width": 2,
                "stroke-dasharray": "-.."
            });


            $("#btn").click(function () {
                if (Raphael.svg) {
                    $("#svg").val($("#image").html());
                } else {
                    var content = $("#image").html()
                            .replace(/<\?xml:namespace prefix = rvml ns = "urn:schemas-microsoft-com:vml" \/>/g, "")
                            .replace(/class=rvml/g, "")
                            .replace(/<DIV /g, "<DIV xmlns:rvml=\"urn:schemas-microsoft-com:vml\"  xmlns=\"http://www.w3.org/1999/xhtml\"");
                    $("#svg").val(content);
                }

                $("#type").val(Raphael.svg ? "svg" : Raphael.vml ? "vml" : "unknown");
                $(this).closest("form").submit();
            });

            $("#btn2").click(function () {
                $("#svg2").val(JSON.stringify(getImageData()));
                $(this).closest("form").submit();
            });

            var getImageData = function () {
                var data = {};
                var elements = [];
                paper.forEach(function (element) {
                    elements.push({type: element.type, attrs: element.attr()});

                });
                data.attrs = {
                    height: paper.height,
                    width: paper.width
                };
                data.elements = elements;
                return data;
            };

            $("#showobj").click(function () {
                $("#pagerText").val(JSON.stringify(getImageData()));
            });

        });

    </script>
</head>
<body>
<h1>SVG</h1>

<div id="image"></div>
<form action="${base}/svg" method="POST">
    <input type="hidden" id="svg" name="svg"/>
    <input type="hidden" id="type" name="type">
    <button id="btn">export png</button>
</form>
<form action="${base}/svg2" method="POST">
    <input type="hidden" id="svg2" name="svg"/>
    <button id="btn2">export png</button>
</form>
<button id="showobj">show objects</button>
<br/>
<textarea id="pagerText" cols="100" rows="10"></textarea>
</body>
</html>
