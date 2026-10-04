//author: Alyssa
package bookClub;

import java.io.Serializable;

public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	private int bookID;
	private String isbn;
	private String title;
	private String author;
	
	/**
	 * constructor for book
	 * @param bookID unique id for the book
	 * @param isbn unique id number for the book
	 * @param title title of the book
	 * @param author author of the book
	 */
	public Book(int bookID, String isbn, String title, String author) {
		this.bookID = bookID;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}
	
	/**
	 * 
	 * @return bookID number of the book
	 */
	public int getBookID() {
		return bookID;
	}
	
	/**
	 * 
	 * @return isbn number of the book
	 */
	public String getISBN() {
		return isbn;
	}
	
	/**
	 * 
	 * @return title of the book
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 
	 * @return author of the book
	 */
	public String getAuthor() {
		return author;
	}
}
