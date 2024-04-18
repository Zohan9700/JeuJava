package controleur;

import vue.Arene;
import modele.JeuClient;
import modele.JeuServeur;

import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.Jeu;
import vue.ChoixJoueur;
import outils.connexion.*;
import outils.connexion.AsyncResponse;
import outils.connexion.Connection;
import outils.connexion.ServeurSocket;
import vue.EntreeJeu;

/**
 * Contrôleur et point d'entrée de l'applicaton
 * 
 * @author emds
 *
 */
public class Controle implements AsyncResponse, Global {

	private EntreeJeu frmEntreeJeu;
	private Arene frmArene;
	private ChoixJoueur frmChoixJoueur;
	private Jeu leJeu;

	/**
	 * Méthode de démarrage
	 * 
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
	}

	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}

	/**
	 * Methode evenement entre jeu
	 */
	public void evenementEntreeJeu(String info) {
		if (info.equals(SERVEUR)) {
			new ServeurSocket(this, PORT);
			this.leJeu = new JeuServeur(this);
			this.frmEntreeJeu.dispose();
			this.frmArene = new Arene(this, SERVEUR);
			((JeuServeur) this.leJeu).constructionMurs();
			this.frmArene.setVisible(true);
		} else {
			new ClientSocket(this, info, PORT);
		}
	}

	/**
	 * Information provenant de la vue choixJoueur
	 * 
	 * @param pseudo   le pseudo du joueur
	 * @param numPerso le numero du personnage choisi par le joueur
	 */
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		this.frmChoixJoueur.dispose();
		this.frmArene.setVisible(true);
		((JeuClient) this.leJeu).envoi(PSEUDO + STRINGSEPARE + pseudo + STRINGSEPARE + numPerso);
	}

	public void evenementArene(Object info) {
		if (info instanceof String) {
			((JeuClient) this.leJeu).envoi(TCHAT + STRINGSEPARE + info);
		}else if (info instanceof Integer) {
			((JeuClient)this.leJeu).envoi(ACTION+STRINGSEPARE+info);
		}
	}

	/**
	 * methode evenement jeu serveur
	 */
	public void evenementJeuServeur(String ordre, Object info) {
		switch (ordre) {
		case AJOUTMUR:
			frmArene.ajoutMur(info);
			break;
		case AJOUTPANELMURS:
			this.leJeu.envoi((Connection) info, this.frmArene.getJpnMurs());
			break;
		case AJOUTJLABELJEU:
			this.frmArene.ajoutJLabelJeu((JLabel) info);
			break;
		case MODIFPANELJEU:
			this.leJeu.envoi((Connection) info, this.frmArene.getJpnJeu());
			break;
		case AJOUTPHRASE:
			this.frmArene.ajoutTchat((String) info);
			((JeuServeur) this.leJeu).envoi(this.frmArene.getTxtChat());
			break;
		}
	}

	/**
	 * Demande provenant de JeuClient
	 * 
	 * @param ordre, ordre à exécuter
	 * @param info,  information à traiter
	 */
	public void evenementJeuClient(String ordre, Object info) {
		switch (ordre) {
		case AJOUTPANELMURS:
			this.frmArene.setJpnMurs((JPanel) info);
			break;
		case MODIFPANELJEU:
			this.frmArene.setJpnJeu((JPanel) info);
			break;
		case MODIFTCHAT:
			this.frmArene.setTxtChat((String) info);
			break;
		}
	}

	/**
	 * Envoi d'information vers l'ordinateur distant
	 * 
	 * @param connection objet de connexion pour l'envoi vers l'ordinateur distant
	 * @param info       information à envoyer
	 */
	public void envoi(Connection connection, Object info) {
		connection.envoi(info);
	}

	@Override
	public void reception(Connection connection, String ordre, Object info) {
		// TODO Auto-generated method stub
		switch (ordre) {
		case CONNEXION:
			if (!(this.leJeu instanceof JeuServeur)) {
				this.leJeu = new JeuClient(this);
				this.leJeu.connexion(connection);
				this.frmEntreeJeu.dispose();
				this.frmArene = new Arene(this, CLIENT);
				this.frmChoixJoueur = new ChoixJoueur(this);
				this.frmChoixJoueur.setVisible(true);
			} else {
				this.leJeu.connexion(connection);
			}
			break;
		case RECEPTION:
			this.leJeu.reception(connection, info);
			break;
		case DECONNEXION:
			break;
		}
	}

}
