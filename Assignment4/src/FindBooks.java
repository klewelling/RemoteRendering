import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Created by Isaac on 7/6/2015.
 */
public class FindBooks extends Application {

    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatement2;
    private TextField tfBooks = new TextField();
    private String[] authors = new String[5];
    ListView<String> listView;

    @Override
    public void start(Stage primaryStage) {

        initializeDB();

        try {
            ResultSet resultSet = preparedStatement2.executeQuery();
            int i = 0;

            while(resultSet.next()){
                String author = resultSet.getString(1);
                authors[i] = author;
                i++;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        listView = new ListView<>
                (FXCollections.observableArrayList(authors));
        listView.setPrefSize(300, 300);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button btFindBooks = new Button("Show Books");
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(new Label("Author"), new ScrollPane(listView), (btFindBooks));

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox, tfBooks);

        btFindBooks.setOnAction(e -> showBooks());

        Scene scene = new Scene(vBox, 520, 300);
        primaryStage.setTitle("FindBooks");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void initializeDB() {
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true");
            System.out.println("Database connected");

            String queryString = "SELECT title " +
                                 "FROM Written_by, Book " +
                                 "WHERE authorName = ? AND Written_by.ISBN = Book.ISBN";

            preparedStatement = connection.prepareStatement(queryString);

            String queryString2 = "SELECT * " +
                                  "FROM Author";

            preparedStatement2 = connection.prepareStatement(queryString2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showBooks() {
        String author = listView.getSelectionModel().getSelectedItem();
        try {
            preparedStatement.setString(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();

            tfBooks.setText("");

            while(resultSet.next()) {
                String book = resultSet.getString(1);

            if(!tfBooks.getText().isEmpty()){
                tfBooks.appendText(", ");
            }
                tfBooks.appendText(book);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
