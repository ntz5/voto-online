import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VotingClient {

	public VotingClient() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args){
		boolean loop = true;
		Scanner prompt = new Scanner(System.in);
		while (loop) {
			try {
				Registry registry =  LocateRegistry.getRegistry();
				VotingService server = (VotingService) registry.lookup("Servidor-Voto");
				
				System.err.println(
						"Bienvenido al Voto Online\n"
					+ 	"Elija un servicio (introduzca un numero):\n"
					+ 	" 1. Votar por un candidato.\n"
					+ 	" 2. Obtener el numero de votos de un candidato.\n"
					+ 	" 3. Enumerar los candidatos existentes.\n"
					+ 	" 4. Obtener el resultado actual del sistema de voto.\n"
					+ 	"91. Eliminar todos los votos. (SOLO PARA ADMIN)\n"
					+ 	"92. Elimina todos los votos y los candidatos. (SOLO PARA ADMIN)\n"
					+ 	" 0. Salir\n");
				int input = Integer.parseInt(prompt.nextLine());
				String n; int v;
				switch (input) {
				case 0:
					loop = false;
					break;
				case 1:
					System.err.println("Nombre del candidato?");
					n = prompt.nextLine();
					System.err.println("Enviando el voto al servidor...");
					v = server.vote(n);
					System.err.printf("Votado con exito. %s=%d\n", n, v);
					break;
				case 2:
					System.err.println("Nombre del candidato?");
					n = prompt.nextLine();
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
					System.err.print("Clave: ");
					n = prompt.nextLine();
					if (n.equals("admin")) {
						server.removeVotes();
						System.err.println("Todos los votos eliminado.");
					} else {
						System.err.println("Clave incorrecta.");
					}
					break;
				case 92:
					System.err.print("Clave: ");
					n = prompt.nextLine();
					if (n.equals("admin")) {
						server.removeVotesAndCandidates();
						System.err.println("Todos los votos y los candidatos eliminado.");
					} else {
						System.err.println("Clave incorrecta.");
					}
					break;
				default:
					System.err.println("No se ha encontrado ningun servicio.");
					break;
				};
			} catch (Exception e) {
				System.err.println("Client side exception: " + e.getMessage());
            e.printStackTrace();
			}
		}
		prompt.close();
    }

}
