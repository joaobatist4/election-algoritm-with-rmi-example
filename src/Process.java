import java.lang.management.ManagementFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Vector;

public class Process extends UnicastRemoteObject implements IProcess, Runnable {

	protected Process() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	private IProcess leader;
	private Vector<IProcess> processes;
	private long maxPID = 0l;

	@Override
	public void startElection() {
		// chamar RMI para pegar os processos existentes
		Registry reg = null;

		try {
			reg = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			e.printStackTrace();

			try {
				reg = LocateRegistry.getRegistry(1099);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			System.exit(0);
		}
		
		try {
			IServer server = (IServer) reg.lookup("server");
			this.processes = server.getProcesses();
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

		// Pegar a lista de tarefas que subiu e verificar qual maior PID
		for (IProcess process : processes) {
			if (process.getPID() > maxPID) {
				maxPID = process.getPID();
			}
		}

		// pegar o processo com maior PID
		IProcess p = processes.stream().filter(process -> process.getPID() == maxPID).findFirst().get();

		// setar o lider
		this.setLeader(p);

		// Zerar comparador
		maxPID = 0;
	}

	@Override
	public void setLeader(IProcess leader) {
		this.leader = leader;
	}

	@Override
	public long getPID() {
		String id = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println("getPID");
		long pid = Long.parseLong(id.split("@")[0]);
		return pid;
	}

	@Override
	public void run() {
		try {
			Random random = new Random();
			int time = random.nextInt((60000 - 30000) + 1) + 30000;
			Thread.sleep(time);

			if (leader.keepAlive()) {
				leader.getPID();
				System.out.println("O processo " + this.getPID() + " encontrou o leader " + leader.getPID());
			}
		} catch (Exception e) {
			leader = null;
			startElection();
		}
	}

	@Override
	public boolean keepAlive() {
		return true;
	}
}
