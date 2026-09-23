public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
		
		byte b1 = (byte) ((value >> 24) & 0xFF);
		byte b2 = (byte) ((value >> 16) & 0xFF);
		byte b3 = (byte) ((value >> 8) & 0xFF);
		byte b4 = (byte) (value & 0xFF);
		
		memory[offset]     = b1;
		memory[offset + 1] = b2;
		memory[offset + 2] = b3;
		memory[offset + 3] = b4;
    
		return 4;
	}

    public static int readInt(byte[] memory, int offset) {
		
        int b1 = memory[offset] & 0xFF;
        int b2 = memory[offset + 1] & 0xFF;
        int b3 = memory[offset + 2] & 0xFF;
        int b4 = memory[offset + 3] & 0xFF;

        return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
    }
	
    public static int writeShort(byte[] memory, int offset, short value) {
		byte b1 = (byte) ((value >> 8) & 0xFF);
		byte b2 = (byte) (value & 0xFF);
		
        memory[offset] = b1;
        memory[offset + 1] = b2;
		
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        int b1 = memory[offset] & 0xFF;
        int b2 = memory[offset + 1] & 0xFF;

        return (short) ((b1 << 8) | b2);
    }
	
	
	
	
	public static int writeLong(byte[] memory, int offset, long value) {
		byte b1 = (byte) ((value >> 56) & 0xFF);
		byte b2 = (byte) ((value >> 48) & 0xFF);
		byte b3 = (byte) ((value >> 40) & 0xFF);
		byte b4 = (byte) ((value >> 32) & 0xFF);
		byte b5 = (byte) ((value >> 24) & 0xFF);
		byte b6 = (byte) ((value >> 16) & 0xFF);
		byte b7 = (byte) ((value >> 8) & 0xFF);
		byte b8 = (byte) (value & 0xFF);

		memory[offset] = b1;
		memory[offset + 1] = b2;
		memory[offset + 2] = b3;
		memory[offset + 3] = b4;
		memory[offset + 4] = b5;
		memory[offset + 5] = b6;
		memory[offset + 6] = b7;
		memory[offset + 7] = b8;

		return 8;
	}

	public static long readLong(byte[] memory, int offset) {
		
		long b1 = memory[offset] & 0xFFL;
		long b2 = memory[offset + 1] & 0xFFL;
		long b3 = memory[offset + 2] & 0xFFL;
		long b4 = memory[offset + 3] & 0xFFL;
		long b5 = memory[offset + 4] & 0xFFL;
		long b6 = memory[offset + 5] & 0xFFL;
		long b7 = memory[offset + 6] & 0xFFL;
		long b8 = memory[offset + 7] & 0xFFL;
		
		return (b1 << 56) | (b2 << 48) | (b3 << 40) | (b4 << 32) | (b5 << 24) | (b6 << 16) | (b7 << 8) | b8 ;
	}

	public static int writeString(byte[] memory, int offset,
			                      String str,int maxLength) {

		byte[] bytes = str.getBytes();

		
        int chaineLongueur; 
		chaineLongueur= bytes.length;
		
        int tailleAEcrire;
		tailleAEcrire = chaineLongueur;

        if (tailleAEcrire > maxLength) {
            tailleAEcrire = maxLength;
        }
		
		//si j n'a pas atteint tailleAEcrire alors on rajoute le texte
        int j = 0;
		while (j < tailleAEcrire) {
			memory[offset + j] = bytes[j];
			j++;
		}
		
		//si j n'a pas atteint maxLength alors on rajoute des 0
        while (j < maxLength) {
            memory[offset + j] = 0;
            j++;
        }

		return maxLength;
	}

	public static String readString(
			byte[] memory,
			int offset,
			int maxLength) {

		// TODO:
		// Lire jusqu'au premier octet nul
		// ou jusqu'à maxLength.

		return "";
	}

}