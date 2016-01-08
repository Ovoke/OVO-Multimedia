/*
 * This file is part of OVO Multimedia
 * 
 * OVO Multimedia is a free software that reads
 * multimedia files common to desktops. It uses external libraries
 * such as the vlcj, PDFRenderer and the jna library
 * 
 *  Software was solely written by Ogbeta Ovokerie <ogbetaovokerie@gmail.com>
 *  Copyright 2015 OVO Software
 */

package ng.co.ovo.Multimedia;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author ovokerie
 * creates the panel that serves as the contentPane of the main frame
 */
public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1138265999732274515L;
	Image img;
	int x;
	int y;
	/**
	 * Create the panel and overrides the paintComponent to display custom image
	 */
	public MainPanel() {
		img = new ImageIcon(getClass().getResource("/images/icons/01-place inside-psd.png")).getImage();
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		x = (int) size.getWidth();
		y = (int) size.getHeight();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x, y, null);
	}
}
