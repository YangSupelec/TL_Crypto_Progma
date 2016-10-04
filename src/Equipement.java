import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	private String monNom; // Identite de l’equipement.
	private int monPort; // Le numéro de port d’ecoute.
	private HashMap<Equipement,Certificat> ca; 
	private HashMap<String, PublicKey> da;
	
	Equipement (String nom, int port) throws Exception 
	{
		// Constructeur de l’equipement identifie par nom
		// et qui « écoutera » sur le port port.
		this.monNom=nom;
		this.monPort=port;
		this.maCle= new PaireClesRSA();
		this.monCert=new Certificat(this.monNom, this.maCle, 10);
		this.autoVerif();
		this.affichage();
		this.ca=new HashMap<Equipement,Certificat>();
		this.da=new HashMap<String, PublicKey>();
	}
	
	public void affichage_da() 
	{
// Affichage de la liste des équipements de DA.
	}
	
	public void affichage_ca() 
	{
	// Affichage de la liste des équipements de CA.
	}
	
	public void affichage() 
	{
	// Affichage de l’ensemble des informations
	// de l’équipement.
		System.out.println("Cle Publique de "+this.monNom()+": Sun RSA public key, 512 bits");
		System.out.println("modulus : "+this.maClePub());
		System.out.println("Certificat de "+this.monNom());
		this.monCert.affichage();
	}
	
	public String monNom()
	{
		// Recuperation de l’identite de l’équipement.
		return this.monNom;
	}
	
	public PublicKey maClePub() 
	{
		// Recuperation de la clé publique de l’équipement.
		return this.maCle.Publique();
	}
	
	public Certificat monCertif() 
	{
		// Recuperation du certificat auto-signé.
		return this.monCert;
	}
	
	public void autoVerif()
	{
		if (this.monCertif().verifCertif(maClePub()))
		{
			System.out.println("Certificat autosigné vérifié.");
		}
		else
		{
			System.out.println("Certificat autosigné non vérifié.");
		}
	}
	private void certification(Equipement other)
	{
		// other possède un certificat de this portant sur la clé publique de other
		Certificat certif = new Certificat(this.monNom(), other.monNom(), other.maClePub(), this.maCle.Privee(), 10);
		// on vérifie
		if (certif.verifCertif(this.maClePub())) // si le certificat est vérifié, on ajoute this au CA de other
		{
			other.ajoutCA(this, certif);
			System.out.println("Le certificat est valide.");
		}
		else
		{
			System.out.println("Le certificat n'est pas valide.");
		}
	}
	public void ajoutCA(Equipement other, Certificat certif)
	{
		// ajoute dans le CA de this, other
		this.ca.put(other, certif);
	}

	
}