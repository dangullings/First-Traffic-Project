import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class Car {
    private double x, y; 
    private double carDist, lightDist;
    private double startX, startY;
    private double destX, destY;
    private double accelRate = .0002;
    private double brakeRate = .0002;
    private double vx, vy;
    private int radius;
    private double w;
    private double h;
	private double maxV;
	private double stopTime;
    private double tripTime;
    
    private int carAhead;
    private int carBehind;
    private int road, block;
    private double carGap;
    private int minCarGap = 3;
	private int lightGap = 20;
    private int nextLight;
	private int index;
	private int interval = 1;
    private int delay = 5000;
    private int period = 1000;
    private int prevProportionalStopTime;
    private int weight;
    
    private char direction, rlLane, rlsLane;
    
    private boolean isAlive;
    private boolean isIcon;
    
	private String type;
	private Timer tripTimer;
	private Color color = new Color(0, 0, 0);
    
	public Car() {
		tripTimer = new Timer();
	}
	
	public void draw() {
		if (Main.viewType == getType() || Main.viewType == "All" || (isIcon))
			StdDraw.setPenColor(color);
		else
			StdDraw.setPenColor(Color.GRAY);
		
        StdDraw.filledRectangle(x, y, w, h);
    }

    public double getCarDist() {
		return carDist;
	}

	public void setCarDist(double carDist) {
		this.carDist = carDist;
	}

	public double getLightDist() {
		return lightDist;
	}

	public void setLightDist(double lightDist) {
		this.lightDist = lightDist;
	}
	
	public boolean nearStoplight(int s){
    	boolean atStoplight = false;
    	
    	if (this.direction == 'N')
    		if ((this.getY()+lightGap>Main.stoplight[s].getY())&&(this.getY()-lightGap<Main.stoplight[s].getY()))
    			if ((this.getX()+5>Main.stoplight[s].getX())&&(this.getX()-5<Main.stoplight[s].getX()))
    				if (this.getY()<Main.stoplight[s].getY())
    					atStoplight = true;
    	if (this.direction == 'S')
    		if ((this.getY()+lightGap>Main.stoplight[s].getY())&&(this.getY()-lightGap<Main.stoplight[s].getY()))
    			if ((this.getX()+5>Main.stoplight[s].getX())&&(this.getX()-5<Main.stoplight[s].getX()))
    				if (this.getY()>Main.stoplight[s].getY())
    					atStoplight = true;
    	if (this.direction == 'W')
    		if ((this.getY()+5>Main.stoplight[s].getY())&&(this.getY()-5<Main.stoplight[s].getY()))
    			if ((this.getX()+lightGap>Main.stoplight[s].getX())&&(this.getX()-lightGap<Main.stoplight[s].getX()))
    				if (this.getX()>Main.stoplight[s].getX())
    					atStoplight = true;
    	if (this.direction == 'E')
    		if ((this.getY()+5>Main.stoplight[s].getY())&&(this.getY()-5<Main.stoplight[s].getY()))
    			if ((this.getX()+lightGap>Main.stoplight[s].getX())&&(this.getX()-lightGap<Main.stoplight[s].getX()))
    				if (this.getX()<Main.stoplight[s].getX())
    					atStoplight = true;
    	
		return atStoplight;
    }
    
    public boolean atStoplight(int s){
    	boolean atStoplight = false;
    	
    	if (this.direction == 'N')
    		if ((this.getY()+1>Main.stoplight[s].getY())&&(this.getY()-1<Main.stoplight[s].getY()))
    			if ((this.getX()+5>Main.stoplight[s].getX())&&(this.getX()-5<Main.stoplight[s].getX()))
    				if (this.getY()<Main.stoplight[s].getY())
    					atStoplight = true;
    	if (this.direction == 'S')
    		if ((this.getY()+1>Main.stoplight[s].getY())&&(this.getY()-1<Main.stoplight[s].getY()))
    			if ((this.getX()+5>Main.stoplight[s].getX())&&(this.getX()-5<Main.stoplight[s].getX()))
    				if (this.getY()>Main.stoplight[s].getY())
    					atStoplight = true;
    	if (this.direction == 'W')
    		if ((this.getY()+5>Main.stoplight[s].getY())&&(this.getY()-5<Main.stoplight[s].getY()))
    			if ((this.getX()+1>Main.stoplight[s].getX())&&(this.getX()-1<Main.stoplight[s].getX()))
    				if (this.getX()>Main.stoplight[s].getX())
    					atStoplight = true;
    	if (this.direction == 'E')
    		if ((this.getY()+5>Main.stoplight[s].getY())&&(this.getY()-5<Main.stoplight[s].getY()))
    			if ((this.getX()+1>Main.stoplight[s].getX())&&(this.getX()-1<Main.stoplight[s].getX()))
    				if (this.getX()<Main.stoplight[s].getX())
    					atStoplight = true;
    	
		return atStoplight;
    }
    
    public void setStoplightIncomingCarAmt(int s){
    	if ((direction == 'N')&&(getRlsLane() != 'R')){
    		if ((getY()+30>=Main.stoplight[s].getY())&&(getY()-30<=Main.stoplight[s].getY()))
    			if ((getX()+30>=Main.stoplight[s].getX())&&(getX()-30<=Main.stoplight[s].getX()))
    				if (getY()<=Main.stoplight[s].getY())
    					Main.stoplight[s].incomingTrafficVert+=getWeight();
    		if ((getY()+10>=Main.stoplight[s].getY())&&(getY()-10<=Main.stoplight[s].getY()))
    			if ((getX()+10>=Main.stoplight[s].getX())&&(getX()-10<=Main.stoplight[s].getX()))
    				if (getY()<=Main.stoplight[s].getY())
    					if (rlsLane == 'S')
    						Main.stoplight[s].crossTrafficNorth++;
    	}
    	if ((direction == 'S')&&(getRlsLane() != 'R')){
    		if ((getY()+30>=Main.stoplight[s].getY())&&(getY()-30<=Main.stoplight[s].getY()))
    			if ((getX()+30>=Main.stoplight[s].getX())&&(getX()-30<=Main.stoplight[s].getX()))
    				if (getY()>=Main.stoplight[s].getY())
    					Main.stoplight[s].incomingTrafficVert+=getWeight();
    		if ((getY()+10>=Main.stoplight[s].getY())&&(getY()-10<=Main.stoplight[s].getY()))
    			if ((getX()+10>=Main.stoplight[s].getX())&&(getX()-10<=Main.stoplight[s].getX()))
    				if (getY()>=Main.stoplight[s].getY())
    					if (rlsLane == 'S')
    						Main.stoplight[s].crossTrafficSouth++;	
    	}
    	if ((direction == 'W')&&(getRlsLane() != 'R')){
    		if ((getY()+30>=Main.stoplight[s].getY())&&(getY()-30<=Main.stoplight[s].getY()))
    			if ((getX()+30>=Main.stoplight[s].getX())&&(getX()-30<=Main.stoplight[s].getX()))
    				if (getX()>=Main.stoplight[s].getX())
    					Main.stoplight[s].incomingTrafficHorz+=getWeight();
    		if ((getY()+10>=Main.stoplight[s].getY())&&(getY()-10<=Main.stoplight[s].getY()))
    			if ((getX()+10>=Main.stoplight[s].getX())&&(getX()-10<=Main.stoplight[s].getX()))
    				if (getX()>=Main.stoplight[s].getX())
    					if (rlsLane == 'S')
    						Main.stoplight[s].crossTrafficWest++;
    	}
    	if ((direction == 'E')&&(getRlsLane() != 'R')){
    		if ((getY()+30>=Main.stoplight[s].getY())&&(getY()-30<=Main.stoplight[s].getY()))
    			if ((getX()+30>=Main.stoplight[s].getX())&&(getX()-30<=Main.stoplight[s].getX()))
    				if (getX()<=Main.stoplight[s].getX())
    					Main.stoplight[s].incomingTrafficHorz+=getWeight();
    		if ((getY()+10>=Main.stoplight[s].getY())&&(getY()-10<=Main.stoplight[s].getY()))
    			if ((getX()+10>=Main.stoplight[s].getX())&&(getX()-10<=Main.stoplight[s].getX()))
    				if (getX()<=Main.stoplight[s].getX())
    					if (rlsLane == 'S')
    						Main.stoplight[s].crossTrafficEast++;
    	}
    }
    
    public boolean crossTrafficRTurnNS(int s) {
    	boolean clear = false;
    	
    	if (getDirection() == 'N')
    		if (Main.stoplight[s].crossTrafficEast == 0)
    			clear = true;
    	if (getDirection() == 'S')
    		if (Main.stoplight[s].crossTrafficWest == 0)
    			clear = true;
    	
		return clear;
    }
    
    public boolean crossTrafficRTurnEW(int s) {
    	boolean clear = false;

    	if (getDirection() == 'E')
    		if (Main.stoplight[s].crossTrafficSouth == 0)
    			clear = true;
    	if (getDirection() == 'W')
    		if (Main.stoplight[s].crossTrafficNorth == 0)
    			clear = true;
    	
		return clear;
    }
    
    public void changeLanes(){
    	if (direction == 'N'){
        	if ((y+1>=Main.road[road].right.block[block].right.getY0())&&(x==Main.road[road].right.getX0())){
        		if ((rlsLane == 'S')&&(Main.road[road].right.block[block].getCarAmtLengthMLane()>=12))
        			vy = 0;
        		if ((rlsLane == 'R')&&(Main.road[road].right.block[block].right.getCarAmtLength()>=12))
        			vy = 0;
        		if ((rlsLane == 'L')&&(Main.road[road].right.block[block].left.getCarAmtLength()>=12))
        			vy = 0;
        	}
        		if ((y >= Main.road[road].right.block[block].right.getY0()) && (x == Main.road[road].right.getX0())&&(block != 7)){
            		if (rlsLane == 'R'){
            			x=Main.road[road].right.block[block].right.getX0();
        				Main.road[road].right.block[block].right.setCarAmt(1);
        				Main.road[road].right.block[block].right.setCarAmtLength(getRadius());
        				setCarAhead(0);
        				if ((Main.car[Main.road[road].right.block[block].right.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].right.block[block].right.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].right.block[block].right.getLastCar()].getBlock() == block))
        					setCarAhead(Main.road[road].right.block[block].right.getLastCar());
        				Main.road[road].right.block[block].right.setLastCar(index);
        			}
            		if (rlsLane == 'L'){
            			x=Main.road[road].right.block[block].left.getX0();
        				Main.road[road].right.block[block].left.setCarAmt(1);
        				Main.road[road].right.block[block].left.setCarAmtLength(getRadius());
        				setCarAhead(0);
        				if ((Main.car[Main.road[road].right.block[block].left.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].right.block[block].left.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].right.block[block].left.getLastCar()].getBlock() == block))
        					setCarAhead(Main.road[road].right.block[block].left.getLastCar());
        				Main.road[road].right.block[block].left.setLastCar(index);
        			}
            		if (rlsLane == 'S'){
            			Main.road[road].right.block[block].setMLane(1);
            			Main.road[road].right.block[block].setCarAmtLengthMLane(getRadius());
            			setCarAhead(0);
            			if ((Main.car[Main.road[road].right.block[block].getStrLastCar()].getRoad()==getRoad()) && (Main.car[Main.road[road].right.block[block].getStrLastCar()].getBlock()==getBlock()))
            				setCarAhead(Main.road[road].right.block[block].getStrLastCar());
            			Main.road[getRoad()].right.block[getBlock()].setStrLastCar(index);
            			x=Main.road[road].right.block[block].getX0()+.00001;
            		}
            	}
    	}
        if (direction == 'S'){
        	if ((y-1<=Main.road[road].left.block[block].right.getY1())&&(x==Main.road[road].left.getX0())){
        		if ((rlsLane == 'S')&&(Main.road[road].left.block[block].getCarAmtLengthMLane()>=12))
        			vy = 0;
        		if ((rlsLane == 'R')&&(Main.road[road].left.block[block].right.getCarAmtLength()>=12))
        			vy = 0;
        		if ((rlsLane == 'L')&&(Main.road[road].left.block[block].left.getCarAmtLength()>=12))
        			vy = 0;
        	}
        if ((y <= Main.road[road].left.block[block].left.getY1()) && (x == Main.road[road].left.getX0()) && (block != 0)){
    		if (rlsLane == 'R'){
    			x=Main.road[road].left.block[block].right.getX0();
				Main.road[road].left.block[block].right.setCarAmt(1);
				Main.road[road].left.block[block].right.setCarAmtLength(getRadius());
				setCarAhead(0);
				if ((Main.car[Main.road[road].left.block[block].right.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].left.block[block].right.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].left.block[block].right.getLastCar()].getBlock() == block))
					setCarAhead(Main.road[road].left.block[block].right.getLastCar());
				Main.road[road].left.block[block].right.setLastCar(index);
			}
    		if (rlsLane == 'L'){
    			x=Main.road[road].left.block[block].left.getX0();
				Main.road[road].left.block[block].left.setCarAmt(1);
				Main.road[road].left.block[block].left.setCarAmtLength(getRadius());
				setCarAhead(0);
				if ((Main.car[Main.road[road].left.block[block].left.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].left.block[block].left.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].left.block[block].left.getLastCar()].getBlock() == block))
					setCarAhead(Main.road[road].left.block[block].left.getLastCar());
				Main.road[road].left.block[block].left.setLastCar(index);
			}
    		if (rlsLane == 'S'){
    			Main.road[road].left.block[block].setMLane(1);
    			Main.road[road].left.block[block].setCarAmtLengthMLane(getRadius());
    			setCarAhead(0);
    			if ((Main.car[Main.road[road].left.block[block].getStrLastCar()].getRoad()==getRoad()) && (Main.car[Main.road[road].left.block[block].getStrLastCar()].getBlock()==getBlock()))
    				setCarAhead(Main.road[road].left.block[block].getStrLastCar()); 
    			Main.road[getRoad()].left.block[getBlock()].setStrLastCar(index);
    			x=Main.road[road].left.block[block].getX0()+.00001;
    		}
    	}
        }
        if (direction == 'W'){
        	if ((x-1<=Main.road[road].left.block[block].right.getX1())&&(y==Main.road[road].left.getY0())){
        		if ((rlsLane == 'S')&&(Main.road[road].left.block[block].getCarAmtLengthMLane()>=12))
        			vx = 0;
        		if ((rlsLane == 'R')&&(Main.road[road].left.block[block].right.getCarAmtLength()>=12))
        			vx = 0;
        		if ((rlsLane == 'L')&&(Main.road[road].left.block[block].left.getCarAmtLength()>=12))
        			vx = 0;
        	}
        	if ((x <= Main.road[road].left.block[block].right.getX1()) && (y == Main.road[road].left.getY0()) && (block != 0)){
        		if (rlsLane=='R'){
        			y=Main.road[road].left.block[block].right.getY0();
    				Main.road[road].left.block[block].right.setCarAmt(1);
    				Main.road[road].left.block[block].right.setCarAmtLength(getRadius());
    				setCarAhead(0);
    				if ((Main.car[Main.road[road].left.block[block].right.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].left.block[block].right.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].left.block[block].right.getLastCar()].getBlock() == block))
    					setCarAhead(Main.road[road].left.block[block].right.getLastCar());
    				Main.road[road].left.block[block].right.setLastCar(index);
    			}
        		if (rlsLane == 'L'){
        			y=Main.road[road].left.block[block].left.getY0();
    				Main.road[road].left.block[block].left.setCarAmt(1);
    				Main.road[road].left.block[block].left.setCarAmtLength(getRadius());
    				setCarAhead(0);
    				if ((Main.car[Main.road[road].left.block[block].left.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].left.block[block].left.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].left.block[block].left.getLastCar()].getBlock() == block))
    					setCarAhead(Main.road[road].left.block[block].left.getLastCar());
    				Main.road[road].left.block[block].left.setLastCar(index);
    			}
        		if (rlsLane == 'S'){
        			Main.road[road].left.block[block].setMLane(1);
        			Main.road[road].left.block[block].setCarAmtLengthMLane(getRadius());
        			setCarAhead(0);
        			if ((Main.car[Main.road[road].left.block[block].getStrLastCar()].getRoad()==getRoad()) && (Main.car[Main.road[road].left.block[block].getStrLastCar()].getBlock()==getBlock()))
        				setCarAhead(Main.road[road].left.block[block].getStrLastCar());  
        			Main.road[getRoad()].left.block[getBlock()].setStrLastCar(index);
        			y=Main.road[road].left.block[block].getY0()+.00001;
        		}
        	}
        }
        if (direction == 'E'){
        	if ((x+1>=Main.road[road].right.block[block].right.getX0())&&(y==Main.road[road].right.getY0())){
        		if ((rlsLane == 'S')&&(Main.road[road].right.block[block].getCarAmtLengthMLane()>=12))
        			vx = 0;
        		if ((rlsLane == 'R')&&(Main.road[road].right.block[block].right.getCarAmtLength()>=12))
        			vx = 0;
        		if ((rlsLane == 'L')&&(Main.road[road].right.block[block].left.getCarAmtLength()>=12))
        			vx = 0;
        	}
        	if ((x >= Main.road[road].right.block[block].right.getX0()) && (y == Main.road[road].right.getY0()) && (block != 7)){
        		if (rlsLane == 'R'){
        			y=Main.road[road].right.block[block].right.getY0();
    				Main.road[road].right.block[block].right.setCarAmt(1);
    				Main.road[road].right.block[block].right.setCarAmtLength(getRadius());
    				setCarAhead(0);
    				if ((Main.car[Main.road[road].right.block[block].right.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].right.block[block].right.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].right.block[block].right.getLastCar()].getBlock() == block))
    					setCarAhead(Main.road[road].right.block[block].right.getLastCar());
    				Main.road[road].right.block[block].right.setLastCar(index);
    			}
        		if (rlsLane == 'L'){
        			y=Main.road[road].right.block[block].left.getY0();
    				Main.road[road].right.block[block].left.setCarAmt(1);
    				Main.road[road].right.block[block].left.setCarAmtLength(getRadius());
    				setCarAhead(0);
    				if ((Main.car[Main.road[road].right.block[block].left.getLastCar()].getRoad() == road) && (Main.car[Main.road[road].right.block[block].left.getLastCar()].getRlsLane() == rlsLane) && (Main.car[Main.road[road].right.block[block].left.getLastCar()].getBlock() == block))
    					setCarAhead(Main.road[road].right.block[block].left.getLastCar());
    				Main.road[road].right.block[block].left.setLastCar(index);
    			}
        		if (rlsLane == 'S'){
        			Main.road[road].right.block[block].setMLane(1);
        			Main.road[road].right.block[block].setCarAmtLengthMLane(getRadius());
        			setCarAhead(0);
        			if ((Main.car[Main.road[road].right.block[block].getStrLastCar()].getRoad()==getRoad()) && (Main.car[Main.road[road].right.block[block].getStrLastCar()].getBlock()==getBlock()))
        				setCarAhead(Main.road[road].right.block[block].getStrLastCar());
        			Main.road[getRoad()].right.block[getBlock()].setStrLastCar(index);
        			y=Main.road[road].right.block[block].getY0()+.00001;
        		}
        	}
        }
    }
    
    public void setVel(Car that){
    	vy = maxV;
    	vx = maxV;
		if ((this.getRoad() == that.getRoad()) && (this.getBlock() == that.getBlock())){
		if (direction == that.direction){
			if (this.getX() == that.getX()){
				setCarDist(Math.abs((this.getY() - that.getY())));
				
				if (getCarDist() <= carGap)
					vy = (maxV * (getCarDist() / carGap));
				else
					speedUp();
				
				if (getCarDist()<=(carGap/3))
					vy = 0;
			}
			else if (this.getY() == that.getY()){
				setCarDist(Math.abs((this.getX()) - (that.getX())));
				
				if (getCarDist() <= carGap)
					vx = (maxV * (getCarDist() / carGap));
				else
					speedUp();
				
				if (getCarDist()<=(carGap/3))
					vx = 0;
			}
		}
		}
	}
   
	public void setVel(Stoplight light){
			if ((direction == 'N') || (direction == 'S')){
				setLightDist(Math.abs(this.getY() - light.getY()));
			
				if (getLightDist() <= lightGap)
					vy = (maxV * (getLightDist() / lightGap));
				else
					vy = maxV;
			
				if (getLightDist()<=(lightGap/2))
					vy = 0;
			}
			else if ((direction == 'E') || (direction == 'W')){
				setLightDist(Math.abs(this.getX() - light.getX()));
			
				if (getLightDist() <= lightGap)
					vx = (maxV * (getLightDist() / lightGap));
				else
					vx = maxV;
			
				if (getLightDist()<=(lightGap/2))
					vx = 0;
			}
	}
	
    public void move() {
    	setVel(Main.car[getCarAhead()]);
    	
    	changeLanes();
        
    			if (nearStoplight(getNextLight())){
        			if ((Main.stoplight[getNextLight()].State == lightState.VERT_RED) || (Main.stoplight[getNextLight()].State == lightState.HORZ_YELLOW)){
        				
        				if (rlsLane != 'R'){
        					if ((direction == 'N') || (direction == 'S'))
        						if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
        							setVel(Main.stoplight[getNextLight()]);
        				//else{
        					//if (crossTrafficRTurnNS(getNextLight()))
            					//vx = maxV;
        				//}
        				if (((direction == 'E') || (direction == 'W')) && (Main.stoplight[getNextLight()].State == lightState.HORZ_YELLOW))
    						if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
    							if (Main.stoplight[getNextLight()].yellowTime<=1)
    								setVel(Main.stoplight[getNextLight()]);

        				}
        				if (direction == 'W'){
        					if (rlsLane == 'S'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].left.block[Main.stoplight[getNextLight()].blockW].getCarAmt() >= Main.blockCarCap){
        						//	vx = 0;
        						//}
        						//else{
        							//if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                						//vx = maxV;
        						//}
        					}
        					if (rlsLane == 'R'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].right.block[Main.stoplight[getNextLight()].blockN].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}
        						//else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vx = maxV;
        						//}
        					}
        					if (rlsLane == 'L'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].left.block[Main.stoplight[getNextLight()].blockS].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}
        						//else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vx = maxV;
        						//}
        					}
        				}
        				if (direction == 'E'){
        					if (rlsLane == 'S'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].right.block[Main.stoplight[getNextLight()].blockE].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}
        						//else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vx = maxV;
        						//}
        					}
        					if (rlsLane == 'R'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].left.block[Main.stoplight[getNextLight()].blockS].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}
        						//else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vx = maxV;
        						//}
        					}
        					if (rlsLane == 'L'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].right.block[Main.stoplight[getNextLight()].blockN].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}
        						//else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vx = maxV;
        						//}
        					}
        				}	
        			}
        			if ((Main.stoplight[getNextLight()].State == lightState.VERT_GREEN) || (Main.stoplight[getNextLight()].State == lightState.VERT_YELLOW)){
        				if (rlsLane != 'R'){
        					if ((direction == 'E') || (direction == 'W'))
        						if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
        							setVel(Main.stoplight[getNextLight()]);
        				//else{
        					//if (crossTrafficRTurnNS(getNextLight()))
            					//vx = maxV;
        				//}
        				if (((direction == 'N') || (direction == 'S')) && (Main.stoplight[getNextLight()].State == lightState.VERT_YELLOW))
    						if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
    							if (Main.stoplight[getNextLight()].yellowTime<=1)
    								setVel(Main.stoplight[getNextLight()]);
        				}
        				
        				if (direction == 'N'){
        					if (rlsLane == 'S'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].right.block[Main.stoplight[getNextLight()].blockN].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        					if (rlsLane == 'R'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].right.block[Main.stoplight[getNextLight()].blockE].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        					if (rlsLane == 'L'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].left.block[Main.stoplight[getNextLight()].blockW].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        				}
        				if (direction == 'S'){
        					if (rlsLane == 'S'){
        						//if (Main.road[Main.stoplight[getNextLight()].vertRoad].left.block[Main.stoplight[getNextLight()].blockS].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        					if (rlsLane == 'R'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].left.block[Main.stoplight[getNextLight()].blockW].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        					if (rlsLane == 'L'){
        						//if (Main.road[Main.stoplight[getNextLight()].horzRoad].right.block[Main.stoplight[getNextLight()].blockE].getCarAmt() >= Main.blockCarCap){
        						//	setVel(Main.stoplight[getNextLight()]);
        						//}else{
        						//	if ((getCarAhead() == 0) || (Main.car[getCarAhead()].road!=road) || (Main.car[getCarAhead()].block!=block))
                				//		vy = maxV;
        						//}
        					}
        				}	
        			}
        		}
    			
        		if (atStoplight(getNextLight())){
        			char prevDir = direction;
        			setCarAhead(0);
        			int r, b, c;
        			
        			if (direction == 'N'){
        				if (rlsLane == 'R'){
        					r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockE();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].right.setCarAmt(-1);
        					Main.road[road].right.block[block].right.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightE());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'E';
        				}
        				if (rlsLane == 'L'){
        					r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockW();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].left.block[b].getX1());
        					setY(Main.road[r].left.block[b].getY0());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].left.setCarAmt(-1);
        					Main.road[road].right.block[block].left.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightW());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'W';
        				}
            			if (rlsLane == 'S'){
            				r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockN();
        					c = Main.road[r].right.block[b].getLastCar();
            				setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].setMLane(-1);
        					Main.road[road].right.block[block].setCarAmtLengthMLane(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightN());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'N';
            			}

            			if (direction == 'E'){
            				if (getX()+65<getDestX())
            					setrlsLane('S');
            				else if ((getX()+35<getDestX())&&(getDestX()!=250))
        						setrlsLane('S');
        					else
        						if (getY()>getDestY())
        							setrlsLane('R');
        						else
        							setrlsLane('L');
        				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
        					setrlsLane('S');
            			}
            			if (direction == 'W'){
            				if (getX()-65>getDestX())
            					setrlsLane('S');
            				else if ((getX()-35>getDestX())&&(getDestX()!=0))
            					setrlsLane('S');
            				else
            					if (getY()>getDestY())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
            					setrlsLane('S');
            			}
            			if (direction == 'N'){
            				if (getY()+65<getDestY())
            					setrlsLane('S');
            				else if ((getY()+35<getDestY())&&(getDestY()!=250))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');
            			}
        			}
        			else if (direction == 'S'){
        				if (rlsLane == 'R'){
        					r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockW();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].left.block[b].getX1());
        					setY(Main.road[r].left.block[b].getY0());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].right.setCarAmt(-1);
        					Main.road[road].left.block[block].right.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightW());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'W';
        				}
        				if (rlsLane == 'L'){
        					r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockE();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].left.setCarAmt(-1);
        					Main.road[road].left.block[block].left.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightE());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'E';
        				}
            			if (rlsLane == 'S'){
            				r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockS();
        					c = Main.road[r].right.block[b].getLastCar();
            				setX(Main.road[r].left.block[b].getX0());
        					setY(Main.road[r].left.block[b].getY1());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].setMLane(-1);
        					Main.road[road].left.block[block].setCarAmtLengthMLane(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightS());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'S';
            			}

            			if (direction == 'E'){
            				if (getX()+65<getDestX())
            					setrlsLane('S');
            				else if ((getX()+35<getDestX())&&(getDestX()!=250))
        						setrlsLane('S');
        					else
        						if (getY()>getDestY())
        							setrlsLane('R');
        						else
        							setrlsLane('L');
        				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
        					setrlsLane('S');
            			}
            			if (direction == 'W'){
            				if (getX()-65>getDestX())
            					setrlsLane('S');
            				else if ((getX()-35>getDestX())&&(getDestX()!=0))
            					setrlsLane('S');
            				else
            					if (getY()>getDestY())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
            					setrlsLane('S');
            			}
            			if (direction == 'S'){
            				if (getY()-65>getDestY())
            					setrlsLane('S');
            				else if ((getY()-35>getDestY())&&(getDestY()!=0))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('R');
            					else
            						setrlsLane('L');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');
            			}
        			}
        			else if (direction == 'W'){
        				if (rlsLane == 'R'){
        					r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockN();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].right.setCarAmt(-1);
        					Main.road[road].left.block[block].right.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightN());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'N';
        				}
        				if (rlsLane == 'L'){
        					r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockS();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].left.block[b].getX0());
        					setY(Main.road[r].left.block[b].getY1());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].left.setCarAmt(-1);
        					Main.road[road].left.block[block].left.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightS());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'S';
        				}
            			if (rlsLane == 'S'){
            				r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockW();
        					c = Main.road[r].right.block[b].getLastCar();
            				setX(Main.road[r].left.block[b].getX1());
        					setY(Main.road[r].left.block[b].getY0());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].left.block[block].setCarAmt(-1);
        					Main.road[road].left.block[block].setMLane(-1);
        					Main.road[road].left.block[block].setCarAmtLengthMLane(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightW());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'W';
            			}
            			
            			if (direction == 'W'){
            				if (getX()-65>getDestX())
            					setrlsLane('S');
            				else if (getX()-35>getDestX()){
            					setrlsLane('S');
            					if ((getY()+10<getDestY())||(getY()-10>getDestY()))
            						if (getY()>getDestY())
                						setrlsLane('L');
                					else
                						setrlsLane('R');
            				}
            				else
            					if (getY()>getDestY())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
            					setrlsLane('S');
            			}
            			if (direction == 'N'){
            				if (getY()+65<getDestY())
            					setrlsLane('S');
            				else if ((getY()+35<getDestY())&&(getDestY()!=250))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');
            			}
            			if (direction == 'S'){
            				if (getY()-65>getDestY())
            					setrlsLane('S');
            				else if ((getY()-35>getDestY())&&(getDestY()!=0))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('R');
            					else
            						setrlsLane('L');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');;
            			}
        			}
        			else if (direction == 'E'){
        				if (rlsLane == 'R'){
        					r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockS();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].left.block[b].getX0());
        					setY(Main.road[r].left.block[b].getY1());
        					Main.road[r].left.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].right.setCarAmt(-1);
        					Main.road[road].right.block[block].right.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightS());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].left.block[b].getLastCar());
        					Main.road[r].left.block[b].setLastCar(index);
        					direction = 'S';
        				}
        				if (rlsLane == 'L'){
        					r = Main.stoplight[getNextLight()].getVertRoad();
        					b = Main.stoplight[getNextLight()].getBlockN();
        					c = Main.road[r].right.block[b].getLastCar();
        					setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].left.setCarAmt(-1);
        					Main.road[road].right.block[block].left.setCarAmtLength(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightN());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'N';
        				}
            			if (rlsLane == 'S'){
            				r = Main.stoplight[getNextLight()].getHorzRoad();
        					b = Main.stoplight[getNextLight()].getBlockE();
        					c = Main.road[r].right.block[b].getLastCar();
            				setX(Main.road[r].right.block[b].getX0());
        					setY(Main.road[r].right.block[b].getY0());
        					Main.road[r].right.block[b].setCarAmt(1);
        					Main.road[road].right.block[block].setCarAmt(-1);
        					Main.road[road].right.block[block].setMLane(-1);
        					Main.road[road].right.block[block].setCarAmtLengthMLane(-getRadius());
        					setNextLight(Main.stoplight[getNextLight()].getLightE());
        					setRoad(r);
        					setBlock(b);
        					//if (Main.car[c].getRoad()==getRoad()&&Main.car[c].getBlock()==getBlock())
        						setCarAhead(Main.road[r].right.block[b].getLastCar());
        					Main.road[r].right.block[b].setLastCar(index);
        					direction = 'E';
            			}
            			
            			if (direction == 'E'){
            				if (getX()+65<getDestX())
            					setrlsLane('S');
            				else if ((getX()+35<getDestX())&&(getDestX()!=250))
        						setrlsLane('S');
        					else
        						if (getY()>getDestY())
        							setrlsLane('R');
        						else
        							setrlsLane('L');
        				if ((getY()+10>getDestY())&&(getY()-10<getDestY()))
        					setrlsLane('S');
            			}
            			if (direction == 'N'){
            				if (getY()+65<getDestY())
            					setrlsLane('S');
            				else if ((getY()+35<getDestY())&&(getDestY()!=250))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('L');
            					else
            						setrlsLane('R');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');
            			}
            			if (direction == 'S'){
            				if (getY()-65>getDestY())
            					setrlsLane('S');
            				else if ((getY()-35>getDestY())&&(getDestY()!=0))
            					setrlsLane('S');
            				else
            					if (getX()>getDestX())
            						setrlsLane('R');
            					else
            						setrlsLane('L');
            				if ((getX()+10>getDestX())&&(getX()-10<getDestX()))
            					setrlsLane('S');
            			}
        			}
        			
        			if ((direction == 'N') || (direction == 'S'))
        				carGap = minCarGap + h + Main.car[getCarAhead()].h;
        			else if ((direction == 'E') || (direction == 'W'))
        				carGap = minCarGap + w + Main.car[getCarAhead()].w;
        			
        			if ((prevDir == 'N') || (prevDir == 'S')){
        				if ((direction == 'W')||(direction == 'E')){
        					double tempw = getW();
            				setW(getH());
            				setH(tempw);
        				}
        			}
        			else if ((prevDir == 'W')||(prevDir == 'E')){
        				if ((direction == 'N')||(direction == 'S')){
        					double tempw = getW();
            				setW(getH());
            				setH(tempw);
        				}
        			}
        		}
        		
        		setStoplightIncomingCarAmt(getNextLight());
        	
    	if ((direction == 'N') && (y >= 250))
    		reset(direction);
    	else if ((direction == 'S') && (y <= 0))
    		reset(direction);
    	else if ((direction == 'W') && (x <= 0))
    		reset(direction);
    	else if ((direction == 'E') && (x >= 250))
    		reset(direction);
    	
    	if (direction == 'N')
        	y += vy;
    	else if (direction == 'S')
    		y -= vy;
    	else if (direction == 'W')
    		x -= vx;
    	else if (direction == 'E')
    		x += vx;
    }

    public void TripSimulation(){
        tripTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if (vx+vy <= 0)
            		stopTime++;
            	tripTime++;
            }
        }, delay, period);
    }
    
    public void reset(char d){
    	if (d == 'N'){
    		Main.road[road].right.block[block].setCarAmt(-1);
    		Main.road[road].right.block[block].setCarAmtLengthMLane(-getRadius());
    	}
    	else if (d == 'S'){
    		Main.road[road].left.block[block].setCarAmt(-1);
    		Main.road[road].left.block[block].setCarAmtLengthMLane(-getRadius());
    	}
    	else if (d == 'W'){
    		Main.road[road].left.block[block].setCarAmt(-1);
    		Main.road[road].left.block[block].setCarAmtLengthMLane(-getRadius());
    	}
    	else if (d == 'E'){
    		Main.road[road].right.block[block].setCarAmt(-1);
    		Main.road[road].right.block[block].setCarAmtLengthMLane(-getRadius());
    	}
    	Main.curAvg++;
    	isAlive = false;
    	setX(-100);
    	setY(-100);
    	
    	if (getType() == "Citizen")
    		Main.citizenAmt--;
    	else if (getType() == "Bus")
    		Main.busAmt--;
    	else if (getType() == "Cop")
    		Main.copAmt--;
    	else if (getType() == "Fire Truck")
    		Main.firetruckAmt--;
    	else if (getType() == "Ambulance")
    		Main.ambulanceAmt--;
    }

    public void speedUp(){
    	if (vx<maxV)
    		vx+=accelRate;
    	if (vy<maxV)
    		vy+=accelRate;
    }
    
    public void slowDown(){
    	if (vx>0)
    		vx-=brakeRate;
    	if (vy>0)
    		vy-=brakeRate;
    	if (vx<0)
    		vx=0;
    	if (vy<0)
    		vy=0;
    }
    
    public void resetVel(double add){
        setMaxVelocity(((0.612 * Math.random()) + 0.31));
    	vx     = maxV;
        vy     = maxV;
    }
    
    public void resetTrip(){
    	if (tripTime>0)
    		setPrevProportionalStopTime((int) ((stopTime/tripTime)*100));
    	tripTime = 0;
    	stopTime = 0;
    	tripTimer.cancel();
    	tripTimer = new Timer();
    	TripSimulation();
    }
    
    public int getMinCarGap() {
		return minCarGap;
	}

	public void setMinCarGap(int minCarGap) {
		this.minCarGap = minCarGap;
	}

	public int getCarBehind() {
		return carBehind;
	}

	public void setCarBehind(int carBehind) {
		this.carBehind = carBehind;
	}

	public int getPeriod() { return period; }

	public void setPeriod(int period) { this.period = period; }

	public double getCarGap() { return carGap; }

	public void setCarGap(double carGap) { this.carGap = carGap; }
	
    public int getNextLight() { return nextLight; }

	public void setNextLight(int nextLight) { this.nextLight = nextLight; }
	
	public String getType() { return type; }

	public void setType(String type) { this.type = type; }

	public int getWeight() { return weight; }

	public void setWeight(int weight) { this.weight = weight; }
	
	public boolean isIcon() { return isIcon; }

	public void setIcon(boolean isIcon) { this.isIcon = isIcon; }
	
	public double getAccelRate() { return accelRate; }

	public void setAccelRate(double accelRate) { this.accelRate = accelRate; }

	public double getBrakeRate() { return brakeRate; }

	public void setBrakeRate(double brakeRate) { this.brakeRate = brakeRate; }
	
	public double getW() { return w; }

	public void setW(double w) { this.w = w; }

	public int getLightGap() { return lightGap; }

	public void setLightGap(int lightGap) { this.lightGap = lightGap; }

	public double getH() { return h; }

	public void setH(double h) { this.h = h; }
	
    public int getPrevProportionalStopTime() { return prevProportionalStopTime; }

	public void setPrevProportionalStopTime(int prevProportionalStopTime) { this.prevProportionalStopTime = prevProportionalStopTime; }

	public double getStopTime() { return stopTime; }

	public void setStopTime(int stopTime) { this.stopTime = stopTime; }

	public double getTripTime() { return tripTime; }

	public void setTripTime(int tripTime) { this.tripTime = tripTime; }
	
    public void setColor(Color newColor){ color = newColor; }
    
    public void setStartPos(double X, double Y){ startX = X; startY = Y; }
    
    public void setMaxVelocity(double max){ maxV = max; }
    
    public int getIndex() { return index; }

	public void setIndex(int index) { this.index = index; }
	
    public double getStartX() { return startX; }

	public void setStartX(double startX) { this.startX = startX; }

	public double getStartY() { return startY; }

	public void setStartY(double startY) { this.startY = startY; }

	public double getDestX() { return destX; }

	public void setDestX(double destX) { this.destX = destX; }

	public double getDestY() { return destY; }

	public void setDestY(double destY) { this.destY = destY; }

	public boolean isAlive() { return isAlive; }

	public void setAlive(boolean isAlive) { this.isAlive = isAlive; }

	public int getCarAhead() { return carAhead; }

	public void setCarAhead(int carAhead) { this.carAhead = carAhead; }

	public int getRoad() { return road; }

	public void setRoad(int road) { this.road = road; }

	public int getBlock() { return block; }

	public void setBlock(int block) { this.block = block; }

	public char getDirection() { return direction; }

	public void setDirection(char direction) { this.direction = direction; }

	public char getRlLane() { return rlLane; }

	public void setRlLane(char rlLane) { this.rlLane = rlLane; }

	public char getRlsLane() { return rlsLane;}

	public void setrlsLane(char rlsLane) { this.rlsLane = rlsLane;}
	
    public double getX() { return x; }
    
    public double getY() { return y; }
    
    public void setX(double X) { x = X; }
    
    public void setY(double Y) { y = Y; }
    
    public double getVel() { return vx+vy; }
    
    public int getRadius() { return radius; }

    public void setRadius(int Radius){ radius = Radius; }
}
