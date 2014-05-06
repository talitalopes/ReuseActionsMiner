package br.ufrj.cos.prisma.miner.util;

public class Constants {

	public static String JAVA_NATURE = "org.eclipse.jdt.core.javanature";
	
	public static String ERROR_KEY = "ERROR";
	public static String PROCESS_NOT_EXISTS = "Process model does not exist.";
	public static String FRAMEWORK_PROJECT_NAME_KEY = "Framework project name";
	
	public static String ERROR_LOADING_FRAMEWORK = "Error loading framework project. Project name is null.";
	public static String ERROR_FRAMEWORK_NOT_EXISTS = "Framework project does not exist.";
	
	public static String LABEL_START_MINING = "Start Mining";
	public static String LABEL_START_MINING_REPO = "Mine remote repositories";
	public static String LABEL_GENERATE_LOG = "Generate Complete Log";
	public static String LABEL_GENERATE_CLASSES_LOG = "Generate Log for Classes";

	public static final int ID_START_MINING = 1001;
	public static final int ID_START_MINING_REPO = 1004;
	public static final int ID_GENERATE_LOG = 1002;
	public static final int ID_GENERATE_LOG_CLASSES_ONLY = 1003;
	
	public static final String CLASS_EXTENSION = "CLASS_EXTENSION";
	public static final String METHOD_EXTENSION = "METHOD_EXTENSION";
	
	public static final String CONSOLE_NAME = "PROCESS_MINING_CONSOLE";
}
