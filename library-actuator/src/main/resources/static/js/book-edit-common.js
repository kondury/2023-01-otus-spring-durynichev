export const bookIdElement = document.getElementById('book-id');
export const bookTitleElement = document.getElementById("book-title");
export const bookAuthorElement = document.getElementById('book-author');
export const bookGenreElement = document.getElementById('book-genre');
export const errorsElement = document.getElementById("validation-errors")

export function saveBook(book, uri, httpMethod) {

    const csrfToken = document.cookie
        .replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1');

    return fetch(uri, {
        method: httpMethod,
        headers: {
            'X-XSRF-TOKEN': csrfToken,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)
    });
}

export async function handleResponse(response, errorsElement, errorsElementHeader) {
    if (!response.ok) {
        const json = await response.json();
        errorsElement.innerHTML = `
            <h4>${errorsElementHeader}</h4>
            <div>${json.errors.join('<br>')}</div>
        `;
    } else {
        location.href = '/books/list'
    }
}

export async function populateAuthors(bookAuthorElement, selectedAuthorId, selectAuthorCaption) {
    populateSelectList('/api/authors',
        bookAuthorElement, selectedAuthorId, selectAuthorCaption,
        'id', 'name');
}

export async function populateGenres(bookGenreElement, selectedGenreId, selectGenreCaption) {
    populateSelectList('/api/genres',
        bookGenreElement, selectedGenreId, selectGenreCaption,
        'id', 'name');
}

async function populateSelectList(uri, selectElement, selectedValue, selectCaption, valueField, textField) {
    const response = await fetch(uri, {method: 'GET'});
    const items = await response.json();
    let temp = selectedValue == null ? `<option value=\"\" selected >${selectCaption}</option>\n` : "";
    temp += items.map(it => {
        let value = it[valueField];
        return `
            <option value="${value}" ${value === selectedValue ? "selected" : ""}>
                ${it[textField]}
            </option>\n
        `;
    }).join('\n');

    selectElement.innerHTML = temp
}