package Main;

import java.util.concurrent.Executors;

import Manager.Gameloop;

public class Main {

	public static void main(String[] args) {
		Executors.newSingleThreadExecutor().execute(new Gameloop());
	}
	
}
