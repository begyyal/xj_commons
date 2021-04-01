package begyyal.commons.util.matrix;

import java.util.stream.IntStream;

import begyyal.commons.util.math.SuperMath;

public class MatrixResolver {

    /**
     * startからdistanceの値だけ順次整数を刻むIntStreamを返却する。<br>
     * diatanceが正ならx方向へ、負なら-x方向に順次的になる。
     * 
     * @param start　exclusive
     * @param distance inclusive
     * @return IntStream
     */
    public static IntStream vectorOrderedStream(int start, int distance) {
	if (distance == 0)
	    throw new IllegalArgumentException("The distance '0' is not allowed.");
	if (distance > 0)
	    return IntStream.range(start + 1, start + distance + 1);
	else
	    return IntStream.range(start + distance, start)
		    .map(i -> -i)
		    .sorted()
		    .map(i -> -i);
    }
    
    /**
     * 引数の大きさ以下でかつマトリクスの格子に合致するベクトルの集合を取得する。
     * 
     * @param vector 
     * @return　ベクトルの集合
     */
    public static Vector[] decompose(Vector vector) {
	
	int gcd = Math.abs(SuperMath.gcd(vector.x(), vector.y()));
	int xFactor = vector.x() / gcd;
	int yFactor = vector.y() / gcd;
	var result = new Vector[gcd];
	for (int i = 1; i <= gcd; i++)
	    result[i - 1] = new Vector(i * xFactor, i * yFactor);
	
	return result;
    }
}
