import java.rmi.Remote;

public interface IProcess extends Remote{
	public void startElection();
	public void setLeader(Processo leader);
	public long getPID();
	public boolean keepAlive();
	
}
