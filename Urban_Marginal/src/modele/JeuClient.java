package modele;

import controleur.Global;
import javax.swing.JPanel;

import controleur.Controle;
import outils.connexion.*;

/**
 * Gestion du jeu c�t� client
 *
 */
public class JeuClient extends Jeu implements Global {

	private Connection connection;
	private Boolean murOk = false;

	/**
	 * Controleur
	 */
	public JeuClient(Controle controle) {
		super.controle = controle;
	}

	@Override
	public void connexion(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void reception(Connection connection, Object info) {
		if (info instanceof JPanel) {
			if (!this.murOk) {
				this.controle.evenementJeuClient(AJOUTPANELMURS, info);
				this.murOk = true;
			}
			else {
				this.controle.evenementJeuClient(MODIFPANELJEU, info);
			}
		}else if(info instanceof String) {
			this.controle.evenementJeuClient(MODIFTCHAT, info);
		}else if(info instanceof Integer) {
			this.controle.evenementJeuClient(JOUESON, info);
		}
	}

	@Override
	public void deconnexion() {
	}

	/**
	 * Envoi d'une information vers le serveur fais appel une fois � l'envoi dans la
	 * classe Jeu
	 */
	public void envoi(String info) {
		super.envoi(this.connection, info);
	}

}
