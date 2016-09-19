package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
import ru.sugarbaron_bicycles.library.synchronization.*;



/**
 * organization of fighting process is based on state machine (automat, graph).
 * this class defines state machine of fighting process.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class Fighting{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** state machine of fighting process */
  static private StateMachine fighting     = null;
  /** current signal for state machine */
  static private GraphSignal currentSignal = null;
  /** fighting semaphore */
  static private Semaphore   semaphore     = new Semaphore(0);
  /** termination flag */
  static private boolean     isTerminate   = false;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private Fighting(){}
  
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
    fighting = StateMachine.create();
    //[02.initing states]
    States.playerTurn        = new PlayerTurnState();
    States.enemyTurn         = new EnemyTurnState();
    States.hitAnalysis       = new HitAnalysisState();
    States.fightingComplete  = new GraphState();
    //[03.initing signals]
    Signals.miss             = new GraphSignal();
    Signals.playerHit        = new GraphSignal();
    Signals.enemyHit         = new GraphSignal();
    Signals.giveTurnToPlayer = new GraphSignal();
    Signals.giveTurnToEnemy  = new GraphSignal();
    Signals.endFighting      = new GraphSignal();
    //[04.setting jumps]
    //[jumps for state #playerTurn]
    StateMachine.setJump(States.playerTurn, States.hitAnalysis, Signals.playerHit);
    StateMachine.setJump(States.playerTurn, States.enemyTurn, Signals.miss);
    //[jumps for state #enemyTurn]
    StateMachine.setJump(States.enemyTurn, States.hitAnalysis,  Signals.enemyHit);
    StateMachine.setJump(States.enemyTurn, States.playerTurn, Signals.miss);
    //[jumps for state #hitAnalysis]
    StateMachine.setJump(States.hitAnalysis, States.playerTurn,       Signals.giveTurnToPlayer);
    StateMachine.setJump(States.hitAnalysis, States.enemyTurn,        Signals.giveTurnToEnemy);
    StateMachine.setJump(States.hitAnalysis, States.fightingComplete, Signals.endFighting);
    //[05.setting initial state]
    fighting.setStart(States.playerTurn);
  }
  
  /**
   * get current state
   * 
   * @return current state
   */
  static public GraphState getCurrentState(){
    return fighting.getCurrentState();
  }
  
  /**
   * get current state
   * 
   * @return current state
   */
  static GraphState getPastState(){
    return fighting.getPastState();
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
    currentSignal = signal;
    semaphore.giveSignal();
  }
  
  /**
   * fighting process activity
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static public void doActivity()
  throws NeedFixCode{
    for( ; true ; ){
      //[waiting for semaphore]
      semaphore.waitSignal();
      //[checking terminate flag]
      if(isTerminate){
        break;
      }
      //[making state machine step]
      fighting.makeStep(currentSignal);
    }
    return;
  }
  
  /**
   * terminate fighting process
   */
  static public void terminate(){
    isTerminate = true;
    semaphore.giveSignal();
    return;
  }
}
