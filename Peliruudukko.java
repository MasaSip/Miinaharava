import java.awt.Point;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 
 * Peliruudukko vastaa loogisesta toteutuksesta. 
 * Tietää missä miinat ja missä ei.
 * (leveys, korkeus) , (0,0) on vasen yläkulma.
 * 
 * metodeihin ei ole merkitty, että ne heittävät poikkeuksia, koska poikkeuksia
 * ei tulisi normaalitilanteessa esiintyä
 * 
 * @author 345707
 *
 */
public class Peliruudukko {
	
	//miinojen arvonta on vakavaa puuhaa
	private static SecureRandom secuRnd = new SecureRandom();
	//jos miinoja on kuitenkin kalan paljon:
	private static Random rnd = new Random();
	
	public static final int OLI_JO_AUKI = -3;
	
	public static final int OLI_LIPUTETTU = -2;
	
	public static final int OLI_MIINA = -1;
	
	private Peliruutu ruudut[][];
	
	
	/**
	 * true, jos peli on päättynyt.
	 */
	private boolean pelipaattynyt;
	
	public Peliruudukko(int leveys, int korkeus, int miinoja) {
		this.pelipaattynyt = false;
		
		//mikäli jokin parametreistä ei ollut 1 tai suurempi, se saa arvon 1
		if (leveys < 1){
			leveys = 1;
		}
	
		if (korkeus < 1){
			korkeus = 1;
		}
		
		
		//oltava vähintään yksi miina
		if (miinoja < 1){
			miinoja = 1;
		}
		
		//Minoja voi olla korkeintaa puolet kentästä
		double ruutujenMaara = leveys*korkeus;
		if ((miinoja/ruutujenMaara) > 0.5){
			miinoja = (int) (0.5*ruutujenMaara);
		}
	

		//luodaan Peliruudut
		this.ruudut = new Peliruutu[leveys][korkeus];
		for (int i = 0; i < leveys; i++){
			for (int j = 0; j < korkeus; j++){
				ruudut[i][j] = new Peliruutu();
			}
		}
		
		
		//arvotaan miinat
		
		for (int i = 0; i < miinoja; i++){
			
			Point miinanSijainti;
			if (miinoja < 50){
				miinanSijainti = this.arvoMiinalleSijainti(true);
			}
			else {
				miinanSijainti = this.arvoMiinalleSijainti(false);
			}
			this.ruudut[miinanSijainti.x][miinanSijainti.y].asetaMiina();
			
		
		}
	
		
	}
	
	
	/**
	 * 
	 * @param turvallinen jos true, käytetään SecureRandom:ia, 
	 * jos false, käytetään tavallista Random:ia        
	 * @return satunnainen koordinaatti, jossa ei ole vielä miinaa
	 */
	public Point arvoMiinalleSijainti(boolean turvallinen){
		int miinaX;
		int miinaY;
		int leveys = this.annaLeveys();
		int korkeus = this.annaKorkeus();
				
		if (turvallinen){				
			miinaX = secuRnd.nextInt(leveys);
			miinaY = secuRnd.nextInt(korkeus);
		}
		else {
			miinaX = rnd.nextInt(leveys);
			miinaY = rnd.nextInt(korkeus);
		}
		if (this.ruudut[miinaX][miinaY].onMiina()){
			return this.arvoMiinalleSijainti(turvallinen);
		}
		Point miinanSijainti = new Point(miinaX, miinaY);
		return miinanSijainti;

		
	}
	
	public int annaLeveys(){
		return this.ruudut.length;
		
	}
	
	public int annaKorkeus(){
		return this.ruudut[0].length;
	}
	
	
	public boolean onLiputettu(int x, int y){
		return this.ruudut[x][y].onLiputettu();
	}
	
	public boolean onAuki(int x, int y){
		return this.ruudut[x][y].onAuki();
	}
	
	public boolean onMiina(int x, int y){
		return this.ruudut[x][y].onMiina();
	}
	 
	/**
	 * ruutu ei aukea jos se oli jo auki tai se oli liputettu
	 * aukeaa jos oli miina tai liputtamaton kaivamaton
	 * @param x
	 * @param y
	 * @return vihjenumero tai ruudun tilaa kuvaava negatiivinen vakioarvo
	 */
	public int avaa(int x, int y){
		Peliruutu ruutu = this.ruudut[x][y];
		
		if (ruutu.onAuki()){
			return Peliruudukko.OLI_JO_AUKI;
		}
		
		if (ruutu.onLiputettu()){
			return Peliruudukko.OLI_LIPUTETTU;
		}
		
		ruutu.avaa();

		if (ruutu.onMiina()){
			return Peliruudukko.OLI_MIINA;
		}
		
		return this.annaVihjenumero(x, y);
	}
	
	/**
	 * Jos lippu on entuudestaan, se otetaan pois. Jos ei lippua, se asetetaan
	 * @param sijainti Point
	 */
	public void muutaLiputus(Point sijainti){
		boolean lippu = this.onLiputettu(sijainti.x, sijainti.y);
		this.asetaLippu(sijainti.x, sijainti.y, !lippu);
	}
	
	/**
	 * tässä ei käytetä Point:ia koska Peliruudukon javadoceissa määriteltiin
	 * toisin
	 * @param x
	 * @param y
	 * @param lippu
	 * @return
	 */
	public boolean asetaLippu(int x, int y, boolean lippu){
		Peliruutu ruutu = this.ruudut[x][y];
		if (lippu != ruutu.onLiputettu() && !ruutu.onAuki()){
			ruutu.asetaLippu(lippu);
			return true;
		}
		return false;
	}
	

	/**
	 * Laskee vihjenumeron Peliruudukko.avaa(int, int) metodia varten
	 * @param x sijainti
	 * @param y sijainti
	 * @return vihjenumero
	 */
	public int annaVihjenumero(int x, int y) {
		int vihje = 0;
		//x:n ja sen hetkisen i:n summa
		int xi;
		//y:n ja sen hetkisen j:n summa
		int yj;
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				xi = x + i;
				yj = y + j;
				if (xi < 0 || xi >= this.annaLeveys() ||
						yj < 0 || yj >= this.annaKorkeus()){
					/* Lopettaa tämän kierroksen for-loopissa. break menee 
					 * kokonaan ulos, mutta continue jatkaa yhtä isommalla 
					 * arvolla
					 */
					continue;
				}
				if (ruudut[xi][yj].onMiina()){
					vihje++;
				}
				
			}
		}
		
		/* ehkä hiukan turha, muttakorjataan miinan vihjenumero oikeaksi, kerta 
		 * sitä tehtävän annossa vaadittiin.
		 */
		if (this.ruudut[x][y].onMiina()){
			vihje--;
		}
		
		return vihje;
	}
	
	/**
	 * kutsutaan kun peli voitetaan tai hävitään
	 */
	public void asetaPeliPaattyneeksi(){
		this.pelipaattynyt = true;
	}
	
	/**
	 * 
	 * @return true jos peli on päättynyt.
	 */
	public boolean onkoPeliPaattynyt(){
		return this.pelipaattynyt;
	}
	/**
	 * 
	 * @return true jos ja vain jos miinat piilossa ja muut auki
	 */
	public boolean peliVoitettu(){
		for (int x = 0; x < this.annaLeveys(); x++){
			for (int y = 0; y < this.annaKorkeus(); y++){
				if (this.onAuki(x, y) == this.onMiina(x, y)){
					return false;
				}

			}
		}

		return true;
		
		
	}
	/*
	public static void main(String[] args){
		Peliruudukko p = new Peliruudukko(-1,10, 8);
		System.out.println(p.annaLeveys());
		System.out.println(p.annaKorkeus());
		
		
		System.out.println(
		p.ruudut[0][3].onMiina());
		System.out.println(p.annaVihjenumero(0, 3));
	}
	*/

}
