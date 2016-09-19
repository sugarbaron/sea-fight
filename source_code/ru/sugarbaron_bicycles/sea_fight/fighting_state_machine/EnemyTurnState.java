package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[standard libraries]
import javax.swing.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.time.*;
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.GameXY;
import ru.sugarbaron_bicycles.sea_fight.EnemyTurnDrawer;
import ru.sugarbaron_bicycles.sea_fight.TextIndicatorController;
import ru.sugarbaron_bicycles.sea_fight.enemy_intellect.EnemyIntellect;
import ru.sugarbaron_bicycles.sea_fight.enemy_intellect.EnemyIntellectTools;



/**
 * this class describes state #enemyTurn of #FightingStateMachine.
 * this class was created for overriding #activity() and set
 * certain actions, which will execute, when state machine will jump
 * in this state
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class EnemyTurnState
extends GraphState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** tool for drawing results of enemy turn in context of swing thread */
  private EnemyTurnDrawer          drawer         = null;
  /** tool for displaying message for player in context of swing thread */
  private TextIndicatorController  displayer      = null;
  /** enemy intellect tools */
  private EnemyIntellectTools enemyIntellectTools = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create example of #EnemyTurnState.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  EnemyTurnState()
  throws NeedFixCode{
    super();
    drawer              = EnemyTurnDrawer.getInstance();
    displayer           = TextIndicatorController.getInstance();
    enemyIntellectTools = EnemyIntellectTools.getInstance();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * indicate message "it is enemy turn"
   */
  @Override
  public void enter(){
    displayer.setMessage("it is enemy turn");
    SwingUtilities.invokeLater(displayer);
  }
  
  /**
   * make enemy turn actions.
   * open fire to player field 
   */
  @Override
  public void activity()
  throws NeedFixCode{
    LogUnit log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][EnemyTurnState]#activity():invoked");
    //[waiting a little bit]
    TimeSubsystem.sleep(1000);
    //[considering empty cells, which have appear because of 
    // player ship destroying]
    enemyIntellectTools.considerEmptyCells();
    //[choosing tactics for making the shot]
    enemyIntellectTools.chooseTactics();
    //[making a shoot]
    GameXY shootedCell = EnemyIntellect.getCurrentState().shoot();
    //[drawing results of shot]
    drawer.setShootedCell(shootedCell);
    SwingUtilities.invokeLater(drawer);
    log.write("[v][EnemyTurnState]#activity():complete");
    return;
  }
}
