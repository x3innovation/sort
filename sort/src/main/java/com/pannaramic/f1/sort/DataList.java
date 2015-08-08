package com.pannaramic.f1.sort;

public interface DataList 
{
	public int	getNoOfItems();
	
	public int compare( long aFirstIndex, long aSecondIndex );
	
	public void swap( long aFirstIndex, long aSecondIndex );
	
	public void dispose();
}
