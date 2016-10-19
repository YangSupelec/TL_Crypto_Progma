import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	protected String monNom; // Identite de l’equipement.
	protected int monPort; // Le numéro de port d’ecoute.
	private ArrayList<Certificat> ca; 
	private HashMap<String, PublicKey> da;

	Equipement (String nom, int port) throws Exception 
	{
		// Constructeur de l’equipement identifie par nom
		// et qui « écoutera » sur le port port.
		this.monNom=nom;
		this.monPort=port;
		this.maCle= new PaireClesRSA();
		this.monCert=new Certificat(this.monNom, this.monNom, this.maClePub(), this.maCle.Privee(), 10);
		this.autoVerif();
		this.affichage();
		this.ca=new ArrayList<Certificat>();
		this.da=new HashMap<String, PublicKey>();
	}

	public void affichage_da() 
	{
		// Affichage de la liste des équipements de DA.
		for (Entry<String, PublicKey> entry : this.da.entrySet())
		{
			System.out.println("Nom de l'equipement : "+entry.getKey());
			System.out.println("Cle publique : "+ entry.getValue());
			System.out.println("___________________");
		}
	}

	public void affichage_ca() 
	{
		// Affichage de la liste des équipements de CA.
		for (Certificat certif : this.ca)
		{
			System.out.println("Nom de l'equipement : "+certif.x509.getIssuerDN().getName().substring(3, certif.x509.getIssuerDN().getName().length()));
			System.out.println("Valable jusque : " + certif.x509.getNotAfter());
			System.out.println("___________________\n");
		}
	}

	public void affichage() 
	{
		// Affichage de l’ensemble des informations
		// de l’équipement.
		System.out.println("Cle Publique de "+this.monNom()+": Sun RSA public key, 512 bits");
		System.out.println(this.maClePub());
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
	public PrivateKey maClePriv()
	{
		return this.maCle.Privee();
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
	public int port()
	{
		return this.monPort;
	}
	
	public void ajoutCA(Certificat certif)
	{
		for (Certificat certificat : this.ca)
		{
			if(certif.x509.getIssuerDN().equals(certif.x509.getIssuerDN()))
			{
				this.ca.remove(certificat);
			}
		}
		this.ca.add(certif);
	}
	public void sync()
	{
		
	}

}