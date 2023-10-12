package br.unifesp.gsferreira16.main;

public class TrafficController {

	private int turn = 0;
	private int bridgeOccupied = 0;
	private int carsInBridge = 0;
	
	private synchronized void manageTurn() {
		if (turn > 0 && bridgeOccupied == 1) {
			turn--;
			if (turn <= 0) {
				turn = -10;
			}
		}
		else if (turn <= 0 && bridgeOccupied == -1){
			turn++;
			if (turn >= 0) {
				turn = 10;
			}
		}
	}
	
	private synchronized void addToBridge(int side) {
		bridgeOccupied = side;
		carsInBridge++;
	}
	
	private synchronized void removeFromBridge() {
		carsInBridge--;
		if (carsInBridge <= 0) {
			bridgeOccupied = 0;
		}
	}
	
    public void enterLeft() {
    	while (turn > 0 || bridgeOccupied == 1) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		manageTurn();
    	}
    	addToBridge(-1);
    }
    public void enterRight() {
    	while (turn < 0 || bridgeOccupied == -1) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		manageTurn();
    	}
    	addToBridge(1);
    }
    public void leaveLeft() {
    	removeFromBridge();
    	
    }
    public void leaveRight() {
    	removeFromBridge();
    }

}