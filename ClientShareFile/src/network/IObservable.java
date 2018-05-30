package network;

public interface IObservable {

	void addObserver(IObserver iObserver);
	
	void removeObserver();
}