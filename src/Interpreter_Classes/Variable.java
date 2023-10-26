package Interpreter_Classes;

import Exceptions.DecrementationException;

public class Variable {

	private int value = 0;

	public void update(String command) throws DecrementationException {
		switch (command) {
			case "incr" -> incr();
			case "decr" -> decr();
			case "clear" -> clear();
		}
	}

	public void update(String command, Variable argument) throws DecrementationException {
		int operand = argument.getValue();
		switch (command) {
			case "add" -> add(operand);
			case "sub" -> add(-operand);
			case "multiply" -> multiply(operand);
			case "divide" -> divide(operand); // uses floor division as only integers are allowed.
		}
	}

	private void add(int argument) {
		value += argument;
	}

	private void multiply(int argument) {
		value *= argument;
	}

	private void divide(int argument) {
		value/=argument;
	}

	public int getValue() {
		return value;
	}

	public void clear() {
		value = 0;
	}

	public void incr() {
		value++;
	}

	public void decr() throws DecrementationException {
		if (value > 0) value--;
		else throw new DecrementationException("Error: Cannot decrement a variable with value 0");
	}

}
