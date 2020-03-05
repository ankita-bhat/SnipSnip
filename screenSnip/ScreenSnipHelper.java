package screenSnip;

import java.time.LocalDateTime;
import javax.swing.ImageIcon;

public class ScreenSnipHelper {
    
    /**
     * SETTINGS file path from XML
     */
    protected static void getPathSettingsFile() {
	
	
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ScreenSnipHome.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Returns system datime in YYYY-MM-DD-hh-mm-ss format
     * 
     * @return
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
	String imgName = "ImgNo-" + getCurrentStrDatime() ;
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
	
	if(withDot) {
	    imgType = ".png";
	} else {
	    imgType = "png";
	}
	
	return imgType;
    }
    
    protected String getFileLocation() {
	String imgPath = "D:\\eclipse-ws\\MyScreenSnipper\\output\\";
	
	return imgPath;
    }
    
    protected String getFullFilePath() {
	
//	initHelper();
	
	
	return getFileLocation() + getImageName() + getImageType(true);
    }

}
