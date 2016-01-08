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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import javax.swing.JList;

/**
 * @author ovokerie
 * creates the windows that reads pdf files and the associated icons. The icons are represented
 * by labels and their listeners are added and implemented
 * 
 * The external library used is the PDFRenderer which is open source and written solely in java
 *
 */
public class BooksFrame extends JFrame implements KeyListener{
	
	/**
	 * pdf viewer
	 */
	private static final long serialVersionUID = 4509293610235601416L;
	private String[] zrange = {"100%", "150%", "200%", "250%", "300%", "350%", "400%", "450%", "500%",
								"550%", "600%"};
	@SuppressWarnings("rawtypes")
	private Vector items;
	private JPanel contentPane, panel, sidePanel, thumbnailPanel, bookmarkPanel;
	private JTextField textField;
	private Cursor hand;
	private String filePath;
	private JLabel lblUp, lblDown, lblPageno, lblZoomin, lblZoomout, lblExit, lblTitle, lblMinimize, 
				   lblConvert, lblPrint, lblFullscreen, thumbnail, bookmark, lblPageSetup;
	private JComboBox<String> comboBox;
	private JScrollPane scroll;
	private JSplitPaneWithZeroSizeDivider split;
	private PagePanel pPanel;
	private File file;
	private PDFFile pdfFile;
	private RandomAccessFile raf; 
	private ByteBuffer buf;
	private FileChannel channel;
	private PDFPage page;
	private int curPage, lastPage, curZoomLevel;
	private JList<Object> list;
	private ThumbnailExtendedPanel expandPanel;
	private PDFPageLoader loader;
	private BookmarkExtendedPanel bep;
	private PageFormat pformat;
	
	private boolean dothumbs;
	private boolean isBookmarkExpanded;

	public BooksFrame(String mrl){
		
		//gets the file path, reads the file into a PDFFile(from the PDFRenderer library) object and catches any 
		//error if the pdf file is #bad
		this.filePath = mrl;
		
		/** The page format for printing */
		pformat = PrinterJob.getPrinterJob().defaultPage();
	    
		try{
			file = new File(filePath);
			raf = new RandomAccessFile(file, "r");
			channel = raf.getChannel();
			buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdfFile = new PDFFile(buf);
			curPage = 1;
			lastPage = pdfFile.getNumPages();
			curZoomLevel = 100;
		}catch(Exception e){
			ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
			JOptionPane.showMessageDialog(this, "File Error", "Error", JOptionPane.INFORMATION_MESSAGE,  icon);
		}
        
		init();
		setListeners();
		setVisible(true);
		        
        displayPage();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(){
		hand = new Cursor(Cursor.HAND_CURSOR);
		dothumbs = true;
		isBookmarkExpanded = false;
		items = new Vector(Arrays.asList(zrange));
		
		//creates a scrollable panel(PagePanel class from PDFRenderer library) where pdf page will be displayed
		pPanel = new PagePanel();
        scroll = new JScrollPane(pPanel);
        pPanel.setBackground(Color.BLACK);
        
        //creates a frame, sets its properties and adds the scrollable panel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setResizable(true);
		setIconImage(new ImageIcon(getClass().getResource("/images/icons/logo.png")).getImage());
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//creates a panel where the control icons will be displayed
		panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setVisible(true);
		contentPane.add(panel, BorderLayout.NORTH);
		
		//splits the file path string to get the name of file
		String[] x = filePath.split("\\\\");
		lblTitle = new JLabel(x[x.length-1]);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Aharoni", Font.BOLD, 12));
		panel.add(lblTitle);
		
		lblConvert = new JLabel("");
		lblConvert.setIcon(new ImageIcon(BooksFrame.class.getResource("/images/icons/convert.png")));
		lblConvert.setCursor(hand);
		panel.add(lblConvert);
		
		lblPrint = new JLabel("");
		lblPrint.setIcon(new ImageIcon(BooksFrame.class.getResource("/images/icons/print.png")));
		lblPrint.setCursor(hand);
		panel.add(lblPrint);
		
		lblPageSetup = new JLabel("");
		lblPageSetup.setIcon(new ImageIcon(BooksFrame.class.getResource("/images/icons/pagesetup.png")));
		lblPageSetup.setCursor(hand);
		panel.add(lblPageSetup);
		
		sidePanel = new JPanel();
		sidePanel.setBackground(Color.BLACK);
		sidePanel.setVisible(true);
		contentPane.add(sidePanel, BorderLayout.WEST);
		
		split = new JSplitPaneWithZeroSizeDivider(JSplitPaneWithZeroSizeDivider.HORIZONTAL_SPLIT);
        split.setOneTouchExpandable(false);
        expandPanel = new ThumbnailExtendedPanel(pdfFile, this);
        split.setLeftComponent(expandPanel);
        split.setRightComponent(scroll);
        contentPane.add(split, BorderLayout.CENTER);
		
		thumbnail = new JLabel();
		thumbnail.setIcon(new ImageIcon(getClass().getResource("/images/icons/thumbnail.png")));
		thumbnailPanel = new JPanel();
		thumbnailPanel.setBackground(Color.BLACK);
		thumbnailPanel.add(thumbnail);
		
		bookmark = new JLabel();
		bookmark.setIcon(new ImageIcon(getClass().getResource("/images/icons/bookmark.png")));
		bookmarkPanel = new JPanel();
		bookmarkPanel.setBackground(Color.BLACK);
		bookmarkPanel.add(bookmark);
		
		list = new JList<Object>();
		list.setBackground(Color.BLACK);
		list.setCellRenderer(new ImageListCellRenderer());
		Object[] panels = {thumbnailPanel, bookmarkPanel};
		list.setListData(panels);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		sidePanel.add(list, BorderLayout.WEST);
		
		//creates and sets the properties of the previous page icon
		lblUp = new JLabel("");
		lblUp.setIcon(new ImageIcon(getClass().getResource("/images/icons/back.png")));
		lblUp.setToolTipText("Previous page");
		lblUp.setCursor(hand);
		lblUp.setEnabled(false);
		panel.add(lblUp);

		//creates and sets the properties of the next page icon		
		lblDown = new JLabel("");
		lblDown.setIcon(new ImageIcon(getClass().getResource("/images/icons/front.png")));
		lblDown.setToolTipText("Next page");
		lblDown.setCursor(hand);
		panel.add(lblDown);

		//creates and sets the properties of the textfield
		textField = new JTextField(10);
		textField.setText("1");
		panel.add(textField);
		
		//creates a label to display current location of page in file
		lblPageno = new JLabel("( 1 of " +lastPage+ " )");
		lblPageno.setForeground(Color.WHITE);
		panel.add(lblPageno);

		//creates and sets the properties of the zoom in icon
		lblZoomin = new JLabel("");
		lblZoomin.setIcon(new ImageIcon(getClass().getResource("/images/icons/ZoomIn.png")));
		lblZoomin.setToolTipText("Zoom into page");
		lblZoomin.setCursor(hand);
		panel.add(lblZoomin);

		//creates and sets the properties of the zoom out icon
		lblZoomout = new JLabel("");
		lblZoomout.setIcon(new ImageIcon(getClass().getResource("/images/icons/ZoomOut.png")));
		lblZoomout.setToolTipText("Zoom out of page");
		lblZoomout.setEnabled(false);
		lblZoomout.setCursor(hand);
		panel.add(lblZoomout);
		
		//creates an editable combobox to set the zoom range of page
		items.addElement(new JSeparator());
		items.addElement("Fit Width");
		items.addElement("Page Level");
		comboBox = new SeparatorComboBox(items);
		comboBox.setMaximumRowCount(10);
		comboBox.setEditable(true);
		panel.add(comboBox);
		
		lblFullscreen = new JLabel("");
		lblFullscreen.setIcon(new ImageIcon(BooksFrame.class.getResource("/images/icons/fullscreen.png")));
		panel.add(lblFullscreen);

		//creates and sets the properties of the minimize frame icon
		lblMinimize = new JLabel("");
		lblMinimize.setIcon(new ImageIcon(getClass().getResource("/images/icons/minimize.png")));
		lblMinimize.setToolTipText("Minimize frame");
		lblMinimize.setCursor(hand);
		panel.add(lblMinimize);

		//creates and sets the properties of the exit icon
		lblExit = new JLabel("");
		lblExit.setIcon(new ImageIcon(getClass().getResource("/images/icons/exit.png")));
		lblExit.setToolTipText("Exit frame");
		lblExit.setCursor(hand);
		panel.add(lblExit);				
	}

	/**
	 * all icons on the frame are activated to listen for mouse clicked events which controls
	 * the pdf page 
	 */
	private void setListeners(){
		lblUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	doPrev();
            }
		});
		
		lblDown.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	doNext();
            }
		});
		
		lblZoomin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(curZoomLevel < 550){
	            	curZoomLevel = curZoomLevel+=50;
	            	zoom();
	            	updateZoom();
            	}
            }
		});
		
		lblZoomout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(curZoomLevel > 100){
	            	curZoomLevel = curZoomLevel-=50;
	            	zoom();
	            	updateZoom();
            	}
            }
		});
		
		lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	try {
					exit();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
		});
		
		lblMinimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	//minimizes the frame
            	setState(BooksFrame.ICONIFIED);
            }
		});
		
		lblPrint.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	doPrint();
            }
		});
		
		lblPageSetup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	doPageSetup();
            }
		});
		
		textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
            	   int x = Integer.parseInt(textField.getText());
            	   if(x < 1 || x > lastPage){
            		   ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
            		   JOptionPane.showMessageDialog(BooksFrame.this, "Enter a valid page number \nFrom 1 to "+lastPage, "Error", 
            				   JOptionPane.INFORMATION_MESSAGE,  icon);
            		   return;
            	   }
				   curPage = x;
				   displayPage();
				   updatePanel();
               } catch (NumberFormatException e1) {
        		   ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
        		   JOptionPane.showMessageDialog(BooksFrame.this, "Invalid Page Number", "Error", JOptionPane.INFORMATION_MESSAGE,  icon);
               }
            }
		});
		
		comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          	   String string = ((JTextField)comboBox.getEditor().getEditorComponent()).getText();
          	   if(string.equals("Fit Width")){
          		   customZoomFitWidth();
          		   return;
          	   }
          	   if(string.equals("Page Level")){
          		   customZoomPageLevel();
          		   return;
          	   }
         	   String[] parts = string.split("%");
         	   int x = Integer.parseInt(parts[0]);
               try {            	             	 
				   curZoomLevel = x;
				   updateZoom();
				   zoom();				   
               } catch (NumberFormatException e1) {
        		   ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
        		   JOptionPane.showMessageDialog(BooksFrame.this, "Enter a number", "Error", JOptionPane.INFORMATION_MESSAGE,  icon);
               	 }
            }
		});
		
		list.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
			@Override
            public void mouseClicked(MouseEvent e) {
            	JList<Object> theList = (JList<Object>) e.getSource();
            	int index = theList.locationToIndex(e.getPoint());
            	if(index >= 0){
            		if(index == 0) expandThumbnailPanel();
            		if(index == 1) expandBookmarkPanel();
            	}
            }
		});

        pPanel.addKeyListener(this);
	}
	
	/**
	 * closes all the resources used and then disposes the frame
	 */
	private void exit() throws IOException{
		raf.close();
		if(expandPanel.thumbs != null) expandPanel.stopThumbnail();
		this.dispose();
	}
	
	public void expandThumbnailPanel(){
		if(split.getDividerLocation() > 0)
			if(split.getLeftComponent() == bep)
				dothumbs = false;
		dothumbs = !dothumbs;
		if(dothumbs){
			split.setLeftComponent(expandPanel);
			split.setDividerLocation(expandPanel.thumbs.getPreferredSize().width + scroll.getVerticalScrollBar().
                    getWidth() + 4);
		} else
			split.setDividerLocation(0);
	}
	
	public void expandBookmarkPanel(){
		if(split.getDividerLocation() > 0)
			if(split.getLeftComponent() == expandPanel)
				isBookmarkExpanded = false;
		isBookmarkExpanded = !isBookmarkExpanded;
		if(isBookmarkExpanded){
	        try {
				if(pdfFile.getOutline()!=null){
					bep = new BookmarkExtendedPanel(pdfFile, this);
					bep.fillOutline();
					split.setLeftComponent(bep);
					split.setDividerLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 4);
					
				}else bookmark.setEnabled(false);
				
			} catch (IOException e) {
				bookmark.setEnabled(false);
				return;
			}
		}else 
			split.setDividerLocation(0);
	}
	
	/**
	 * gets the current page from the PDFFile object and displays it and setting the defalult zoom
	 * tool of the PagePanel to false
	 */
	private void displayPage(){
		page = pdfFile.getPage(curPage);
        pPanel.useZoomTool(false);
        pPanel.showPage(page);
        pPanel.requestFocusInWindow();
        loadNextPage();
	}
	
	/**
	 * updates the page number when user moves to next page
	 */
	private void updatePageNo(){
		String s = "(" +curPage+ " of " +lastPage+ ")";
		lblPageno.setText(s);
	}
	
	/**
	 * updates the control panel icons by enabling or disabling icons depending on state of page
	 */
	private void updatePanel(){
		textField.setText(String.valueOf(curPage));
        updatePageNo();
		if(curPage > 1 && curPage < lastPage){
			lblUp.setEnabled(true);
			lblDown.setEnabled(true);
		} else if(curPage >= lastPage){
			lblDown.setEnabled(false);
			lblUp.setEnabled(true);
		} else if(curPage == 1){
			lblDown.setEnabled(true);
			lblUp.setEnabled(false);
		} else if(curPage == lastPage){
			lblDown.setEnabled(false);
			lblUp.setEnabled(false);
		}
		lblPrint.setEnabled(pdfFile.isPrintable());
		lblPageSetup.setEnabled(pdfFile.isPrintable());
		updateThumbnail();
	}
	
    /**
     * Posts the Page Setup dialog
     */
    public void doPageSetup() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pformat = pjob.pageDialog(pformat);
    }
    
    /**
     * A thread for printing in.
     */
    class PrintThread extends Thread {

        PDFPrintPage ptPages;
        PrinterJob ptPjob;

        public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
            ptPages = pages;
            ptPjob = pjob;
            setName(getClass().getName());
        }

        public void run() {
            try {
                ptPages.show(ptPjob);
                ptPjob.print();
            } catch (PrinterException pe) {
            	displayPrintError();
            }
            ptPages.hide();
        }
    }
    
    public void displayPrintError(){
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
		JOptionPane.showMessageDialog(this, "Printing Error", "Print Aborted", JOptionPane.INFORMATION_MESSAGE,  icon);
		
    }

    /**
     * Print the current document.
     */
    public void doPrint() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setJobName(lblTitle.getText());
        Book book = new Book();
        PDFPrintPage pages = new PDFPrintPage(pdfFile);
        book.append(pages, pformat, pdfFile.getNumPages());

        pjob.setPageable(book);
        if (pjob.printDialog()) {
            new PrintThread(pages, pjob).start();
        }
    }

	/**
	 * updates the zoom icons by enabling or disabling it depending on state of page and
	 * also sets the maximum and minimum zoom percentage to be 600 and 100 respectively 
	 */
	private void updateZoom(){
    	if(curZoomLevel < 100) curZoomLevel = 100;
		else if(curZoomLevel > 600) curZoomLevel = 600;
		if(curZoomLevel > 100 && curZoomLevel < 550 ){
			lblZoomout.setEnabled(true);
			lblZoomin.setEnabled(true);
		}else if(curZoomLevel <= 100){
			lblZoomin.setEnabled(true);
			lblZoomout.setEnabled(false);
		}else if(curZoomLevel >= 550){
			lblZoomin.setEnabled(false);
			lblZoomout.setEnabled(true);
		}
	}
	
	/**
	 * zooms into and out of the page depending on the zoom percentage
	 */
	private void zoom(){
	    page = pdfFile.getPage(curPage);
		double width = page.getWidth();
	    double height = page.getHeight();
	    double zwidth = (width * curZoomLevel) / 100;
	    double zheight = (height * curZoomLevel) / 100; 
	    pPanel.setPreferredSize(new Dimension((int)zwidth, (int)zheight));
	    pPanel.setBounds(0, 0, (int)zwidth, (int)zheight);
        pPanel.useZoomTool(false);
        pPanel.showPage(page);
        pPanel.requestFocus();
	}
	
	private void customZoomFitWidth(){
	    page = pdfFile.getPage(curPage);
	    double aspectRatio = (double) page.getAspectRatio();
		double zwidth = (double) (Toolkit.getDefaultToolkit().getScreenSize().width - 
				(sidePanel.getWidth() + scroll.getVerticalScrollBar().getWidth() + 10));
		double zheight = zwidth / aspectRatio;
	    pPanel.setPreferredSize(new Dimension((int)zwidth, (int)zheight));
	    pPanel.setBounds(0, 0, (int)zwidth, (int)zheight);
        pPanel.useZoomTool(false);
        pPanel.showPage(page);
        pPanel.requestFocus();
	}
	
	private void customZoomPageLevel(){
		page = pdfFile.getPage(curPage);
	    double aspectRatio = (double) page.getAspectRatio();
		double zheight = (double) (Toolkit.getDefaultToolkit().getScreenSize().height - 
				(panel.getHeight() + scroll.getHorizontalScrollBar().getHeight() + 10));
		double zwidth = zheight * aspectRatio;
	    pPanel.setPreferredSize(new Dimension((int)zwidth, (int)zheight));
	    pPanel.setBounds(0, 0, (int)zwidth, (int)zheight);
        pPanel.useZoomTool(false);
        pPanel.showPage(page);
        pPanel.requestFocus();
	}
	
	public void updateThumbnail(){
		if(dothumbs){
			expandPanel.thumbs.pageShown(curPage - 1);
		}
	}
	
	public void loadNextPage(){
		if (loader != null) {
			loader.quit();
        }
		loader = new PDFPageLoader(curPage, pdfFile, pPanel);
		loader.start();
	}

	public void gotoPage(int page) {
		if (page < 0) {
            page = 0;
        } else if (page >= pdfFile.getNumPages()) {
            page = pdfFile.getNumPages() - 1;
        }
		curPage = page + 1;
		displayPage();
		updatePanel();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent evt) {
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            doPrev();
        } else if (code == KeyEvent.VK_RIGHT) {
            doNext();
        } else if (code == KeyEvent.VK_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_HOME) {
            gotoPage(0);
        } else if (code == KeyEvent.VK_END) {
            gotoPage(pdfFile.getNumPages() - 1);
        } else if (code == KeyEvent.VK_PAGE_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_PAGE_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_SPACE) {
            doNext();
        } else if (code == KeyEvent.VK_ESCAPE) {
        	
        }		
	}

	private void doNext() {
    	if(curPage < lastPage){
            curPage++;
            displayPage();
        	updatePanel(); 
    	}
	}

	private void doPrev() {
    	if(curPage > 1){
            curPage--;
            displayPage();
        	updatePanel();
    	}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
