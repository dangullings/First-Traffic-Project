import java.awt.Color;

public class TurnLane {
	private double x0, y0, x1, y1;
    private int carCap, carAmt;
    private int carAmtLength;
    private int lastCar;
    private Color color = new Color(50,50,50); 
    
    public TurnLane(double X0, double Y0, double X1, double Y1, Color newColor) {
    	this.x0 = X0;
    	this.y0 = Y0;
    	this.x1 = X1;
    	this.y1 = Y1;
    	this.color = newColor;
    }
    
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
    
    public int getCarAmtLength() {
		return carAmtLength;
	}

	public void setCarAmtLength(int carAmtLength) {
		this.carAmtLength += carAmtLength;
	}

	public int getCarCap() { return carCap; }

	public void setCarCap(int carCap) { this.carCap = carCap; }

	public int getCarAmt() { return carAmt; }

	public void setCarAmt(int carAmt) { this.carAmt += carAmt; }

	public int getLastCar() { return lastCar; }

	public void setLastCar(int lastCar) { this.lastCar = lastCar; }
    
    public double getX0() { return x0; }

	public void setX0(double x0) { this.x0 = x0; }

	public double getY0() { return y0; }

	public void setY0(double y0) { this.y0 = y0; }

	public double getX1() { return x1; }

	public void setX1(double x1) { this.x1 = x1; }

	public double getY1() { return y1; }

	public void setY1(double y1) { this.y1 = y1; }

	public void setColor(Color newColor){ color = newColor; }
	
}
