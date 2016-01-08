package ng.co.ovo.Multimedia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;

public class BookmarkExtendedPanel extends JPanel implements TreeSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184378087062460496L;
	PDFFile curFile;
	OutlineNode outline;
	JPanel titlePanel, panel, emptyPanel;
	JLabel titleLabel, lblClose, lblExpand;
	BooksFrame booksFrame;

	Border paddingBorder, border;
	
	public BookmarkExtendedPanel(PDFFile pdfFile, BooksFrame booksFrame){
		setLayout(new BorderLayout(0, 0));
		paddingBorder = BorderFactory.createEmptyBorder(12, 0, 12, 0);
		border = BorderFactory.createLineBorder(Color.GRAY);
		curFile = pdfFile;
		this.booksFrame = booksFrame;

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
		
		titleLabel = new JLabel("Bookmarks");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Aharoni", Font.BOLD, 12));
		titleLabel.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
		titlePanel.add(titleLabel, BorderLayout.WEST);
	}
	
	public void fillOutline(){
		  try {
	            outline = curFile.getOutline();
	            
	        } catch (IOException ioe) {
				ImageIcon icon = new ImageIcon(getClass().getResource("/images/icons/sadIcon.gif"));
				JOptionPane.showMessageDialog(this, "Sorry could not read bookmarks from PDF file", "Bookmark Error",
						JOptionPane.INFORMATION_MESSAGE,  icon);
				
	        }
	        if (outline != null) {
	            if (outline.getChildCount() > 0) {
	                JTree jt = new JTree(outline);
	                
		            CustomDefaultRenderer dtcr = new CustomDefaultRenderer();
		            jt.setCellRenderer(dtcr);
		            dtcr.setLeafIcon(new ImageIcon(getClass().getResource("/images/icons/leafnode.png")));
		            dtcr.setOpenIcon(new ImageIcon(getClass().getResource("/images/icons/node.png")));
		            dtcr.setClosedIcon(new ImageIcon(getClass().getResource("/images/icons/node.png")));
		            
	                jt.setRootVisible(false);
	                jt.addTreeSelectionListener(this);
	                JScrollPane jsp = new JScrollPane(jt);
	                add(jsp, BorderLayout.CENTER);
	                setVisible(true);
	                
	            } else {
	                if (this != null) {
	                	setVisible(false);
	                }
	            }
	        }
	}

	@Override
    /**
     * Someone changed the selection of the outline tree.  Go to the new
     * page.
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            OutlineNode node = (OutlineNode) e.getPath().getLastPathComponent();
            if (node == null) {
                return;
            }

            try {
                PDFAction action = node.getAction();
                if (action == null) {
                    return;
                }

                if (action instanceof GoToAction) {
                    PDFDestination dest = ((GoToAction) action).getDestination();
                    if (dest == null) {
                        return;
                    }

                    PDFObject page = dest.getPage();
                    if (page == null) {
                        return;
                    }

                    int pageNum = curFile.getPageNumber(page);
                    if (pageNum >= 0) {
                        booksFrame.gotoPage(pageNum);
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
	
	protected static class CustomDefaultRenderer extends DefaultTreeCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4934507053771986315L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, 
				boolean leaf, int row, boolean hasFocus){
			Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			
			if(leaf){
				if(leafForeground != null){
					c.setForeground(leafForeground);
					
				}
			}
			return c;
		}
		
		public void setLeafForeground(Color color){
			this.leafForeground = color;
		}
		
		private Color leafForeground;
	}
}
