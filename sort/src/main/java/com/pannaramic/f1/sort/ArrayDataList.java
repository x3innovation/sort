package com.pannaramic.f1.sort;

import java.util.ArrayList;

public class ArrayDataList 
implements DataList
{
	private ArrayList<Comparable>	list
									= new ArrayList<Comparable>();
	@Override
	public int getNoOfItems() 
	{
		return list.size();
	}

	@Override
	public int compare(long aFirstIndex, long aSecondIndex) 
	{
		return list.get((int)aFirstIndex).compareTo(list.get((int)aSecondIndex));
	}

	@Override
	public void swap(long aFirstIndex, long aSecondIndex) {
		Comparable temp = list.set( (int)aFirstIndex, list.get( (int)aSecondIndex ) );
		list.set( (int)aSecondIndex, temp );	
	}

	public ArrayDataList	add( Comparable aValue )
	{
		list.add( aValue );
		return this;
	}
	
	public String	toString()
	{
		return list.toString();
	}

	@Override
	public void dispose() 
	{
	}
}
