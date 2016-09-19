package ru.sugarbaron_bicycles.sea_fight;



/**
 * game coordinates. battlefield cells numbers (from 0, not from 1). 
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public final class GameXY{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** game x-coordinate */
  public int x;
  /** game y-coordinate */
  public int y;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create game coordinates object by specified screen coordinates
   * 
   * @param screenX  screen x-coordinate
   * @param screenY  screen y-coordinate
   */
  GameXY(int screenX, int screenY){
    x = screenX/Battlefield.SIZE_UNIT;
    y = screenY/Battlefield.SIZE_UNIT;
  }
  
  /**
   * create game coordinates object. default game coordinates are (0, 0)
   */
  public GameXY(){
    this(0, 0);
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * compare game coordinate pairs
   * 
   * @return "true" if specified coordinate pairs are equal
   *         otherwise returns "false"
   */
  static boolean compare(GameXY cell1, GameXY cell2){
    if((cell1.x == cell2.x)&&(cell1.y == cell2.y)){
      return true;
    }else{
      return false;
    }
  }
}