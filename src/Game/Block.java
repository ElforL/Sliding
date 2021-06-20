package Game;

public class Block {
	private static int nums = 1;
	public int num;
	public boolean isEmpty = false;
	
	public Block() {
		num = nums++;
	}
	
	public void setEmpty() {
		isEmpty = true;
		num = 0;
	}
}
