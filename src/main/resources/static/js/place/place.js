$(document).ready(function() {
    var clickX = 0, clickY = 0, transX = -1250, transY = -700, deltaX = -1250, deltaY = -700, zoom = 0.5; // weird offsets to center pre-zoomed canvas
    var canvasZoom = 10; // multiplier
    var canvasMinZoomRatio = 0.1;
    var canvasMaxZoomRatio = 4.0;
    var canvasMinDrawRatio = 0.5;
    var canvas = document.querySelector('canvas');
        canvas.width = 500;
        canvas.height = 282;
        canvas.style.width = canvas.width * canvasZoom;
        canvas.style.height = canvas.height * canvasZoom;
    var ctx = canvas.getContext('2d', {antialias: false});
        ctx.textBaseline = 'middle';
        ctx.textAlign = 'center';
        ctx.font = '36px sans-serif';
    var canvasController = document.getElementById('canvas-controller');
    var canvasMouse = Array.from({length: 3}, i => i = false);
    var selectedColour = "null";
    var ping;
    var lastPixelUpdateLabel;
    var lastPixelUpdate = 0;
    var connectedClients = 0;
    $('#canvas-container').css({'width': '' + canvas.width * canvasZoom + 'px','height':'' + canvas.height * canvasZoom + 'px','transform': 'translate(' + deltaX + 'px,' + deltaY + 'px) scale(' + zoom + ')'});

    function connect() {
        ws = new WebSocket('wss://' + location.host + ':' + location.port + '/place/events');
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillText('connecting...', canvas.width/2, canvas.height/2);

        ws.onopen = function(event) {
            ping = setInterval(function(){ send("ping"); }, 30000); // ping the server every 30 seconds to keep the connection alive
            send("r");
        };

        ws.onerror = function(event) {
            ws.close();
        };

        ws.onclose = function(event) {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.fillText('connection lost...', canvas.width/2, canvas.height/2);
            clearInterval(ping); // clear the ping interval to stop pinging the server after it has closed
            connect();
        };

        ws.onmessage = function(event) {
            const data = event.data;

            if(data.startsWith("r:")) {
                var image = new Image();
                image.onload = function() {
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(image, 0, 0);
                };
                image.src = data.substring(2, data.length);
                return;
            }

            if(data.startsWith("c:")) {
                connectedClients = data.substring(2, data.length);
                $('.lead').text("Wipes comically large bead of sweat from forehead of " + connectedClients + " connected client" + ((connectedClients > 1) ? "s." : ".")); // neat right?
                return;
            }

            if(data.startsWith("u:")) {
                lastPixelUpdate = data.substring(2, data.length)*1; // need to *1 because value isn't parsed as a number
                $('#lastPixelUpdate').text("last update " + dayjs(lastPixelUpdate).fromNow()); // 3 separate js files for this functionality
                lastPixelUpdateLabel = setInterval(function(){$('#lastPixelUpdate').text("last update " + dayjs(lastPixelUpdate).fromNow())},1000);
                return;
            }

            if(data.startsWith("p:")) {
                lastPixelUpdate = new Date();
                var segment = data.substring(2, data.length).split(',');
                ctx.fillStyle = "#" + segment[2];
                ctx.fillRect(segment[0], segment[1], 1, 1); // x, y, w, h
                return;
            }
        };
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

    $('.colourSelector').on('click', function() {
        selectedColour = $(this).data('hex');
        $('a').removeClass('border-primary');
        $(this).addClass('border-primary');
    })

    canvas.addEventListener('mousedown', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        if(selectedColour == "null" || zoom < canvasMinDrawRatio || e.button !== 0) { // null colour, <zoom, left click
            return;
        }
        send("p:" + p.x + "," + p.y + "," + selectedColour);
    });

    canvas.addEventListener('mousemove', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        $('#xCord').text(p.x);
        $('#yCord').text(p.y);
    });

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
            temp = (temp < canvasMinZoomRatio) ? canvasMinZoomRatio : temp > canvasMaxZoomRatio ? canvasMaxZoomRatio : temp; // minimum 0.1, maximum 4
            zoom = Math.round(temp * 10) / 10; // deal with strange non-precise math
            $('#zCord').text(zoom + "x" + ((zoom < canvasMinDrawRatio) ? " (drawing disabled)" : ""));
            $('#canvas-container').css('transform', 'translate('+deltaX+'px,'+deltaY+'px) scale('+zoom+')');
        }
    });

    connect();
})