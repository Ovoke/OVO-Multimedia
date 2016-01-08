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

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import java.awt.Font;

/**
 * @author ovokerie
 * creates the panel that reads music files and the associated icons. The icons are represented
 * by labels and their listeners are added and implemented. This panel is displayed on the main
 * frame when it is created
 * 
 * The external library used is the VLCJ library which is open source.
 *
 */
public class MusicPanel extends JPanel { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -4164417826648505656L;
	protected static final int SKIP_TIME = 30000;
	private JPanel contentPane, panel_1, panel;
	private JLabel lblAnimation, label, lblSt, lblPl, lblPa, lblFo, lblBa, lblLabel, lblEnd;
	private JProgressBar progressBar;
	private String filePath;
	private Cursor hand;

	//creates a new instance of audio player which plays the music files
	private AudioMediaPlayerComponent audioPlayerComponent;
	private MediaPlayer audioPlayer;
	
	public MusicPanel(String mrl, JPanel contentPane) {
		this.contentPane = contentPane;
		filePath = mrl;
		audioPlayerComponent = new AudioMediaPlayerComponent();
		audioPlayer = audioPlayerComponent.getMediaPlayer();
		
		init();
		contentPane.repaint();
		addListeners();
		startPlay();
	}

	private void init(){
		hand = new Cursor(Cursor.HAND_CURSOR);
		
		//creates and sets the properties the displayed panel 
		setBackground(Color.BLACK);
		setBounds(20, 330, 230, 170);
		setLayout(null);
		
		//creates and set the properties of the panel that holds the control icons
		panel = new JPanel();
		panel.setBounds(0, 118, 230, 32);
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.BLACK);
		add(panel);
		
		//creates and sets the properties of the search backward icon
		lblBa = new JLabel("");
		lblBa.setIcon(new ImageIcon(getClass().getResource("/images/icons/back.png")));
		lblBa.setToolTipText("Search backward");
	    lblBa.setCursor(hand);
		panel.add(lblBa);

		//creates and sets the properties of the pause icon
		lblPa = new JLabel("");
		lblPa.setIcon(new ImageIcon(getClass().getResource("/images/icons/pause.png")));
		lblPa.setToolTipText("Pause");
	    lblPa.setCursor(hand);
		panel.add(lblPa);

		//creates and sets the properties of the play icon
		lblPl = new JLabel("");
		lblPl.setIcon(new ImageIcon(getClass().getResource("/images/icons/fullplay.png")));
		lblPl.setToolTipText("Play");
	    lblPl.setCursor(hand);
		panel.add(lblPl);

		//creates and sets the properties of the stop icon
		lblSt = new JLabel("");
		lblSt.setIcon(new ImageIcon(getClass().getResource("/images/icons/stop.png")));
		lblSt.setToolTipText("Stop");
	    lblSt.setCursor(hand);
		panel.add(lblSt);

		//creates and sets the properties of the search forward icon
		lblFo = new JLabel("");
		lblFo.setIcon(new ImageIcon(getClass().getResource("/images/icons/front.png")));
		lblFo.setToolTipText("Search forward");
	    lblFo.setCursor(hand);
		panel.add(lblFo);

		//creates and sets the properties of the exit icon
		lblEnd = new JLabel("");
		lblEnd.setIcon(new ImageIcon(getClass().getResource("/images/icons/exit.png")));
		lblEnd.setToolTipText("Exit");
		lblEnd.setCursor(hand);
		panel.add(lblEnd);
		
		//creates a panel that holds the progress bar and time label
		panel_1 = new JPanel();
		panel_1.setBounds(0, 96, 230, 24);
		panel_1.setForeground(Color.WHITE);
		panel_1.setBackground(Color.BLACK);
		add(panel_1);
		
		progressBar = new JProgressBar(0, 1000);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);
	    progressBar.setForeground(Color.red);	
		panel_1.add(progressBar);
		
		label = new JLabel("00:00:00");
		label.setForeground(Color.WHITE);
		panel_1.add(label);
		
		//creates a label that holds the gif animation
		lblAnimation = new JLabel("");
		lblAnimation.setBounds(32, 0, 166, 98);
		lblAnimation.setIcon(new ImageIcon(getClass().getResource("/images/sound/0040.gif")));
		add(lblAnimation);
		
		//splits the file path string to get the name of file
		String[] x = filePath.split("\\\\");
		lblLabel = new JLabel(x[x.length-1]);
		lblLabel.setFont(new Font("Aharoni", Font.PLAIN, 12));
		lblLabel.setForeground(Color.WHITE);
		lblLabel.setBackground(Color.BLACK);
		lblLabel.setBounds(0, 156, 230, 14);
		add(lblLabel);
		
		contentPane.add(this);
	}
	

	/**
	 * all icons on the frame are activated to listen for mouse clicked events which controls
	 * the audio player that plays the music
	 */
	private void addListeners(){
		lblSt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
	            audioPlayer.stop();
	            lblAnimation.setVisible(false);
	         }
	      });
		
		lblFo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
	            skip(SKIP_TIME);
	         }
	      });
		
		lblBa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
	            skip(-SKIP_TIME);
	         }
	      });
		
		lblPl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
	            audioPlayer.play();
	            lblAnimation.setVisible(true);
	         }
	      });
		
		lblPa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
	            audioPlayer.pause();
	            lblAnimation.setVisible(false);
	         }
	      });
		
		lblEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	theEnd();
	         }
	      });
		audioPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
			@Override			
			public void finished(MediaPlayer mediaPlayer){
				mediaPlayer.setRepeat(true);
			}
			public void timeChanged(final MediaPlayer mediaPlayer, long newTime){
				final long time = mediaPlayer.getTime();
	            final int position = (int)(mediaPlayer.getPosition() * 1000f);

	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    if(mediaPlayer.isPlaying()) {
	                        updateTime(time);
	                        updatePosition(position);
	                    }
	                }
	            });
			}
		});
	}
	
	public void startPlay(){
		audioPlayer.playMedia(filePath);
	}
	
	private void updateTime(long millis) {
	    String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	    label.setText(s);
	}
	
	private void updatePosition(int value) {
        progressBar.setValue(value);
    }
	
	private void skip(int skipTime) {
        if(audioPlayer.getLength() > 0) {
            audioPlayer.skip(skipTime);
            updateState();
        }
    }
	
	private void updateState(){
		long time = audioPlayer.getTime();
        int position = (int)(audioPlayer.getPosition() * 1000.0f);
        updateTime(time);
        updatePosition(position);
	}
	
    
	private void theEnd(){
	    contentPane.remove(this);
	    contentPane.repaint();
	    audioPlayer.stop();
	}
}
