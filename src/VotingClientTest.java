import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class VotingClientTest implements Runnable {

	private static int task = 20;
	private static Registry registry;
    private static VotingService server;
	private static CountDownLatch latch = new CountDownLatch(task);

	public VotingClientTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args){
        try {
            registry =  LocateRegistry.getRegistry();
            server = (VotingService) registry.lookup("Servidor-Voto");

            for (int i = 0; i < task; i++) {
        		new Thread(new VotingClientTest()).start();
        	}
			latch.await();
			promptServices(server, 4, null);
			promptServices(server, 91, null);
			promptServices(server, 4, null);
			promptServices(server, 92, null);
			promptServices(server, 4, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private static void promptServices(VotingService server, int input, String n) throws RemoteException {
		int v;
    	switch (input) {
		case 1:
			System.err.println("Enviando el voto al servidor...");
			v = server.vote(n);
			System.err.printf("Votado con exito. %s=%d\n", n, v);
			break;
		case 2:
			v = server.getCount(n);
			System.err.printf("%s=%d\n", n, v);
			break;
		case 3:
			List<String> l = server.getAllCandidates();
			if (l.isEmpty()) {
				System.err.println("No hay candidatos en el sistema.");
			} else {
				System.err.println(l.toString());
			}
			break;
		case 4:
			Map<String, Integer> m = server.getVotingInformation();
			if (m.isEmpty()) {
				System.err.println("No hay candidatos en el sistema.");
			} else {
				System.err.println(m.toString());
			}
			break;
		case 91:
			server.removeVotes();
			System.err.println("Todos los votos eliminado.");
			break;
		case 92:
			server.removeVotesAndCandidates();
			System.err.println("Todos los votos y los candidatos eliminado.");
			break;
		default:
			System.err.println("No se ha encontrado ningun servicio.");
			break;
		}
	}

	@Override
	public void run() {
		try {
            int input = ThreadLocalRandom.current().nextInt(1,4);
            String n = "";
//            for (int i = 0; i < 3; i++) {
				n += Character.toString(ThreadLocalRandom.current().nextInt(65,90));
//			}            		
        	promptServices(server, input, n);

        } catch (Exception e) {
            System.err.println("Client side exception: " + e.getMessage());
//            e.printStackTrace();
        }
		System.err.flush();
		latch.countDown();
	}

}
