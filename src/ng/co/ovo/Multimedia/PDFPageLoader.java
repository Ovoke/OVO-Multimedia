package ng.co.ovo.Multimedia;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * A class to load the next page for better UI response
 */
public class PDFPageLoader extends Thread {

    int waitforPage;
    int prepPage;
    int curPage;
    PDFFile pdfFile;
    PagePanel pPanel;
    
    /**
     * Creates a new PagePreparer to prepare the page after the current
     * one.
     * @param waitforPage the current page number, 0 based 
     */
    public PDFPageLoader(int curPage, PDFFile pdfFile, PagePanel pPanel) {
        setDaemon(true);
        setName(getClass().getName());

        this.waitforPage = curPage - 1;
        this.curPage = curPage;
        this.prepPage = waitforPage + 1;
        this.pdfFile = pdfFile;
        this.pPanel = pPanel;
    }

    public void quit() {
        waitforPage = -1;
    }

    public void run() {
        Dimension size = null;
        Rectangle2D clip = null;

        if (pPanel != null) {
        	pPanel.waitForCurrentPage();
            size = pPanel.getCurSize();
            clip = pPanel.getCurClip();
        }
        
        if (waitforPage == curPage){
	        PDFPage pdfPage = pdfFile.getPage(prepPage + 1, true);
	        
	        if (pdfPage != null) {
	            pdfPage.getImage(size.width, size.height, clip, null, true, true);
	        }
        }
    }
}
