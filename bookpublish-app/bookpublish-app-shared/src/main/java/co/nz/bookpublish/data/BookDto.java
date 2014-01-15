package co.nz.bookpublish.data;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class BookDto implements Serializable {
	private Long bookId;

	private String title;

	private String author;

	private String isbn;

	private Integer pages;

	private String publishDate;

	private BigDecimal price;

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public static Builder getBuilder(String isbn, String title,
			BigDecimal price, String author, Integer pages, String publishDate) {
		return new Builder(isbn, title, price, author, pages, publishDate);
	}

	public static Builder getBuilder(String isbn, String title,
			BigDecimal price, String author, Integer pages) {
		return new Builder(isbn, title, price, author, pages);
	}

	public static class Builder {

		private BookDto built;

		public Builder(String isbn, String title, BigDecimal price,
				String author, Integer pages, String publishDate) {
			built = new BookDto();
			built.isbn = isbn;
			built.price = price;
			built.author = author;
			built.title = title;
			built.pages = pages;
			built.publishDate = publishDate;
		}

		public Builder(String isbn, String title, BigDecimal price,
				String author, Integer pages) {
			built = new BookDto();
			built.isbn = isbn;
			built.price = price;
			built.author = author;
			built.pages = pages;
			built.title = title;
		}

		public BookDto build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.bookId, ((BookDto) obj).bookId).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.bookId).toHashCode();
	}
}
