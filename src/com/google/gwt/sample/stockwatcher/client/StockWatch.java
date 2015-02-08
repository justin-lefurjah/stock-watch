package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.NumberFormat;


	public class StockWatch implements EntryPoint{

		/**
		 ** Layout Objects 
		 **/
		private VerticalPanel mainPanel = new VerticalPanel();
		private FlexTable stockTable = new FlexTable();
		private HorizontalPanel addPanel = new HorizontalPanel();
		private TextBox newSymbolBox = new TextBox();
		private Button addStockButton = new Button("Add");
		private Label lastUpdatedLabel = new Label();
		private ArrayList<String> stocks = new ArrayList<String>();
		private static final int REFRESH_INTERVAL = 5000;
		/**
		 **   ENTRY POINT
		 **/
		
		
		public void onModuleLoad() {
			 // TODO Create table for stock data.
			
			stockTable.setText(0, 0, "Symbol");
			stockTable.setText(0, 1, "Price");
			stockTable.setText(0, 2, "Change");
			stockTable.setText(0, 3, "remove");
			
		    // TODO Assemble Add Stock panel.
			
			addPanel.add(newSymbolBox);
			addPanel.add(addStockButton);
			
		    // TODO Assemble Main panel.
			
			mainPanel.add(stockTable);
			mainPanel.add(addPanel);
			mainPanel.add(lastUpdatedLabel);
			
		    // TODO Associate the Main panel with the HTML host page.
		   
			RootPanel.get("stocklist").add(mainPanel);
			
			// TODO Move cursor focus to the input box.
			
			newSymbolBox.setFocus(true);
			
			// TODO Set up timer to automatically refresh list
			  Timer refreshTimer = new Timer() {
			      @Override
			      public void run() {
			        refreshWatchList();
			      }
			    };
			    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
			
			// Listen for mouse events on the Add button.
			
			 addStockButton.addClickHandler(new ClickHandler() {
			      public void onClick(ClickEvent event) {
			        addStock();
			      }
			 });
			 newSymbolBox.addKeyDownHandler(new KeyDownHandler() {
			      public void onKeyDown(KeyDownEvent event) {
			        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			          addStock();
			        }
			      }
			    });
			 
			      }
		
		

		      
		
		
		
		/**
	       * Add stock to FlexTable. Executed when the user clicks the addStockButton or
	       * presses enter in the newSymbolTextBox.
	   */

		private void addStock() {
			
			//gets the text from the box, makes it uppercase and takes away leading and trailing space.
			 final String symbol = newSymbolBox.getText().toUpperCase().trim();
			    newSymbolBox.setFocus(true);
			    
			    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
			    if (!symbol.matches("^[0-9A-Z&#92;&#92;.]{1,10}$")) {
			      Window.alert("'" + symbol + "' is not a valid symbol.");
			      newSymbolBox.selectAll();
			      return;
			    }

			    newSymbolBox.setText("");

			    // TODO Don't add the stock if it's already in the table.
			    
			    if(stocks.contains(symbol)){
			    	return;
			    }
			    
			    // TODO Add the stock to the table
			    
			    int row = stockTable.getRowCount();
			    stocks.add(symbol);
			    stockTable.setText(row, 0, symbol);
			    
			    
			    // TODO Add a button to remove this stock from the table.
			    
			    Button removeStockButton = new Button("x");
			    removeStockButton.addClickHandler(new ClickHandler() {
			      public void onClick(ClickEvent event) {
			        int removedIndex = stocks.indexOf(symbol);
			        stocks.remove(removedIndex);
			        stockTable.removeRow(removedIndex + 1);
			      }
			    });
			    
			    stockTable.setWidget(row, 3, removeStockButton);
			    
			    // TODO Get the stock price.
			    
			    refreshWatchList();

	      }



		  private void refreshWatchList() {
		    final double MAX_PRICE = 100.0; // $100.00
		    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

		    StockPrice[] prices = new StockPrice[stocks.size()];
		    
		    for (int i = 0; i < stocks.size(); i++) {
		      double price = Random.nextDouble() * MAX_PRICE;
		      double change = price * MAX_PRICE_CHANGE
		              * (Random.nextDouble() * 2.0 - 1.0);

		          prices[i] = new StockPrice(stocks.get(i), price, change);
		    }

		        updateTable(prices);
		  }
		
		
	private void updateTable(StockPrice[] prices) {
	    // TODO Auto-generated method stub
		for (int i = 0; i < prices.length; i++) {
			updateTable(prices[i]);
	      }
	
	
	
	
		}
	
	
	
	/**
     * Update a single row in the stock table.
 *
     * @param price Stock data for a single row.
 */
		private void updateTable(StockPrice price) {
		  // Make sure the stock is still in the stock table.
		  if (!stocks.contains(price.getSymbol())) {
		    return;
		  }

      int row = stocks.indexOf(price.getSymbol()) + 1;

      // Format the data in the Price and Change fields.
	  String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
	  NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
	  String changeText = changeFormat.format(price.getChange());
	  String changePercentText = changeFormat.format(price.getChangePercent());

      // Populate the Price and Change fields with new data.
	  stockTable.setText(row, 1, priceText);
	  stockTable.setText(row, 2, changeText + " (" + changePercentText + "%)");
}

		
		
		
			
		
		
		
		
	}