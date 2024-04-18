package vue;

import controleur.Controle;
import controleur.Global;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Arene extends JFrame implements Global {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel jpnMurs;
	private JPanel jpnJeu;
	private JTextField txtSaisie;
	private JTextArea txtChat;
	private Boolean client;
	private Controle controle;

	/**
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { Arene frame = new Arene();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	public void setJpnMurs(JPanel jpnMurs) {
		this.jpnMurs.add(jpnMurs);
		this.jpnMurs.repaint();
	}

	public JPanel getJpnMurs() {
		return jpnMurs;
	}

	public JPanel getJpnJeu() {
		return jpnJeu;
	}

	public void setJpnJeu(JPanel jpnJeu) {
		this.jpnJeu.removeAll();
		this.jpnJeu.add(jpnJeu);
		this.jpnJeu.repaint();
		this.contentPane.requestFocus();
	}

	public String getTxtChat() {
		return txtChat.getText();
	}

	public void setTxtChat(String txtChat) {
		this.txtChat.setText(txtChat);
		this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());
	}

	public void ajoutMur(Object unMur) {
		jpnMurs.add((JLabel) unMur);
		jpnMurs.repaint();
	}

	public void ajoutTchat(String phrase) {
		this.txtChat.setText(this.txtChat.getText() + phrase + "\r\n");
		this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());
	}

	public void ajoutJLabelJeu(JLabel unJLabel) {
		this.jpnJeu.add(unJLabel);
		this.jpnJeu.repaint();
	}

	public void txtSaisie_KeyPressed(KeyEvent e) {
		// si validation
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// si la zone de saisie n'est pas vide
			if(!this.txtSaisie.getText().equals("")) {
				this.controle.evenementArene(this.txtSaisie.getText());
				this.txtSaisie.setText("");
			}
			this.contentPane.requestFocus();
		}
	}
	
	public void contentPane_KeyPressed(KeyEvent e) {
		int touche = -1;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT :
		case KeyEvent.VK_RIGHT :
		case KeyEvent.VK_UP :
		case KeyEvent.VK_DOWN :
			touche = e.getKeyCode();
			break;
		}
		// si touche correcte, alors envoi de sa valeur
		if(touche != -1) {
			this.controle.evenementArene(touche);
		}
	}

	/**
	 * Create the frame.
	 */
	public Arene(Controle controle, String typeJeu) {
		this.client = typeJeu.equals(CLIENT);
		this.getContentPane().setPreferredSize(new Dimension(LARGEURARENE, HAUTEURARENE + 25 + 140));
		this.pack();
		// interdiction de changer taille
		this.setResizable(false);

		setTitle("Arena");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				contentPane_KeyPressed(e);
			}
		});
		
		setContentPane(contentPane);
		contentPane.setLayout(null);

		jpnJeu = new JPanel();
		jpnJeu.setBounds(0, 0, LARGEURARENE, HAUTEURARENE);
		jpnJeu.setOpaque(false);
		jpnJeu.setLayout(null);
		contentPane.add(jpnJeu);

		jpnMurs = new JPanel();
		jpnMurs.setBounds(0, 0, LARGEURARENE, HAUTEURARENE);
		jpnMurs.setOpaque(false);
		jpnMurs.setLayout(null);
		contentPane.add(jpnMurs);

		if (this.client) {
			txtSaisie = new JTextField();
			txtSaisie.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					txtSaisie_KeyPressed(e);
				}

			});
			txtSaisie.setBounds(0, 600, 800, 25);
			contentPane.add(txtSaisie);
			txtSaisie.setColumns(10);
		}
		
		JScrollPane jspChat = new JScrollPane();
		jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspChat.setBounds(0, 625, 800, 140);
		contentPane.add(jspChat);
		
		txtChat = new JTextArea();
		txtChat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				contentPane_KeyPressed(e);
			}
		});
		txtChat.setEditable(false);
		jspChat.setViewportView(txtChat);

		JLabel lblNewLabel = new JLabel("");
		URL resource = getClass().getClassLoader().getResource(FONDARENE);
		lblNewLabel.setIcon(new ImageIcon(resource));
		lblNewLabel.setBounds(0, 0, 800, 600);
		contentPane.add(lblNewLabel);
		
		//récupération de l'instance de Controle
		this.controle = controle;
	}

}
