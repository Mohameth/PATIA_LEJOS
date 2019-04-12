package MyNavigator;

import java.util.ArrayList;
import java.util.List;

public class Observable {
	private List<MyObserver> obs = new ArrayList<>();
	
	
	
	public void addObserver(MyObserver channel) {
	    this.obs.add(channel);
	}

	public void removeObserver(MyObserver channel) {
	    this.obs.remove(channel);
	}
	
	public void notifyAll(String newCol) {
        for (MyObserver channel : this.obs) {
            channel.update(newCol);
        }
    }
	
	
}



