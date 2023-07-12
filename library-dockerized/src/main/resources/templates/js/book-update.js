import {saveBook, handleResponse, populateAuthors, populateGenres} from "/js/book-edit-common.js";
import {
    bookIdElement,
    bookTitleElement,
    bookAuthorElement,
    bookGenreElement,
    errorsElement
} from "/js/book-edit-common.js";


const selectAuthorCaption = [[#{select-author-caption}]];
const selectGenreCaption = [[#{select-genre-caption}]];
const errorsElementHeader = [[#{validation-errors-header}]];

document.getElementById('save-book-button').addEventListener("click", updateBook)
populateUpdateBookForm([[${bookId}]]);

async function populateUpdateBookForm(bookId) {
    const book = await getBookDetails(bookId);
    const bookExists = book != null;
    document.getElementById("book-update-form").hidden = !bookExists;
    document.getElementById("book-does-not-exist").hidden = bookExists;
    document.getElementById("save-book-button").disabled = !bookExists;
    if (bookExists) {
        bookIdElement.setAttribute("value", book.id);
        bookTitleElement.setAttribute("value", book.title)
        populateAuthors(bookAuthorElement, book.author.id, selectAuthorCaption);
        populateGenres(bookGenreElement, book.genre.id, selectGenreCaption);
    }
}

async function getBookDetails(bookId) {
    return fetch('/api/books/' + bookId, {
        method: 'GET'
    })
        .then(response => response.json());
}

async function updateBook() {
    const book = {
        id: bookIdElement.value,
        title: bookTitleElement.value,
        authorId: bookAuthorElement.value,
        genreId: bookGenreElement.value
    }
    saveBook(book, '/api/books/' + book.id, 'PUT')
        .then(response => handleResponse(response, errorsElement, errorsElementHeader));
}
