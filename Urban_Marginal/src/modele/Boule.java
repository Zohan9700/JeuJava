package modele;

import controleur.Global;
import java.net.URL;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Gestion de la boule
 *
 */
public class Boule extends Objet implements Global, Runnable {

	/**
	 * instance de JeuServeur pour la communication
	 */
	private JeuServeur jeuServeur ;
	/**
	 * joueur qui lance la boule
	 */
	private Joueur attaquant;
	/**
	 * Colllection de murs
	 */
	private Collection lesMurs;
	
	/**
	 * Constructeur
	 */
	public Boule(JeuServeur jeuServeur) {
		this.jeuServeur = jeuServeur;
		super.jLabel = new JLabel();
		super.jLabel.setVisible(false);
		URL resource = getClass().getClassLoader().getResource(BOULE);
		super.jLabel.setIcon(new ImageIcon(resource));
		super.jLabel.setBounds(0, 0, LARGEURBOULE, HAUTEURBOULE);
	}
	
	/**
	 * Tire d'une boule
	 */
	public void tireBoule(Joueur attaquant, Collection lesMurs) {
		this.lesMurs = lesMurs;
		this.attaquant = attaquant;
		//positionnement de la boule
		if(attaquant.getOrientation()==GAUCHE) {
			posX = attaquant.getPosX() - LARGEURBOULE - 1;
		}else {
			posX = attaquant.getPosX() + LARGEURPERSO + 1;
		}
		posY = attaquant.getPosY() + HAUTEURPERSO/2;
		// démarrer le thread pour gérer le tir de la boule
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// envoi du son fight
		this.jeuServeur.envoi(FIGHT);
		//afficher l'attaquant à l'étape repos de la marche
		this.attaquant.affiche(MARCHE, 1);
		//rendre la boule visible
		super.jLabel.setVisible(true);
		//préparer la victime (dans le cas ou un joueur est touché)
		Joueur victime = null;
		//pas positif ou négatif (suivant l'orientation du joueur) pour faire avancer la boule
		int lePas;
		if(attaquant.getOrientation()== GAUCHE) {
			lePas = -PAS;
		}else {
			lePas = PAS;
		}
		// gestion de la trajectoire de la boule
		do {
			//la boule avance
			posX += lePas;
			jLabel.setBounds(posX, posY, LARGEURBOULE, HAUTEURBOULE);
			//envoi de la nouvelle zone de jeu à tous (pour que tous voient la boule avancer)
			this.jeuServeur.envoieJeuATous();
			//récuperer la collection actuelle de joueurs
			Collection lesJoueurs = this.jeuServeur.getLesJoueurs();
			//récuperation de l'eventuelle victime
			victime = (Joueur)super.toucheCollectionObjets(lesJoueurs);
		}while(posX>=0 && posX<=LARGEURARENE && this.toucheCollectionObjets(lesMurs)==null && victime==null);
		//vérifier s'il y a une victime et qu'elle n'est pas déjà morte
		if(victime != null && !victime.estMort()) {
			// envoi du son hurt
			this.jeuServeur.envoi(HURT);
			victime.perteVie();
			attaquant.gainVie();
			//joue l'animation de la victime blessée
			for(int k=1; k<NBETAPESTOUCHE; k++) {
				victime.affiche(TOUCHE, k);
				pause(80, 0);
			}
			// controle si la victime est morte
			if(victime.estMort()) {
				//envoie du son mort
				this.jeuServeur.envoi(DEATH);
				//joue l'animation de la mort
				for(int k=1; k<=NBETAPESMORT; k++) {
					victime.affiche(MORT, k);
					pause(80, 0);
				}
			}else {
				//remettre le joueur dans la position de repos (marche)
				victime.affiche(MARCHE, 1);
			}
		}
		//rendre à nouveau la boule invisible
		this.jLabel.setVisible(false);
		//envoyer le nouveau jeu à tous
		this.jeuServeur.envoieJeuATous();
		
	}
	
	private void pause(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		}catch(InterruptedException e) {
			System.out.println("erreur pause");
		}
	}
	
}
