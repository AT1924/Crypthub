
// Waits for DOM to load before running
$(document).ready(() => {
    var url = 'https://newsapi.org/v2/everything?' +
        'q=Blockchain&' +
        'from=2019-04-06&' +
        'sortBy=popularity&' +
        'apiKey=779efe7b75144c8aaea2650d9453cde1';



var req = new Request(url);

fetch(req)
    .then(function(response) {
        //console.log(response.json());
        const responseObject = response.json();
        responseObject.then(function (value) {
            let articles = value.articles;
            console.log(articles);
            let articleInformation = [];

            for (let i = 0; i < 3;i++) {
                let articleInfo = { title: articles[i].title, url: articles[i].url,
                    urlToImage: articles[i].urlToImage, author: articles[i].author, publishedAt: articles[i].publishedAt};
                articleInformation[i] = articleInfo;
            }

            setNews(articleInformation);
        })



    })

});

let $postDate1 = $("#post-date-1");
let $postDate2 = $("#post-date-2");
let $postDate3 = $("#post-date-3");


let $newsImage1= $("#newsImage1");
let $newsImage2= $("#newsImage2");
let $newsImage3= $("#newsImage3");

let $news1Link = $("#news1Link");
let $news2Link = $("#news2Link");
let $news3Link = $("#news3Link");

let $author1 = $("#author1");
let $author2 = $("#author2");
let $author3 = $("#author3");

function setNews(values) {
    // $news1Link.html(values[0].title);
    let str = values[0].title;
    let link = str.link(values[0].url);
    let author1 = values[0].author;
    let imageURL1 = values[0].urlToImage;
    let image1Link = imageURL1.link(imageURL1);
    let pD1 = values[0].publishedAt.substring(1, 3);
    let pD2 = values[0].publishedAt.substring(4, 6);
    let pD3 = values[0].publishedAt.substring(7, 9);
    let pub = values[0].publishedAt.substring(0, 10);
    pD1 = pD1.concat(pD2,pD3);
    $postDate1.html(pub);
    $news1Link.html(link);
    $author1.html(author1);

    let str2 = values[1].title;
    let link2 = str2.link(values[1].url);
    let author2 = values[1].author;
    let pub2 = values[1].publishedAt.substring(0, 10);
    $news2Link.html(link2);
    $author2.html(author2);
    $postDate2.html(pub2);

    let str3 = values[2].title;
    let link3 = str3.link(values[2].url);
    let author3 = values[2].author;
    let pub3 = values[2].publishedAt.substring(0, 10);
    $news3Link.html(link3);
    $author3.html(author3);
    $postDate3.html(pub3);


}

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


