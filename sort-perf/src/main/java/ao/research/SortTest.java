package ao.research;


import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

public class SortTest {
    private static final Logger logger = LoggerFactory.getLogger(SortTest.class);

    private static final int RECORD_SIZE = 100;
    private static final int RECORD_SIZE_INT = RECORD_SIZE / Integer.BYTES;

    static {
        //noinspection ConstantConditions
        if (RECORD_SIZE % RECORD_SIZE_INT != 0) {
            throw new Error();
        }
    }

    public static void main(String[] args) throws IOException {
//        logger.info("foo");

        Path from = Paths.get(
                "1gb-b.bin");
//                "8gb.bin");
        Path to = Paths.get("out-b.bin");

        Files.deleteIfExists(to);

        logger.info("sorting from {} to {}", from, to);
        Stopwatch timer = Stopwatch.createStarted();

        long size = Files.size(from);
        checkArgument(size % RECORD_SIZE == 0);

        long count = size / RECORD_SIZE;

        logger.info("size is {}, count is {}", size, count);

        int smallCount = Ints.checkedCast(count);
        byte[][] chunks = new byte[smallCount][RECORD_SIZE];

        logger.info("done allocating memory");

        try (FileChannel input = FileChannel.open(from)) {
            MappedByteBuffer inputMap = input.map(FileChannel.MapMode.READ_ONLY, 0, size);

            for (long i = 0; i < count; i++) {
                int smallIndex = Ints.checkedCast(i);

                inputMap.get(chunks[smallIndex]);
//                int read = input.read(chunks[smallIndex]);
//                if (read != 100) {
//                    throw new Error();
//                }

                if (i % 25_000_000 == 0) {
                    logger.info("read {}", i);
                }
            }
        }

        logger.info("finished reading");

        Arrays.parallelSort(chunks, SortTest::compare);
//        Arrays.sort(chunks, SortTest::compare);

        logger.info("done sorting");

        try (
//        try (FileChannel output = new RandomAccessFile(to.toFile(), "w").getChannel()) {
                FileChannel output = FileChannel.open(
                        to,
                        StandardOpenOption.READ,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE_NEW)) {

//                OutputStream out = new BufferedOutputStream(Files.newOutputStream(to))) {
            MappedByteBuffer outputMap = output.map(FileChannel.MapMode.READ_WRITE, 0, size);

            for (int i = 0, chunksLength = chunks.length; i < chunksLength; i++) {
                outputMap.put(chunks[i]);
//                out.write(chunks[i]);

                if (i % 25_000_000 == 0) {
                    logger.info("wrote {}", i);
                }
            }
        }

        long elapsedMillis = timer.elapsed(TimeUnit.MILLISECONDS);
        logger.info("finished writing, total time: {}, at {} per second", timer, 1000L * count / elapsedMillis );
    }


    static int compare(byte[] a, byte[] b) {
//        return compareConditionalLoop(a, b);
        return compareConditionalUnrolled(a, b);
    }

    static int compareConditionalLoop(byte[] a, byte[] b) {
        for (int i = 0; i < RECORD_SIZE; i++) {
            if (a[i] != b[i]) {
                return Byte.toUnsignedInt(a[i]) - Byte.toUnsignedInt(b[i]);
            }
        }
        return 0;
    }

    static int compareConditionalUnrolled(byte[] a, byte[] b) {
        if (a[0] != b[0]) { return Byte.toUnsignedInt(a[0]) - Byte.toUnsignedInt(b[0]); }
        if (a[1] != b[1]) { return Byte.toUnsignedInt(a[1]) - Byte.toUnsignedInt(b[1]); }
        if (a[2] != b[2]) { return Byte.toUnsignedInt(a[2]) - Byte.toUnsignedInt(b[2]); }
        if (a[3] != b[3]) { return Byte.toUnsignedInt(a[3]) - Byte.toUnsignedInt(b[3]); }
        if (a[4] != b[4]) { return Byte.toUnsignedInt(a[4]) - Byte.toUnsignedInt(b[4]); }
        if (a[5] != b[5]) { return Byte.toUnsignedInt(a[5]) - Byte.toUnsignedInt(b[5]); }
        if (a[6] != b[6]) { return Byte.toUnsignedInt(a[6]) - Byte.toUnsignedInt(b[6]); }
        if (a[7] != b[7]) { return Byte.toUnsignedInt(a[7]) - Byte.toUnsignedInt(b[7]); }
        if (a[8] != b[8]) { return Byte.toUnsignedInt(a[8]) - Byte.toUnsignedInt(b[8]); }
        if (a[9] != b[9]) { return Byte.toUnsignedInt(a[9]) - Byte.toUnsignedInt(b[9]); }
        if (a[10] != b[10]) { return Byte.toUnsignedInt(a[10]) - Byte.toUnsignedInt(b[10]); }
        if (a[11] != b[11]) { return Byte.toUnsignedInt(a[11]) - Byte.toUnsignedInt(b[11]); }
        if (a[12] != b[12]) { return Byte.toUnsignedInt(a[12]) - Byte.toUnsignedInt(b[12]); }
        if (a[13] != b[13]) { return Byte.toUnsignedInt(a[13]) - Byte.toUnsignedInt(b[13]); }
        if (a[14] != b[14]) { return Byte.toUnsignedInt(a[14]) - Byte.toUnsignedInt(b[14]); }
        if (a[15] != b[15]) { return Byte.toUnsignedInt(a[15]) - Byte.toUnsignedInt(b[15]); }
        if (a[16] != b[16]) { return Byte.toUnsignedInt(a[16]) - Byte.toUnsignedInt(b[16]); }
        if (a[17] != b[17]) { return Byte.toUnsignedInt(a[17]) - Byte.toUnsignedInt(b[17]); }
        if (a[18] != b[18]) { return Byte.toUnsignedInt(a[18]) - Byte.toUnsignedInt(b[18]); }
        if (a[19] != b[19]) { return Byte.toUnsignedInt(a[19]) - Byte.toUnsignedInt(b[19]); }
        if (a[20] != b[20]) { return Byte.toUnsignedInt(a[20]) - Byte.toUnsignedInt(b[20]); }
        if (a[21] != b[21]) { return Byte.toUnsignedInt(a[21]) - Byte.toUnsignedInt(b[21]); }
        if (a[22] != b[22]) { return Byte.toUnsignedInt(a[22]) - Byte.toUnsignedInt(b[22]); }
        if (a[23] != b[23]) { return Byte.toUnsignedInt(a[23]) - Byte.toUnsignedInt(b[23]); }
        if (a[24] != b[24]) { return Byte.toUnsignedInt(a[24]) - Byte.toUnsignedInt(b[24]); }
        if (a[25] != b[25]) { return Byte.toUnsignedInt(a[25]) - Byte.toUnsignedInt(b[25]); }
        if (a[26] != b[26]) { return Byte.toUnsignedInt(a[26]) - Byte.toUnsignedInt(b[26]); }
        if (a[27] != b[27]) { return Byte.toUnsignedInt(a[27]) - Byte.toUnsignedInt(b[27]); }
        if (a[28] != b[28]) { return Byte.toUnsignedInt(a[28]) - Byte.toUnsignedInt(b[28]); }
        if (a[29] != b[29]) { return Byte.toUnsignedInt(a[29]) - Byte.toUnsignedInt(b[29]); }
        if (a[30] != b[30]) { return Byte.toUnsignedInt(a[30]) - Byte.toUnsignedInt(b[30]); }
        if (a[31] != b[31]) { return Byte.toUnsignedInt(a[31]) - Byte.toUnsignedInt(b[31]); }
        if (a[32] != b[32]) { return Byte.toUnsignedInt(a[32]) - Byte.toUnsignedInt(b[32]); }
        if (a[33] != b[33]) { return Byte.toUnsignedInt(a[33]) - Byte.toUnsignedInt(b[33]); }
        if (a[34] != b[34]) { return Byte.toUnsignedInt(a[34]) - Byte.toUnsignedInt(b[34]); }
        if (a[35] != b[35]) { return Byte.toUnsignedInt(a[35]) - Byte.toUnsignedInt(b[35]); }
        if (a[36] != b[36]) { return Byte.toUnsignedInt(a[36]) - Byte.toUnsignedInt(b[36]); }
        if (a[37] != b[37]) { return Byte.toUnsignedInt(a[37]) - Byte.toUnsignedInt(b[37]); }
        if (a[38] != b[38]) { return Byte.toUnsignedInt(a[38]) - Byte.toUnsignedInt(b[38]); }
        if (a[39] != b[39]) { return Byte.toUnsignedInt(a[39]) - Byte.toUnsignedInt(b[39]); }
        if (a[40] != b[40]) { return Byte.toUnsignedInt(a[40]) - Byte.toUnsignedInt(b[40]); }
        if (a[41] != b[41]) { return Byte.toUnsignedInt(a[41]) - Byte.toUnsignedInt(b[41]); }
        if (a[42] != b[42]) { return Byte.toUnsignedInt(a[42]) - Byte.toUnsignedInt(b[42]); }
        if (a[43] != b[43]) { return Byte.toUnsignedInt(a[43]) - Byte.toUnsignedInt(b[43]); }
        if (a[44] != b[44]) { return Byte.toUnsignedInt(a[44]) - Byte.toUnsignedInt(b[44]); }
        if (a[45] != b[45]) { return Byte.toUnsignedInt(a[45]) - Byte.toUnsignedInt(b[45]); }
        if (a[46] != b[46]) { return Byte.toUnsignedInt(a[46]) - Byte.toUnsignedInt(b[46]); }
        if (a[47] != b[47]) { return Byte.toUnsignedInt(a[47]) - Byte.toUnsignedInt(b[47]); }
        if (a[48] != b[48]) { return Byte.toUnsignedInt(a[48]) - Byte.toUnsignedInt(b[48]); }
        if (a[49] != b[49]) { return Byte.toUnsignedInt(a[49]) - Byte.toUnsignedInt(b[49]); }
        if (a[50] != b[50]) { return Byte.toUnsignedInt(a[50]) - Byte.toUnsignedInt(b[50]); }
        if (a[51] != b[51]) { return Byte.toUnsignedInt(a[51]) - Byte.toUnsignedInt(b[51]); }
        if (a[52] != b[52]) { return Byte.toUnsignedInt(a[52]) - Byte.toUnsignedInt(b[52]); }
        if (a[53] != b[53]) { return Byte.toUnsignedInt(a[53]) - Byte.toUnsignedInt(b[53]); }
        if (a[54] != b[54]) { return Byte.toUnsignedInt(a[54]) - Byte.toUnsignedInt(b[54]); }
        if (a[55] != b[55]) { return Byte.toUnsignedInt(a[55]) - Byte.toUnsignedInt(b[55]); }
        if (a[56] != b[56]) { return Byte.toUnsignedInt(a[56]) - Byte.toUnsignedInt(b[56]); }
        if (a[57] != b[57]) { return Byte.toUnsignedInt(a[57]) - Byte.toUnsignedInt(b[57]); }
        if (a[58] != b[58]) { return Byte.toUnsignedInt(a[58]) - Byte.toUnsignedInt(b[58]); }
        if (a[59] != b[59]) { return Byte.toUnsignedInt(a[59]) - Byte.toUnsignedInt(b[59]); }
        if (a[60] != b[60]) { return Byte.toUnsignedInt(a[60]) - Byte.toUnsignedInt(b[60]); }
        if (a[61] != b[61]) { return Byte.toUnsignedInt(a[61]) - Byte.toUnsignedInt(b[61]); }
        if (a[62] != b[62]) { return Byte.toUnsignedInt(a[62]) - Byte.toUnsignedInt(b[62]); }
        if (a[63] != b[63]) { return Byte.toUnsignedInt(a[63]) - Byte.toUnsignedInt(b[63]); }
        if (a[64] != b[64]) { return Byte.toUnsignedInt(a[64]) - Byte.toUnsignedInt(b[64]); }
        if (a[65] != b[65]) { return Byte.toUnsignedInt(a[65]) - Byte.toUnsignedInt(b[65]); }
        if (a[66] != b[66]) { return Byte.toUnsignedInt(a[66]) - Byte.toUnsignedInt(b[66]); }
        if (a[67] != b[67]) { return Byte.toUnsignedInt(a[67]) - Byte.toUnsignedInt(b[67]); }
        if (a[68] != b[68]) { return Byte.toUnsignedInt(a[68]) - Byte.toUnsignedInt(b[68]); }
        if (a[69] != b[69]) { return Byte.toUnsignedInt(a[69]) - Byte.toUnsignedInt(b[69]); }
        if (a[70] != b[70]) { return Byte.toUnsignedInt(a[70]) - Byte.toUnsignedInt(b[70]); }
        if (a[71] != b[71]) { return Byte.toUnsignedInt(a[71]) - Byte.toUnsignedInt(b[71]); }
        if (a[72] != b[72]) { return Byte.toUnsignedInt(a[72]) - Byte.toUnsignedInt(b[72]); }
        if (a[73] != b[73]) { return Byte.toUnsignedInt(a[73]) - Byte.toUnsignedInt(b[73]); }
        if (a[74] != b[74]) { return Byte.toUnsignedInt(a[74]) - Byte.toUnsignedInt(b[74]); }
        if (a[75] != b[75]) { return Byte.toUnsignedInt(a[75]) - Byte.toUnsignedInt(b[75]); }
        if (a[76] != b[76]) { return Byte.toUnsignedInt(a[76]) - Byte.toUnsignedInt(b[76]); }
        if (a[77] != b[77]) { return Byte.toUnsignedInt(a[77]) - Byte.toUnsignedInt(b[77]); }
        if (a[78] != b[78]) { return Byte.toUnsignedInt(a[78]) - Byte.toUnsignedInt(b[78]); }
        if (a[79] != b[79]) { return Byte.toUnsignedInt(a[79]) - Byte.toUnsignedInt(b[79]); }
        if (a[80] != b[80]) { return Byte.toUnsignedInt(a[80]) - Byte.toUnsignedInt(b[80]); }
        if (a[81] != b[81]) { return Byte.toUnsignedInt(a[81]) - Byte.toUnsignedInt(b[81]); }
        if (a[82] != b[82]) { return Byte.toUnsignedInt(a[82]) - Byte.toUnsignedInt(b[82]); }
        if (a[83] != b[83]) { return Byte.toUnsignedInt(a[83]) - Byte.toUnsignedInt(b[83]); }
        if (a[84] != b[84]) { return Byte.toUnsignedInt(a[84]) - Byte.toUnsignedInt(b[84]); }
        if (a[85] != b[85]) { return Byte.toUnsignedInt(a[85]) - Byte.toUnsignedInt(b[85]); }
        if (a[86] != b[86]) { return Byte.toUnsignedInt(a[86]) - Byte.toUnsignedInt(b[86]); }
        if (a[87] != b[87]) { return Byte.toUnsignedInt(a[87]) - Byte.toUnsignedInt(b[87]); }
        if (a[88] != b[88]) { return Byte.toUnsignedInt(a[88]) - Byte.toUnsignedInt(b[88]); }
        if (a[89] != b[89]) { return Byte.toUnsignedInt(a[89]) - Byte.toUnsignedInt(b[89]); }
        if (a[90] != b[90]) { return Byte.toUnsignedInt(a[90]) - Byte.toUnsignedInt(b[90]); }
        if (a[91] != b[91]) { return Byte.toUnsignedInt(a[91]) - Byte.toUnsignedInt(b[91]); }
        if (a[92] != b[92]) { return Byte.toUnsignedInt(a[92]) - Byte.toUnsignedInt(b[92]); }
        if (a[93] != b[93]) { return Byte.toUnsignedInt(a[93]) - Byte.toUnsignedInt(b[93]); }
        if (a[94] != b[94]) { return Byte.toUnsignedInt(a[94]) - Byte.toUnsignedInt(b[94]); }
        if (a[95] != b[95]) { return Byte.toUnsignedInt(a[95]) - Byte.toUnsignedInt(b[95]); }
        if (a[96] != b[96]) { return Byte.toUnsignedInt(a[96]) - Byte.toUnsignedInt(b[96]); }
        if (a[97] != b[97]) { return Byte.toUnsignedInt(a[97]) - Byte.toUnsignedInt(b[97]); }
        if (a[98] != b[98]) { return Byte.toUnsignedInt(a[98]) - Byte.toUnsignedInt(b[98]); }
        if (a[99] != b[99]) { return Byte.toUnsignedInt(a[99]) - Byte.toUnsignedInt(b[99]); }
        return 0;
    }

    static int compareUnconditionalLoop(byte[] a, byte[] b) {
        int cmpTotal = 0;
        for (int i = 0; i < RECORD_SIZE; i++) {
            int cmp = compareUnsigned(a[i], b[i]);
            cmpTotal = Integer.signum(2 * cmpTotal + cmp);
        }
        return cmpTotal;
    }

    static int compareUnsigned(byte a, byte b) {
        int unsignedA = Byte.toUnsignedInt(a);
        int unsignedB = Byte.toUnsignedInt(b);

        int difference = unsignedA - unsignedB;

        return Integer.signum(difference);
    }
}
