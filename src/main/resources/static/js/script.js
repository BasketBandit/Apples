$(document).ready(function() {
    var wordLetters = word.split("");
    var attempts = [];
    var complete = false;
    var failed = false;

    function submitAttempt() {
        if(complete || failed) {
            return;
        }

        var attempt = $(".in").val().toUpperCase();

        // validates an attempt for length and if it is a dictionary word
        if(attempt.length < wordLetters.length || !words.includes(attempt)) {
            return;
        }

        attempts.push(attempt);

        var index = attempts.length-1;
        var letters = attempts[index].split("")
        var correctPosition = 0;
        for(var i = 1; i < letters.length+1; i++) {
            $("#a"+(index+1)+" #l"+i).text(letters[i-1])
            if(letters[i-1] == wordLetters[i-1]) {
                $("#a"+(index+1)+" #l"+i).css('background', '#4CBB17');
                correctPosition++;
            } else if(wordLetters.includes(letters[i-1])) {
                $("#a"+(index+1)+" #l"+i).css('background', '#FFBF00');
            }
        }

        if(correctPosition == wordLetters.length) {
            complete = true;
            $("#inputGroup").html("<a href='" + word.length + "'><div id='notification' class='p-4 m-4'>gg go next</div></a>").removeClass("input-group").css('background', '#4CBB17');
            if(word.length < 15) {
                $("#inputGroup").append("<a href='" + (word.length+1) + "'><div id='difficulty' class='p-2'>harder 😩</div></a>");
            }
            $("#notification");
            return;
        }

        if(attempts.length > 4) {
            $("#inputGroup").html("<a href='" + word.length + "'><div id='notification' class='p-4 m-4'>Yikes... it was " + word + "</div></a>").removeClass("input-group").css('background', '#8F1C2A');
            if(word.length > 3) {
                $("#inputGroup").append("<a href='" + (word.length-1) + "'><div id='difficulty' class='p-2'>im sussy 😭</div></a>");
            }
            failed = true;
        }
    }

    $(".in").keydown(function(e){
         if(e.keyCode === 13) {
               submitAttempt();
         }
    });

    $('.btn').on('click', function() {
        submitAttempt();
    })
})