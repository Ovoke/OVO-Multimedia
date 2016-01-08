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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class ImagePanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6274346170881429674L;
	BufferedImage image;
    double scale;
    int degree;
  
    public ImagePanel(String filePath){
        loadImage(filePath);
        scale = 1.0;
        degree = 0;
        setBackground(Color.black);
    }
    
    /**
     * overrides the paintComponent to display image on panel and uses the AffineTransform class 
     * to perform 2D translations such as rotate, scale etc on the image
     */
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        at.rotate(Math.toRadians((double) degree), imageWidth/2, imageHeight/2);
        g2.drawRenderedImage(image, at);
    }
  
    public Dimension getPreferredSize(){
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }
  
    public void setScale(double s){
        scale = s;
        revalidate();     
        repaint();
    }
    
    public void setDegree(int d){
    	degree = d;
    	revalidate();     
        repaint();
    }
  
    private void loadImage(String filePath){
        try{
            image = ImageIO.read(new File(filePath));
        }
        catch(Exception alle){
 		   ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
 		   JOptionPane.showMessageDialog(this, "Error occured when reading file", "Error", JOptionPane.INFORMATION_MESSAGE,  icon);
        }
    }
}
 
