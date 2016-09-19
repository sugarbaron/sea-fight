package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.random.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * class of content pane for base application window.
 * this pane is for stage of battle.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class BattlePane
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit log = null;
  /** enemy field instance */
  private Battlefield enemyField;
  /** player field instance */
  private Battlefield playerField;
  /** text indicator */
  private TextIndicator indicator;
  /** refernce to single example of this class */
  static private BattlePane instance = null;
  /** x-coordinate of base ponit */
  static final int BASE_POSITION_X = ThisApplicationWindow.MARGIN;
  /** y-coordinate of base ponit */
  static final int BASE_POSITION_Y = ThisApplicationWindow.MARGIN;
  /** x-coordinate of base ponit */
  static final int ENEMY_FIELD_POSITION_X = ThisApplicationWindow.MARGIN +
                                            ThisApplicationWindow.BATTLEFIELD_SIZE +
                                            ThisApplicationWindow.DISTANCE_BETWEEN_FIELDS;
  /** default z-order value for components */
  static final int Z_INDEX_DEFAULT = 1;
  /** version uid */
  private static final long serialVersionUID = 1L;
  /** FIXME:dbg shit */
  /*
  private GameXY[] dbgHeads = new GameXY[10];
  private Directions[] dbgDirections = new Directions[10];
  private int[] dbgCellsNums = new int[10];
  private Battlefield dbgField = new Battlefield();
  private int dbgIdx = 0;
  */
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create instance of battle pane (using #singleton pattern)
   * 
   * @param stage1Data  data, provided by stage1 for stage2
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private BattlePane(DataForStage2 stage1Data)
  throws NeedFixCode{
    super(null);
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][BattlePane]constructor begins work");
    //[01.checking arguments validation]
    if(null == stage1Data){
      log.write("[x][BattlePane]argument is null");
      throw new NeedFixCode("[x][BattlePane]argument is null");
    }
    //[02.setting battle pane parameters]
    setBackground(Color.white);
    setOpaque(true);
    //[03.accepting stage1 data]
    //[adding player field]
    playerField = stage1Data.getPlayerField();
    add(playerField);
    //[adding #exit button]
    JButton btnExit = stage1Data.getBtnExit();
    add(btnExit);
    //[04.creating enemy field]
    enemyField = new Battlefield(ENEMY_FIELD_POSITION_X, BASE_POSITION_Y);
    EnemyFieldEventsHandler playerShotsHandler;
    playerShotsHandler = EnemyFieldEventsHandler.getInstance();
    enemyField.addMouseListener(playerShotsHandler);
    add(enemyField);
    //[creating and locating enemy ships]
    generateEnemyShips();
    //FIXME:dbg section. kill__________________________________________________
    /*
    JWindow dbgWindow = new JWindow();
    dbgWindow.setLocation(1200, 125);
    dbgWindow.setSize(350, 350);
    dbgCreateShips();
    dbgWindow.getContentPane().add(dbgField);
    dbgWindow.setVisible(true);
    */
    //FIXME:endof dbg section. kill____________________________________________
    //[06.attaching event handler]
    BattlePaneEventsHandler eventsHandler;
    eventsHandler = BattlePaneEventsHandler.getInstance();
    addMouseListener(eventsHandler);
    addMouseMotionListener(eventsHandler);
    //[07.creating text indicator]
    indicator = new TextIndicator();
    add(indicator);
    //[08.configuring z-order for components]
    setComponentZOrder(playerField, Z_INDEX_DEFAULT);
    setComponentZOrder(enemyField,  Z_INDEX_DEFAULT);
    setComponentZOrder(btnExit,     Z_INDEX_DEFAULT);
    //setComponentZOrder(indicator,   Z_INDEX_DEFAULT);
    log.write("[v][BattlePane]instance constructed");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * we are using swing framework. overriding #paintComponent() method
   * for managing view of this component. 
   * 
   * @param graphics  graphic context for self-drawing of component
   */
  @Override
  protected void paintComponent(Graphics graphics){
    super.paintComponent(graphics);
    int width  = ThisApplicationWindow.WIDTH - 1;
    int height = ThisApplicationWindow.HEIGHT - 1;
    graphics.setColor(Color.black);
    graphics.drawRect(0, 0, width, height);
  }
  
  /**
   * create battle pane.
   * 
   * @param stage1Data  data, provided by stage1 for stage2
   * 
   * @return reference to single instance of this class
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static BattlePane create(DataForStage2 stage1Data)
  throws NeedFixCode{
    if(null == instance){
      instance = new BattlePane(stage1Data);
    }
    return instance;
  }
  
  /**
   * get reference to instance of #this class. (using #singleton pattern)
   * 
   * @return reference to single instance of this class
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static BattlePane getInstance()
  throws NeedFixCode{
    if(null == instance){
      throw new NeedFixCode("[x][BattlePane]#getInstance():#BattlePane is not created. invoke #createBattlePane() before using #getInstance()");
    }
    return instance;
  }
  
  /**
   * get reference to player field
   * 
   * @return reference to player field
   */
  public Battlefield getPlayerField(){
    return playerField;
  }
  
  /**
   * get reference to player field
   * 
   * @return reference to player field
   */
  public Battlefield getEnemyField(){
    return enemyField;
  }
  
  /**
   * create all enemy ships. generate game coordinates for all enemy ships.
   * locate all enemy ships at enemy field.
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private void generateEnemyShips()
  throws NeedFixCode{
    //[for every type of ships]
    for(int cellsNum=4; cellsNum>0; cellsNum--){
      generateAllShipsTypeOf(cellsNum);
    }
  }
  
  /**
   * create all enemy ships of specified type.
   * generate game coordinates for these ships.
   * locate all these ships at enemy field.
   * 
   * @param cellsNum  type of ships (ship size in cells)
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private void generateAllShipsTypeOf(int cellsNum)
  throws NeedFixCode{
    //[01.checking argument validation]
    if((cellsNum < 1)||(cellsNum > 4)){
      log.write("[x][BattlePane]#NeedFixCode exception");
      log.write("[x][BattlePane]#generateAllShipsTypeOf():wrong argument #%d", cellsNum);
      throw new NeedFixCode("[x][BattlePane]#generateAllShipsTypeOf():wrong argument");
    }
    //[creating locals]
    int quantityOfUnlocated = enemyField.getUnlocatedShipsQuantity(cellsNum);
    Directions direction    = null;
    GameXY shipHeadXY       = null;
    Positions isOkToLocate  = null;
    //[locating all ships of such type (of such cells size)]
    for( ; quantityOfUnlocated > 0 ; ){
      //[02.generating direction]
      direction = randomDirection();
      //[03.generating game coordinates]
      shipHeadXY = randomCell();
      //[04.trying to locate ship]
      isOkToLocate = enemyField.isAbleToLocate(shipHeadXY, cellsNum, direction, null);
      if(Positions._free == isOkToLocate){
        //[it is ok to locate]
        //[locating ship]
        try{
          enemyField.locateShip(shipHeadXY, cellsNum, direction, false);
          //FIXME:kill dbg
          /*
          dbgHeads[dbgIdx] = new GameXY();
          dbgHeads[dbgIdx].x = shipHeadXY.x;//<=dbg
          dbgHeads[dbgIdx].y = shipHeadXY.y;//<=dbg
          dbgDirections[dbgIdx] = direction;//<=dbg
          dbgCellsNums[dbgIdx] = cellsNum;//<=dbg
          ++dbgIdx;
          */
        }
        catch(ExecutionAborted exception){
          //[it means #shipHeadXY is out of battlefield. #shipHeadXY must
          // be generated correctly]
          throw new NeedFixCode("[x][BattlePane]#generateAllShipsTypeOf():#shipHeadXY is wrong");
        }
        //[here #quantityOfUnlocated must decrease]
        quantityOfUnlocated = enemyField.getUnlocatedShipsQuantity(cellsNum);
      }
    }
  }
  
  /**
   * set message for text indicator
   * 
   * @param message  message to set
   */
  void indicate(String message){
    indicator.setText(message);
  }
  
  //secondary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * define random direction
   * 
   * @return defined direction
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private Directions randomDirection()
  throws NeedFixCode{
    log.write("[v][BattlePane]#randomDirection():genering random direction");
    int directionVariants = Directions.values().length;
    int choosed = Random.roll(directionVariants);
    log.write("[v][BattlePane]#randomDirection():directionVariants #%d", directionVariants);
    log.write("[v][BattlePane]#randomDirection():choosed           #%d", choosed);
    switch(choosed){
    case 0:
     return Directions._horizontal;
     //break; //compiler damns because of unreachable code
    case 1:
      return Directions._vertical;
      //break; //compiler damns because of unreachable code
    default:
      throw new NeedFixCode("[x][BattlePane]#randomDirection():unhandled direction variant");
      //break; //compiler damns because of unreachable code
    }
  }
    
  /**
   * define cell
   */
  private GameXY randomCell()
  throws NeedFixCode{
    int xVariants = Battlefield.CELLS_IN_SIDE;
    int yVariants = Battlefield.CELLS_IN_SIDE;
    int xRoll     = Random.roll(xVariants);
    int yRoll     = Random.roll(yVariants);
    //[creating result object]
    GameXY result = new GameXY(0, 0);
    //[initing result object]
    result.x      = xRoll;
    result.y      = yRoll;
    //[profit!]
    return result;
  }
  
  /**
   * FIXME:kill it 
   */
  /*
  private void dbgCreateShips(){
    for(int i=0; i<10; i++){
      try{
        dbgField.locateShip(dbgHeads[i], dbgCellsNums[i], dbgDirections[i], true);
      }
      catch(ExecutionAborted x){
        log.write("[x][BattlePane]wtf?");
      }
      catch(NeedFixCode x){
        log.write("[x][BattlePane]#NeedFixCode exception");
        log.write("[x][BattlePane]exception message #%s", x.getMessage());
      }
    }
  }
  */
}
