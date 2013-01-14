import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;



public class Miinapeli extends JFrame {
	//JFrame:n oletus layout on BorderLayout
	
	private Pelipaneeli pelipaneeli;
	private JPanel alapalkki;
	private JLabel alateksti;
	
	private int leveys = 25;
	private int korkeus = 5;
	private int miinoja = 10;
	
	public Miinapeli() {
		super("Miinaharava");
		//this.setPreferredSize(new Dimension(400,300));
		
//keskiosa		
		Peliruudukko peliruudukko = new Peliruudukko(leveys, korkeus, miinoja);
		this.pelipaneeli = new Pelipaneeli(this, peliruudukko);
		this.add(this.pelipaneeli, BorderLayout.CENTER);
		
//Menu		
		JMenuBar menupalkki = new JMenuBar();
		this.setJMenuBar(menupalkki);
		
		JMenu menu = new JMenu("Peli");
		menupalkki.add(menu);
		
		JMenuItem aloita = new JMenuItem("Aloita alusta");
		aloita.addActionListener(new aloitaAlusta());
		menu.add(aloita);
		menu.addSeparator();
		
		JMenuItem lopeta = new JMenuItem("Lopeta");
		lopeta.addActionListener(new lopeta());
		menu.add(lopeta);
		
//alapalkki		
		this.alapalkki = new JPanel();
		alapalkki.setBackground(Color.GREEN);
		this.add(alapalkki, BorderLayout.SOUTH);
		
		this.alateksti = new JLabel();
		this.alateksti.setText(" Peli on kesken");
		this.alapalkki.add(alateksti);

		this.pack();//ikkunan koko mukautuu alakompponenttien mieltymyskokoihin
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 * @return alapalkissa oleva teksti
	 */
	public String annaAlateksti(){
		return this.alateksti.getText();
	}
	/**
	 * 
	 * @param text alapalkkiin laitettava teksti
	 */
	public void asetaAlateksti(String text){
		//Font omaFontti = new Font("Comic Sans MS",Font.BOLD, 10);
		//this.alateksti.setFont(omaFontti);
		
		this.alateksti.setText(text);
	}
	
	public void asetaVoittoTeksti(){
		this.asetaAlateksti("Hihii, kutittaa. Voitit pelin!");
	}
	
	public void asetaTappioTeksti(){
	
		this.asetaAlateksti("\"Kyllä se siitä!\"");
	}
	
	public void vaihdaAlateksti(){
		String[] alaTekstit = {" Peli on kesken", 
				" Hmm.. mitähän Motivaatiovalas sanoisi tähän"};
		String nykTeksti = this.annaAlateksti();
		for (int i = 0; i < alaTekstit.length; i++){
			int seuraava = 1;
			if (nykTeksti.equals(alaTekstit[i])){
				//jos taulukon viimeinen teksti
				
				if ((i+1) == alaTekstit.length){
					seuraava -= alaTekstit.length;
				}
				this.asetaAlateksti(alaTekstit[i + seuraava]);
			
			}
		}
	}
	public void aloitaAlusta(){
		//luodaan uusi peli
		Miinapeli peli = new Miinapeli();
		//asetetaan se keskelle näyttöä
		peli.setLocationRelativeTo(null);
		//asetetaan näkyväksi
		peli.setVisible(true);
		//poistetaan edellinen peli, eli olio joka loi tään uuden olion
		this.dispose();
	}

	
	public static void main(String[] args){
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e){
			e.printStackTrace();
		}
		
		/*
		 * yllä olevan anisiosta napin oletusarvo opaquelle on true eikä false
		 */
		
		Miinapeli miinapeli = new Miinapeli();
		miinapeli.setLocationRelativeTo(null);
		miinapeli.setVisible(true);
		
		//Testi, jolla saa kaikki ruudut auki.
		/*
		int x = 0;
		int y = 0;
		while (x < miinapeli.leveys){
			System.out.println("*** " + x);
			while (y < miinapeli.korkeus){
				miinapeli.pelipaneeli.yritaAvata(new Point(x, y));
				System.out.println(x + ", " + y);
				y++;
			}
			x++;
			y = 0;
		}
		*/
	}
	
	public class lopeta implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}

	}
	
	public class aloitaAlusta implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			//Viitataan miinapeli olioon, jota käsitellään
			Miinapeli.this.vaihdaAlateksti();
			Miinapeli.this.aloitaAlusta();
			
		}
	}
	
}
