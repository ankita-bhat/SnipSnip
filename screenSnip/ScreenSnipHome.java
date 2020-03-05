package screenSnip;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScreenSnipHome extends JPanel implements ActionListener {
    protected JButton btnNewSnip, btnOpenSettings;
    protected int keyPressed;

    /**
     * Constructor
     */
    public ScreenSnipHome() {
	
	ImageIcon imgIcon = ScreenSnipHelper.createImageIcon("/images/lookies.gif");
	
	btnNewSnip = new JButton(ScreenSnipConstants.LABEL_NEW_SNIP, imgIcon);
	btnNewSnip.setVerticalTextPosition(AbstractButton.CENTER);
	btnNewSnip.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
	btnNewSnip.setMnemonic(KeyEvent.VK_D);
	btnNewSnip.setActionCommand(ScreenSnipConstants.ACTION_NEW_SNIP);
 
	btnOpenSettings = new JButton(ScreenSnipConstants.LABEL_OPEN_SETTINGS);
	btnOpenSettings.setMnemonic(KeyEvent.VK_M);
	btnOpenSettings.setActionCommand(ScreenSnipConstants.ACTION_OPEN_SETTINGS);
	
	btnNewSnip.addActionListener(this);
        btnOpenSettings.addActionListener(this);
        
        btnNewSnip.setToolTipText(ScreenSnipConstants.TOOLTIP_NEW_SNIP);
        btnOpenSettings.setToolTipText(ScreenSnipConstants.TOOLTIP_OPEN_SETTINGS);
        
        add(btnNewSnip);
        add(btnOpenSettings);
    }
    
    @Override
    /**
     * Actions to perform based on Event (from : EventListener)
     */
    public void actionPerformed(ActionEvent e) {
	
	if (ScreenSnipConstants.ACTION_NEW_SNIP.equals(e.getActionCommand())) {
	    // open new snip
	    System.out.println(ScreenSnipConstants.ACTION_NEW_SNIP);
	    
	    NewSnip ns = new NewSnip();
	    ns.openNewSnipBox();
	    
	} else if (ScreenSnipConstants.ACTION_OPEN_SETTINGS.contentEquals(e.getActionCommand())) {
	    // open settings
	    System.out.println(ScreenSnipConstants.ACTION_OPEN_SETTINGS);
	    
	    
	}
    }

    /**
     * Create the GUI and show it. For thread safety, this method
     * should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        
	// Create and set up the window.
	JFrame frame = new JFrame(ScreenSnipConstants.LABEL_MAIN);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(300, 80));

	// Create and set up the content pane.
	ScreenSnipHome scrSnip = new ScreenSnipHome();
	scrSnip.setOpaque(true); //content panes must be opaque
        frame.setContentPane(scrSnip);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * The MAIN method.
     * @param args
     */
    public static void main(String[] args) {
	// Schedule a job for the event-dispatching thread:
	// creating and showing this application's GUI.
	
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		createAndShowGUI();
	    }
	});
    }


}
