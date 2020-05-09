package websockets;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.thread.RSINotifierThread;
import edu.brown.cs.term.thread.RsiThread;

@WebSocket
public class RsiWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions
      = new ConcurrentLinkedQueue<>();
  private static final Map<Session, String> usernames
      = new ConcurrentHashMap<>();
  private RsiThread rsi;

  public static enum MESSAGE_TYPE {
  CONNECT, UPDATE_RSI, UPDATE_TRADE, SET_USERNAME, START_RSI, WALLET_INFO, UPDATE_AUTOMATIC
  }

  @OnWebSocketConnect
  public void connected(Session session) {
    sessions.add(session);
    JsonObject js = new JsonObject();
    js.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    String sendThis = GSON.toJson(js);

    try {
      session.getRemote().sendString(sendThis);
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    if (sessions.contains(session)) {
      sessions.remove(session);
      session.close(statusCode, reason);
    }
  }

  public static void sendRSIUpdateMessage(List<String> messages,
      String username) {
    System.out.println("SEND RSI UPDATE");
    JsonObject updateMessage = new JsonObject();
    updateMessage.addProperty("type", MESSAGE_TYPE.UPDATE_RSI.ordinal());

    JsonObject payload = new JsonObject();
    JsonArray messageArray = new JsonArray();
    for (String message : messages) {
      messageArray.add(message);
    }

    payload.add("message", messageArray);

    updateMessage.add("payload", payload);

    for (Session currSession : sessions) {
      try {
        currSession.getRemote().sendString(GSON.toJson(updateMessage));
      } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }

  }

  public static void sendWalletUpdate(double walletValue,
      String username) {
    JsonObject updateMessage = new JsonObject();
    updateMessage.addProperty("type", MESSAGE_TYPE.WALLET_INFO.ordinal());

    JsonObject payload = new JsonObject();

    payload.addProperty("amount", walletValue);

    updateMessage.add("payload", payload);

    for (Session currSession : sessions) {
      try {
        currSession.getRemote().sendString(GSON.toJson(updateMessage));
      } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  public static void sendAutomaticradingUpdateMessage(String username,
      String message) {
    JsonObject updateMessage = new JsonObject();
    updateMessage.addProperty("type",
        MESSAGE_TYPE.UPDATE_AUTOMATIC.ordinal());

    JsonObject payload = new JsonObject();
    payload.addProperty("message", message);

    updateMessage.add("payload", payload);

    for (Session currSession : sessions) {
      try {
        currSession.getRemote().sendString(GSON.toJson(updateMessage));
      } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

    }
  }

  public static void sendCustomTradingUpdateMessage(String username,
      Long orderId, String message) {
    JsonObject updateMessage = new JsonObject();
    updateMessage.addProperty("type", MESSAGE_TYPE.UPDATE_TRADE.ordinal());

    JsonObject payload = new JsonObject();
    payload.addProperty("message", message);
    payload.addProperty("orderId", orderId);

    updateMessage.add("payload", payload);

    for (Session currSession : sessions) {
      try {
        currSession.getRemote().sendString(GSON.toJson(updateMessage));
      } catch (IOException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

    }
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    int messageType = received.get("type").getAsInt();
    JsonObject receivedPayload;
    Set<String> symbols = new HashSet<String>();
    String username;

    switch (messageType) {
      case 3:
        receivedPayload = received.get("payload").getAsJsonObject();
        username = receivedPayload.get("username").getAsString();
        usernames.put(session, username);
        break;

      case 4:
        receivedPayload = received.get("payload").getAsJsonObject();
        username = receivedPayload.get("username").getAsString();

        CrypthubUser user = new CrypthubUser();
        symbols = user.symbolsInWallet(username);

        System.out.println(symbols.toString());
        new RSINotifierThread(username, "hello", symbols, "50", "50");

        break;
    }

  }
}
