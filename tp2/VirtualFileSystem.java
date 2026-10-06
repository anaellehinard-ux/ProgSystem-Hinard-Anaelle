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
	
	public boolean writeFile(
        int inodeNum,
        byte[] data) {

		int blocksNeeded =
				(data.length
				+ MemoryManager.BLOCK_SIZE - 1)
				/ MemoryManager.BLOCK_SIZE;

		if (blocksNeeded > Inode.DIRECT_POINTERS) {
			return false;
		}

		int[] blockPointers =
				new int[Inode.DIRECT_POINTERS];

		//Allouer les blocs nécessaires avec le bitmap de MemoryManager
        for (int i = 0; i < blocksNeeded; i++) {
            int blockNum;
			blockNum = memoryManager.allocateBlock();
            
            // Si la mémoire est pleine = allocation échouée
            if (blockNum == -1) {
                return false;
            }
            
            blockPointers[i] = blockNum;
        }

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int bytesRemaining =
				data.length;

		int dataSrcOffset = 0;
		
        for (int i = 0; i < blocksNeeded; i++) {
            // Calculer le nombre à copier pour ce bloc (max -> BLOCK_SIZE)
            int bytesToCopy;
			bytesToCopy = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
            
            // Récupérer le numéro du bloc
            int blockNum;
			blockNum = blockPointers[i];
            
            // Calculer l'offset dans la mémoire
            int blockOffset;
            blockOffset = blockNum * MemoryManager.BLOCK_SIZE;
            
            // Copie des données du tableau vers la mémoire
            System.arraycopy(data, dataSrcOffset, memory, blockOffset, bytesToCopy);
            
            // Mise à jour des pointeurs de lecture/écriture
            dataSrcOffset = dataSrcOffset + bytesToCopy;
            bytesRemaining = bytesRemaining - bytesToCopy;
        }

        //Mise à jour de l'inode avec les nouveaux paramètres
        Inode inode = new Inode(memoryManager, inodeNum);
        
        long currentTime ;
        currentTime = System.currentTimeMillis();
        
        inode.writeToMemory(
                1,           
				// Nouvelle taille (en octets)
                data.length,  
				
				// Date de création
                currentTime,  
				
				// Date de modification
                currentTime, 
				
				// Nouveaux pointeurs
                blockPointers, 
				
				// Pas de pointeur indirect
                0, 
				
				// Permissions
                (short) 0644,      
				
				// Nombre de liens               
			    1                        
        );
		return true;
	}
	
	public byte[] readFile(int inodeNum) {

		Inode inode =
				new Inode(memoryManager, inodeNum);

		int fileSize =
				inode.getFileSize();

		if (fileSize == 0) {
			return new byte[0];
		}

		byte[] fileData =
				new byte[fileSize];

		byte[] memory =
				memoryManager.getFilesystemMemory();

		int[] blockPointers =
				inode.getDirectPointers();

		
		int blocksNeeded ;
		blocksNeeded = (fileSize + MemoryManager.BLOCK_SIZE - 1) / MemoryManager.BLOCK_SIZE;

		int bytesRemaining ;
		bytesRemaining = fileSize;
		
		int dataDestOffset;
		dataDestOffset = 0;

		for (int i = 0; i < blocksNeeded; i++) {
			// Taille du fragment à lire pour le bloc
			int bytesToCopy;
			bytesToCopy = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);

			// Numéro du bloc et son adresse en mémoire
			int blockNum;
			blockNum = blockPointers[i];
			
			int blockOffset;
			blockOffset = blockNum * MemoryManager.BLOCK_SIZE;

			// Copie des données du bloc vers le tableau fileData
			System.arraycopy(memory, blockOffset, fileData, dataDestOffset, bytesToCopy);

			// Décalage des curseurs
			dataDestOffset = dataDestOffset + bytesToCopy;
			bytesRemaining = bytesRemaining - bytesToCopy;
		}

		return fileData;
	}
}
