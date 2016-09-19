package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.Battlefield;
import ru.sugarbaron_bicycles.sea_fight.GameXY;



/**
 * tools for making enemy shot. instance of this class contains collection
 * of unshooted cells (#GameXY) of player field and different tools for working
 * with it. 
 * 
 * @author sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
public final class UnshootedCellsCollection{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** collection size */
  private final int COLLECTION_SIZE = Battlefield.CELLS_IN_SIDE * Battlefield.CELLS_IN_SIDE;
  /** unshooted cells */
  private GameXY[] unshootedCells = null;
  /** current quantity of actual elements in #ushootedCells[] */
  private int currentElementsQuantity = 0;
  /** reference to single example of this class */
  static private UnshootedCellsCollection instance = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * create #UnshootedCellsCollection instance
   */
  private UnshootedCellsCollection(){
    //[creating collection]
    unshootedCells = new GameXY[COLLECTION_SIZE];
    //[initializing collection]
    int x = 0;
    int y = 0;
    for(int i=0; i<COLLECTION_SIZE; i++){
      unshootedCells[i] = new GameXY();
      unshootedCells[i].x = x;
      unshootedCells[i].y = y;
      ++x;
      if(!(x < Battlefield.CELLS_IN_SIDE)){
        x = 0;
        ++y;
      }
    }
    currentElementsQuantity = COLLECTION_SIZE;
    return;
  }
  
  //primary_section__________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * get instance. (singleton)
   * 
   * @return refernce to single instance of #UnshootedCellsCollection
   */
  static public UnshootedCellsCollection getInstance(){
    if(null == instance){
      instance = new UnshootedCellsCollection();
    }
    return instance;
  }
  
  /**
   * get specified element and remove it from unshooted cells collection
   * 
   * @param index  index of element in #unshootedCells[] array
   * 
   * @return reference to specified element
   * 
   * @throws NeedFixCode  if argument is wrong, or
   *                      if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  public GameXY withdraw(int index)
  throws NeedFixCode{
    //[01.checking argument validation]
    if(!(index < currentElementsQuantity)){
      throw new NeedFixCode("[x][UnshootedCellsCollection]#withdraw():wrong argument");
    }
    //[02.getting choosed element]
    GameXY choosed = new GameXY();
    choosed.x = unshootedCells[index].x;
    choosed.y = unshootedCells[index].y;
    //[03.removing choosed element from collection]
    --currentElementsQuantity;
    unshootedCells[index] = unshootedCells[currentElementsQuantity];
    //[04.returning value]
    return choosed;
  }
  
  /**
   * get quantity of unshooted cells
   * 
   * @return unshooted cells quantity
   */
  public int getQuantity(){
    return currentElementsQuantity;
  }
  
  /**
   * find cell with specified coordinates and remove it
   * from collection. if cell is not found - do nothing
   * 
   * @param removable  game coordinates of cell, wich must be finded and
   *                   removed from collection
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  void removeElement(GameXY removable)
  throws NeedFixCode{
    //[creating locals]
    boolean isFound      = false;
    int     removableIdx = -1;
    //[searching specified cell]
    for(int i=0; i<currentElementsQuantity; i++){
      if((unshootedCells[i].x == removable.x)&&
         (unshootedCells[i].y == removable.y)){
        //[specified cell is found]
        isFound      = true;
        removableIdx = i;
        break;
      }
    }
    //[making analysis searching results]
    if(isFound){
      //[removing specified element]
      withdraw(removableIdx);
    }
    return;
  }
  
  /**
   * check specified element for presence in unshooted cells collection
   * 
   * @param cell  element for checking
   * 
   * @return "true" if specified element is absent in collection,
   *         otherwise returns "false".
   */
  boolean isShooted(GameXY cell){
    for(int i=0; i<currentElementsQuantity; i++){
      if((unshootedCells[i].x == cell.x)&&
         (unshootedCells[i].y == cell.y)){
        //[specified cell is found]
        return false;
      }
    }
    return true;
  }
}
