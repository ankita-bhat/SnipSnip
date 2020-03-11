package screenSnip;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class NewSnip extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    private JFrame homeFrame, grayBgFrame, snipAreaFrame;
    private int startPointX, startPointY, endPointX, endPointY, imgWidth, imgHeight;
    private Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    // private boolean isMouseReleased = false;

    /**
     * Readies screen for Screenshot. This is called whenever "New Snip" button is
     * clicked from Main Menu. Initializes the Gray background to show ready state
     * for screenshot.
     */
    protected void openNewSnipBox(JFrame homeFrame) {

	this.homeFrame = homeFrame;
	homeFrame.setVisible(false);

	// Make a full screen JFrame - gray, then add Mouse Event Listener on it
	grayBgFrame = new JFrame();
	grayBgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	grayBgFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	grayBgFrame.setUndecorated(true);
	grayBgFrame.setResizable(true);

	com.sun.awt.AWTUtilities.setWindowOpacity(grayBgFrame, 0.5f);

	grayBgFrame.addMouseListener(this);
	grayBgFrame.addMouseMotionListener(this);

	// Display the window.
	grayBgFrame.pack();
	grayBgFrame.setVisible(true);

	// TODO: ESC key must close this frame!!
	// Currently any key press will close this frame
	grayBgFrame.addKeyListener(this);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
	// do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	// do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
	// do nothing
    }

    @Override
    /**
     * User mouse down event : When Mouse is pressed in gray background Area, new
     * Frame is created based on user click starting position (x, y)
     * 
     * 
     * Sequence 1 of 3
     */
    public void mousePressed(MouseEvent e) {

	startPointX = e.getX();
	startPointY = e.getY();

	if (snipAreaFrame == null) {
	    snipAreaFrame = new JFrame();
	    snipAreaFrame.setUndecorated(true); // Specifies I don't want menu bar

	    snipAreaFrame.setResizable(true);
	    snipAreaFrame.getRootPane().setBorder(border);
	    // snipAreaFrame.getContentPane().setBorder(BorderFactory.createLineBorder(Color.BLACK,
	    // 1));

	    snipAreaFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

	    snipAreaFrame.addMouseListener(this);
	    snipAreaFrame.addMouseMotionListener(this);
	    snipAreaFrame.addKeyListener(this);
	}
    }

    @Override
    /**
     * User mouse up event : Finalize window - auto save print screen to default
     * location. Remove the background gray Frame, finalize user snapshot area, call
     * takeScreenshot() method
     * 
     * Sequence 3 of 3
     */
    public void mouseReleased(MouseEvent e) {
	grayBgFrame.dispose();

	// Don't need frame at all since we are taking SS based on coordinates
	if (!ScreenSnipHome.saveWithBorders) {
	    snipAreaFrame.dispose();
	}

	takeScreenshot();
    }

    /** Save screenshot to Save Location */
    private void takeScreenshot() {

	try {

	    Robot robot = new Robot();
	    BufferedImage image = robot
		    .createScreenCapture(new Rectangle(startPointX, startPointY, imgWidth, imgHeight));

	    ScreenSnipHelper sshelper = new ScreenSnipHelper();
	    ImageIO.write(image, sshelper.getImageType(false), new File(sshelper.getFullFilePath()));

	} catch (IOException | AWTException e) {
	    String errMsg = "Exception while saving image ";
	    // System.out.println("Exception while saving image " +
	    // e.getLocalizedMessage());
	    JOptionPane.showMessageDialog(this, errMsg, "SnipSnip Error", JOptionPane.ERROR_MESSAGE);

	}

	resetAllFramesAndValues();
    }

    /**
     * Disposes off the snipping area after SS is taken. Resets dimensions for next
     * Screenshot
     */
    private void resetAllFramesAndValues() {

	if (snipAreaFrame != null) {
	    snipAreaFrame.dispose();
	}
	snipAreaFrame = null;

	startPointX = 0;
	startPointY = 0;
	endPointX = 0;
	endPointY = 0;
	imgWidth = 0;
	imgHeight = 0;

	// Main menu should be visible again
	this.homeFrame.setVisible(true);
    }

    @Override
    /**
     * User mouse drag event : Draws frame that changes dimensions based on mouse
     * location Sequence 2 of 3
     */
    public void mouseDragged(MouseEvent e) {

	endPointX = e.getX();
	endPointY = e.getY();

	imgWidth = endPointX - startPointX;
	imgHeight = endPointY - startPointY;

	snipAreaFrame.setBounds(startPointX, startPointY, imgWidth, imgHeight);
	snipAreaFrame.getRootPane().setBorder(border);
	snipAreaFrame.setVisible(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	// do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
	// Check for ESC

	int id = e.getID();

	if (id == KeyEvent.KEY_TYPED) {

	    grayBgFrame.dispose();
	    grayBgFrame = null;
//            Alt: frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

	    snipAreaFrame.dispose();
	    snipAreaFrame = null;

	}
    }

    // Keyboard handling
    @Override
    public void keyPressed(KeyEvent e) {
	// do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
	// do nothing
    }

}
