package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class describes a ship
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
class Ship
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** version UID */
  private static final long serialVersionUID = 1L;
  /** thickness of ship lines */
  static final int THICKNESS = 1;
  /** log link */
  protected LogUnit log;
  /** ship size (number of cells, occupied by ship) */
  protected int cellsNum;
  /** ship cells array */
  protected Cell[] cells;
  /** direction (horizontal/vertical) */
  protected Directions direction;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * creates a ship with specified size at specified coordinates
   * 
   * @param x             x screen coordinate (relative parent container)
   * @param y             y screen coordinate (relative parent container)
   * @param size          ship size (number of cells, occupied by ship)
   * @param direction     ship direction
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Ship(int x, int y, int cellsNum, Directions direction)
  throws NeedFixCode{
    //00.calling superclass constructor
    super(null);
    //01.obtaining log link
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][Ship]constructing Ship");
    //02.checking arguments validation
    if((x < 0) || (y < 0)){
      log.write("[x][Ship]wrong arguments. x #%d, y#%d", x, y);
      throw new NeedFixCode("[x][Ship]wrong arguments #x or #y");
    }
    if((cellsNum < 1) || (cellsNum > 4)){
      log.write("[x][Ship]wrong aruments. size #%d", cellsNum);
      throw new NeedFixCode("[x][Ship]wrong argument #size");
    }
    //03.initializing fields
    this.cellsNum = cellsNum;
    this.cells = new Cell[cellsNum];
    this.direction = direction;
    //04.configuring jpanel settings
    int xDimension;
    int yDimension;
    if(direction == Directions._vertical){
      xDimension = Cell.SIDE_SIZE + 2*THICKNESS;
      yDimension = cellsNum*(Cell.SIDE_SIZE + THICKNESS) + THICKNESS;
    }else{
      xDimension = cellsNum*(Cell.SIDE_SIZE + THICKNESS) + THICKNESS;
      yDimension = Cell.SIDE_SIZE + 2*THICKNESS;
      
    }
    setSize(xDimension, yDimension);
    setLocation(x, y);
    setBackground(Color.black);
    //05.constructing ship using cells
    for(int i=0; i<cellsNum; i++){
      Cell shipCell;
      int location = THICKNESS + i*(Cell.SIDE_SIZE + THICKNESS);
      if(direction == Directions._vertical){
        shipCell = new Cell(THICKNESS, location);
      }else{
        shipCell = new Cell(location, THICKNESS);
      }
      shipCell.setState(Cell.States.SHIP);
      add(shipCell);
      cells[i] = shipCell;
    }
    log.write("[v][Ship]instance constructed");
  }
  
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * we are using swing framework, so we need to override #paintComponent()
   * for viewing battlefield
   * 
   * @param graphics  graphics context
   */
  @Override
  protected void paintComponent(Graphics graphics){
    super.paintComponent(graphics);
  }
  
  /**
   * deletes ship
   */
  void delete(){
    setVisible(false);
  }
  
  /**
   * get direction
   */
  public Directions getDirection(){
    return direction;
  }
  
  /**
   * captain "obvious"
   */
  public int getCellsNum(){
    return cellsNum;
  }
}
