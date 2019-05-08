import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ProcessoMain {

	public ProcessoMain() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		String host = "localhost";
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			IServer stub = (IServer) registry.lookup("server");
			if (stub != null) {
				Processo p = new Processo();
				stub.register(p);
				p.start();
			}
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
