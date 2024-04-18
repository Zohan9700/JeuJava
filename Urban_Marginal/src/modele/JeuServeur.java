package modele;

import controleur.Global;
import outils.connexion.*;
import controleur.Controle;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JLabel;

/**
 * Gestion du jeu c�t� serveur
 *
 */
public class JeuServeur extends Jeu implements Global {

	/**
	 * Collection de murs
	 */
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>() ;
	/**
	 * Collection de joueurs
	private ArrayList<Joueur> lesJoueurs = new ArrayList<Joueur>() ;*/
	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>();
	
	/**
	 * Constructeur
	 */
	public JeuServeur(Controle controle) {
		super.controle = controle;
	}
	
	@Override
	public void connexion(Connection connection) {
		this.lesJoueurs.put(connection, new Joueur(this));
	}

	@Override
	public void reception(Connection connection, Object info) {
		String[] infos = ((String)info).split(STRINGSEPARE);
		String ordre = infos[0];
		switch (ordre) {
		case PSEUDO:
			controle.evenementJeuServeur(AJOUTPANELMURS, connection);
			String pseudo = infos[1];
			int numPerso = Integer.parseInt(infos[2]);
			this.lesJoueurs.get(connection).initPerso(pseudo, numPerso, this.lesJoueurs.values(), this.lesMurs);
			String premierMessage = "***"+pseudo+" vient de se connecter ***";
			this.controle.evenementJeuServeur(AJOUTPHRASE, premierMessage);
			break;
		case TCHAT:
			String phrase = infos[1];
			phrase = this.lesJoueurs.get(connection).getPseudo()+" > "+phrase;
			this.controle.evenementJeuServeur(AJOUTPHRASE, phrase);
			break;
		case ACTION :
			Integer action = Integer.parseInt(infos[1]);
			this.lesJoueurs.get(connection).action(action, this.lesJoueurs.values(), this.lesMurs);
		}
	}
	
	@Override
	public void deconnexion() {
	}
	
	public void ajoutJLabelJeuArene(JLabel jLabel) {
		this.controle.evenementJeuServeur(AJOUTJLABELJEU, jLabel);
	}
	
	public void envoieJeuATous() {
		for(Connection connection : this.lesJoueurs.keySet()) {
			this.controle.evenementJeuServeur(MODIFPANELJEU, connection);
		}
	}

	/**
	 * Envoi d'une information vers tous les clients
	 * fais appel plusieurs fois � l'envoi de la classe Jeu
	 */
	public void envoi(Object info) {
		for(Connection connection : this.lesJoueurs.keySet()) {
			super.envoi(connection, info);
		}
	}

	/**
	 * G�n�ration des murs
	 */
	public void constructionMurs() {
		for (int x=0; x<NBMURS; x++) {
			this.lesMurs.add(new Mur());
			this.controle.evenementJeuServeur(AJOUTMUR, lesMurs.get(lesMurs.size()-1).getjLabel());
		}
	}
	
}
