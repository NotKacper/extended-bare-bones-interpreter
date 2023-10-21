package Interpreter_Classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxMatcher {
	private final Pattern commandExpression = Pattern.compile("(incr|decr|clear) [A-Za-z](\\d|[A-Za-z])*;");
	private final Pattern loopExpression = Pattern.compile("while [A-Za-z](\\d|[A-Za-z])* not 0 do;");
	private final Pattern loopEndExpression = Pattern.compile("end;");

	// New addition to bare-bones!, comments!
	private final Pattern commentPattern = Pattern.compile("//.*");

	public String matchToSyntax(String line) {
		Matcher commentMatcher = commentPattern.matcher(line);
		if (commentMatcher.find()) return "comment";
		Matcher commandMatcher = commandExpression.matcher(line);
		if (commandMatcher.find()) return "command";
		Matcher loopMatcher = loopExpression.matcher(line);
		if (loopMatcher.find()) return "loop";
		Matcher loopEndMatcher = loopEndExpression.matcher(line);
		if (loopEndMatcher.find()) return "end";
		return "error";
	}

}
