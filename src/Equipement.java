import java.security.*;
import java.util.HashMap;

public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	private String monNom; // Identite de l’equipement.
	private int monPort; // Le numéro de port d’ecoute.
	private HashMap<String, PublicKey> ca; 
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
		this.ca=new HashMap<String, PublicKey>();
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
}