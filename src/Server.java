import java.rmi.RemoteException;
import java.util.HashMap;

public class Server implements IServer {

	private HashMap<Long, Processo> tProcesses;

	protected Server() throws RemoteException {
		tProcesses = new HashMap<>();
	}

	@Override
	public HashMap<Long, Processo> getProcesses() {
		return tProcesses;
	}

	@Override
	public void register(Processo process) {
		System.out.println("Adicionando processo.:" + process.getPID());
		this.tProcesses.put(process.getPID(), process);
	}
	
	@Override
	public void remove(Processo process) {
		this.tProcesses.remove(process.getPID());
	}

}
