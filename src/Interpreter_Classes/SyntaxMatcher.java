package Interpreter_Classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxMatcher {
	// added arithmetic methods
	private final Pattern commandExpression = Pattern.compile("(incr|decr|clear|out|in)\\s+[A-Za-z](\\d|[A-Za-z])*\\s*;");

	private final Pattern commandExpression2 = Pattern.compile("(add|sub|multiply|divide)\\s+[A-Za-z](\\d|[A-Za-z])*\\s+(([A-Za-z](\\d|[A-Za-z])*)|(\\d+))\\s*;");

	// add boolean expressions to this, using another object to parse nested brackets.
	// need this to work before implementing for loops as they are otherwise moot.
	private final Pattern loopIndefiniteBeginningExpression = Pattern.compile("while\\s+\\(");
	private final Pattern loopIndefiniteEndingExpression = Pattern.compile("\\)\\s*;");

	private final Pattern loopDefiniteExpression = Pattern.compile("for\\s+(([A-Za-z](\\d|[A-Za-z])*)|(\\d+))*\\s*;");
	private final Pattern loopWhileEndExpression = Pattern.compile("while\\s+end\\s*;");

	private final Pattern loopForEndExpression = Pattern.compile("for\\s+end\\s*;");

	// New addition to bare-bones!, comments!
	private final Pattern commentPattern = Pattern.compile("//.*");

	public String matchToSyntax(String line) {
		Matcher matcher1 = commentPattern.matcher(line);
		if (matcher1.find()) return "comment";
		matcher1 = commandExpression.matcher(line);
		Matcher matcher2 = commandExpression2.matcher(line);
		if (matcher1.find() || matcher2.find()) return "command";
		matcher1 = loopIndefiniteBeginningExpression.matcher(line);
		matcher2 = loopIndefiniteEndingExpression.matcher(line);
		if (matcher1.find() && matcher2.find()) return "while loop";
		matcher1 = loopDefiniteExpression.matcher(line);
		if (matcher1.find()) return "for loop";
		matcher1 = loopWhileEndExpression.matcher(line);
		if (matcher1.find()) return "while end";
		matcher1 = loopForEndExpression.matcher(line);
		if (matcher1.find()) return "for end";
		return "error";
	}

}
