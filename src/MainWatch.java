import static java.nio.file.LinkOption.*;
import static java.nio.file.StandardWatchEventKinds.*;
 
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
 
public class MainWatch {
 
	public static String finalPath = "";		//Stores the path to return to the UI
		
	public static void watchDirectoryPath(Path path) {
		
		
		// Sanity check - Check if path is a folder
		try {
			Boolean isFolder = (Boolean) Files.getAttribute(path,
					"basic:isDirectory", NOFOLLOW_LINKS);
			if (!isFolder) {
				throw new IllegalArgumentException("Path: " + path + " is not a folder");
			}
		} catch (IOException ioe) {
			// Folder does not exists
			ioe.printStackTrace();
		}
		
		System.out.println("Watching path: " + path);
		
		// We obtain the file system of the Path
		FileSystem fs = path.getFileSystem ();
		
		// We create the new WatchService using the new try() block
		
		
		try(WatchService service = fs.newWatchService()) {
			
			// We register the path to the service
			// We watch for creation events
			path.register(service, ENTRY_CREATE, ENTRY_DELETE);
			
			// Start the infinite polling loop
			
			boolean keepSearching = true;
			
			WatchKey key = null;
			while(keepSearching) {
				key = service.take();
				
				// Dequeueing events
				Kind<?> kind = null;
				for(WatchEvent<?> watchEvent : key.pollEvents()) {
					// Get the type of the event
					kind = watchEvent.kind();
					if (OVERFLOW == kind) {
						continue; //loop
					} else if (ENTRY_CREATE == kind) {
						// A new Path was created 
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						// Output
						//System.out.println("New path created: " + newPath);
						Main.currentFlashDrives.add(newPath.toString()); //Stores newPath to ArrayList for keeping tabs in Main class
						keepSearching = false;
					} else if (ENTRY_DELETE == kind) {
						// A Path was deleted 
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						// Output
						//System.out.println(newPath + " was deleted");
						for(int i = 0; i < Main.currentFlashDrives.size(); i++){
							if(Main.currentFlashDrives.get(i).equals(newPath.toString())) Main.currentFlashDrives.remove(i); //Removes from current flashdrive pool
						}
						keepSearching = false;
					}
				}
				
				if(!key.reset()) {
					break; //loop
				}
			}
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		
	}
 
	public static void main(String[] args, String targetDir) throws IOException,
			InterruptedException {
		// Folder we are going to watch
		Path folder = Paths.get(targetDir);		
		watchDirectoryPath(folder);
	}
}