package edu.brown.cs.term.trading;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;

public class Symbols {

  public static Set<String> getAllSymbols() {
    String APIkey
        = "qW9xUO8kNpHHnxb0dJjStRrzE7tN0oTlPCbah4IWdxkgyAa5vqJXzSME6IYlDhrp";
    String secretKey
        = "PXEisYYJhKd6T36nC8mH05AoZl4gcyytcz1Gd5Ybin1iv6GeS27rXySsaAn3Z5Mp";
    BinanceApiClientFactory fact
        = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiRestClient client = fact.newRestClient();
    List<TickerPrice> tickers = client.getAllPrices();
    return convertTickersToSymbols(tickers);
  }

  private static Set<String> convertTickersToSymbols(
      List<TickerPrice> tickers) {
    if (tickers == null) {
      return new HashSet<>();
    }
    Set<String> symbols = new HashSet<>();
    for (TickerPrice onePrice : tickers) {
      symbols.add(onePrice.getSymbol());
    }
    return symbols;
  }
}
