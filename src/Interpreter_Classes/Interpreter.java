package Interpreter_Classes;

import Exceptions.DecrementationException;
import Exceptions.InvalidSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.mvel2.MVEL;

public class Interpreter {

	// stores the sourceCode field ArrayList indexes mapped to the actual line count of the source code file.
	private final HashMap<Integer, Integer> logicalLineToFileLine = new HashMap<>();

	// maps the identifiers of variables in file to their values.
	private final HashMap<String, Variable> variables = new HashMap<>();

	// stack used to keep track of nested while loops

	// need to change loopStack structure to allow the new boolean expression approach to while loops
	private final Stack<ArrayList<String>> loopStack = new Stack<>();

	// object which matches a line to its correct instruction using regex.
	private final SyntaxMatcher syntaxMatcher = new SyntaxMatcher();

	private boolean debugging;

	private int currentLinePointer;

	private ArrayList<String> sourceCode;

	public void start() throws IOException {
		boolean running = true;
		while (running) {
			this.run();
			// allows user to interpret multiple files in one run of the program.
			running = !IOHandler.getUserInput("Do you want to interpret another file? y/n").equalsIgnoreCase("n");
		}
	}

	public void clearRuntime() {
		variables.clear();
		logicalLineToFileLine.clear();
		loopStack.clear();
	}

	public void run() {
		clearRuntime();
		try {
			String fileName = IOHandler.getUserInput("What file would you like to interpret? (input absolute file path)");
			debugging = IOHandler.getUserInput("Would you like to debug? y/n").equalsIgnoreCase("y");
			// store file lines inside an ArrayList as Strings
			sourceCode = IOHandler.getFileContents(fileName, logicalLineToFileLine);
			// method which goes line by line executing the file code
			interpretCode();
			IOHandler.outputMessage("Interpretation complete");
		} catch (Exception error) {
			// outputs error messages
			IOHandler.outputMessage(error.getMessage());
		}
	}

	private void interpretCode() throws DecrementationException, InvalidSyntaxException, IOException {
		// linearly search through the code ArrayList with index pointer in order to point to earlier code to execute loops.
		String lineType;
		boolean comment;
		while (currentLinePointer < sourceCode.size()) {
			// checks if a line of code is a command, while loop, or end of a while loop.
			lineType = syntaxMatcher.matchToSyntax(sourceCode.get(currentLinePointer));
			comment = executeLine(lineType);
			if (!comment) {
				IOHandler.displayStatesOfVariables(currentLinePointer, variables, logicalLineToFileLine);
				if (debugging) {
					IOHandler.outputMessage(sourceCode.get(currentLinePointer));
					IOHandler.getUserInput("enter any input to continue");
				}
			}
			currentLinePointer++;
		}
	}

	private boolean executeLine(String lineType) throws InvalidSyntaxException, DecrementationException {
		if (lineType.equals("comment")) return true;
		switch (lineType) {
			case "command" -> executeCommand();
			case "while loop" -> executeWhileLoop();
			case "for loop" -> executeForLoop();
			case "while end" -> endWhileLoop();
			case "for end" -> endForLoop();
			default ->
					throw new InvalidSyntaxException("Invalid syntax at line " + logicalLineToFileLine.get(currentLinePointer));
		}
		return false;
	}

	private void executeForLoop() {
	}

	private void endForLoop() {
	}

	// !!!spaghetti code warning!!!
	private void executeWhileLoop() throws InvalidSyntaxException {
		String line = sourceCode.get(currentLinePointer);
		// use StringBuilder to reverse line to find end of brackets for boolean expressions.
		StringBuilder sb = new StringBuilder();
		String reversedLine = String.valueOf(sb.append(line).reverse());
		int lowerBound = line.indexOf('(');
		int upperBound = line.length() - reversedLine.indexOf(')');
		String booleanExpression = line.substring(lowerBound, upperBound);
		Boolean run = evaluateBooleanExpression(booleanExpression);
		if (run) { // adds loop to the loopStack
			loopStack.push(new ArrayList<>());
			loopStack.peek().add(booleanExpression);
			loopStack.peek().add(String.valueOf(currentLinePointer));
		} else {
			skipLoop(); // if the initial conditions for the loop are not met then skip to the end of the loop
		}
	}


	// road block hit with this lmao ---- scriptEngine is no longer used since Java 11 so I cannot
	// use it to evaluate stuff.
	private Boolean evaluateBooleanExpression(String booleanExpression) throws InvalidSyntaxException {
		try {
			String[] tokens = booleanExpression.split("(\\s+)|(\\(|\\))");
			String[] temp = new String[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				if (variables.containsKey(tokens[i])) {
					temp[i] = variables.get(tokens[i]).getValue() + " ";
				} else {
					temp[i] = tokens[i] + " ";
				}
			}
			StringBuilder evaluatableExpression = new StringBuilder();
			for (String token : temp) {
				evaluatableExpression.append(token);
			}
		} catch (Exception error) {
			throw new InvalidSyntaxException("Invalid boolean syntax at line " + logicalLineToFileLine.get(currentLinePointer));
		}
	}


	// if the currentLinePointerPointer encounters an end it will either remove a loop from the stack or continue looping
	private void endWhileLoop() throws InvalidSyntaxException {
		if (!loopStack.empty()) {
			String booleanExpression = loopStack.peek().get(0);
			Boolean run = evaluateBooleanExpression(booleanExpression);
			int index = Integer.parseInt(loopStack.peek().get(1));
			if (!run) {
				loopStack.pop();
			} else {
				currentLinePointer = index;
			}
		}
	}

	private void skipLoop() {
		int noOfLoops = loopStack.size();
		int count = 0;
		int temp = currentLinePointer;
		do {
			if (sourceCode.get(temp).contains("while")) {
				noOfLoops++;
			} else if (sourceCode.get(temp).contains("end;")) {
				count++;
			}
			temp++;
		} while (noOfLoops != count);
		currentLinePointer = temp - 1;
	}

	private void executeCommand() throws DecrementationException {
		String command = sourceCode.get(currentLinePointer);
		String[] tokens = command.split("(\\s+)|;");
		String operator = tokens[0]; // returns 1 of "incr", "decr", "clear", "in" ...
		String variable = tokens[1];
		boolean doubleInput = tokens.length > 2;
		int operand = 0;
		if (doubleInput) {
			if (tokens[2].matches("\\d+")) {
				operand = Integer.parseInt(tokens[2]);
			} else {
				operand = variables.get(tokens[2]).getValue();
			}
		}
		if (!variables.containsKey(variable)) { // if the variable doesn't exist in the map then add it.
			variables.put(variable, new Variable());
		}
		if (!doubleInput) {
			variables.get(variable).update(operator);
		} else {
			variables.get(variable).update(operator, operand);
		}
	}
}
