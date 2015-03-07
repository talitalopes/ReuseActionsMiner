package br.ufrj.cos.prisma.miner.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public abstract class BaseFileWalker {

	protected ASTParser parser;
	protected boolean parseMethods = false;

	public BaseFileWalker() {
		parser = ASTParser.newParser(AST.JLS8);
	}

	public void walk(String path) {
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath());
				continue;
			}

			try {
				if (!f.getAbsolutePath().contains(".java")) {
					continue;
				}

				String content = readFile(f.getAbsolutePath(),
						StandardCharsets.UTF_8);
				getClassInfo(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void getClassInfo(String content);
	
	String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
