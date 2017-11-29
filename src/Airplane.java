package crayxu;

import java.util.Random;

/**
 *
 *@Author: Cray Xu	
 *@Date: Nov 27, 2017 11:28:01 PM
 *
 */

public class Airplane extends Flyer {
	private int speed = 2; //the speed of moving down
    private int score = 5; //get score
       
    public int getScore(){  
        return score;  
    }  
      
    //no parameter constructor
    public Airplane(){  
        image = ShootGame.airplane;  
        width = image.getWidth();  
        height = image.getHeight();  
        y = -height;  
        Random r = new Random();  
        x = r.nextInt(ShootGame.WIDTH - width);  
    }  
  
    public void step() {   
        y += speed;  
    }  
  
    public boolean outOfBounds() {  
        return y > ShootGame.HEIGHT;  
    }  
}
