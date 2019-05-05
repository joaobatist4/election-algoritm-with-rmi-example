import java.rmi.Remote;
import java.util.Vector;

public interface IServer extends Remote{
	void register(IProcess process);
	Vector<IProcess> getProcesses();
}
