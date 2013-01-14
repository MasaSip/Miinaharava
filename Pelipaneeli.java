import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Pelipaneeli luokka vastaa graafisesta toteutuksesta.
 * (leveys, korkeus) , (0,0) on vasen ylŠkulma
 * 
 * @author 345707
 *
 */
public class Pelipaneeli extends JPanel
								implements MouseListener
	{
	
	private Miinapeli miinapeli;
	
	/**
	 * looginen osa
	 */
	private Peliruudukko peliruudukko;

	private Ruutunappi ruudukko[][];

	public Pelipaneeli(Miinapeli miinapeli, Peliruudukko peliruudukko) {
		super(new GridBagLayout());
		this.miinapeli = miinapeli;
		this.peliruudukko = peliruudukko;
	
		
		int leveys = peliruudukko.annaLeveys();
		int korkeus = peliruudukko.annaKorkeus();
		
		if (leveys < 1){
			leveys = 1;
		}
		if (korkeus < 1){
			korkeus = 1;
		}
		this.ruudukko = new Ruutunappi[leveys][korkeus];
		GridBagConstraints c = new GridBagConstraints();
		for (int x = 0; x < leveys; x++){ 
			for (int y = 0; y < korkeus; y++){

				
				c.gridx = x;
				c.gridy = y;
				Ruutunappi nappi = new Ruutunappi(new Point(x,y));
				
/*
	vaihtoehto lookAndFeel asetuksiin sorkkimiselle			 
				
				System.out.println(nappi.isOpaque());
				nappi.setOpaque(true);
				nappi.setBorderPainted(false);
*/				
						
				
				nappi.setPreferredSize(new Dimension(25, 25));
				nappi.addMouseListener(this);
				this.add(nappi, c);
				ruudukko[x][y] = nappi;
			}
		}
	
	}
	
	
	public void yritaAvata(Point sijainti){
		Ruutunappi nappi = this.ruudukko[sijainti.x][sijainti.y];
		
		if (this.peliruudukko.onAuki(sijainti.x, sijainti.y)){
			return;
		}
		if (this.peliruudukko.onLiputettu(sijainti.x, sijainti.y)){
			return;
		}
		
		int vihjeNro = this.peliruudukko.avaa(sijainti.x, sijainti.y);
		
		if (vihjeNro == Peliruudukko.OLI_MIINA){
			this.gameOver(nappi);
			return;
		}
		
		nappi.naytaVihje(vihjeNro);
		
		if (vihjeNro == 0){
			
			List<Point> naapuritLista = this.poimiNaapurit(sijainti);
			for (Point naapuri: naapuritLista){
				
				this.yritaAvata(naapuri);
			}
		}
		
		this.tarkistaVoitto();
	}
	
	/**
	 * Vain kaivatamaton ruutu voidaan liputtaa tai ottaa liputus pois
	 * @param nappi graafinen Ruutunappi, joka sijaitsee Pelipaneelissa
	 */
	public void yritaMuuttaaLiputusta(Point sijainti){
		Ruutunappi nappi = ruudukko[sijainti.x][sijainti.y];
		
		if (this.peliruudukko.onAuki(sijainti.x, sijainti.y)){
			return;
		}
		boolean lippuEntuudestaan = this.peliruudukko.onLiputettu(sijainti.x, sijainti.y);
		this.peliruudukko.muutaLiputus(sijainti);
		nappi.naytaLippu(!lippuEntuudestaan);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (this.peliruudukko.onkoPeliPaattynyt()){
			return;
		}
	
		
		int painikeNro = e.getButton();
		Ruutunappi painettuNappi = (Ruutunappi)e.getComponent();
		Point sijainti = painettuNappi.annaSijainti();
		//jos clikattiin hiiren vasemmalla
		if (painikeNro == 1){
			this.yritaAvata(sijainti);
		}
		
		//jos clikattiin hiiren oikealla
		if (painikeNro == 3){
			this.yritaMuuttaaLiputusta(sijainti);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	/**
	 * 
	 * @param sijainti, jonka naapuri sijainnit listaillaan
	 * @return lista naapuri sijainnesta
	 */
	//pointilla pelailulla ei tartte kirjotta kahtaparametriŠ erikseen
	private java.util.List<Point> poimiNaapurit(Point sijainti){
		
		List<Point> naapurit = new ArrayList<Point>();
		Point naapuri;
		int leveys = this.peliruudukko.annaLeveys();
		int korkeus = this.peliruudukko.annaKorkeus();
		
		/* for-lauseet lisŠŠvŠt listaan koordinaatit, jos se ei ole sama kuin 
		 * tŠmŠn hetkinen tai jos se on peliruudukon ulkopuolella
		 */
		for (int deltaX = -1; deltaX <= 1 ; deltaX++){
			for (int deltaY = -1; deltaY <= 1 ; deltaY++){
				naapuri = new Point(sijainti.x + deltaX, sijainti.y + deltaY);
				
				if (naapuri.equals(sijainti)){
					continue;
				}
				
				if (naapuri.x < 0 || naapuri.x >= leveys ||
						naapuri.y < 0 || naapuri.y >= korkeus){
					continue;
				}
				naapurit.add(naapuri);		
			}
		}
	
		return naapurit;
		
	}
	/**
	 * Avataan avaamatomat miinanapit.
	 * Nappi josta rŠjŠhdys alkoi, vŠrjŠtŠŠn punaiseksi.
	 * MerkitŠŠn vŠŠrin sijoitetut liput.
	 * Muutetaan alapalkin teksti kertomaan pelin pŠŠttymisestŠ.
	 * @param nappi, jonka avaamisesta pelinpŠŠttyminen johtui
	 */
	public void gameOver(Ruutunappi nappi){
		
		this.peliruudukko.asetaPeliPaattyneeksi();
		
		int leveys = this.peliruudukko.annaLeveys();
		int korkeus = this.peliruudukko.annaKorkeus();
		
		for (int x = 0; x < leveys; x++){
			for (int y = 0; y < korkeus; y++){
			
				if (this.peliruudukko.onMiina(x, y) &&
						!this.peliruudukko.onLiputettu(x, y)){
					ruudukko[x][y].naytaLippu(false);
					ruudukko[x][y].naytaMiina();					
				}
				else if(this.peliruudukko.onLiputettu(x, y)) {
					
					//ruudukko[x][y].naytaVaaraLippu();
					ruudukko[x][y].asetaTaustaVari(Color.CYAN);
				}
			}
		}
		
		nappi.asetaTaustaVari(Color.RED);
		
		this.miinapeli.asetaTappioTeksti();
	
	}
	
	/**
	 * Tarkistaa onko peli voitettu. Jos peli on voitettu eli kaikki ei-miinat
	 * on jo avattu niin merkitsee lipuilla sellaiset miinat, joita ei vielŠ
	 * ole liputettu. (MikŠli kaikkia miinoja ei vielŠ ole liputettu)
	 * Alapaneelin teksti pŠivittyy kertomaan voitosta
	 * Pelipaneeli ei enŠŠ reagoi klikkauksiin.
	 */
	public void tarkistaVoitto(){
		if (!this.peliruudukko.peliVoitettu()){
			return;
		}
		//pelipaneeliin ei voi enŠŠ koskea
		this.peliruudukko.asetaPeliPaattyneeksi();
		
		int leveys = this.peliruudukko.annaLeveys();
		int korkeus = this.peliruudukko.annaKorkeus();
		
		for (int x = 0; x < leveys; x++){
			for (int y = 0; y < korkeus; y++){
				if (this.peliruudukko.onMiina(x, y)){
					this.ruudukko[x][y].naytaLippu(true);
				}
			}
		}
		
		this.miinapeli.asetaVoittoTeksti();
		
	}

}
