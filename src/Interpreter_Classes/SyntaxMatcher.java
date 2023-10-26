package Interpreter_Classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxMatcher {
	// added arithmetic methods
	private final Pattern commandExpression = Pattern.compile("(incr|decr|clear|add|mulitply|divide)\\s+[A-Za-z](\\d|[A-Za-z])*\\s+;");
	private final Pattern loopExpression = Pattern.compile("while\\s+[A-Za-z](\\d|[A-Za-z])*\\s+not\\s+0\\s+do\\s+;");
	private final Pattern loopEndExpression = Pattern.compile("end\\s+;");

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
