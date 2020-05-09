package edu.brown.cs.term.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class holds all the SparkHandlers for the GUI.
 *
 * @author Chrissy
 *
 */
public class TermSparkHandlers {
  private static final Gson GSON = new Gson();

  /**
   * Handles requests to the front page of the website.
   *
   * @author Chrissy
   *
   */
  public static class CryptoFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "index.ftl");
    }
  }

  /**
   * Handles request to the about page of the website.
   * @author AT1924
   */
  public static class AboutHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "about.ftl");
    }
  }

  /**
   * Handles request to the contact page of the website.
   * @author AT1924
   */
  public static class ContactHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "contactUs.ftl");
    }
  }

  /**
   * Handles request to the contact page of the website.
   * @author AT1924
   */
  public static class TradesHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "trades.ftl");
    }
  }

  /**
   * Handles request to the news page of the website.
   * @author AT1924
   */
  public static class NewsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "news.ftl");
    }
  }

  /**
   * Handles request for the signup page of the website.
   * @author Chrissy
   */
  public static class SignUpHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "signup.ftl");
    }
  }

  // /**
  // * Handles request for the signup page of the website.
  // * @author Chrissy
  // */
  // public static class SignUpPostHandler implements Route {
  // @Override
  // public Object handle(Request req, Response res) {
  // QueryParamsMap qm = req.queryMap();
  // String error = qm.value("error");
  // System.out.println("HERE" + error);
  // //String error = "error";
  // //Map<String, Object> variables = ImmutableMap.of();
  // return GSON.toJson(error);
  // }
  // }

  /**
   * Handles request for the login page of the website.
   * @author Chrissy
   */
  public static class SignUpPostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String error = req.params("error");
      System.out.println("im in the post handler");
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "signup.ftl");
    }
  }

  /**
   * Handles request for the login page of the website.
   * @author Chrissy
   */
  public static class LogInHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "login.ftl");
    }
  }

  /**
   * Handles request to the profile page of the website.
   * @author AT1924
   */
  public static class ProfileHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "profile.ftl");
    }
  }

  /**
   * Handles request to the strategy page of the website.
   * @author AT1924
   */
  public static class StrategyHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "strategy.ftl");
    }
  }

  /**
   * Handles request to the strategy page of the website.
   * @author AT1924
   */
  public static class StrategyFormHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "strategy-after.ftl");
    }
  }

}
