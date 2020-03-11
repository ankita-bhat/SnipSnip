package screenSnip;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ScreenSnipHome extends JPanel implements ActionListener {

    protected static JFrame homeFrame;
    protected static String savePath;
    protected static boolean saveWithBorders = true;

    /**
     * Constructor
     */
    public ScreenSnipHome() {

	JButton btnNewSnip, btnOpenSettings;
	ImageIcon imgIcon = ScreenSnipHelper.createSmileyIcon();

	btnNewSnip = new JButton(ScreenSnipConstants.LABEL_NEW_SNIP, imgIcon);
	btnNewSnip.setVerticalTextPosition(AbstractButton.CENTER);
	btnNewSnip.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales
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

	ScreenSnipHelper.readPropFile();
	initialiseFileLocation();
    }

    /**
     * Opens settings if save location is empty
     */
    private void initialiseFileLocation() {

	if (ScreenSnipHome.savePath == null || ScreenSnipHome.savePath.equals("")) {

	    JOptionPane.showMessageDialog(this, "Please choose Screen shot save location");

	    SettingsAction sa = new SettingsAction();
	    sa.openSettingsBox();
	}
    }

    @Override
    /**
     * Actions to perform based on Event (from : EventListener)
     */
    public void actionPerformed(ActionEvent e) {

	if (ScreenSnipConstants.ACTION_NEW_SNIP.equals(e.getActionCommand())) {
	    // open new snip

	    NewSnip ns = new NewSnip();
	    ns.openNewSnipBox(homeFrame);

	} else if (ScreenSnipConstants.ACTION_OPEN_SETTINGS.contentEquals(e.getActionCommand())) {
	    // open settings

	    SettingsAction sa = new SettingsAction();
	    sa.openSettingsBox();

	}
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked
     * from the event-dispatching thread.
     */
    private static void createAndShowGUI() {

	// Create and set up the window.
	homeFrame = new JFrame(ScreenSnipConstants.LABEL_MAIN);
	homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	homeFrame.setPreferredSize(new Dimension(300, 80));

	// Create and set up the content pane.
	ScreenSnipHome scrSnip = new ScreenSnipHome();
	scrSnip.setOpaque(true); // content panes must be opaque
	homeFrame.setContentPane(scrSnip);

	// Display the window.
	homeFrame.pack();
	homeFrame.setVisible(true);
    }

    /**
     * The MAIN method.
     * 
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
