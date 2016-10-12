package core;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	protected String monNom; // Identite de l’equipement.
	protected int monPort; // Le numéro de port d’ecoute.
	private HashMap<String,HashMap<PublicKey, Certificat>> ca; 
	private HashMap<String, PublicKey> da;

	public Equipement (String nom, int port) throws Exception 
	{
		// Constructeur de l’equipement identifie par nom
		// et qui « écoutera » sur le port port.
		this.monNom=nom;
		this.monPort=port;
		this.maCle= new PaireClesRSA();
		this.monCert=new Certificat(this.monNom, this.monNom, this.maClePub(), this.maCle.Privee(), 10);
		this.autoVerif();
		this.affichage();
		this.ca=new HashMap<String,HashMap<PublicKey,Certificat>>();
		this.da=new HashMap<String, PublicKey>();
	}

	public void affichage_da() 
	{
		// Affichage de la liste des équipements de DA.
		for (Entry<String, PublicKey> entry : this.da.entrySet())
		{
			System.out.println("Nom de l'equipement : "+entry);
			System.out.println("Cle publique : "+ this.da.get(entry));
			System.out.println("___________________");
		}
	}

	public void affichage_ca() 
	{
		// Affichage de la liste des équipements de CA.
		for (Entry<String, HashMap<PublicKey, Certificat>> entry : this.ca.entrySet())
		{
			System.out.println("Nom de l'equipement : "+entry);
			for (Entry<PublicKey, Certificat> ent : this.ca.get(entry).entrySet())
			{
				System.out.println("Cle publique : "+ ent);
			}
			System.out.println("___________________");
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
	private void certification(Equipement other)
	{
		// other possède un certificat de this portant sur la clé publique de other
		Certificat certifA = new Certificat(this.monNom(), other.monNom(), other.maClePub(), this.maCle.Privee(), 10);
		// on vérifie
		if (certifA.verifCertif(this.maClePub())) // si le certificat est vérifié, on ajoute this au CA de other
		{
			other.ajoutCA(this, certifA);
			Certificat certifB = new Certificat(other.monNom(), this.monNom(), this.maClePub(), other.maCle.Privee(), 10);
			if (certifB.verifCertif(other.maClePub()))
			{
				this.ajoutCA(other, certifB);
			}
			else
			{
				System.out.println("Le certificat n'est pas valide.");
			}
		}
		else
		{
			System.out.println("Le certificat n'est pas valide.");
		}
	}
	public void ajoutCA(Equipement other, Certificat certif)
	{
		HashMap<PublicKey, Certificat> pubCertif = new HashMap();
		pubCertif.put(other.maClePub(), certif);
		// ajoute dans le CA de this, other
		if (! this.ca.containsValue(pubCertif)) // si la valeur n'est pas déjà dans la hash map, on l'ajoute.
		{
			this.ca.put(other.monNom(), pubCertif);
		}
	}
<<<<<<< HEAD:src/Equipement.java
	public void sync()
	{
		
	}
=======
>>>>>>> c2bf34c1a44175eb19039f7d564fd91e7f1d98e0:src/core/Equipement.java

}