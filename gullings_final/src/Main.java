import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Main {
	static final int carCapMax = 2000;
	static final int blockCarCap = 30;
	
	static Road[] road = new Road[14];
	static Stoplight[] stoplight = new Stoplight[50];
	static Car[] car = new Car[carCapMax];
	static Car[] carIcon = new Car[5];
	static int[] simAvg = new int[8];
	
	static int animationSpeed = 0;
	static int interval = 1;
	static int delay = 3000;
    static int period = 50;
    static int periodX = 1000;
    static int simTime, avgIndex, simNum, totalAvg, curAvg, bestAvg;
    static int carAmt;
    static int ProportionalStopTime;
    static int validPropStopTimeCarAmt;
    static int citizenAmt;
    static int busAmt;
    static int copAmt;
    static int firetruckAmt;
    static int ambulanceAmt;
    
    static boolean Resume = true;
	static boolean blockOverlay;
    static boolean autoSmartStoplight = true;
    static boolean autoIntervalStoplight = false;
    static boolean manualStoplight = false;
    static boolean carReset = true;
    
    static Timer timer, timerX;
    static Random rndNum = new Random();
    static String viewType = "All";

	public static void startSimulation() {
		StdDraw.clear();
		
		StdDraw.setPenRadius(.005);
		
		for (int i=0;i<road.length;i++)
			road[i].draw();
		
		for (int i=0;i<stoplight.length-1;i++){
			stoplight[i].incomingTrafficVert = 0;
			stoplight[i].incomingTrafficHorz = 0;
			stoplight[i].crossTrafficNorth = 0;
			stoplight[i].crossTrafficSouth = 0;
			stoplight[i].crossTrafficEast = 0;
			stoplight[i].crossTrafficWest = 0;
		}
		
		carAmt = 0;
		ProportionalStopTime = 0;
		validPropStopTimeCarAmt = 0;
		for (int i=1;i<carCapMax;i++){
			if (car[i].isAlive()){
				car[i].move();
				car[i].draw();
				carAmt++;
				if (car[i].getPrevProportionalStopTime()>0){
					ProportionalStopTime += car[i].getPrevProportionalStopTime();
					validPropStopTimeCarAmt++;
				}
			}
		}
		
		if (validPropStopTimeCarAmt>0)
			ProportionalStopTime /= validPropStopTimeCarAmt;
		
		for (int i = 0;i<carIcon.length;i++)
			carIcon[i].draw();
		
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.text(10, 252, "max ["+carCapMax+"]");
		StdDraw.text(10, 248, "car amt ["+carAmt+"]");
		
		StdDraw.text(10, 240, "time: "+simTime);
		StdDraw.text(10, 236, "cars through: "+curAvg);
		StdDraw.text(10, 232, "avg [best]: "+totalAvg+" ["+bestAvg+"]");
		StdDraw.text(10, 228, "avg stop time %: "+ProportionalStopTime);
		
		StdDraw.textRight(248, 252, citizenAmt+" citizens ");
		StdDraw.textRight(248, 248, busAmt+" buses ");
		StdDraw.textRight(248, 244, copAmt+" cops ");
		StdDraw.textRight(248, 240, firetruckAmt+" fire trucks ");
		StdDraw.textRight(248, 236, ambulanceAmt+" ambulances ");
		StdDraw.textRight(248, 232, "view type ["+viewType+"]");
		
		if (autoSmartStoplight)
			StdDraw.textRight(248, 228, "stoplights [smart]");
		else if (autoIntervalStoplight)
			StdDraw.textRight(248, 228, "stoplights [interval]");
		else if (manualStoplight)
			StdDraw.textRight(248, 228, "stoplights [manual]");
		
		for (int i=0;i<stoplight.length-1;i++){
			if (StdDraw.mousePressed())	
				if (manualStoplight)
					if ((StdDraw.mouseX()+3>stoplight[i].getX())&&(StdDraw.mouseX()-3<stoplight[i].getX()))
						if ((StdDraw.mouseY()+3>stoplight[i].getY()+5)&&(StdDraw.mouseY()-3<stoplight[i].getY()+5))
							changeStoplight(i);

			stoplight[i].draw();
		}
		
		StdDraw.show(animationSpeed);
		
	}
	
	public static void changeStoplight(int s){
		if ((stoplight[s].State==lightState.VERT_YELLOW)||(stoplight[s].State==lightState.HORZ_YELLOW))
			return;
		else if (stoplight[s].State==lightState.VERT_GREEN)
			stoplight[s].State = lightState.VERT_YELLOW;
		else if (stoplight[s].State==lightState.VERT_RED)
			stoplight[s].State = lightState.HORZ_YELLOW;
		
		stoplight[s].resetYellowTime();
		stoplight[s].YellowTimer();
	}
	
	public static void simulationTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

    			int i = 0;
    			do { i++; } while ((car[i].isAlive())&&(i<carCapMax-1));
    			int ii = i;
   
    			if (ii>=carCapMax-1)
    				return;
    			if (!carReset)
    				return;
    			
    			car[ii].setAlive(true);

    			int type = rndNum.nextInt(200);
    			
    			if (type<=165){
    				car[ii].setType("Citizen");
    				car[ii].setWeight(1);
    				car[ii].setRadius(1);
    				car[ii].setColor(Color.WHITE);
    				car[ii].resetVel(.0001);
    				car[ii].setAccelRate(.001);
    				car[ii].setBrakeRate(.001);
    				car[ii].setCarGap(1);
    				car[ii].setLightGap(4);
    				citizenAmt++;
    			}
    			if ((type>=166)&&(type<=168)){
    				car[ii].setType("Cop");
    				car[ii].setWeight(6);
    				car[ii].setRadius(1);
    				car[ii].setColor(Color.BLUE);
    				car[ii].resetVel(.015);
    				car[ii].setAccelRate(.01);
    				car[ii].setBrakeRate(.01);
    				car[ii].setCarGap(1);
    				car[ii].setLightGap(4);
    				copAmt++;
    			}
    			if (type==169){
    				car[ii].setType("Fire Truck");
    				car[ii].setWeight(10);
    				car[ii].setRadius(1);
    				car[ii].setColor(Color.RED);
    				car[ii].resetVel(.01);
    				car[ii].setAccelRate(.005);
    				car[ii].setBrakeRate(.005);
    				car[ii].setCarGap(2);
    				car[ii].setLightGap(5);
    				firetruckAmt++;
    			}
    			if (type==170){
    				car[ii].setType("Ambulance");
    				car[ii].setWeight(10);
    				car[ii].setRadius(1);
    				car[ii].setColor(Color.CYAN);
    				car[ii].resetVel(.02);
    				car[ii].setAccelRate(.02);
    				car[ii].setBrakeRate(.02);
    				car[ii].setCarGap(1);
    				car[ii].setLightGap(4);
    				ambulanceAmt++;
    			}
    			if (type>=171){
    				car[ii].setType("Bus");
    				car[ii].setWeight(3);
    				car[ii].setRadius(1);
    				car[ii].setColor(Color.YELLOW);
    				car[ii].resetVel(-.005);
    				car[ii].setAccelRate(.0007);
    				car[ii].setBrakeRate(.0007);
    				car[ii].setCarGap(2);
    				car[ii].setLightGap(5);
    				busAmt++;
    			}
    			
    			car[ii].resetTrip();
    			
    			car[ii].setRoad(rndNum.nextInt(14));
    			
    			int LeftOrRight = rndNum.nextInt(2);

    			if (LeftOrRight==0)
    				car[ii].setBlock(0);
    			else
    				car[ii].setBlock(7);
    			
    			if ((car[ii].getRoad()<7)&&(car[ii].getBlock()==7)){
    				car[ii].setDirection(road[car[ii].getRoad()].left.getDirection());
    				car[ii].setX(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getX1());
    				car[ii].setY(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getY1());
    				road[car[ii].getRoad()].left.block[car[ii].getBlock()].setCarAmt(1);
        			car[ii].setCarAhead(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getLastCar());
        			road[car[ii].getRoad()].left.block[car[ii].getBlock()].setLastCar(ii);
    			}
    			if ((car[ii].getRoad()<7)&&(car[ii].getBlock()==0)){
    				car[ii].setDirection(road[car[ii].getRoad()].right.getDirection());
    				car[ii].setX(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getX1());
    				car[ii].setY(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getY0());
    				road[car[ii].getRoad()].right.block[car[ii].getBlock()].setCarAmt(1);
        			car[ii].setCarAhead(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getLastCar());
        			road[car[ii].getRoad()].right.block[car[ii].getBlock()].setLastCar(ii);
    			}
    			if ((car[ii].getRoad()>=7)&&(car[ii].getBlock()==7)){
    				car[ii].setDirection(road[car[ii].getRoad()].left.getDirection());
    				car[ii].setX(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getX1());
    				car[ii].setY(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getY1());
    				road[car[ii].getRoad()].left.block[car[ii].getBlock()].setCarAmt(1);
        			car[ii].setCarAhead(road[car[ii].getRoad()].left.block[car[ii].getBlock()].getLastCar());
        			road[car[ii].getRoad()].left.block[car[ii].getBlock()].setLastCar(ii);
    				
    			}
    			if ((car[ii].getRoad()>=7)&&(car[ii].getBlock()==0)){
    				car[ii].setDirection(road[car[ii].getRoad()].right.getDirection());
    				car[ii].setX(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getX0());
    				car[ii].setY(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getY0());
    				road[car[ii].getRoad()].right.block[car[ii].getBlock()].setCarAmt(1);
        			car[ii].setCarAhead(road[car[ii].getRoad()].right.block[car[ii].getBlock()].getLastCar());
        			road[car[ii].getRoad()].right.block[car[ii].getBlock()].setLastCar(ii);
    			}
    			
    			LeftOrRight = rndNum.nextInt(2);
    			if (LeftOrRight==0){
    				car[ii].setDestX(road[rndNum.nextInt(13)].getX0()); // (carCap-1)+7
        			car[ii].setDestY(road[rndNum.nextInt(13)].getY0());
    			}else{
    				car[ii].setDestX(road[rndNum.nextInt(13)].getX1());
    				car[ii].setDestY(road[rndNum.nextInt(13)].getY1());
    			}
    			
    			if (car[ii].getDirection() == 'W'){
    				if (car[ii].getRoad()==7)
    					car[ii].setNextLight(6);
    				else if (car[ii].getRoad()==8)
    					car[ii].setNextLight(13);
    				else if (car[ii].getRoad()==9)
    					car[ii].setNextLight(20);
    				else if (car[ii].getRoad()==10)
    					car[ii].setNextLight(27);
    				else if (car[ii].getRoad()==11)
    					car[ii].setNextLight(34);
    				else if (car[ii].getRoad()==12)
    					car[ii].setNextLight(41);
    				else if (car[ii].getRoad()==13)
    					car[ii].setNextLight(48);
    				
    				if (Main.car[ii].getX()-65>Main.car[ii].getDestX())
    					Main.car[ii].setrlsLane('S');
    				else if ((Main.car[ii].getX()-35>Main.car[ii].getDestX())&&(Main.car[ii].getDestX()!=0))
    					Main.car[ii].setrlsLane('S');
    				else
    					if (Main.car[ii].getY()>Main.car[ii].getDestY())
    						Main.car[ii].setrlsLane('L');
    					else
    						Main.car[ii].setrlsLane('R');
    				if ((Main.car[ii].getY()+10<Main.car[ii].getDestY())&&(Main.car[ii].getY()-10>Main.car[ii].getDestY()))
    					Main.car[ii].setrlsLane('S');
    			}
    			if (car[ii].getDirection() == 'E'){
    				if (car[ii].getRoad()==7)
    					car[ii].setNextLight(0);
    				else if (car[ii].getRoad()==8)
    					car[ii].setNextLight(7);
    				else if (car[ii].getRoad()==9)
    					car[ii].setNextLight(14);
    				else if (car[ii].getRoad()==10)
    					car[ii].setNextLight(21);
    				else if (car[ii].getRoad()==11)
    					car[ii].setNextLight(28);
    				else if (car[ii].getRoad()==12)
    					car[ii].setNextLight(35);
    				else if (car[ii].getRoad()==13)
    					car[ii].setNextLight(42);
    				
    				if (Main.car[ii].getX()+65<Main.car[ii].getDestX())
    					Main.car[ii].setrlsLane('S');
    				if ((Main.car[ii].getX()+35<Main.car[ii].getDestX())&&(Main.car[ii].getDestX()!=250))
    					Main.car[ii].setrlsLane('S');
    				else
    					if (Main.car[ii].getY()>Main.car[ii].getDestY())
    						Main.car[ii].setrlsLane('R');
    					else
    						Main.car[ii].setrlsLane('L');
    			if ((Main.car[ii].getY()+10<Main.car[ii].getDestY())&&(Main.car[ii].getY()-10>Main.car[ii].getDestY()))
    				Main.car[ii].setrlsLane('S');
    			}
    			if (car[ii].getDirection() == 'N'){
    				if (car[ii].getRoad()==0)
    					car[ii].setNextLight(0);
    				else if (car[ii].getRoad()==1)
    					car[ii].setNextLight(1);
    				else if (car[ii].getRoad()==2)
    					car[ii].setNextLight(2);
    				else if (car[ii].getRoad()==3)
    					car[ii].setNextLight(3);
    				else if (car[ii].getRoad()==4)
    					car[ii].setNextLight(4);
    				else if (car[ii].getRoad()==5)
    					car[ii].setNextLight(5);
    				else if (car[ii].getRoad()==6)
    					car[ii].setNextLight(6);
    				
    				if (Main.car[ii].getY()+65<Main.car[ii].getDestY())
    					Main.car[ii].setrlsLane('S');
    				else if ((Main.car[ii].getY()+35<Main.car[ii].getDestY())&&(Main.car[ii].getDestY()!=250))
    					Main.car[ii].setrlsLane('S');
    				else
    					if (Main.car[ii].getX()>Main.car[ii].getDestX())
    						Main.car[ii].setrlsLane('L');
    					else
    						Main.car[ii].setrlsLane('R');
    				if ((Main.car[ii].getX()+10<Main.car[ii].getDestX())&&(Main.car[ii].getX()-10>Main.car[ii].getDestX()))
    					Main.car[ii].setrlsLane('S');
    			}
    			if (car[ii].getDirection() == 'S'){
    				if (car[ii].getRoad()==0)
    					car[ii].setNextLight(42);
    				else if (car[ii].getRoad()==1)
    					car[ii].setNextLight(43);
    				else if (car[ii].getRoad()==2)
    					car[ii].setNextLight(44);
    				else if (car[ii].getRoad()==3)
    					car[ii].setNextLight(45);
    				else if (car[ii].getRoad()==4)
    					car[ii].setNextLight(46);
    				else if (car[ii].getRoad()==5)
    					car[ii].setNextLight(47);
    				else if (car[ii].getRoad()==6)
    					car[ii].setNextLight(48);
    				
    				if (Main.car[ii].getY()-65>Main.car[ii].getDestY())
    					Main.car[ii].setrlsLane('S');
    				else if ((Main.car[ii].getY()-35>Main.car[ii].getDestY())&&(Main.car[ii].getDestY()!=0))
    					Main.car[ii].setrlsLane('S');
    				else
    					if (Main.car[ii].getX()>Main.car[ii].getDestX())
    						Main.car[ii].setrlsLane('R');
    					else
    						Main.car[ii].setrlsLane('L');
    				if ((Main.car[ii].getX()+10<Main.car[ii].getDestX())&&(Main.car[ii].getX()-10>Main.car[ii].getDestX()))
    					Main.car[ii].setrlsLane('S');
    			}
    			
    			if ((car[ii].getDirection() == 'N')||(car[ii].getDirection() == 'S')){
    				if (car[ii].getType()=="Citizen"){
    					car[ii].setW(.3);
            			car[ii].setH(.3);	
    				}
    				else if (car[ii].getType()=="Bus"){
    					car[ii].setW(.45);
            			car[ii].setH(1);	
    				}
    				else if (car[ii].getType()=="Cop"){
    					car[ii].setW(.35);
            			car[ii].setH(.35);	
    				}
    				else if (car[ii].getType()=="Fire Truck"){
    					car[ii].setW(.5);
            			car[ii].setH(1);	
    				}
    				else if (car[ii].getType()=="Ambulance"){
    					car[ii].setW(.4);
            			car[ii].setH(.55);	
    				}
    				car[ii].setCarGap(car[ii].getMinCarGap() + car[ii].getH() + car[car[ii].getCarAhead()].getH());
    			}
    			else if ((car[ii].getDirection() == 'W')||(car[ii].getDirection() == 'E')){
    				if (car[ii].getType()=="Citizen"){
    					car[ii].setW(.3);
            			car[ii].setH(.3);	
    				}
    				else if (car[ii].getType()=="Bus"){
    					car[ii].setW(1);
            			car[ii].setH(.45);	
    				}
    				else if (car[ii].getType()=="Cop"){
    					car[ii].setW(.35);
            			car[ii].setH(.35);	
    				}
    				else if (car[ii].getType()=="Fire Truck"){
    					car[ii].setW(1);
            			car[ii].setH(.5);	
    				}
    				else if (car[ii].getType()=="Ambulance"){
    					car[ii].setW(.55);
            			car[ii].setH(.4);	
    				}
    				car[ii].setCarGap(car[ii].getMinCarGap() + car[ii].getW() + car[car[ii].getCarAhead()].getW());
    			}
                return;
            }
         
        }, delay, period);
    }
	
	public static void perMinTimer(){
        timerX.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if (simTime==60){

            		if (simNum<8)
            			simNum++;
            		
            		simAvg[avgIndex] = curAvg;
            		
            		if (avgIndex<7)
            			avgIndex++;
            		else
            			avgIndex = 0;
            		totalAvg = 0;
            		for (int i = 0; i < simNum; i++){
            			totalAvg += simAvg[i]; 
            		}
            		
            		totalAvg = totalAvg / simNum;
            		if (curAvg>bestAvg)
            			bestAvg = curAvg;
            		simTime = 0;
            		curAvg = 0;
            		
                	return;
            	}
            	simTime++;
            }
        }, delay, periodX);
    }
	
	public static void resetSimulation(){
		totalAvg=0;
		curAvg = 0;
		simTime = 0;
		simNum = 0;
		bestAvg = 0;
		avgIndex = 0;
		
		citizenAmt = 0;
		busAmt = 0;
		copAmt = 0;
		firetruckAmt = 0;
		ambulanceAmt = 0;
		
		for (int i = 1; i < car.length; i++){
			car[i].setAlive(false);
			car[i].setX(-100);
			car[i].setY(-100);
			car[i].setPrevProportionalStopTime(0);
		}
		for (int i = 0; i < road.length; i++){
			for (int ii = 0; ii < 8; ii++){
				road[i].right.block[ii].left.setCarAmt(0);
				road[i].right.block[ii].right.setCarAmt(0);
				road[i].right.block[ii].setCarAmt(0);
				road[i].left.block[ii].left.setCarAmt(0);
				road[i].left.block[ii].right.setCarAmt(0);
				road[i].left.block[ii].setCarAmt(0);
			}
		}
	}
	
	public static void checkboxOverlay(boolean state){
		blockOverlay = state;
	}
	
	public static void checkboxResettingCars(boolean state){
		carReset = state;
	}

	public static void changeStoplights(String state){
		autoSmartStoplight = false;
		autoIntervalStoplight = false;
		manualStoplight = false;
		
		if (state == "Auto Smart")
			autoSmartStoplight = true;
		else if (state == "Auto Interval")
			autoIntervalStoplight = true;
		else
			manualStoplight = true;
	}

	public static void changeViewType(String type){
		viewType = type;
	}
	
	public static void testMe(){
		for (int i = 0; i < road.length; i++){
			road[i] = new Road();
			
			for (int ii = 0; ii < 8; ii++){
				road[i].right.block[ii] = new Block();
				road[i].right.setRl('R');
				road[i].left.block[ii] = new Block();
				road[i].left.setRl('L');
			}
		}

		for (int i = 0; i < car.length; i++){
			car[i] = new Car();
			car[i].setIndex(i);	
		}

		for (int i = 0; i < carIcon.length; i++){
			carIcon[i] = new Car();
			carIcon[i].setIcon(true);
			carIcon[i].setX(250);
		}
		
		carIcon[0].setY(252);
		carIcon[0].setColor(Color.WHITE);
		carIcon[0].setW(.3);
		carIcon[0].setH(.3);
		carIcon[1].setY(248);
		carIcon[1].setColor(Color.YELLOW);
		carIcon[1].setW(1);
		carIcon[1].setH(.45);
		carIcon[2].setY(244);
		carIcon[2].setColor(Color.BLUE);
		carIcon[2].setW(.35);
		carIcon[2].setH(.35);
		carIcon[3].setY(240);
		carIcon[3].setColor(Color.RED);
		carIcon[3].setW(1);
		carIcon[3].setH(.5);
		carIcon[4].setY(236);
		carIcon[4].setColor(Color.CYAN);
		carIcon[4].setW(.55);
		carIcon[4].setH(.4);
		
		road[0].setPosVert(31, 0, 31, 250);
		road[0].right.setup(road[0].getX0()+1, road[0].getY0(), road[0].getX1()+1, road[0].getY1(), Color.DARK_GRAY, 'N');
		road[0].left.setup(road[0].getX0()-1, road[0].getY0(), road[0].getX1()-1, road[0].getY1(), Color.DARK_GRAY, 'S');
		
		road[1].setPosVert(62, 0, 62, 250);
		road[1].right.setup(road[1].getX0()+1, road[1].getY0(), road[1].getX1()+1, road[1].getY1(), Color.DARK_GRAY, 'N');
		road[1].left.setup(road[1].getX0()-1, road[1].getY0(), road[1].getX1()-1, road[1].getY1(), Color.DARK_GRAY, 'S');
		
		road[2].setPosVert(93, 0, 93, 250);
		road[2].right.setup(road[2].getX0()+1, road[2].getY0(), road[2].getX1()+1, road[2].getY1(), Color.DARK_GRAY, 'N');
		road[2].left.setup(road[2].getX0()-1, road[2].getY0(), road[2].getX1()-1, road[2].getY1(), Color.DARK_GRAY, 'S');
		
		road[3].setPosVert(124, 0, 124, 250);
		road[3].right.setup(road[3].getX0()+1, road[3].getY0(), road[3].getX1()+1, road[3].getY1(), Color.DARK_GRAY, 'N');
		road[3].left.setup(road[3].getX0()-1, road[3].getY0(), road[3].getX1()-1, road[3].getY1(), Color.DARK_GRAY, 'S');
		
		road[4].setPosVert(155, 0, 155, 250);
		road[4].right.setup(road[4].getX0()+1, road[4].getY0(), road[4].getX1()+1, road[4].getY1(), Color.DARK_GRAY, 'N');
		road[4].left.setup(road[4].getX0()-1, road[4].getY0(), road[4].getX1()-1, road[4].getY1(), Color.DARK_GRAY, 'S');
		
		road[5].setPosVert(186, 0, 186, 250);
		road[5].right.setup(road[5].getX0()+1, road[5].getY0(), road[5].getX1()+1, road[5].getY1(), Color.DARK_GRAY, 'N');
		road[5].left.setup(road[5].getX0()-1, road[5].getY0(), road[5].getX1()-1, road[5].getY1(), Color.DARK_GRAY, 'S');
		
		road[6].setPosVert(217, 0, 217, 250);
		road[6].right.setup(road[6].getX0()+1, road[6].getY0(), road[6].getX1()+1, road[6].getY1(), Color.DARK_GRAY, 'N');
		road[6].left.setup(road[6].getX0()-1, road[6].getY0(), road[6].getX1()-1, road[6].getY1(), Color.DARK_GRAY, 'S');
		
		for (int i = 0;i<7;i++){
			for (int ii = 0;ii<8;ii++){
				road[i].right.block[ii].setPos(road[i].getX0()+1, ii*31.25, road[i].getX1()+1, (ii+1)*31.25);
				road[i].left.block[ii].setPos(road[i].getX0()-1, ii*31.25, road[i].getX1()-1, (ii+1)*31.25);
				
				road[i].right.block[ii].right.setPos(road[i].getX0()+2, road[i].right.block[ii].getY0()+17, road[i].getX1()+2, road[i].right.block[ii].getY1());
				road[i].right.block[ii].left.setPos(road[i].getX0(), road[i].right.block[ii].getY0()+17, road[i].getX1(), road[i].right.block[ii].getY1());

				road[i].left.block[ii].right.setPos(road[i].getX0()-2, road[i].right.block[ii].getY0(), road[i].getX1()-2, road[i].right.block[ii].getY1()-17);
				road[i].left.block[ii].left.setPos(road[i].getX0(), road[i].right.block[ii].getY0(), road[i].getX1(), road[i].right.block[ii].getY1()-17);
			}
		}
		
		// ---------------------------------
		
		road[7].setPosHorz(0, 31, 250, 31);
		road[7].right.setup(road[7].getX0(), road[7].getY0()-1, road[7].getX1(), road[7].getY1()-1, Color.DARK_GRAY, 'E');
		road[7].left.setup(road[7].getX0(), road[7].getY0()+1, road[7].getX1(), road[7].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[8].setPosHorz(0, 62, 250, 62);
		road[8].right.setup(road[8].getX0(), road[8].getY0()-1, road[8].getX1(), road[8].getY1()-1, Color.DARK_GRAY, 'E');
		road[8].left.setup(road[8].getX0(), road[8].getY0()+1, road[8].getX1(), road[8].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[9].setPosHorz(0, 93, 250, 93);
		road[9].right.setup(road[9].getX0(), road[9].getY0()-1, road[9].getX1(), road[9].getY1()-1, Color.DARK_GRAY, 'E');
		road[9].left.setup(road[9].getX0(), road[9].getY0()+1, road[9].getX1(), road[9].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[10].setPosHorz(0, 124, 250, 124);
		road[10].right.setup(road[10].getX0(), road[10].getY0()-1, road[10].getX1(), road[10].getY1()-1, Color.DARK_GRAY, 'E');
		road[10].left.setup(road[10].getX0(), road[10].getY0()+1, road[10].getX1(), road[10].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[11].setPosHorz(0, 155, 250, 155);
		road[11].right.setup(road[11].getX0(), road[11].getY0()-1, road[11].getX1(), road[11].getY1()-1, Color.DARK_GRAY, 'E');
		road[11].left.setup(road[11].getX0(), road[11].getY0()+1, road[11].getX1(), road[11].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[12].setPosHorz(0, 186, 250, 186);
		road[12].right.setup(road[12].getX0(), road[12].getY0()-1, road[12].getX1(), road[12].getY1()-1, Color.DARK_GRAY, 'E');
		road[12].left.setup(road[12].getX0(), road[12].getY0()+1, road[12].getX1(), road[12].getY1()+1, Color.DARK_GRAY, 'W');
		
		road[13].setPosHorz(0, 217, 250, 217);
		road[13].right.setup(road[13].getX0(), road[13].getY0()-1, road[13].getX1(), road[13].getY1()-1, Color.DARK_GRAY, 'E');
		road[13].left.setup(road[13].getX0(), road[13].getY0()+1, road[13].getX1(), road[13].getY1()+1, Color.DARK_GRAY, 'W');
		
		for (int i = 7;i<road.length;i++){
			for (int ii = 0;ii<8;ii++){
				road[i].right.block[ii].setPos(ii*31.25, road[i].getY0()-1, (ii+1)*31.25, road[i].getY1()-1);
				road[i].left.block[ii].setPos(ii*31.25, road[i].getY0()+1, (ii+1)*31.25, road[i].getY1()+1);
				
				road[i].right.block[ii].right.setPos(road[i].right.block[ii].getX0()+17, road[i].getY0()-2, road[i].right.block[ii].getX1(), road[i].getY1()-2);
				road[i].right.block[ii].left.setPos(road[i].right.block[ii].getX0()+17, road[i].getY0(), road[i].right.block[ii].getX1(), road[i].getY1());

				road[i].left.block[ii].right.setPos(road[i].right.block[ii].getX0(), road[i].getY0()+2, road[i].right.block[ii].getX1()-17, road[i].getY1()+2);
				road[i].left.block[ii].left.setPos(road[i].right.block[ii].getX0(), road[i].getY0(), road[i].right.block[ii].getX1()-17, road[i].getY1());
			}
		}
		
		int ii = -1, v = -1, h = 7;
		do { ii++; v++;
		stoplight[ii] = new Stoplight();
		stoplight[ii].setVertRoad(v); stoplight[ii].setHorzRoad(h);
		stoplight[ii].setPos(road[v].getX0(), road[h].getY0());
			if (v==6){
				v=-1;
				h++;
			}
			stoplight[ii].setColor(Color.DARK_GRAY);
		} while (ii<48);

		stoplight[0].setLightN(7); stoplight[0].setLightS(49);
		stoplight[0].setLightW(49); stoplight[0].setLightE(1);
		stoplight[1].setLightN(8); stoplight[1].setLightS(49);
		stoplight[1].setLightW(0); stoplight[1].setLightE(2);
		stoplight[2].setLightN(9); stoplight[2].setLightS(49);
		stoplight[2].setLightW(1); stoplight[2].setLightE(3);
		stoplight[3].setLightN(10); stoplight[3].setLightS(49);
		stoplight[3].setLightW(2); stoplight[3].setLightE(4);
		stoplight[4].setLightN(11); stoplight[4].setLightS(49);
		stoplight[4].setLightW(3); stoplight[4].setLightE(5);
		stoplight[5].setLightN(12); stoplight[5].setLightS(49);
		stoplight[5].setLightW(4); stoplight[5].setLightE(6);
		stoplight[6].setLightN(13); stoplight[6].setLightS(49);
		stoplight[6].setLightW(5); stoplight[6].setLightE(49);
		
		stoplight[7].setLightN(14); stoplight[7].setLightS(0);
		stoplight[7].setLightW(49); stoplight[7].setLightE(8);
		stoplight[8].setLightN(15); stoplight[8].setLightS(1);
		stoplight[8].setLightW(7); stoplight[8].setLightE(9);
		stoplight[9].setLightN(16); stoplight[9].setLightS(2);
		stoplight[9].setLightW(8); stoplight[9].setLightE(10);
		stoplight[10].setLightN(17); stoplight[10].setLightS(3);
		stoplight[10].setLightW(9); stoplight[10].setLightE(11);
		stoplight[11].setLightN(18); stoplight[11].setLightS(4);
		stoplight[11].setLightW(10); stoplight[11].setLightE(12);
		stoplight[12].setLightN(19); stoplight[12].setLightS(5);
		stoplight[12].setLightW(11); stoplight[12].setLightE(13);
		stoplight[13].setLightN(20); stoplight[13].setLightS(6);
		stoplight[13].setLightW(12); stoplight[13].setLightE(49);
		
		stoplight[14].setLightN(21); stoplight[14].setLightS(7);
		stoplight[14].setLightW(49); stoplight[14].setLightE(15);
		stoplight[15].setLightN(22); stoplight[15].setLightS(8);
		stoplight[15].setLightW(14); stoplight[15].setLightE(16);
		stoplight[16].setLightN(23); stoplight[16].setLightS(9);
		stoplight[16].setLightW(15); stoplight[16].setLightE(17);
		stoplight[17].setLightN(24); stoplight[17].setLightS(10);
		stoplight[17].setLightW(16); stoplight[17].setLightE(18);
		stoplight[18].setLightN(25); stoplight[18].setLightS(11);
		stoplight[18].setLightW(17); stoplight[18].setLightE(19);
		stoplight[19].setLightN(26); stoplight[19].setLightS(12);
		stoplight[19].setLightW(18); stoplight[19].setLightE(20);
		stoplight[20].setLightN(27); stoplight[20].setLightS(13);
		stoplight[20].setLightW(19); stoplight[20].setLightE(49);
		
		stoplight[21].setLightN(28); stoplight[21].setLightS(14);
		stoplight[21].setLightW(49); stoplight[21].setLightE(22);
		stoplight[22].setLightN(29); stoplight[22].setLightS(15);
		stoplight[22].setLightW(21); stoplight[22].setLightE(23);
		stoplight[23].setLightN(30); stoplight[23].setLightS(16);
		stoplight[23].setLightW(22); stoplight[23].setLightE(24);
		stoplight[24].setLightN(31); stoplight[24].setLightS(17);
		stoplight[24].setLightW(23); stoplight[24].setLightE(25);
		stoplight[25].setLightN(32); stoplight[25].setLightS(18);
		stoplight[25].setLightW(24); stoplight[25].setLightE(26);
		stoplight[26].setLightN(33); stoplight[26].setLightS(19);
		stoplight[26].setLightW(25); stoplight[26].setLightE(27);
		stoplight[27].setLightN(34); stoplight[27].setLightS(20);
		stoplight[27].setLightW(26); stoplight[27].setLightE(49);
		
		stoplight[28].setLightN(35); stoplight[28].setLightS(21);
		stoplight[28].setLightW(49); stoplight[28].setLightE(29);
		stoplight[29].setLightN(36); stoplight[29].setLightS(22);
		stoplight[29].setLightW(28); stoplight[29].setLightE(30);
		stoplight[30].setLightN(37); stoplight[30].setLightS(23);
		stoplight[30].setLightW(29); stoplight[30].setLightE(31);
		stoplight[31].setLightN(38); stoplight[31].setLightS(24);
		stoplight[31].setLightW(30); stoplight[31].setLightE(32);
		stoplight[32].setLightN(39); stoplight[32].setLightS(25);
		stoplight[32].setLightW(31); stoplight[32].setLightE(33);
		stoplight[33].setLightN(40); stoplight[33].setLightS(26);
		stoplight[33].setLightW(32); stoplight[33].setLightE(34);
		stoplight[34].setLightN(41); stoplight[34].setLightS(27);
		stoplight[34].setLightW(33); stoplight[34].setLightE(49);
		
		stoplight[35].setLightN(42); stoplight[35].setLightS(28);
		stoplight[35].setLightW(49); stoplight[35].setLightE(36);
		stoplight[36].setLightN(43); stoplight[36].setLightS(29);
		stoplight[36].setLightW(35); stoplight[36].setLightE(37);
		stoplight[37].setLightN(44); stoplight[37].setLightS(30);
		stoplight[37].setLightW(36); stoplight[37].setLightE(38);
		stoplight[38].setLightN(45); stoplight[38].setLightS(31);
		stoplight[38].setLightW(37); stoplight[38].setLightE(39);
		stoplight[39].setLightN(46); stoplight[39].setLightS(32);
		stoplight[39].setLightW(38); stoplight[39].setLightE(40);
		stoplight[40].setLightN(47); stoplight[40].setLightS(33);
		stoplight[40].setLightW(39); stoplight[40].setLightE(41);
		stoplight[41].setLightN(48); stoplight[41].setLightS(34);
		stoplight[41].setLightW(40); stoplight[41].setLightE(49);
		
		stoplight[42].setLightN(49); stoplight[42].setLightS(35);
		stoplight[42].setLightW(49); stoplight[42].setLightE(43);
		stoplight[43].setLightN(49); stoplight[43].setLightS(36);
		stoplight[43].setLightW(42); stoplight[43].setLightE(44);
		stoplight[44].setLightN(49); stoplight[44].setLightS(37);
		stoplight[44].setLightW(43); stoplight[44].setLightE(45);
		stoplight[45].setLightN(49); stoplight[45].setLightS(38);
		stoplight[45].setLightW(44); stoplight[45].setLightE(46);
		stoplight[46].setLightN(49); stoplight[46].setLightS(39);
		stoplight[46].setLightW(45); stoplight[46].setLightE(47);
		stoplight[47].setLightN(49); stoplight[47].setLightS(40);
		stoplight[47].setLightW(46); stoplight[47].setLightE(48);
		stoplight[48].setLightN(49); stoplight[48].setLightS(41);
		stoplight[48].setLightW(47); stoplight[48].setLightE(49);
		
		stoplight[0].setBlockN(1); stoplight[0].setBlockE(1);
		stoplight[0].setBlockS(0); stoplight[0].setBlockW(0);
		stoplight[1].setBlockN(1); stoplight[1].setBlockE(2);
		stoplight[1].setBlockS(0); stoplight[1].setBlockW(1);
		stoplight[2].setBlockN(1); stoplight[2].setBlockE(3);
		stoplight[2].setBlockS(0); stoplight[2].setBlockW(2);
		stoplight[3].setBlockN(1); stoplight[3].setBlockE(4);
		stoplight[3].setBlockS(0); stoplight[3].setBlockW(3);
		stoplight[4].setBlockN(1); stoplight[4].setBlockE(5);
		stoplight[4].setBlockS(0); stoplight[4].setBlockW(4);
		stoplight[5].setBlockN(1); stoplight[5].setBlockE(6);
		stoplight[5].setBlockS(0); stoplight[5].setBlockW(5);
		stoplight[6].setBlockN(1); stoplight[6].setBlockE(7);
		stoplight[6].setBlockS(0); stoplight[6].setBlockW(6);
		
		stoplight[7].setBlockN(2); stoplight[7].setBlockE(1);
		stoplight[7].setBlockS(1); stoplight[7].setBlockW(0);
		stoplight[8].setBlockN(2); stoplight[8].setBlockE(2);
		stoplight[8].setBlockS(1); stoplight[8].setBlockW(1);
		stoplight[9].setBlockN(2); stoplight[9].setBlockE(3);
		stoplight[9].setBlockS(1); stoplight[9].setBlockW(2);
		stoplight[10].setBlockN(2); stoplight[10].setBlockE(4);
		stoplight[10].setBlockS(1); stoplight[10].setBlockW(3);
		stoplight[11].setBlockN(2); stoplight[11].setBlockE(5);
		stoplight[11].setBlockS(1); stoplight[11].setBlockW(4);
		stoplight[12].setBlockN(2); stoplight[12].setBlockE(6);
		stoplight[12].setBlockS(1); stoplight[12].setBlockW(5);
		stoplight[13].setBlockN(2); stoplight[13].setBlockE(7);
		stoplight[13].setBlockS(1); stoplight[13].setBlockW(6);
		
		stoplight[14].setBlockN(3); stoplight[14].setBlockE(1);
		stoplight[14].setBlockS(2); stoplight[14].setBlockW(0);
		stoplight[15].setBlockN(3); stoplight[15].setBlockE(2);
		stoplight[15].setBlockS(2); stoplight[15].setBlockW(1);
		stoplight[16].setBlockN(3); stoplight[16].setBlockE(3);
		stoplight[16].setBlockS(2); stoplight[16].setBlockW(2);
		stoplight[17].setBlockN(3); stoplight[17].setBlockE(4);
		stoplight[17].setBlockS(2); stoplight[17].setBlockW(3);
		stoplight[18].setBlockN(3); stoplight[18].setBlockE(5);
		stoplight[18].setBlockS(2); stoplight[18].setBlockW(4);
		stoplight[19].setBlockN(3); stoplight[19].setBlockE(6);
		stoplight[19].setBlockS(2); stoplight[19].setBlockW(5);
		stoplight[20].setBlockN(3); stoplight[20].setBlockE(7);
		stoplight[20].setBlockS(2); stoplight[20].setBlockW(6);
		
		stoplight[21].setBlockN(4); stoplight[21].setBlockE(1);
		stoplight[21].setBlockS(3); stoplight[21].setBlockW(0);
		stoplight[22].setBlockN(4); stoplight[22].setBlockE(2);
		stoplight[22].setBlockS(3); stoplight[22].setBlockW(1);
		stoplight[23].setBlockN(4); stoplight[23].setBlockE(3);
		stoplight[23].setBlockS(3); stoplight[23].setBlockW(2);
		stoplight[24].setBlockN(4); stoplight[24].setBlockE(4);
		stoplight[24].setBlockS(3); stoplight[24].setBlockW(3);
		stoplight[25].setBlockN(4); stoplight[25].setBlockE(5);
		stoplight[25].setBlockS(3); stoplight[25].setBlockW(4);
		stoplight[26].setBlockN(4); stoplight[26].setBlockE(6);
		stoplight[26].setBlockS(3); stoplight[26].setBlockW(5);
		stoplight[27].setBlockN(4); stoplight[27].setBlockE(7);
		stoplight[27].setBlockS(3); stoplight[27].setBlockW(6);
		
		stoplight[28].setBlockN(5); stoplight[28].setBlockE(1);
		stoplight[28].setBlockS(4); stoplight[28].setBlockW(0);
		stoplight[29].setBlockN(5); stoplight[29].setBlockE(2);
		stoplight[29].setBlockS(4); stoplight[29].setBlockW(1);
		stoplight[30].setBlockN(5); stoplight[30].setBlockE(3);
		stoplight[30].setBlockS(4); stoplight[30].setBlockW(2);
		stoplight[31].setBlockN(5); stoplight[31].setBlockE(4);
		stoplight[31].setBlockS(4); stoplight[31].setBlockW(3);
		stoplight[32].setBlockN(5); stoplight[32].setBlockE(5);
		stoplight[32].setBlockS(4); stoplight[32].setBlockW(4);
		stoplight[33].setBlockN(5); stoplight[33].setBlockE(6);
		stoplight[33].setBlockS(4); stoplight[33].setBlockW(5);
		stoplight[34].setBlockN(5); stoplight[34].setBlockE(7);
		stoplight[34].setBlockS(4); stoplight[34].setBlockW(6);
		
		stoplight[35].setBlockN(6); stoplight[35].setBlockE(1);
		stoplight[35].setBlockS(5); stoplight[35].setBlockW(0);
		stoplight[36].setBlockN(6); stoplight[36].setBlockE(2);
		stoplight[36].setBlockS(5); stoplight[36].setBlockW(1);
		stoplight[37].setBlockN(6); stoplight[37].setBlockE(3);
		stoplight[37].setBlockS(5); stoplight[37].setBlockW(2);
		stoplight[38].setBlockN(6); stoplight[38].setBlockE(4);
		stoplight[38].setBlockS(5); stoplight[38].setBlockW(3);
		stoplight[39].setBlockN(6); stoplight[39].setBlockE(5);
		stoplight[39].setBlockS(5); stoplight[39].setBlockW(4);
		stoplight[40].setBlockN(6); stoplight[40].setBlockE(6);
		stoplight[40].setBlockS(5); stoplight[40].setBlockW(5);
		stoplight[41].setBlockN(6); stoplight[41].setBlockE(7);
		stoplight[41].setBlockS(5); stoplight[41].setBlockW(6);
		
		stoplight[42].setBlockN(7); stoplight[42].setBlockE(1);
		stoplight[42].setBlockS(6); stoplight[42].setBlockW(0);
		stoplight[43].setBlockN(7); stoplight[43].setBlockE(2);
		stoplight[43].setBlockS(6); stoplight[43].setBlockW(1);
		stoplight[44].setBlockN(7); stoplight[44].setBlockE(3);
		stoplight[44].setBlockS(6); stoplight[44].setBlockW(2);
		stoplight[45].setBlockN(7); stoplight[45].setBlockE(4);
		stoplight[45].setBlockS(6); stoplight[45].setBlockW(3);
		stoplight[46].setBlockN(7); stoplight[46].setBlockE(5);
		stoplight[46].setBlockS(6); stoplight[46].setBlockW(4);
		stoplight[47].setBlockN(7); stoplight[47].setBlockE(6);
		stoplight[47].setBlockS(6); stoplight[47].setBlockW(5);
		stoplight[48].setBlockN(7); stoplight[48].setBlockE(7);
		stoplight[48].setBlockS(6); stoplight[48].setBlockW(6);
		
		stoplight[49] = new Stoplight();
		stoplight[49].setPos(-135, -135);
		
		for (int i = 0;i<car.length;i++)
			car[i].setIndex(i);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		StdDraw.setCanvasSize(screenSize.width, screenSize.height);
		StdDraw.setXscale(0.0, 250.0);
		StdDraw.setYscale(0.0, 250.0);
		
		timer = new Timer();
		simulationTimer();
		timerX = new Timer();
		perMinTimer();
		while (Resume)
			startSimulation();
	}
}