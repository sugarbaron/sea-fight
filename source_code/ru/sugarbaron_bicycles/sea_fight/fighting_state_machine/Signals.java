package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[my bicycles]
import ru.sugarbaron_bicycles.library.state_machine.*;



/**
 * this class incapsulates all signals of fighting process
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class Signals{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** signal: miss */
  static public GraphSignal miss             = null;
  /** signal: player hit */
  static public GraphSignal playerHit        = null;
  /** signal: enemy hit */
  static public GraphSignal enemyHit         = null;
  /** signal: give turn to player */
  static public GraphSignal giveTurnToPlayer = null;
  /** signal: give turn to enemy */
  static public GraphSignal giveTurnToEnemy  = null;
  /** signal: end fighting */
  static public GraphSignal endFighting      = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private Signals(){}
}