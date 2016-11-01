import java.io.Serializable;
import java.security.PublicKey;


public class InfoEquipement implements Serializable{
	private PublicKey maClePub;
	private String monNom;
	
	public InfoEquipement (String monNom, PublicKey pub) // type utilisé pour les envoies d'informations, nom+clé publique
	{
		this.maClePub=pub;
		this.monNom=monNom;
	}
	
	public PublicKey maClePub()
	{
		return this.maClePub;
	}
	public String monNom()
	{
		return this.monNom;
	}
	public String toString()
	{
		return ""+this.monNom + "\n" + this.maClePub;
	}
	public boolean equals(InfoEquipement other)
	{
		if(this.monNom.equals(other.monNom()) && this.maClePub.equals(other.maClePub()))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public int hashCode()
	{
		return monNom().hashCode()+maClePub().hashCode();
	}

}
