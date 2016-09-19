package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import java.awt.event.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class proceeds events, produced by battleships
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class BattleshipEventsHandler
extends MouseAdapter{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  LogUnit log;
  /** reference to this object */
  private static BattleshipEventsHandler instance = null;
  /** flag of dragging */
  private boolean isDragged = false;
  /** dragable ship */
  Ship draggableShip = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * realising singleton pattern
   */
   private BattleshipEventsHandler()
   throws NeedFixCode{
     super();
     log = LogSubsystem.getLog("system_log.txt");
   }
   
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * method #getInstance() realises singleton pattern.
   * 
   * @return reference to ShipExampleEventsHandler object
   */
  static BattleshipEventsHandler getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new BattleshipEventsHandler();
    }
    return instance;
  }
  
  /**
   * create dragable ship instance at current mouse cursor coordinates.
   * it became visible only if mouse was dragged.
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void createDraggableShip(MouseEvent event)
  throws NeedFixCode{
    //[reading source ship parameters]
    Ship       sourceShip = (Ship)event.getSource();
    int        cellsNum   = sourceShip.getCellsNum();
    Directions direction  = sourceShip.getDirection();
    //[getting reference to #ShipsLocatingPane]
    ThisApplicationWindow baseWindow;
    Container             shipsLocatingPane;
    baseWindow        = ThisApplicationWindow.getInstance();
    shipsLocatingPane = baseWindow.getContentPane();
    //[getting coordinates of mouse cursor relative source ship]
    int sourceMouseX = event.getX();
    int sourceMouseY = event.getY();
    //[converting source ship-related coordinates to battlefield-relatd]
    int battlefieldMouseX = sourceMouseX + sourceShip.getX();
    int battlefieldMouseY = sourceMouseY + sourceShip.getY();
    //[converting battlefield-related coordinates to content pane-related]
    Container battlefield = sourceShip.getParent();
    int absX = battlefieldMouseX + battlefield.getX();
    int absY = battlefieldMouseY + battlefield.getY();
    //[creating draggable ship]
    draggableShip = new Ship(absX, absY, cellsNum, direction);
    //[adding just-created ship to content pane]
    shipsLocatingPane.add(draggableShip);
    shipsLocatingPane.setComponentZOrder(draggableShip, ShipsLocatingPane.Z_INDEX_DRAGGABLE);
  }
  
  /**
   * on "mouse pressed" event creating draggableship instance
   */
  @Override
  public void mousePressed(MouseEvent event){
    try{
      //[processing #isDragged flag]
      isDragged = false;
      //[creating dragable ship instance]
      createDraggableShip(event);
    }
    catch(NeedFixCode exception){
      log.write("[x][BattleshipEventsHandler]#NeedFixCode exception");
      log.write("[x][BattleshipEventsHandler]reason: %s", exception.getMessage());
      Dbg.out("[x][BattleshipEventsHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
  }
  
  /**
   * on "mouse releasing" event can happen selecting/deselecting of battleship,
   * or replacing of battleship
   */
  @Override
  public void mouseReleased(MouseEvent event){
    try{
      //[0.creating locals]
      Battleship source = (Battleship)event.getSource();
      ShipsLocatingPane shipsLocatingPane;
      shipsLocatingPane = (ShipsLocatingPane)ThisApplicationWindow.getInstance().getContentPane();
      Battlefield playerField  = shipsLocatingPane.getPlayerField();
      //[1.releasing selecting/deselecting]
      if(!isDragged){
        log.write("[v][BattleshipEventsHandler]player selects/deselects the ship");
        source.changeSelected();
        //[repaint!]
        playerField.repaint();
        throw new ExecutionAborted("current action is selecting");
      }
      //[2.realizing replacing battleship]
      //[2.1.creating locals]
      log.write("[v][BattleshipEventsHandler]player is trying to replace ship");
      int         cellsNum     = source.getCellsNum();
      Directions  direction    = source.getDirection();
      boolean     isSelected   = source.getSelected();
      Positions   isOkToLocate;
      //[coordinates, where mouse was released (relatively source ship)]
      int shipMouseX = event.getX();
      int shipMouseY = event.getY();
      //[converting source ship-related coordinates to battlefield-related]
      int fieldMouseX = shipMouseX + source.getX() - Battlefield.SIZE_UNIT;
      int fieldMouseY = shipMouseY + source.getY() - Battlefield.SIZE_UNIT;
      //[2.2.checking battlefield-related coordinates
      //for ability of locating replaceable ship]
      isOkToLocate = playerField.isAbleToLocate(fieldMouseX, fieldMouseY, cellsNum, direction, source);
      //[checking for players desire to remove ship from battlefield]
      if(isOkToLocate == Positions._outOfBattlefield){
        //[player wants to remove ship from battlefield]
        log.write("[v][BattleshipEventsHandler]player wants to remove ship");
        playerField.deleteShip(source);
        //[actualizing ships-counters labels]
        shipsLocatingPane.actualizeShipsCountersLabels();
        throw new ExecutionAborted("current action is removing");
      }
      if(isOkToLocate != Positions._free){
        throw new ExecutionAborted("unable to locate");
      }
      //[2.3.deleting ship from old position]
      playerField.deleteShip(source);
      //[2.4.locating ship at new position]
      GameXY newPosition = new GameXY(fieldMouseX, fieldMouseY);
      playerField.locateShip(newPosition, cellsNum, direction, isSelected, true);
      playerField.highlightLockedArea();
      //[repaint!]
      playerField.repaint();
    }
    catch(NeedFixCode exception){
      log.write("[x][BattleshipEventsHandler]#NeedFixCode exception");
      log.write("[x][BattleshipEventsHandler]reason: %s", exception.getMessage());
      Dbg.out("[x][BattleshipEventsHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
    catch(ExecutionAborted exception){
      log.write("[v][BattleshipEventsHandler]#ExecutionAborted exception");
      log.write("[v][BattleshipEventsHandler]reason: %s", exception.getMessage());
    }
    finally{
      //[deleting draggable ship in any case]
      draggableShip.delete();
      draggableShip = null;
    }
  }
  
  /**
   * on "mouse dragged" event need to redraw draggable
   * ship at current mouse cursor coordinates
   */
  @Override
  public void mouseDragged(MouseEvent event){
    //[processing #isDragged flag]
    isDragged = true;
    //[initialising local variables]
    Ship sourceShip = (Ship)event.getSource();
    //[getting current mouse cursor coordinates (relative source ship)]
    int sourceMouseX = event.getX();
    int sourceMouseY = event.getY();
    //[converting source ship-related coordinates to battlefield-relatd]
    int battlefieldMouseX = sourceMouseX + sourceShip.getX();
    int battlefieldMouseY = sourceMouseY + sourceShip.getY();
    //[converting battlefield-related coordinates to content pane-related]
    Container battlefield = sourceShip.getParent();
    int absX = battlefieldMouseX + battlefield.getX();
    int absY = battlefieldMouseY + battlefield.getY();
    //[redrawing draggable ship at current coordinates]
    draggableShip.setLocation(absX, absY);
    draggableShip.repaint();
  }
}