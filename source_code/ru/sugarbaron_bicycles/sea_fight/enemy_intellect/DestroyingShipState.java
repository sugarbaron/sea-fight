package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.random.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.GameXY;
import ru.sugarbaron_bicycles.sea_fight.Directions;
import ru.sugarbaron_bicycles.sea_fight.ShotResults;



/**
 * this class defines enemy logic of destroying player ship
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class DestroyingShipState
extends GraphState
implements EnemyIntellectState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** unshooted cells collection */
  private UnshootedCellsCollection unshootedCells = null;
  /** neighbor cells, relative known cells of shootable ship */
  private GameXY[]                 possibleShipCells = null;
  /** enemy intellect tools */
  private EnemyIntellectTools      enemyIntellectTools = null;
  /** current quantity of unshoted cells among #possibleShipCells[] */
  private int                      quantityOfPossible  = -1;
  /** direction of shootable player ship */
  private Directions               shootableDirection  = null;
  /** log */
  private LogUnit                  log                 = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  DestroyingShipState()
  throws NeedFixCode{
    super();
    log                  = LogSubsystem.getLog("system_log.txt");
    unshootedCells       = UnshootedCellsCollection.getInstance();
    enemyIntellectTools  = EnemyIntellectTools.getInstance();
    possibleShipCells    = new GameXY[2];
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * on entering in this state, need to determine cells, which will be
   * shooted for destroying player ship.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  @Override
  public void enter()
  throws NeedFixCode{
    log.write("[v][DestroyingShipState]#enter()");
    //[clearing values]
    possibleShipCells[0] = new GameXY();
    possibleShipCells[1] = new GameXY();
    quantityOfPossible   = 0;
    log.write("[v][DestroyingShipState]constructor:changed quantityOfPossible #%d", quantityOfPossible);
    //[initing #possibleShipCells[]]
    //[creating locals]
    GameXY   checkable  = new GameXY();
    GameXY[] knownCells = enemyIntellectTools.getKnownCells();
    shootableDirection  = enemyIntellectTools.getShootableDirection();
    if(Directions._vertical == shootableDirection){
      log.write("[v][DestroyingShipState]#enter():vertical");
      log.write("[v][DestroyingShipState]#enter():known[0] #%d #%d", knownCells[0].x, knownCells[0].y);
      log.write("[v][DestroyingShipState]#enter():known[1] #%d #%d", knownCells[1].x, knownCells[1].y);
      if(knownCells[0].y > knownCells[1].y){
        checkable.x = knownCells[0].x;
        checkable.y = knownCells[0].y + 1;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
        checkable.x = knownCells[1].x;
        checkable.y = knownCells[1].y - 1;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
      }
      else{
        checkable.x = knownCells[0].x;
        checkable.y = knownCells[0].y - 1;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
        checkable.x = knownCells[1].x;
        checkable.y = knownCells[1].y + 1;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
      }
    }
    else{
      log.write("[v][DestroyingShipState]#enter():horizontal");
      log.write("[v][DestroyingShipState]#enter():known[0] #%d #%d", knownCells[0].x, knownCells[0].y);
      log.write("[v][DestroyingShipState]#enter():known[1] #%d #%d", knownCells[1].x, knownCells[1].y);
      if(knownCells[0].x > knownCells[1].x){
        checkable.x = knownCells[0].x + 1;
        checkable.y = knownCells[0].y;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
        checkable.x = knownCells[1].x - 1;
        checkable.y = knownCells[1].y;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
      }
      else{
        checkable.x = knownCells[0].x - 1;
        checkable.y = knownCells[0].y;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
        checkable.x = knownCells[1].x + 1;
        checkable.y = knownCells[1].y;
        log.write("[v][DestroyingShipState]#enter():checking #%d #%d", checkable.x, checkable.y);
        checkAndRemember(checkable);
      }
    }
    //dbg_section______________________________________________________________
    log.write("[v][DestroyingShipState]#enter():quantityOfPossible #%d", quantityOfPossible);
    log.write("[v][DestroyingShipState]#enter():possibleShipCells[] is");
    for(int i=0; i<quantityOfPossible; i++){
      log.write("[v][DestroyingShipState]#enter():possible[%d] #%d #%d", i, possibleShipCells[i].x, possibleShipCells[i].y);
    }
    //endof_dbg_section________________________________________________________
    return;
  }
  
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
    log.write("[v][DestroyingShipState]#shoot()");
    //dbg_section______________________________________________________________
    log.write("[v][DestroyingShipState]#shoot():quantityOfPossible #%d", quantityOfPossible);
    log.write("[v][DestroyingShipState]#shoot():possibleShipCells[] is");
    for(int i=0; i<quantityOfPossible; i++){
      log.write("[v][DestroyingShipState]#enter():possible[%d] #%d #%d", i, possibleShipCells[i].x, possibleShipCells[i].y);
    }
    //endof_dbg_section________________________________________________________
    //[randomly choosing among #possibleShipCells[]]
    int random = Random.roll(quantityOfPossible);
    log.write("[v][DestroyingShipState]#shoot():#quantityOfPossible #%d", quantityOfPossible);
    log.write("[v][DestroyingShipState]#shoot():#random             #%d", random);
    GameXY shot = possibleShipCells[random];
    //[removing choosed cell from #unshootedCells]
    unshootedCells.removeElement(shot);
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
    log.write("[v][DestroyingShipState]#analyzeLastShot()");
    ShotResults shotResult = enemyIntellectTools.getLastShotResult();
    if(ShotResults._miss == shotResult){
      processMiss();
    }
    else
    if(ShotResults._hit == shotResult){
      processHit();
    }
    else{
      log.write("[v][DestroyingShipState]#analyzeLastShot():going to #SearchingForShipState");
      //[case of destroying shootable ship. going to search
      // for next player ship]
      EnemyIntellect.receiveSignal(Signals.shipDestroyed);
    }
    return;
  }
  
  //secondary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * check specified cell (is it already shooted) and remember by
   * saving it to #possibleShipCells[]
   * 
   * @param checkable  game coordinates of cell for checking 
   */
  private void checkAndRemember(GameXY checkable){
    //[creating locals]
    boolean isShooted = false;
    //[checking]
    isShooted = unshootedCells.isShooted(checkable);
    //[remembering, if cell is not already shooted]
    if(!isShooted){
      possibleShipCells[quantityOfPossible].x = checkable.x;
      possibleShipCells[quantityOfPossible].y = checkable.y;
      ++quantityOfPossible;
      log.write("[v][DestroyingShipState]#checkAndRemember():#%d #%d is not shooted", checkable.x, checkable.y);
      log.write("[v][DestroyingShipState]#checkAndRemember():possible[%d] #%d #%d saved", (quantityOfPossible-1), checkable.x, checkable.y);
      log.write("[v][DestroyingShipState]#checkAndRemember():changed (++) quantityOfPossible #%d", quantityOfPossible);
    }
    else{
      log.write("[v][DestroyingShipState]#checkAndRemember():#%d #%d is shooted", checkable.x, checkable.y);
      log.write("[v][DestroyingShipState]#checkAndRemember():quantity #%d", quantityOfPossible);
    }
    return;
  }
  
  /**
   * remove shooted cell from #possibleShipCells[]
   * 
   * @param idx  index of shooted cell in #possibleShipCells[]
   */
  private void removeBecauseShooted(int idx){
    --quantityOfPossible;
    log.write("[v][DestroyingShipState]removeLastShooted():changed (--) quantityOfPossible #%d", quantityOfPossible);
    possibleShipCells[idx] = possibleShipCells[quantityOfPossible]; 
  }
  
  /**
   * process #_miss case
   */
  private void processMiss(){
    //[removing #_miss variant. now we know, where to shoot]
    GameXY lastAim = enemyIntellectTools.getLastShotCoordinates();
    if((possibleShipCells[0].x == lastAim.x)&&
       (possibleShipCells[0].y == lastAim.y)){
      removeBecauseShooted(0);
    }
    else{
      removeBecauseShooted(1);
    }
    return;
  }
  
  /**
   * process #_hit case
   */
  private void processHit(){
    boolean isShooted = false;
    GameXY lastAim = enemyIntellectTools.getLastShotCoordinates();
    if((possibleShipCells[0].x == lastAim.x)&&
       (possibleShipCells[0].y == lastAim.y)){
      updatePossibleShipCells(0);
      isShooted = unshootedCells.isShooted(possibleShipCells[0]);
      if(isShooted){
        removeBecauseShooted(0);
      }
    }
    else{
      updatePossibleShipCells(1);
      isShooted = unshootedCells.isShooted(possibleShipCells[1]);
      if(isShooted){
        removeBecauseShooted(1);
      }
    }
    return;
  }
  
  /**
   * update possible ship cells
   * 
   * @param idx  index of updatable element
   */
  private void updatePossibleShipCells(int idx){
    //[creating locals]
    GameXY  checkable = new GameXY();
    boolean isShooted = false;
    //[determining next shot aim]
    if(Directions._vertical == shootableDirection){
      log.write("[v][DestroyingShipState]#updatePossibleShipCells():_vertical");
      //[supposing next shot aim]
      checkable.y = possibleShipCells[idx].y + 1;
      checkable.x = possibleShipCells[idx].x;
      //[checking supposition]
      isShooted = unshootedCells.isShooted(checkable);
      if(isShooted){
        //[supposed wrong]
        possibleShipCells[idx].y -= 1;
      }
      else{
        //[supposed right]
        possibleShipCells[idx].y += 1;
      }
    }
    else{
      log.write("[v][DestroyingShipState]#updatePossibleShipCells():_horizontal");
      //[supposing next shot aim]
      checkable.y = possibleShipCells[idx].y;
      checkable.x = possibleShipCells[idx].x + 1;
      //[checking supposition]
      isShooted = unshootedCells.isShooted(checkable);
      if(isShooted){
        //[supposed wrong]
        possibleShipCells[idx].x -= 1;
      }
      else{
        //[supposed right]
        possibleShipCells[idx].x += 1;
      }
    }
    return;
  }
}
