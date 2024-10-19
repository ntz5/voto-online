import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class VotingServer extends UnicastRemoteObject implements VotingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String, Integer> candidateVotes;
	private ReentrantLock mutex = new ReentrantLock();

	public VotingServer() throws RemoteException {
		candidateVotes = new HashMap<>();
	}

	@Override
	public int vote(String candidate) throws RemoteException {
		if (candidate.isBlank()) {
			throw new IllegalArgumentException("Nombre ilegal");
		}
		mutex.lock();
		candidate = candidate.toUpperCase();
		candidateVotes.putIfAbsent(candidate, 0);
		candidateVotes.put(candidate, candidateVotes.get(candidate) + 1);
		int v = candidateVotes.get(candidate);
		mutex.unlock();
		return v;
	}

	@Override
	public int getCount(String candidate) throws RemoteException {
		mutex.lock();
		candidate = candidate.toUpperCase();
		Integer c = candidateVotes.get(candidate);
		mutex.unlock();
		if (c == null) {
			throw new IllegalArgumentException("Candidato \"" + candidate + "\" no existente");
		}
		return c;
	}

	@Override
	public List<String> getAllCandidates() throws RemoteException {
		mutex.lock();
		List<String> l = new ArrayList<>(candidateVotes.keySet());
		mutex.unlock();
		return l;
	}

	@Override
	public void removeVotes() throws RemoteException {
		mutex.lock();
		candidateVotes.forEach((candidate, vote) -> candidateVotes.put(candidate, 0));
		mutex.unlock();
	}

	@Override
	public void removeVotesAndCandidates() throws RemoteException {
		mutex.lock();
		candidateVotes = new HashMap<>();
		mutex.unlock();
	}

	@Override
	public Map<String, Integer> getVotingInformation() throws RemoteException {
		return candidateVotes;
	}

	public static void main(String[] args) {
		try {
            VotingService server = new VotingServer();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Servidor-Voto", server);
            System.err.println("Sevidor-Voto iniciado");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
	}

}
