package service;

public class GoodReadsBookLoader {

    //TODO: request shelves
    //https://www.goodreads.com/shelf/list.xml?key=aB9VcY1rOGCzxMONqjk8Ug&user_id=60626198&page=1

    //TODO: get book_count from read shelve
    //TODO: configure page=1,200&per_page=200 in order to get all books
    //https://www.goodreads.com/review/list?v=2&key=aB9VcY1rOGCzxMONqjk8Ug&id=60626198&shelf=read&page=1,200&per_page=200

    //TODO: set original publication date on book
    //TODO: pass book id to check if result is right + isbn

    //TODO: get stats

    //TODO: DATA VALIDATOR -> what stats will be unreliable or unavailable -> local list of good values

    //TODO: https://www.goodreads.com/book/isbn/0441172717?key=xQXvrwOTLq7xonOLcjt2A
    //TODO: get top shelves, exlude below, select 3 genres
//    genreExceptions = [
//            'to-read', 'currently-reading', 'owned', 'default', 'favorites', 'books-i-own',
//            'ebook', 'kindle', 'library', 'audiobook', 'owned-books', 'audiobooks', 'my-books',
//            'ebooks', 'to-buy', 'english', 'calibre', 'books', 'british', 'audio', 'my-library',
//            'favourites', 're-read', 'general', 'e-books'
//            ]


    //TODO: most important!
    //https://www.goodreads.com/book/show/50.xml?key=aB9VcY1rOGCzxMONqjk8Ug
    //get original_publication_year and shelves in one call!!

    //TODO: OR research         ISBNdb API
    //https://openlibrary.org/developers/api

    //TODO: idea - if can't get isbn from edition, try others


}