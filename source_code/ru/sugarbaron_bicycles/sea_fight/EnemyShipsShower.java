package ru.sugarbaron_bicycles.sea_fight;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;



public final class EnemyShipsShower
implements Runnable{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to enemy field */
  private Battlefield enemyField = null;
  
  //consrtucrors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create instance of #EnemyShipsShower
   * 
   * @throws NeedFixCode  in case, when was detected wrong work of a programm
   *                      because of errors in code
   */
  public EnemyShipsShower()
  throws NeedFixCode{
    //[creating locals]
    ThisApplicationWindow baseWindow        = null;
    BattlePane            stage2ContentPane = null;
    //[initializing locals]
    baseWindow = ThisApplicationWindow.getInstance();
    stage2ContentPane = (BattlePane)baseWindow.getContentPane();
    //[initializing fields]
    enemyField = stage2ContentPane.getEnemyField();
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * showing enemy ships location
   */
  @Override
  public void run(){
    enemyField.showShips();
    return;
  }
}