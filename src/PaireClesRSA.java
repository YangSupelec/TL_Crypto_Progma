import java.io.Serializable;
import java.security.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PaireClesRSA implements Serializable{
	private KeyPair key;

	static{
		try{
			Security.addProvider(new BouncyCastleProvider());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	PaireClesRSA() 
	{
		// On va mettre un peu d'alea :
		SecureRandom rand = new SecureRandom();
		// On initialise la structure pour la generation de cle :
		KeyPairGenerator kpg=null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Impossible de créer la paire de clé RSA.");
			e.printStackTrace();
		}
		// On definit la taille de cle :
		kpg.initialize(512, rand);
		// On genere la paire de cle :
		this.key = kpg.generateKeyPair();
	}

	public PublicKey Publique() 
	{
		// Recuperation de la clé publique
		return this.key.getPublic();
	}

	public PrivateKey Privee() 
	{
		// Recuperation de la clé privée
		return this.key.getPrivate();
	}
}