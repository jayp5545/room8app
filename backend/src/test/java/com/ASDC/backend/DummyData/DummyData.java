package com.ASDC.backend.DummyData;

import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.RequestDTO.*;
import com.ASDC.backend.dto.ResponseDTO.*;
import com.ASDC.backend.dto.UserDTO;
import com.ASDC.backend.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyData {

    public User createUser() {
        return User.builder()
                .id(1L)
                .firstName("Jay Sanjaybhai")
                .lastName("Patel")
                .email("jay@gmail.com")
                .password("123456")
                .build();
    }

    public Room createRoom() {
        return Room.builder()
                .id(1)
                .name("Test Room")
                .code(5423)
                .members(1)
                .date_of_creation(LocalDateTime.now())
                .active(true)
                .build();
    }

    public GroceryList createGroceryList() {
        return GroceryList.builder()
                .id(1)
                .room(createRoom())
                .name("Test List")
                .items(0)
                .items_purchased(0)
                .last_modified_on(LocalDateTime.now())
                .last_modified_by(createUser())
                .created_by(createUser())
                .date_of_creation(LocalDateTime.now())
                .active(true)
                .grocery_items(new ArrayList<>())
                .build();
    }

    public GrocerylistDTORequest createGroceryListDTORequest() {
        return GrocerylistDTORequest.builder()
                .name("Test List")
                .build();
    }

    public UserDTOResponse createUserDTOResponse() {
        return UserDTOResponse.builder()
                .id(1L)
                .firstName("Jay Sanjaybhai")
                .lastName("Patel")
                .email("jay@gmail.com")
                .build();
    }

    public RoomDTOResponse createRoomDTOResponse() {
        return RoomDTOResponse.builder()
                .id(1)
                .name("Test Room")
                .code(5423)
                .members(1)
                .active(true)
                .build();
    }

    public GrocerylistDTOResponse createGroceryListDTOResponse() {
        return GrocerylistDTOResponse.builder()
                .room(createRoomDTOResponse())
                .name("Test List")
                .items(0)
                .items_purchased(0)
                .last_modified_on(LocalDateTime.now())
                .last_modified_by(createUserDTOResponse())
                .created_by(createUserDTOResponse())
                .date_of_creation(LocalDateTime.now())
                .active(true)
                .grocery_items(new ArrayList<>())
                .build();
    }

    public GroceryItems createGroceryItems() {
        return GroceryItems.builder()
                .id(1)
                .name("Test Item")
                .quantity(5)
                .added_on(LocalDateTime.now())
                .added_by(createUser())
                .last_modified_on(LocalDateTime.now())
                .last_modified_by(createUser())
                .purchased(false)
                .groceryList(createGroceryList())
                .build();
    }

    public GroceryItemDTORequest createGroceryItemDTORequest() {
        return GroceryItemDTORequest.builder()
                .id(1)
                .name("Test Item")
                .quantity(5)
                .groceryListName("Test List")
                .note("Test Note")
                .build();
    }

    public GroceryItemDTOResponse createGroceryItemDTOResponse() {
        return GroceryItemDTOResponse.builder()
                .id(1)
                .name("Test Item")
                .quantity(5)
                .note("Test Note")
                .added_on(LocalDateTime.now())
                .last_modified_on(LocalDateTime.now())
                .purchased(false)
                .added_by(createUserDTOResponse())
                .last_modified_by(createUserDTOResponse())
                .groceryListName("Test List")
                .build();
    }

    public UserRoomMapping createUserRoomMapping() {
        UserRoomMapping userRoomMapping = UserRoomMapping.builder()
                .id(1)
                .userid(createUser())
                .roomid(createRoom())
                .join_date(LocalDateTime.now())
                .remove_date(LocalDateTime.now())
                .build();
        return userRoomMapping;
    }

    public Expense createExpense() {
        Expense expense = Expense.builder()
                .id(1)
                .description("Test Expense")
                .amount(100.0)
                .paid_by(createUser())
                .date_of_creation(LocalDateTime.now())
                .last_modified_on(LocalDateTime.now())
                .created_by(createUser())
                .last_modified_by(createUser())
                .room(createRoom())
                .status(true)
                .participants(Arrays.asList(createParticipant(), createParticipant()))
                .build();
        return expense;
    }

    public ExpenseDTORequest createExpenseDTORequest() {
        ExpenseDTORequest expenseDTORequest = ExpenseDTORequest.builder()
                .description("Test Expense")
                .amount(100.0)
                .paidBy("test@test.com")
                .participantEmails(List.of("participant_1@gmail.com", "participant_2@gmail.com"))
                .build();
        return expenseDTORequest;
    }

    public ExpenseDTOResponse createExpenseDTOResponse() {
        ExpenseDTOResponse expenseDTOResponse = ExpenseDTOResponse.builder()
                .id(1)
                .description("Test Expense")
                .amount(100.0)
                .paidBy(createUserDTOResponse())
                .dateOfCreation(LocalDateTime.now())
                .lastModifiedOn(LocalDateTime.now())
                .createdBy(createUserDTOResponse())
                .lastModifiedBy(createUserDTOResponse())
                .room(createRoomDTOResponse())
                .status(true)
                .participants(List.of(createParticipantDTOResponse(), createParticipantDTOResponse()))
                .build();
        return expenseDTOResponse;
    }

    public Participant createParticipant() {
        Participant participant = Participant.builder()
                .amount(100)
                .user_id(createUser())
                .build();
        return participant;
    }

    public ParticipantDTOResponse createParticipantDTOResponse() {
        ParticipantDTOResponse participantDTOResponse = ParticipantDTOResponse.builder()
                .amount(100)
                .user(createUserDTOResponse())
                .id(1)
                .build();
        return participantDTOResponse;
    }

    public Activity createActivity() {
        return Activity.builder()
                .id(1)
                .room(createRoom())
                .settleUp(null)
                .expense(null)
                .date(LocalDateTime.now())
                .build();
    }

    public SettleUp createSettleUp() {
        return SettleUp.builder()
                .id(1)
                .amount(100.0)
                .paid_by(createUser())
                .paid_to(createUser())
                .date_of_creation(LocalDateTime.now())
                .created_by(createUser())
                .last_modified_on(LocalDateTime.now())
                .last_modified_by(createUser())
                .room(createRoom())
                .status(true)
                .build();
    }

    public SettleUpDTORequest createSettleUpDTORequest() {
        return SettleUpDTORequest.builder()
                .id(1)
                .to("receiver@gmail.com")
                .amount(100.0)
                .build();
    }

    public SettleUpDTOResponse createSettleUpDTOResponse() {
        return SettleUpDTOResponse.builder()
                .id(1)
                .amount(100.0)
                .paidBy(createUserDTOResponse())
                .paidTo(createUserDTOResponse())
                .dateOfCreation(LocalDateTime.now())
                .lastModifiedOn(LocalDateTime.now())
                .createdBy(createUserDTOResponse())
                .lastModifiedBy(createUserDTOResponse())
                .room(createRoomDTOResponse())
                .status(true)
                .build();
    }

    public RoomDTORequest createRoomDTORequest() {
        RoomDTORequest roomDTORequest = RoomDTORequest.builder()
                .id(1)
                .name("Conference Room")
                .build();
        return roomDTORequest;
    }

    public JoinRoomDTORequest createJoinRoomDTORequest() {
        JoinRoomDTORequest joinRoomDTORequest = JoinRoomDTORequest.builder()
                .joinCode(123456)
                .build();
        return joinRoomDTORequest;
    }

    public JoinRoomDTOResponse createJoinRoomDTOResponse() {
        JoinRoomDTOResponse joinRoomDTOResponse = JoinRoomDTOResponse.builder()
                .userDTOResponse(createUserDTOResponse())
                .join_date(LocalDateTime.now())
                .roomDTOResponse(createRoomDTOResponse())
                .build();
        return joinRoomDTOResponse;
    }

    public ProfileDTOResponse createProfileDTOResponse() {
        ProfileDTOResponse profileDTOResponse = ProfileDTOResponse.builder()
                .userDTOResponse(createUserDTOResponse())
                .roomDTOResponse(createRoomDTOResponse())
                .joinDate(LocalDateTime.now())
                .build();
        return profileDTOResponse;
    }

    public ProfileDTORequest createProfileDTORequest() {
        ProfileDTORequest profileDTORequest = ProfileDTORequest.builder()
                .email("test@example.com")
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .build();
        return profileDTORequest;
    }

    public UserDTO createUserDTO() {
        UserDTO userDTO = UserDTO.builder()
                .email("testing@gmail.com")
                .password("demoPass")
                .build();
        return userDTO;
    }

    public User createUserForUserService() {
        User user = User.builder()
                .id(1L)
                .email("testing@gmail.com")
                .password("demoPass")
                .build();
        return user;
    }

    public Task createTask() {
        return Task.builder()
                .id(1)
                .room(createRoom())
                .name("Test Task")
                .created_by(createUser())
                .last_modified_by(createUser())
                .date_of_creation(LocalDateTime.now())
                .last_modified_on(LocalDateTime.now())
                .taskDate(LocalDate.now())
                .description("Test Description")
                .assigned_to(createUser())
                .finished(false)
                .build();
    }
    public TaskDTOResponse createTaskDTO() {
        return TaskDTOResponse.builder()
                .id(1)
                .name("Test Task")
                .date_of_creation(LocalDateTime.now())
                .last_modified_On(LocalDateTime.now())
                .taskDate(LocalDate.now())
                .description("Test Description")
                .finished(false)
                .build();

    }

    public TaskDTORequest createTaskRequestDTO() {
        return TaskDTORequest.builder()
                .name("Test Task")
                .description("Test Description")
                .taskDate(LocalDate.now())
                .assignedTO("Jay Patel")
                .build();
    }

    public Announcement createAnnouncement(User user,Room room) {

        Announcement announcement = Announcement.builder()
                .id(1L)
                .title("Test Announcement")
                .description("This is a test announcement")
                .userPosted(user)
                .room(room)
                .postedDateTime(LocalDateTime.now())
                .build();
        return announcement;
    }
    public AnnouncementRequestDTO createAnnouncementRequestDTO() {

        AnnouncementRequestDTO announcementRequestDTO = AnnouncementRequestDTO.builder()
                .title("Test Announcement")
                .description("This is a test announcement")
                .build();
        return announcementRequestDTO;
    }
    public AnnouncementDTOResponse createAnnouncementDTOResponse() {
        AnnouncementDTOResponse announcementDTOResponse = AnnouncementDTOResponse.builder()
                .id(1L)
                .title("Test Announcement")
                .description("This is a test announcement")
                .build();
        return announcementDTOResponse;
    }
}