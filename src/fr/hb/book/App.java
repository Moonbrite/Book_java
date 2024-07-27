package fr.hb.book;

import fr.hb.book.business.Author;
import fr.hb.book.business.Book;
import fr.hb.book.business.Library;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class App {
    public static List<Book> books = new ArrayList<>();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        saveToCSV("src/fr/hb/book/ReLIRE-Livres-en-gestion-collective-20240727.csv" );
        makeAction();
    }

    public static void saveToCSV(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Fichier CSV indisponible");
            System.exit(1);
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.toURI().toURL().openStream()));

            reader.readLine();
            String ligne = null;
            while ((ligne = reader.readLine()) != null) {
                // On charge les différentes parties de la ligne dans un tableau de String
                String[] elements = ligne.split(";");


                if ( elements.length > 9 && (!elements[2].isEmpty() || !elements[3].isEmpty() || !elements[1].isEmpty() || !elements[9].isEmpty() )) {
                    Author author = new Author(elements[2],elements[3]);
                    Book book = new Book(elements[1],elements[9],author);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Library library = new Library(books);
    }

    public static void addBook() {
        System.out.println("Donnez moi le nom de l'auteur");
        String nameAuthor = scanner.next();
        Author author = new Author(nameAuthor,"zzzz");
        System.out.println("bookTitle");
        String bookTitle = scanner.next();
        System.out.println("isbn ?");
        String bookIsbn = scanner.next();
        Book book = new Book(bookTitle,bookIsbn,author);
        books.add(book);
        System.out.println(book +"`\n"+ "Le livre a bien été ajouté !!");
    }

    public static int askAction(){
        System.out.println("Que voulez vous faire ?");
        System.out.println("1: Ajouter un livre" + "\n" +
                "2: Chercher par auteur" + "\n" +
                "3: Voire tout les livres dispo"+ "\n" +
                "4: Chercher par titre" + "\n" +
                "5: Supprimer un livre par ??");
        return scanner.nextInt();
    }

    private static void findBookByAuthor() {
        System.out.println("Quelle auteur cherche tu ?");
        String authorName = scanner.next();
        List<Book> booksByAuthor = books.stream()
                .filter(book -> book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .toList();

        booksByAuthor.forEach(book -> System.out.println(book.getTitle()));
    }

    public static void makeAction(){

        switch (askAction()){
            case 1:
                addBook();
                break;
            case 2:
                findBookByAuthor();
                break;
            case 3:
                displayBooks();
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private static void displayBooks() {
        AtomicInteger numberOfBookWithIsbn = new AtomicInteger();
        System.out.println("Voci la liste des livre qui on des ISBN !!");

        books.forEach(book -> {
            if(!book.getIsbn().equals("\"\"")) {
                System.out.println(book.getTitle());
                numberOfBookWithIsbn.getAndIncrement();
            }
        });

        System.out.println( "Nombre de livre avec ISBN : " + numberOfBookWithIsbn.get());

    }


}