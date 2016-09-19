package ru.sugarbaron_bicycles.sea_fight;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.fighting_state_machine.Fighting;
import ru.sugarbaron_bicycles.sea_fight.fighting_state_machine.Signals;
import ru.sugarbaron_bicycles.sea_fight.enemy_intellect.EnemyIntellectTools;



/**
 * this class incapsulates drawing of enemy turn results.
 * drawing operations are allocated in separate class for
 * possiblity of running it in context of swing thread
 * 
 * @autor sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class EnemyTurnDrawer
implements Runnable{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to instance of #EnemyTurnDrawer */
  static private EnemyTurnDrawer instance = null;
  /** reference to log */
  private LogUnit             log                 = null;
  /** shootable cell, which will be redrawn */
  private GameXY              shootedCell         = null;
  /** reference to player field */
  private Battlefield         playerField         = null;
  /** enemy intellect tools */
  private EnemyIntellectTools enemyIntellectTools = null;
  
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** 
   * create #EnemyTurnDrawer
   * 
   * @param playerField  reference to player field
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  private EnemyTurnDrawer()
  throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
    //[02.initializing fields]
    //[creating locals]
    ThisApplicationWindow baseWindow = null;
    BattlePane stage2ContentPane     = null;
    baseWindow = ThisApplicationWindow.getInstance();
    stage2ContentPane = (BattlePane)baseWindow.getContentPane();
    //[initializing fields]
    playerField = stage2ContentPane.getPlayerField();
    enemyIntellectTools = EnemyIntellectTools.getInstance();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * draw enemy turn results
   */
  @Override
  public void run(){
    try{
      log.write("[v][EnemyTurnDrawer]#run():invoked");
      log.write("[v][EnemyTurnDrawer]#run():shooted cell: x #%d y #%d", shootedCell.x, shootedCell.y);
      //[defining and drawing shot results]
      ShotResults shotResult = playerField.handleShot(shootedCell);
      //[creating locals]
      GraphSignal continueSignal = null;
      //[defining signal for switching state machine of fighting]
      if(ShotResults._destruction == shotResult){
        log.write("[v][EnemyTurnDrawer]#run():shot result is #_destruction");
        continueSignal = Signals.enemyHit;
      }
      else
      if(ShotResults._hit == shotResult){
        log.write("[v][EnemyTurnDrawer]#run():shot result is #_hit");
        continueSignal = Signals.enemyHit;
      }
      else{
        log.write("[v][EnemyTurnDrawer]#run():shot result is #_miss");
        continueSignal = Signals.miss;
      }
      //[registering enemy shot result]
      enemyIntellectTools.registerLastShotResult(shotResult);
      //[switching state machine]
      Fighting.receiveSignal(continueSignal);
    }
    catch(NeedFixCode exception){
      log.write("[x][EnemyTurnDrawer]#run():#NeedFixCode exception");
      log.write("[x][EnemyTurnDrawer]#run():exception message: %s", exception.getMessage());
    }
  }
  
  /**
   * set shooted cell
   * 
   * @param cell  game coordinates of shooted cell
   */
  public void setShootedCell(GameXY cell){
    shootedCell = cell;
    return;
  }
  
  /**
   * get instance of #EnemyTurnDrawer
   * 
   * @return instance of #EnemyTurnDrawer
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  static public EnemyTurnDrawer getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new EnemyTurnDrawer();
    }
    return instance;
  }
}