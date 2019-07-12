package MainPack;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) {
		System.out.println("Strart");
		@SuppressWarnings("resource")
		Scanner scanIn = new Scanner(System.in);
		while(true) {
			try {
				String cmd = scanIn.nextLine();
				switch(cmd) {
					case "showR":
						//
				}
			}
			catch(Exception exp) {
				System.out.println("Error");
				break;
			}
		}
	}
}