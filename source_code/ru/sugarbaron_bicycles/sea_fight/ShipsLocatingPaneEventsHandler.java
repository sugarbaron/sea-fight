package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import java.awt.event.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;





/**
 * this class processes events, provided by ships locating pane
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class ShipsLocatingPaneEventsHandler
extends MouseAdapter{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit log;
  /** point, where user have pressed mouse button for dragging the window */
  private Point draggingPoint;
  /** reference to this object */
  static private ShipsLocatingPaneEventsHandler instance = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * construct ShipsLocatingPaneEventsHandler.
   * realisation of singleton pattern
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private ShipsLocatingPaneEventsHandler()
  throws NeedFixCode{
    super();
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][ShipsLocatingPaneEventsHandler]constructing ShipsLocatingPaneEventsHandler");
    draggingPoint = new Point(0, 0);
    log.write("[v][ShipsLocatingPaneEventsHandler]instance constructed");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * method #getInstance() realises singleton pattern.
   * 
   * @return reference to ShipsMouseEventsHandler object
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static ShipsLocatingPaneEventsHandler getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new ShipsLocatingPaneEventsHandler();
    }
    return instance;
  }
  
  /**
   * on mouse pressing event all battleships must be deselected
   */
  @Override
  public void mousePressed(MouseEvent event){
    log.write("[v][ShipsLocatingPaneEventsHandler]player pressed mouse button");
    //[deselecting all battleships]
    ShipsLocatingPane contentPane;
    contentPane = (ShipsLocatingPane)event.getSource();
    contentPane.getPlayerField().deselectAll();
    //[repaint!]
    contentPane.repaint();
    //[initializing dragging point]
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
      log.write("[x][ShipsLocatingPaneEventsHandler]#NeedFixCode exception");
      log.write("[x][ShipsLocatingPaneEventsHandler]exception message:");
      log.write("[x][ShipsLocatingPaneEventsHandler]%s", exception.getMessage());
      Dbg.out(exception.getMessage());
    }
  }
}