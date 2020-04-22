import java.awt.Color;

public class Lane {
    public Block[] block = new Block[8];
    private double x0, y0, x1, y1;
    private char direction;
    private char rl;
    private Color color = new Color(50,50,50);
    
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.line(x0, y0, x1, y1);
        
      for (int i = 0;i<8;i++){
    	  if ((i!=7))
    		  if (rl == 'R'){
    			  block[i].right.draw();
    			  block[i].left.draw();
    		  }
    	  if ((i!=0))
    		  if (rl == 'L'){
    			  block[i].left.draw();
    			  block[i].right.draw();
      		  }
    	  
    	  if (Main.blockOverlay){
    		  block[i].setColor(block[i].getCarAmt());
    		  if ((StdDraw.mouseX()+1>=block[i].getX0())&&(StdDraw.mouseX()-1<=block[i].getX1()))
					if ((StdDraw.mouseY()+1>=block[i].getY0()+5)&&(StdDraw.mouseY()-1<=block[i].getY1()+5)){
						block[i].setColor(Color.ORANGE);
						StdDraw.setPenColor(Color.ORANGE);
				      	 StdDraw.text(10, 244, "block car amt ["+block[i].getCarAmt()+"]");
					}
    	  	  block[i].draw();
    	  }
      }
    }
    
    public void setup(double X0, double Y0, double X1, double Y1, Color color, char dir) {
        x0 = X0;
        y0 = Y0;
        x1 = X1;
        y1 = Y1;
        this.color = color;
        this.direction = dir;
    }
    
    public void setColor(Color newColor){ color = newColor; }
    
    public char getRl() { return rl; }

	public void setRl(char rl) { this.rl = rl; }
	
	public char getDirection() { return direction; }

	public double getX0() { return x0; }

	public void setX0(double x0) { this.x0 = x0; }

	public double getY0() { return y0; }

	public void setY0(double y0) { this.y0 = y0; }

	public double getX1() { return x1; }

	public void setX1(double x1) { this.x1 = x1; }

	public double getY1() { return y1; }

	public void setY1(double y1) { this.y1 = y1; }

	public void setDirection(char direction) { this.direction = direction; }
}
