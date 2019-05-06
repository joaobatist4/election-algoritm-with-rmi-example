import java.rmi.Remote;
import java.util.HashMap;
import java.rmi.RemoteException; 

public interface IServer extends Remote {
	void register(Processo process)throws RemoteException ;
	HashMap<Long, Processo> getProcesses()throws RemoteException ;
	void remove(Processo process)throws RemoteException ;
}
