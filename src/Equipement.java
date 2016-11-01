import java.security.*;
import java.util.HashSet;


public class Equipement 
{
	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	protected String monNom; // Identite de l’equipement.
	protected int monPort; // Le numéro de port d’ecoute.
	private HashSet<InfoEquipement> ca; // ensemble des Autorités de Certification reconnues par this
	private HashSet<InfoEquipement> da; // ensemble des Autorités dérivées reconnues par this
	private InfoEquipement mesInfos; // objet contenant le nom et la clé publique de this
	Equipement (String nom, int port) throws Exception 
	{
		// Constructeur de l’equipement identifie par nom
		// et qui « écoutera » sur le port port.
		this.monNom=nom;
		this.monPort=port;
		this.maCle= new PaireClesRSA(); // génération des clés publiques et privées
		this.monCert=new Certificat(this.monNom, this.monNom, this.maClePub(), this.maCle.Privee(), 10); // création du certificat autosigné
		this.autoVerif(); // vérification du certificat autosigné
		this.affichage(); // affichage du certificat autosigné
		this.ca=new HashSet<InfoEquipement>(); // initialisation du CA
		this.da=new HashSet<InfoEquipement>(); // initialisation du DA
		this.mesInfos = new InfoEquipement(this.monNom, this.maClePub()); // création de l'objet InfoEquipement
	}
	public InfoEquipement mesInfos() // retourne les informations de l'équipement, qui sera envoyé 
	{
		return this.mesInfos;
	}

	public void affichage_da() 
	{
		System.out.println("\n\t\t Liste des equipements DA :\n");
		// Affichage de la liste des équipements de DA.
		if (this.da.isEmpty())
		{
			System.out.println("Aucun équipement dans le DA.");
		}
		for (InfoEquipement certif : this.da)
		{
			System.out.println("Nom de l'equipement : "+certif.monNom());
		}
		System.out.println("\n");
	}
	
	public HashSet<InfoEquipement> monDA()
	{
		return this.da;
	}

	public void affichage_ca() 
	{
		System.out.println("\n\t\t Liste des equipements CA :\n");
		// Affichage de la liste des équipements de CA.
		if (this.ca.isEmpty())
		{
			System.out.println("Aucun équipement dans le CA.");
		}
		for (InfoEquipement certif : this.ca)
		{
			System.out.println("Nom de l'equipement : "+certif.monNom());
		}
		System.out.println("\n");
	}
	
	public HashSet<InfoEquipement> monCA()
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
		// Recuperation de la clé privée de l'équipement.
		return this.maCle.Privee();
	}

	public Certificat monCertif() 
	{
		// Recuperation du certificat auto-signé.
		return this.monCert;
	}

	public void autoVerif() // vérification de certificat
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
		// récupère le port de l'équipement
		return this.monPort;
	}
	
	public void ajoutCA(InfoEquipement infos) // ajouter un équipement au CA, on retient ses "informations" (son Nom et sa clé publique)
	{
		boolean test=false;
		for (InfoEquipement inf : this.ca) // on parcourt tous les équipement du CA
		{
			if(inf.equals(infos)) // si l'équipement en paramètre y est déjà, on ne l'ajoute pas
			{
				test=true;
			}
		}
		if (test==false)
		{
			this.ca.add(infos); // sinon on l'ajoute
		}
	}
	public void supprimerDA(InfoEquipement infos) // supprimer du DA l'équipement passé en paramètre 
	{
		HashSet<InfoEquipement> temp = new HashSet<InfoEquipement>();
		for(InfoEquipement inf : this.da) // on recopie le HashSet sans mettre l'équipement en paramètre (à supprimer)
		{
			if (!inf.equals(infos))
			{
				temp.add(inf);
			}
		}
		this.da=temp;
	}
	public void sync(HashSet<InfoEquipement> other) // si l'équipement en paramètre n'est pas l'équipement lui même et n'est ni dans son CA ou DA alors on l'ajoute dans son DA
	{
		boolean test = false;
		for (InfoEquipement infosOther : other)
		{
			test =false;
			if (this.mesInfos.equals(infosOther))
			{
				test=true;
			}
			else
			{
				for (InfoEquipement inf : this.da)
				{
					if(inf.equals(infosOther))
					{
						test=true;
					}
				}
				for (InfoEquipement inf : this.ca)
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
	public boolean estDansDACA(InfoEquipement other)
	{
		boolean flag=false;
		for(InfoEquipement inf : this.ca)
		{
			if(inf.equals(other))
			{
				flag=true;
			}
		}
		for(InfoEquipement inf : this.da)
		{
			if(inf.equals(other))
			{
				flag=true;
			}
		}
		return flag;
	}
}