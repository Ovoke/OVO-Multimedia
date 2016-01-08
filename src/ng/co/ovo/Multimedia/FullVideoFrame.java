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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

/**
 * @author ovokerie
 * creates the windows that reads video files and the associated icons. The icons are represented
 * by labels and their listeners are added and implemented
 * 
 * The external library used is the VLCJ library which is open source.
 *
 */
public class FullVideoFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4763353117039972237L;
	protected static final int SKIP_TIME_MS = 60000;
	private boolean mousePressedPlaying = false;
	
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblBack, lblFront, lblPause, lblStop, label, lblExit, lblPlay, lblTitle, lblMinimize;
	private JSlider slider;
	private Cursor hand;
	String filePath;
	
	private EmbeddedMediaPlayerComponent mpc;
	private EmbeddedMediaPlayer mediaPlayer;
	
	public FullVideoFrame(String filePath){	  
	  this.filePath = filePath;
	  //creates a new instance of media player which plays the video files
	  mpc = new EmbeddedMediaPlayerComponent();
	  mediaPlayer = mpc.getMediaPlayer();
	  
	  //disables the native media player keyboard and mouse input handling to allow java handle it.
	  mediaPlayer.setEnableKeyInputHandling(false);
      mediaPlayer.setEnableMouseInputHandling(false);
  
      createUI();
	}
	
	/**
	 * creates the frame, sets the icon listeners and starts to play the video file
	 */
	private void createUI() {
		createControls(); 
		registerListeners();
		setVisible(true);
		mediaPlayer.playMedia(filePath);
	}
	
	public void createControls() {
		hand = new Cursor(Cursor.HAND_CURSOR);
		
		//creates and sets the properties of the frame
		setUndecorated(true);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getResource("/images/icons/logo.png")).getImage());
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(mpc, BorderLayout.CENTER);
		setContentPane(contentPane);

		//creates and sets the properties of the panel that holds the icons
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setForeground(Color.WHITE);
		contentPane.add(panel, BorderLayout.NORTH);

		//splits the file path string to get the name of file
		String[] x = filePath.split("\\\\");
		lblTitle = new JLabel(x[x.length-1]);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Aharoni", Font.BOLD, 12));
		panel.add(lblTitle);
		
		//creates and sets the properties of the search backward icon
		lblBack = new JLabel("");
		lblBack.setIcon(new ImageIcon(getClass().getResource("/images/icons/back.png")));
		lblBack.setForeground(Color.WHITE);
		lblBack.setToolTipText("Search backward");
		lblBack.setCursor(hand);
		panel.add(lblBack);

		//creates and sets the properties of the pause icon
		lblPause = new JLabel("");
		lblPause.setIcon(new ImageIcon(getClass().getResource("/images/icons/pause.png")));
		lblPause.setForeground(Color.WHITE);
		lblPause.setToolTipText("Pause");
		lblPause.setCursor(hand);
		panel.add(lblPause);

		//creates and sets the properties of the stop icon
		lblStop = new JLabel("");
		lblStop.setIcon(new ImageIcon(getClass().getResource("/images/icons/stop.png")));
		lblStop.setForeground(Color.WHITE);
		lblStop.setToolTipText("Stop");
		lblStop.setCursor(hand);
		panel.add(lblStop);

		//creates and sets the properties of the play icon
		lblPlay = new JLabel("");
		lblPlay.setIcon(new ImageIcon(getClass().getResource("/images/icons/fullplay.png")));
		lblPlay.setForeground(Color.WHITE);
		lblPlay.setToolTipText("Play");
		lblPlay.setCursor(hand);
		panel.add(lblPlay);

		//creates and sets the properties of the search forward icon
		lblFront = new JLabel("");
		lblFront.setIcon(new ImageIcon(getClass().getResource("/images/icons/front.png")));
		lblFront.setForeground(Color.WHITE);
		lblFront.setToolTipText("Search forward");
		lblFront.setCursor(hand);
		panel.add(lblFront);
		
		//creates a slider
		slider = new JSlider();
		slider.setBackground(Color.BLACK);
		slider.setMinimum(0);
        slider.setMaximum(1000);
        slider.setValue(0);
		panel.add(slider);
		
		//creates a label for displaying time
		label = new JLabel("00:00:00");
		label.setForeground(Color.WHITE);
		panel.add(label);

		//creates and sets the properties of the minimize icon
		lblMinimize = new JLabel("");
		lblMinimize.setIcon(new ImageIcon(getClass().getResource("/images/icons/minimize.png")));
		lblMinimize.setForeground(Color.WHITE);
		lblMinimize.setToolTipText("Minimize frame");
		lblMinimize.setCursor(hand);
		panel.add(lblMinimize);

		//creates and sets the properties of the exit icon
		lblExit = new JLabel("");
		lblExit.setIcon(new ImageIcon(getClass().getResource("/images/icons/exit.png")));
		lblExit.setForeground(Color.WHITE);
		lblExit.setToolTipText("Exit frame");
		lblExit.setCursor(hand);
		panel.add(lblExit);
	}

	/**
	 * all icons on the frame are activated to listen for mouse clicked events which controls
	 * the media player that plays the video
	 */
	public void registerListeners(){
		lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	skip(-SKIP_TIME_MS);
            }
		});
		
		lblFront.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	skip(SKIP_TIME_MS);
            }
		});
		
		lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	close();
            }
		});
		
		lblMinimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	setState(BooksFrame.ICONIFIED);
            }
		});
		
		lblStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	mediaPlayer.stop();
            }
		});
		
		lblPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	mediaPlayer.play();
            }
		});
		
		lblPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	mediaPlayer.pause();
            }
		});
		
		slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(mediaPlayer.isPlaying()) {
                    mousePressedPlaying = true;
                    mediaPlayer.pause();
                }
                else {
                    mousePressedPlaying = false;
                }
                setSliderBasedPosition();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setSliderBasedPosition();
                updateUIState();
            }
        });
		
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
			
			//set and positions the marquee(text displayed in video player)
			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount){
				mediaPlayer.setMarqueeText("ogbetaovokerie@gmail.com");
		        mediaPlayer.setMarqueeSize(20);
		        mediaPlayer.setMarqueeOpacity(95);
		        mediaPlayer.setMarqueeColour(Color.white);
		        mediaPlayer.setMarqueeTimeout(10000);
		        mediaPlayer.setMarqueeLocation(50, 120);
		        mediaPlayer.enableMarquee(true); 
			}
			//method is called when the video finishes 
			public void finished(MediaPlayer mediaPlayer){
				close();
			}
			//method is called when time changes every millisecond
			public void timeChanged( final MediaPlayer mediaPlayer, long newTime){
				final long time = mediaPlayer.getTime();
	            final int position = (int)(mediaPlayer.getPosition() * 1000.0f);

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
	
	private void close(){
    	this.dispose();
    	if(mediaPlayer.isPlaying())
    		mediaPlayer.stop();
	}
	
	private void skip(int skipTime) {
        if(mediaPlayer.getLength() > 0) {
            mediaPlayer.skip(skipTime);
            updateUIState();
        }
    }
	
	private void setSliderBasedPosition() {
        if(!mediaPlayer.isSeekable()) {
            return;
        }
        float positionValue = slider.getValue() / 1000.0f;
        if(positionValue > 0.99f) {
            positionValue = 0.99f;
        }
        mediaPlayer.setPosition(positionValue);
    }
	
	private void updateUIState() {
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.play();
            if(!mousePressedPlaying) {
                try {
                    Thread.sleep(500);
                }
                catch(InterruptedException e) {
                }
                mediaPlayer.pause();
            }
        }
        long time = mediaPlayer.getTime();
        int position = (int)(mediaPlayer.getPosition() * 1000.0f);
        updateTime(time);
        updatePosition(position);
    }
	
	private void updateTime(long millis) {
	    String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	    label.setText(s);
	}
	
	private void updatePosition(int value) {
        slider.setValue(value);
    }
}
