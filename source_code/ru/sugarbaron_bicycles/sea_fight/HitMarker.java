package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class describes a hit marker
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class HitMarker
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** thickness of ship lines */
  static private final int THICKNESS = Ship.THICKNESS;
  /** hit marker side size (in pixels) */
  static private final int SIDE_SIZE = Cell.SIDE_SIZE + 2*THICKNESS;
  /** serial version uid */
  private static final long serialVersionUID = 1L;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create hit marker
   * 
   * @param x  x screen coordinate (relative parent container)
   * @param y  y screen coordinate (relative parent container)
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  HitMarker(int x, int y)
  throws NeedFixCode{
    //[calling #JPanel constructor]
    super();
    //[configuring #JPanel settings]
    setSize(SIDE_SIZE, SIDE_SIZE);
    setLocation(x, y);
    setBackground(Color.black);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * repaint hit marker.
   */
  @Override
  protected void paintComponent(Graphics graphics){
    super.paintComponent(graphics);
    graphics.setColor(Color.white);
    graphics.fillRect(THICKNESS, THICKNESS, Cell.SIDE_SIZE, Cell.SIDE_SIZE);
    graphics.setColor(Color.black);
    graphics.drawLine(THICKNESS, THICKNESS, Cell.SIDE_SIZE-1, Cell.SIDE_SIZE-1);
    graphics.drawLine(THICKNESS, Cell.SIDE_SIZE-1, Cell.SIDE_SIZE-1, THICKNESS);
  }
}
