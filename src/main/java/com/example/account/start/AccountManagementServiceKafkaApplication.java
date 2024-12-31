package com.example.account.start;

import org.platformlambda.core.annotations.MainApplication;
import org.platformlambda.core.models.EntryPoint;
import org.platformlambda.core.system.AutoStart;
import org.platformlambda.core.system.Platform;
import org.platformlambda.core.system.ServerPersonality;

@MainApplication
public class AccountManagementServiceKafkaApplication implements EntryPoint {

	public static void main(String[] args) {

		AutoStart.main(args);
	}

	@Override
	public void start(String[] args) throws Exception {
		ServerPersonality.getInstance().setType(ServerPersonality.Type.REST);

		Platform platform = Platform.getInstance();
		/*
		 * In application.properties, the "cloud.connector" is set to "none" when your
		 * app is running in standalone mode.
		 *
		 * If you want to use a network event stream system to connect application service
		 * instances together, you may configure "cloud.connector" to "kafka" or a connector
		 * that you implement.
		 *
		 * When "cloud.connector" is set to "none", connectToCloud() method has no effect.
		 */
		platform.connectToCloud();
	}
}
