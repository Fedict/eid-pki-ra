package be.fedict.eid.pkira.integration.xkms;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.testng.annotations.BeforeSuite;

public class SetupBouncyCastle {

	private static final Provider PROVIDER = new BouncyCastleProvider();


	@BeforeSuite
	public void registerProvider() {
		Security.addProvider(PROVIDER);
	}
}
