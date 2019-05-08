import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Random;

public class Processo extends Thread implements IProcess, Serializable {
	private static final long serialVersionUID = 1L;
	Long PID;

	protected Processo() throws RemoteException {
		setPID();
	}

	private Processo leader = null;
	private HashMap<Long, Processo> processes;
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
		Processo p = null;
		for (Long key : processes.keySet()) {
			if (key > maxPID) {
				p = processes.get(key);
				maxPID = key;
			}
		}
		// pegar o processo com maior PID
		// setar o lider
		this.setLeader(p);
		if(getPID() != maxPID)
			System.out.println("Novo lider eh.: " + maxPID);
		else
			System.out.println("Eu sou o novo lider. Processo " + this.getPID());
		// Zerar comparador
		maxPID = 0;
	}

	@Override
	public void setLeader(Processo leader) {
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
				if (leader.keepAlive()) {
					if (getPID() != leader.getPID())
						System.out.println("O processo " + this.getPID() + " encontrou o leader " + leader.getPID());
					else
						System.out.println("Eu sou o lider. Processo " + this.getPID());
				} else {
					removeLeader();
					leader = null;
					startElection();
				}
			} catch (Exception e) {
				leader = null;
				startElection();
			}
		}
	}

	private void removeLeader() {
		String host = "localhost";
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			IServer stub = (IServer) registry.lookup("server");
			if (stub != null) {
				stub.remove(leader);
			}
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		leader = null;
	}

	public boolean keepAlive() {
		return isStillAllive("" + PID);
	}

	boolean isStillAllive(String pidStr) {
		String OS = System.getProperty("os.name").toLowerCase();
		String command = null;
		if (OS.indexOf("win") >= 0) {
			command = "cmd /c tasklist /FI \"PID eq " + pidStr + "\"";
			return isProcessIdRunning(pidStr, command);
		} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
			command = "ps -p " + pidStr;
			return isProcessIdRunning(pidStr, command);
		}
		return false;
	}

	boolean isProcessIdRunning(String pid, String command) {
		try {
			Runtime rt = Runtime.getRuntime();
			java.lang.Process pr = rt.exec(command);

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader bReader = new BufferedReader(isReader);
			String strLine = null;
			while ((strLine = bReader.readLine()) != null) {
				if (strLine.contains(" " + pid + " ")) {
					return true;
				}
			}

			return false;
		} catch (Exception ex) {
			return true;
		}
	}

}
