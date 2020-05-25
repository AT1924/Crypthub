# Crypthub

## Overview
Crypthub is a downloadable cryptocurrency trading application that automatically buys and sells various cryptocurrencies on the Binance marketplace using the Binance API. We implement automated trading using an optimized algorithm that utilizes multi-threading to access various market financial indicators using the Binance API. The trading algorithm then sells and buys automatially given some set user parameters. Crypthub also supports the use of simple algorithms such as stop losses (buy/sell when the cryptocurrency falls below a certain price), and limit orders (buy/sell when it rises above a certain price).The SaaS application also features full user functionality such as login in/sign up/log on as well as web sockets to provide realtime updates on the status of your cryptocurrency.


## Source Code and Technologies Used

Stack was implemented in Sqlite3/Java/Javascript(ES6)/HTML5/CSS (Bootstrap).

View source code at path:
  Java Code: Crypthub/src/main/java/edu/brown/cs/term/
  
  HTML (.ftl files for spark) Code: Crypthub/src/main/resources/spark
  
  Javascript Code: Crypthub/src/main/resources/static/js
  
  CSS Code: Crypthub/src/main/resources/static/css

## Installation Instructions
Simply clone this repo, cd into the home directory, and run ./run --gui

