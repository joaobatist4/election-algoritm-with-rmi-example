import java.rmi.RemoteException;
import java.util.HashMap;

public class Server implements IServer {

	private HashMap<Long, Process> tProcesses;

	protected Server() throws RemoteException {
		tProcesses = new HashMap<>();
	}

	@Override
	public HashMap<Long, Process> getProcesses() {
		return tProcesses;
	}

	@Override
	public void register(Process process) {
		System.out.println("Adicionando processo.:" + process.getPID());
		this.tProcesses.put(process.getPID(), process);
	}

}
