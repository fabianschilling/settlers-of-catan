package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import controller.Controller;

/**
 * Die Klasse ist das Informationsmenu des eigenen Spielers auf der GUI Es baut
 * auf der Klasse <code>PlayerFrame</code> aus
 * 
 * @author Thomas Wimmer, Fabian Schilling
 * 
 */
@SuppressWarnings("serial")
public class SettlerFrame extends PlayerFrame {

	/**
	 * Der obligatorische Controller
	 */
	private Controller controller;

	/**
	 * Ein Button um das Kartenmen&uuml; aufzurufen
	 */
	private PlayerButton bCard;

	/**
	 * Ein Button um das Handelsmen&uuml; aufzurufen
	 */
	private PlayerButton bTrade;

	/**
	 * Ein Button um zu w&uuml;rfeln
	 */
	private PlayerButton bRoll;

	/**
	 * Ein Button, um seinen Zug zu beenden
	 */
	private PlayerButton bNext;

	/**
	 * Der Konstruktor erzeugt ein neuen Settlerframe, der als Dreh- und
	 * Angelpunkt der GUI fungiert
	 * 
	 * @param controller
	 *            ist der Controller
	 * @param img
	 *            ist das Anzeigebild des Players
	 * @param width
	 *            ist die Breite des Panels
	 * @param height
	 *            ist die H&ouml;he des Panels
	 */
	public SettlerFrame(Controller controller, Image img, int width, int height) {
		this.controller = controller;
		setWidth(width);
		setHeight(height);
		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		setAvatarImage(img);
		init();
	}

	/**
	 * Initialisiert das <code>SettlerFrame</code>
	 */
	public void init() {
		createButtons();
		setupInteraction();
		createWidgets();
		addWidgets();
	}

	/**
	 * Erstellt die <code>PlayerButtons</code> f&uuml;r das
	 * <code>SettlerFrame</code>
	 */
	public void createButtons() {
		int buttonWidth = getComponentWidth();
		int buttonHeight = getComponentHeight();
		bCard = new PlayerButton(ImportImages.cardBtn, buttonWidth,
				buttonHeight);
		bCard.setToolTipText(Messages.getString("SettlerFrame.KarteSpielen")); //$NON-NLS-1$
		bTrade = new PlayerButton(ImportImages.tradeBtn, buttonWidth,
				buttonHeight);
		bTrade.setToolTipText(Messages.getString("SettlerFrame.Handeln")); //$NON-NLS-1$
		bRoll = new PlayerButton(ImportImages.rollBtn, buttonWidth,
				buttonHeight);
		bRoll.setToolTipText(Messages.getString("SettlerFrame.Wuerfeln")); //$NON-NLS-1$
		bNext = new PlayerButton(ImportImages.nextBtn, buttonWidth,
				buttonHeight);
		bNext.setToolTipText(Messages.getString("SettlerFrame.ZugBeenden")); //$NON-NLS-1$
		addComponent(bCard);
		addComponent(bTrade);
		addComponent(bRoll);
		addComponent(bNext);
	}

	/**
	 * F&uuml;gt die Interaktion mit dem <code>Controller</code> zum
	 * <code>SettlerFrame<code> hinzu.
	 */
	public void setupInteraction() {
		bCard.setActionCommand("menu.card"); //$NON-NLS-1$
		bCard.addActionListener(controller);
		bCard.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cardBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.cardBtn);
			}
		});
		bTrade.setActionCommand("menu.trade"); //$NON-NLS-1$
		bTrade.addActionListener(controller);
		bTrade.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.tradeBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.tradeBtn);
			}
		});
		bRoll.setActionCommand("menu.roll"); //$NON-NLS-1$
		bRoll.addActionListener(controller);
		bRoll.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.rollBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.rollBtn);
			}
		});
		bNext.setActionCommand("menu.next"); //$NON-NLS-1$
		bNext.addActionListener(controller);
		bNext.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.nextBtnActive);
			}
			public void mouseExited(MouseEvent me){
				((PlayerButton)me.getSource()).changeIcon(ImportImages.nextBtn);
			}
		});
	}
}
