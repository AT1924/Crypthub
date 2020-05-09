package edu.brown.cs.term.general;

import java.util.Objects;

/**
 * Holds 2 values of type T and E.
 *
 * @param <T> first entry of the tuple of type T
 * @param <E> second entry of the tuple of type E
 */
public class Tuple<T, E> {
  private T firstEntry;
  private E secondEntry;

  /**
   * Constructor for Tuple.
   *
   * @param first  first entry to be held in the tuple.
   * @param second seocnd entry to be held in the tuple.
   */
  public Tuple(T first, E second) {
    firstEntry = first;
    secondEntry = second;
  }

  /**
   * Gets the first entry of the Tuple.
   *
   * @return first entry of type T.
   */
  public T getFirstEntry() {
    return firstEntry;
  }

  /**
   * Gets the second entry of the Tuple.
   *
   * @return second entry of type E.
   */
  public E getSecondEntry() {
    return secondEntry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tuple<?, ?> tuple = (Tuple<?, ?>) o;
    return Objects.equals(firstEntry, tuple.firstEntry)
      && Objects.equals(secondEntry, tuple.secondEntry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstEntry, secondEntry);
  }

  @Override
  public String toString() {
    return "(" + firstEntry + "," +secondEntry + ")";
  }
}
