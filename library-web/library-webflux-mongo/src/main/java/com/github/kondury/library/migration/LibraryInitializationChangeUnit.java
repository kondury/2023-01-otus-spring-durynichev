package com.github.kondury.library.migration;

import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.repository.GenreRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.*;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import org.bson.Document;

import java.util.List;
import java.util.Map;

@ChangeUnit(id = "initial-documents", order = "001", author = "kondury")
public class LibraryInitializationChangeUnit {

    private static final Map<String, Class> collections = Map.of(
            "genres", Genre.class, "authors", Author.class,
        "books", Book.class, "comments", Comment.class);

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        collections.keySet().forEach(collection -> {
            SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
            mongoDatabase.createCollection(collection).subscribe(subscriber);
            subscriber.await();
        });
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        collections.keySet().forEach(collection -> {
            SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
            mongoDatabase.getCollection(collection).drop().subscribe(subscriber);
            subscriber.await();
        });
    }

    @Execution
    public void execution(AuthorRepository authorRepository, GenreRepository genreRepository,
                          BookRepository bookRepository, CommentRepository commentRepository) {

        Author stanislavLem = new Author("Stanislaw Lem");
        Author frankHerbert = new Author("Frank Herbert");
        Author agathaChristie = new Author("Agatha Christie");
        final List<Author> authors = List.of(stanislavLem, frankHerbert, agathaChristie);
        authorRepository.saveAll(authors).blockLast();

        Genre scienceFiction = new Genre("Science Fiction");
        Genre detective = new Genre("Detective");
        final List<Genre> genres = List.of(scienceFiction, detective);
        genreRepository.saveAll(genres).blockLast();

        Book solaris = new Book("Solaris", stanislavLem, scienceFiction);
        Book dune = new Book("Dune", frankHerbert, scienceFiction);
        Book murderOnTheOrientExpress = new Book("Murder on the Orient Express", agathaChristie, detective);
        final List<Book> books = List.of(solaris, dune, murderOnTheOrientExpress);
        bookRepository.saveAll(books).blockLast();

        final List<Comment> comments = List.of(
                new Comment("Have already read it twice and will do more", solaris),
                new Comment("It's the real masterpiece!!! Must read!!!", dune),
                new Comment("As for me it was a little bit too lingering and somewhat boring", dune),
                new Comment("Classics means classics! Neither more nor less", murderOnTheOrientExpress)
        );
        commentRepository.saveAll(comments).blockLast();
    }

    @RollbackExecution
    public void rollbackExecution(MongoDatabase mongoDatabase) {

        collections.forEach((key, value) -> {
            SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();
            mongoDatabase.getCollection(key, value)
                    .deleteMany(new Document())
                    .subscribe(subscriber);
            subscriber.await();
        });
    }
}
