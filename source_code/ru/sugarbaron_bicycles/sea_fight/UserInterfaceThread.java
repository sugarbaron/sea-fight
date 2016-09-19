package ru.sugarbaron_bicycles.sea_fight;

import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * this class describes a thread, which must contain user interface objects
 * (such as windows, user events handlers, and other)
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class UserInterfaceThread
implements Runnable{
  //link to system log
  private LogUnit log;
  
  /**
   * entry point of user interface thread. 
   */
  public void run(){
  //part 1: initializing log link
  try{log = LogSubsystem.getLog("system_log.txt");}
  catch(NeedFixCode x){
    Dbg.out("[x]NeedFixCode excepton in [UserInterfaceThread]");
    Dbg.out("[x]problems with getting log");
    Dbg.out("[x]possible, specified log file name is wrong");
    Dbg.out("[x]exception.getMessage() is:");
    Dbg.out(x.getMessage());
    Dbg.out("[x]exception.toString() is:");
    Dbg.out(x.toString());
    Dbg.out("[x]stack trace:");
    x.printStackTrace(System.out);
    return;
  }
  //part 2: now we have log, and we can do other actions
  try{
    log.write("[v][UserInterfaceThread]#run called");
    ThisApplicationWindow baseWindow;
    ShipsLocatingPane stage1ContentPane;
    //01.creating base window of this application
    baseWindow = ThisApplicationWindow.getInstance();
    log.write("[v][UserInterfaceThread]created #baseWindow");
    //02.creating user interface for 1st stage (ships locating)
    stage1ContentPane = new ShipsLocatingPane(null);
    log.write("[v][UserInterfaceThread]created #stage1ContentPane");
    //03.linking 1st stage user interface to base window
    baseWindow.setContentPane(stage1ContentPane);
    log.write("[v][UserInterfaceThread]#stage1ContentPane linked to #baseWindow");
    //04.starting stage 1 (ships locating)
    baseWindow.setVisible(true);
    log.write("[v][UserInterfaceThread]#baseWindow displayed");
  }
  catch(NeedFixCode x){
    log.write("[x][UserInterfaceThread]NeedFixCode exception (x)");
    log.write("[x][UserInterfaceThread]x.getMessage() is:");
    log.write("[x][UserInterfaceThread]%s", x.getMessage());
    log.write("[x][UserInterfaceThread]x.toString() is:");
    log.write("[x][UserInterfaceThread]%s", x.toString());
    log.write("[x][UserInterfaceThread]thread terminated");
    return;
  }//endof: try
  log.write("[v][UserInterfaceThread]#run complete");
  }//endof: run()
}