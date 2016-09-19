package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.BattlePane;
import ru.sugarbaron_bicycles.sea_fight.Battleship;
import ru.sugarbaron_bicycles.sea_fight.Directions;
import ru.sugarbaron_bicycles.sea_fight.ShotResults;
import ru.sugarbaron_bicycles.sea_fight.GameXY;
import ru.sugarbaron_bicycles.sea_fight.Battlefield;
import ru.sugarbaron_bicycles.sea_fight.ThisApplicationWindow;



/**
 * this class gives different tools for enemy intellect logic
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class EnemyIntellectTools{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to single example of this class */
  static private EnemyIntellectTools instance = null; 
  /** last shot result */
  private ShotResults lastShotResult                   = null;
  /** last shot coordinates */
  private GameXY                   lastShotCoordinates = null;
  /** reference to player field */
  private Battlefield              playerField         = null;
  /** reference to unshooted cells collection */
  private UnshootedCellsCollection unshootedCells      = null;
  /** direction of shootable player ship */
  private Directions               shootableDirection  = null;
  /** known cells of shootable player ship */
  private GameXY[]                 knownCells          = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create instance of #EnemyIntellectTools
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  private EnemyIntellectTools()
  throws NeedFixCode{
    //[creating locals]
    ThisApplicationWindow baseWindow = null;
    BattlePane stage2ContentPane     = null;
    //[initializing locals]
    baseWindow        = ThisApplicationWindow.getInstance();
    stage2ContentPane = (BattlePane)baseWindow.getContentPane();
    //[initing fields]
    lastShotResult      = null;
    lastShotCoordinates = new GameXY();
    playerField         = stage2ContentPane.getPlayerField();
    unshootedCells      = UnshootedCellsCollection.getInstance();
    knownCells          = new GameXY[2];
    knownCells[0]       = new GameXY();
    knownCells[1]       = new GameXY();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * get reference to single example of this class
   * 
   * @return reference to single example of this class
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  static public EnemyIntellectTools getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new EnemyIntellectTools();
    }
    return instance;
  }
  
  /**
   * register last shot result
   * 
   * @param result  last shot result
   */
  public void registerLastShotResult(ShotResults result){
    lastShotResult = result;
  }
  
  /**
   * register last shot coordinates
   * 
   * @param shot  game coordinates of shot
   */
  void registerLastShotCoordinates(GameXY shot){
    lastShotCoordinates.x = shot.x;
    lastShotCoordinates.y = shot.y;
  }
  
  /**
   * consider empty cells by removing it from #unshootedCells collection.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  public void considerEmptyCells()
  throws NeedFixCode{
    //[01.checking for ability of considering empty cells]
    if(lastShotResult != ShotResults._destruction){
      //[nothing to consider]
      return;
    }
    //[02.obtaining dead ship]
    Battleship deadShip = playerField.getShip(lastShotCoordinates);
    //[reading dead ship parameters]
    GameXY     head      = deadShip.getGameXY();
    Directions direction = deadShip.getDirection();
    int        cellsNum  = deadShip.getCellsNum();
    //[03.defining empty cells]
    //[creating locals]
    GameXY central = new GameXY();
    GameXY side1   = new GameXY();
    GameXY side2   = new GameXY();
    //[removing empty cells from unshooted cells collection]
    for(int i=-1; i<(cellsNum+1); i++){
      if(Directions._vertical == direction){
        central.x = head.x;
        side1.x   = central.x - 1;
        side2.x   = central.x + 1;
        central.y = head.y + i;
        side1.y   = central.y;
        side2.y   = central.y;
      }
      else{
        central.y = head.y;
        side1.y   = central.y - 1;
        side2.y   = central.y + 1;
        central.x = head.x + i;
        side1.x   = central.x;
        side2.x   = central.x;
      }
      //[celtral line]
      unshootedCells.removeElement(central);
      //[side 1]
      unshootedCells.removeElement(side1);
      //[side 2]
      unshootedCells.removeElement(side2);
    }
    return;
  }
  
  /**
   * choose tactics for making the shot.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  public void chooseTactics()
  throws NeedFixCode{
    EnemyIntellect.getCurrentState().analyzeLastShot();
    return;
  }
  
  /**
   * get last shot coordinates
   * 
   * @return last shot game coordinates
   */
  GameXY getLastShotCoordinates(){
    return lastShotCoordinates;
  }
  
  /**
   * get last shot result
   * 
   * @return last shot result
   */
  ShotResults getLastShotResult(){
    return lastShotResult;
  }
  
  /**
   * get direction of shootable player ship
   * 
   * @return direction of shootable player ship
   */
  Directions getShootableDirection(){
    return shootableDirection;
  }
  
  /**
   * set direction of shootable player ship
   * 
   * @param direction  direction of shootable player ship
   */
  void setShootableDirection(Directions direction){
    shootableDirection = direction;
    return;
  }
  
  /**
   * get known cells of shootable player ship
   * 
   * @return array of known cells of shootable player ship
   */
  GameXY[] getKnownCells(){
    return knownCells;
  }
  
  /**
   * get known cells of shootable player ship
   * 
   * @param cell1  one edge cell of shootable player ship
   * @param cell2  another edge cell of shootable player ship
   */
  void setKnownCells(GameXY cell1, GameXY cell2){
    knownCells[0].x = cell1.x;
    knownCells[0].y = cell1.y;
    knownCells[1].x = cell2.x;
    knownCells[1].y = cell2.y;
    return;
  }
}
