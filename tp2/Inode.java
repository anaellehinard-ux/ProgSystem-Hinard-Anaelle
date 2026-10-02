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
		/** 
		 * On décale le début de la table des inodes avec 
		 * MemoryManager.INODE_TABLE_OFFSET.
		 * On saute les x inodes d'avant de 128 octets
		 */
        return MemoryManager.INODE_TABLE_OFFSET + (inodeNumber * INODE_SIZE);
    }

    public int getFileType() {
        byte[] memory = memoryManager.getFilesystemMemory();
		// Décalage de 4 octets par rapport au début de l'inode
        int offset = getInodeOffset() + 4;

        // Lecture grace a la classe Utils
        return Utils.readInt(memory, offset);
    }

    public int getFileSize() {
        byte[] memory = memoryManager.getFilesystemMemory();
		 // Décalage de 8 octets par rapport au début de l'inode
        int offset = getInodeOffset() + 8;

        // Lecture avec la classe Utils
        return Utils.readInt(memory, offset);


    public int[] getDirectPointers() {

        byte[] memory = memoryManager.getFilesystemMemory();
        int[] pointers = new int[DIRECT_POINTERS];
		
		// L'adresse du départ des pointeurs directs = offset de l'inode + 28 octets
        int departOffset = getInodeOffset() + 28;
		
        for (int i = 0; i < DIRECT_POINTERS; i++) {
            /**
             * Explication :
			 *
             * tous les pointeur sont des entiers sur 4 octets.
             * Lire le i pointeur --> calcule de  l'adresse relative
             * departOffset + (i * 4 octets)
             */
            int pointerOffset = departOffset + (i * 4);
            pointers[i] = Utils.readInt(memory, pointerOffset);
        }
        return pointers;
    }
}