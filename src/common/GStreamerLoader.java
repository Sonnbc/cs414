package common;

public class GStreamerLoader
{
	public static void loadGStreamer() {
		if (com.sun.jna.Platform.isMac()) {
			final String jnaLibraryPath = System.getProperty("jna.library.path");
 
			final StringBuilder newJnaLibraryPath = new StringBuilder(
					jnaLibraryPath != null ? (jnaLibraryPath + ":") : "");
 
			newJnaLibraryPath.append(
					"/Library/Frameworks/GStreamer.framework/Versions/0.10/lib:");
 
			System.setProperty("jna.library.path", newJnaLibraryPath.toString());
		}
	}
}
