import java.awt.Color;

public class Road {
    private double x0, y0, x1, y1;
    Lane left = new Lane();
    Lane right = new Lane();

    public void draw() {
        left.draw();
        right.draw();
    }

    public void setPosVert(double X0, double Y0, double X1, double Y1) {
        x0 = X0;
        y0 = Y0;
        x1 = X1;
        y1 = Y1;
    }
    
    public void setPosHorz(double X0, double Y0, double X1, double Y1) {
        x0 = X0;
        y0 = Y0;
        x1 = X1;
        y1 = Y1;
    }
    
    public void setColor(Color newColor){
    	left.setColor(newColor);
    	right.setColor(newColor);
    }
    
    public double getX0() { return x0; }
    
    public double getY0() { return y0; }

    public double getX1() { return x1; }
    
    public double getY1() { return y1; }
}