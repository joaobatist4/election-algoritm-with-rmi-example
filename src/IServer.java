import java.rmi.Remote;
import java.util.HashMap;
import java.rmi.RemoteException; 

public interface IServer extends Remote {
	void register(Process process)throws RemoteException ;
	HashMap<Long, Process> getProcesses()throws RemoteException ;
}
