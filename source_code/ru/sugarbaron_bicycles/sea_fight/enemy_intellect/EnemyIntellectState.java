package ru.sugarbaron_bicycles.sea_fight.enemy_intellect;

//[my bicycles]
import ru.sugarbaron_bicycles.library.exceptions.*;
//[interpackage imports]
import ru.sugarbaron_bicycles.sea_fight.GameXY;



/**
 * interface of enemy intellect states
 * 
 * @author sugarbaron ([sugarbaron_bicycles] e-mail:sugarbaron1@mail.ru)
 */
public interface EnemyIntellectState{
  /**
   * shoot to player field.
   * states of enemy intellect must override this method.
   */
  abstract GameXY shoot()
  throws NeedFixCode;
  
  /**
   * analyze last shot result
   * states of enemy intellect must override this method.
   */
  abstract void analyzeLastShot()
  throws NeedFixCode;
}
