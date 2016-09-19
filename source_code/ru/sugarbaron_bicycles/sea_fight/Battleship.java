package ru.sugarbaron_bicycles.sea_fight;

import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class describes ship, located on the battlefield
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
public final class Battleship
extends Ship{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** version UID */
  private static final long serialVersionUID = 1L;
  /** flag: selected/deselected */
  private boolean isSelected = false;
  /** flag: dead/alive */
  private boolean isDead = false;
  /** game coordinates of a ship */
  private GameXY gameXY;
  /** events handler */
  private BattleshipEventsHandler eventsHandler;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * creates a battleship with specified size at specified coordinates
   * 
   * @param x             x screen coordinate (relative battlefield)
   * @param y             y screen coordinate (relative battlefield)
   * @param size          ship size (number of cells, occupied by ship)
   * @param direction     ship direction
   * @param isVisible     battleship visiblity flag
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Battleship(int x, int y, int cellsNum, Directions direction, boolean isVisible)
  throws NeedFixCode{
    super(x, y, cellsNum, direction);
    setVisible(isVisible);
    //[1.initialising fields]
    gameXY = new GameXY((x-Battlefield.SIZE_UNIT), (y-Battlefield.SIZE_UNIT));
    //[2.registering events handler]
    eventsHandler = BattleshipEventsHandler.getInstance();
    addMouseListener(eventsHandler);
    addMouseMotionListener(eventsHandler);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * captain "obvious"
   */
  void becameSelected(){
    for(int i=0; i<cellsNum; i++){
      cells[i].setState(Cell.States.SELECTED);
    }
    isSelected = true;
    //deselecting other
    Battlefield parentField = (Battlefield)getParent();
    parentField.deselectOther(this);
  }
  
  /**
   * captain "obvious"
   */
  void becameDeselected(){
    for(int i=0; i<cellsNum; i++){
      cells[i].setState(Cell.States.SHIP);
    }
    isSelected = false;
  }
  
  /**
   * captain "obvious"
   */
  void changeSelected(){
    if(isSelected){
      becameDeselected();
    }else{
      becameSelected();
    }
  }
  
  /**
   * captain "obvious"
   */
  boolean getSelected(){
    return isSelected;
  }
  
  /**
   * get game coordinates
   */
  public GameXY getGameXY(){
    return gameXY;
  }
  
  /**
   * remove ship events handler 
   */
  void removeEventsHandler(){
    if(eventsHandler != null){
      removeMouseListener(eventsHandler);
      removeMouseMotionListener(eventsHandler);
    }
  }
  
  /**
   * check for only one last undamaged cell left 
   * 
   * @return "true" if there is only one undamaged cell,
   *         (it means the ship will be destroyed), because this method
   *         is invoked only in case of hit of the ship
   *         otherwise returns "false"
   */
  boolean isLastUnbrokenCell(){
    int undamagedCells = 0;
    for(int i=0; i<cellsNum; i++){
      if(cells[i].getState() == Cell.States.SHIP){
        ++undamagedCells;
      }
    }
    if(1 == undamagedCells){
      isDead = true;
      return true;
    }else{
      return false;
    }
  }
  
  /**
   * check, is the ship destroyed.
   * 
   * @return "true" if the ship is destroyed,
   *         otherwise returns "false"
   */
  boolean isDestroyed(){
    return isDead;
  }
  
  /**
   * highlight damaged cell, specified by hit coordinates
   * 
   * @param hit  game coordinates of hit
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void highlightHit(GameXY hit)
  throws NeedFixCode{
    //[creating locals]
    int idx = -1;
    //[checking argumets validation]
    if(Directions._vertical == direction){
      if(hit.x != gameXY.x){
        throw new NeedFixCode("[x][Battleship]#highlightHit():#aim is out of ship (x)");
      }
      idx = hit.y - gameXY.y;
    }else{
      if(hit.y != gameXY.y){
        throw new NeedFixCode("[x][Battleship]#highlightHit():#aim is out of ship (y)");
      }
      idx = hit.x - gameXY.x;
    }
    if(idx < 0){
      throw new NeedFixCode("[x][Battleship]#highlightHit():#aim is out of ship (i)");
    }
    //[highlighting hit]
    cells[idx].setState(Cell.States.DAMAGED);
    //[profit!]
  }
}
