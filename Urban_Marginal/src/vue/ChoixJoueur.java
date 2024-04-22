package vue;

import outils.son.*;
import controleur.Global;
import controleur.Controle;
import vue.Arene;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.JTextField;

public class ChoixJoueur extends JFrame implements Global{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Arene frmArene;
	private Controle controle;
	private JLabel lblPersonnage;
	private static final int NBPERSOS = 3;
	private int numPerso;
	private JTextField txtPseudo;
	private Son welcome;
	private Son precedent;
	private Son suivant;
	private Son go;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChoixJoueur frame = new ChoixJoueur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	/**
	 * Méthode évenementielle
	 */
	
	private void lblPrecedent_clic() {
		numPerso = ((numPerso+1)%NBPERSOS)+1;
		affichePerso();
		precedent.play();
	}
	
	private void lblSuivant_clic() {
		numPerso = (numPerso%NBPERSOS)+1;
		affichePerso();
		suivant.play();
	}
	
	private void lblGo_clic() {
		if (txtPseudo.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "La saisie du Pseudo est obligatoire");
			txtPseudo.requestFocus();
		}
		else {
			this.controle.evenementChoixJoueur(this.txtPseudo.getText(), numPerso);
			go.play();
		}
		
	}
	
	private void affichePerso() {
		String chemin = "personnages/perso" + this.numPerso+ "marche"+1+"d"+1+".gif";
		URL resource = getClass().getClassLoader().getResource(chemin);
		this.lblPersonnage.setIcon(new ImageIcon(resource));
	}
	
	private void sourisDoigt() {
		contentPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	private void sourisNormale() {
		contentPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Create the frame.
	 */
	public ChoixJoueur(Controle controle) {
		//Dimension de la frame en fonction de son contenue
		this.getContentPane().setPreferredSize(new Dimension(400,275));
		this.pack();
		//interdiction de changer la taille
		this.setResizable(false);
		
		setTitle("Choice");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 315);
		contentPane = new JPanel();
		/*contentPane.setPreferredSize(new Dimension(9, 10));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));*/
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPersonnage = new JLabel("");
		lblPersonnage.setBounds(151, 119, 121, 122);
		lblPersonnage.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPersonnage);
		
		
		JLabel lblPrecedent = new JLabel("");
		lblPrecedent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblPrecedent_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
		});
		
		
		lblPrecedent.setBounds(71, 153, 37, 40);
		contentPane.add(lblPrecedent);
		
		JLabel lblSuivant = new JLabel("");
		lblSuivant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblSuivant_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
		});
		lblSuivant.setBounds(302, 153, 45, 40);
		contentPane.add(lblSuivant);
		
		JLabel lblGo = new JLabel("");
		lblGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblGo_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormale();
			}
		});
		lblGo.setBounds(323, 204, 66, 64);
		contentPane.add(lblGo);	
		
		txtPseudo = new JTextField();
		txtPseudo.setBounds(151, 251, 121, 17);
		contentPane.add(txtPseudo);
		txtPseudo.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 10, 408, 268);
		lblNewLabel.setIcon(new ImageIcon(ChoixJoueur.class.getResource("/fonds/fondchoix.jpg")));
		contentPane.add(lblNewLabel);
		
		//récuperation instance de Controle
		this.controle = controle;
		
		//affichage du premier perso
		this.numPerso = 1;
		this.affichePerso();
		
		//récupération des sons
		precedent = new Son(getClass().getClassLoader().getResource(SONPRECEDENT));
		suivant = new Son(getClass().getClassLoader().getResource(SONSUIVANT));
		go = new Son(getClass().getClassLoader().getResource(SONGO));
		welcome = new Son(getClass().getClassLoader().getResource(SONWELCOME));
		welcome.play();
		
		//positionnement sur la zone de saisie
		txtPseudo.requestFocus();
		
		
	}
}
