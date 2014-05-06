package br.ufrj.cos.prisma.helpers;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import br.ufrj.cos.prisma.miner.util.Constants;

public class LogHelper {

	public static void log(String message) {
		System.out.println(message);
		MessageConsole myConsole = findConsole();
		MessageConsoleStream out = myConsole.newMessageStream();
		out.println(message);
	}
	
	public static void log(String errorType, String message) {
		if (errorType == null) {
			errorType = "Unknown"; 
		}
		
		if (message == null) {
			message = "null";
		}
		
		String completeMessage = String.format("Error: %s: %s", errorType, message);
		log(completeMessage);
	}
	
	private static MessageConsole findConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			if (Constants.CONSOLE_NAME.equals(existing[i].getName())) {
				return (MessageConsole) existing[i];
			}
		}
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(Constants.CONSOLE_NAME,
				null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
}
