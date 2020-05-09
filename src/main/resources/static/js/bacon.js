
function capitalizeFirstLetter(str) {
    var splitStr = str.toLowerCase().split(' ');
    for (var i = 0; i < splitStr.length; i++) {
       // You do not need to check if i is larger than splitStr length, as your for does that for you
       // Assign it back to the array
       splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);     
   }
   // Directly return the joined string
   return splitStr.join(' '); 
}

// Waits for DOM to load before running
$(document).ready(() => {
    // Variables to keep track of autocorrect state
    let sourceActor = '',
        destActor = '',
        sourceSuggestionList = [], 
        destSuggestionList = [];

    //use jQuery to get reference to the HTML element with an id of "suggestions".
    const $sourceSuggestions = $('#source-suggestions');
    const $destSuggestions = $('#dest-suggestions');

    // Listen for keypress events. If a user presses a key, autocorrect their input. 
    $("#source-actor").keyup(event => {
        console.log("key being clicked");
        
        sourceActor = document.getElementById("source-actor").value;

        // if the input is all whitespace, empty the select box don't call the ac command. 
        if (sourceActor.trim() === "") {
            $sourceSuggestions.empty(); 
            return; 
        }

        const postParametersSource ={input: sourceActor, universe: "bacon"}; 

        //Make a POST request to the "/suggestions" endpoint with the word information
        $.post("/suggestions", postParametersSource, responseJSON => {

            // Parse the JSON response into a JavaScript object.
            const responseObject = JSON.parse(responseJSON);
            sourceSuggestionList = responseObject.suggestions;

            const limit = sourceSuggestionList.length >= 6 ? 6 : sourceSuggestionList.length; 
            const restOfInput = capitalizeFirstLetter(sourceSuggestionList[0]);
            let newHTML = [];
            for (let i = 1; i < limit; i++) {
                newHTML.push('<span id = "source-suggestion">' + restOfInput + capitalizeFirstLetter(sourceSuggestionList[i]) + '</span>');
            }
            $sourceSuggestions.html(newHTML.join(""));
        });

        $sourceSuggestions.empty(); 
    });

    $("#dest-actor").keyup(event => {
        console.log("key being clicked");
        
        destActor = document.getElementById("dest-actor").value;

        // if the input is all whitespace, empty the select box don't call the ac command. 
        if (destActor.trim() === "") {
            $destSuggestions.empty(); 
            return; 
        }

        const postParametersDest ={input: destActor, universe: "bacon"}; 

        //Make a POST request to the "/suggestions" endpoint with the word information
        $.post("/suggestions", postParametersDest, responseJSON => {

            // Parse the JSON response into a JavaScript object.
            const responseObject = JSON.parse(responseJSON);
            destSuggestionList = responseObject.suggestions;

            const limit = destSuggestionList.length >= 6 ? 6 : destSuggestionList.length; 
            const restOfInput = capitalizeFirstLetter(destSuggestionList[0]);
            let newHTML = [];
            for (let i = 1; i < limit; i++) {
                newHTML.push('<span id = "dest-suggestion">' + restOfInput + capitalizeFirstLetter(destSuggestionList[i]) + '</span>');
            }
            $destSuggestions.html(newHTML.join(""));
        });

        $destSuggestions.empty(); 
    });
});


