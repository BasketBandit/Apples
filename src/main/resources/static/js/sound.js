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
        player.setVolume(7); // soundcloud volume oddly high
        $('#play-button').removeClass('btn-primary').removeClass('btn').addClass('play-ready').text("▶️");
//        setTimeout(function (){
//            player.getSounds(function(sounds) {
//                sounds.forEach((track) => {
//                    var pub = track.publisher_metadata;
//                    console.log(track.id + "," + ((pub.artist == undefined) ? track.user.username : pub.artist) + " - " + track.title)
//                });
//            });
//        }, 5000);
        // for later :)
    });

    player.bind(SC.Widget.Events.PLAY_PROGRESS, function(data) {
        progress.value = data.currentPosition;
        $('#progress-text').text("0:" + pad(Math.floor(data.currentPosition / 1000)) + " / 0:" + pad(maxTime / 1000));
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
        attempts.push("-");
        increaseTime();
        displayAttempts();
    });

    $('.giveup').on('click', function() {
        while(attempts.length < 5) {
            attempts.push("-");
        }
        submit();
    });

    $(".text").keydown(function(e){
        if(e.keyCode === 13) {
            submit();
        }
    });

    $('.submit').on('click', function() {
        submit();
    })

    function displayTrackInfo(currentSound) {
        if(successful) {
            $("#input").html("<a href=''><div id='notification' class='p-3 mt-2 mb-2'>Nice! You got it in " + successfulTime + " second(s)!</div></a><a href=''><div class='next'>Next</div></a>").removeClass("input-group").addClass('correct');
        } else {
            $("#input").html("<a href=''><div id='notification' class='p-3 mt-2 mb-2'>Yikes... that was a hard one!</div></a><a href=''><div class='next'>Next</div></a>").removeClass("input-group").addClass('wrong');
        }

        progress.max = maxTime = currentTime = 30000; // allow 30 second clip to be played
        var publisher = currentSound.publisher_metadata;
        $('.help').addClass('d-none');
        $('.giveup').addClass('d-none');
        $('div.line').remove();
        $('#track-art').prop("src", currentSound.artwork_url);
        $('#track-title').text(currentSound.title);
        $('#track-artist').text((publisher.artist == undefined) ? currentSound.user.username : publisher.artist);
    }

    function submit() {
        player.getCurrentSound(function(currentSound) {
            var attempt = $(".text").val();
            attempts.push(attempt);
            $(".text").val(""); // clears the old attempt

            var publisher = currentSound.publisher_metadata;
            if(attempt == ((publisher.artist == undefined) ? currentSound.user.username : publisher.artist) + " - " + currentSound.title) {
                successful = true;
                successfulTime = currentTime / 1000;
                displayTrackInfo(currentSound);
            } else {
                increaseTime();
            }

            displayAttempts();

            // if player has run out of attempts;
            if(attempts.length > 4) {
                displayTrackInfo(currentSound);
            }
        });
    }

    function increaseTime() {
        if(currentTime < maxTime) {
            currentTime = currentTime * 2;
            $('.line-hidden').last().removeClass('line-hidden');
            if(currentTime == maxTime) {
                $('.help').addClass("d-none");
                $('.giveup').removeClass("d-none");
            }
        }
    }

    function displayAttempts() {
        for(var i = 0; i < attempts.length; i++) {
            $("#a"+(i)).text(attempts[i]);
            if(successful && i == attempts.length-1) {
                $("#a"+(i)).addClass('correct');
            }
        }
    }

    function pad(n) {
        return (n < 10) ? ("0" + n) : n;
    }
});
