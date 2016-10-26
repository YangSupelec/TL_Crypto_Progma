import java.io.Serializable;
import java.security.PublicKey;


public class NomPubKey implements Serializable{
	private PublicKey maClePub;
	private String monNom;
	
	public NomPubKey (String monNom, PublicKey pub)
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
	public boolean equals(NomPubKey other)
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
