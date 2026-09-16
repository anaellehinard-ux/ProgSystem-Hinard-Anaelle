public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
		
		byte b1 = (byte) (value & 0xFF);
		byte b2 = (byte) (value >> 8 & 0xFF);
		byte b3 = (byte) (value >> 16 & 0xFF);
		byte b4 = (byte) (value >> 24 & 0xFF);
		
		memory[offset] = b1;
		memory[offset + 1] = b2;
		memory[offset + 2] = b3;
		memory[offset + 3] = b4;
		
		return 4;
	}

    public static int readInt(byte[] memory, int offset) {
        // TODO: Reconstituer le int sur 4 octets.
        return 0;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        // TODO: Écrire les 2 octets de 'value'.
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        // TODO: Lire le short sur 2 octets.
        return 0;
    }
}