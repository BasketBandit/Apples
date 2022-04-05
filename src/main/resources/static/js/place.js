$(document).ready(function() {
    var selectedColour = "000000";

    function connect() {
        ws = new WebSocket('ws://3.9.0.140:' + location.port +'/place/events');
        ws.onmessage = function(data){
            update(data.data);
        }
        console.log("Connected");
    }

    function disconnect() {
        if(ws != null) {
            ws.close();
        }
        console.log("Disconnected");
    }

    function send() {
        ws.send();
    }

    function update(data) {
        if(data.startsWith("place:")) {
            data = data.substring(6, data.length);
            var segment = data.split(',');
            $('.pixel[data-x='+segment[0]+'][data-y='+segment[1]+']').css("backgroundColor","#"+segment[2])
            console.log(segment);
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