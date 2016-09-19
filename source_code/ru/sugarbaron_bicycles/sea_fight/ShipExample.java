package ru.sugarbaron_bicycles.sea_fight;

import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * type of ship, designed to be choosed by player and dragged to battlefield
 * 
 * @author sugarbaron (sugarbaron1@mail.ru)
 */
final class ShipExample
extends Ship{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** version UID */
  private static final long serialVersionUID = 1L;

  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * creates a ship example with specified size at specified coordinates
   * 
   * @param x             x-coordinate
   * @param y             y-coordinate
   * @param size          ship size (number of cells, occupied by ship)
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  ShipExample(int x, int y, int cellsNum)
  throws NeedFixCode{
    super(x, y, cellsNum, Directions._vertical);
    //[registering events handler]
    ShipExampleEventsHandler eventsHandler;
    eventsHandler = ShipExampleEventsHandler.getInstance();
    addMouseListener(eventsHandler);
    addMouseMotionListener(eventsHandler);
  }
}