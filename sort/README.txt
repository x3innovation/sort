Test various access model on sorting. Heapsort is used for its efficient O(1) memory space requirement

RunConfig/HeapSortDataList.launch is provided to run the test program under Eclipse.

Test program takes 3 parameters - source file, destination file, and access strategy

Current available access strategies are:

BUFFER - use memory mapped file with MappedByteBuffer to access the data and final sorted list will be in the same file
UNSAFE - use memory mapped file but instead of using MappedByteBuffer use Usafe to access the data and final sorted list will be in the same file
INMEM - use Java array to load the data from the source file and sort and save to the new file

Please note for BUFFER and UNSAFE, new file should be used for each run to avoid OS caching affecting the performance data

Test data is located in src/test/resources/data. sample30.bin contains 30 100bytes data records

When running BUFFER and UNSAFE, need to copy the source data file to src/test/resources/data/sort.data first

After finished running, program will display the following metrics:

Load Time - Time to load the data into the memory for sorting in milliseconds
Sort Time - Time to perform heap sort in milliseconds
Dispose Time - Time to save the data into the destination file in milliseconds. For BUFFER and UNSAFE this will represent the time to flush the data
   