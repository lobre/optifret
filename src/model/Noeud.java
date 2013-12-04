package model;


/**
 * Class Noeud
 */
public class Noeud {

  //
  // Fields
  //

  private int id;
  private int x;
  private int y;
  
  //
  // Constructors
  //
  public Noeud () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of id
   * @param newVar the new value of id
   */
  private void setId ( int newVar ) {
    id = newVar;
  }

  /**
   * Get the value of id
   * @return the value of id
   */
  private int getId ( ) {
    return id;
  }

  /**
   * Set the value of x
   * @param newVar the new value of x
   */
  private void setX ( int newVar ) {
    x = newVar;
  }

  /**
   * Get the value of x
   * @return the value of x
   */
  private int getX ( ) {
    return x;
  }

  /**
   * Set the value of y
   * @param newVar the new value of y
   */
  private void setY ( int newVar ) {
    y = newVar;
  }

  /**
   * Get the value of y
   * @return the value of y
   */
  private int getY ( ) {
    return y;
  }

  //
  // Other methods
  //

}
