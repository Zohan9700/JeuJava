package vue;

import vue.Arene;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChoixJoueur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Arene frmArene;

	/**
	 * Launch the application.
	 */
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
	}
	/**
	 * Méthode évenementielle
	 */
	
	private void lblGauche_clic() {
		System.out.println("Précedent");
	}
	
	private void lblDroit_clic() {
		System.out.println("Suivant");
	}
	
	private void lblGo_clic() {
		this.frmArene = new Arene();
		this.frmArene.setVisible(true);
		this.dispose();
		
	}

	/**
	 * Create the frame.
	 */
	public ChoixJoueur() {
		/*
		this.getContentPane().setPreferredSize(new Dimension(400,275));
		this.pack();*/
		//interdiction de changer la taille
		this.setResizable(false);
		setTitle("Choice");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 315);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(9, 10));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 10, 408, 268);
		lblNewLabel.setIcon(new ImageIcon(ChoixJoueur.class.getResource("/fonds/fondchoix.jpg")));
		contentPane.add(lblNewLabel);
		
		JLabel lblGauche = new JLabel("");
		lblGauche.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblGauche_clic();
			}
		});
		
		lblGauche.setBounds(71, 153, 37, 40);
		contentPane.add(lblGauche);
		
		JLabel lblDroit = new JLabel("");
		lblDroit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblDroit_clic();
			}
		});
		lblDroit.setBounds(302, 153, 45, 40);
		contentPane.add(lblDroit);
		
		JLabel lblGo = new JLabel("");
		lblGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblGo_clic();
			}
		});
		lblGo.setBounds(323, 204, 66, 64);
		contentPane.add(lblGo);
	}
}
