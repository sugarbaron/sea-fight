package ru.sugarbaron_bicycles.sea_fight;

import java.awt.event.*;



/**
 * this class processes mouse events, provided by player field
 * (actually, when player clicks on player field,
 * all ships must became deselected)
 * 
 * @autor sugarbaron ([sugarbaron_bicycles] sugarbaron1@mail.ru)
 */
 public class PlayerFieldEventsHandler
 extends MouseAdapter{
   //data_section_______________________________________________________________
   /////////////////////////////////////////////////////////////////////////////
   /** reference to this object */
   private static PlayerFieldEventsHandler instance = null;
   
   //constructors_section_______________________________________________________
   /////////////////////////////////////////////////////////////////////////////
   /**
    * construct BattlefieldEventHandler.
    * realisation of singleton pattern
    */
   private PlayerFieldEventsHandler(){
     super();
   }
   
   //primary_section____________________________________________________________
   /////////////////////////////////////////////////////////////////////////////
   /**
    * method #getInstance() realises singleton pattern.
    * 
    * @return reference to ShipsMouseEventsHandler object 
    */
   static PlayerFieldEventsHandler getInstance(){
     if(null == instance){
       instance = new PlayerFieldEventsHandler();
     }
     return instance;
   }
   
   /**
    * on mouse pressing event all battleships must be deselected
    */
   @Override
   public void mousePressed(MouseEvent event){
     Battlefield sourceField = (Battlefield)event.getSource();
     sourceField.deselectAll();
     //[repaint!]
     sourceField.repaint();
   }
 }