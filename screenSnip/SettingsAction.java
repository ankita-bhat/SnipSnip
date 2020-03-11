package screenSnip;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SettingsAction extends JPanel implements ActionListener {

    private String tmpPath, tmpSaveWithBorders;
    private JFrame settingsFrame;
    private JTextField txtFldFilePath;
    private ButtonGroup btnGrpBorderYN;
    private JFileChooser fc;

    /**
     * Constructor
     */
    public SettingsAction() {

	JButton btnChoosePath = initializeSavePathFields();

	// Initialize With Border option
	JRadioButton borderYes = new JRadioButton("Yes");
	borderYes.setActionCommand("Yes");
	JRadioButton borderNo = new JRadioButton("No");
	borderNo.setActionCommand("No");
	btnGrpBorderYN = new ButtonGroup();
	btnGrpBorderYN.add(borderYes);
	btnGrpBorderYN.add(borderNo);

	if (ScreenSnipHome.saveWithBorders) {
	    borderYes.setSelected(true);
	    tmpSaveWithBorders = "true";
	} else {
	    borderNo.setSelected(true);
	    tmpSaveWithBorders = "false";
	}

	// Initialize Labels
	JLabel labelFilePath = new JLabel("Screen Snip Save Location: ");
	labelFilePath.setLabelFor(txtFldFilePath);
	labelFilePath.setAlignmentX(LEFT_ALIGNMENT);

	JLabel labelBorderYN = new JLabel("Save Image with Borders");
	labelBorderYN.setLabelFor(borderYes);

	JButton btnConfirmOk = new JButton("OK");
	btnConfirmOk.setActionCommand("CONFIRM");
	btnConfirmOk.addActionListener(this);

	JButton btnCancel = new JButton("Cancel");
	btnCancel.setActionCommand("CANCEL");
	btnCancel.addActionListener(this);

	addComponentsToFrameBody(btnChoosePath, borderYes, borderNo, labelFilePath, labelBorderYN, btnConfirmOk,
		btnCancel);

    }

    /**
     * Initializes Save Path Fields based on Property File
     * 
     * @return
     */
    private JButton initializeSavePathFields() {
	// Do not need to read property file since already read when app loads
	if (ScreenSnipHome.savePath == null || ScreenSnipHome.savePath.equals("")) {
	    tmpPath = ScreenSnipHelper.getDefaultSavePath();
	} else {
	    tmpPath = ScreenSnipHome.savePath;
	}

	txtFldFilePath = new JTextField(tmpPath, 30);
	txtFldFilePath.setActionCommand("CLICKED");
	txtFldFilePath.addActionListener(this);
	txtFldFilePath.setAlignmentX(LEFT_ALIGNMENT);
	txtFldFilePath.setEditable(false);
	txtFldFilePath.setToolTipText("This is where the snapshot will be stored. Change "
		+ "based on Select button, or directly via .properties file");

	fc = new JFileChooser(tmpPath);
	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	JButton btnChoosePath = new JButton("Select");
	btnChoosePath.addActionListener(this);
	btnChoosePath.setActionCommand("CHOOSE_PATH");
	return btnChoosePath;
    }

    /**
     * Adds components using default add method
     * 
     * @param btnChoosePath
     * @param borderYes
     * @param borderNo
     * @param labelFilePath
     * @param labelBorderYN
     * @param btnConfirmOk
     * @param btnCancel
     */
    private void addComponentsToFrameBody(JButton btnChoosePath, JRadioButton borderYes, JRadioButton borderNo,
	    JLabel labelFilePath, JLabel labelBorderYN, JButton btnConfirmOk, JButton btnCancel) {
	add(labelFilePath);
	add(txtFldFilePath);
	add(btnChoosePath);

	add(labelBorderYN);
	add(borderYes);
	add(borderNo);

	add(btnConfirmOk);
	add(btnCancel);
    }

    /**
     * Opens settings box, makes content visible
     */
    protected void openSettingsBox() {

	settingsFrame = new JFrame("SnipSnip - Settings");

	settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	settingsFrame.setPreferredSize(new Dimension(450, 150));

	settingsFrame.pack();
	settingsFrame.setContentPane(this);
	settingsFrame.setVisible(true);
	settingsFrame.setAlwaysOnTop(true);
	settingsFrame.setAutoRequestFocus(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

	if ("CHOOSE_PATH".equals(e.getActionCommand())) {
	    // ALT: or e.getSource() == btnChoosePath
	    // This just sets the path in the text field. Will not finalize until CONFIRM
	    // button is pressed
	    int returnVal = fc.showOpenDialog(SettingsAction.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		String path = fc.getSelectedFile().getPath();
		path = path.replace("\\", "/");
		path += "/"; // adding slash to ending if needed
		path = path.replace("//", "/"); // correcting just in case

		txtFldFilePath.setText(path);

	    } else {
		System.out.println("User cancelled to choose ");
	    }

	} else if ("CONFIRM".equals(e.getActionCommand())) {

	    // Update tmpPath and tmpSaveWithBorders
	    tmpPath = txtFldFilePath.getText();
	    tmpSaveWithBorders = getSelectedButtonValue(btnGrpBorderYN);
	    // ALT: If Action Command is added on buttons, then
	    // btnGrpBorderYN.getSelection().getActionCommand();

	    saveNewSettings();
	} else if ("CANCEL".equals(e.getActionCommand())) {

	    // tmpPath is whatever was the value before
	    rejectNewSettings();

	}

    }

    private String getButtonValueFromTxt(String btnTxt) {
	if (btnTxt != null && btnTxt.equalsIgnoreCase("Yes")) {
	    return "true";
	}
	return "false";
    }

    /**
     * Get which Radio Button was selected
     * 
     * @param buttonGroup
     * @return
     */
    private String getSelectedButtonValue(ButtonGroup buttonGroup) {
	for (Enumeration<AbstractButton> btnsInGrp = buttonGroup.getElements(); btnsInGrp.hasMoreElements();) {
	    AbstractButton tmBtn = btnsInGrp.nextElement();

	    if (tmBtn.isSelected()) {
		return getButtonValueFromTxt(tmBtn.getText());
	    }
	}

	return null;
    }

    /**
     * After user presses CONFIRM, saves user settings to .properties file
     */
    private void saveNewSettings() {
	// Set default path just in case user chooses not to select a path
	// ScreenSnipHome.savePath = pathValue;
	File configFile = new File(ScreenSnipHelper.getPathSettingsFile());

	try {

	    File directory = new File(tmpPath);
	    // if (! directory.exists()){
	    directory.mkdirs();
	    // Use .mkdir for creating only one folder.
	    // User .mkdirs for making entire path including parents
	    // } // Not needed to check if it exists or not

	    Properties props = new Properties();
	    props.setProperty("savePath", tmpPath);
	    props.setProperty("saveWithBorders", tmpSaveWithBorders);

	    FileWriter writer = new FileWriter(configFile);
	    props.store(writer, "Property File Updated ");
	    writer.close();

	    ScreenSnipHome.savePath = tmpPath;
	    ScreenSnipHome.saveWithBorders = tmpSaveWithBorders == "true" ? true : false;

	} catch (FileNotFoundException ex) {
	    String errMsg = "Unable to save properties File";
	    JOptionPane.showMessageDialog(this, errMsg, "SnipSnip Error", JOptionPane.ERROR_MESSAGE);
	    // System.out.println("File not found ");

	} catch (IOException ex) {
	    String errMsg = "Unknown File Read Write error";
	    JOptionPane.showMessageDialog(this, errMsg, "SnipSnip Error", JOptionPane.ERROR_MESSAGE);
	    // ex.printStackTrace();
	}

	if (settingsFrame != null) {
	    settingsFrame.dispose();
	}

    }

    /**
     * 
     */
    private void rejectNewSettings() {
	// do nothing
	// dispose frame

	if (ScreenSnipHome.savePath == null || ScreenSnipHome.savePath.equals("")) {

	    File directory = new File(tmpPath);
	    directory.mkdirs();

	    // show dialog box that you are proceeding with default settings
	    // storing to default path, and border of img Yes
	    ScreenSnipHome.savePath = tmpPath;

	    String infoMsg = "Proceeding with Default Settings: \n\n";
	    infoMsg += " Save Location: \n" + tmpPath;
	    infoMsg += "\n\n Save with Borders: " + ("true".equalsIgnoreCase(tmpSaveWithBorders) ? "Yes" : "No");
	    infoMsg += "\n\nYou may change this in Settings";

	    JOptionPane.showMessageDialog(this, infoMsg);
	}

	if (settingsFrame != null) {
	    settingsFrame.dispose();
	}
    }

}
