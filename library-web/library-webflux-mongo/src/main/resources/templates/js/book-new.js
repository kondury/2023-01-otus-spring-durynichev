import {saveBook, handleResponse, populateAuthors, populateGenres} from "/js/book-edit-common.js";
import {bookTitleElement, bookAuthorElement, bookGenreElement, errorsElement} from "/js/book-edit-common.js";

const selectAuthorCaption = [[#{select-author-caption}]];
const selectGenreCaption = [[#{select-genre-caption}]];
const errorsElementHeader = [[#{validation-errors-header}]];

document.getElementById('save-book-button').addEventListener("click", insertBook)
populateNewBookForm();

async function populateNewBookForm() {
    bookTitleElement.setAttribute("value", "");
    populateAuthors(bookAuthorElement, null, selectAuthorCaption);
    populateGenres(bookGenreElement, null, selectGenreCaption);
}

async function insertBook() {
    const book = {
        title: bookTitleElement.value,
        authorId: bookAuthorElement.value,
        genreId: bookGenreElement.value
    }
    saveBook(book, '/api/books', 'POST')
        .then(response => handleResponse(response, errorsElement, errorsElementHeader))
}


