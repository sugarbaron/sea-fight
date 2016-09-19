package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import javax.swing.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * description of a cell. (#).
 * cell is an atomic particle of a field.
 * cell is an atomic particle of a ship.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru) 
 */
class Cell
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** cell can be empty, or occupied by ship, etc... */
  private States state;
  /** system log link */
  private LogUnit log;
  /** reference to battleship, which occupies this cell */
  private Battleship ship;
  /** flag: shooted/not shooted */
  private boolean isShootedFlag;
  
  /** version UID */
  private final static long serialVersionUID = 1L;
  /** cell side size */
  static final int SIDE_SIZE = 24;
  /** coordinate value of end point of cell side */
  private static final int END_COORD = (SIDE_SIZE-1);
  /** coordinate value of middle point of cell side */
  private static final int MID_COORD = (SIDE_SIZE/2);
  
  /**
   * cell states
   */
  enum States{
    //empty cell
    EMPTY(0),
    //ship cell
    SHIP(1),
    //attacked empty cell
    MISSED(2),
    //attacked ship cell
    DAMAGED(3),
    //selected ship cell
    SELECTED(4),
    //locked cell
    LOCKED(5);
    
    //captain "obvoius"
    private int stateValue;
    //captain "obvoius"
    private States(int value){
      this.stateValue = value;
    }
    //captain "obvoius"
    int getValue(){
      return this.stateValue;
    }
  }
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * cell constructor. creates cell at point with specified coordinates.
   * 
   * @param x            x-coordinate of left upper corner
   * @param y            y-coordinate of left upper corner
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Cell(int coordX, int coordY)
  throws NeedFixCode{
    super(null);
    //01.getting log
    log = LogSubsystem.getLog("system_log.txt");
    //02.checking arguments validation
    if((coordX < 0)||(coordY < 0)){
      log.write("[x][Cell]wrong arguments:");
      log.write("[x][Cell]x #%d, y #%d", coordX, coordY);
      log.write("[x][Cell]exception: NeedFixCode");
      throw new NeedFixCode("[x][Cell]wrong arguments");
    }
    //03.initializing class members
    state         = States.EMPTY;
    ship          = null;
    isShootedFlag = false;
    //04.setting cell parameters
    setOpaque(true);
    setSize(SIDE_SIZE, SIDE_SIZE);
    //05.locating cell
    setLocation(coordX, coordY);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * repaint cell. calling by system.
   */
  @Override
  protected void paintComponent(Graphics graphics){
    super.paintComponent(graphics);
    setBackground(Color.white);
    switch(state){
    case EMPTY:
      drawEmptyCell(graphics);
      break;
    case SHIP:
      drawShipCell(graphics);
      break;
    case MISSED:
      drawMissedCell(graphics);
      break;
    case DAMAGED:
      drawDamagedCell(graphics);
      break;
    case SELECTED:
      drawSelectedCell(graphics);
      break;
    case LOCKED:
      drawLockedCell(graphics);
      break;
    default:
      log.write("[x][Cell]wrong value of #state. it is #%d", state.getValue());
      log.write("[x][Cell]NeedFixCode");
      break;
    }
  }
  
  /**
   * set sell state
   * 
   * @param newState  state for setting
   */
  void setState(States newState){
    if(state.getValue() == newState.getValue()){
      return;
    }
    state = newState;
  }
  
  /**
   * get cell state
   * 
   * @return current cell state
   */
  States getState(){
    return state;
  }
  
  /**
   * get reference to battleship, which occupies this cell.
   * 
   * @return reference to battleship, which occupies this cell.
   *         returns "null" if cell is not a part of any ship.
   */
  Battleship partOf(){
    return ship;
  }
   
   
  /**
   * save reference to battleship, which occupies this cell.
   * 
   * @param ship  reference to battleship, which occupies this cell.
   */
  void becamePartOf(Battleship ship){
    this.ship = ship;
  }
  
  /**
   * set #isShooted flag
   * 
   * @param value  value for setting
   */
  void setShooted(boolean value){
    isShootedFlag = value;
  }
  
  /**
   * get #isShooted flag value
   * 
   * @return #isShooted flag value
   */
  boolean isShooted(){
    return isShootedFlag;
  }
  
  //secondary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * captain "obvoius"
   */
  private void drawEmptyCell(Graphics graphics){
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
  }
  
  /**
   * captain "obvoius"
   */
  private void drawShipCell(Graphics graphics){
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
  }

  /**
   * captain "obvoius"
   */
  private void drawMissedCell(Graphics graphics){
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
    graphics.setColor(Color.black);
    graphics.drawLine(0, SIDE_SIZE, SIDE_SIZE, 0);
    graphics.drawLine(0, MID_COORD-1, MID_COORD-1, 0);
    graphics.drawLine(MID_COORD, SIDE_SIZE, SIDE_SIZE, MID_COORD);
  }

  /**
   * captain "obvoius"
   */
  private void drawDamagedCell(Graphics graphics){
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
    graphics.setColor(Color.black);
    graphics.drawLine(0, 0, END_COORD, END_COORD);
    graphics.drawLine(0, END_COORD, END_COORD, 0);
  }
  
  /**
   * captain "obvoius"
   */
  private void drawSelectedCell(Graphics graphics){
    Color cellColor = new Color(200, 200, 200);
    graphics.setColor(cellColor);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
  }
  
  /**
   * captain "obvoius"
   */
  private void drawLockedCell(Graphics graphics){
    Color cellColor = new Color(230, 230, 230);
    graphics.setColor(cellColor);
    graphics.fillRect(0, 0, SIDE_SIZE, SIDE_SIZE);
  }
}
