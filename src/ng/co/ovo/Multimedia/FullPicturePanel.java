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

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FullPicturePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4976537850165933786L;
	private ScheduledExecutorService executorService;
	JPanel contentPane;
	JLabel lblLabel;

	public FullPicturePanel(JPanel contentPane) {
		this.contentPane = contentPane;
		setBounds(300, 31, 480, 400);
		setBackground(Color.BLACK);
		setLayout(null);
		
		lblLabel = new JLabel("");
		lblLabel.setBounds(0, 0, 480, 400);
		add(lblLabel);
		
		contentPane.add(this);

	    //Creates a single-threaded executor that can schedule commands to run after a given delay (5sec)
		executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleAtFixedRate(new UpdatePanel(), 0L, 5L, TimeUnit.SECONDS);
	}
	
	public static int randInt(){
		Random rand = new Random();
		int randomNum = rand.nextInt(36)+1;
		return randomNum;
	}
	
	public static Image randImg(){
		Image img = (new ImageIcon(FullPicturePanel.class.getResource("/images/Quotes/"+randInt()+".jpg"))).getImage();
		return img;
	}
	
	public void end(){
		contentPane.remove(this);
		executorService.shutdown();
	}
	
	/**
	 * 
	 * class that is run by the executor thread
	 *
	 */
	private final class UpdatePanel implements Runnable {
		
        @Override
        public void run() {
    		final Image img = randImg();
			
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	lblLabel.setIcon(new ImageIcon(img.getScaledInstance(lblLabel.getWidth(), lblLabel.getHeight(), Image.SCALE_SMOOTH)));
                }
            });
        }
    }
}
