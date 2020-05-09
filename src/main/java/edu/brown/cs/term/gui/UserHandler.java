package edu.brown.cs.term.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.database.Trades;
import edu.brown.cs.term.repl.Main;
import edu.brown.cs.term.thread.PopulateValuesDBThread;
import edu.brown.cs.term.thread.WalletThread;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler {

  public static class CheckUser implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      CrypthubUser user = new CrypthubUser();
      return user.checkUser(username);
    }
  }

  public static class AddUser implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      String firstName = qm.value("firstName");
      String lastName = qm.value("lastName");
      String number = qm.value("number");
      CrypthubUser user = new CrypthubUser();
      return user.addUser(username, password, firstName, lastName, number);
    }
  }

  public static class VerifyLogin implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      CrypthubUser user = new CrypthubUser();
      return user.login(username, password);
    }
  }

  public static class UpdatePublicPrivateKeys implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String public_key = qm.value("publicKey");
      String private_key = qm.value("privateKey");
      CrypthubUser user = new CrypthubUser();
      return user.addPublicPrivateKey(username, public_key, private_key);
    }
  }

  public static class UpdateKeys implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String publicKey = qm.value("publicKey");
      String privateKey = qm.value("privateKey");
      CrypthubUser user = new CrypthubUser();
      return user.addPublicPrivateKey(username, publicKey, privateKey);
    }
  }

  /**
   * This handles the post request for converting street names to
   * coordinates.
   *
   * @author Chrissy
   */
  public static class GetUsersTrades implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      Trades trade = new Trades();
      List<Map<String, Object>> userTrades = trade.getAllTrades(username);

      Map<String, Object> variables
          = ImmutableMap.of("trades", userTrades);
      return Main.GSON.toJson(variables);
    }
  }

  /**
   * Gets the wallet information of a particular user.
   */
  public static class GetWalletInformation implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      CrypthubUser user = new CrypthubUser();
      return user.wallet(username);
    }
  }

  /**
   * Checks if the keys are valid and exists in the database.
   */
  public static class CheckKeysAndInDB implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      CrypthubUser user = new CrypthubUser();
      return user.keysExistAndValid(username);
    }
  }

  public static class CheckKeys implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String public_key = qm.value("public_key");
      String private_key = qm.value("private_key");
      CrypthubUser user = new CrypthubUser();
      return user.keysValid(public_key, private_key);
    }
  }

  public static class startWalletUpdates implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      WalletThread t = new WalletThread(username);
      return Main.GSON.toJson(ImmutableMap.of("success", true));
    }
  }

  public static class getGraphInfo implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      CrypthubUser user = new CrypthubUser();
      if(!Main.threadMap.contains(username + " populator")){
        PopulateValuesDBThread thread = new
        PopulateValuesDBThread(username); 
      }
      return user.historicalWalletValue(username, 20);
    }
  }
}
