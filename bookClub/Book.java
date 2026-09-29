package bookClub;

import java.io.Serializable;

public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	private int isbn;
	private String title;
	private String author;
	
	/**
	 * constructor for book
	 * @param isbn unique id number for the book
	 * @param title title of the book
	 * @param author author of the book
	 */
	public Book(int isbn, String title, String author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}
	
	/**
	 * 
	 * @return isbn number of the book
	 */
	public int getISBN() {
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
