package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import javax.swing.*;



/**
 * element for indacation text informatin for user
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class TextIndicator
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** label, which contains text for player */
  private JLabel indicator = null;
  /** renamed constants from other classes */
  private int BASE_POSITION_X = ShipsLocatingPane.BASE_POSITION_X;
  private int BASE_POSITION_Y = ShipsLocatingPane.BASE_POSITION_Y;
  private int CELLS_IN_SIDE   = Battlefield.CELLS_IN_SIDE;
  private int SIZE_UNIT       = Battlefield.SIZE_UNIT;
  private int SIDE_SIZE       = Battlefield.SIDE_SIZE;
  private int THICKNESS       = Battlefield.THICKNESS;
  private int DISTANCE_UNIT   = ShipsLocatingPane.DISTANCE_UNIT;
  /** killer of warinig */
  static private final long serialVersionUID = 1L;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create #TextIndicator instance
   */
  TextIndicator(){
    //[creating #JPanel]
    super();
    //[setting #JPanel parameters]
    int sizeX = 5*Battlefield.SIZE_UNIT + THICKNESS;
    int sizeY = 2*Battlefield.SIZE_UNIT + THICKNESS;
    setSize(sizeX, sizeY);
    int locationX = BASE_POSITION_X + (CELLS_IN_SIDE - 4)*SIZE_UNIT;
    int locationY = BASE_POSITION_Y + SIDE_SIZE + DISTANCE_UNIT;
    setLocation(locationX, locationY);
    //[creating #JLabel]
    indicator = new JLabel("it is your turn");
    //[adding text label to this indicator]
    add(indicator);
    setVisible(true);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * draw instance of #TextIndicator.
   */
  @Override
  protected void paintComponent(Graphics graphics){
    super.paintComponent(graphics);
    int width  = 5*Battlefield.SIZE_UNIT + THICKNESS;
    int height = 2*Battlefield.SIZE_UNIT + THICKNESS;
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, width, height);
    width  -= 1;
    height -= 1;
    Color lineColor = new Color(200, 200 ,200);
    graphics.setColor(lineColor);
    graphics.drawRect(0, 0, width, height);
  }
  
  /**
   * set text to indicate
   * 
   * @param message  text to indicate
   */
  void setText(String message){
    indicator.setText(message);
  }
}
