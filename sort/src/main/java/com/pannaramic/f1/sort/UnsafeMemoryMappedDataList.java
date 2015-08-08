package com.pannaramic.f1.sort;

import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import oracle.jrockit.jfr.events.Bits;
import sun.misc.Unsafe;
import sun.nio.ch.FileChannelImpl;

public final class UnsafeMemoryMappedDataList 
implements	DataList
{
	private static final Unsafe unsafe;
	private static final Method mmap;
	private static final Method unmmap;
	private static final int BYTE_ARRAY_OFFSET;	
	
	static 
	{
		try 
		{
			Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
			singleoneInstanceField.setAccessible(true);
			unsafe = (Unsafe) singleoneInstanceField.get(null);
 
			mmap = getMethod(FileChannelImpl.class, "map0", int.class, long.class, long.class);
			unmmap = getMethod(FileChannelImpl.class, "unmap0", long.class, long.class);
 
			BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//Bundle reflection calls to get access to the given method
	private static Method getMethod(Class<?> cls, String name, Class<?>... params) throws Exception 
	{
		Method m = cls.getDeclaredMethod(name, params);
		m.setAccessible(true);
		return m;
	}
 
	//Round to next 4096 bytes
	private static long roundTo4096(long i) 
	{
		return (i + 0xfffL) & ~0xfffL;
	}
 
	private RandomAccessFile file;
	private long recordLength;
	private int indexStartOffset;
	private int indexEndOffset;
	private int noOfRecords;
	private byte[] tempRecord;
	long fileSize = 0;
	private long address;
	private byte unused = 0;
	
	/**
	 * create memory mapped data list
	 * @param aFileName file name
	 * @param aLengthOfRecord length of record
	 * @param aIndexStartOffset offset 
	 */
	public	UnsafeMemoryMappedDataList
			( String	aFileName,
			  int		aLengthOfRecord,
			  int		anIndexStartOffset,
			  int		anIndexEndOffset )
	throws	Exception
	{
		recordLength = aLengthOfRecord;
		indexStartOffset = anIndexStartOffset;
		indexEndOffset = anIndexEndOffset;
		file = new RandomAccessFile( aFileName, "rw" );
		fileSize = file.length();
		noOfRecords = (int)(fileSize/recordLength);
		tempRecord = new byte[aLengthOfRecord];
		FileChannel channel = file.getChannel();
		address = (long)mmap.invoke( channel, 1, 0L, fileSize );
		channel.close();
		file.close();
		// load each byte from each page to force pre loading
		int pageSize = unsafe.pageSize();
		int pageCount = (int)(fileSize + (long)pageSize - 1L) / pageSize;
		byte readByte = 0;
		for( int pageIndex=0; pageIndex<pageCount; pageIndex++ )
		{
			readByte ^= unsafe.getByte( address + pageIndex * pageSize );
		}
		if ( unused != 0 )
		{
			unused = readByte;
		}
	}

	@Override
	public int getNoOfItems() 
	{
		return noOfRecords;
	}

	@Override
	public int compare(long aFirstIndex, long aSecondIndex) 
	{
		if ( aFirstIndex == aSecondIndex )
		{
			return 0;
		}
		long firstIndexAddress = aFirstIndex * recordLength + address;
		long secondIndexAddress = aSecondIndex * recordLength + address;
		for( int offset=indexStartOffset; offset<indexEndOffset; offset++ )
		{
			int firstByte = unsafe.getByte(firstIndexAddress+offset) & 0xff;
			int secondByte = unsafe.getByte(secondIndexAddress+offset) & 0xff;
			if ( firstByte > secondByte )
			{
//System.out.println( "LARGER Offset " + offset + " First " + String.format( "%02X", unsafe.getByte(firstIndexAddress) ) + " Second "  + String.format( "%02X", unsafe.getByte(secondIndexAddress) )  );
				return 1;
			}
			else if ( firstByte < secondByte )
			{
//System.out.println( "SMALLER Offset " + offset + " First " + String.format( "%02X", unsafe.getByte(firstIndexAddress) ) + " Second "  + String.format( "%02X", unsafe.getByte(secondIndexAddress) )  );				
				return -1;
			}
		}
		return 0;
	}

	@Override
	public void swap(long aFirstIndex, long aSecondIndex) 
	{
		//long time = System.nanoTime();
		long firstIndexAddress = aFirstIndex * recordLength + address;
		long secondIndexAddress = aSecondIndex * recordLength + address;
//System.out.println( "Swapping " + aFirstIndex + " " + aSecondIndex + " First " + String.format( "%02X", unsafe.getByte(firstIndexAddress) ) 
//		+ " Second "  + String.format( "%02X", unsafe.getByte(secondIndexAddress) )  );

		unsafe.copyMemory( null, firstIndexAddress, tempRecord, BYTE_ARRAY_OFFSET, recordLength );
		unsafe.copyMemory( null, secondIndexAddress, null, firstIndexAddress, recordLength );
		unsafe.copyMemory( tempRecord, BYTE_ARRAY_OFFSET, null, secondIndexAddress, recordLength );	
//System.out.println( "After Swamping First " + String.format( "%02X", unsafe.getByte(firstIndexAddress) ) 
//				+ " Second "  + String.format( "%02X", unsafe.getByte(secondIndexAddress) )  );		
		//time = System.nanoTime() - time;
		//System.out.println( "Swap Time " + time );
	}

	@Override
	public void dispose() 
	{
		/*
int previous1 = 0;
int previous2 = 0;
for( int index=0; index<noOfRecords; index++ )
{
	int currentValue1 = unsafe.getByte(index * recordLength + address) & 0xff;
	int currentValue2 = unsafe.getByte(index * recordLength + address + 1) & 0xff;
	if ( currentValue1 < previous1 || ( currentValue1 <= previous1 && currentValue2 < previous2 ) )
	{
		System.out.println( "WRONG VALUE" );
	}
	previous1 = currentValue1;
	previous2 = currentValue2;
	System.out.println( "INDEX " + index + " " +  String.format( "%02X", unsafe.getByte(index * recordLength + address) ) + String.format( "%02X", unsafe.getByte(index * recordLength + address +1) ) );
}
*/
		try
		{
			unmmap.invoke( null, address, fileSize );
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
	}

}
