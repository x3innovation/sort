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
        	if ( args[2].equals( "BUFFER" ) )
        	{
        		list = new MemoryMappedDataList( 
    					args[1],
    					100,
    					0,
    					10 );
        	}
        	else if ( args[2].equals( "UNSAFE" ) )
        	{
        		list = new UnsafeMemoryMappedDataList( 
    					args[1],
    					100,
    					0,
    					10 );   		
        	}
        	else if ( args[2].equals( "INMEM" ) )
        	{
        		list = new InMemoryDataList( 
    					args[0],
    					args[1],
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
