$(document).ready(function() {
    var canvas = document.querySelector('canvas');
    var canvasZoom = 10; // multiplier
    canvas.width = 500;
    canvas.height = 141;
    canvas.style.width = canvas.width * canvasZoom;
    canvas.style.height = canvas.height * canvasZoom;
    var ctx = canvas.getContext('2d', {antialias: false});
    var canvasController = document.getElementById('canvas-controller');
    var canvasMouse = Array.from({length: 3}, i => i = false);
    var clickX = 0, clickY = 0, transX = -1250, transY = -350, deltaX = -1250, deltaY = -350, zoom = 0.5; // weird offsets to center pre-zoomed canvas
    var selectedColour = "null";
    var ping;

    function connect() {
        ws = new WebSocket('wss://localhost:' + location.port +'/place/events');
        $('#status').text("Connecting...");

        ws.addEventListener('message', function(event) {
            if(event.data.startsWith("r:")) {
                var image = new Image();
                image.onload = function() {
                    ctx.clearRect(0, 0, canvas.width, canvas.height)
                    ctx.drawImage(image, 0, 0);
                };
                image.src = event.data.substring(2, event.data.length)
                return;
            }
            update(event.data);
        });

        ws.addEventListener('open', function(event) {
            $('#status').text("Connected");
            ping = setInterval(function(){ send("ping"); }, 30000); // ping the server every 30 seconds to keep the connection alive
            send("r");
        });

        ws.addEventListener('error', function(event) {
            ws.close();
        });

        ws.addEventListener('close', function(event) {
            $('#status').text("Connection lost...");
            clearInterval(ping); // clear the ping interval to stop pinging the server after it has closed
            connect();
        });
    }

    function send(data) {
        if(ws.readyState == 0) {
            setTimeout(() => {
                send(data);
            }, 10);
            return;
        }
        if(ws.readyState == 1) {
            ws.send(data);
        }
    }

    function update(data) {
        if(data.startsWith("p:")) {
            var segment = data.substring(2, data.length).split(',');
            ctx.fillStyle = "#" + segment[2];
            ctx.fillRect(segment[0], segment[1], 1, 1); // x, y, w, h
            return;
        }
    }

    function getCanvasMouseRelativePosition(e) {
        var rect = canvas.getBoundingClientRect();
        var x = Math.floor(((e.clientX - rect.left)/canvasZoom) / zoom);
        x = (x == -1) ? 0 : x == canvas.width ? x-1 : x;
        var y = Math.floor(((e.clientY - rect.top)/canvasZoom) / zoom);
        y = (y == -1) ? 0 : y == canvas.height ? y-1 : y;
        return {
            x:x,
            y:y
        }
    }

    canvas.addEventListener('mousedown', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        if(selectedColour == "null" || zoom < 0.5 || e.button !== 0) { // null colour, <1 zoom, left click
            return;
        }
        send("p:" + p.x + "," + p.y + "," + selectedColour);
    });

    canvas.addEventListener('mousemove', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        $('#coords').text("X: "+p.x+", Y: "+p.y);
    });

    $('.colourSelector').on('click', function() {
        selectedColour = $(this).data('hex');
        $('a').removeClass('border-primary');
        $(this).addClass('border-primary');
    })

    canvasController.addEventListener('mousedown', (e) => {
        if(e.button !== 1 && e.button !== 2) { // middle click || right click
            return;
        }
        canvasMouse[e.button] = true;
        // store initial mouse location when translation begins
        clickX = e.clientX;
        clickY = e.clientY;
        // set origin of translation to final value of previous translation
        transX = deltaX;
        transY = deltaY;
    });

    canvasController.addEventListener('mouseup', (e) => {
        if(e.button !== 1 && e.button !== 2) { // middle click || right click
            return;
        }
        canvasMouse[e.button] = false;
    });

    canvasController.addEventListener('mousemove', (e) => {
        if(canvasMouse[1] || canvasMouse[2]) {
            // deltaX|Y is final translation value, where transX|Y is initial value
            deltaX = transX + (e.clientX - clickX);
            deltaY = transY + (e.clientY - clickY);
            $('#canvas-container').css('transform', 'translate('+deltaX+'px,'+deltaY+'px) scale('+zoom+')');
        }
    });

    canvasController.addEventListener('wheel', (e) => {
        if(e.deltaY !== 0) {
            var delta = (e.deltaY < 0) ? 0.1 : -0.1;
            var temp = zoom + delta;
            temp = (temp < 0.1) ? 0.1 : temp > 4.0 ? 4.0 : temp; // minimum 0.1, maximum 4
            zoom = Math.round(temp * 10) / 10; // deal with strange non-precise math
            $('#zoom').text("zoom: " + zoom + "x" + ((zoom < 0.5) ? " (drawing disabled)" : ""));
            $('#canvas-container').css('transform', 'translate('+deltaX+'px,'+deltaY+'px) scale('+zoom+')');
        }
    });

    connect();
})