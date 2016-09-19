package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * game field. player game field with player ships and enemy game field
 * with enemy ships are examples of #Battlefield. 
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
public final class Battlefield
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** battlefield side size (quantity of cells, contained in side) */
  static public final int CELLS_IN_SIDE = 10;
  /** thickness of division lines */
  static final int THICKNESS = Ship.THICKNESS;
  /** for easyment we will define size unit */
  static final int SIZE_UNIT = THICKNESS + Cell.SIDE_SIZE;
  /** battlefield side size (in pixels) */
  static final int SIDE_SIZE = ((CELLS_IN_SIDE + 1)*SIZE_UNIT) + THICKNESS;
  /** default z-order value of components */
  static private final int Z_INDEX_DEFAULT = 1;
  /** default z-order value of components */
  static final int Z_INDEX_SHIP = 0;
  /** total ships quantity */
  static private final int TOTAL_SHIPS_QUANTITY = 10;
  /** version UID */
  static final long serialVersionUID = 1L;
  
  /** current ships quantity */
  private int currentShipsQuantity = 0;
  /** battlefield cells array */
  private Cell[][] cells = new Cell[CELLS_IN_SIDE][CELLS_IN_SIDE];
  /** battlefield ships array */
  private Battleship[] ships = null; 
  /** array of ship counters(for each type(by cells number) of ships) */
  private int[] unlocatedShips;
  /** log link */
  private LogUnit log;
  
  //costructors_section________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create battlefield
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Battlefield()
  throws NeedFixCode{
    super(null);
    //01.getting link to system log
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][Battlefield]constructing Battlefield");
    //02.adjusting base settings
    Color linesColor = new Color(200, 200, 200); 
    setBackground(linesColor);
    setOpaque(true);
    setSize(SIDE_SIZE, SIDE_SIZE);
    //03.initializing fields
    ships = new Battleship[TOTAL_SHIPS_QUANTITY];
    unlocatedShips = new int[4];
    resetUnlocatedShipsQuantity();
    //04.creating left upper cell
    Cell leftUpper = new Cell(THICKNESS, THICKNESS);
    add(leftUpper);
    //05.creating cells of ships locating area
    for(int vertical=0; vertical<CELLS_IN_SIDE; vertical++){
      //x-coordinate of left upper corner of a cell
      int xCoord;
      //y-coordinate of left upper corner of a cell
      int yCoord;
      for(int horisontal=0; horisontal<CELLS_IN_SIDE; horisontal++){
        xCoord = THICKNESS + ((horisontal + 1)*SIZE_UNIT);
        yCoord = THICKNESS + ((vertical   + 1)*SIZE_UNIT);
        cells[horisontal][vertical] = new Cell(xCoord, yCoord);
        add(cells[horisontal][vertical]);
        //configuring z order
        setComponentZOrder(cells[horisontal][vertical], Z_INDEX_DEFAULT);
      }
    }
    //06.creating coordinate letters
    char          xCoordName = 'a';
    int           yCoordName = 1;
    int           labelsize  = Cell.SIDE_SIZE-4;
    int           offset     = SIZE_UNIT;
    LayoutManager layout     = new FlowLayout();
    Cell          xCoordCell;
    Cell          yCoordCell;
    JLabel        xCoordinate;
    JLabel        yCoordinate;
    String        xTxt;
    String        yTxt;
    for(int i=0; i<CELLS_IN_SIDE; i++){
      //creating coordinate cells
      int location = THICKNESS + (i+1)*offset;
      xCoordCell = new Cell(location, THICKNESS);
      yCoordCell = new Cell(THICKNESS, location);
      xCoordCell.setLayout(layout);
      yCoordCell.setLayout(layout);
      //creating coordinate labels
      xTxt = new String(String.valueOf(xCoordName));
      yTxt = new String(String.valueOf(yCoordName));
      xCoordinate = new JLabel(xTxt);
      yCoordinate = new JLabel(yTxt);
      //setting size of coordinate labels
      yCoordinate.setSize(labelsize, labelsize);
      //adding coordinate labels to coordinate cell
      xCoordCell.add(xCoordinate);
      yCoordCell.add(yCoordinate);
      //adding coordinate cells to battlefield
      add(xCoordCell);
      add(yCoordCell);
      //preparing coordinate labels text to next iteration
      ++xCoordName;
      ++yCoordName;
    }
    log.write("[v][Battlefield]instance constructed");
  }
  
  /**
   * create battlefield at specified coordinates of parent component
   * 
   * @param x  x-coordinate of left upper corner of battlefield
   * @param y  y-coordinate of left upper corner of battlefield
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Battlefield(int x, int y)
  throws NeedFixCode{
    //01.constructing battlefield
    this();
    //02.checking arguments validation
    if((x<0) || (y<0)){
      log.write("[x][Battlefield]wrong arguments. x #%d, y #%d", x, y);
      throw new NeedFixCode("[x][Battlefield]wrong arguments");
    }
    //03.setting location
    setLocation(x, y);
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
   * define ability of location a ship at specified game coordinates
   * 
   * @param head       game coordinates of head cell of battleship
   * @param cellsNum   size (in cells) of locatable ship
   * @param direction  direction of battleship
   * @param deletable  reference to a ship, which will be deleted in case
   *                   of successfull locating of a ship, this check is making for
   * 
   * @return if it is ok to locate a ship at specified game coordinates,
   *         returns "Positions._free";
   *         if specified coordinates are out of battlefield, 
   *         returns "Positions._outOfBattlefield";
   *         othervise return "Positions._locked"
   */
  Positions isAbleToLocate(
  GameXY     head,
  int        cellsNum,
  Directions direction,
  Battleship deletable){
    //[method body begins]
    Positions checkableCellState;
    GameXY checkableCellXY = new GameXY(-1, -1);
    checkableCellXY.x = head.x;
    checkableCellXY.y = head.y;
    for(int i=0; i<cellsNum; i++){
      log.write("[v][Battlefield]checkableCellX #%d", checkableCellXY.x);
      log.write("[v][Battlefield]checkableCellY #%d", checkableCellXY.y);
      checkableCellState = isOccupiedByOtherShip(deletable, checkableCellXY);
      if(checkableCellState != Positions._free){
        //[#checkableCellState can be #_locked or #_outOfBattlefield]
        log.write("[v][Battlefield]locked");
        return checkableCellState;
      }
      //incrementing coordinate for checking next cell at next iteration
      if(direction == Directions._vertical){
        ++checkableCellXY.y;
      }else{
        ++checkableCellXY.x;
      }
    }
    log.write("[v][Battlefield]free");
    return Positions._free;
    //[method body ends]
  }
  
  /**
   * define ability of location a ship at specified content pane coordinates
   * 
   * @param headX      x-coordinate (in pixels) of head cell of battleship
   *                   (relatively content pane)
   * @param headY      y-coordinate (in pixels) of head cell of battleship
   *                   (relatively content pane)
   * @param cellsNum   size (in cells) of locatable ship
   * @param direction  direction of battleship
   * @param deletable  reference to a ship, which will be deleted in case
   *                   of successfull locating of a ship, this check is 
   *                   making for
   * 
   * @return if it is ok to locate a ship at specified game coordinates,
   *         returns "Positions._free";
   *         if specified coordinates are out of battlefield, 
   *         returns "Positions._outOfBattlefield";
   *         othervise return "Positions._locked"
   */
  Positions isAbleToLocate(
  int        headX,
  int        headY,
  int        cellsNum,
  Directions direction,
  Battleship deletable){
    //handling situation, when #futureXY
    //specifies cell, which is out of battlefield
    int leftest  = 0;
    int upperst  = 0;
    int rightest = (CELLS_IN_SIDE*SIZE_UNIT)+THICKNESS;
    int lowerst  = (CELLS_IN_SIDE*SIZE_UNIT)+THICKNESS;
    if((headX < leftest )||
       (headY < upperst )||
       (headX > rightest)||
       (headY > lowerst )){
      //checkable coordinates are out of battlefield
      log.write("[v][Battlefield]x #%d y #%d", headX, headY);
      log.write("[v][Battlefield]xmin #%d xmax #%d", leftest, rightest);
      log.write("[v][Battlefield]ymin #%d ymax #%d", upperst, lowerst);
      log.write("[v][Battlefield]specified coordinates are out of battlefield");
      return Positions._outOfBattlefield;
    }
    GameXY headXY = new GameXY(headX, headY);
    return isAbleToLocate(headXY, cellsNum, direction, deletable);
  }
  
  /**
   * locates ship in cells according specified coordinates
   * 
   * @param head          game coordinates of head cell of battleship   
   * @param cellsNum      ship size (in cells)
   * @param direction     ship direction
   * @param isSelected    makes locatable ship selected if "true"
   * @param isVisible     locatable ship visiblity flag
   * 
   * @return reference to just-located ship
   * 
   * @throws NeedFixCode       if parameters are wrong, or
   *                           if was detected wrong work of a
   *                           programm, because of errors in code.
   *                      
   * @throws ExecutionAborted  if ship was not located because of coordinates
   *                           of ship head are out of battlefield
   */
  Battleship locateShip(GameXY head, int cellsNum, Directions direction, boolean isSelected, boolean isVisible)
  throws NeedFixCode, ExecutionAborted{
    //01.checking arguments validation
    if(null == head){
      log.write("[x][Battlefield]wrong arument. head is null");
      throw new NeedFixCode("#locateShip():#head is null");
    }
    if((head.x < 0) || (head.y < 0)){
      //specified coordinates is out of battlefield
      log.write("[v][Battlefield]is out of battlefield. head #[%d %d]", head.x, head.y);
      throw new ExecutionAborted("#locateShip():#head is out of battlefield");
    }
    if((head.x >= CELLS_IN_SIDE)||(head.y >= CELLS_IN_SIDE)){
      //specified coordinates is out of battlefield
      log.write("[v][Battlefield]is out of battlefield. head #[%d %d]", head.x, head.y);
      throw new ExecutionAborted("#locateShip():#head is out of battlefield");
    }
    if((cellsNum < 1) || (cellsNum > 4)){
      log.write("[x][Battlefield]wrong arument. cellsNum #%d", cellsNum);
      throw new NeedFixCode("#locateShip():wrong argument #cellsNum");
    }
    //02.locating battleship
    Cell choosedByUser = cells[head.x][head.y];
    int shipX = choosedByUser.getX() - THICKNESS;
    int shipY = choosedByUser.getY() - THICKNESS;
    Battleship locatableShip = new Battleship(shipX, shipY, cellsNum, direction, isVisible);
    ships[currentShipsQuantity] = locatableShip;
    ++currentShipsQuantity;
    actualizeShipsCounters();
    add(locatableShip);
    if(isSelected){
      locatableShip.becameSelected();
    }
    setComponentZOrder(locatableShip, Z_INDEX_SHIP);
    //03.occupied cells must store information about occupant
    for(int i=0; i<cellsNum; i++){
      int x = head.x;
      int y = head.y;
      if(direction == Directions._vertical){
        y += i;
      }else{
        x += i;
      }
      cells[x][y].becamePartOf(locatableShip);
    }
    //04.profit!
    return locatableShip;
  }
  
  /**
   * locates ship in cells according specified coordinates
   * 
   * @param head          game coordinates of head cell of battleship   
   * @param cellsNum      ship size (in cells)
   * @param direction     ship direction
   * @param isVisible     locatable ship visiblity flag
   * 
   * @return reference to just-located ship
   * 
   * @throws NeedFixCode       if parameters are wrong, or
   *                           if was detected wrong work of a
   *                           programm, because of errors in code.
   *                      
   * @throws ExecutionAborted  if ship was not located because of coordinates
   *                           of ship head are out of battlefield
   */
  Battleship locateShip(GameXY head, int cellsNum, Directions direction, boolean isVisible)
  throws NeedFixCode, ExecutionAborted{
    return locateShip(head, cellsNum, direction, false, isVisible);
  }
  
  /**
   * check for variants of possible ship location after rotating and try to
   * locate rotated ship
   * 
   * @throws ExecutionAborted  if selected ship was not rotated because of
   *                           there is no selected ships, or if
   *                           there is no place for locating ship after rotating
   * 
   * @throws NeedFixCode       if parameters are wrong, or
   *                           if was detected wrong work of a
   *                           programm, because of errors in code.
   */
  void tryToRotateShip()
  throws ExecutionAborted, NeedFixCode{
    //[01.checking for selected ship]
    Battleship selectedShip = findSelectedShip();
    if(null == selectedShip){
      //nothing to rotate
      log.write("[v][Battlefield]nothing to rotate. no one ship is selected");
      throw new ExecutionAborted("#tryToRotateShip():no one ship is selected");
    }
    log.write("[v][Battlefield]there is selected ship");
    //[02.searching for variants of possible ship location.]
    //coordinates of ship position after rotation:
    GameXY foundPosition = null;
    //switching variants of possible ship locations area:
    for(int areaPriority=0; areaPriority<selectedShip.getCellsNum(); areaPriority++){
      //#areaPriority is for changing order of checking variants
      int area = selectedShip.getCellsNum() - (areaPriority + 2);
      if(area < 0){
        area = selectedShip.getCellsNum() - 1;
      }
      //switching variants of possible locations of a ship inside current area
      for(int locationPriority=0; locationPriority<selectedShip.getCellsNum(); locationPriority++){
        //#locationPriority is for changing order of checking variants
        int location = locationPriority + 1;
        if(location == selectedShip.getCellsNum()){
          location = 0;
        }
        log.write("[v][Battlefield]checking variant:area #%d location #%d", area, location);
        foundPosition = checkVariant(selectedShip, area, location);
        if(null == foundPosition){
          //if checkable variant is bad, need to check next variant
          log.write("[v][Battlefield]bad variant");
          continue;
        }else{
          //stop searching. need to locate
          //selected ship at found position
          log.write("[v][Battlefield]good variant");
          log.write("[v][Battlefield]head: #%d #%d", foundPosition.x, foundPosition.y);
          break;
        }
      }//endof: for(location...)
      if(foundPosition != null){
        //stop searching. need to locate
        //selected ship at found position
        break;
      }
    }//endof: for(area...)
    //[03.search result reaction]
    if(null == foundPosition){
      //unable to rotate selected ship
      log.write("[v][Battlefield]variant is not found. unable to rotate");
      throw new ExecutionAborted("#tryToRotateShip():unable to rotate");
    }
    //[following code is only for case, when was found good variant of ship
    //position after rotating]
    //[04.defining ship direction after rotating]
    Directions direction;
    if(selectedShip.getDirection() == Directions._vertical){
      direction = Directions._horizontal;
    }else{
      direction = Directions._vertical;
    }
    int cellsNum = selectedShip.getCellsNum();
    //[05.deleting old ship view]
    log.write("[v][Battlefield]ships quantity #%d", currentShipsQuantity);
    log.write("[v][Battlefield]deleting ship before rotating");
    deleteShip(selectedShip);
    selectedShip = null;
    log.write("[v][Battlefield]ships quantity #%d", currentShipsQuantity);
    //[06.locating ship at found coordinates]
    log.write("[v][Battlefield]locating ship after rotating");
    locateShip(foundPosition, cellsNum, direction, true);
    highlightLockedArea();
    log.write("[v][Battlefield]ships quantity #%d", currentShipsQuantity);
    ships[currentShipsQuantity-1].becameSelected();
    //[repaint!]
    repaint();
  }
  
  /**
   * deselect all battleships except specified
   * 
   * @param selected  reference to battleship, which will not became deselected
   */
  void deselectOther(Battleship selected){
    for(int i=0; i<currentShipsQuantity; i++){
      if(ships[i] != selected){
        ships[i].becameDeselected();
      }
    }
  }
  
  /**
   * deselect all battleships except specified
   */
  void deselectAll(){
    for(int i=0; i<currentShipsQuantity; i++){
      ships[i].becameDeselected();
    }
  }
  
  /**
   * delete ship
   * 
   * @param deletable     deletable ship
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void deleteShip(Battleship deletable)
  throws NeedFixCode{
    boolean isDeleted = false;
    //searching for ship to delete
    for(int i=0; i<currentShipsQuantity; i++){
      if(ships[i] == deletable){
        //found the ship to delete
        ships[i].delete();
        isDeleted = true;
        continue;
      }
      if(isDeleted){
        //if the ship was deleted, filling "hole" in #ships[] array
        ships[i-1] = ships[i];
      }
    }
    if(!isDeleted){
      log.write("[x][Battlefield]deletable ship was not found");
      throw new NeedFixCode("[x][Battlefield]deletable ship was not found");
    }
    --currentShipsQuantity;
    ships[currentShipsQuantity] = null;
    actualizeShipsCounters();
    //unlocking cells around old location
    int cellsNum = deletable.getCellsNum();
    for(int i=0; i<cellsNum; i++){
      GameXY  deletableShipHeadXY = deletable.getGameXY();
      int x = deletableShipHeadXY.x;
      int y = deletableShipHeadXY.y;
      if(deletable.getDirection() == Directions._vertical){
        y += i;
      }else{
        x += i;
      }
      cells[x][y].becamePartOf(null);
    }
    //highlighting unlocked cells
    highlightUnlockedArea(deletable);
    return;
  }
  
  /**
   * define, are there selected ships or not
   * 
   * @return  if there was found selected ship, returns reference to it
   *          otherwise returns "null"  
   */
  Battleship findSelectedShip(){
    for(int i=0; i<currentShipsQuantity; i++){
      if(true == ships[i].getSelected()){
        return ships[i];
      }
    }
    return null;
  }
  
  /**
   * get current quantity of ships with specified cells number
   * 
   * @param cellsNum  cells number
   * 
   * @return current quantity of ships with specified cells number
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  int getUnlocatedShipsQuantity(int cellsNum)
  throws NeedFixCode{
    //[checking arguments validation]
    if((cellsNum < 1)||(cellsNum > 4)){
      log.write("[x][Battlefield]#NeedFixCode exception");
      log.write("[x][Battlefield]wrong argument for #getQuantityOf()");
      log.write("[x][Battlefield]argument is #%d", cellsNum);
      throw new NeedFixCode("[x][Battlefield]wrong argument for #getQuantityOf()");
    }
    int idx = cellsNum - 1;
    return unlocatedShips[idx];
  }
  
  /**
   * check: are all ships located, or not
   * 
   * @return "true" if all ships are located at battlefield,
   *          othervise returns "false"
   */
  boolean isAllShipsLocated(){
    if(currentShipsQuantity == TOTAL_SHIPS_QUANTITY){
      return true;
    }else{
      return false;
    }
  }
  
  /**
   * switch off highlighting of locked cells at battlefield
   */
  void switchOffLockedHighlighting(){
    for(int y=0; y<CELLS_IN_SIDE; y++){
      for(int x=0; x<CELLS_IN_SIDE; x++){
        cells[x][y].setState(Cell.States.EMPTY);
      }
    }
    //[repaint!]
    repaint();
  }
  
  /**
   * remove events handlers form all ships, located on battlefield.
   */
  void removeShipsEventsHandlers(){
    for(int i=0; i<currentShipsQuantity; i++){
      ships[i].removeEventsHandler();
    }
  }
  
  /**
   * handle shot. check, is there a ship at specified cell,
   * and define the result of the shot: miss, hit, destruction
   * 
   * @param coords  game coordinates of a cell to check
   * 
   * @return shot result
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  ShotResults handleShot(GameXY coords)
  throws NeedFixCode{
    ShotResults thisShotResult = null;
    try{
      //[setting #isShooted flag]
      cells[coords.x][coords.y].setShooted(true);
      //[checking for #_miss]
      if(null == cells[coords.x][coords.y].partOf()){
        thisShotResult = ShotResults._miss;
        throw new ExecutionAborted("[v][Battlefield]#handleShot():result is #_miss. task complete.");
      }
      //[if execution have reached this line of code, it means
      // that shot result is #_hit or #_destruction]
      //[searching for shooted ship]
      int idx = findShipIdx(cells[coords.x][coords.y].partOf());
      //[defining, is specified ship destroyed?]
      if(ships[idx].isLastUnbrokenCell()){
        thisShotResult = ShotResults._destruction;
        log.write("[v][Battlefield]#handleShot():result is #_destruction");
      }else{
        thisShotResult = ShotResults._hit;
        log.write("[v][Battlefield]#handleShot():result is #_hit");
      }
    }
    catch(ExecutionAborted exception){
      log.write("[v][Battlefield]#handleShot():#ExecutionAborted exception");
      log.write("[v][Battlefield]exception message:#%s", exception.getMessage());
    }
    //[highlighting shot result on the battlefield]
    highlightShotResults(coords, thisShotResult);
    return thisShotResult;
  }
  
  /**
   * highlight locked area around all ships
   */
  void highlightLockedArea(){
    for(int i=0; i<currentShipsQuantity; i++){
      highlightLockedArea(ships[i]);
    }
  }
  
  /**
   * check for undestroyed battleships
   * 
   * @return "true" if all battleships are destroyed,
   *         othrewise returns "false"
   */
  public boolean isAllShipsDestroyed(){
    for(Battleship ship: ships){
      if(!ship.isDestroyed()){
        return false;
      }
    }
    return true;
  }
  
  /**
   * get reference to a ship, which is located at specified
   * game coordinates
   * 
   * @param cell  game coordinates of a cell for checking of occupation
   *              this cell by ship
   * 
   * @return reference to a ship, which is located at specified
   *         game coordinates
   */
  public Battleship getShip(GameXY cell){
    int x = cell.x;
    int y = cell.y;
    return cells[x][y].partOf();
  }
  
  /**
   * check specified cell: is it already shooted?
   * 
   * @param checkable  game coordinates of checkable cell
   * 
   * @return "true" if specified cell is already shooted,
   *         otherwise returns "false"
   */
  boolean isCellAlreayShooted(GameXY checkable){
    return cells[checkable.x][checkable.y].isShooted();
  }
  
  /**
   * show ships. make all ships visible
   */
  void showShips(){
    for(int i=0; i<currentShipsQuantity; i++){
      ships[i].setVisible(true);
    }
  }
  
  //secondary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * check variant of positioning ship after rotation. checkable variaint is
   * defined by specified code of area and code of location inside this area.
   * 
   * @param ship
   * @param area
   * @param location
   * 
   * @return coordinates of ship head if checkable variant is good for
   *         positioning ship after rotating. otherwise returns "null"
   */
  private GameXY checkVariant(Battleship selected, int area, int location){
    //[checking the ability of positioning ship at coordinates, specified by
    //code of area and code of location]
    GameXY     futureXY  = new GameXY(-1, -1);
    GameXY     currentXY = selected.getGameXY();
    int        cellsNum  = selected.getCellsNum();
    Directions direction;
    if(selected.getDirection() == Directions._vertical){
      direction = Directions._horizontal;
      futureXY.x = currentXY.x + area - (selected.getCellsNum() - 1);
      futureXY.y = currentXY.y + location;
    }else{
      direction = Directions._vertical;
      futureXY.x = currentXY.x + location;
      futureXY.y = currentXY.y + area - (selected.getCellsNum() - 1);
    }
    if(Positions._free == isAbleToLocate(futureXY, cellsNum, direction, selected)){
      log.write("[v][Battlefield]good cell");
      return futureXY;
    }else{
      log.write("[v][Battlefield]bad cell");
      return null;
    }
  }
  
  /**
   * look over neighbor cells and current cell, specified by #futureXY
   * for following condition: are they occupied by ohter ships?
   * 
   * @param selected  selected battleship
   * @param futureXY  game coordinates of cell, which is ckecking to be
   *                  occupied by selected ship after rotating
   * 
   * @return  "Positions._outOfBattlefield" if specified cell is out of battleifeld,
   *          "Positions._locked" if specified cell is locked by other ships,
   *          othrewise returns "Positions._free"
   */
  private Positions isOccupiedByOtherShip(Battleship selected, GameXY futureXY){
    //handling situation, when #futureXY
    //specifies cell, which is out of battlefield
    if((futureXY.x < 0)||
       (futureXY.y < 0)||
       (futureXY.x >= CELLS_IN_SIDE)||
       (futureXY.y >= CELLS_IN_SIDE)){
      //checkable cell is out of battlefield
      return Positions._outOfBattlefield;
    }
    //checking area 3x3 around cell, specified by parameter #futureXY
    for(int y=(futureXY.y-1); y<=(futureXY.y+1); y++){
      for(int x=(futureXY.x-1); x<=(futureXY.x+1); x++){
        log.write("[v][Battlefield]q:is occupied (%d, %d)?", x, y);
        if((x < 0)||
           (y < 0)||
           (x >= CELLS_IN_SIDE)||
           (y >= CELLS_IN_SIDE)){
          //such #x and #y defines cell, which is out of battlefield
          log.write("[v][Battlefield]a:out of battlefield");
          continue;
        }
        Battleship other = cells[x][y].partOf();
        if(null == other){
          //free cell
          log.write("[v][Battlefield]a:free cell");
          continue;
        }
        if(other == selected){
          //selected ship is out of checking
          log.write("[v][Battlefield]a:part of deletable");
          continue;
        }
        if(other != null){
          //occupied cell (occupied by other ship).
          log.write("[v][Battlefield]a:occupied cell");
          return Positions._locked;
        }
        //log.write("[v][Battlefield]a:no"); //compiler damns: dead code
      }
    }
    //all required cells are checked and they are not occupied. 
    return Positions._free;
  }
  
  /**
   * actualize current quantity of ships with every cells number 
   */
  private void actualizeShipsCounters(){
    int cellsNum;
    int idx;
    //[resetting ships counters]
    resetUnlocatedShipsQuantity();
    //[actualizing ships counters]
    for(int i=0; i<currentShipsQuantity; i++){
      cellsNum = ships[i].getCellsNum();
      idx      = cellsNum - 1;
      unlocatedShips[idx] = unlocatedShips[idx] - 1;
    }
  }
  
  /**
   * reset unlocated ships quantity
   */
  private void resetUnlocatedShipsQuantity(){
    unlocatedShips[0] = 4;
    unlocatedShips[1] = 3;
    unlocatedShips[2] = 2;
    unlocatedShips[3] = 1;
  }
  
  /**
   * highlight cells as locked/unlocked for ships locating
   * 
   * @param x      game x-coordinate
   * @param y      game y-coordinate
   * @param state  parameter, which defines how to highlight an area
   */
  private void highlightCell(int x, int y, Cell.States state){
    if((x < 0)||
       (y < 0)||
       (x >= CELLS_IN_SIDE)||
       (y >= CELLS_IN_SIDE)){
       //[doing nothing]
       return;
    }
    if(Cell.States.EMPTY == state){
      GameXY cell = new GameXY();
      cell.x = x;
      cell.y = y;
      if(Positions._free == isOccupiedByOtherShip(null, cell)){
        cells[x][y].setState(Cell.States.EMPTY);
      }
    }else{
      cells[x][y].setState(state);
    }
  }
  
  /**
   * highlight locked area around of a ship
   * 
   * @param ship  fuck! ship means ship...
   */
  private void highlightLockedArea(Battleship ship){
    highlightArea(ship, Cell.States.LOCKED);
  }
  
  /**
   * highlight unlocked area around of a ship, which was deleted
   * 
   * @param ship  fuck! ship means ship...
   */
  private void highlightUnlockedArea(Battleship ship){
    highlightArea(ship, Cell.States.EMPTY);
  }
  
  /**
   * highlight area around of a ship as locked or unlocked
   * 
   * @param ship   fuck! ship means ship...
   * @param state  parameter, which defines how to highlight an area   
   */
  private void highlightArea(Battleship ship, Cell.States state){
    int x = ship.getGameXY().x;
    int y = ship.getGameXY().y;
    int side1x   = 0;
    int side1y   = 0;
    int side2x   = 0;
    int side2y   = 0;
    int centralX = 0;
    int centralY = 0;
    for(int i= -1; i<(ship.getCellsNum()+1); i++){
      if(ship.getDirection() == Directions._vertical){
        centralX = x;
        side1x   = x - 1;
        side2x   = x + 1;
        centralY = y + i;
        side1y   = centralY;
        side2y   = centralY;
      }
      else{
        centralY = y;
        side1y   = y - 1;
        side2y   = y + 1;
        centralX = x + i;
        side1x   = centralX;
        side2x   = centralX;
      }
      //[central line]
      //if((-1 == i)||(i == ship.getCellsNum())){
        highlightCell(centralX, centralY, state);
      //}
      //[side 1]
      highlightCell(side1x, side1y, state);
      //[side 2]
      highlightCell(side2x, side2y, state);
    }
  }
  
  /**
   * find in #ships[] array index of specified ship
   * 
   * @param searchable  searchable ship
   * 
   * @return array index of specified ship
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private int findShipIdx(Battleship searchable)
  throws NeedFixCode{
    //[checking arguments validation]
    if(null == searchable){
      throw new NeedFixCode("[x][Battlefield]#findShipIdx():argument is null");
    }
    //[searching for specified ship]
    for(int i=0; i<currentShipsQuantity; i++){
      if(ships[i] == searchable){
        //[specified ship is found]
        return i;
      }
    }
    throw new NeedFixCode("[x][Battlefield]#findShipIdx():specified ship is not found");
  }
  
  /**
   * highlight shot results on battlefield cells an on ship cells
   * 
   * @param aim             game coordinates of a cell, which is an aim f shooting
   * @param thisShotResult  result of current shot
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private void highlightShotResults(GameXY aim, ShotResults thisShotResult)
  throws NeedFixCode{
    if(ShotResults._miss == thisShotResult){
      highlightCell(aim.x, aim.y, Cell.States.MISSED);
    }
    else{
      highlightHit(aim);
      predictEmptyCells(thisShotResult, aim);
    }
    //[repaint!]
    repaint();
  }
  
  /**
   * highlight destruction of a ship, specified by game coordinates of shot
   * 
   * @param aim  game coordinates of shot
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  /*private void highlightDestruction(GameXY aim)
  throws NeedFixCode{
    //[defining index of just-destroyed ship in #ships[] array]
    int idx = findShipIdx(cells[aim.x][aim.y].partOf());
    //[defining visiblity]
    boolean isVisible = ships[idx].isVisible();
    if(isVisible){
      //[highlighting ship cell, which was hit]
      ships[idx].highlightHit(aim);
    }
    else{
    }
    //[highlighting neighbor cells as missed]
    highlightArea(ships[idx], Cell.States.MISSED);
  }*/
  
  /**
   * highlight hit to a ship, specified by game coordinates of shot
   * 
   * @param aim             game coordinates of shot
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private void highlightHit(GameXY aim)
  throws NeedFixCode{
    //[defining index of just-destroyed ship in #ships[] array]
    int idx = findShipIdx(cells[aim.x][aim.y].partOf());
    //[highlighting ship cell, which was hit]
    ships[idx].highlightHit(aim);
    //[defining visiblity]
    boolean isVisible = ships[idx].isVisible();
    if(!isVisible){
      locateHitMarker(aim);
    }
  }
  
  /**
   * predict empty cells by highlighting it as missed
   * 
   * @param thisShotResult  result of this shot
   * @param hit             game coordinates of cell, which was hit
   */
  private void predictEmptyCells(ShotResults thisShotResult, GameXY aim){
    //[watching variants for prediction]
    if(ShotResults._destruction == thisShotResult){
      //[case, when this shot have destroyed the ship]
      Battleship deadShip = cells[aim.x][aim.y].partOf();
      highlightArea(deadShip, Cell.States.MISSED);
    }
    else{
      //[unable predict anythinging in this case]
    }
    return;
  }
  
  /**
   * locate hit marker at specified game coordinates
   * 
   * @param aim  game coordinates, where must be located hit marker
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private void locateHitMarker(GameXY aim)
  throws NeedFixCode{
    int x = cells[aim.x][aim.y].getX() - THICKNESS;
    int y = cells[aim.x][aim.y].getY() - THICKNESS;
    HitMarker marker = new HitMarker(x, y);
    add(marker);
    setComponentZOrder(marker, Z_INDEX_SHIP);
  }
}
