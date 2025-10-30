package operation;
import java.io.Serializable;

public class operation implements Serializable {
   
	private static final long serialVersionUID = 1L;
	int premier;
	String op;
	int deuxieme;
	public operation (int x,String y,int z)
	{
		premier=x;
		op=y;
		deuxieme=z;
	}
	public int getPremier() {
		return premier;
	}
	public String getOp() {
		return op;
	}
	public int getDeuxieme() {
		return deuxieme;
	}

	
}