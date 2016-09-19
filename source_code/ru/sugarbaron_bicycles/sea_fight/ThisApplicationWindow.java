package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
import java.awt.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.log.*;



/**
 * main window of this application
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class ThisApplicationWindow{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to log */
  private LogUnit log;
  /** base window for this application */
  private JFrame baseWindow;
  /** instance of this application window */
  private static ThisApplicationWindow instance = null;
  
  /** size of margin between components and edge of base window */
  static final int MARGIN = 20;
  /** size of battlefield */
  static final int BATTLEFIELD_SIZE = (Battlefield.CELLS_IN_SIDE + 1)*Battlefield.SIZE_UNIT + Battlefield.THICKNESS;
  /** size of space under the battlefield */
  static final int BUTTONS_AREA_SIZE_Y = (ShipsLocatingPane.BTN_SIZE_Y + ShipsLocatingPane.DISTANCE_UNIT)*3;
  /** distance between player field and enemy field */
  static final int DISTANCE_BETWEEN_FIELDS = 4*Battlefield.SIZE_UNIT + Battlefield.THICKNESS;
  /** width of base window */
  static final int WIDTH  = MARGIN +
                            BATTLEFIELD_SIZE +
                            DISTANCE_BETWEEN_FIELDS +
                            BATTLEFIELD_SIZE +
                            MARGIN;
  /** height of base window */
  static final int HEIGHT = MARGIN +
                            BATTLEFIELD_SIZE +
                            BUTTONS_AREA_SIZE_Y +
                            MARGIN;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * creates a window of this application.
   * realizes singleton patten.
   */
  private ThisApplicationWindow()
  throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][ThisApplicationWindow]constructor begins work");
    baseWindow = new JFrame("sea_fight");
    baseWindow.setSize(WIDTH, HEIGHT);
    baseWindow.setLocation(500, 100);
    baseWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    baseWindow.setUndecorated(true);
    log.write("[v][ThisApplicationWindow]instance constructed");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * method #getInstance() realizes singleton pattern
   * 
   * @return an instance of this application window
   */
  static public ThisApplicationWindow getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new ThisApplicationWindow();
    }
    return instance;
  }
  
  /**
   * close application
   */
  void close(){
    baseWindow.dispose();
  }
  
  /**
   * captain "obvious"
   */
  void repaint(){
    baseWindow.repaint();
  }
  
  /**
   * captain "obvious"
   */
  void setContentPane(Container pane){
    baseWindow.setContentPane(pane);
  }
  
  
  /**
   * captain "obvious"
   */
  void setVisible(boolean isVisible){
    baseWindow.setVisible(isVisible);
  }
  
  /**
   * captain "obvious"
   */
  void setLocation(Point newLocation){
    baseWindow.setLocation(newLocation);
  }
  
  /**
   * captain "obvious"
   */
  public Container getContentPane(){
    return baseWindow.getContentPane();
  }
  
  /**
   * captain "obvious"
   */
  Point getLocation(){
    return baseWindow.getLocation();
  }
} 