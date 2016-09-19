package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import javax.swing.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.library.exceptions.*;




/**
 * this class is for keeping data, provided by stage1 (ships locating)
 * for stage2 (battle)
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
final class DataForStage2{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** log */
  private LogUnit log;
  /** reference to player field */
  private Battlefield playerField;
  /** reference to #exit button */
  private JButton btnExit;
  /** refernce to single example of this class */
  static private DataForStage2 instance = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * default constructor. (using #singleton pattern)
   * 
   * @throws NeedFixCode  if parameters are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private DataForStage2()
  throws NeedFixCode{
    log = LogSubsystem.getLog("system_log.txt");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create DataForStage2 object.
   * 
   * @return reference to single instance of this class
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static DataForStage2 createDataForStage2()
  throws NeedFixCode{
    if(null == instance){
      instance = new DataForStage2();
    }
    return instance;
  }
  
  /**
   * get reference to instance of #this class. (using #singleton pattern)
   * 
   * @return reference to single instance of this class
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static DataForStage2 getInstance()
  throws NeedFixCode{
    if(null == instance){
      throw new NeedFixCode("[x][DataForStage2]#DataForStage2 is not created. invoke #createDataForStage2() before using #getInstance()");
    }
    return instance;
  }
  
  /**
   * save reference to player field
   * 
   * @param needToSave reference to player field for saving
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void savePlayerField(Battlefield needToSave)
  throws NeedFixCode{
    //[01.checking arguments validation]
    if(null == needToSave){
      log.write("[x][DataForStage2]#savePlayerField(): argument is null");
      throw new NeedFixCode("[x][DataForStage2]#savePlayerField(): argument is null");
    }
    //[02.saving data]
    playerField = needToSave;
  }
  
  /**
   * save reference to #exit button
   * 
   * @param needToSave reference to #exit button for saving
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void saveBtnExit(JButton needToSave)
  throws NeedFixCode{
    //[01.checking arguments validation]
    if(null == needToSave){
      log.write("[x][DataForStage2]#saveBtnExit(): argument is null");
      throw new NeedFixCode("[x][DataForStage2]#saveBtnExit(): argument is null");
    }
    //[02.saving data]
    btnExit = needToSave;
  }
  
  /**
   * get reference to player field
   * 
   * @return reference to player field
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  Battlefield getPlayerField()
  throws NeedFixCode{
    //[01.checking correctness of using]
    if(null == playerField){
      log.write("[x][DataForStage2]#playerField is not inited");
      throw new NeedFixCode("[x][DataForStage2]#playerField is not inited");
    }
    //[02.executing primary task]
    return playerField;
  }
   
  /**
   * get reference to #exit button
   * 
   * @return reference to #exit button
   * 
   * @throws NeedFixCode  if arguments are wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  JButton getBtnExit()
  throws NeedFixCode{
    //[01.checking correctness of using]
    if(null == btnExit){
      log.write("[x][DataForStage2]#btnExit is not inited");
      throw new NeedFixCode("[x][DataForStage2]#btnExit is not inited");
    }
    //[02.executing primary task]
    return btnExit;
  }
}
 
 