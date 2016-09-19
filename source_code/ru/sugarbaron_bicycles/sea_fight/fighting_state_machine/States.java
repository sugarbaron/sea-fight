package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[my bicycles]
import ru.sugarbaron_bicycles.library.state_machine.*;



/**
 * this class incapsulates all states of fighting process
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class States{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** state: player turn */
  static public GraphState playerTurn         = null;
  /** state: enemy turn */
  static public GraphState enemyTurn          = null;
  /** state: hit analysis */
  static public GraphState hitAnalysis        = null;
  /** state: fighting complete */
  static public GraphState fightingComplete   = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor
   */
  private States(){}
}
