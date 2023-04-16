package com.github.kondury.library.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

import com.github.kondury.library.repository.CommentRepository;
import com.github.kondury.library.repository.AuthorRepository;
import com.github.kondury.library.repository.BookRepository;
import com.github.kondury.library.repository.GenreRepository;
import com.github.kondury.library.domain.Author;
import com.github.kondury.library.domain.Book;
import com.github.kondury.library.domain.Genre;
import com.github.kondury.library.domain.Comment;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author stanislavLem;
    private Author frankHerbert;
    private Author agathaChristie;

    private Genre scienceFiction;
    private Genre detective;
    private Book solaris;
    private Book dune;
    private Book murderOnTheOrientExpress;

    @ChangeSet(order = "000", id = "dropDB", author = "kondury", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "kondury", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        stanislavLem = repository.save(new Author("Stanislaw Lem"));
        frankHerbert = repository.save(new Author("Frank Herbert"));
        agathaChristie = repository.save(new Author("Agatha Christie"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "kondury", runAlways = true)
    public void initGenres(GenreRepository repository) {
        scienceFiction = repository.save(new Genre("Science Fiction"));
        detective = repository.save(new Genre("Detective"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "kondury", runAlways = true)
    public void initBooks(BookRepository repository) {
        solaris = repository.save(new Book("Solaris", stanislavLem, scienceFiction));
        dune = repository.save(new Book("Dune", frankHerbert, scienceFiction));
        murderOnTheOrientExpress = repository.save(new Book("Murder on the Orient Express", agathaChristie, detective));
    }

    @ChangeSet(order = "004", id = "initComments", author = "kondury", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment("Have already read it twice and will do more", solaris));
        repository.save(new Comment("It's the real masterpiece!!! Must read!!!", dune));
        repository.save(new Comment("As for me it was a little bit too lingering and somewhat boring", dune));
        repository.save(new Comment("Classics means classics! Neither more nor less", murderOnTheOrientExpress));
    }

}
