package co.nz.bookpublish.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOK")
public class BookModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_ID")
	private Long bookId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "PAGES")
	private Integer pages;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "AUTHOR")
	private String author;

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

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
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

	public static Builder getBuilder(String title, String isbn, Integer pages,
			BigDecimal price, String author) {
		return new Builder(title, isbn, pages, price, author);
	}

	public static Builder getBuilder(String title, String isbn, Integer pages,
			BigDecimal price, String author, Date publishDate) {
		return new Builder(title, isbn, pages, price, author, publishDate);
	}

	public static class Builder {
		private BookModel built;

		public Builder(String title, String isbn, Integer pages,
				BigDecimal price, String author) {
			built = new BookModel();
			built.title = title;
			built.isbn = isbn;
			built.pages = pages;
			built.price = price;
			built.author = author;
		}

		public Builder(String title, String isbn, Integer pages,
				BigDecimal price, String author, Date publishDate) {
			built = new BookModel();
			built.title = title;
			built.isbn = isbn;
			built.pages = pages;
			built.price = price;
			built.publishDate = publishDate;
			built.author = author;
		}

		public BookModel build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("bookId", bookId).append("title", title)
				.append("isbn", isbn).append("pages", pages)
				.append("author", author).append("publishDate", publishDate)
				.append("price", price).toString();
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.isbn, ((BookModel) obj).isbn).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.isbn).toHashCode();
	}
}
