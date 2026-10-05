import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager =
                new MemoryManager();
    }

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        // On parcours les inodes de 0 à MAX_INODES - 1
        for (int i = 0; i < MemoryManager.MAX_INODES; i++) {
            Inode inode = new Inode(memoryManager, i);

            /**
             * Explication :
			 * 
             * L'inode est considéré comme libre si son type = 0
             */
            if (inode.getFileType() == 0) {
                return i; // Retourne le premier inode libre trouvé
            }
        }

        return -1; // -1 = Aucun inode libre disponible
    }

    public boolean createFile(
            String directory,
            String filename) {

        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

        // Construiction de l'inode
        Inode inode = new Inode(memoryManager, inodeNum);

        // Créer un fichier vide :
		
		// 0 = libre et 1 = non libre
        int fileType = 1;
		
		// 0 octet = fichier vide
        int fileSize = 0;    

		// Timestamp de création et ou de modification		
        long currentTime = System.currentTimeMillis();
		
		// les 10 pointeurs initialisés à 0
        int[] directPointers = new int[Inode.DIRECT_POINTERS];
		
		// Pas de bloc indirect
        int indirectPointer = 0;  

		// Permissions de lecture et d'écriture
        short permissions = 0644;  
		
		// 1 lien initial		
        int linkCount = 1;                             


        // Écriture physique des données dans le Virtual File System
        inode.writeToMemory(fileType, fileSize, currentTime, currentTime,
							directPointers, indirectPointer, permissions,
							linkCount);

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
}
