package crayxu;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 *@Author: Cray Xu	
 *@Date: Nov 27, 2017 2:58:09 PM
 *
 */

public class ShootGame extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 320;  
    public static final int HEIGHT = 568;

    public static final int FRAME_WIDTH = 350;  
    public static final int FRAME_HEIGHT = 600;
    
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage airplane;
    public static BufferedImage bigplane;
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage bullet;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    
    static{
    	try{
    		background = ImageIO.read(ShootGame.class.getResource("background.png"));  
            airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));  
            bigplane = ImageIO.read(ShootGame.class.getResource("bigplane.png"));  
            bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));  
            start = ImageIO.read(ShootGame.class.getResource("start.png"));  
            pause = ImageIO.read(ShootGame.class.getResource("pause.png"));  
            hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));  
            hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));  
            gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));  
    	}catch (IOException e){
    		e.printStackTrace();
    	}
    }
    
    public Hero hero = new Hero();
    public Flyer[] flyers = {};
    public Bullet[] bullets = {};
    
    private int state = START;
    
    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAME_OVER = 3;
    
    public static void main(String[] args) {
    	JFrame frame = new JFrame("PlaneWar");  
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);  
        frame.setAlwaysOnTop(true); //top 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close button 
        frame.setLocationRelativeTo(null);//location null: center
        frame.setVisible(true); // show the window
        
        ShootGame game = new ShootGame(); //add game
        frame.add(game); //add game to jpanel
        game.action();  
	}
    
    public void action(){
    	MouseAdapter mouse = new MouseAdapter(){
    		public void mouseMoved(MouseEvent e){
    			if(state == RUNNING){
    				int x = e.getX();  
                    int y = e.getY();
                    hero.move(x, y);  
    			}
    		}
    		
    		public void mouseClicked(MouseEvent e){
    			if(state == START || state == PAUSE){
                    state = RUNNING;  
                }else if(state == RUNNING){
                    state = PAUSE;  
                }else if(state == GAME_OVER){
                    state = START;  
                    flyers = new Flyer[0];  
                    bullets = new Bullet[0];  
                    hero = new Hero();  
                }  
    		}
    		
            public void mouseExited(MouseEvent e) {  
                if(state == RUNNING){  
                    state = PAUSE;  
                }  
            }
            
            public void mouseEntered(MouseEvent e) {  
                if(state == PAUSE){  
                    state = RUNNING;  
                }  
            }
    		
    	};//comma to end
    	
    	// add mouse to the listener of game
    	this.addMouseMotionListener(mouse); //mouse move 
        this.addMouseListener(mouse);; //mouse listener
        
        //create timer
        Timer timer = new Timer();
        
        //
        timer.schedule(new TimerTask(){
        	private int runTimes = 0;
        	public void run(){
        		if(state == RUNNING){
        			runTimes++;
        			 if(runTimes % 50 == 0){  
                         nextOne(); //randon enemy 
                     }  
        			 for(int i = 0;i < flyers.length;i++){  
                         flyers[i].step();  
                     }  
        			 if(runTimes % 30 == 0){  
                         shoot(); //创建一次子弹  
                     }  
        			 for(int i = 0;i < bullets.length;i++){  
                         bullets[i].step();  
                     } 
        			 hero.step(); 
        			 boom();  
        			 hit();  
        			 outOfBounds();  
        		}
        		
        		repaint();
        		
        	}
        }, 10, 10);
    }
    
    public void paint(Graphics g){
    	//step1: paint background
    	g.drawImage(background, 0, 0, null);  
        //step2: paint hero
        paintHero(g);  
        //step3: paint enemy array 
        paintFlyers(g);  
        //step4: paint bullet array
        paintBullets(g);  
        //step5: paint score and life
        paintScore_Life(g);  
          
        //according to different state, change different picture 
        if(state == START){  
            g.drawImage(start, 0, 0, null);  
        }else if(state == PAUSE){  
            g.drawImage(pause, 0, 0, null);  
        }else if(state == GAME_OVER){  
            g.drawImage(gameover, 0, 0, null);  
        }  
    }
    
    public void paintHero(Graphics g){  
        g.drawImage(hero.image, hero.x, hero.y, null);  
    }  
    
    public void paintFlyers(Graphics g){  
        for(int i = 0;i < flyers.length;i++){  
            g.drawImage(flyers[i].image, flyers[i].x, flyers[i].y, null);  
        }  
    }  
    
    public void paintBullets(Graphics g){  
        for(int i = 0;i < bullets.length;i++){  
            g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);  
        }  
    }  
    
    public void nextOne(){  
        Random r = new Random();  
        Flyer f = null;  
        if(r.nextInt(20) == 0){ //only random == 1 , create bigplane
            f = new BigPlane();  
        }else{ //else common enemy plane 
            f = new Airplane();  
        }  
        //flyers array extends length  
        flyers = Arrays.copyOf(flyers, flyers.length + 1);  
        //new enemy to the last of array  
        flyers[flyers.length - 1] = f;  
    }
    
    public void shoot(){  
        Bullet[] newBullets = hero.shoot(); 
        //according to the number of return bullets, extends the bullets array
        bullets = Arrays.copyOf(bullets, bullets.length + newBullets.length);  
        //copy all elements of newBullets array to the end of bullet array  
        System.arraycopy(newBullets, 0, bullets, bullets.length - newBullets.length, newBullets.length);  
    }  
    public void boom(){
    	for(int i = 0;i < bullets.length;i++){
    		 for(int j = 0;j < flyers.length;j++){
    			 if(Flyer.boom(bullets[i], flyers[j])){
    				 hero.getScore_Award(flyers[j]);  
                     //step1： use the last element to replace the plane which is hit  
                     flyers[j] = flyers[flyers.length - 1];  
                     //step2: compress the array
                     flyers = Arrays.copyOf(flyers, flyers.length - 1);  
                     //delete bullet  
                     bullets[i] = bullets[bullets.length - 1];  
                     bullets = Arrays.copyOf(bullets, bullets.length -1);  
                     i--; //once hit, i-- 
                     break; //once crash, break the loop of flyers
    			 }
    		 }
    	}
    }
    
    public void paintScore_Life(Graphics g){  
        int x = 10;   
        int y = 15; 
        Font font = new Font(Font.SANS_SERIF,Font.BOLD,14);  
        g.setFont(font); 
        //paint score
        g.drawString("SCORE: " + hero.getScore(), x, y);  
        //paint life  
        y += 20;  
        g.drawString("LIFE: " + hero.getLife(), x, y);  
    }  
    
    public void outOfBounds(){
    	Flyer[] Flives = new Flyer[flyers.length];  
    	int index = 0;  
    	for(int i = 0;i < flyers.length;i++){
    		if(!flyers[i].outOfBounds()){ 
                Flives[index] = flyers[i];  
                index++;  
            }
    	}
    	flyers = Arrays.copyOf(Flives, index); 
    	
    	Bullet[] Blives = new Bullet[bullets.length];  
        index = 0;  
        for(int i = 0;i < bullets.length;i++){
        	if(!bullets[i].outOfBounds()){  
                Blives[index] = bullets[i];  
                index++;  
            }  
        }
        bullets = Arrays.copyOf(Blives, index);
    }
    
    public void hit(){
    	Flyer[] lives = new Flyer[flyers.length];  
     
        int index = 0;  
        for(int i = 0;i < flyers.length;i++){
        	if(!hero.hit(flyers[i])){  
                lives[index] = flyers[i];  
                index++;  
            }  
        }
        if(hero.getLife() <= 0){ //if life < 0  game over 
            state = GAME_OVER;  
        }  
        flyers = Arrays.copyOf(lives, index);  
    }    
}
