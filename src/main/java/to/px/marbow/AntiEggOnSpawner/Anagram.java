package to.px.marbow.AntiEggOnSpawner;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class Anagram {

	static Random rnd = new Random(System.currentTimeMillis());

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	static public byte[] EnCode(String strText, String strSeed) {
		byte[] byts = StandardCharsets.UTF_8.encode(strText).array();
		return EnCode(byts, strSeed);
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	static public byte[] EnCode(byte[] byts, String strSeed) {
		byte[] bytsReturn = new byte[0];

		if (byts.length > 0) {
			if (strSeed.length() < 5) {
				StringBuffer sb = new StringBuffer(strSeed);
				for (; sb.length() < 5;) {
					sb.append("3");
				}
				strSeed = sb.toString();
			}

			bytsReturn = Arrays.copyOf(byts, byts.length);

			int key;
			for (key = rnd.nextInt(100); String.valueOf(Math.log10(key)).length() < 7; key = rnd.nextInt(100)) {
			}

			double dblKey = Math.log10(key);
			String passWord = String.valueOf(dblKey).substring(2, 5);

			int[] intsSeed = new int[passWord.length()];
			for (int i = 0; i < intsSeed.length; i++) {
				intsSeed[i] = Integer.parseInt(passWord.substring(i, i + 1)) + 5
						+ strSeed.substring(i, i + 1).toCharArray()[0];
			}

			// スクランブルの実行
			for (int i = 0; i < intsSeed.length; i++) {
				byts = EnCode_Sub(bytsReturn, intsSeed[i]);
			}

			// 推測されないようにさらにごにょごにょ
			for (int i = 0; i < bytsReturn.length; i++) {
				bytsReturn[i] ^= (byte) intsSeed[i % intsSeed.length];
			}

			ByteBuffer bb = ByteBuffer.allocate(bytsReturn.length + 1);
			bb.position(1);
			bb.put(bytsReturn, 0, bytsReturn.length);
			bb.position(0);
			bb.put((byte) key);

			bytsReturn = bb.array();
		}

		return bytsReturn;
	}

	// ==========================================================

	/**
	 * key にしたがって byts の内容を入れ替える
	 * 
	 * @param byts
	 *            データbyte配列
	 * @param key
	 *            入れ替えキー
	 * @return byte[] 入れ替え完了データbyte配列
	 */
	static public byte[] EnCode_Sub(byte[] byts, int key) {
		int point = 0;
		int swapCount = key;

		for (; point < byts.length;) {
			if (swapCount > byts.length - point)
				swapCount = byts.length - point;

			ByteBuffer bbSwap = ByteBuffer.allocate(swapCount);
			bbSwap.put(byts, point, swapCount);
			byte[] bytsSwap = bbSwap.array();

			for (int i = 0; i < swapCount; i++) {
				byts[point + i] = bytsSwap[swapCount - i - 1];
			}

			point += swapCount;
			swapCount = ((byts[point - 1] + 5) % 16) + 1;
		}

		return byts;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	static public byte[] DeCode(byte[] byts, String strSeed) {

		byte[] bytsReturn = new byte[0];

		if (byts.length > 0) {
			if (strSeed.length() < 5) {
				StringBuffer sb = new StringBuffer(strSeed);
				for (; sb.length() < 5;) {
					sb.append("3");
				}
				strSeed = sb.toString();
			}

			ByteBuffer bbReturn = ByteBuffer.allocate(byts.length - 1);
			bbReturn.put(byts, 1, bbReturn.limit());
			bytsReturn = bbReturn.array();

			int key = (int) byts[0];

			double dblKey = Math.log10(key);
			String passWord = String.valueOf(dblKey).substring(2, 5);

			int[] intsSeed = new int[passWord.length()];
			for (int i = 0; i < intsSeed.length; i++) {
				intsSeed[i] = Integer.parseInt(passWord.substring(i, i + 1)) + 5
						+ strSeed.substring(i, i + 1).toCharArray()[0];
			}

			// さらにごにょごにょの復旧
			for (int i = 0; i < bytsReturn.length; i++) {
				bytsReturn[i] ^= (byte) intsSeed[i % intsSeed.length];
			}

			// スクランブルの復旧
			for (int i = 0; i < intsSeed.length; i++) {
				bytsReturn = DeCode_Sub(bytsReturn, intsSeed[intsSeed.length - i - 1]);
			}
		}

		return bytsReturn;
	}

	// ==========================================================

	static private byte[] DeCode_Sub(byte[] byts, int key) {
		int point = 0;
		int swapCount = key;

		for (; point < byts.length;) {
			if (swapCount > byts.length - point)
				swapCount = byts.length - point;

			ByteBuffer bbSwap = ByteBuffer.allocate(swapCount);
			bbSwap.put(byts, point, swapCount);
			byte[] bytsSwap = bbSwap.array();

			for (int i = 0; i < swapCount; i++) {
				byts[point + i] = bytsSwap[swapCount - i - 1];
			}

			point += swapCount;
			swapCount = ((byts[point - swapCount] + 5) % 16) + 1;
		}

		return byts;
	}
}
