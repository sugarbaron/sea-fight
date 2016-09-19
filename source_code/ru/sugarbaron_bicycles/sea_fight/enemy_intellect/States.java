package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.state_machine.*;



/**
 * this class incapsulates all states of enemy intellect,
 * which is represented by state machine.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class States{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** state: searching for player ship */
  static public GraphState searchingForShip  = null;
  /** state: defining direction of player ship */
  static public GraphState definingDirection = null;
  /** state: destroying player ship */
  static public GraphState destroyingShip    = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private States(){}
}
