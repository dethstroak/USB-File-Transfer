import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    
    public static ArrayList<String> currentFlashDrives = new ArrayList();
    
    public static void main(String[] args) throws IOException, InterruptedException{
        String targetDir = "D:/TestDir/";
        String currentDir = targetDir;
        Scanner in = new Scanner(System.in);
        
        for(int i = 0; i < (dirList(targetDir)).length; i++){
            currentFlashDrives.add((dirList(targetDir))[i].toString());
        }
        while(true){
            System.out.println("You are currently in " + currentDir);
            System.out.println("\n\nWould you like to: \n1:View file list in current dir \n2:Scan for a new flash-drive \n3:Select a flash-drive to explore into \n4:Select what to copy and where to");
            switch(in.nextInt()){
                case 1:		//Prints out current directory
                	System.out.println("");
                    printDirList(currentDir);
                    break;
                case 2:		//Scan for a new flash-drive input
                	System.out.println("");
                    MainWatch.main(args, targetDir);
                    break;
                case 3:		//Select flash-drive to explore and look into
                	System.out.println("");
                    printDirList(currentDir);
                    System.out.println("Which flash-drive would you like to explore further into?");
                    String flashDir = in.next();
                    currentDir += flashDir + "/";
                    printDirList(currentDir);
                    break;
                case 4:
                	currentDir = targetDir;
                	System.out.println("");
                    printDirList(currentDir);
                    System.out.println("Which flash-drive would you like to set as the flash-drive to copy from?");
                    String toDir = in.next();
                    currentDir += toDir; //Appending target flash-drive onto currentDir
                    printDirList(currentDir);
                    System.out.println("Which file would you like to copy? (Include extention)");
                    String fileDir = in.next();
                    toDir=targetDir + toDir + "/" + fileDir;	//Setting toDir equal to the flash-drive to copy from
                    currentDir = targetDir; printDirList(currentDir); 
                    System.out.println("Which flash-drive would you like to copy to?");
                    String finalDir = in.next();
                    finalDir = currentDir + finalDir + "/" + fileDir; //Setting finalDir equal to the flash-drive to copy to
                    System.out.println(toDir + "\t" + finalDir);
                    System.out.println(copyFile(toDir, finalDir)); //Copies file from toDir to finalDir and returns true if successful and false otherwise
                    break;
        }
      }
    }
    public static File[] dirList(String targetDir){ //Lists all the files and sub-directories in the directory
        File file = new File(targetDir);
        File[] files = file.listFiles();
        return files;
    }
    
    public static void printDirList(String targetDir){	//Prints the output from dirList
        for(int i =0; i < (dirList(targetDir)).length; i++){
            System.out.println(dirList(targetDir)[i]);
        }
    }
    
    public static boolean verifyDir(String dir){        //Verifies that the directory of the file target exists
        File file = new File(dir);
        if(file.exists()) return true;
        else return false;
    }
    
    public static boolean copyFile(String initialLocation, String finalLocation){   //Copies file from initialLocation to finalLocation
        File fileInitial = new File(initialLocation);
        File fileFinal = new File(finalLocation);
        if(verifyDir(initialLocation) == true){		//Copies files if the directory is verified to exist
            try{
                Files.copy(fileInitial.toPath(), fileFinal.toPath());
                return true;						//Returns true if files are successfully copied to the directory
            }catch(IOException e){
                e.printStackTrace();
                return false;						//Returns false if files are not successfully transfered to directory
            }   
        } else{
            return false;							//directory was not able to be verified
        }
        
        
    }

}