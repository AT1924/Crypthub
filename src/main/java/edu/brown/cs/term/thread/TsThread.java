package edu.brown.cs.term.thread;

import java.util.Set;

import com.binance.api.client.domain.market.CandlestickInterval;
import edu.brown.cs.term.trading.RSICalculator;
import edu.brown.cs.term.trading.TrailingStop;

public class TsThread implements Runnable{
	String name;
	Thread t;
	String username;
	Set<String> symbols;
	double stopPercent = 0.10;
	public TsThread(String user, String thread,
	    Set<String> symb, String percent) {
	  username = user;
	  stopPercent = Double.valueOf(percent);
		name = thread;
		t = new Thread(this, name);
		t.start();
	}
	public void run() {
		try {
			TrailingStop ts = new TrailingStop(username, symbols, stopPercent);
			ts.calculate();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println(name + " was interrupted");
		}
	}

}
