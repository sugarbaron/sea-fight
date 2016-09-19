package ru.sugarbaron_bicycles.sea_fight;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;



/**
 * tool for displaying message for player in context of swing thread
 * 
 * @autor sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class TextIndicatorController
implements Runnable{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to single instance of #TextIndicatorController */
  static private TextIndicatorController instance = null;
  /** message for displaying */
  private String     message           = null;
  /** reference to battle pane */
  private BattlePane stage2ContentPane = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create single instance of #TextIndicatorController
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  private TextIndicatorController()
  throws NeedFixCode{
    //[creating locals]
    ThisApplicationWindow baseWindow = null;
    baseWindow = ThisApplicationWindow.getInstance();
    //[initializing #stage2ContentPane]
    stage2ContentPane = (BattlePane)baseWindow.getContentPane();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * get instance of #TextIndicatorController
   * 
   * @return instance of #TextIndicatorController
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  static public TextIndicatorController getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new TextIndicatorController();
    }
    return instance;
  }
  
  /**
   * set message for displaying on text indicator
   * 
   * @param message  message for displaying on text indicator
   */
  public void setMessage(String message){
    this.message = message;
  }
  
  /**
   * display message specified by value of #message field on text indicator
   */
  @Override
  public void run(){
    stage2ContentPane.indicate(message);
    return;
  }
}
