import java.rmi.RemoteException;

public class Main {

	public static void main(String args[]) {

		try {
			IProcess p0 = new Process();
			IProcess p1 = new Process();
			IProcess p2 = new Process();
			IProcess p3 = new Process();
			IProcess p4 = new Process();
			
			
			
			System.out.println("p0: " + p0.getPID());
			System.out.println("p1: " + p1.getPID());
			System.out.println("p2: " + p2.getPID());
			System.out.println("p3: " + p3.getPID());
			System.out.println("p4: " + p4.getPID());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}
}
