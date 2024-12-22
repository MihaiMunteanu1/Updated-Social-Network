package org.example.llab67.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.llab67.domain.FriendshipDTO;
import org.example.llab67.domain.Utilizator;
import org.example.llab67.service.Service;
import org.example.llab67.utils.events.UtilizatorEntityChangeEvent;
import org.example.llab67.utils.paging.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class FriendsController {
    Service service;

    ObservableList<FriendshipDTO> modelFriends = FXCollections.observableArrayList();

    Utilizator user;

    private int currentPage=0;
    private static final int PAGE_SIZE = 5;
    @FXML
    Label labelCurrentPage;

    @FXML
    Button buttonNextPage;

    @FXML
    Button buttonPreviousPage;
    @FXML
    TableView<FriendshipDTO> tableViewFriends;

    @FXML
    TableColumn<FriendshipDTO, String> tableColumnFirstNameFriend;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnLastNameFriend;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnEmailFriend;
    @FXML
    TableColumn<FriendshipDTO, LocalDateTime> tableColumnFriendsFrom;

    public void setService(Service service, Utilizator utilizator) {
        this.service = service;
        this.user=utilizator;
        initModelFriends();
        initializeTableViews();
    }
    private void initModelFriends() {
        Pageable pageable = new Pageable(currentPage, PAGE_SIZE);
        List<FriendshipDTO> friends = (List<FriendshipDTO>) service.findAllUserFriends(pageable,user).getElementsOnPage();
        modelFriends.setAll(friends);
        updatePageLabel();
    }

    private void updatePageLabel() {
        labelCurrentPage.setText("Page: " + (currentPage + 1));
    }

    @FXML
    public void handleNextPage(ActionEvent actionEvent) {
        Pageable pageable = new Pageable(currentPage, PAGE_SIZE);
        int totalFriends = service.findAllUserFriends(pageable, user).getTotalElementCount();
        int maxFriendsOnCurrentPage = (currentPage + 1) * PAGE_SIZE;

        if (totalFriends > maxFriendsOnCurrentPage) {
            currentPage++;
            initModelFriends();
        } else {
            MessageAlert.showErrorMessage(null, "You are on the last page.");
        }
    }

    @FXML
    public void handlePreviousPage(ActionEvent actionEvent) {
        if (currentPage > 0) {
            currentPage--;
        }
        initModelFriends();
    }

    public void update(UtilizatorEntityChangeEvent utilizatorEntityChangeEvent) {
        initModelFriends();
    }

    private void initializeTableViews() {
        tableColumnFirstNameFriend.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastNameFriend.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnEmailFriend.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnFriendsFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        tableViewFriends.setItems(modelFriends);
    }

    @FXML
    public void handleDeleteFriend() {
        FriendshipDTO friend = tableViewFriends.getSelectionModel().getSelectedItem();
        System.out.print(friend+" ");
        if (friend != null) {
            Long idFriend = service.findOneUserByNameAndEmail(friend.getFirstName(), friend.getLastName(), friend.getEmail()).getId();
            System.out.println(idFriend);
            service.removePrietenie(user.getId(), idFriend);
            update(null);


        } else {
            MessageAlert.showErrorMessage(null, "You must select a friend!");
        }
    }

}
