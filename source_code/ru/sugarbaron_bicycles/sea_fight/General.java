package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
//[my_bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.time.*;
import ru.sugarbaron_bicycles.library.synchronization.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.fighting_state_machine.Fighting;
import ru.sugarbaron_bicycles.sea_fight.enemy_intellect.EnemyIntellect;
import ru.sugarbaron_bicycles.sea_fight.enemy_intellect.EnemyIntellectTools;



/**
 * main class. contains application entry point.
 * 
 * @autor sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class General{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** permittion for building state machine of fighting process */
  static private Semaphore buildGraphPermittion = null;
  /** termination flag */
  static private boolean   isTerminate          = false;
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * entry point.
   */
  public static void main(String[] unusableShit)
  throws Exception{
    //[01.creating locals]
    ClockUnit           clock;
    LogUnit             log;
    UserInterfaceThread userInterface;
    //[02.initialization]
    clock = TimeSubsystem.getClock();
    LogSubsystem.createLog("system_log.txt", clock);
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][General]programm execution begins");
    log.write("[v][General]time subsystem inited");
    log.write("[v][General]log  subsystem inited");
    buildGraphPermittion = new Semaphore(0);
    //[03.creating gui]
    userInterface = new UserInterfaceThread();
    log.write("[v][General]user iterface instance created");
    //[04.starting gui thread]
    SwingUtilities.invokeLater(userInterface);
    log.write("[v][General]user iterface thread launched");
    //[05.building fighting state machine structure]
    //[waiting permittion]
    buildGraphPermittion.waitSignal();
    //[checking terminate flag]
    if(isTerminate){
      return;
    }
    Fighting.buildGraph();
    EnemyIntellect.buildGraph();
    EnemyIntellectTools.getInstance();
    //[06.starting fighting state machine activity]
    Fighting.doActivity();
    return;
  }
  
  /**
   * receive permittion for building state machine of fighting process
   */
  static public void receivePermittion(){
    buildGraphPermittion.giveSignal();
  }
  
  /**
   * terminate #Genenral thread
   */
  static void terminate(){
    isTerminate = true;
    receivePermittion();
    Fighting.terminate();
  }
}
