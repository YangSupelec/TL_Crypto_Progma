import java.security.*;
import java.util.HashSet;


public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	protected String monNom; // Identite de l’equipement.
	protected int monPort; // Le numéro de port d’ecoute.
	private HashSet<NomPubKey> ca; 
	private HashSet<NomPubKey> da;
	private NomPubKey mesInfos;
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
		this.ca=new HashSet<NomPubKey>();
		this.da=new HashSet<NomPubKey>();
		this.mesInfos = new NomPubKey(this.monNom, this.maClePub());
	}
	public NomPubKey mesInfos()
	{
		return this.mesInfos;
	}

	public void affichage_da() 
	{
		System.out.println("\n\t\t Liste des equipements DA :\n");
		// Affichage de la liste des équipements de DA.
		for (NomPubKey certif : this.da)
		{
			System.out.println("Nom de l'equipement : "+certif.monNom());
		}
		System.out.println("\n");
	}
	
	public HashSet<NomPubKey> monDA()
	{
		return this.da;
	}

	public void affichage_ca() 
	{
		System.out.println("\n\t\t Liste des equipements CA :\n");
		// Affichage de la liste des équipements de CA.
		for (NomPubKey certif : this.ca)
		{
			System.out.println("Nom de l'equipement : "+certif.monNom());
		}
		System.out.println("\n");
	}
	
	public HashSet<NomPubKey> monCA()
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
	
	public void ajoutCA(NomPubKey infos)
	{
		
		boolean test=false;
		for (NomPubKey inf : this.ca)
		{
			if(inf.equals(infos))
			{
				test=true;
			}
		}
		if (test==false)
		{
			this.ca.add(infos);
		}
	}
	public void supprimerDA(NomPubKey infos)
	{
		
		HashSet<NomPubKey> temp = new HashSet<NomPubKey>();
		for(NomPubKey inf : this.da)
		{
			if (!inf.equals(infos))
			{
				temp.add(inf);
			}
		}
		this.da=temp;
	}
	public void sync(HashSet<NomPubKey> other)
	{
		boolean test = false;
		for (NomPubKey infosOther : other)
		{
			test =false;
			if (this.mesInfos.equals(infosOther))
			{
				test=true;
			}
			else
			{
				for (NomPubKey inf : this.da)
				{
					if(inf.equals(infosOther))
					{
						test=true;
					}
				}
				for (NomPubKey inf : this.ca)
				{
					if(inf.equals(infosOther))
					{
						test=true;
					}
				}
			}
			if (test == false)
			{
				this.da.add(infosOther);
			}
		}
	}

}