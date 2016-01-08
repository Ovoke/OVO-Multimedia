package ng.co.ovo.Multimedia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.sun.pdfview.PDFFile;

public class ThumbnailExtendedPanel extends JPanel implements PageChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5533952425751707733L;
	JPanel titlePanel, panel, emptyPanel;
	JLabel titleLabel, lblClose, lblExpand;
	ThumbPanel thumbs;
	JScrollPane scroll;
	Border paddingBorder, border;
	BooksFrame bf;
	
	public ThumbnailExtendedPanel(PDFFile file, BooksFrame bf){
		this.bf = bf;
		
		setLayout(new BorderLayout(0, 0));
		paddingBorder = BorderFactory.createEmptyBorder(12, 0, 12, 0);
		border = BorderFactory.createLineBorder(Color.GRAY);
		
		thumbs = new ThumbPanel(file);
        scroll = new JScrollPane(thumbs);
        scroll.getViewport().setView(thumbs);
        scroll.getViewport().setBackground(Color.gray);
        add(scroll, BorderLayout.CENTER);
		
        emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.GRAY);
        emptyPanel.setVisible(true);
        emptyPanel.setLayout(new BorderLayout(0, 0));
		add(emptyPanel, BorderLayout.NORTH);
        
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.GRAY);
		titlePanel.setVisible(true);
		titlePanel.setLayout(new BorderLayout(0, 0));
		emptyPanel.add(titlePanel, BorderLayout.NORTH);
		
		//panel to hold the extended panel buttons and title
		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setVisible(true);
		titlePanel.add(panel, BorderLayout.EAST);
		
		lblClose = new JLabel("");
		lblClose.setIcon(new ImageIcon(getClass().getResource("/images/icons/close.png")));
		lblClose.setBackground(Color.GRAY);
		lblClose.setToolTipText("collapse");
		lblClose.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
		panel.add(lblClose);
		
		lblExpand = new JLabel("");
		lblExpand.setIcon(new ImageIcon(getClass().getResource("/images/icons/expand.png")));
		lblExpand.setBackground(Color.BLACK);
		lblExpand.setToolTipText("expand");
		lblExpand.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
		panel.add(lblExpand);
		
		titleLabel = new JLabel("Page Thumbnails");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Aharoni", Font.BOLD, 12));
		titleLabel.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
		titlePanel.add(titleLabel, BorderLayout.WEST);
		
		setThumbListeners();
	}
	
	private void setThumbListeners() {
		thumbs.addPageChangeListener(this);
	}

	public void stopThumbnail(){
		thumbs.stop();
	}
	
	@Override
	public void gotoPage(int page) {
		bf.gotoPage(page);
	}
}
