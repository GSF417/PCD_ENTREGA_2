package br.unifesp.gsferreira16.main;

import java.util.Random;

public class Main {

	private static Random rnd = new Random();
	private static int request = -1;
	private static int respond = -1;
	private static int sum = 0;
	
	private static class ClientProc implements Runnable {

		int thNum = 0;
		int maxRuns = 0;
		
		@Override
		public void run() {
			for (int i = 0; i < maxRuns; i++) {
				try {
					int local = 0;
					while (respond != thNum) {
						request = thNum;
						Thread.sleep(rnd.nextInt(1));
					}
					local = sum + 1;
					sum = local;
					if (local % 1000 == 0) System.out.println("Th."+thNum+" "+local);
					System.out.flush();
					respond = -1;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		
		public static Thread createProc(int thNum, int maxRuns) {
			ClientProc client = new ClientProc();
			client.maxRuns = maxRuns;
			client.thNum = thNum;
			Thread t = new Thread(client, ""+thNum);
			t.start();
			return t;
		}
		
	}
	
	private static class ServerProc implements Runnable {

		int maxRuns = 0;
		
		@Override
		public void run() {
			for (int i = 0; i < maxRuns; i++) {
				while (request == -1) {
					//System.out.println("S.w");
					System.out.flush();
				}
				respond = request;
				//System.out.println("S."+respond+" C.r");
				System.out.flush();
				while (respond != -1) {
					//System.out.println("S.s ");
					System.out.flush();
				}
				request = -1;
			}
			return;
		}
		
		public static Thread createProc(int maxRuns) {
			ServerProc server = new ServerProc();
			server.maxRuns = maxRuns;
			Thread t = new Thread(server, "Server");
			t.start();
			return t;
		}
		
	}
	
	public static void main(String[] args) {
		int maxRuns = 120000000;
		int clientCount = 2;
		Thread server = ServerProc.createProc(maxRuns);
		Thread[] clients = new Thread[clientCount];
		for (int i = 0; i < clientCount; i++) {
			clients[i] = ClientProc.createProc(i, maxRuns / clientCount);
		}
		try {
			server.join();
			for (int i = 0; i < clientCount; i++) {
				clients[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Final SUM: "+sum);
	}
}
