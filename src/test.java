import java.util.ArrayList;


public class test {
	public static void main(String[] args) {
		int x = 100;
		x = x +1;
		while(true){
			if(isSparse(x)){
				System.out.println(x);
				break;
			}
			else
				x++;
		}
	}

	private static boolean isSparse(int x) {
		// TODO Auto-generated method stub
		ArrayList<Integer> booleanRep = new ArrayList<Integer>();
		while(x != 0){
			booleanRep.add(x&1);
			x >>= 1; 
		}
		for (int i = 0; i < booleanRep.size() - 1; i++) {
			if(booleanRep.get(i) == 1 && booleanRep.get(i+1) == 1){
				return false;
			}
		}
		return true;
	}
}
