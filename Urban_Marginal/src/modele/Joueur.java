package modele;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;
/**
 * Gestion des joueurs
 *
 */
public class Joueur extends Objet implements Global {
	
	/**
	 * pseudo saisi
	 */
	private String pseudo ;
	/**
	 * n� correspondant au personnage (avatar) pour le fichier correspondant
	 */
	private int numPerso ; 
	/**
	 * message qui s'affiche sous le personnage (contenant pseudo et vie)
	 */
	private JLabel message;
	/**
	 * instance de JeuServeur pour communiquer avec lui
	 */
	private JeuServeur jeuServeur ;
	/**
	 * num�ro d'�tape dans l'animation (de la marche, touch� ou mort)
	 */
	private int etape ;
	/**
	 * la boule du joueur
	 */
	private Boule boule ;
	/**
	 * vie restante du joueur
	 */
	private int vie ; 
	/**
	 * tourn� vers la gauche (0) ou vers la droite (1)
	 */
	private int orientation ;
	
	/**
	 * Constructeur : récupération de jeuServeur et initialisation de certaines propriétés
	 * @param jeuServeur instance de JeuServeur pour lui envoyer des informations
	 */
	public Joueur(JeuServeur jeuServeur) {
		this.jeuServeur = jeuServeur;
		this.vie = MAXVIE;
		this.etape = 1;
		this.orientation = DROITE;
	}
	
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Initialisation d'un joueur (pseudo et num�ro, calcul de la 1�re position, affichage, cr�ation de la boule)
	 */
	public void initPerso(String pseudo, int numPerso, Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		System.out.println("joueur"+ pseudo + " - num perso" + numPerso + "crée");
		//création du label personnage
		super.jLabel = new JLabel();
		//création du label du message sous le joueur
		this.message = new JLabel();
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setFont(new Font("Dialog", Font.PLAIN, 8));
		//calcul de la première position d'un perso
		this.premierePosition(lesJoueurs, lesMurs);
		// demande d'ajout du label perso et du message dans l'arene du serveur
		this.jeuServeur.ajoutJLabelJeuArene(jLabel);
		this.jeuServeur.ajoutJLabelJeuArene(message);
		// afficher le perso
		this.affiche(MARCHE, this.etape);
	}

	/**
	 * Calcul de la premi�re position al�atoire du joueur (sans chevaucher un autre joueur ou un mur)
	 */
	private void premierePosition(Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		jLabel.setBounds(0, 0, LARGEURPERSO, HAUTEURPERSO);
		do {
			posX = (int) Math.round(Math.random() * (LARGEURARENE - LARGEURPERSO));
			posY = (int) Math.round(Math.random() * (HAUTEURARENE - HAUTEURPERSO - HAUTEURMESSAGE));
		}while(this.toucheJoueur(lesJoueurs) || this.toucheMur(lesMurs));
	}
	
	/**
	 * Affiche le personnage et son message
	 */
	public void affiche(String etat, int etape) {
		//positionnement du personnage et affectation de la bonne image
		super.jLabel.setBounds(posX, posY, LARGEURPERSO, HAUTEURPERSO);
		String chemin = CHEMINPERSONNAGES + PERSO + this.numPerso+etat+etape+"d"+this.orientation+EXTFICHIERPERSO;
		URL resource = getClass().getClassLoader().getResource(chemin);
		super.jLabel.setIcon(new ImageIcon(resource));
		//positionnement et remplissage du message
		this.message.setBounds(posX-10, posY-HAUTEURPERSO, LARGEURPERSO+10, HAUTEURMESSAGE);
		this.message.setText(pseudo+" : "+vie);
		//demande d'envoie à tous des modification d'affichache
		this.jeuServeur.envoieJeuATous();
		
	}

	/**
	 * G�re une action re�ue et qu'il faut afficher (d�placement, tire de boule...)
	 */
	public void action(Integer action, Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		switch(action) {
		case KeyEvent.VK_LEFT :
			orientation = GAUCHE;
			posX = deplace(posX, action, -PAS, LARGEURARENE - LARGEURPERSO, lesJoueurs, lesMurs);
			break;
		case KeyEvent.VK_RIGHT :
			orientation = DROITE;
			posX = deplace(posX, action, PAS, LARGEURARENE - LARGEURPERSO, lesJoueurs, lesMurs);
			break;
		case KeyEvent.VK_UP :
			posY = deplace(posY, action, -PAS, HAUTEURARENE - HAUTEURPERSO - HAUTEURMESSAGE, lesJoueurs, lesMurs);
			break;
		case KeyEvent.VK_DOWN :
			posY = deplace(posY, action, PAS, HAUTEURARENE - HAUTEURPERSO - HAUTEURMESSAGE, lesJoueurs, lesMurs);
			break;
		}
		this.affiche(MARCHE, this.etape);
	}

	/**
	 * G�re le d�placement du personnage
	 */
	private int deplace(int position, int action, int lepas, int max, Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		int ancpos = position;
		position += lepas;
		position = Math.max(position, 0);
		position = Math.min(position, max);
		if(action==KeyEvent.VK_LEFT || action==KeyEvent.VK_RIGHT) {
			posX = position;
		}else {
			posY = position;
		}
		//controle s'il y a collision, dans ce cas, le personnage reste sur place
		if(toucheJoueur(lesJoueurs) || toucheMur(lesMurs)) {
			position = ancpos;
		}
		//passe à l'étape suivante de l'animation de la marche
		etape = (etape % NBETAPEMARCHE) + 1;
		return position;
		
	}

	/**
	 * Contr�le si le joueur touche un des autres joueurs
	 * @return true si deux joueurs se touchent
	 */
	private Boolean toucheJoueur(Collection<Joueur> lesJoueurs) {
		for(Joueur unJoueur : lesJoueurs) {
			if(!this.equals(unJoueur)) {
				if(super.toucheObjet(unJoueur)) {
					return true;
				}			
			}
		}
		return false;
	}

	/**
	* Contr�le si le joueur touche un des murs
	 * @return true si un joueur touche un mur
	 */
	private Boolean toucheMur(ArrayList<Mur> lesMurs) {
		for(Mur unMur : lesMurs) {
			if(super.toucheObjet(unMur)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gain de points de vie apr�s avoir touch� un joueur
	 */
	public void gainVie() {
	}
	
	/**
	 * Perte de points de vie apr�s avoir �t� touch� 
	 */
	public void perteVie() {
	}
	
	/**
	 * vrai si la vie est � 0
	 * @return true si vie = 0
	 */
	public Boolean estMort() {
		return null;
	}
	
	/**
	 * Le joueur se d�connecte et disparait
	 */
	public void departJoueur() {
	}
	
}
