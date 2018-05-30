package network;

public interface IObserver {

	void update(Connection connection);

	void removeConnection(Connection connection);
	
	void sendFileList(Connection connection, String userName);
}