package edu.brown.cs.term.tests;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.binance.api.client.exception.BinanceApiException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Tests the Binance API Functionality
 */
public class BinanceAPITest {
  private String APIkey = "qW9xUO8kNpHHnxb0dJjStRrzE7tN0oTlPCbah4IWdxkgyAa5vqJXzSME6IYlDhrp";
  private String secretKey = "PXEisYYJhKd6T36nC8mH05AoZl4gcyytcz1Gd5Ybin1iv6GeS27rXySsaAn3Z5Mp";


  /**
   * Tests if connection successfully connects to the server and gets the server
   * time successfully.
   */
  @Test
  public void assertConnectionToServer() {
    BinanceApiClientFactory fact = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiRestClient client = fact.newRestClient();
    client.ping();
    assertNotNull(client);
    long serverTime = client.getServerTime();
    assertNotNull(serverTime);
    assertTrue(serverTime > 0);
  }

  /**
   * Successfully tests the Websockets functionality of binance
   */
  @Test
  public void webSocketsBinance() {
    BinanceApiClientFactory fact = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiWebSocketClient client = fact.newWebSocketClient();
    for(int i = 0; i< 500000; i++) {
      client.onAggTradeEvent("ethbtc", new BinanceApiCallback<AggTradeEvent>() {
        @Override
        public void onResponse(AggTradeEvent aggTradeEvent) throws BinanceApiException {
          System.out.println(aggTradeEvent.getPrice());
          assertTrue(true);
        }
      });
    }
  }

  @Test
  public void getPrice(){
    BinanceApiClientFactory fact = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiRestClient client = fact.newRestClient();
    System.out.println(client.getAllPrices());
  }


}
