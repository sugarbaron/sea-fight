package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.event.*;
//[by bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * handler for event of pressing button "remove"
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class RemoveBtnHandler
implements ActionListener{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** battlefield, which contains ships for removing */
  private Battlefield battlefield;
  /** log */
  private LogUnit log;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create handler for event of pressing button "remove"
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  RemoveBtnHandler(Battlefield battlefield)
  throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
    this.battlefield = battlefield;
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * handle event of pressing button "remove"
   */
  public void actionPerformed(ActionEvent event){
    try{
      log.write("[v][RemoveBtnHandler]player pressed #remove button");
      Battleship removableShip;
      removableShip = battlefield.findSelectedShip();
      if(removableShip != null){
        battlefield.deleteShip(removableShip);
        //[repaint!]
        battlefield.repaint();
        //[actualizing ships-counters labels]
        ShipsLocatingPane contentPane;
        contentPane = (ShipsLocatingPane)battlefield.getParent();
        contentPane.actualizeShipsCountersLabels();
      }
    }
    catch(NeedFixCode exception){
      log.write("[x][RemoveBtnHandler]#NeedFixCode exception");
      log.write("[x][RemoveBtnHandler]exception message: %s", exception.getMessage());
      Dbg.out("[x][RemoveBtnHandler]#NeedFixCode exception");
      Dbg.out(exception.getMessage());
    }
  }
}
