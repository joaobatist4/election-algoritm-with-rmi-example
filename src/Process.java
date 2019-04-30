import java.lang.management.ManagementFactory;
import java.util.Random;

public class Process implements IProcess, Runnable{

	private IProcess leader;
	
	@Override
	public void startElection() {
		//chamar RMI
		//Pegar a lista de tarefas que subiu
		//verificar qual o maior PID
		//setar o lider
		
	}

	@Override
	public void setLeader(IProcess leader) {
		this.leader = leader;
	}

	@Override
	public long getPID() {
		String id = ManagementFactory.getRuntimeMXBean().getName();
		long pid = Long.parseLong(id.split("@")[0]);
		return pid;
	}

	@Override
	public void run() {
		try {
			Random random = new Random();
			int time = random.nextInt((60000 - 30000) + 1) + 30000;
			Thread.sleep(time);
			
			if(leader.keepAlive()) {
				leader.getPID();
				System.out.println("O processo " + this.getPID() + " encontrou o leader " + leader.getPID());
			}
		}
		catch(Exception e) {
			leader = null;
			startElection();
		}
	}

	@Override
	public boolean keepAlive() {
		return true;
	}
}
