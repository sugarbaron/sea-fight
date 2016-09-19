package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.event.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class processing events, provided by button "rotate" 
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class RotateBtnHandler
implements ActionListener{
  //data_section______________________________________________________________
  ////////////////////////////////////////////////////////////////////////////
  /** battlefield, which contains rotateable ships */
  private Battlefield battlefield;
  /** system log */
  LogUnit log;

  //constructors_section______________________________________________________
  ////////////////////////////////////////////////////////////////////////////
  /**
   * create instance of handler for button "rotate"
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  RotateBtnHandler(Battlefield battlefield)
      throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
    this.battlefield = battlefield;
  }

  //primary_section___________________________________________________________
  ////////////////////////////////////////////////////////////////////////////
  /**
   * handle event of pressing button "rotate"
   */
  public void actionPerformed(ActionEvent event){
    try{
      log.write("[v][RotateBtnHandler]player pressed #rotate button");
      //trying to rotate selected ship
      battlefield.tryToRotateShip();
    }
    catch(ExecutionAborted exception){
      log.write("[v][RotateBtnHandler]#ExecutionAborted exception");
      log.write("[v][RotateBtnHandler]unable to rotate ship");
      log.write("[v][RotateBtnHandler]%s", exception.getMessage());
    }
    catch(NeedFixCode exception){
      log.write("[x][RotateBtnHandler]#NeedFixCode exception");
      log.write("[x][RotateBtnHandler]%s", exception.getMessage());
      Dbg.out("[x][RotateBtnHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
  }
}
 