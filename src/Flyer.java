package crayxu;

import java.awt.image.BufferedImage;

/**
 *
 *@Author: Cray Xu	
 *@Date: Nov 27, 2017 10:58:49 PM
 *
 */

public abstract class Flyer {
	protected int x; 
	protected int y; 
    protected int height; 
    protected int width; 
    protected BufferedImage image; 

    public abstract void step(); 
    
    public abstract boolean outOfBounds();  
    
    public static boolean boom(Flyer f1,Flyer f2){  
        //step1: get the center points of two rectangles
        int f1x = f1.x + f1.width/2;  
        int f1y = f1.y + f1.height/2;  
        int f2x = f2.x + f2.width/2;  
        int f2y = f2.y + f2.height/2;  
        //step2: detect horizontal and vertical crash  
        boolean H = Math.abs(f1x - f2x) < (f1.width + f2.width)/2;  
        boolean V = Math.abs(f1y -f2y) < (f1.height + f2.height)/2;  
        //step3: both crash will return true
        return H&V;  
    }  
    
}
