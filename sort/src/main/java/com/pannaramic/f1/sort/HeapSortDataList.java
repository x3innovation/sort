package com.pannaramic.f1.sort;

public class HeapSortDataList
{
    private static int n;
    private static int left;
    private static int right;
    private static int largest;

    // Build-Heap Function
    public static void buildheap( DataList	aDataList )
    {
    	n = aDataList.getNoOfItems() - 1;
    	for(int i=n/2;i>=0;i--)
    	{
    		maxheap( aDataList, i );
    	}
    }
    
    // Max-Heap Function
    public static void maxheap( DataList aDataList, int i )
    {
    	left=2*i;
    	right=2*i+1;
    	if(left <= n && aDataList.compare(left, i ) > 0 )
    	{
    		largest=left;
    	}
    	else
    	{
    		largest=i;
    	}
    	
    	if(right <= n && aDataList.compare( right, largest ) > 0 )
    	{
    		largest=right;
    	}
    	if(largest!=i)
    	{
    		aDataList.swap( i, largest );
    		maxheap( aDataList, largest);
    	}
    }
    
    // Sort Function
    public static void sort(DataList aDataList ){
    	buildheap( aDataList );
    	
    	for(int i=n;i>0;i--){
    		aDataList.swap( 0, i );
    		n=n-1;
    		maxheap( aDataList, 0 );
    	}
    }
    
    public static void main(String[] args) 
    {
/*
    	ArrayDataList list = new ArrayDataList();
    	for( int value = 2000; value >=0; value-- )
    	{
    		list.add( value );
    	}
		sort(list);
    	System.out.print(list);
  */
    	try
    	{
        	DataList list = null;
    		long time = System.currentTimeMillis();
    		String sourceFile = args[0]; // only applicable to INMEM
    		String destinationFile = args[1]; // BUFFER and UNSAFE should contain raw data already
    		// Access Strategy indicates how to 
    		// access the underlying data
    		// BUFFER - use memory mapped file with MappedByteBuffer to access the data and final sorted list will be in the same file
    		// UNSAFE - use memory mapped file but instead of using MappedByteBuffer use Usafe to access the data and final sorted list will be in the same file
    		// INMEM - use Java array to load the data from the source file and sort and save to the new file
    		// Please note for BUFFER and UNSAFE, new file should be used for each run to avoid OS caching affecting the performance data
    		String accessStrategy = args[2]; 
    		
    		if ( accessStrategy.equals( "BUFFER" ) )
        	{
        		list = new MemoryMappedDataList( 
        				destinationFile,
    					100,
    					0,
    					10 );
        	}
        	else if ( accessStrategy.equals( "UNSAFE" ) )
        	{
        		list = new UnsafeMemoryMappedDataList( 
        				destinationFile,
    					100,
    					0,
    					10 );   		
        	}
        	else if ( accessStrategy.equals( "INMEM" ) )
        	{
        		list = new InMemoryDataList( 
        				sourceFile,
    					destinationFile,
    					100,
    					0,
    					10 );    		
        	}
	    	time = System.currentTimeMillis() - time;
	    	System.out.println( "Load Time " + time );
	    	time = System.currentTimeMillis();
	    	sort( list );
	    	time = System.currentTimeMillis() - time;
	    	System.out.println( "Sort Time " + time );
	    	time = System.currentTimeMillis();
	    	list.dispose();
	    	time = System.currentTimeMillis() - time;
	    	System.out.println( "Dispose Time " + time );
    	}
    	catch( Throwable t )
    	{
    		t.printStackTrace();
    	} 
	}
} 
