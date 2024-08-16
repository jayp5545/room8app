package com.ASDC.backend.Util;

import com.ASDC.backend.RequestDTO.TaskRequestDTO;
import com.ASDC.backend.dto.RequestDTO.ExpenseDTORequest;
import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.*;
import com.ASDC.backend.dto.TaskDTO;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.repository.ActivityRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public User createUser(){

        User user = new User();
        user.setId(1L);
        user.setFirstName("Jay Sanjaybhai");
        user.setLastName("Patel");
        user.setEmail("jay@gmail.com");
        user.setPassword("123456");

        return user;
    }

    public UserDTOResponse createUserDTOResponse(){
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setId(1L);
        userDTOResponse.setFirstName("Jay Sanjaybhai");
        userDTOResponse.setLastName("Patel");
        userDTOResponse.setEmail("jay@gmail.com");

        return userDTOResponse;
    }

    public Room createRoom(){

        Room room = new Room();
        room.setId(1);
        room.setName("Test Room");
        room.setCode(5423);
        room.setMembers(1);
        room.setDate_of_creation(LocalDateTime.now());
        room.setActive(true);

        return room;
    }

    public RoomDTOResponse createRoomDTOResponse(){
        RoomDTOResponse roomDTOResponse = new RoomDTOResponse();
        roomDTOResponse.setId(1);
        roomDTOResponse.setName("Test Room");
        roomDTOResponse.setCode(5423);
        roomDTOResponse.setMembers(1);
        roomDTOResponse.setActive(true);

        return roomDTOResponse;
    }

    public UserRoomMapping createUserRoomMapping(){

        UserRoomMapping userRoomMapping = new UserRoomMapping();

        userRoomMapping = new UserRoomMapping();
        userRoomMapping.setId(1);
        userRoomMapping.setUserid(createUser());
        userRoomMapping.setRoomid(createRoom());
        userRoomMapping.setJoin_date(LocalDateTime.now());
        userRoomMapping.setRemove_date(LocalDateTime.now());

        return userRoomMapping;
    }



    public GroceryList createGroceryList(){

        GroceryList groceryList = new GroceryList();

        groceryList = new GroceryList();
        groceryList.setId(1);
        groceryList.setRoom(createRoom());
        groceryList.setName("Test List");
        groceryList.setItems(0);
        groceryList.setItems_purchased(0);
        groceryList.setLast_modified_on(LocalDateTime.now());
        groceryList.setLast_modified_by(createUser());
        groceryList.setCreated_by(createUser());
        groceryList.setDate_of_creation(LocalDateTime.now());
        groceryList.setActive(true);
        groceryList.setGrocery_items(new ArrayList<>());

        return groceryList;
    }

    public GroceryListDTO createGroceryListDTO(){

        GroceryListDTO groceryListDTO = new GroceryListDTO();

        groceryListDTO = new GroceryListDTO();
        groceryListDTO.setName("Test List");
        groceryListDTO.setItems(0);
        groceryListDTO.setItems_purchased(0);
        groceryListDTO.setLast_modified_on(LocalDateTime.now());
        groceryListDTO.setDate_of_creation(LocalDateTime.now());
        groceryListDTO.setActive(true);
        groceryListDTO.setGrocery_items(new ArrayList<>());


        return groceryListDTO;
    }

    public GroceryItems createGroceryItems(){

        GroceryItems groceryItems = new GroceryItems();

        groceryItems = new GroceryItems();
        groceryItems.setId(1);
        groceryItems.setName("Test Item");
        groceryItems.setQuantity(5);
        groceryItems.setAdded_on(LocalDateTime.now());
        groceryItems.setAdded_by(createUser());
        groceryItems.setLast_modified_on(LocalDateTime.now());
        groceryItems.setLast_modified_by(createUser());
        groceryItems.setPurchased(false);
        groceryItems.setGroceryList(createGroceryList());

        return  groceryItems;
    }

    public GroceryItemDTO createGroceryItemDTO(){

        GroceryItemDTO groceryItemDTO = new GroceryItemDTO();

        groceryItemDTO = new GroceryItemDTO();
        groceryItemDTO.setId(1);
        groceryItemDTO.setName("Test Item");
        groceryItemDTO.setQuantity(5);
        groceryItemDTO.setNote("");
        groceryItemDTO.setAdded_on(LocalDateTime.now());
        groceryItemDTO.setLast_modified_on(LocalDateTime.now());
        groceryItemDTO.setPurchased(false);
        groceryItemDTO.setGroceryListName("Test List");

        return groceryItemDTO;
    }

    public Task createTask(){

        Task task = new Task();
        task.setId(1);
        task.setRoom(createRoom());
        task.setName("Test Task");
        task.setCreated_by(createUser());
        task.setLast_modified_by(createUser());
        task.setDate_of_creation(LocalDateTime.now());
        task.setLast_modified_on(LocalDateTime.now());
        task.setTaskDate(LocalDate.now());
        task.setDescription("Test Description");
        task.setAssigned_to(createUser());
        task.setFinished(false);

        return task;
    }

    public TaskDTO createTaskDTO(){

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setName("Test Task");
        taskDTO.setDate_of_creation(LocalDateTime.now());
        taskDTO.setLast_modified_On(LocalDateTime.now());
        taskDTO.setTaskDate(LocalDate.now());
        taskDTO.setDescription("Test Description");
        taskDTO.setFinished(false);

        return taskDTO;
    }


    public TaskRequestDTO createTaskRequestDTO(){

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setName("Test Task");
        taskRequestDTO.setDescription("Test Description");
        taskRequestDTO.setTaskDate(LocalDate.now());
        taskRequestDTO.setAssignedTO("Jay Patel");

        return taskRequestDTO;
    }


    public ExpenseDTORequest createExpenseDTORequest() {
        ExpenseDTORequest expenseDTORequest = new ExpenseDTORequest();
        expenseDTORequest.setDescription("Test Expense");
        expenseDTORequest.setAmount(100.0);
        expenseDTORequest.setPaid_by("test@test.com");

        List<String> participants = new ArrayList<>();
        participants.add("participant_1@gmail.com");
        participants.add("participant_2@gmail.com");

        expenseDTORequest.setParticipantEmails(participants);
        return expenseDTORequest;
    }

    public Expense createExpense() {
        Expense expense = new Expense();
        expense.setId(1);
        expense.setDescription("Test Expense");
        expense.setAmount(100.0);
        expense.setPaid_by(createUser());
        expense.setDate_of_creation(LocalDateTime.now());
        expense.setLast_modified_on(LocalDateTime.now());
        expense.setCreated_by(createUser());
        expense.setLast_modified_by(createUser());
        expense.setRoom(createRoom());
        expense.setStatus(true);

        List<Participant> participants = new ArrayList<>();
        Participant participant_1 = createParticipant();
        Participant participant_2 = createParticipant();
        participants.add(participant_1);
        participants.add(participant_2);
        expense.setParticipants(participants);

        return expense;
    }

    public ExpenseDTOResponse createExpenseDTOResponse(){
        ExpenseDTOResponse expenseDTOResponse = new ExpenseDTOResponse();
        expenseDTOResponse.setId(1);
        expenseDTOResponse.setDescription("Test Expense");
        expenseDTOResponse.setAmount(100.0);
        expenseDTOResponse.setPaid_by(createUserDTOResponse());
        expenseDTOResponse.setDate_of_creation(LocalDateTime.now());
        expenseDTOResponse.setLast_modified_on(LocalDateTime.now());
        expenseDTOResponse.setCreated_by(createUserDTOResponse());
        expenseDTOResponse.setLast_modified_by(createUserDTOResponse());
        expenseDTOResponse.setRoom(createRoomDTOResponse());
        expenseDTOResponse.setStatus(true);

        List<ParticipantDTOResponse> participants = new ArrayList<>();
        ParticipantDTOResponse participant_1 = createParticipantDTOResponse();
        ParticipantDTOResponse participant_2 = createParticipantDTOResponse();
        expenseDTOResponse.setParticipants(participants);

        return expenseDTOResponse;
    }

    public Participant createParticipant(){
        Participant participant = new Participant();
        participant.setAmount(100);
        participant.setUser_id(createUser());

        return participant;
    }

    public ParticipantDTOResponse createParticipantDTOResponse(){
        ParticipantDTOResponse participantDTOResponse = new ParticipantDTOResponse();
        participantDTOResponse.setAmount(100);
        participantDTOResponse.setUser(createUserDTOResponse());
        participantDTOResponse.setId(1);

        return participantDTOResponse;
    }

    public Activity createActivity(){
        Activity activity = new Activity();
        activity.setId(1);
        activity.setRoom(createRoom());
        activity.setSettleUp(null);
        activity.setExpense(null);
        activity.setDate(LocalDateTime.now());
        return activity;
    }

    public SettleUp createSettleUp(){
        SettleUp settleUp = new SettleUp();
        settleUp.setId(1);
        settleUp.setPaid_to(createUser());
        settleUp.setCreated_by(createUser());
        settleUp.setRoom(createRoom());
        settleUp.setStatus(true);
        settleUp.setAmount(100);
        settleUp.setLast_modified_by(createUser());
        settleUp.setLast_modified_on(LocalDateTime.now());
        settleUp.setDate_of_creation(LocalDateTime.now());
        return settleUp;
    }

    public SettleUpDTOResponse createSettleUpDTOResponse(){
        SettleUpDTOResponse settleUpDTOResponse = new SettleUpDTOResponse();
        settleUpDTOResponse.setId(1);
        settleUpDTOResponse.setPaid_to(createUserDTOResponse());
        settleUpDTOResponse.setCreated_by(createUserDTOResponse());
        settleUpDTOResponse.setRoom(createRoomDTOResponse());
        settleUpDTOResponse.setStatus(true);
        settleUpDTOResponse.setAmount(100);
        settleUpDTOResponse.setLast_modified_by(createUserDTOResponse());
        settleUpDTOResponse.setLast_modified_on(LocalDateTime.now());
        settleUpDTOResponse.setDate_of_creation(LocalDateTime.now());
        settleUpDTOResponse.setPaid_by(createUserDTOResponse());
        return settleUpDTOResponse;
    }

    public SettleUpAmountDTOResponse createSettleUpAmountDTOResponse(){
        SettleUpAmountDTOResponse settleUpAmountDTOResponse = new SettleUpAmountDTOResponse();
        settleUpAmountDTOResponse.setFrom(createUserDTOResponse());
        settleUpAmountDTOResponse.setTo(createUserDTOResponse());
        settleUpAmountDTOResponse.setAmount(100);
        settleUpAmountDTOResponse.setStatus(true);
        return settleUpAmountDTOResponse;
    }

    public SettleUpDTORequest createSettleUpDTORequest(){
        SettleUpDTORequest settleUpDTORequest = new SettleUpDTORequest();
        settleUpDTORequest.setId(1);
        settleUpDTORequest.setAmount(100);
        settleUpDTORequest.setTo("jay@gmail.com");
        return settleUpDTORequest;
    }

    public ActivityExpenseDTOResponse createActivityExpenseDTOResponse(){
        ActivityExpenseDTOResponse activityExpenseDTOResponse = new ActivityExpenseDTOResponse();
        activityExpenseDTOResponse.setId(1);
        activityExpenseDTOResponse.setRoom(createRoomDTOResponse());
        activityExpenseDTOResponse.setExpense(createExpenseDTOResponse());
        activityExpenseDTOResponse.setDate(LocalDateTime.now());
        activityExpenseDTOResponse.setType(Activity.ActivityType.Expense_ADD);
        activityExpenseDTOResponse.setDetails("Jay added 'Groceries'");
        return activityExpenseDTOResponse;
    }

    public ActivitySettleUpDTOResponse createActivitySettleUpDTOResponse(){
        ActivitySettleUpDTOResponse activitySettleUpDTOResponse = new ActivitySettleUpDTOResponse();
        activitySettleUpDTOResponse.setId(1);
        activitySettleUpDTOResponse.setRoom(createRoomDTOResponse());
        activitySettleUpDTOResponse.setSettleup(createSettleUpDTOResponse());
        activitySettleUpDTOResponse.setDate(LocalDateTime.now());
        return activitySettleUpDTOResponse;
    }

    public ActivityDTOResponse createActivityDTOResponse(){
        ActivityDTOResponse activityDTOResponse = new ActivityDTOResponse();
        activityDTOResponse.setId(1);
        activityDTOResponse.setRoom(createRoomDTOResponse());
        activityDTOResponse.setExpense(createExpenseDTOResponse());
        activityDTOResponse.setSettleUp(createSettleUpDTOResponse());
        activityDTOResponse.setDate(LocalDateTime.now());
        return activityDTOResponse;
    }
}
