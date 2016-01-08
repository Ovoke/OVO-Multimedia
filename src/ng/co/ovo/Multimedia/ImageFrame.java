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
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JSlider;

import java.awt.Component;

import javax.swing.Box;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

/**
 * @author ovokerie
 * creates the windows that reads image files and the associated icons. The icons are represented
 * by labels and their listeners are added and implemented
 * 
 * No external library is used purely written using the java libraries
 *
 */
public class ImageFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 788119502673331244L;
	private JPanel contentPane, panel, panel_1;
	private JLabel lblTitle, lblMin, lblMax, lblTurnleft, lblTurnright, lblMinimize, lblExit;
	private JSlider slider;
	String filePath;
	private Cursor hand;
	private ImagePanel panelImg;
	float positionValue;
	int degreeValue;

	public ImageFrame(String mrl) {
		filePath = mrl;
		
		start();
		setAction();
		setVisible(true);
	}	
	
	private void start(){
		hand = new Cursor(Cursor.HAND_CURSOR);
		positionValue = 1.0f;
		degreeValue = 0;

		//creates and sets the properties of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setResizable(true);
		setIconImage(new ImageIcon(getClass().getResource("/images/icons/logo.png")).getImage());
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
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
		lblTitle.setFont(new Font("Aharoni", Font.BOLD, 14));
		panel.add(lblTitle);
		
		//creates a horizontal space between file title and icons
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		

		//creates and sets the properties of the zoom out icon
		lblMin = new JLabel("");
		lblMin.setIcon(new ImageIcon(getClass().getResource("/images/icons/ZoomOut.png")));
		lblMin.setForeground(Color.WHITE);
		lblMin.setToolTipText("Zoom out of image");
		lblMin.setCursor(hand);
		panel.add(lblMin);
		
		 //creates and sets the properties of a slider
		slider = new JSlider();
		slider.setBackground(Color.BLACK);
		slider.setMinimum(1);
        slider.setMaximum(20);
        slider.setValue(10);
		panel.add(slider);

		//creates and sets the properties of the zoom in icon
		lblMax = new JLabel("");
		lblMax.setIcon(new ImageIcon(getClass().getResource("/images/icons/ZoomIn.png")));
		lblMax.setForeground(Color.WHITE);
		lblMax.setToolTipText("Zoom into image");
		lblMax.setCursor(hand);
		panel.add(lblMax);

		//creates and sets the properties of the rotate image left icon
		lblTurnleft = new JLabel("");
		lblTurnleft.setIcon(new ImageIcon(getClass().getResource("/images/icons/turnleft.png")));
		lblTurnleft.setForeground(Color.WHITE);
		lblTurnleft.setToolTipText("Rotate image left");
		lblTurnleft.setCursor(hand);
		panel.add(lblTurnleft);
		

		//creates and sets the properties of the rotate image right icon
		lblTurnright = new JLabel("");
		lblTurnright.setIcon(new ImageIcon(getClass().getResource("/images/icons/turnright.png")));
		lblTurnright.setForeground(Color.WHITE);
		lblTurnright.setToolTipText("Rotate image right");
		lblTurnright.setCursor(hand);
		panel.add(lblTurnright);

		//creates and sets the properties of the minimize icon
		lblMinimize = new JLabel("");
		lblMinimize.setIcon(new ImageIcon(getClass().getResource("/images/icons/minimize.png")));
		lblMinimize.setForeground(Color.WHITE);
		lblMinimize.setToolTipText("Minimize frame");
		lblMinimize.setCursor(hand);
		panel.add(lblMinimize);

		//creates and sets the properties of the minimize icon
		lblExit = new JLabel("");
		lblExit.setIcon(new ImageIcon(getClass().getResource("/images/icons/exit.png")));
		lblExit.setForeground(Color.WHITE);
		lblExit.setToolTipText("Exit frame");
		lblExit.setCursor(hand);
		panel.add(lblExit);
		
		//creates an image panel which displays the ImagePanel(class) that reads the image file
		panel_1 = new JPanel();
		panel_1.setBackground(Color.BLACK);
		panel_1.setForeground(Color.WHITE);
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));		
		panelImg = new ImagePanel(filePath);
		panel_1.add(new JScrollPane(panelImg));		
	}

	/**
	 * all icons on the frame are activated to listen for mouse clicked events which controls
	 * the ImagePanel that displays the image
	 */
	private void setAction(){
		lblMin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(positionValue > 0.1f){
	            	positionValue -= 0.1f;
	            	scaleImage();
            	}
            	updatePanel();
            }
		});
		
		lblMax.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(positionValue < 2.0f){
	            	positionValue += 0.1f;
	            	scaleImage();
            	}
                updatePanel();
            }
		});
		
		lblTurnleft.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	degreeValue = (degreeValue -= 90) % 360;
            	panelImg.setDegree(degreeValue);
            }
		});
		
		lblTurnright.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	degreeValue = (degreeValue += 90) % 360;
            	panelImg.setDegree(degreeValue);            	
            }
		});
		
		lblMinimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	setState(ImageFrame.ICONIFIED);
            }
		});
		
		lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	ImageFrame.this.dispose();
            }
		});
		
		slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	setSliderPosition();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	setSliderPosition();
            	scaleImage();              
            }
        });
	}
	
	private void setSliderPosition(){
		positionValue = slider.getValue() / 10.0f;
        if(positionValue > 2.0f) {
            positionValue = 2.0f;
        }
        updatePanel();
	}
	
	private void scaleImage(){
		panelImg.setScale(positionValue);
        slider.setValue((int) (positionValue*10));  
	}
	

	/**
	 * updates the control panel icons by enabling or disabling icons depending on state of image
	 */
	private void updatePanel(){
		if(positionValue > 0.1f && positionValue < 2.0f){
			lblMin.setEnabled(true);
			lblMax.setEnabled(true);
		} else if(positionValue <= 0.1f){
			lblMin.setEnabled(false);
			lblMax.setEnabled(true);
		} else if(positionValue >= 2.0f){
			lblMin.setEnabled(true);
			lblMax.setEnabled(false);
		}
	}
}
