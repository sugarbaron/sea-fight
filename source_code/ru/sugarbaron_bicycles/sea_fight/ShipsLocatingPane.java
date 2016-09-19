package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import javax.swing.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * class of content pane for base application window.
 * this pane is for stage of locating player ships.
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
final class ShipsLocatingPane
extends JPanel{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** four deck ship counter label */
  JLabel fourDeckCounterLabel;
  /** three deck ship counter label */
  JLabel threeDeckCounterLabel;
  /** two deck ship counter label */
  JLabel twoDeckCounterLabel;
  /** one deck ship counter label */
  JLabel oneDeckCounterLabel;
  
  /** reference to system log */
  private LogUnit log;
  /** reference to player field */
  private Battlefield playerField;
  
  /** default z-order value for components */
  static final int Z_INDEX_DEFAULT = 1;
  /** z-order value for draggable components */
  static final int Z_INDEX_DRAGGABLE = 0;
  /** x-coordinate of base ponit */
  static final int BASE_POSITION_X = ThisApplicationWindow.MARGIN;
  /** y-coordinate of base ponit */
  static final int BASE_POSITION_Y = ThisApplicationWindow.MARGIN;
  /** x-coordinate of base ponit */
  static final int EXAMPLES_POSITION_X = ThisApplicationWindow.MARGIN +
                                         ThisApplicationWindow.BATTLEFIELD_SIZE +
                                         ThisApplicationWindow.DISTANCE_BETWEEN_FIELDS;
  /** for easyment and more beautiful code */
  static final int SIZE_UNIT     = Battlefield.SIZE_UNIT;
  static final int THICKNESS     = Battlefield.THICKNESS;
  static final int CELLS_IN_SIDE = Battlefield.CELLS_IN_SIDE;
  /** x-size of buttons */
  static final int BTN_SIZE_X     = 3*SIZE_UNIT + THICKNESS;
  /** y-size of buttons */
  static final int BTN_SIZE_Y     =   SIZE_UNIT + THICKNESS;
  /** unit of distance between components */
  static final int DISTANCE_UNIT  = Cell.SIDE_SIZE/4;
  /** version UID */
  static final long serialVersionUID = 1L;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * creates pane, which allows user to locate his ships
   * 
   * @param layoutManagerType
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  ShipsLocatingPane(LayoutManager layoutManagerType)
  throws NeedFixCode{
    super(layoutManagerType);
    //00.getting link to system log
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][ShipsLocatingPane]constructor begins work");
    //02.converting content pane related values to battlefield related
    setBackground(Color.white);
    setOpaque(true);
    //03.creating player field
    playerField = new Battlefield(BASE_POSITION_X, BASE_POSITION_Y);
    //attaching events listener
    PlayerFieldEventsHandler eventsHandler;
    eventsHandler = PlayerFieldEventsHandler.getInstance();
    playerField.addMouseListener(eventsHandler);
    add(playerField);
    //04.creating examples of ships
    //initing preconditions:
    //xB - x base
    int xB = EXAMPLES_POSITION_X;
    //yB - y base
    int yB = BASE_POSITION_Y;
    //xD - x delta
    int xD = SIZE_UNIT + DISTANCE_UNIT;
    //yD - y delta
    int yD = SIZE_UNIT;
    //constructing ships examples
    Ship fourDeckShipExample  = new ShipExample(xB,      yB+yD, 4);
    Ship threeDeckShipExample = new ShipExample(xB+  xD, yB+yD, 3);
    Ship twoDeckShipExample   = new ShipExample(xB+2*xD, yB+yD, 2);
    Ship oneDeckShipExample   = new ShipExample(xB+3*xD, yB+yD, 1);
    //adding ships examples to ships locating panel
    add(fourDeckShipExample);
    add(threeDeckShipExample);
    add(twoDeckShipExample);
    add(oneDeckShipExample);
    //05.creating counters of already-placed ships
    //initing preconditions
    LayoutManager layout = new FlowLayout();
    //creating substrates for ship-counter labels
    Cell fourDeckCounterCell;
    Cell threeDeckCounterCell;
    Cell twoDeckCounterCell;
    Cell oneDeckCounterCell;
    fourDeckCounterCell  = new Cell(xB,      yB);
    threeDeckCounterCell = new Cell(xB+  xD, yB);
    twoDeckCounterCell   = new Cell(xB+2*xD, yB);
    oneDeckCounterCell   = new Cell(xB+3*xD, yB);
    fourDeckCounterCell.setLayout(layout);
    threeDeckCounterCell.setLayout(layout);
    twoDeckCounterCell.setLayout(layout);
    oneDeckCounterCell.setLayout(layout);
    //creating ship-counter labels
    fourDeckCounterLabel  = new JLabel("x1");
    threeDeckCounterLabel = new JLabel("x2");
    twoDeckCounterLabel   = new JLabel("x3");
    oneDeckCounterLabel   = new JLabel("x4");
    //adding ship-counter labels to substrates
    fourDeckCounterCell.add(fourDeckCounterLabel);
    threeDeckCounterCell.add(threeDeckCounterLabel);
    twoDeckCounterCell.add(twoDeckCounterLabel);
    oneDeckCounterCell.add(oneDeckCounterLabel);
    //adding ship-counter substrates to ships locating panel
    add(fourDeckCounterCell);
    add(threeDeckCounterCell);
    add(twoDeckCounterCell);
    add(oneDeckCounterCell);
    //06.creating buttons
    //button #rotate
    int x = BASE_POSITION_X + (CELLS_IN_SIDE-2)*SIZE_UNIT;
    int y = BASE_POSITION_Y + Battlefield.SIDE_SIZE + DISTANCE_UNIT;
    Insets insets = new Insets(5, 5, 5, 5);
    JButton btnRotate = new JButton("rotate");
    btnRotate.setMargin(insets);
    btnRotate.setBackground(Color.white);
    btnRotate.setSize(BTN_SIZE_X, BTN_SIZE_Y);
    btnRotate.setLocation(x, y);
    btnRotate.addActionListener(new RotateBtnHandler(playerField));
    add(btnRotate);
    //button #remove
    x = btnRotate.getLocation().x - DISTANCE_UNIT - BTN_SIZE_X;
    y = btnRotate.getLocation().y;
    JButton btnRemove = new JButton("remove");
    btnRemove.setMargin(insets);
    btnRemove.setBackground(Color.white);
    btnRemove.setSize(BTN_SIZE_X, BTN_SIZE_Y);
    btnRemove.setLocation(x, y);
    btnRemove.addActionListener(new RemoveBtnHandler(playerField));
    add(btnRemove);
    //button #fight!
    x = btnRotate.getLocation().x;
    y = btnRotate.getLocation().y + 2*(BTN_SIZE_Y + DISTANCE_UNIT);
    JButton btnFight = new JButton("fight!");
    btnFight.setMargin(insets);
    btnFight.setBackground(Color.white);
    btnFight.setSize(BTN_SIZE_X, BTN_SIZE_Y);
    btnFight.setLocation(x, y);
    btnFight.addActionListener(new FightBtnHandler(playerField));
    add(btnFight);
    //button #exit
    x = btnRotate.getLocation().x + 15*SIZE_UNIT + 2*THICKNESS;
    y = btnFight.getLocation().y;
    JButton btnExit = new JButton("exit");
    btnExit.setMargin(insets);
    btnExit.setBackground(Color.white);
    btnExit.setSize(BTN_SIZE_X, BTN_SIZE_Y);
    btnExit.setLocation(x, y);
    btnExit.addActionListener(new ExitBtnHandler());
    add(btnExit);
    //07.configuring z-order for added components
    setComponentZOrder(playerField, Z_INDEX_DEFAULT);
    setComponentZOrder(fourDeckCounterCell,  Z_INDEX_DEFAULT);
    setComponentZOrder(threeDeckCounterCell, Z_INDEX_DEFAULT);
    setComponentZOrder(twoDeckCounterCell,   Z_INDEX_DEFAULT);
    setComponentZOrder(oneDeckCounterCell,   Z_INDEX_DEFAULT);
    setComponentZOrder(fourDeckShipExample,  Z_INDEX_DEFAULT);
    setComponentZOrder(threeDeckShipExample, Z_INDEX_DEFAULT);
    setComponentZOrder(twoDeckShipExample,   Z_INDEX_DEFAULT);
    setComponentZOrder(oneDeckShipExample,   Z_INDEX_DEFAULT);
    //08.attaching event handler
    ShipsLocatingPaneEventsHandler paneEventsHandler;
    paneEventsHandler = ShipsLocatingPaneEventsHandler.getInstance();
    addMouseListener(paneEventsHandler);
    addMouseMotionListener(paneEventsHandler);
    //09.saving stage1 data
    DataForStage2 stage1Data = DataForStage2.createDataForStage2();
    stage1Data.savePlayerField(playerField);
    stage1Data.saveBtnExit(btnExit);
    log.write("[v][ShipsLocatingPane]instance constructed");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * we are using swing framework. overriding #paintComponent() method
   * for managing view of this component.
   * 
   * @param graphics  graphic context for self-drawing of component
   */
  @Override
  protected void paintComponent(Graphics graphics){
    //dbg: log.write("[v][ShipsLocatingPane]#paintComponent called");
    super.paintComponent(graphics);
    int width  = ThisApplicationWindow.WIDTH  - 1;
    int height = ThisApplicationWindow.HEIGHT - 1;
    graphics.setColor(Color.black);
    graphics.drawRect(0, 0, width, height);
    //dbg: log.write("[v][ShipsLocatingPane]#paintComponent complete");
  }
  
  /**
   * actualize ships counter labels
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void actualizeShipsCountersLabels()
  throws NeedFixCode{
    int FOUR_DECK   = 4;
    int THREE_DECK  = 3;
    int TWO_DECK    = 2;
    int ONE_DECK    = 1;
    int fourDeckQuantity;
    int threeDeckQuantity;
    int twoDeckQuantity;
    int oneDeckQuantity;
    String fourDeckText;
    String threeDeckText;
    String twoDeckText;
    String oneDeckText;
    fourDeckQuantity  = playerField.getUnlocatedShipsQuantity(FOUR_DECK);
    threeDeckQuantity = playerField.getUnlocatedShipsQuantity(THREE_DECK);
    twoDeckQuantity   = playerField.getUnlocatedShipsQuantity(TWO_DECK);
    oneDeckQuantity   = playerField.getUnlocatedShipsQuantity(ONE_DECK);
    fourDeckText      = "x" + String.valueOf(fourDeckQuantity);
    threeDeckText     = "x" + String.valueOf(threeDeckQuantity);
    twoDeckText       = "x" + String.valueOf(twoDeckQuantity);
    oneDeckText       = "x" + String.valueOf(oneDeckQuantity);
    fourDeckCounterLabel.setText(fourDeckText);
    threeDeckCounterLabel.setText(threeDeckText);
    twoDeckCounterLabel.setText(twoDeckText);
    oneDeckCounterLabel.setText(oneDeckText);
  }
  
  /**
   * get refernce to player field
   * 
   * @return refernce to player field
   */
  Battlefield getPlayerField(){
    return playerField;
  }
}
