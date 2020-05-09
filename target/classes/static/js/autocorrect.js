
// Waits for DOM to load before running
$(document).ready(() => {
    // Variables to keep track of autocorrect state
    let currInput = '',
        suggestionList = [];

    //use jQuery to get reference to the HTML element with an id of "suggestions".
    const $suggestions = $('#suggestions');

    // Listen for keypress events. If a user presses a key, autocorrect their input. 
    $(document).keyup(event => {
        console.log("key being clicked");
        
        currInput = document.getElementById("user-input").value;

        // if the input is all whitespace, empty the select box don't call the ac command. 
        if (currInput.trim() === "") {
            $suggestions.empty(); 
            return; 
        }

        const postParameters = {input: currInput, universe: "autocorrect"}; 

        //Make a POST request to the "/suggestions" endpoint with the word information
        $.post("/suggestions", postParameters, responseJSON => {

            // Parse the JSON response into a JavaScript object.
            const responseObject = JSON.parse(responseJSON);
            suggestionList = responseObject.suggestions;

            const limit = suggestionList.length >= 6 ? 6 : suggestionList.length; 
            const restOfInput = suggestionList[0];
            let newHTML = [];
            for (let i = 1; i < limit; i++) {
                newHTML.push('<span id = "suggestion">' + restOfInput + suggestionList[i] + '</span>');
            }
            $suggestions.html(newHTML.join(""));
        });

        $suggestions.empty(); 
    });
});

$(function(){
  $('input[name="prefix"]').click(function(){
    let value = $('input[name="prefix"]:checked', '#prefix-form').val();
    postParameters = {command: "prefix", setting: value}; 
    $.post("/settings", postParameters); 
  });
});

$(function(){
  $('input[name="whitespace"]').click(function(){
    let value = $('input[name="whitespace"]:checked', '#whitespace-form').val();
    postParameters = {command: "whitespace", setting: value};    
    $.post("/settings", postParameters); 
  });
});

$(function(){
  $('input[name="smart"]').click(function(){
    let value = $('input[name="smart"]:checked', '#smart-form').val();
    postParameters = {command: "smart", setting: value}; 

    $.post("/settings", postParameters); 
  });
});

$(function(){
  $('input[name="led"]').click(function(){
    let value = $('input[name="led"]:checked', '#led-form').val();
    postParameters = {command: "led", setting: value};  

    $.post("/settings", postParameters); 
  });
});


