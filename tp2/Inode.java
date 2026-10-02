public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
		// On décale le début de la table des inodes avec 
		// MemoryManager.INODE_TABLE_OFFSET.
		// On saute les x inodes d'avant de 128 octets
        return MemoryManager.INODE_TABLE_OFFSET + (inodeNumber * INODE_SIZE);
    }

    public int getFileType() {
        // TODO:
        // Lire le type à offset + 4.
        return 0;
    }

    public int getFileSize() {
        // TODO:
        // Lire la taille à offset + 8.
        return 0;
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];

        // TODO:
        // Lire les 10 pointeurs directs.

        return pointers;
    }
}