import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;


public class Ruutunappi extends JButton {
	private Point sijainti;
	
	public Ruutunappi(Point sijainti){
		this.sijainti = sijainti;
		this.asetaOletusTaustaVari();
		Border ylataso = BorderFactory.createRaisedBevelBorder();
		this.setBorder(ylataso);
		
	}
	
	/**
	 * Oletustaustaväri on vaaleanharmaa
	 */
	public void asetaOletusTaustaVari(){
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	/**
	 * 
	 * @return ruudun sijainti
	 */
	public Point annaSijainti(){
		return this.sijainti;
	}
	
	
	/**
	 * Miina piirretään näkyviin. Reunat tekevät ruudun syvennetyn näköiseksi
	 */
	public void naytaMiina(){
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		ImageIcon miinakuva 
			= new ImageIcon(getClass().getResource("/pommi.gif"));
		JLabel kuva = new JLabel(miinakuva);
		this.add(kuva);
		//this.setText("@");
	}
	
	/**
	 * Piirtää vihjenumeron. Jos virhe numero == 0, ruutu on tyhjä. Reunat 
	 * tekevät ruudun syvennetyn näköiseksi
	 * @param vihjeNro ruudun vihjenumero
	 */
	public void naytaVihje(int vihjeNro){
		Color vihjeVari[] = {
				Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, 
				Color.CYAN, Color.BLACK, Color.BLACK, Color.BLACK 
		};
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLoweredBevelBorder());

		if (vihjeNro == 0){
			return;
		}
		Font omaFontti = new Font("Comic Sans MS",Font.BOLD, 16);
		this.setFont(omaFontti);
		this.setText(Integer.toString(vihjeNro));
		this.setForeground(vihjeVari[vihjeNro-1]);
		}
	
	public void naytaLippu(boolean lippu){
		ImageIcon lippukuva 
			= new ImageIcon(getClass().getResource("/lippu.gif"));
		
		if (lippu){
			this.add(new JLabel(lippukuva));
			//this.setBackground(Color.ORANGE);
			this.setText(".");
			return;
		}
		this.removeAll();
		this.asetaOletusTaustaVari();
		this.setText("");	
	}
	
	public void asetaTaustaVari(Color vari){
		this.setBackground(vari);
	}
	
	public void naytaVaaraLippu(){
		ImageIcon vaaraLippu = new ImageIcon("huti.gif");
		this.add(new JLabel(vaaraLippu));
	}
	
}
