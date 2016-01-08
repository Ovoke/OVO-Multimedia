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

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * @author ovokerie
 * Creates the main window and all the components(menus, menu items, labels, panels and the
 * quote animation). The icons are represented by labels
 * 
 * The listeners for the labels(icons) are added and implemented. Also all native Libraries
 * (vlcj) which are open source and not written in java are loaded. VLCJ plays the audio and video files.
 * 
 * The class extends JFrame and implements ActionListener(to respond to events fired by the menu item buttons)
 *
 */
public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020820564713511149L;
	private MainPanel contentPane;
	private FileNameExtensionFilter filter;
	Cursor hand;
	JMenuBar menuBar;
	JLabel lblMusic, lblMyMusic, lblBooks, 
		   lblMyBooks, lblPicture, lblMyPictures, lblVideo, 
		   lblMyVideos, lblShelf, lblShelf_1, lblShelf_2, lblUndo;
	MusicPanel musicPanel;
	private JMenu mnFile, mnEdit, mnView, mnTools, mnHelp, mnGoTo;
	private JMenuItem mntmNew, mntmExit, mntmShowUrl, mntmMyMusic, mntmMyBooks, mntmMyVideos, mntmMyPictures,
			mntmUrlMedia, mntmInfo, mntmAbout;
	private boolean toogle;
	private PicturePanel picPanel;
	private FullPicturePanel fPicPanel;
    JFileChooser fileChooser;
    static String filePath;
    String[] music = {"mp3", "m4a",  "wma",  "wav", "a52", "aac", "dts", "oma", "spx", "flac",
			"mp1", "xm", "ace", "mod", "mp2", "mka"};
    String[] video = {"avi", "mp4", "3gp", "mkv", "flv", "divx", "vob",  "mpeg", "mpeg1", "mpeg2", "wmv", "mov", "mpeg4",
    		"mts","ogm", "dv", "m1v", "m2ts", "ts", "vlc", "3g2", "mpg", "ogg",  "m2v", "m4v", "asf"};
    String[] image = {"jpg", "png", "gif", "jpeg"};
	
	public static void main(String[] args) {
		//loads the vlcj native libraries from the file path C:\\Program Files\\OVO Multimedia\\lib which
		//is created on installation of program
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\OVO Multimedia");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		//set look and feel to that of windows
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * starts the sequence of initializing and creating the main frame of the application
	 */
	public MainFrame() {
		create();
		addListeners();
		setVisible(true);
	}	
	
	//creates the components of the frame (initializes and sets the properties of the component)
	private void create(){
		//creates a hand cursor which is passed to the icons(labels) when cursor hovers over it
		hand = new Cursor(Cursor.HAND_CURSOR);
		toogle = true;
		
		//sets the properties of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 581);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("OVO multimedia");
		setIconImage(new ImageIcon(getClass().getResource("/images/icons/logo.png")).getImage());
		
		//creates menu bar and sets it on the frame
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//creates a file menu and adds it to the menu bar
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		//creates the new menu item, sets its properties and adds it to the file menu
		mntmNew = new JMenuItem("New");
		mntmNew.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnNew.png")));
		mntmNew.addActionListener(this);
		mntmNew.setActionCommand("new");
		mnFile.add(mntmNew);

		//creates the exit menu item, sets its properties and adds it to the exit menu
		mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnExit.png")));
		mntmExit.addActionListener(this);
		mntmExit.setActionCommand("exit");
		mnFile.add(mntmExit);

		//creates an edit menu and adds it to the menu bar
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		//creates the show file details menu item, sets its properties and adds it to the edit menu		
		mntmShowUrl = new JMenuItem("Show File Details");
		mntmShowUrl.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnDetails.png")));
		mntmShowUrl.addActionListener(this);
		mntmShowUrl.setActionCommand("show_details");
		mnEdit.add(mntmShowUrl);

		//creates a view menu and adds it to the menu bar		
		mnView = new JMenu("View");
		menuBar.add(mnView);

		//creates a go to sub-menu and adds it to the view menu item		
		mnGoTo = new JMenu("Go To");
		mnGoTo.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnGoto.png")));
		mnView.add(mnGoTo);
		
		//creates a my music sub-menu item, sets its property and adds it to the go to sub-menu
		mntmMyMusic = new JMenuItem("My Music");
		mntmMyMusic.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnMusic.png")));
		mntmMyMusic.addActionListener(this);
		mntmMyMusic.setActionCommand("my_music");
		mnGoTo.add(mntmMyMusic);

		//creates a my books sub-menu item, sets its property and adds it to the go to sub-menu
		mntmMyBooks = new JMenuItem("My Books");
		mntmMyBooks.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnBook.png")));
		mntmMyBooks.addActionListener(this);
		mntmMyBooks.setActionCommand("my_books");
		mnGoTo.add(mntmMyBooks);

		//creates a my pictures sub-menu item, sets its property and adds it to the go to sub-menu
		mntmMyPictures = new JMenuItem("My Pictures");
		mntmMyPictures.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnPictures.png")));
		mntmMyPictures.addActionListener(this);
		mntmMyPictures.setActionCommand("my_pictures");
		mnGoTo.add(mntmMyPictures);

		//creates a my videos sub-menu item, sets its property and adds it to the go to sub-menu
		mntmMyVideos = new JMenuItem("My Videos");
		mntmMyVideos.setIcon(new ImageIcon(getClass().getResource("/images/icons/mnMovie.png")));
		mntmMyVideos.addActionListener(this);
		mntmMyVideos.setActionCommand("my_videos");
		mnGoTo.add(mntmMyVideos);
		
		//creates a tool menu adds  adds it to the menu bar
		mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		//creates a url media menu item, sets its properties and adds it to the tools menu
		mntmUrlMedia = new JMenuItem("URL Media");
		mntmUrlMedia.setIcon(new ImageIcon("C:\\Users\\Public\\Pictures\\icons\\mnUrlMedia.png"));
		mntmUrlMedia.addActionListener(this);
		mntmUrlMedia.setActionCommand("url_media");
		mnTools.add(mntmUrlMedia);

		//creates a help menu adds  adds it to the menu bar
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		//creates a information menu item, sets its properties and adds it to the help menu
		mntmInfo = new JMenuItem("Information");
		mntmInfo.setIcon(new ImageIcon("C:\\Users\\Public\\Pictures\\icons\\mnInfo.png"));
		mntmInfo.addActionListener(this);
		mntmInfo.setActionCommand("info");
		mnHelp.add(mntmInfo);

		//creates a about menu item, sets its properties and adds it to the help menu		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setIcon(new ImageIcon("C:\\Users\\Public\\Pictures\\icons\\mnAbout.png"));
		mntmAbout.addActionListener(this);
		mntmAbout.setActionCommand("about");
		mnHelp.add(mntmAbout);
		
		//creates and sets the panel of the frame. The panel is a wooden panel implemented
		//in the MainPanel class
		contentPane = new MainPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//creates and sets the properties of the music icon
		lblMusic = new JLabel("");
		lblMusic.setIcon(new ImageIcon(getClass().getResource("/images/icons/Music2.png")));
		lblMusic.setBounds(10, 55, 128, 96);
		lblMusic.setToolTipText("Open music files");
		lblMusic.setCursor(hand);
		contentPane.add(lblMusic);

		//creates and sets the properties of the books icon
		lblBooks = new JLabel("");
		lblBooks.setIcon(new ImageIcon(getClass().getResource("/images/icons/Document.png")));
		lblBooks.setBounds(148, 51, 139, 105);
		lblBooks.setToolTipText("Open pdf files");
		lblBooks.setCursor(hand);
		contentPane.add(lblBooks);

		//creates and sets the properties of the picture icon
		lblPicture = new JLabel("");
		lblPicture.setIcon(new ImageIcon(getClass().getResource("/images/icons/Pictures2.png")));
		lblPicture.setBounds(10, 187, 128, 105);
		lblPicture.setToolTipText("Open image files");
		lblPicture.setCursor(hand);
		contentPane.add(lblPicture);

		//creates and sets the properties of the video icon
		lblVideo = new JLabel("");
		lblVideo.setIcon(new ImageIcon(getClass().getResource("/images/icons/Movies2.png")));
		lblVideo.setBounds(148, 187, 134, 105);
		lblVideo.setToolTipText("Open video files");
		lblVideo.setCursor(hand);
		contentPane.add(lblVideo);
		
		//creates and set the properties of the picture icon title
		lblMyPictures = new JLabel("My Pictures");
		lblMyPictures.setFont(new Font("Aharoni", Font.BOLD, 16));
		lblMyPictures.setBounds(10, 164, 114, 22);
		contentPane.add(lblMyPictures);

		//creates and set the properties of the video icon title
		lblMyVideos = new JLabel("My Videos");
		lblMyVideos.setFont(new Font("Aharoni", Font.BOLD, 16));
		lblMyVideos.setBounds(148, 168, 103, 14);
		contentPane.add(lblMyVideos);

		//creates and set the properties of the books icon title
		lblMyBooks = new JLabel("My Books");
		lblMyBooks.setFont(new Font("Aharoni", Font.BOLD, 16));
		lblMyBooks.setBounds(148, 33, 84, 14);
		contentPane.add(lblMyBooks);

		//creates and set the properties of the music icon title
		lblMyMusic = new JLabel("My Music");
		lblMyMusic.setFont(new Font("Aharoni", Font.BOLD, 16));
		lblMyMusic.setBounds(10, 33, 84, 14);
		contentPane.add(lblMyMusic);
		
		//creates an icon below the animated picture quote to toogle between image 
		//animation modes
		lblUndo = new JLabel("");
		lblUndo.setIcon(new ImageIcon(getClass().getResource("/images/icons/undo.png")));
		lblUndo.setBounds(525, 440, 34, 37);
		lblUndo.setToolTipText("Toogle between image animation mode");
		lblUndo.setCursor(hand);
		contentPane.add(lblUndo);
		
		//displays and starts the animation of quotes 
		animation();
	}
	
	/**
	 * all icons on the frame are activated to listen for mouse clicked events which makes 
	 * a sound to notify users it has been clicked and carries out specified function
	 */
	private void addListeners(){
		lblMusic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	makeSound();
            	music();
            }
		});
		
		lblBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	makeSound();
            	books();
            }
		});
		
		lblPicture.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	makeSound();
            	picture();
            }
		});
		
		lblVideo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	makeSound();
            	video();
            }
		});
		
		lblUndo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	toogle = !toogle;
            	animation();
            }
		});
	}
	
	/**
	 * creates a panel of animated quote depending on the state of the toogle variable which is initiallly 
	 * set to true.
	 * 
	 * if toogle is true the panel consisting of various quotes is shown with its animation implemented in
	 *   the PicturePanel class
	 * if toogle is false the panel class consisting of a single quote is shown with its animation implemented 
	 *   in the FullPicturePanel class
	 */
	private void animation(){		
		if(toogle){
			if(fPicPanel!=null)				
				fPicPanel.end();
			picPanel = new PicturePanel(contentPane);
		}
		if(!toogle){
			picPanel.end();
			fPicPanel = new FullPicturePanel(contentPane);
		}
	}
	
	/**
	 * plays a sound every time an icon is clicked
	 */
	private void makeSound(){
		 AudioClip clip = Applet.newAudioClip(getClass().getResource("/images/sound/sound.wav"));
		 clip.play();
	}
	
	/**
	 * creates a file chooser for user to select a file from filesystem and the path
	 * of the selected file is returned as a string to a filePath variable
	 */
	public void createPanel() {
		 fileChooser = new JFileChooser();
		 filePath = null;
		 File file = null;
		 
	     fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	     fileChooser.setFileFilter(filter);
	     int returnVal = fileChooser.showOpenDialog(this);
	     if (returnVal == JFileChooser.APPROVE_OPTION){
	    	file = fileChooser.getSelectedFile();
	     	filePath = file.getAbsolutePath();
	     }
	}
	
	/**
	 * checks to see if the file is actually in the file path given by the file chooser, if
	 * present it returns the path as a string else an error message is shown and a null 
	 * vale returned
	 * 
	 * @param String path
	 * @return String
	 */
	private String validateFile(String path){
		if(path == null) return null;
		Path fileName = Paths.get(path);
		if(!Files.exists(fileName)){
		   ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
		   JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.INFORMATION_MESSAGE,  icon);
 		   return null;
		}
		return path;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//responds to the new menu item
		if(e.getActionCommand()=="new"){
			String[] possibilities = {"Music", "Books", "Picture", "Video"};
			
			//gets the selected string from the string array of possibilities (using the JOptionPane 
			//as the user interface) and stores it in the String variable s
            String s = (String)JOptionPane.showInputDialog(this, "Please Select type of file to open",
                                "Media Type", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Music");
            if(s == null) return;
            
            //if a string was selected the user also types in a valid file path and the value is stored in the 
            //string variable t
            else {
            	String t = (String)JOptionPane.showInputDialog(this, "Give the path of file", "Location",
                        JOptionPane.PLAIN_MESSAGE, null, null, "C:\\");
            	if(validateFile(t) == null) return;
            	
            	//if both are valid the proper class is created to read the file
            	else {
            		if(s=="Music") new MusicPanel(t, contentPane);
            		else if(s=="Books") new BooksFrame(t);
            		else if(s=="Picture") new ImageFrame(t);
            		else if(s=="Video") new FullVideoFrame(t);
            	}
            }
		
        //responds to the exit menu item    
		}else if(e.getActionCommand()=="exit"){
			System.exit(JFrame.EXIT_ON_CLOSE);
		
		//responds to the show details menu item to show the details of selected file
		}else if(e.getActionCommand()=="show_details"){
			if(filePath == null) return;
			
			//creates a Path class from the filePath making it easier to get properties of 
			//a file in that path
			Path fileName = Paths.get(filePath);
			long size;
			String name, type, location;
			FileTime created, modified, accessed;
			
			//gets the name of a file
			name = fileName.getFileName().toString();
			
			//gets the string value after the dot in the file path (file extension) to give the type of file
	        int dot = filePath.lastIndexOf(".");
	        type = filePath.substring(dot + 1);
	        
	        //gets the string value of the Path
			location = fileName.toString();
			BasicFileAttributes attr;
			
			try{
				size = Files.size(fileName);			
				attr = Files.readAttributes(fileName, BasicFileAttributes.class);
			}catch(Exception ex){
				ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
				JOptionPane.showMessageDialog(this, "Problem accessing file attributes", "Error",
					    JOptionPane.INFORMATION_MESSAGE,  icon);
				return;
			}

			created = attr.creationTime();
			modified = attr.lastAccessTime();
			accessed = attr.lastModifiedTime();
			
	 		JOptionPane.showMessageDialog(this, "Name:    " +name+ "\nType of File:    ." +type+ "\nLocation:    " 
	 				+location+ "\nSize:    " +size+ "bytes\n\nCreated:    " +created+ "\nModified:    " 
	 				+modified+ "\nAccessed:    " +accessed); 
		
	 	//responds to the picture menu item
		}else if(e.getActionCommand()=="my_pictures"){
			picture();

		 //responds to the video menu item	
		}else if(e.getActionCommand()=="my_videos"){
			video();

		 //responds to the music menu item
		}else if(e.getActionCommand()=="my_music"){
			music();

		 //responds to the books menu item
		}else if(e.getActionCommand()=="my_books"){
			books();

		 //responds to the url media menu item with its function similar to that of the new menu item
		}else if(e.getActionCommand()=="url_media"){
			String[] possibilities = {"Music", "Books", "Picture", "Video"};
            String s = (String)JOptionPane.showInputDialog(this, "Please Select type of file to open",
                                "URL Media", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Music");
            if(s == null) return;
            else {
            	String t = (String)JOptionPane.showInputDialog(this, "Give the path of file", "URL Media",
                        JOptionPane.PLAIN_MESSAGE, null, null, "");
            	if(validateFile(t) == null) return;
            	else {
            		if(s=="Music") new MusicPanel(t, contentPane);
            		else if(s=="Books") new BooksFrame(t);
            		else if(s=="Picture") new ImageFrame(t);
            		else if(s=="Video") new FullVideoFrame(t);
            	}
            }
		
        //responds to the information menu item to show the supported file type
		}else if(e.getActionCommand()=="info"){
			ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/logo.png"));
			JOptionPane.showMessageDialog(this, "Files Supported are:\n\n\nMusic Files:\n a52, aac, dts, oma, spx, flac, m4a,"
    			+ "mp1, wav, \nxm, ace, mod, mp2, mp3, wma, mka"+"\n\nBook Files:\n only pdf\n\nImage Files:\n jpg, png, gif, jpeg"
				+ "\n\nVideo File:\nm2v, m4v, mpeg1, mpeg2, mts, ogm, divx, dv, \nflv, m1v, m2ts, mkv, mov, mpeg4, ts, vob, vlc,"
    			+ "3g2, \navi, mpeg, mpg, ogg, 3gp, wmv, asf, mp4", "Information",
				    JOptionPane.INFORMATION_MESSAGE,  icon);
			
		}else if(e.getActionCommand()=="about"){
			ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/logo.png"));
			JOptionPane.showMessageDialog(this, "oVO multimedia\nVersion 1.0.0.0\n\nCopyright 2015 Ogbeta Ovokerie\nAll"
					+ " right reserved\n\nFor further enquires email\nogbetaovokerie@gmail.com\n\nENJOY", "About",
				    JOptionPane.INFORMATION_MESSAGE,  icon);
			
		}
	}
	
	//the following functions creates the specific file chooser by passing in the appropriate filter variable
	//having the specified file extensions, validates the file and calls on the appropriate class to read file
	private void music(){
    	filter = new FileNameExtensionFilter("Music Files", "a52", "aac", "dts", "oma", "spx", "flac", "m4a",
    			"mp1", "wav", "xm", "ace", "mod", "mp2", "mp3", "wma", "mka");
    	createPanel();
    	if(validateFile(filePath) == null) return;
    	if(!validateMusic())
    		displayError();    	
    	if(MainFrame.filePath!=null && validateMusic())
    		new MusicPanel(MainFrame.filePath, contentPane);
	}
	
	private void books(){
    	filter = new FileNameExtensionFilter("PDF Files", "pdf");
    	createPanel();
    	if(validateFile(filePath) == null) return;
    	if(!validateBook())
    		displayError();
    	if(MainFrame.filePath!=null && validateBook())
    		new BooksFrame(MainFrame.filePath);		
	}
	
	private void video(){
    	filter = new FileNameExtensionFilter("Video Files", "m2v", "m4v", "mpeg1", "mpeg2", "mts",
    			"ogm", "divx", "dv", "flv", "m1v", "m2ts", "mkv", "mov", "mpeg4", "ts", "vob", "vlc",
    			"3g2", "avi", "mpeg", "mpg", "ogg", "3gp", "wmv", "asf", "mp4");
    	createPanel();
    	if(validateFile(filePath) == null) return;
    	if(!validateVideo())
    		displayError();
    	if(MainFrame.filePath!=null && validateVideo())
    		new FullVideoFrame(MainFrame.filePath);		
	}
	
	private void picture(){
    	filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
    	createPanel();
    	if(validateFile(filePath) == null) return;
    	if(!validateImage())
    		displayError();
    	if(MainFrame.filePath!=null && validateImage())
    		new ImageFrame(MainFrame.filePath);		
	}
	
	//the following functions validates the music image video and book file path by comparing the string after
	//the dot (file extension) with the array of supported file type and returns a boolean value
	private boolean validateMusic(){
        int dot = filePath.lastIndexOf(".");
        String type = filePath.substring(dot + 1);
        for(String s : music)
        	if(type.equalsIgnoreCase(s)) return true;
        return false;
	}

	private boolean validateImage(){
		 int dot = filePath.lastIndexOf(".");
	     String type = filePath.substring(dot + 1);
	     for(String s : image)
	       	if(type.equalsIgnoreCase(s)) return true;
	     return false;
	}

	private boolean validateVideo(){
		 int dot = filePath.lastIndexOf(".");
	     String type = filePath.substring(dot + 1);
	     for(String s : video)
	       	if(type.equalsIgnoreCase(s)) return true;
	     return false;		
	}

	private boolean validateBook(){
		 int dot = filePath.lastIndexOf(".");
	     String type = filePath.substring(dot + 1);
	     if(type.equalsIgnoreCase("pdf")) return true;
	     return false;		
	}
	
	//displays when a file type is not supported
	private void displayError(){
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
		JOptionPane.showMessageDialog(this, "File type not supported\n\nGoTo Help > Information to see supported file type", 
				"Error", JOptionPane.INFORMATION_MESSAGE,  icon);
	}
}
