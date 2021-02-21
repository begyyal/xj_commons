package begyyal.commons.util.math;

public class SuperMath {

    public static int gcd(int a, int b) {
	return b != 0 ? gcd(b, a % b) : a;
    }

    public static int lcm(int a, int b) {
	return a * b / gcd(a, b);
    }
}
