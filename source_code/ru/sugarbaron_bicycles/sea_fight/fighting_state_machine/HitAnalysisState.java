package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[standard libraries]
import javax.swing.*;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.BattlePane;
import ru.sugarbaron_bicycles.sea_fight.Battlefield;
import ru.sugarbaron_bicycles.sea_fight.EnemyShipsShower;
import ru.sugarbaron_bicycles.sea_fight.TextIndicatorController;
import ru.sugarbaron_bicycles.sea_fight.ThisApplicationWindow;


/**
 * this class describes state #hitAnalysis of #Fighting state machine.
 * this class was created for overriding #activity() and set
 * certain actions, which will execute, when state machine will jump
 * in this state
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class HitAnalysisState
extends GraphState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to player field */
  private Battlefield              playerField      = null;
  /** reference to enemy field */
  private Battlefield              enemyField       = null;
  /** tool for displaying message for player in context of swing thread */
  private TextIndicatorController  displayer        = null;
  /** tool for showing enemy ships location in context of swing thread */
  private EnemyShipsShower         enemyShipsShower = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create example of #HitAnalysState.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  HitAnalysisState()
  throws NeedFixCode{
    //[01.constructing superclass]
    super();
    //[02.initializing fields]
    //[creating locals]
    ThisApplicationWindow baseWindow = null;
    BattlePane stage2ContentPane     = null;
    //[initializing locals]
    baseWindow        = ThisApplicationWindow.getInstance();
    stage2ContentPane = (BattlePane)baseWindow.getContentPane();
    //[initializing fields]
    playerField      = stage2ContentPane.getPlayerField();
    enemyField       = stage2ContentPane.getEnemyField();
    displayer        = TextIndicatorController.getInstance();
    enemyShipsShower = new EnemyShipsShower();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * define, is it time to end the battle? 
   */
  @Override
  public void activity()
  throws NeedFixCode{
    LogUnit log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][HitAnalysState]#activity() invoked");
    //[creating locals]
    boolean     isAllShipsDestroyed = false;
    GraphSignal continueSignal      = null;
    String      gameResultMessage   = null;
    boolean     isPlayerLose        = false;
    //[defining past state]
    GraphState pastState = Fighting.getPastState();
    //[initializing locals]
    if(States.enemyTurn == pastState){
      isAllShipsDestroyed = playerField.isAllShipsDestroyed();
      continueSignal      = Signals.giveTurnToEnemy;
      gameResultMessage   = "you lose";
      isPlayerLose        = true;
    }
    else{
      isAllShipsDestroyed = enemyField.isAllShipsDestroyed();
      continueSignal      = Signals.giveTurnToPlayer;
      gameResultMessage   = "you win";
      isPlayerLose        = false;
    }
    //[checking for end of battle]
    if(isAllShipsDestroyed){
      log.write("[v][HitAnalysState]#activity():end of battle");
      displayer.setMessage(gameResultMessage);
      SwingUtilities.invokeLater(displayer);
      if(isPlayerLose){
        SwingUtilities.invokeLater(enemyShipsShower);
      }
      Fighting.receiveSignal(Signals.endFighting);
    }
    else{
      Fighting.receiveSignal(continueSignal);
    }
    return;
  }
}
