package com.pannaramic.f1.sort;

import java.io.RandomAccessFile;

public final class InMemoryDataList 
implements	DataList
{
	private long recordLength;
	private int indexStartOffset;
	private int indexEndOffset;
	private int noOfRecords;
	private byte[] tempRecord;
	private byte[] data;
	private String targetFileName;
	
	/**
	 * create in memory data list
	 * @param aSourceFileName source file name
	 * @param aTargetFileName target file name
	 * @param aLengthOfRecord length of record
	 * @param aIndexStartOffset offset 
	 */
	public	InMemoryDataList
			( String	aSourceFileName,
			  String	aTargetFileName,
			  int		aLengthOfRecord,
			  int		anIndexStartOffset,
			  int		anIndexEndOffset )
	throws	Exception
	{
		targetFileName = aTargetFileName;
		recordLength = aLengthOfRecord;
		indexStartOffset = anIndexStartOffset;
		indexEndOffset = anIndexEndOffset;
		RandomAccessFile file = new RandomAccessFile( aSourceFileName, "r" );
		long fileSize = file.length();
		data = new byte[(int)fileSize];
		noOfRecords = (int)(fileSize/recordLength);
		tempRecord = new byte[aLengthOfRecord];
		file.read( data );
		file.close();
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
			int firstByte = data[firstIndexAddress+offset] & 0xff;
			int secondByte = data[secondIndexAddress+offset] & 0xff;
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
		long firstIndexAddress = aFirstIndex * recordLength;
		long secondIndexAddress = aSecondIndex * recordLength;
		System.arraycopy( data, (int)firstIndexAddress, tempRecord, 0, (int)recordLength );
		System.arraycopy( data, (int)secondIndexAddress, data, (int)firstIndexAddress, (int)recordLength );
		System.arraycopy( tempRecord, 0, data, (int)secondIndexAddress, (int)recordLength );
	}

	@Override
	public void dispose() 
	{
		try
		{
			RandomAccessFile file = new RandomAccessFile( targetFileName, "rw" );
			file.write( data );
			file.close();
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
	}

}
