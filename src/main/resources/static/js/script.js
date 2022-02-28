$(document).ready(function() {
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
            $("#notification").text('gg go next').css('background', '#4CBB17');
            return;
        }

        if(attempts.length > 4) {
            $("#notification").text('yikes').css('background', '#8F1C2A');
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