class HT15 {
	public static void main (String [] args) {
		System.out.println(new A().start());
	}
}
class A extends HT15 {
	public int start() {
		int x;
		x = 0;
		while(this.cond(x, 5)) {
			x = x+1;
			System.out.println(1);
		}
		return 0;
	}
	public boolean cond(int a, int b) {
		boolean ret;
		if (a <= b)
			ret = true;
		else
			ret = false;
		System.out.println(a+b);
		return ret;
	}
}
