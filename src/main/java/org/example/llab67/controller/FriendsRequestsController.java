package org.example.llab67.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.llab67.domain.FriendshipDTO;
import org.example.llab67.domain.Utilizator;
import org.example.llab67.exceptions.ServiceException;
import org.example.llab67.service.Service;
import org.example.llab67.utils.events.UtilizatorEntityChangeEvent;

import java.time.LocalDateTime;
import java.util.List;

public class FriendsRequestsController {
    Service service;
    ObservableList<FriendshipDTO> modelFriendRequests = FXCollections.observableArrayList();
    Utilizator user;
    @FXML
    TableView<FriendshipDTO> tableViewFriendsRequests;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnLastName;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnEmail;
    @FXML
    TableColumn<FriendshipDTO, LocalDateTime> tableColumnSendDate;

    public void setService(Service service, Utilizator utilizator) {
        this.service = service;
        this.user=utilizator;
        initModelFriendRequests();
        initializeTableViews();
    }

    private void initModelFriendRequests() {
        List<FriendshipDTO> friendRequests = service.getFriendRequests(user.getId());
        modelFriendRequests.setAll(friendRequests);
    }

    public void update(UtilizatorEntityChangeEvent utilizatorEntityChangeEvent) {
        initModelFriendRequests();
    }

    private void initializeTableViews() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnSendDate.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        tableViewFriendsRequests.setItems(modelFriendRequests);

    }
    @FXML
    public void handleAcceptFriendRequest() {
        FriendshipDTO friend = tableViewFriendsRequests.getSelectionModel().getSelectedItem();
        if (friend != null) {
            try {
                service.acceptFriendRequest(user.getId(), service.findOneUserByNameAndEmail(friend.getFirstName(), friend.getLastName(), friend.getEmail()).getId());
                update(null);
            } catch (ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        } else {
            MessageAlert.showErrorMessage(null, "You must select a friend request!");
        }
    }

    @FXML
    public void handleDeclineFriendRequest() {
        FriendshipDTO friend = tableViewFriendsRequests.getSelectionModel().getSelectedItem();
        if (friend != null) {
            try{
                service.declineFriendRequest(user.getId(), service.findOneUserByNameAndEmail(friend.getFirstName(), friend.getLastName(), friend.getEmail()).getId());
                update(null);
            } catch (ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        } else {
            MessageAlert.showErrorMessage(null, "You must select a friend request!");
        }
    }
}
