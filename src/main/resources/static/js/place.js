$(document).ready(function() {
    var selectedColour = "ffffff";

    function connect() {
        ws = new WebSocket('ws://localhost:' + location.port +'/place/events'); // 3.9.0.140
        ws.onmessage = function(data){
            update(data.data);
        }
        console.log("Connected to the server...");
    }

    function disconnect() {
        if(ws != null) {
            ws.close();
        }
        console.log("Disconnected from the server...");
    }

    function send() {
        ws.send();
    }

    function update(data) {
        if(data.startsWith("place:")) {
            data = data.substring(6, data.length);
            var segment = data.split(',');
            $('.pixel[data-x='+segment[0]+'][data-y='+segment[1]+']').css("backgroundColor","#"+segment[2])
            return;
        }
    }

    $('.pixel').on('click', function() {
        ws.send("place:"+$(this).data('x')+","+$(this).data('y')+","+selectedColour);
    })

    $('.colourSelector').on('click', function() {
        selectedColour = $(this).data('hex');
    })

    connect();
})