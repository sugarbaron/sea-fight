package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import java.awt.event.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;





/**
 * this class processes events, provided by battle pane
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class BattlePaneEventsHandler
extends MouseAdapter{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit log;
  /** point, where user have pressed mouse button for dragging the window */
  private Point draggingPoint;
  /** reference to instance of battle pane events handler */
  static private BattlePaneEventsHandler instance;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create battle pane events handler. (using a #singleton pattern)
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private BattlePaneEventsHandler()
  throws NeedFixCode{
    super();
    log = LogSubsystem.getLog("system_log.txt");
    draggingPoint = new Point(0, 0);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * get instance of battle pane events handler. (using a #singleton pattern)
   * 
   * @return instance of battle pane events handler
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static BattlePaneEventsHandler getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new BattlePaneEventsHandler();
    }
    return instance;
  }
  
  /**
   * on mouse pressed event must be initialised dragging point
   */
  @Override
  public void mousePressed(MouseEvent event){
    log.write("[v][BattlePaneEventsHandler]player pressed mouse button");
    //[initialising dragging point]
    draggingPoint.x = event.getX();
    draggingPoint.y = event.getY();
  }
  
  /**
   * on mouse dragged event base window must be replaced
   */
  @Override
  public void mouseDragged(MouseEvent event){
    try{
      //[creating locals]
      ThisApplicationWindow baseWindow;
      baseWindow        = ThisApplicationWindow.getInstance();
      Point mouse       = event.getPoint();
      Point oldLocation = baseWindow.getLocation();
      Point newLocation = new Point();
      //[calculating coordinates of new location]
      int deltaX = mouse.x - draggingPoint.x;
      int deltaY = mouse.y - draggingPoint.y;
      newLocation.x = oldLocation.x + deltaX;
      newLocation.y = oldLocation.y + deltaY;
      //[replacing base window]
      baseWindow.setLocation(newLocation);
    }
    catch(NeedFixCode exception){
      log.write("[x][BattlePaneEventsHandler]#NeedFixCode exception");
      log.write("[x][BattlePaneEventsHandler]exception message:");
      log.write("[x][BattlePaneEventsHandler]%s", exception.getMessage());
      Dbg.out(exception.getMessage());
    }
  }
}
