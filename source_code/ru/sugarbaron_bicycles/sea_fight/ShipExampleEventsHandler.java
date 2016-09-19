package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.event.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;




/**
 * this class processes mouse events, provided by ships examples
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class ShipExampleEventsHandler
extends MouseAdapter{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to log */
  private LogUnit log;
  /** reference to this object */
  private static ShipExampleEventsHandler instance = null;
  /** reference to processable ship */
  private Ship draggableShip;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * capitan "obvious"
   */
  private ShipExampleEventsHandler()
  throws NeedFixCode{
    super();
    log = LogSubsystem.getLog("system_log.txt");
    //log.write("[v][ShipExampleEventsHandler]instance constructed");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * method #getInstance() realises singleton pattern.
   * 
   * @return reference to ShipExampleEventsHandler object 
   */
  static ShipExampleEventsHandler getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new ShipExampleEventsHandler();
    }
    return instance;
  }
  
  /**
   * create draggable ship at curent coordinates of mouse cursor
   * created ship will becames invisible on "mouse pressed" event.
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void createDraggableShip(MouseEvent event)
  throws NeedFixCode{
    //ship, clicked by user
    Ship sourceShip;
    //x-coordinate of source ship (relatively content pane)
    int sourceShipX;
    //y-coordinate of source ship (relatively content pane)
    int sourceShipY;
    //x-coordinate of mouse cursor (relatively source ship)  
    int mouseX;
    //y-coordinate of mouse cursor (relatively source ship)
    int mouseY;
    //x-coordinate of ship, which we will create (relatively content pane)
    int x;
    //y-coordinate of ship, which we will create (relatively content pane)
    int y;
    //number of cells, occupied by source ship
    int cellsNum;
    //obtaining reference to source ship
    sourceShip = (Ship)event.getSource();
    //obtainig number of cells, occupied by source ship
    cellsNum = sourceShip.getCellsNum();
    //obtaining source ship coordinates
    sourceShipX = sourceShip.getX();
    sourceShipY = sourceShip.getY();
    //obtaining mouse cursor coordinates
    mouseX = event.getX();
    mouseY = event.getY();
    //calculating coordinates of creatable ship
    x = sourceShipX + mouseX;
    y = sourceShipY + mouseY;
    Directions direction = sourceShip.getDirection();
    //creatinng draggable ship and adding it to content pane
    draggableShip = new Ship(x, y, cellsNum, direction);
    Container contentPane;
    contentPane = ThisApplicationWindow.getInstance().getContentPane();
    contentPane.add(draggableShip);
    contentPane.setComponentZOrder(draggableShip, ShipsLocatingPane.Z_INDEX_DRAGGABLE);
  }
  
  /**
   * on mouse pressing event create new ship instance at the same coordinates
   * with clicked ship. this new ship will be draggable by player. 
   */
  @Override
  public void mousePressed(MouseEvent event){
    try{
      log.write("[v][ShipExampleEventsHandler]mouse pressed event");
      //creating draggable ship
      createDraggableShip(event);
      draggableShip.repaint();
    }
    catch(NeedFixCode exception){
      log.write("[x][ShipExampleEventsHandler]NeedFixCode exception");
      log.write("[x][ShipExampleEventsHandler]%s", exception.getMessage());
      return;
    }
  }
  
  /**
   * on mouse releasing event appeared draggable ship must dissapear
   */
  @Override
  public void mouseReleased(MouseEvent event){
    log.write("[v][ShipExampleEventsHandler]player is trying to add new ship");
    //[source ship]
    Ship sourceShip = (Ship)event.getSource();
    //[coordinates, where mouse was released (relatively source ship)]
    int shipMouseX = event.getX();
    int shipMouseY = event.getY();
    //log.write("[v][ShipExampleEventsHandler]shipMouseX #%d", shipMouseX);
    //log.write("[v][ShipExampleEventsHandler]shipMouseY #%d", shipMouseY);
    //[coordinates, where mouse was released (absolut value; 
    //relatively content pane)]
    int absX = shipMouseX + sourceShip.getX();
    int absY = shipMouseY + sourceShip.getY();
    //log.write("[v][ShipExampleEventsHandler]absX #%d", absX);
    //log.write("[v][ShipExampleEventsHandler]absY #%d", absY);
    //[getting ships locating content pane]
    ShipsLocatingPane contentPane;
    Battlefield playerField;
    try{
      contentPane = (ShipsLocatingPane)ThisApplicationWindow.getInstance().getContentPane();
      playerField          = contentPane.getPlayerField();
      int cellsNum         = draggableShip.getCellsNum();
      //[converting absolute coordinates to ships locating area coordainates]
      int playerFieldX     = absX - playerField.getLocation().x;
      int playerFieldY     = absY - playerField.getLocation().y;
      //log.write("[v][ShipExampleEventsHandler]playerFieldX #%d", playerFieldX);
      //log.write("[v][ShipExampleEventsHandler]playerFieldY #%d", playerFieldY);
      int shipHeadX        = playerFieldX - Battlefield.SIZE_UNIT;
      int shipHeadY        = playerFieldY - Battlefield.SIZE_UNIT;
      //log.write("[v][ShipExampleEventsHandler]shipHeadX #%d", shipHeadX);
      //log.write("[v][ShipExampleEventsHandler]shipHeadY #%d", shipHeadY);
      Directions direction = sourceShip.getDirection();
      //[looking for ability of ship positioning at specified coordinates]
      Positions isOkToLocate;
      isOkToLocate = playerField.isAbleToLocate(shipHeadX, shipHeadY, cellsNum, direction, null);
      if(isOkToLocate != Positions._free){
        throw new ExecutionAborted("unable to locate ship");
      }
      log.write("[v][ShipExampleEventsHandler]ok to locate at #%d #%d", shipHeadX, shipHeadY);
      GameXY shipHeadXY = new GameXY(shipHeadX, shipHeadY);
      //log.write("[v][ShipExampleEventsHandler]shipHeadX #%d", shipHeadXY.x);
      //log.write("[v][ShipExampleEventsHandler]shipHeadY #%d", shipHeadXY.y);
      //[looking for ability of adding one more ship of such size]
      int unlocatedShipsQuantity = playerField.getUnlocatedShipsQuantity(cellsNum);
      if(!(unlocatedShipsQuantity > 0)){
        throw new ExecutionAborted("maximum quantity of such ships is reached");
      }
      //[locating ship at specified position]
      Battleship locatableShip = null;
      locatableShip = playerField.locateShip(shipHeadXY, cellsNum, direction, true);
      playerField.highlightLockedArea();
      locatableShip.becameSelected();
      playerField.repaint();
      //[actualizing ships-counters labels]
      contentPane.actualizeShipsCountersLabels();
    }
    catch(NeedFixCode exception){
      log.write("[x][ShipExampleEventsHandler]#NeedFixCode exception");
      log.write("[x][ShipExampleEventsHandler]possible it is wrong arguments for #playerField.locateShip()");
      log.write("[x][ShipExampleEventsHandler]reason: %s", exception.getMessage());
      Dbg.out("[x][ShipExampleEventsHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
    catch(ExecutionAborted exception){
      log.write("[v][ShipExampleEventsHandler]#ExecutionAborted exception");
      log.write("[v][ShipExampleEventsHandler]reason: %s", exception.getMessage());
    }
    finally{
      draggableShip.delete();
      draggableShip = null;
    }
  }
  
  /**
   * handle mouse dragging event
   */
  @Override
  public void mouseDragged(MouseEvent event){
    //ship, clicked by user
    Ship sourceShip;
    //x-coordinate of source ship (relatively content pane)
    int sourceShipX;
    //y-coordinate of source ship (relatively content pane)
    int sourceShipY;
    //x-coordinate of mouse cursor (relatively source ship)  
    int mouseX;
    //y-coordinate of mouse cursor (relatively source ship)
    int mouseY;
    //x-coordinate of ship, which we will create (relatively content pane)
    int x;
    //y-coordinate of ship, which we will create (relatively content pane)
    int y;
    //obtaining reference to source ship
    sourceShip = (Ship)event.getSource();
    //obtaining source ship coordinates
    sourceShipX = sourceShip.getX();
    sourceShipY = sourceShip.getY();
    //obtaining mouse cursor coordinates
    mouseX = event.getX();
    mouseY = event.getY();
    //calculating coordinates of creatable ship
    x = sourceShipX + mouseX;
    y = sourceShipY + mouseY;
    draggableShip.setLocation(x, y);
    draggableShip.repaint();
  }
}