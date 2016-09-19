package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.event.*;

//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * handler for event of pressing button "battle!"
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class FightBtnHandler
implements ActionListener{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit     log         = null;
  /** reference to player field */
  private Battlefield playerField = null;
  
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create handler for event of pressing button "battle!"
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  FightBtnHandler(Battlefield battlefield)
  throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
    playerField = battlefield;
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * handle event of pressing button "battle!"
   */
  public void actionPerformed(ActionEvent event){
    log.write("[v][FightBtnHandler]player pressed #battle! button");
    try{
      //[01.checking for quantity of located ships]
      if(!playerField.isAllShipsLocated()){
        log.write("[v][FightBtnHandler]there are unlocated ships");
        log.write("[v][FightBtnHandler]unable to start battle");
        throw new ExecutionAborted("[v][FightBtnHandler]not all ships are located");
      }
      //[02.creating locals]
      ThisApplicationWindow baseWindow;
      baseWindow = ThisApplicationWindow.getInstance();
      PlayerFieldEventsHandler eventsHandler;
      eventsHandler = PlayerFieldEventsHandler.getInstance();
      //[03.switching off highlighting of locked cells at player field]
      playerField.switchOffLockedHighlighting();
      playerField.deselectAll();
      //[04.removing 1st stage event listeners]
      playerField.removeMouseListener(eventsHandler);
      playerField.removeShipsEventsHandlers();
      //[05.creating battle pane]
      BattlePane stage2ContentPane;
      DataForStage2 stage1Data = DataForStage2.getInstance();
      stage2ContentPane = BattlePane.create(stage1Data);
      //[06.switching content pane]
      baseWindow.setContentPane(stage2ContentPane);
      //[07.giving permittion for main thread to build graph]
      General.receivePermittion();
      //[08.making new content visible]
      baseWindow.setVisible(true);
    }
    catch(ExecutionAborted exception){
      log.write("[v][FightBtnHandler]#ExecutionAborted exception");
      log.write("[v][FightBtnHandler]exception message: %s", exception.getMessage());
    }
    catch(NeedFixCode exception){
      log.write("[x][FightBtnHandler]#NeedFixCode exception");
      log.write("[x][FightBtnHandler]exception message: %s", exception.getMessage());
    }
  }
}
 