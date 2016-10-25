import java.security.*;
import java.util.HashSet;


public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	protected String monNom; // Identite de l’equipement.
	protected int monPort; // Le numéro de port d’ecoute.
	private HashSet<Certificat> ca; 
	private HashSet<Certificat> da;

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
		this.ca=new HashSet<Certificat>();
		this.da=new HashSet<Certificat>();
	}

	public void affichage_da() 
	{
		System.out.println("\n\t\t Liste des equipements DA :\n");
		// Affichage de la liste des équipements de DA.
		for (Certificat certif : this.da)
		{
			System.out.println("Nom de l'equipement : "+certif.x509.getSubjectDN().getName().substring(3, certif.x509.getIssuerDN().getName().length()));
			System.out.println("Valable jusque : " + certif.x509.getNotAfter());
			System.out.println("___________________\n");
		}
	}
	
	public HashSet<Certificat> monDA()
	{
		return this.da;
	}

	public void affichage_ca() 
	{
		System.out.println("\n\t\t Liste des equipements CA :\n");
		// Affichage de la liste des équipements de CA.
		for (Certificat certif : this.ca)
		{
			System.out.println("Nom de l'equipement : "+certif.x509.getSubjectDN().getName().substring(3, certif.x509.getIssuerDN().getName().length()));
			System.out.println("Valable jusque : " + certif.x509.getNotAfter());
			System.out.println("___________________\n");
		}
	}
	
	public HashSet<Certificat> monCA()
	{
		return this.ca;
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
		this.ca.add(certif);
	}
	public void supprimerDA(Certificat certif)
	{
		for (Certificat certifDA : this.da)
		{
			if(certif.equals(certifDA))
			{
				this.da.remove(certif);
			}
		}
	}
	public void sync(HashSet<Certificat> other)
	{
		boolean deja =false;
		for (Certificat certificat : other)
		{
			deja=false;
			if (!(this.monCert.equals(certificat)))
			{
				for (Certificat certifCA : this.ca)
				{
					if(certificat.equals(certifCA))
					{
						deja=true;
					}
				}
				for (Certificat certifDA : this.da)
				{
					if(certificat.equals(certifDA))
					{
						deja=true;
					}
				}
				if (deja==false)
				{
					this.da.add(certificat);
				}
			}
		}
	}

}