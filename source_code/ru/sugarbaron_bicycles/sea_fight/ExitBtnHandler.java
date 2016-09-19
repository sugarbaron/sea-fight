package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.event.*;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * handler for event of pressing button "exit"
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class ExitBtnHandler
implements ActionListener{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit log;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create handler for "exit" button
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  ExitBtnHandler()
  throws NeedFixCode{
    super();
    log = LogSubsystem.getLog("system_log.txt");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * handle event of pressing button "exit"
   */
  public void actionPerformed(ActionEvent event){
    try{
      log.write("[v][ExitBtnHandler]player pressed #exit button");
      General.terminate();
      ThisApplicationWindow baseWindow;
      baseWindow = ThisApplicationWindow.getInstance();
      baseWindow.close();
    }
    catch(NeedFixCode exception){
      log.write("[x][ExitBtnHandler]#NeedFixCode exception");
      log.write("[x][ExitBtnHandler]exception message:");
      log.write("[x][ExitBtnHandler]%s", exception.getMessage());
      Dbg.out("[x][ExitBtnHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
  }
}
