package Interpreter_Classes;

import Exceptions.DecrementationException;

public class Variable {

	private int value = 0;

	public void update(String command) throws DecrementationException {
		switch (command) {
			case "incr" -> incr();
			case "decr" -> decr();
			case "clear" -> clear();
			case "in" -> input();
			case "out" -> output();
		}
	}

	public void update(String command, int argument) throws DecrementationException {
		switch (command) {
			case "add" -> add(argument);
			case "sub" -> add(-argument);
			case "multiply" -> multiply(argument);
			case "divide" -> divide(argument); // uses floor division as only integers are allowed.
			case "store" -> store(argument);
		}
	}

	private void store(int argument) {
		value = argument;
	}

	private void input() {
		value = IOHandler.getIntInput();
	}

	private void output() {
		System.out.println(value);
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
