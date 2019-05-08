import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

	public static void main(String args[]) throws RemoteException {
		Server obj = new Server();
		IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);
		// Bind the remote object's stub in the registry
		Registry reg = null;
		try {
			System.out.println("Creating registry...");
			reg = LocateRegistry.createRegistry(1099);
		} catch (Exception e) {
			try {
				reg = LocateRegistry.getRegistry(1099);
			} catch (Exception ee) {
				System.exit(0);
			}
		}
		reg.rebind("server", stub);
	}
}
