$(document).ready(function() {
    const canvas = document.querySelector('canvas');
    const ctx = canvas.getContext('2d', {antialias: false});
    var selectedColour = "ffffff";

    function connect() {
        ws = new WebSocket('ws://localhost:' + location.port +'/place/events');  // 3.9.0.140

        var connecting = new Image();
        connecting.onload = function() {
            ctx.clearRect(0, 0, canvas.width, canvas.height)
            ctx.drawImage(connecting, 0, 0);
        };
        connecting.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAACNCAYAAACey2dEAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAS1SURBVHhe7dpbqG1VAcbxZUpJKpFZmlmZJZpmUllWgmX2UEg38VLkLctANFGKLiBBPfpihFhv9VRSDxqFJmqQmpAakZdMi0zxBkV01dJKv2+tOU6T3TmeI+zDsc3vB3/mnGvtvdfanDPWGHPOvQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIDt4fj0ktUusBG8Lx282l16dXoynb88AjaE36UvrHY3eeO05f/UztOW9XdMOjW9Iz0ndQANx6UPp2PTS9Mdabgg7Zr6+FnpiHRX+keqzrafSDemT6YT0h7pV2nuyPTR9J70ovTLNHdi+kh6c3o0PZLOTR9ItXt6fXpD2if9Mf05vTKdk25IH0/9PV6Y1v78M9Jp6U3pbemo9JMEG8anUpe730xfnfbfmury9HDq499Ife6SNFyTbkp9/MfpnnRfGjqw+9zd6XvpO9PxSWn4dOpjX0yXTfvzpffFqY9dmr4+7b8sXZf+k362pvnP7wdHj3+Rrkzfno4/loar0w9Tn7sl/S19K8GGsVfq7NvBPhySdkknpw6K16ahs2If2395tBroncEPXB4tFm9Jfb6zf42Bft7yaOVH6bur3cWrUgfru5dHKx3M9692l7Nrv78Ddjh62tbf02dXu5v067sCqDHQ51/zg3TVanfTOf3ey6PF4nWpxwctj9ghuqRkfXVp+7x07fJopcvaf6UO2s6QHcjD96ftfCB0Rvz1andxc+rge/Hy6L+6Ihh+nsbzXdrvlLrk/1z6fOrS+uWpX3NAqr7GcP20rcemtuaiaVv9ncbr97SjxlX6v0zbzursIAb6+hv/4f86bed6rjv+4w/j6zoYh87Ic4+n+b/VE9N2+Gcaz4+Z9O2ppwuHp56Dd1b/fer766DrLLs541rAMzF/f3emP6TbUl+jpx09fXgwsYMY6Ovvt9N2LMXn7k1rH+9Su34zbWvtQK/O0sPmnh//luP1O5t/KPWCW8+fz07V53uhbUv3xTubz5fytaUPhbnx+u9PvQbRi3q9VnBK6ntgBzLQ118vnvXq8tdSr5x3Kdvl8zvTFakDu1fWO9i6zL8w9Rz31jRsbWD9e9rOjTsoXYb3PfTcv9cFqu9jnMP3XHpcYX9BekX6Supqozrrj1l9fCitXUFsznj9vuZh6YOpr9ufMb9e0Cv5veh36PJosXhN6nGv/le/p9cpereCdWKgbx8dRD0nfyh1hnxvuj11MPfWWAd3l+y95bZn6qB8OvNlfT1/2m7J6am31zpAH0idxccqoI+dmXpr7k+pS+uev3fwVz+MOgv3w2ZcgHvutN0WvVXYpXsHeAd/Lyp24PYOQ+2b3pXGKcba41797wfDOIZnvQ7Q/Va7/6MDfL4c3x46u3Ygbel1OpjGTD7X75sv7Xebttuit+7mpyHV23i9Bz+s/aDa2jHwLPPl1NVA/5CmtwL7x0E/Tf2bAmAD+VLqeXfv3fePfj6TAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAYGmxeAp3OMk/JsHWBgAAAABJRU5ErkJggg=="

        ws.addEventListener('message', function(event) {
            if(event.data.startsWith("retrieve:")) {
                var image = new Image();
                image.onload = function() {
                    ctx.clearRect(0, 0, canvas.width, canvas.height)
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

        ws.addEventListener('error', function(event) {
            console.log("Something went wrong, closing connection.");
            ws.close();
        });

        ws.addEventListener('close', function(event) {
            console.log("Connection closed, attempting to reconnect...");
            var disconnected = new Image();
            disconnected.onload = function() {
                ctx.clearRect(0, 0, canvas.width, canvas.height)
                ctx.drawImage(disconnected, 0, 0);
            };
            disconnected.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPoAAACNCAYAAACey2dEAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAVpSURBVHhe7dlXiHRnHQbwUbE3YjdWTJSoIWpUbFixJgYVUryJLcUWS1CxoYJ4IyoW7IFEEuuNuUgk0VhvVKIpahTEFoKCHaNYg+V5zpn342Syn2WTwY/w+8HDvO+Z3TOzs/t/264AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA2KJ7JN9PHj/1Vqs7Jk+fm/wbN05+kBw79diK664fuWbca/1YL03OTK4/9a6dTkyuicHswPUjW6LQt+e1yUHJFVPv2ukuyZvnJvsyhX71PDPpH/qLpt6VHZEcOTf3eH7y3uT45Ia9sHbL5GXJ25LNJewDkpOSdyQv7IWFhySvTHqvvoe3JE9IljrgdHn8tOQ9yQuSTTdKjkvemWy+RnWl8qqk939GL8TLkyclfe9dvZyTDLdN+jpvT07ohQ1dBbwx6ff1+9kyhb57r08+kdw1aRF9PVm6T9I/5uHzyWOS7yTdx4+CPCDptScmf0taQG9N6nXJhcnDpt5q9eLk4rk5uV/yhuTHSe99cPK5ZFnsLc5uIfr4j+QDyceToe+zr3//5A/J+5PeY+j7uiTpoNIB5dNJi/fk5A7JdZInJ79O6pHJt5N7Tr3V6sPJp+bmpANA38+9k34O3Z/DPusXSQtv6Mz+z2Qcxr06+evcXN0q6XOPmHpX1sL50tzco8XVWfh3yXIGvm/S+7xk6s175PafO/VmFyUfmpuTvyfLQmuB9ntaoPWZ5N1zc/LYpM934KgOLMv7dQbu4FAt4h5ALn0tWQ5wxyS93/ieDirL5X5XMn3eYdwWmdF3p4dHt0vOnXqzj6wfd/Lb5BvJWcnm4dUhyZfn5h6dUXu9RXVeL6x9N/lK0pl8+GVy2tyctDC7dF46ff1YLcQaX/OopKuSDkyvSTpzVwuzB4l9rS/2wtrlyffm5jSI/GluTjo4PTTpKmXcr4NT9X43S7rq+GwvrHWwYMsU+u7sv378X3SmfF/y0eSC5O5JteC6ZN7Uf8/V79ePQ/v7zc1Jl+NLXUVs/l47Yw5jldEZ/aZJi6/v5eHJoUkL8pTk/KSDWe30/urPybLQb79+7J6+9+v5wt2SFnO3IHdK+D9Q6Lsz9qOjGGv8ke/NH5Pup2++br8pqZ8kOxVA993Vk/ulxyU/mpuTzUKv//b32vfx8+TUpOcMRyfPSrol+Gbys6QDQ0/Xd/KXZPn6lybtvyvp/bpsf3bSQ8j+PONzW35Wy0NJtkSh706Xrj2g6ml4T6y7lO9ed2+OSnpgVZ1du9zubFifTPr8OHA7POnM3wOtLvf7Gi20zryvSG6S9MBsuDqFXh9LWtxjQOlMv9wvn5H0IK8DTO/bw7yellcL90FJl/5drle/vqf7Y0/ez+d5c3P1m6TbgJ473CLpyqiDzFI/yy8kD55682Da7cujp968nemW6SlTD7ash1WdwUbhPifpqflOh3Hdt3bm/lXSr+8f7nL53aVtrzf9urGP71L6q8l4roX11GQYh3FLH0zOnpuTPn/Y3Jx0Od1rY1btPrwDS5fgnXX7XAttrFZuk/RsodebbyUPTKrF/dOk17sCqG5Fel7Rn/2HSZ9bDoI93e8BXq/3sLGHcZclY3DpmUGfGz9nB5L2u9qoDiDtLw8gYes6mw2by9DNmbUzWGfmnVwvufPcvIobJF3yb1vf3962ID1o2+n9jRXNTnq/W8/Nq+i/5obNz62rlqX/1AcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABgX7Ja/QsVWtx8GY8PkwAAAABJRU5ErkJggg=="
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