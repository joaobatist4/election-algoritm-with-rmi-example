import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Server extends UnicastRemoteObject implements IServer {

	
	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 2L;
	private Vector<IProcess> processes;
	
	@Override
	public Vector<IProcess> getProcesses(){
		return processes;
	}
	
	@Override
	public void register(IProcess process) {
		this.processes.add(process);		
	}
	
	public static void Main(String[] args) {
		Registry reg = null;
		
		try {
			reg = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			IServer server = new Server();
			reg.rebind("server", server);
			
			Process p0 = new Process();
			Process p1 = new Process();
			Process p2 = new Process();
			Process p3 = new Process();
			Process p4 = new Process();
			
			p0.run();
			p1.run();
			p2.run();
			p3.run();
			p4.run();
			
			server.register(p0);
			server.register(p1);
			server.register(p2);
			server.register(p3);
			server.register(p4);
			
			System.out.println("p0: " + p0.getPID());
			System.out.println("p1: " + p1.getPID());
			System.out.println("p2: " + p2.getPID());
			System.out.println("p3: " + p3.getPID());
			System.out.println("p4: " + p4.getPID());
			System.out.println("server: " + server.getProcesses());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
