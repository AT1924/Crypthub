package edu.brown.cs.plyalyut_rtownsen.maps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class ApiTest {

  /**
   * Loads in the database before every single test.
   */
  @Before
  public void loadDB() {
    repl = new Maps();
    GSON = new Gson();
    repl.loadDB("data/maps/smallMaps.sqlite3");
  }

  /**
   * Makes sure the connection exists with the database.
   */
  @Test
  public void assertConnectionExists() {
    assertTrue(repl.isConnectedToDB());
  }

  /**
   * Makes sure the tile information is in the valid get tile format.
   */
  @Test
  public void getTile() {
    String mapsInfo = repl.loadMapTile(42, -72,
            41.8, -71.3);
    assertNotNull(mapsInfo);
    assertTrue(mapsInfo.length() != 0);
    assertTrue(mapsInfo.contains("\"name\":\"Radish Spirit Blvd\""));
  }

  /**
   * Tests to make sure nearest neighbor works as expected.
   */
  @Test
  public void getNearestPoint() {
    String mapsInfo = repl.nearestNode(0, 0);
    assertTrue(mapsInfo.contains("\"id\":\"/n/0\""));
    assertTrue(mapsInfo.contains("\"x\":41.82"));
  }

  /**
   * Testing the functionality of route by coordinate
   */
  @Test
  public void getRouteByCoor() {
    String mapsInfo = repl.findPath(41.8, -71.3,
            42, -72);
    List<Way> ways = GSON.fromJson(mapsInfo,
            new TypeToken<List<Way>>() {
            }.getType());
    assertEquals(ways.get(0).getName(), "Chihiro Ave");
    assertEquals(ways.get(0).getType(), "residential");
  }

  /**
   * Testing the functionality of route by street.
   */
  @Test
  public void getRouteByStreet() {
    String mapsInfo = repl.findPath("Sootball Ln", "Chihiro Ave",
            "Sootball Ln", "Yubaba St");
    List<Way> ways = GSON.fromJson(mapsInfo,
            new TypeToken<List<Way>>() {
            }.getType());
    assertEquals(ways.get(0).getName(), "Sootball Ln");
  }

  /**
   * Test all the basic autocorrect functionality.
   */
  @Test
  public void autocorrectFunctionality() {
    String corrections = repl.autocorrect("Soot");
    corrections.contains("Sootball");
  }

  /**
   * Tests basic tile loading in the entire tile.
   */
  @Test
  public void tileAllWayTest() {
    String tile = repl.loadMapTile(42, -72,
            41.8, -71.3);
    assertTrue(tile.contains("Yubaba"));
  }

  /**
   * Test to check that all ways are loaded in when using multiple tiles.
   */
  @Test
  public void multipleTiles() {
    String tile1 = repl.loadMapTile(42, -72,
            41.8, -71.65);
    String tile2 = repl.loadMapTile(42, -71.65,
            41.8, -71.3);
    assertTrue(tile1.contains("Yubaba") || tile2.contains("Yubaba"));
    assertTrue(tile1.contains("Radish Spirit")
            || tile2.contains("Radish Spirit"));
  }

  /**
   * Intersection Test on Big Database
   */
  @Test
  public void intersectionsBigDB(){
    repl.loadDB("data/maps/maps.sqlite3");
    String path = repl.findPath("Thayer Street", "George Street",
            "Benefit Street", "Benevolent Street");
  }

}
