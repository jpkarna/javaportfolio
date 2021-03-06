package com.stt.portfolio.gui.treetable;

import com.stt.portfolio.BookEntry;
import com.stt.portfolio.BookEntryItem;
import com.stt.portfolio.XIRRAdapter;

public class BookEntryNode {

	BookEntry bookEntry = null;
	Object[] items = null;
	Object[] row = new Object[15];
	XIRRAdapter xirrAdapter = new XIRRAdapter();
	static Object[] dummy = new Object[0];

	public BookEntryNode(BookEntry bookEntry) {
		this.bookEntry = bookEntry;
		populateRow(bookEntry);
		Object[] bookEntryItems = bookEntry.getChildren();
		items = new BookEntryItemNode[bookEntryItems.length];

		int i = 0;
		for (Object o : bookEntryItems) {
			items[i++] = new BookEntryItemNode((BookEntryItem) o, this);
		}

	}

	protected Object[] getChildren() {
		if (items.length > 1) {
			return items;
		}
		return dummy;
	}

	public BookEntry getBookEntry() {
		return bookEntry;
	}

	public String toString() {
		return bookEntry.getName();
	}

	public Class getColumnClass(int column) {
		return row[column].getClass();
	}
	
	public Object getColumnValue(int column) {
		return row[column];
	}
	
	private void populateRow(BookEntry e) {
		
		
		int j = 0;
		row[j++] = e.getName();
		row[j++] = e.getCcy();

		row[j++] = new Double(e.getMarketPrice() * e.getRate());
		row[j++] = (e.getQuoteDate() != null) ? e.getQuoteDate() : "N/A";

		row[j++] = new Integer((int) e.getAmount());
		// table[i][j++] = new Double(e.getCost());
		// table[i][j++] = String.format("%1$.2f", e.getPrice());
		row[j++] = new Double(e.getPriceOriginalCurrency()); // Show buy price
																// in original
																// currency
		row[j++] = e.getPurchaseDate();

		row[j++] = new Double(e.getMarketPrice() * e.getRate() * e.getAmount());
		row[j++] = new Double(e.getMarketPrice() * e.getAmount());

		row[j++] = new Double(e.getTaxCost());
		row[j++] = new Double(e.getDividents());
		row[j++] = new Double(e.getProfit(e.getMarketPrice()));
		row[j++] = new Double(e.getProfitPercent(e.getMarketPrice()));
		row[j++] = new Double(xirrAdapter.getXirr(e.getQuoteDate(), e));
		row[j++] = new Double(0.0);
	}
	
	
}
