package ru.sugarbaron_bicycles.sea_fight;

//[standard libraries]
import java.awt.*;
import java.awt.event.*;
//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
import ru.sugarbaron_bicycles.library.log.*;
import ru.sugarbaron_bicycles.sea_fight.fighting_state_machine.*;



/**
 * this class processes mouse events, provided by enemy field.
 * (actually, it is handling of player shoots)
 * 
 * @autor sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
public final class EnemyFieldEventsHandler
extends MouseAdapter{
  //data_section_______________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /** reference to this object */
  private static EnemyFieldEventsHandler instance = null;
  /** log */
  private LogUnit log = null;
  
  //constructors_section_______________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * construct #EnemyFieldEventsHandler example.
   * realisation of singleton pattern
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  private EnemyFieldEventsHandler()
  throws NeedFixCode{
    super();
    log = LogSubsystem.getLog("system_log.txt");
    log.write("[v][EnemyFieldEventsHandler]instance constucted");
  }
  
  //primary_section____________________________________________________________
  /////////////////////////////////////////////////////////////////////////////
  /**
   * method #getInstance() realises singleton pattern.
   * 
   * @return reference to #EnemyFieldEventsHandler object
   * 
   * @throws NeedFixCode  if was detected wrong work of a
   *                      programm, because of errors in code.
   */
  static EnemyFieldEventsHandler getInstance()
  throws NeedFixCode{
    if(null == instance){
      instance = new EnemyFieldEventsHandler();
    }
    return instance;
  }
  
  /**
   * make player shoot
   */
  @Override
  public void mousePressed(MouseEvent event){
    log.write("[v][EnemyFieldEventsHandler]player wants to shoot");
    try{
      //[01.checking permittion for user to make shoot]
      if(Fighting.getCurrentState() != States.playerTurn){
        //[shooting denied]
        log.write("[v][EnemyFieldEventsHandler]shooting denied");
        throw new ExecutionAborted("[v][EnemyFieldEventsHandler]#mousePressed():shooting denied");
      }
      log.write("[v][EnemyFieldViewEventsHandler]shooting allowed");
      //[02.defining game coordinates of cell, which was shooted by player]
      //[coordinates of mouse click relative #enemyFieldView]
      Point mouse = event.getPoint();
      //[coordinates of mouse click relative #enemyFieldView.cells[] array]
      mouse.x = mouse.x - Battlefield.SIZE_UNIT;
      mouse.y = mouse.y - Battlefield.SIZE_UNIT;
      //[checking for shooting coordinates: are they out of battlefield?]
      if( (mouse.x < 0) || (mouse.y < 0) ){
        //[coordinates of shot are out of battlefield]
        log.write("[v][EnemyFieldEventsHandler]coordinates are out of battlefield");
        throw new ExecutionAborted("[v][EnemyFieldEventsHandler]#mousePressed():coordinates are out of battlefield");
      }
      GameXY      shot       = new GameXY(mouse.x, mouse.y);
      Battlefield enemyField = (Battlefield)event.getSource();
      ShotResults shotResult = null;
      boolean     isShooted  = false;
      //[checking choosed cell: is it already shooted?]
      isShooted = enemyField.isCellAlreayShooted(shot);
      if(isShooted){
        log.write("[v][EnemyFieldEventsHandler]choosed cell is already shooted");
        throw new ExecutionAborted("[v][EnemyFieldEventsHandler]#mousePressed():choosed cell is already shooted");
      }
      //[defining player shot results]
      shotResult = enemyField.handleShot(shot);
      //[switching state machine]
      if(ShotResults._miss == shotResult){
        Fighting.receiveSignal(Signals.miss);
      }
      else{
        Fighting.receiveSignal(Signals.playerHit);
      }
    }
    catch(ExecutionAborted exception){
      log.write("[v][EnemyFieldViewEventsHandler]#ExecutionAborted exception");
      log.write("[v][EnemyFieldViewEventsHandler]exception message %s", exception.getMessage());
    }
    catch(NeedFixCode exception){
      log.write("[x][EnemyFieldViewEventsHandler]#NeedFixCode exception");
      log.write("[x][EnemyFieldViewEventsHandler]exception message %s", exception.getMessage());
    }
    return;
  }
}
