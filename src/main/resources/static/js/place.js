$(document).ready(function() {
    const canvas = document.querySelector('canvas');
    const ctx = canvas.getContext('2d', {antialias: false});
    var selectedColour = "ffffff";

    function connect() {
        ws = new WebSocket('ws://localhost:' + location.port +'/place/events');

        ws.addEventListener('message', function(event) {
            if(event.data.startsWith("retrieve:")) {
                var image = new Image();
                image.onload = function() {
                    ctx.drawImage(image, 0, 0);
                };
                image.src = event.data.substring(9, event.data.length)
                return;
            }
            update(event.data);
        });

        ws.addEventListener('open', function(event) {
            console.log("Connected to the server.");
            send("retrieve");
        });

        ws.addEventListener('close', function(event) {
            console.log("Connection closed, attempting to reconnect...");
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
        if(data.startsWith("place:")) {
            var segment = data.substring(6, data.length).split(',');
            ctx.fillStyle = "#" + segment[2];
            ctx.fillRect(segment[0], segment[1], 1, 1); // x, y, w, h
            return;
        }
    }

    function getCanvasMouseRelativePosition(e) {
        var rect = canvas.getBoundingClientRect();
        var x = Math.floor((e.clientX - rect.left)/5);
        var y = Math.floor((e.clientY - rect.top)/5);
        return {
            x:x,
            y:y
        }
    }

    canvas.addEventListener('mousedown', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        send("place:" + p.x + "," + p.y + "," + selectedColour);
    });

    canvas.addEventListener('mousemove', (e) => {
        var p = getCanvasMouseRelativePosition(e);
        $('#coords').text("x: "+p.x+", y: "+p.y);
    });

    $('.colourSelector').on('click', function() {
        selectedColour = $(this).data('hex');
        $('a').removeClass('border-primary');
        $(this).addClass('border-primary');
    })

    connect();
})