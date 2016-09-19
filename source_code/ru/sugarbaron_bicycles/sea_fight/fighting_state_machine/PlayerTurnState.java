package ru.sugarbaron_bicycles.sea_fight.fighting_state_machine;

//[standard libraries]
import javax.swing.SwingUtilities;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.state_machine.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.TextIndicatorController;



/**
 * this class is created for possibility of displaying message
 * "it is your turn" on text indicator.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class PlayerTurnState
extends GraphState{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** tool for displaying message for player in context of swing thread */
  private TextIndicatorController  displayer      = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create example of #EnemyTurnState.
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  PlayerTurnState()
  throws NeedFixCode{
    super();
    displayer = TextIndicatorController.getInstance();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * indicate message "it is your turn"
   */
  @Override
  public void enter(){
    displayer.setMessage("it is your turn");
    SwingUtilities.invokeLater(displayer);
  }
}
