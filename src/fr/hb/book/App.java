package fr.hb.book;

import com.sun.source.tree.TryTree;
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
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                // On charge les différentes parties de la ligne dans un tableau de String
                String[] elements = ligne.split(";");


                if ( elements.length > 9 && (!elements[2].isEmpty() || !elements[7].isEmpty() || !elements[1].isEmpty() || !elements[9].isEmpty() )) {
                    Author author = new Author(elements[2],elements[7]);
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Donnez moi le nom de l'auteur");
        String nameAuthor = scanner.nextLine();
        System.out.println("Donne moi sa biographie");
        String biographieAuthor = scanner.nextLine();
        Author author = new Author(nameAuthor,biographieAuthor);
        System.out.println("Le titre de ton livre");
        String bookTitle = scanner.nextLine();
        System.out.println("Son isbn ?");
        String bookIsbn = scanner.next();
        Book book = new Book(bookTitle,bookIsbn,author);
        books.add(book);
        System.out.println(book +"`\n"+ "Le livre a bien été ajouté !!");


    }

    public static int askAction(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Que voulez vous faire ?");
        System.out.println("""
                1: Ajouter un livre
                2: Chercher par auteur
                3: Voire tout les livres dispo
                4: Chercher par titre
                5: Supprimer un livre par son auteur/titre""");
        return scanner.nextInt();
    }

    private static void findBookByAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quelle auteur cherche tu ?");
        String authorName = scanner.nextLine();
        List<Book> booksByAuthor = books.stream()
                .filter(book -> book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .toList();
        if (booksByAuthor.isEmpty()) {
            System.out.println("Aucun livre trouvé pour l'auteur : " + authorName);
        } else {
            System.out.println("Livres trouvés pour l'auteur " + authorName + ":");
            booksByAuthor.forEach(book -> System.out.println("id: " + book.getId() + " / " + book.getTitle()));
        }
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
                findBookByTitle();
                break;
            case 5:
                deleteBook();
                break;
            default:
                System.out.println("Le nombre que vous avez rentré n'est pas autorisé");
                makeAction();
        }
        remake();
    }

    public static void remake(){
        Scanner scan= new Scanner(System.in);
        System.out.println("Voulez vous faire une autre action ? (0/N)");
        String choiceRemake = scan.next().toUpperCase();
        if (choiceRemake.equals("O")){
            makeAction();
        }else{
            System.out.println("Merci d'avoir utilisé notre application !");
        }
    }

    private static void deleteBook() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Par quelle moyen veux tu trouver ton livre pour le supprimer ?");
        System.out.println("1: Par nom de l'auteur; \n" +
                "2: Par titre du livre");
        int choiceUser = scanner.nextInt();
        switch (choiceUser){
            case 1 :
                deleteBookByAuthor();
                break;
            case 2 :
                deleteBookByTitle();
                break;
        }

    }

    private static void deleteBookByTitle() {

        findBookByTitle();
        Scanner scan= new Scanner(System.in);
        System.out.println("Noter l'id du livre que vous voulez supprimer");
        int bookId = scan.nextInt();
        System.out.println("Le livre "+ books.get(bookId -1).getTitle() + " a bien été supprimé.");
        books.remove(bookId -1);
    }

    private static void deleteBookByAuthor() {

        findBookByAuthor();
        Scanner scan= new Scanner(System.in);
        System.out.println("Noter l'id du livre que vous voulez supprimer");
        int bookId = scan.nextInt();
        System.out.println();
        System.out.println("Le livre de "+ books.get(bookId -1).getTitle() + " a bien été supprimé.");
        books.remove(bookId -1);
    }

    private static void findBookByTitle() {
        Scanner sca = new Scanner(System.in);
        System.out.println("Donne moi le titre de ton livre");
        String bookTitle = sca.nextLine();
        System.out.println(bookTitle);

        List<Book> booksByTitle = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                .toList();

        if (booksByTitle.isEmpty()) {
            System.out.println("Aucun livre trouvé pour le titre : " + bookTitle);
        } else {
            System.out.println("Livres trouvés pour le titre " + bookTitle + ":");
            booksByTitle.forEach(book -> System.out.println("id: " + book.getId() + " / " +  book.getTitle()));
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