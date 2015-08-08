package com.pannaramic.f1.sort;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SingleSort 
{
	private byte nextByte;
	
	public static void main( String[] args )
	{
		RandomAccessFile file = null;
		MappedByteBuffer buffer = null;
		try
		{
			file = new RandomAccessFile( args[0], "r" );
			long fileSize = file.length();
			long loadTime = System.currentTimeMillis();
			buffer = file.getChannel().map( FileChannel.MapMode.READ_ONLY, 0, fileSize );
			loadTime = System.currentTimeMillis() - loadTime;
			SingleSort sort = new SingleSort();
			long readTime = System.currentTimeMillis();
			for( int index = 0; index<fileSize; index++ )
			{
				sort.nextByte = buffer.get();
			}
			readTime = System.currentTimeMillis() - readTime;
			System.out.println( "Load Time " + loadTime + " Read Time " + readTime + " Next Byte " + sort.nextByte );
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				file.close();
			}
			catch( Throwable t )
			{}
		}
		
	}
}
