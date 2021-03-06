$(document).ready(function() {
    $.get("/api/v1/words/"+$('.key').text(), function(data, status){
        var word = data['random_word'];
        var words = data['words'];
        var letters = word.split("");
        var letterMap = new Map();
        var attempts = [];
        var complete = false;
        var failed = false;

        // counts each letter in the answer
        for(var i = 0; i < letters.length; i++) {
            if(letterMap.get(letters[i]) == null) {
                letterMap.set(letters[i], 1);
            } else {
                letterMap.set(letters[i], (letterMap.get(letters[i])+1));
            }
        }

        $(".text").keydown(function(e){
            if(e.keyCode === 13) {
                submitAttempt(letterMap);
            }
        });

        $('.btn').on('click', function() {
            submitAttempt(letterMap);
        })

        function submitAttempt(map) {
            var attempt = $(".text").val().toUpperCase();

            // validates an attempt for length and if it is a dictionary word
            if(complete || attempt.length < letters.length || !words.includes(attempt)) {
                return;
            }

            $(".text").val(""); // clears the old attempt
            attempts.push(attempt);

            var letterMap = new Map(map); // clone map
            var index = attempts.length-1;
            var attempt = attempts[index].split("");

            // set boxes to players attempt (doing this in a separate loop is obv inefficient but i'm the dev i don't care ¦])
            for(var i = 0; i < attempt.length; i++) {
                $("#a"+(index)+" #l"+i).text(attempt[i]);
            }

            // check each letter of the attempt against the answer, colour the matching positions in green
            var correctPosition = 0;
            for(var i = 0; i < attempt.length; i++) {
                if(attempt[i] == letters[i]) {
                    $("#a"+(index)+" #l"+i).css({
                        'background': '#4CBB17',
                        '-webkit-transition': 'all 0.5s linear',
                        '-moz-transition': 'all 0.5s linear',
                        '-o-transition': 'all 0.5s linear',
                        'transition': 'all 0.5s linear',
                    });
                    correctPosition++;
                    if(letterMap.get(attempt[i]) == 1) {
                        letterMap.delete(attempt[i]);
                    } else {
                        letterMap.set(attempt[i], (letterMap.get(attempt[i])-1));
                    }
                }
            }

            // if player has guessed correctly, offer harder difficulty (lim 15)
            if(correctPosition == letters.length) {
                complete = true;
                $("#input").html("<a href='wordle?letters=" + word.length + "'><div id='notification' class='p-4 m-4'>gg go next</div></a>").removeClass("input-group").addClass('correct');
                if(word.length < 15) {
                    $("#input").append("<a href='wordle?letters=" + (word.length+1) + "'><div id='difficulty' class='p-2'>harder 😩</div></a>");
                }
                return;
            }

            // check each letter of the attempt against the answer, colour the non-matching positions in yellow
            for(var i = 0; i < attempt.length; i++) {
                if(letterMap.has(attempt[i]) && letters.includes(attempt[i]) && (attempt[i] != letters[i])) {
                    $("#a"+(index)+" #l"+i).css({
                        'background': '#FFD55E',
                        '-webkit-transition': 'all 0.5s linear',
                        '-moz-transition': 'all 0.5s linear',
                        '-o-transition': 'all 0.5s linear',
                        'transition': 'all 0.5s linear',
                    });
                    if(letterMap.get(attempt[i]) == 1) {
                        letterMap.delete(attempt[i]);
                    } else {
                        letterMap.set(attempt[i], (letterMap.get(attempt[i])-1));
                    }
                }
            }

            // if player has run out of attempts, offer an easier difficulty (lim 3)
            if(attempts.length > 4) {
                complete = true;
                $("#input").html("<a href='wordle?letters=" + word.length + "'><div id='notification' class='p-4 m-4'>Yikes... it was " + word + "</div></a>").removeClass("input-group").addClass('wrong');
                if(word.length > 3) {
                    $("#input").append("<a href='wordle?letters=" + (word.length-1) + "'><div id='difficulty' class='p-2'>im sussy 😭</div></a>");
                }
            }
        }
    });
})