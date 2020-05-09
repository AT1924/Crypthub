package edu.brown.cs.term.repl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;

import com.google.gson.Gson;

import edu.brown.cs.term.connection.DatabaseConnection;
import edu.brown.cs.term.gui.TermSparkHandlers;
import edu.brown.cs.term.gui.TermSparkHandlers.CryptoFrontHandler;
import edu.brown.cs.term.gui.TermSparkHandlers.TradesHandler;
import edu.brown.cs.term.gui.TradingHandler;
import edu.brown.cs.term.gui.UserHandler;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import websockets.RsiWebSocket;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  public static final Gson GSON = new Gson();
  public static HashSet<String> threadMap = new HashSet<>();

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    DatabaseConnection.setConnection("data/db.sqlite3");
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates
        = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  @SuppressWarnings("unchecked")
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    this.setUpSparkRoutes(freeMarker);
  }

  private void setUpSparkRoutes(FreeMarkerEngine freeMarker) {
    Spark.webSocket("/rsi", RsiWebSocket.class);
    // Spark.webSocket("/walletSocket", WalletSocket.class);
    Spark.get("/home", new CryptoFrontHandler(), freeMarker);
    Spark.get("/about", new TermSparkHandlers.AboutHandler(), freeMarker);
    Spark.get("/contact", new TermSparkHandlers.ContactHandler(),
        freeMarker);
    Spark.get("/news", new TermSparkHandlers.NewsHandler(), freeMarker);
    Spark.get("/profile", new TermSparkHandlers.ProfileHandler(),
        freeMarker);
    Spark.get("/trades", new TradesHandler(), freeMarker);
    Spark.get("/strategy", new TermSparkHandlers.StrategyHandler(),
        freeMarker);
    Spark.get("/strategyForm", new TermSparkHandlers.StrategyFormHandler(),
        freeMarker);
    Spark.get("/signup", new TermSparkHandlers.SignUpHandler(),
        freeMarker);
    Spark.post("/signUpUser", new UserHandler.AddUser());
    Spark.get("/login", new TermSparkHandlers.LogInHandler(), freeMarker);
    Spark.post("/checkUser", new UserHandler.CheckUser());
    Spark.post("/verifyLogin", new UserHandler.VerifyLogin());
    Spark.post("/getUserTrades", new UserHandler.GetUsersTrades());
    Spark.post("/getWallet", new UserHandler.GetWalletInformation());

    Spark.post("/updateKeys", new UserHandler.UpdatePublicPrivateKeys());
    Spark.post("/checkKeyValidity", new UserHandler.CheckKeys());
    Spark.post("/validKeysInDb", new UserHandler.CheckKeysAndInDB());
    Spark.post("/validManual", new TradingHandler.ValidOrders());
    Spark.post("/startFullTradeBuy",
        new TradingHandler.FullTradeBuyHandler());
    Spark.post("/startFullTradeSell",
        new TradingHandler.FullTradeSellHandler());
    Spark.post("/tradingAlgo", new TradingHandler.AlgorithmicTrader());
    Spark.post("/populateValue", new UserHandler.startWalletUpdates());
    Spark.post("/getGraphData", new UserHandler.getGraphInfo());

  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  private static String testDecimal(String lastPrice, double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double sellPrice = Double.valueOf(lastPrice);
    double buyPrice = sellPrice - (sellPrice * percentGain);
    BigDecimal bp = new BigDecimal(buyPrice, MathContext.DECIMAL64);
    String[] s = bp.toString().split("\\.");
    System.out.println(s[0] + ": " + s[1]);
    int length = s[1].length();
    String finalBuyPrice = "";
    if (length > decimalLength) {
      int difference = length - decimalLength;
      String decimal = "";
      decimal = s[1].substring(0, s[1].length() - difference);
      StringBuilder sb = new StringBuilder();
      sb.append(s[0]);
      sb.append(".");
      sb.append(decimal);
      finalBuyPrice = sb.toString();
    } else {
      finalBuyPrice = bp.toString();
    }
    System.out.println("Final buy price is " + finalBuyPrice);
    return finalBuyPrice;
  }

  private static String testBigDecimal(String lastPrice,
      double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double sellPrice = Double.valueOf(lastPrice);
    double buyPrice = sellPrice - (sellPrice * percentGain);
    BigDecimal bd = new BigDecimal(buyPrice).setScale(decimalLength,
        RoundingMode.HALF_DOWN);
    System.out.println("BigDecimal price: " + bd.toPlainString());
    return bd.toPlainString();
  }

}
