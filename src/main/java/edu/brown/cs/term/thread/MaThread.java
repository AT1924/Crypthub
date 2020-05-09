package edu.brown.cs.term.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.binance.api.client.domain.market.CandlestickInterval;

import edu.brown.cs.term.trading.MovingAverage;

public class MaThread implements Runnable{
  String username;
	String name;
	Set<String> symbols;
	String multiplier;
	Thread t;
	MovingAverage ma;
	Map<String, Double> maMap = new HashMap<String, Double>();
	Map<String, Double> sdMap = new HashMap<String, Double>();
	public MaThread(String user, String thread, Set<String> symb, String mult) {
	  username = user;
		name = thread;
		symbols = symb;
		multiplier = mult;
		t = new Thread(this, name);
		t.start();
	}
	public void run() {
		try {
			ma = new MovingAverage(CandlestickInterval.HALF_HOURLY, username, symbols, multiplier);
			ma.calculate();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println(name + " was interrupted");
		}
	}
	
	public Map<String, Double> getMAMap() {
	  return ma.getMAMap();
	}
	
	public Map<String, Double> getSDMap() {
	  return ma.getSDMap();
	}

}
