package br.ufrj.cos.prisma.miner.util;

public class Log {
	private static boolean VERBOSE = true;

	public static void i(String key, String message) {
		if (VERBOSE) {
			System.out.println(String.format("%s: %s", key, message));
		}
	}

	public static void i(String message) {
		if (VERBOSE) {
			System.out.println(String.format("%s", message));
		}
	}

}
