package pokerupserver;

import com.shephertz.app42.server.AppWarpServer;

public class Main {
	public static void main(String[] args) {
		// Get the config file for AppWarp S2
		String appconfigPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "AppConfig.json";
		System.out.println("AppConfig : " + appconfigPath);
		// Start the AppWarp server
		boolean started = AppWarpServer
				.start(new PokerServerExtension(), appconfigPath);
		if (!started) {

			System.out.println("Main : AppWarpServer did not start. See logs for details. ");
			try {
				throw new Exception(
						"AppWarpServer did not start. See logs for details.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Main : Server Started ");
		}
	}
}
