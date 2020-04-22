import java.awt.Color;

public class Block {
	private double x0, y0, x1, y1; 
    private int carAmt;
    private int lastCar;
    private int strLastCar;
	private int queueS;
	private int MLane;
	private int carAmtLength;
	private int carAmtLengthMLane;
	private Color color = new Color(62,62,62);

	TurnLane left = new TurnLane(x0, y0, x1, y1, color);
    TurnLane right = new TurnLane(x0, y0, x1, y1, color);
    
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.line(x0, y0, x1, y1);
    }
	
    public void setPos(double X0, double Y0, double X1, double Y1) {
        x0 = X0;
        y0 = Y0;
        x1 = X1;
        y1 = Y1;
    }

	public void setColor(int carAmt2) {
		carAmt2 = (int) (carAmt2);
		
		if (carAmt2 > 25)
			carAmt2 = 25;
		if (carAmt2 < 0)
			carAmt2 = 0;
		
		color = new Color((255*carAmt2)/25, (255*(25-carAmt2))/25, 0);
	}
	
    public int getCarAmtLength() {
		return carAmtLength;
	}

	public void setCarAmtLength(int carAmtLength) {
		this.carAmtLength = carAmtLength;
	}

	public int getCarAmtLengthMLane() {
		return carAmtLengthMLane;
	}

	public void setCarAmtLengthMLane(int carAmtLengthMLane) {
		this.carAmtLengthMLane += carAmtLengthMLane;
	}

	public int getMLane() { return MLane; }

	public void setMLane(int mLane) { MLane += mLane; }
	
    public int getStrLastCar() { return strLastCar; }

	public void setStrLastCar(int strLastCar) { this.strLastCar = strLastCar; }
	
    public int getQueueS() { return queueS; }

	public void setQueueS(int queueS) { this.queueS = queueS; }
	
    public double getX0() { return x0; }

	public int getCarAmt() { return carAmt; }

	public void setCarAmt(int carAmt) { this.carAmt += carAmt; }

	public int getLastCar() { return lastCar; }

	public void setLastCar(int lastCar) { this.lastCar = lastCar; }

	public void setX0(double x0) { this.x0 = x0; }

	public double getY0() { return y0; }

	public void setY0(double y0) { this.y0 = y0; }

	public double getX1() { return x1;}

	public void setX1(double x1) { this.x1 = x1; }

	public double getY1() { return y1; }

	public void setY1(double y1) { this.y1 = y1; }

	public void setColor(Color newColor){ color = newColor; }
}
