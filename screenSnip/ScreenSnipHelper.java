package screenSnip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ScreenSnipHelper {

    /**
     * SETTINGS file path from XML
     */
    protected static String getPathSettingsFile() {
	String currentFolder = System.getProperty("user.dir");

	String propFulPath = currentFolder + "/prop/snipSnipProp.properties";

	return propFulPath;
    }

    /**
     * Returns path of Smiley Face (inside JAR)
     */
    protected static String getSmileyIconPath() {
	return "/images/lookies.gif";
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = ScreenSnipHelper.class.getResource(path);
	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    /**
     * Creates Smiley Face for New Snip
     */
    public static ImageIcon createSmileyIcon() {
	return createImageIcon(getSmileyIconPath());
    }

    /**
     * Returns system datime in YYYY-MM-DD-hh-mm-ss format
     * 
     * @return strDate
     */
    private String getCurrentStrDatime() {

	LocalDateTime localDatime = LocalDateTime.now();

	String strDate = localDatime.getYear() + String.format("-%02d", localDatime.getMonthValue())
		+ String.format("-%02d", localDatime.getDayOfMonth()) + String.format("-%02d", localDatime.getHour())
		+ String.format("-%02d", localDatime.getMinute()) + String.format("-%02d", localDatime.getSecond());

	return strDate;
    }

    /**
     * Returns name of snapshot Image based on System datime
     * 
     * @return imageName
     */
    protected String getImageName() {
	String imgName = "Img-" + getCurrentStrDatime();
	return imgName;
    }

    /**
     * Returns required image type (with or without dot)
     * 
     * @param withDot
     * @return imgType
     */
    protected String getImageType(boolean withDot) {
	String imgType;

	if (withDot) {
	    imgType = ".png";
	} else {
	    imgType = "png";
	}

	return imgType;
    }

    /**
     * Returns File Save Location from session
     * 
     * @return
     */
    protected String getFileLocation() {

	String imgPath;

	if (ScreenSnipHome.savePath == null || ScreenSnipHome.savePath.equals("")) {
	    imgPath = getDefaultSavePath();
	} else {
	    imgPath = ScreenSnipHome.savePath;
	}

	return imgPath;
    }

    /**
     * Returns default save path based on current user directory
     * 
     * @return
     */
    protected static String getDefaultSavePath() {

	String currentFolder = System.getProperty("user.dir");
	currentFolder = currentFolder.replace("\\", "/");

	String savePathDefault = currentFolder + "/output/";
	return savePathDefault;
    }

    /**
     * Returns full path including Path, Img name and Img type
     * 
     * @return
     */
    protected String getFullFilePath() {

	return getFileLocation() + getImageName() + getImageType(true);
    }

    /**
     * Reads Property file and sets Save Location and Border setting in session
     */
    protected static void readPropFile() {

	File configFile = new File(ScreenSnipHelper.getPathSettingsFile());

	try {
	    FileReader reader = new FileReader(configFile);
	    Properties props = new Properties();
	    props.load(reader);

	    ScreenSnipHome.savePath = props.getProperty("savePath");
	    ;

	    String saveWithBorders = props.getProperty("saveWithBorders");
	    if (saveWithBorders != null && saveWithBorders.equalsIgnoreCase("TRUE")) {
		ScreenSnipHome.saveWithBorders = true;
	    } else if (saveWithBorders != null && saveWithBorders.equalsIgnoreCase("FALSE")) {
		ScreenSnipHome.saveWithBorders = false;
	    }

	    reader.close();
	} catch (FileNotFoundException ex) {
	    String errMsg = "Properties File Not Found"; // can use null as frame as well
	    JOptionPane.showMessageDialog(ScreenSnipHome.homeFrame, errMsg, "SnipSnip Error",
		    JOptionPane.ERROR_MESSAGE);
	} catch (IOException ex) {
	    String errMsg = "Unknown File Read Write error";
	    JOptionPane.showMessageDialog(ScreenSnipHome.homeFrame, errMsg, "SnipSnip Error",
		    JOptionPane.ERROR_MESSAGE);
	    // ex.printStackTrace();
	}
    }

}
