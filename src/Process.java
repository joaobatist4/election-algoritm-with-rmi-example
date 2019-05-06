import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Random;

public class Process extends Thread implements IProcess, Serializable {
	private static final long serialVersionUID = 1L;
	Long PID;

	protected Process() throws RemoteException {
		setPID();
	}

	private Process leader = null;
	private HashMap<Long, Process> processes;
	private long maxPID = 0l;

	@Override
	public void startElection() {
		// chamar RMI para pegar os processos existentes
		System.out.println("Chamando eleicao");
		String host = "localhost";
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			IServer server = (IServer) registry.lookup("server");
			this.processes = server.getProcesses();
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		// Pegar a lista de tarefas que subiu e verificar qual maior PID
		Process p = null;
		for (Long key : processes.keySet()) {
			if (key > maxPID) {
				p = processes.get(key);
				maxPID = key;
			}
		}
		// pegar o processo com maior PID
		// setar o lider
		this.setLeader(p);
		System.out.println("Novo lider eh.: " + maxPID);
		// Zerar comparador
		maxPID = 0;
	}

	@Override
	public void setLeader(Process leader) {
		this.leader = leader;
	}

	@Override
	public long getPID() {
		return PID;
	}

	public void setPID() {
		String id = ManagementFactory.getRuntimeMXBean().getName();
		// System.out.println("getPID");
		long pid = Long.parseLong(id.split("@")[0]);
		PID = pid;
	}

	@Override
	public void run() {
		System.out.println("Iniciando o processo .:" + getPID());
		while (true) {
			try {
				Random random = new Random();
				int time = random.nextInt((60000 - 30000) + 1) + 30000;
				Thread.sleep(time);
				System.out.println("Verificando se o lider esta ativo");
				if (keepAlive()) {
					if (getPID() != leader.getPID())
						System.out.println("O processo " + this.getPID() + " encontrou o leader " + leader.getPID());
					else
						System.out.println("Eu sou o lider dessa P@@@ Toda");
				} else {
					leader = null;
					startElection();
				}
			} catch (Exception e) {

			}
		}
	}

	public boolean keepAlive() {
		if (leader == null)
			return false;
		if (leader.isAlive())
			return true;
		processes.remove(leader.getPID());
		return false;
	}
}
