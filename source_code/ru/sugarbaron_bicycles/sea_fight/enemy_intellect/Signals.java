package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.state_machine.*;



/**
 * this class incapsulates all signals of enemy intellect,
 * which is represented by state machine.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class Signals{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** signal: ship is found */
  static public GraphSignal shipFound = null;
  /** signal: direction is defined */
  static public GraphSignal directionDefined = null;
  /** signal: ship is destroyed */
  static public GraphSignal shipDestroyed = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private Signals(){}
}
