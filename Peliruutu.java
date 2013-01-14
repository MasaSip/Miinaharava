/**
 * Peliruutu sŠilyttŠŠ tietoja, ettŠ onko hŠn: miina, liputettu tai auki
 * 
 * @author 345707
 *
 */
public class Peliruutu {
	
	private boolean onLiputettu;
	private boolean onAuki;
	private boolean onMiina;
	
	/**
	 * Oletuksena ruutu on kiinni ja ei ole liputettu
	 * @param onMiina true jos ruudussa on miina
	 */
	public Peliruutu() {
		this.onLiputettu = false;
		this.onAuki = false;
		this.onMiina = false;
	}
	
	public void asetaMiina(){
		this.onMiina = true;
	}
	
	public boolean onMiina(){
		return this.onMiina;
	}
	
	/**
	 * 
	 * @param lippu jos true, asetetaan lippu, jos false, poistetaan lippu
	 */
	public void asetaLippu(boolean lippu){
		this.onLiputettu = lippu;
	}
	
	public boolean onLiputettu(){
		return this.onLiputettu;
	}
	
	public void avaa(){
		this.onAuki = true;
	}

	public boolean onAuki(){
		return this.onAuki;
	}
	
}
