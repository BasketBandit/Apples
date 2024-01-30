$(document).ready(function() {
    $.get("/api/v1/words/"+$('.key').text(), function(data, status){
        var word = data['random_word'];
        var words = data['words'];
        var letters = word.split("");
        var letterMap = new Map();
        var input = [];
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

        $(".keyboard-key").on("click", function(e) {
            if(!complete && input.length < letters.length) {
                $("#a"+attempts.length+" #l"+input.length).text($(this).data('key'));
                input.push($(this).data('key'));
            }
        });

        $('.backspace-key').on('click', function(e) {
            if(input.length > 0) {
                $("#a"+attempts.length+" #l"+(input.length-1)).text("");
                input.pop();
            }
        })

        $('.submit-key').on('click', function(e) {
            submitAttempt(input.join(""));
        })

        function submitAttempt(string) {
            if(complete) {
                return;
            }

            if(string.length < letters.length) {
                console.log("word too short")
                return;
            }

            if(!words.includes(string)) {
                console.log("word doesn't exist");
                return;
            }

            input = [];
            attempts.push(string);

            var map = new Map(letterMap); // clone map
            var index = attempts.length-1;
            var attempt = attempts[index].split("");

            for(var i = 0; i < attempt.length; i++) {
                $("#a"+(index)+" #l"+i).addClass('incorrect');
            }

            // check each letter of the attempt against the answer, colour the matching letters in matching positions in green
            var correctPosition = 0;
            for(var i = 0; i < attempt.length; i++) {
                if(attempt[i] == letters[i]) {
                    $("#a"+(index)+" #l"+i).addClass('correct');
                    $('.keyboard-key:contains("'+(attempt[i])+'")').addClass('correct');
                    correctPosition++;
                    if(map.get(attempt[i]) == 1) {
                        map.delete(attempt[i]);
                    } else {
                        map.set(attempt[i], (map.get(attempt[i])-1));
                    }
                }
            }

            // check each letter of the attempt against the answer, colour the matching letters in non-matching positions in yellow
            for(var i = 0; i < attempt.length; i++) {
                if(map.has(attempt[i]) && letters.includes(attempt[i]) && (attempt[i] != letters[i])) {
                    $("#a"+(index)+" #l"+i).addClass('kinda');
                    $('.keyboard-key:contains("'+(attempt[i])+'")').addClass('kinda');
                    if(map.get(attempt[i]) == 1) {
                        map.delete(attempt[i]);
                    } else {
                        map.set(attempt[i], (map.get(attempt[i])-1));
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

            // if player has run out of attempts, offer an easier difficulty (lim 3)
            if(attempts.length > 5) {
                complete = true;
                $("#input").html("<a href='wordle?letters=" + word.length + "'><div id='notification' class='p-4 m-4'>Yikes... it was " + word + "</div></a>").removeClass("input-group").addClass('wrong');
                if(word.length > 3) {
                    $("#input").append("<a href='wordle?letters=" + (word.length-1) + "'><div id='difficulty' class='p-2'>im sussy 😭</div></a>");
                }
            }
        }
    });
})