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

        // TODO:
        // Construire l'inode.
        // L'initialiser comme fichier vide.

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
}
