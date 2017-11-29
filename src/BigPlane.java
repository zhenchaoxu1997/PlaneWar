package crayxu;

import java.util.Random;

/**
 *
 *@Author: Cray Xu	
 *@Date: Nov 27, 2017 11:28:30 PM
 *
 */

public class BigPlane extends Flyer {
	public static final int DOUBLE_FIRE = 0; //double fire
    public static final int FILE = 1; //add life
      
    /*大飞机类私有成员*/  
    private int xspeed = 1; //x speed == 1
    private int yspeed = 2; //y speed == 2  
    private int awardType; //big airplane award  
        
    public int getAwardType(){  
        return awardType;  
    }  
    
    public BigPlane(){  
        //step1: get picture  
        image = ShootGame.bigplane;  
        //step2: set width and height
        width = image.getWidth();  
        height= image.getHeight();  
        //step3: falling height
        y = -height;  
        //step4:  x coordinate (0, WIDTH-width)
        Random r = new Random();  
        x = r.nextInt(ShootGame.WIDTH - width);  
        //0 or 1 award
        awardType = r.nextInt(2);  
    }  
   
    public void step() {  
        //move in Z line
        x += xspeed;  
        y += yspeed;  
        //can not out of bound, or xspeed*（-1），move in the opposite direction
        if(x < 0 || x > ShootGame.WIDTH - width){  
            xspeed *= -1;  
        }  
    }  
    
    public boolean outOfBounds() {    
        return y > ShootGame.HEIGHT;  
    }  
}
