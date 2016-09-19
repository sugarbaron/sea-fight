package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;



/**
 * this class defines the logic of enemy actions in battle.
 * base principle is a state machine.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class EnemyIntellect{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** state machine of enemy intellect */
  static private StateMachine intellect           = null;
  
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private EnemyIntellect(){}
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create state machine of fighting process
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  static public void buildGraph()
  throws NeedFixCode{
    //[01.creating state machine]
    intellect = StateMachine.create();
    //[02.initing states]
    States.searchingForShip  = new SearchingForShipState();
    States.definingDirection = new DefiningDirectionState();
    States.destroyingShip    = new DestroyingShipState();
    //[03.initing signals]
    Signals.shipFound        = new GraphSignal();
    Signals.directionDefined = new GraphSignal();
    Signals.shipDestroyed    = new GraphSignal();
    //[04.setting jumps]
    //[jumps for state #searchingForShip]
    StateMachine.setJump(States.searchingForShip, States.definingDirection, Signals.shipFound);
    //[jumps for state #definingDirection]
    StateMachine.setJump(States.definingDirection, States.destroyingShip,   Signals.directionDefined);
    StateMachine.setJump(States.definingDirection, States.searchingForShip, Signals.shipDestroyed);
    //[jumps for state #destroyingShip]
    StateMachine.setJump(States.destroyingShip, States.searchingForShip, Signals.shipDestroyed);
    //[05.setting initial state]
    intellect.setStart(States.searchingForShip);
  }
  
  /**
   * get current state
   * 
   * @return current state
   */
  static public EnemyIntellectState getCurrentState(){
    return (EnemyIntellectState)intellect.getCurrentState();
  }
  
  /**
   * receive signal. causing state machine to make step
   * 
   * @param signal  signal, delivered to state machine
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  static public void receiveSignal(GraphSignal signal)
  throws NeedFixCode{
    intellect.makeStep(signal);
  }
}