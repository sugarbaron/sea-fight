package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.random.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.GameXY;
import ru.sugarbaron_bicycles.sea_fight.ShotResults;



/**
 * this class defines enemy logic of searching for player ship
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class SearchingForShipState
extends GraphState
implements EnemyIntellectState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** unshooted cells collection */
  private UnshootedCellsCollection unshootedCells = null;
  /** enemy intellect tools */
  private EnemyIntellectTools enemyIntellectTools = null;
  /** log */
  private LogUnit             log                 = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  SearchingForShipState()
  throws NeedFixCode{
    super();
    log                 = LogSubsystem.getLog("system_log.txt");
    unshootedCells      = UnshootedCellsCollection.getInstance();
    enemyIntellectTools = EnemyIntellectTools.getInstance();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * shoot to player field.
   * this method defines the logic of choosing shootable cell for
   * this state
   * 
   * @return game coordinates of shootable cell
   */
  @Override
  public GameXY shoot()
  throws NeedFixCode{
    log.write("[v][SearchingForShipState]#shoot()");
    //[choosing shootable cell by random roll]
    int variantsQuantity = unshootedCells.getQuantity();
    int random = Random.roll(variantsQuantity);
    GameXY shot = unshootedCells.withdraw(random);
    enemyIntellectTools.registerLastShotCoordinates(shot);
    return shot;
  }
  
  /**
   * analyze last shot result
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  @Override
  public void analyzeLastShot()
  throws NeedFixCode{
    log.write("[v][SearchingForShipState]#analyzeLastShot()");
    ShotResults lastShotResult = enemyIntellectTools.getLastShotResult();
    if(ShotResults._hit == lastShotResult){
      log.write("[v][SearchingForShipState]#analyzeLastShot():going to #DefiningDirectionState");
      //[ship is found: need to change tactics]
      EnemyIntellect.receiveSignal(Signals.shipFound);
    }
    return;
  }
}
