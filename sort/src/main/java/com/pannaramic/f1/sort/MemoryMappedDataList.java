package com.pannaramic.f1.sort;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class MemoryMappedDataList 
implements	DataList
{
	private RandomAccessFile file;
	private long recordLength;
	private int indexStartOffset;
	private int indexEndOffset;
	private MappedByteBuffer buffer;
	private int noOfRecords;
	private byte[] tempRecord1;
	private byte[] tempRecord2;
	
	/**
	 * create memory mapped data list
	 * @param aFileName file name
	 * @param aLengthOfRecord length of record
	 * @param aIndexStartOffset offset 
	 */
	public	MemoryMappedDataList
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
		long fileSize = file.length();
		noOfRecords = (int)(fileSize/recordLength);
		tempRecord1 = new byte[aLengthOfRecord];
		tempRecord2 = new byte[aLengthOfRecord];
		buffer = file.getChannel().map( FileChannel.MapMode.READ_WRITE, 0, fileSize );
		buffer.load();
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
		int firstIndexAddress = (int)(aFirstIndex * recordLength);
		int secondIndexAddress = (int)(aSecondIndex * recordLength);
		for( int offset=indexStartOffset; offset<indexEndOffset; offset++ )
		{
			int firstByte = buffer.get( firstIndexAddress+offset ) & 0xff;
			int secondByte = buffer.get( secondIndexAddress+offset ) & 0xff;
			if ( firstByte > secondByte )
			{
				return 1;
			}
			else if ( firstByte < secondByte )
			{
				return -1;
			}
		}
		return 0;
	}

	@Override
	public void swap(long aFirstIndex, long aSecondIndex) 
	{
		//long time = System.nanoTime();
		long firstIndexAddress = aFirstIndex * recordLength;
		long secondIndexAddress = aSecondIndex * recordLength;
		buffer.position( (int)firstIndexAddress );
		buffer.get(tempRecord1, 0, (int)recordLength);
		buffer.position( (int)secondIndexAddress );
		buffer.get(tempRecord2, 0, (int)recordLength);
		buffer.position( (int)firstIndexAddress );
		buffer.put(tempRecord2, 0, (int)recordLength);
		buffer.position( (int)secondIndexAddress );
		buffer.put(tempRecord1, 0, (int)recordLength);
		//time = System.nanoTime() - time;
		//System.out.println( "Swap Time " + time );
	}

	@Override
	public void dispose() 
	{
		buffer.force();
		try
		{
			file.close();
		}
		catch( Throwable t )
		{}
	}

}
