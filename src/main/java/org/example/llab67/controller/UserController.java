package org.example.llab67.controller;
import javafx.scene.control.*;
import org.example.llab67.domain.Utilizator;
import org.example.llab67.service.Service;
import org.example.llab67.utils.events.UtilizatorEntityChangeEvent;
import org.example.llab67.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

//implements Observer<UtilizatorEntityChangeEvent>
public class UserController implements Observer<UtilizatorEntityChangeEvent>{
    private Service service;
    private Utilizator user;
    private Stage dialogStage;

    @FXML
    private Label nrFriends;

    @FXML
    private Label friendsWith;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label email;


    public void setUtilizatorService(Service service, Utilizator utilizator, Stage dialogStage) {
        this.service = service;
        this.user = utilizator;
        this.dialogStage = dialogStage;
        service.addObserver(this);
        init();
        //initialize();
    }



    @Override
    public void update(UtilizatorEntityChangeEvent event) {
        if (user != null) {
            //initialize();
            init();
        }
    }


    public void init(){
        lastName.setText(user.getLastName());
        firstName.setText(user.getFirstName());
        email.setText(user.getEmail());
        nrFriends.setText(service.numberOfFriends(user) + " friends");
        List<Utilizator> friends = service.get2Friends(user);
        if (!friends.isEmpty()){
            if(friends.size()==1){
                friendsWith.setText("friend with " + friends.getFirst().getFirstName()+" "+
                        friends.getFirst().getLastName()+", ...");
            } else {
                friendsWith.setText("friend with " + friends.get(0).getFirstName()+" "+
                        friends.get(0).getLastName()+", "+friends.get(1).getFirstName()+" "+
                        friends.get(1).getLastName()+", ...");
            }
        }
        else{
            friendsWith.setText("No friends yet");
        }
    }

    @FXML
    public void handleFriendRequests(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/friendsrequests.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FriendsRequestsController friendsRequestsController = loader.getController();
            friendsRequestsController.setService(service, user);

            dialogStage.close();

            stage.setOnHiding(event -> {
                try {
                    FXMLLoader userLoader = new FXMLLoader();
                    userLoader.setLocation(getClass().getResource("/org/example/llab67/views_new/mainwindow.fxml"));
                    AnchorPane userRoot = userLoader.load();
                    Stage userStage = new Stage();
                    userStage.initModality(Modality.WINDOW_MODAL);
                    Scene userScene = new Scene(userRoot);
                    userStage.setScene(userScene);
                    userStage.show();

                    UserController userController = userLoader.getController();
                    userController.setUtilizatorService(service, user, userStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleFriends(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/friendsnewpage.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FriendsController friendsController = loader.getController();
            friendsController.setService(service, user);

            dialogStage.close();

            stage.setOnHiding(event -> {
                try {
                    FXMLLoader userLoader = new FXMLLoader();
                    userLoader.setLocation(getClass().getResource("/org/example/llab67/views_new/mainwindow.fxml"));
                    AnchorPane userRoot = userLoader.load();
                    Stage userStage = new Stage();
                    userStage.initModality(Modality.WINDOW_MODAL);
                    Scene userScene = new Scene(userRoot);
                    userStage.setScene(userScene);
                    userStage.show();

                    UserController userController = userLoader.getController();
                    userController.setUtilizatorService(service, user, userStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChat() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/messages.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            SendMessagesController sendMessagesController = loader.getController();
            sendMessagesController.setService(service, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleAddFriend() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/sendfriendrequest.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            SendFriendRequestController sendFriendRequestController = loader.getController();
            sendFriendRequestController.setService(service, stage, user);

            dialogStage.close();

            stage.setOnHiding(event -> {
                try {
                    FXMLLoader userLoader = new FXMLLoader();
                    userLoader.setLocation(getClass().getResource("/org/example/llab67/views_new/mainwindow.fxml"));
                    AnchorPane userRoot = userLoader.load();
                    Stage userStage = new Stage();
                    userStage.initModality(Modality.WINDOW_MODAL);
                    Scene userScene = new Scene(userRoot);
                    userStage.setScene(userScene);
                    userStage.show();

                    UserController userController = userLoader.getController();
                    userController.setUtilizatorService(service, user, userStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
       } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteUser(){
        service.removeUtilizator(user.getId());
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Delete user", "Userul a fost sters");
        dialogStage.close();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/loginwdw.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LoginController loginController = loader.getController();
            loginController.setService(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
        update(null);
    }

    @FXML
    public void handleUpdateUser() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/updateprofile.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            UpdateUserController updateUserController = loader.getController();
            updateUserController.setService(service, stage, user);

            dialogStage.close();

            // Reopen the UserController window when the UpdateUserController window is closed
            stage.setOnHiding(event -> {
                Utilizator updatedUser = service.findOneUserByNameAndEmail(user.getFirstName(), user.getLastName(), user.getEmail());
                if (updatedUser != null) {
                    this.user = updatedUser;
                    init();
                }
                try {
                    FXMLLoader userLoader = new FXMLLoader();
                    userLoader.setLocation(getClass().getResource("/org/example/llab67/views_new/mainwindow.fxml"));
                    AnchorPane userRoot = userLoader.load();
                    Stage userStage = new Stage();
                    userStage.initModality(Modality.WINDOW_MODAL);
                    Scene userScene = new Scene(userRoot);
                    userStage.setScene(userScene);
                    userStage.show();

                    UserController userController = userLoader.getController();
                    userController.setUtilizatorService(service, updatedUser, userStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/llab67/views_new/loginwdw.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LoginController loginController = loader.getController();
            loginController.setService(service);
            if (dialogStage != null) {
                dialogStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
