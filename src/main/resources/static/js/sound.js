$(document).ready(function() {
    var iframeElement = document.querySelector('iframe');
    var player = SC.Widget(iframeElement);

    var progress = document.getElementById("progress");
    var currentTime = 1000;
    var maxTime = 16000;

    var successful = false;
    var successfulTime = 1000;
    var attempts = [];

    player.bind(SC.Widget.Events.READY, function() {
        player.setVolume(50);
        $('#play-button').text("▶️");
        //        player.getSounds(function(sounds) {
        //            sounds.forEach((track) => {
        //                var pub = track.publisher_metadata;
        //                console.log(track.id + ", " + ((pub.artist == undefined) ? track.user.username : pub.artist) + " - " + track.title)
        //            });
        //        });
        // for later :)
    });

    player.bind(SC.Widget.Events.PLAY_PROGRESS, function(data) {
        progress.value = data.currentPosition;
        $('#progress-text').text("0:" + pad(Math.floor(data.currentPosition / 1000)) + " / 0:" + pad(currentTime / 1000));
        if(data.currentPosition >= currentTime) {
            player.pause();
            $('#play-button').text("▶️");
        }
    });

    $('.play').on('click', function() {
        player.isPaused(function isPaused(p) {
            if(p == true) {
                player.seekTo(0);
                player.play();
                $('#play-button').text("⏸");
            } else {
                player.pause();
                progress.value = 0;
                $('#play-button').text("▶️");
            }
        });
    })

    $('.help').on('click', function() {
        if(currentTime < maxTime) {
            currentTime = currentTime * 2;
            progress.max = currentTime;
            $('#progress-text').text("0:00 / 0:" + pad(currentTime / 1000));
            if(currentTime == maxTime) {
                $('.help').addClass("d-none");
                $('.giveup').removeClass("d-none");
            }
        }
    });

    $('.giveup').on('click', function() {
        for(var i = 0; i < 6; i++) {
            attempts.push(i);
        }
        submit();
    });

    $(".in").keydown(function(e){
        if(e.keyCode === 13) {
            submit();
        }
    });

    $('.submit').on('click', function() {
        submit();
    })

    function displayTrackInfo(currentSound) {
        if(successful) {
            $("#inputGroup").html("<a href=''><div id='notification' class='p-3 mt-2 mb-2'>Nice! You got it in " + successfulTime + " second(s), and in only " + attempts.length + " attempt(s)!</div></a>").removeClass("input-group").addClass('correct');
        } else {
            $("#inputGroup").html("<a href=''><div id='notification' class='p-3 mt-2 mb-2'>Yikes... that was a hard one!</div></a>").removeClass("input-group").addClass('wrong');
        }

        progress.max = currentTime = 30000; // allow 30 second clip to be played
        var publisher = currentSound.publisher_metadata;
        $('.help').addClass('d-none');
        $('.giveup').addClass('d-none');
        $('#track-art').prop("src", currentSound.artwork_url);
        $('#track-title').text(currentSound.title);
        $('#track-artist').text((publisher.artist == undefined) ? currentSound.user.username : publisher.artist);
    }

    function submit() {
        player.getCurrentSound(function(currentSound) {
            // if player has run out of attempts;
            if(attempts.length > 4) {
                displayTrackInfo(currentSound);
                return;
            }

            var attempt = $(".in").val();
            if(attempt == "") {
                return;
            }

            $(".in").val(""); // clears the old attempt
            attempts.push(attempt);

            var publisher = currentSound.publisher_metadata;
            if(attempt == ((publisher.artist == undefined) ? currentSound.user.username : publisher.artist) + " - " + currentSound.title) {
                successful = true;
                successfulTime = currentTime / 1000;
                displayTrackInfo(currentSound);
            }

            for(var i = 0; i < attempts.length; i++) {
                $("#a"+(i)).text(attempts[i]);
                if(successful && i == attempts.length-1) {
                    $("#a"+(i)).addClass('correct');
                }
            }
        });
    }

    function pad(n) {
        return (n < 10) ? ("0" + n) : n;
    }
});
