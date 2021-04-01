package bg.geist.util;


public class Flags {
    public static int set(int flags, int bitmask) {
        return (flags | bitmask);
    }
    public static int clear(int flags, int bitmask) {
        return (flags & ~bitmask);
    }
    public static int toggle(int flags, int bitmask) {
        return (flags ^ bitmask);
    }
    public static boolean isTrue(int flags, int bitmask) {
        return ((flags & bitmask) > 0);
    }
    public static boolean isFalse(int flags, int bitmask) {
        return ((flags & bitmask) == 0);
    }
    /**
     * Check if flags count < 2 (IsPowerOfTwo)
     *
     * @param bits flags
     * @return TRUE for single flag or no flags
     */
    public static boolean isSingle(int bits) {
        return ((bits & (bits - 1)) == 0);
    }
    /**
     * Index to bitmask: 0, 1, 2, 3 ... 30  =>  1, 2, 4, 8, ... 1073741824
     *
     * @param index (max: ~30)
     * @return bitmask
     */
    public static int toBits(int index) {
        return (1 << index);
    }
    /**
     * Set flags by length
     *
     * @param length Number of flags
     * @return flags
     */
    public static int toFlags(int length) {
        int flags = 0;
        for (int i = 0; i < length; i++) {
            flags = (flags | (1 << i));
        }
        return flags;
    }
    /**
     * Array of booleans to flags: [true, false, true] => 5
     *
     * @param arr Array of booleans
     * @return flags
     */
    public static int toFlags(Boolean[] arr) {
        if (arr == null) {
            return 0;
        }
        int bits = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]) {
                bits += (1 << i);
            }
        }
        return bits;
    }
    /**
     * Bits to index
     *
     * base 2: Math.log2(bits)
     * @return index (0 for bits=0)
     */
    public static int toIndex(int bits) {
        int log = 0;
        if((bits & 0xffff0000) != 0) {bits >>>= 16; log = 16;}
        if(bits >= 256) {bits >>>= 8; log += 8;}
        if(bits >= 16 ) {bits >>>= 4; log += 4;}
        if(bits >= 4  ) {bits >>>= 2; log += 2;}
        return (log + (bits >>> 1));
    }
}