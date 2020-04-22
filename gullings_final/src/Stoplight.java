import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class Stoplight {
	private final int startYellowTime = 4;
    private final int startGreenTime = 25;
    
    private double x, y; 
    
    public int vertRoad, horzRoad, blockN, blockS, blockW, blockE;
    private int lightN, lightS, lightW, lightE;
	public int yellowTime;
    public int greenTime;
    public int incomingTrafficVert;
    public int incomingTrafficHorz;
    public int crossTrafficNorth, crossTrafficSouth, crossTrafficEast, crossTrafficWest;
    int interval = 1;     
    int delay = 1000;
    int period = 1000; 
    
    private Color color = new Color(0,0,0);
    public lightState State = lightState.VERT_GREEN;
    
    Timer timer;
	Timer timerXtra;
	
    public Stoplight() {
    	timer = new Timer();
    	timerXtra = new Timer();
    	
    	resetGreenTime();
		GreenTimer();
    }

    public void draw() {
    	StdDraw.setPenColor(color);
        StdDraw.filledSquare(x, y, 2.5);
        
        if (State==null){
        	StdDraw.setPenColor(Color.GRAY);
        	StdDraw.filledCircle(x, y, .6);
        	StdDraw.filledCircle(x-.9, y, .6);
        	StdDraw.filledCircle(x+.9, y, .6);
        	StdDraw.filledCircle(x, y+.9, .6);
        	StdDraw.filledCircle(x, y-.9, .6);
        }
        else if (State==lightState.VERT_GREEN){
        	StdDraw.setPenColor(Color.GRAY);
        	StdDraw.filledCircle(x, y, .6);
        	StdDraw.setPenColor(Color.RED);
        	StdDraw.filledCircle(x-1.3, y, .6);
        	StdDraw.filledCircle(x+1.3, y, .6);
        	StdDraw.setPenColor(Color.GREEN);
        	StdDraw.filledCircle(x, y+1.3, .6);
        	StdDraw.filledCircle(x, y-1.3, .6);
        }
        else if (State==lightState.VERT_RED){
        	StdDraw.setPenColor(Color.GRAY);
        	StdDraw.filledCircle(x, y, .6);
        	StdDraw.setPenColor(Color.GREEN);
        	StdDraw.filledCircle(x-1.3, y, .6);
        	StdDraw.filledCircle(x+1.3, y, .6);
        	StdDraw.setPenColor(Color.RED);
        	StdDraw.filledCircle(x, y+1.3, .6);
        	StdDraw.filledCircle(x, y-1.3, .6);
        }
        else if (State==lightState.VERT_YELLOW){
        	StdDraw.setPenColor(Color.YELLOW);
        	StdDraw.filledCircle(x, y, .6);
        	StdDraw.setPenColor(Color.RED);
        	StdDraw.filledCircle(x-1.3, y, .6);
        	StdDraw.filledCircle(x+1.3, y, .6);
        	StdDraw.setPenColor(Color.GRAY);
        	StdDraw.filledCircle(x, y+1.3, .6);
        	StdDraw.filledCircle(x, y-1.3, .6);
        }
        else if (State==lightState.HORZ_YELLOW){
        	StdDraw.setPenColor(Color.YELLOW);
        	StdDraw.filledCircle(x, y, .6);
        	StdDraw.setPenColor(Color.GRAY);
        	StdDraw.filledCircle(x-1.3, y, .6);
        	StdDraw.filledCircle(x+1.3, y, .6);
        	StdDraw.setPenColor(Color.RED);
        	StdDraw.filledCircle(x, y+1.3, .6);
        	StdDraw.filledCircle(x, y-1.3, .6);
        }
    }
    
    public void YellowTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if (yellowTime<=0){
            		if (State==lightState.VERT_YELLOW)
            			State = lightState.VERT_RED;
            		else if (State==lightState.HORZ_YELLOW)
            			State = lightState.VERT_GREEN;
            		resetGreenTime();
            		GreenTimer();
                	timer.cancel();
                	timer = new Timer();
                	return;
            	}
                yellowTime--;
            }
        }, delay, period);
    }
    
    public void smartStoplight(){
    	if (State == lightState.VERT_RED){
			if (greenTime>startGreenTime/3)
				if ((Main.road[horzRoad].left.block[blockE].getCarAmt() >= 25) || (Main.road[horzRoad].right.block[blockW].getCarAmt() >= 25))
					return;
			if (greenTime < 0)
        		if (incomingTrafficVert > 0){
        			State = lightState.HORZ_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
                	return;
        		}
			if ((incomingTrafficHorz == 0) && (incomingTrafficVert > 0)){
    			State = lightState.HORZ_YELLOW;
    			resetYellowTime();
        		YellowTimer();
        		timerXtra.cancel();
        		timerXtra = new Timer();
        		return;
    		}
			if (greenTime <= (startGreenTime * .6))
        		if ((incomingTrafficVert -3) > incomingTrafficHorz){
        			State = lightState.HORZ_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
            		return;
        		}
			if (greenTime <= (startGreenTime * .9))
        		if ((incomingTrafficVert - 8) > incomingTrafficHorz){
        			State = lightState.HORZ_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
            		return;
        		}
		}
    	
		if (State == lightState.VERT_GREEN){
			if (greenTime > startGreenTime / 3)
				if ((Main.road[vertRoad].left.block[blockN].getCarAmt() >= 30) || (Main.road[vertRoad].right.block[blockS].getCarAmt() >= 30))
        			return;
			if (greenTime < 0)
        		if (incomingTrafficHorz > 0){
        			State = lightState.VERT_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
                	return;
        		}
        	if ((incomingTrafficVert == 0) && (incomingTrafficHorz > 0)){
        		State = lightState.VERT_YELLOW;
        		resetYellowTime();
            	YellowTimer();
            	timerXtra.cancel();
            	timerXtra = new Timer();
            	return;
        	}
			if (greenTime <= (startGreenTime * .6))
        		if ((incomingTrafficVert + 3) < incomingTrafficHorz){
        			State = lightState.VERT_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
            		return;
        		}
        	if (greenTime <= (startGreenTime * .9))
        		if ((incomingTrafficVert + 8) < incomingTrafficHorz){
        			State = lightState.VERT_YELLOW;
        			resetYellowTime();
            		YellowTimer();
            		timerXtra.cancel();
            		timerXtra = new Timer();
            		return;
        		}
		}
    }
    
    public void intervalStoplight(){
    	if (greenTime <= 0){
    		if (State == lightState.VERT_GREEN){
    			State = lightState.VERT_YELLOW;
    			resetYellowTime();
        		YellowTimer();
        		timerXtra.cancel();
        		timerXtra = new Timer();
            	return;
    		}
    		else if (State == lightState.VERT_RED){
    			State = lightState.HORZ_YELLOW;
    			resetYellowTime();
        		YellowTimer();
        		timerXtra.cancel();
        		timerXtra = new Timer();
            	return;
    		}
    	}
    }
 
    public void GreenTimer(){
        timerXtra.scheduleAtFixedRate(new TimerTask() {
            public void run() { greenTime--;
            	
            	if (Main.autoSmartStoplight)
            		smartStoplight();
            	else if (Main.autoIntervalStoplight)
            		intervalStoplight();
            }
        }, delay, period);
    }
    
    public int getPeriod() { return period; }

	public void setPeriod(int period) { this.period = period; }

	public int getCrossTrafficNorth() { return crossTrafficNorth; }

	public void setCrossTrafficNorth(int crossTrafficNorth) { this.crossTrafficNorth = crossTrafficNorth; }

	public int getLightN() { return lightN; }

	public void setLightN(int lightN) { this.lightN = lightN; }

	public int getLightS() { return lightS; }

	public void setLightS(int lightS) { this.lightS = lightS; }

	public int getLightW() { return lightW; }

	public void setLightW(int lightW) { this.lightW = lightW; }

	public int getLightE() { return lightE; }

	public void setLightE(int lightE) { this.lightE = lightE; }

	public int getCrossTrafficSouth() { return crossTrafficSouth; }

	public void setCrossTrafficSouth(int crossTrafficSouth) { this.crossTrafficSouth = crossTrafficSouth; }

	public int getCrossTrafficEast() { return crossTrafficEast; }

	public void setCrossTrafficEast(int crossTrafficEast) { this.crossTrafficEast = crossTrafficEast; }

	public int getCrossTrafficWest() { return crossTrafficWest; }

	public void setCrossTrafficWest(int crossTrafficWest) { this.crossTrafficWest = crossTrafficWest; }
	
    public void resetYellowTime(){ yellowTime = startYellowTime; }
    
    public void resetGreenTime(){ greenTime = startGreenTime; }
    
    public void setPos(double X, double Y) { x = X; y = Y; }
    
    public void setColor(Color newColor){ color = newColor; }
    
    public int getVertRoad() { return vertRoad; }

	public void setVertRoad(int vertRoad) {this.vertRoad = vertRoad; }

	public int getHorzRoad() { return horzRoad; }

	public void setHorzRoad(int horzRoad) {this.horzRoad = horzRoad; }

	public int getBlockN() { return blockN; }

	public void setBlockN(int blockN) { this.blockN = blockN; }

	public int getBlockS() { return blockS; }

	public void setBlockS(int blockS) { this.blockS = blockS; }

	public int getBlockW() { return blockW; }

	public void setBlockW(int blockW) { this.blockW = blockW; }

	public int getBlockE() { return blockE; }

	public void setBlockE(int blockE) { this.blockE = blockE; }
	
    public double getX() { return x; }
    
    public double getY() { return y; }
}