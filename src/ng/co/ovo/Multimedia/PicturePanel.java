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
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;

public class PicturePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1083115183691443105L;
	private ScheduledExecutorService executorService;
	private JLabel lblPic, lblPic_1, lblPic_2, lblPic_3, lblPic_4, lblPic_5;
	static ArrayList<JLabel> labels;
	JPanel contentPane;
	
	public PicturePanel(JPanel contentPane) {
		this.contentPane = contentPane;
		setBounds(300, 31, 480, 400);
		setLayout(null);
		setBackground(Color.BLACK);
		
		lblPic = new JLabel("");
		lblPic.setBounds(0, 0, 148, 266);
		add(lblPic);
		
		lblPic_1 = new JLabel("");
		lblPic_1.setBounds(326, 0, 154, 133);
		add(lblPic_1);
		
		lblPic_2 = new JLabel("");
		lblPic_2.setBounds(144, 0, 182, 133);
		add(lblPic_2);
		
		lblPic_3 = new JLabel("");
		lblPic_3.setBounds(144, 133, 168, 133);
		add(lblPic_3);
		
		lblPic_4 = new JLabel("");
		lblPic_4.setBounds(0, 266, 316, 134);
		add(lblPic_4);
		
		lblPic_5 = new JLabel("");
		lblPic_5.setBounds(312, 133, 168, 267);
		add(lblPic_5);	
		
		contentPane.add(this);
	    labels = new ArrayList<JLabel>(Arrays.asList(lblPic, lblPic_1, lblPic_2, lblPic_3, lblPic_4, lblPic_5));
	    initDisplay();
		
	    //Creates a single-threaded executor that can schedule commands to run after a given delay (1sec)
		executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleAtFixedRate(new UpdatePanel(), 0L, 1L, TimeUnit.SECONDS);		
	}
	
	/**
	 * displays a random Image
	 */
	private void initDisplay(){
		for(JLabel a : labels){
			Image img = (new ImageIcon(getClass().getResource("/images/Quotes/"+randInt()+".jpg"))).getImage();
			a.setIcon(new ImageIcon(img.getScaledInstance(a.getWidth(), a.getHeight(), Image.SCALE_SMOOTH)));
		}
	}
	
	/**
	 * Creates a new random number generator from the number of picture quotes(36)
	 * @return int
	 */
	public static int randInt(){
		Random rand = new Random();
		int randomNum = rand.nextInt(36)+1;
		return randomNum;
	}
	
	/**
	 * Creates a new random number generator, gets the number and select a random JLabel from array
	 * @return JLabel
	 */
	public static JLabel randJLabel(){
		Random randGen = new Random();
		int index = randGen.nextInt(labels.size());
		return labels.get(index);
	}
	
	/**
	 * Creates a new random Image
	 * @return Image
	 */
	public static Image randImg(){
		Image img = (new ImageIcon(PicturePanel.class.getResource("/images/Quotes/"+randInt()+".jpg"))).getImage();
		return img;
	}
	
	public void end(){
		contentPane.remove(this);
		executorService.shutdown();
	}
	
	/**
	 * class that is run by the executor thread
	 *
	 */
	private final class UpdatePanel implements Runnable {
	
        @Override
        public void run() {
    		final JLabel lbl = randJLabel();
    		final Image img = randImg();
			
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	lbl.setIcon(new ImageIcon(img.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH)));
                }
            });
        }
    }
}
