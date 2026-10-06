import java.io.*;

public class MemoryManager {

    public static final int BLOCK_SIZE = 512;
    public static final int TOTAL_MEMORY = 1024 * 1024;
    public static final int NUM_BLOCKS =
            TOTAL_MEMORY / BLOCK_SIZE;

    public static final int SUPERBLOCK_OFFSET = 0;
    public static final int BITMAP_OFFSET = BLOCK_SIZE;
    public static final int INODE_TABLE_OFFSET =
            2 * BLOCK_SIZE;
    public static final int DATA_OFFSET =
            129 * BLOCK_SIZE;

    public static final int INODE_SIZE = 128;

    public static final int INODE_TABLE_SIZE =
            DATA_OFFSET - INODE_TABLE_OFFSET;

    public static final int MAX_INODES =
            INODE_TABLE_SIZE / INODE_SIZE;

    private byte[] memory;

    public MemoryManager() {
        this.memory = new byte[TOTAL_MEMORY];
        initializeFilesystem();
    }

    private void initializeFilesystem() {
        writeSuperblock();

        for (int i=0 ; i < 16; i++){
			memory[BLOCK_SIZE + i] = (byte) 0xFF;
		}
    }

    private void writeSuperblock() {
        Utils.writeString(
                memory,
                SUPERBLOCK_OFFSET,
                "MYFS1.0",
                16);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 16,
                BLOCK_SIZE);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 20,
                TOTAL_MEMORY);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 24,
                NUM_BLOCKS);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 28,
                MAX_INODES);
    }

    public byte[] getFilesystemMemory() {
        return memory;
    }
	
	//-- étape 5 --
	public boolean setBlockUsed(int blockNumber, boolean used) {
		if (blockNumber < 0 ||
			blockNumber >= NUM_BLOCKS) {
			return false;
		}

		int byteIndex = blockNumber / 8;
		int bitPosition = blockNumber % 8;
		int offset = BITMAP_OFFSET + byteIndex;

		byte masque = (byte) (1 << bitPosition);

		if (used) {
			/**
			 * explication :
			 * Si le bit en memory[offset] est a 1 il reste a 1
			 * Ou alors si il est a 0 comme le masque est a 1
			 * il prend la valeur du masque.
			 */
			memory[offset] = (byte) (memory[offset] | masque);
		} else {
			/**
			 * explication :
			 * ~masque = inverse tous les bits, donc le bit a 1
			 * que l'on veut passe a 0
			 * 
			 * Si le bit est a 1 ça devient 0
			 * Sinon le bit est a 0 ça reste a 0
			 * Tous les autres bits sont comparé avec 1 donc ils
			 * conservent leur valeur d'origine
			 */
			memory[offset] = (byte) (memory[offset] & ~masque);
		}

		return true;
	}

	public int isBlockUsed(int blockNumber) {

		if (blockNumber < 0 ||
			blockNumber >= NUM_BLOCKS) {
			return -1;
		}

		int byteIndex = blockNumber / 8;
		int bitPosition = blockNumber % 8;
		int offset = BITMAP_OFFSET + byteIndex;
		
		
		/**
		 * explication :
		 * 
		 * le décalage vers la droite qui amène le bit qui nous
		 * intéresse vers la droite. Puis on va le comparer a 1.
		 *
		 * exemple :
		 *
		 * si on a l'octet = 10011000 et bitPosition = 3
		 * après décalage on obtiens 00010011
		 * Ensuite on le compare avec un 1;
		 * Si quand on le compare a 1 on obtiens 1
		 * Alors c'est occupé
		 * Sinon si on obtiens 0 c'est libre.
		 */
		
		return ((memory[offset] & 0xFF) >> bitPosition) & 1;
	}

	public int allocateBlock() {

		// for de 129 a NUM_Block-1
		// isBlockUsed de i
		// si i n'est pas utilisé -> setBlockUsed
		// si mémoire pleine --> retourne -1
		
		for (int i = 129; i < NUM_BLOCKS; i++){
			if (isBlockUsed(i) == 0) {
				setBlockUsed(i, true);
				return i;
			}
		}
		return -1;
	}
}