import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VotingService extends Remote {
    int vote(String candidate) throws RemoteException;
    int getCount(String candidate) throws RemoteException;
    List<String> getAllCandidates() throws RemoteException;
    void removeVotes() throws RemoteException;
    void removeVotesAndCandidates() throws RemoteException;
    Map<String, Integer> getVotingInformation() throws RemoteException;
}
