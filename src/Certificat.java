import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.Serializable;




public class Certificat implements Serializable{
	static private BigInteger seqnum = BigInteger.ZERO;
	public X509Certificate x509;
	
	Certificat(String issuer, String subject, PublicKey clePub, PrivateKey signature, int validityDays) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException // constructeur du certificat 
	{
		// On recupere la cle publique et la cle privee :
		PublicKey pubkey = clePub;
		PrivateKey privkey = signature;
		// On cree la structure qui va nous permettre de creer le certificat
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		
		// Le certificat sera valide pour 10 jours
		Calendar expiry = Calendar.getInstance();
		Date startDate = expiry.getTime();
		expiry.add(Calendar.DAY_OF_YEAR, validityDays);
		Date expiryDate = expiry.getTime();
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		
		// On le positionne dans le futur certificat
		seqnum=seqnum.add(BigInteger.ONE);
		certGen.setSerialNumber(seqnum);
		
		// Le nom du proprietaire et du certificateur :
		// ici, les memes car auto-signe.
		X500Principal cnNameSubject = new X500Principal("CN="+subject);
		X500Principal cnNameIssuer = new X500Principal("CN="+issuer);
		certGen.setSubjectDN(cnNameSubject);
		certGen.setIssuerDN(cnNameIssuer);
		
		// L'algorithme de signature utilise pour la certification
		certGen.setSignatureAlgorithm("sha1WithRSA");
		
		// La cle a certifier
		certGen.setPublicKey(pubkey);
		
		// On calcule le certificat au format X509 !
		try {
			this.x509 = certGen.generate(privkey, "BC");
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean verifCertif (PublicKey pubkey) // méthode de vérification d'un certificat
	{
		try {
			this.x509.verify(pubkey);
			return true;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public void affichage() // affichage des données du certificat
	{
		System.out.println("\tVersion : "+this.x509.getVersion());
		System.out.println("\tSerialNumber : "+this.x509.getSerialNumber());
		System.out.println("\tIssuerDN : "+this.x509.getIssuerDN());
		System.out.println("\tStart Date : "+this.x509.getNotBefore());
		System.out.println("\tFinal Date : "+this.x509.getNotAfter());
		System.out.println("\tSubjectDN : "+this.x509.getSubjectDN());
		System.out.println("\tPublicKey : "+this.x509.getPublicKey());
		System.out.println("\tSignature Algorithm : "+this.x509.getSigAlgName()
				);
		BigInteger bi = new BigInteger(this.x509.getSignature());
		String signature = bi.toString(16);
		System.out.println("\tSignature : "+signature);
	}
}