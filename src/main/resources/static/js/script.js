$(document).ready(function() {
    var wordLetters = word.split("");
    var letterMap = new Map();
    var attempts = [];
    var complete = false;
    var failed = false;

    // counts each letter in the answer
    for(var i = 0; i < wordLetters.length; i++) {
        if(letterMap.get(wordLetters[i]) == null) {
            letterMap.set(wordLetters[i], 1);
        } else {
            letterMap.set(wordLetters[i], (letterMap.get(wordLetters[i])+1));
        }
    }

    $(".in").keydown(function(e){
        if(e.keyCode === 13) {
            submitAttempt(letterMap);
        }
    });

    $('.btn').on('click', function() {
        submitAttempt(letterMap);
    })

    function submitAttempt(map) {
        var attempt = $(".in").val().toUpperCase();

        // validates an attempt for length and if it is a dictionary word
        if(complete || attempt.length < wordLetters.length || !words.includes(attempt)) {
            return;
        }

        $(".in").val(""); // clears the old attempt
        attempts.push(attempt);

        var letterMap = new Map(map); // clone map
        var index = attempts.length-1;
        var letters = attempts[index].split("");

        // set boxes to players attempt (doing this in a separate loop is obv inefficient but i'm the dev i don't care ¦])
        for(var i = 0; i < letters.length; i++) {
            $("#a"+(index)+" #l"+i).text(letters[i]);
        }

        // check each letter of the attempt against the answer, colour the matching positions in green
        var correctPosition = 0;
        for(var i = 0; i < letters.length; i++) {
            if(letters[i] == wordLetters[i]) {
                $("#a"+(index)+" #l"+i).addClass('correct');
                correctPosition++;
                if(letterMap.get(letters[i]) == 1) {
                    letterMap.delete(letters[i]);
                } else {
                    letterMap.set(letters[i], (letterMap.get(letters[i])-1));
                }
            }
        }

        // if player has guessed correctly, offer harder difficulty (lim 15)
        if(correctPosition == wordLetters.length) {
            complete = true;
            $("#inputGroup").html("<a href='" + word.length + "'><div id='notification' class='p-4 m-4'>gg go next</div></a>").removeClass("input-group").addClass('correct');
            if(word.length < 15) {
                $("#inputGroup").append("<a href='" + (word.length+1) + "'><div id='difficulty' class='p-2'>harder 😩</div></a>");
            }
            return;
        }

        // check each letter of the attempt against the answer, colour the non-matching positions in yellow
        for(var i = 0; i < letters.length; i++) {
            if(letterMap.has(letters[i]) && wordLetters.includes(letters[i]) && (letters[i] != wordLetters[i])) {
                $("#a"+(index)+" #l"+i).addClass('kinda');
                if(letterMap.get(letters[i]) == 1) {
                    letterMap.delete(letters[i]);
                } else {
                    letterMap.set(letters[i], (letterMap.get(letters[i])-1));
                }
            }
        }

        // if player has run out of attempts, offer an easier difficulty (lim 3)
        if(attempts.length > 4) {
            complete = true;
            $("#inputGroup").html("<a href='" + word.length + "'><div id='notification' class='p-4 m-4'>Yikes... it was " + word + "</div></a>").removeClass("input-group").addClass('wrong');
            if(word.length > 3) {
                $("#inputGroup").append("<a href='" + (word.length-1) + "'><div id='difficulty' class='p-2'>im sussy 😭</div></a>");
            }
        }
    }
})