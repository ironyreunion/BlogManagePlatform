package jdt;

import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

public class EnhanceSourceCodeGenerator {

	private static final String PATH =
		"file:/D:/Code/Eclipse/workspace/BlogManagePlatform/src/main/java/frodez/dao/param/task/AddTask.java";

	public static void main(String[] args) throws URISyntaxException, IOException, MalformedTreeException,
		BadLocationException {
		System.out.println("代码生成开始!");
		EnhancePlugin plugin = new SwaggerEnhancePlugin();
		plugin.init(PATH);
		plugin.run();
		plugin.close();
		System.out.println("代码生成成功!");
	}

}
