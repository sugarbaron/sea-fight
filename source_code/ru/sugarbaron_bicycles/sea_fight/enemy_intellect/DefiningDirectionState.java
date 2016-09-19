package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.random.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.GameXY;
import ru.sugarbaron_bicycles.sea_fight.ShotResults;
import ru.sugarbaron_bicycles.sea_fight.Directions;



/**
 * this class defines enemy logic of defining player ship direction
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class DefiningDirectionState
extends GraphState
implements EnemyIntellectState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** unshooted cells collection */
  private UnshootedCellsCollection unshootedCells      = null;
  /** enemy intellect tools */
  private EnemyIntellectTools      enemyIntellectTools = null;
  /** first damaged cell of a ship, which direction we are rtying to define */
  private GameXY                   determinedShipCell  = null;
  /** neighbor cells, relative first damaged cell */
  private GameXY[]                 possibleShipCells   = null;
  /** current quantity of unshoted cells among #possibleShipCells[] */
  private int                      quantityOfPossible  = -1;
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
  DefiningDirectionState()
  throws NeedFixCode{
    super();
    log                  = LogSubsystem.getLog("system_log.txt");
    unshootedCells       = UnshootedCellsCollection.getInstance();
    enemyIntellectTools  = EnemyIntellectTools.getInstance();
    determinedShipCell   = new GameXY();
    possibleShipCells    = new GameXY[4];
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * on entering in this state, need to determine cells, which will be
   * shooted for defining player ship direction
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  @Override
  public void enter()
  throws NeedFixCode{
    log.write("[v][DefiningDirectionState]#enter()");
    //[01.creating locals]
    GameXY  checkable = new GameXY();
    GameXY  lastAim   = enemyIntellectTools.getLastShotCoordinates();
    //[02.clearing values]
    possibleShipCells[0] = new GameXY();
    possibleShipCells[1] = new GameXY();
    possibleShipCells[2] = new GameXY();
    possibleShipCells[3] = new GameXY();
    quantityOfPossible   = 0;
    log.write("[v][DefiningDirectionState]constructor:changed quantityOfPossible #%d", quantityOfPossible);
    //[03.obtaining first damaged cell]
    determinedShipCell.x = lastAim.x;
    determinedShipCell.y = lastAim.y;
    //[04.initing neghbor cells]
    //[checking neighbor cell number 1]
    checkable.x = determinedShipCell.x;
    checkable.y = determinedShipCell.y - 1;
    checkAndRemember(checkable);
    //[checking neighbor cell number 2]
    checkable.x = determinedShipCell.x + 1;
    checkable.y = determinedShipCell.y;
    checkAndRemember(checkable);
    //[checking neighbor cell number 3]
    checkable.x = determinedShipCell.x;
    checkable.y = determinedShipCell.y + 1;
    checkAndRemember(checkable);
    //[checking neighbor cell number 4]
    checkable.x = determinedShipCell.x - 1;
    checkable.y = determinedShipCell.y;
    checkAndRemember(checkable);
    //dbg_section______________________________________________________________
    log.write("[v][DefiningDirectionState]#enter():quantityOfPossible #%d", quantityOfPossible);
    log.write("[v][DefiningDirectionState]#enter():possibleShipCells[] is");
    for(int i=0; i<quantityOfPossible; i++){
      log.write("[v][DefiningDirectionState]#enter():possible[%d] #%d #%d", i, possibleShipCells[i].x, possibleShipCells[i].y);
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
    log.write("[v][DefiningDirectionState]#shoot()");
    //dbg_section______________________________________________________________
    log.write("[v][DefiningDirectionState]#enter():quantityOfPossible #%d", quantityOfPossible);
    log.write("[v][DefiningDirectionState]#enter():possibleShipCells[] is");
    for(int i=0; i<quantityOfPossible; i++){
      log.write("[v][DefiningDirectionState]#enter():possible[%d] #%d #%d", i, possibleShipCells[i].x, possibleShipCells[i].y);
    }
    //endof_dbg_section________________________________________________________
    //[randomly choosing among #possibleShipCells[]]
    int random = Random.roll(quantityOfPossible);
    GameXY shot = possibleShipCells[random];
    //[removing already shooted cells from #possibleShipCells[]]
    removeLastShooted(random);
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
    log.write("[v][DefiningDirectionState]#analyzeLastShot()");
    ShotResults lastShotResult = enemyIntellectTools.getLastShotResult();
    if(ShotResults._destruction == lastShotResult){
      log.write("[v][DefiningDirectionState]#analyzeLastShot():going to #SearchingForShipState");
      //[ship is destroyed: searching for next ship]
      EnemyIntellect.receiveSignal(Signals.shipDestroyed);
    }
    else
    if(ShotResults._hit == lastShotResult){
      log.write("[v][DefiningDirectionState]#analyzeLastShot():going to #DestroyingShipState");
      //[watching coordinates of last shot]
      GameXY lastAim = enemyIntellectTools.getLastShotCoordinates();
      //[defining shootable ship direction]
      if(lastAim.x == determinedShipCell.x){
        log.write("[v][DefiningDirectionState]#analyzeLastShot():setting direction: _vertical");
        enemyIntellectTools.setShootableDirection(Directions._vertical);
      }
      else{
        log.write("[v][DefiningDirectionState]#analyzeLastShot():setting direction: _horizontal");
        enemyIntellectTools.setShootableDirection(Directions._horizontal);
      }
      log.write("[v][DefiningDirectionState]#analyzeLastShot():setting known");
      log.write("[v][DefiningDirectionState]#analyzeLastShot():determ #%d #%d", determinedShipCell.x, determinedShipCell.y);
      log.write("[v][DefiningDirectionState]#analyzeLastShot():lastAim #%d #%d", lastAim.x, lastAim.y);
      //[setting known cells]
      enemyIntellectTools.setKnownCells(determinedShipCell, lastAim);
      //[direction is defined: going to destroying state]
      EnemyIntellect.receiveSignal(Signals.directionDefined);
    }
    return;
  }
  
  //secondary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * remove shooted cell from #possibleShipCells[]
   * 
   * @param idx  index of shooted cell in #possibleShipCells[]
   */
  private void removeLastShooted(int idx){
    --quantityOfPossible;
    log.write("[v][DefiningDirectionState]removeLastShooted():changed (--) quantityOfPossible #%d", quantityOfPossible);
    possibleShipCells[idx] = possibleShipCells[quantityOfPossible]; 
  }
  
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
    }
    return;
  }
}
