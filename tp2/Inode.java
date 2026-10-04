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
	
	public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int offset = getInodeOffset();

		// 1. Numéro d'inode
		Utils.writeInt(memory, offset, this.inodeNumber);
		offset = offset + 4;
		
		// 2. Type
		Utils.writeInt(memory, offset, fileType);
		offset = offset + 4;
		
		// 3. Taille
		Utils.writeInt(memory, offset, fileSize);
		offset = offset + 4;
		
		// 4. Création
		Utils.writeLong(memory, offset, creationTime);
		offset = offset + 8;
		
		// 5. Modification
		Utils.writeLong(memory, offset, modificationTime);
		offset = offset + 8;
		
		// 6. 10 pointeurs directs
		for (int i = 0; i < DIRECT_POINTERS; i++) {
			/**
			 * Explication :
			 * 
			 * On initialise le pointeur à 0 (bloc non alloué).
			 * Si le tableau n'est pas nul ET que l'indice i
			 * ne dépasse pas la taille du tableau,
			 * alors on récupère la vraie valeur du pointeur.
			 */
			int pointeur = 0;

			if (directPointers != null) {
				if (i < directPointers.length) {
					pointeur = directPointers[i];
				}
			}
			
			Utils.writeInt(memory, offset, pointeur);
			offset = offset + 4;
		}
		
		// 7. Pointeur indirect
		Utils.writeInt(memory, offset, indirectPointer);
		offset = offset + 4;
		
		// 8. Permissions
		Utils.writeShort(memory, offset, permissions);
		offset = offset + 2;
		
		// 9. Nombre de liens
		Utils.writeInt(memory, offset, linkCount);
		offset = offset + 4;
	}
}