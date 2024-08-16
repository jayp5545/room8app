package com.ASDC.backend.service.implementation;

import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.RequestDTO.SettleUpDTORequest;
import com.ASDC.backend.dto.ResponseDTO.*;
import com.ASDC.backend.entity.*;
import com.ASDC.backend.mapper.SettleUpMapper;
import com.ASDC.backend.service.Interface.*;
import com.ASDC.backend.service.PersistentServices.SettleupPersistentService;
import com.ASDC.backend.service.StrategyFactory.ExpenseRetrievalStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettleUpServiceImpl implements SettleUpService{

    private final SettleUpMapper settleUpMapper;
    private final SettleupPersistentService settleupPersistentService;
    private final ExpenseService expenseService;
    private final UserService userService;
    private final UserRoomService userRoomService;
    private final ActivityLoggingServiceImpl activityLoggingServiceImpl;
    private final ExpenseRetrievalStrategyFactory strategyFactory;
    private final SettleupCalculationImpl settleupCalculationImpl;

    @Autowired
    public SettleUpServiceImpl(SettleUpMapper settleUpMapper, SettleupPersistentService settleupPersistentService, ExpenseService expenseService, UserService userService, UserRoomService userRoomService, ActivityLoggingServiceImpl activityLoggingServiceImpl, ExpenseRetrievalStrategyFactory strategyFactory, SettleupCalculationImpl settleupCalculationImpl) {
        this.settleupPersistentService = settleupPersistentService;
        this.settleUpMapper = settleUpMapper;
        this.expenseService = expenseService;
        this.userService = userService;
        this.userRoomService = userRoomService;
        this.activityLoggingServiceImpl = activityLoggingServiceImpl;
        this.strategyFactory = strategyFactory;
        this.settleupCalculationImpl = settleupCalculationImpl;
    }

    @Override
    public SettleUpDTOResponse createSettleUp(SettleUpDTORequest request, User user, Room room, UserRoomMapping userRoom) {
        User receiver = validateReceiver(request.getTo());
        SettleUp settleUp = saveSettleUp(receiver, request.getAmount(), user, room);
        return settleUpMapper.toSettleUpDTOResponse(settleUp);
    }

    @Override
    public SettleUpDTOResponse updateSettleUp(SettleUpDTORequest request, User user, Room room, UserRoomMapping userRoom) {
        User receiver = validateReceiver(request.getTo());
        SettleUp existingSettleUp = findSettleUpById(request.getId());
        SettleUp updatedSettleUp = updateSettleUpDetails(receiver, request.getAmount(), user, room, existingSettleUp);
        return settleUpMapper.toSettleUpDTOResponse(updatedSettleUp);
    }

    @Override
    public SettleUpDTOResponse findSettleUpById(int settleUpId, User user, Room room) {
        SettleUp settleUp = findSettleUpById(settleUpId);
        return settleUpMapper.toSettleUpDTOResponse(settleUp);
    }

    @Override
    public void deleteSettleUp(int settleUpID, User user, Room room) {
        SettleUp settleUp = findSettleUpById(settleUpID);
        deleteSettleUpDetails(user, room, settleUp);
    }

    @Override
    public List<SettleUpDTOResponse> findAllSettleUps(Room room, User user, String type) {
        List<SettleUp> settleUps = findSettleUpsByType(room, type);
        return convertToDTOResponse(settleUps);
    }

    private User validateReceiver(String email) {
        User user = userService.getUserByEmail(email);
        UserRoomMapping userRoomMapping = userRoomService.getUserRoom(user);
        return user;
    }

    private SettleUp saveSettleUp(User receiver, double amount, User sender, Room room) {
        SettleUp settleUp = new SettleUp();
        settleUp.setAmount(amount);
        settleUp.setPaid_by(sender);
        settleUp.setPaid_to(receiver);
        settleUp.setDate_of_creation(LocalDateTime.now());
        settleUp.setCreated_by(sender);
        settleUp.setLast_modified_on(LocalDateTime.now());
        settleUp.setLast_modified_by(sender);
        settleUp.setRoom(room);
        settleUp.setStatus(true);

        SettleUp savedSettleUp = settleupPersistentService.saveSettleUp(settleUp);
        activityLoggingServiceImpl.logSettleUpActivity(sender, room, savedSettleUp, Activity.ActivityType.SETTLE_UP_Add, sender.getFirstName() + " paid to '" +  receiver.getFirstName()+ "'");

        return savedSettleUp;
    }

    private SettleUp updateSettleUpDetails(User receiver, double amount, User sender, Room room, SettleUp existingSettleUp) {
        existingSettleUp.setAmount(amount);
        existingSettleUp.setPaid_by(sender);
        existingSettleUp.setPaid_to(receiver);
        existingSettleUp.setLast_modified_on(LocalDateTime.now());
        existingSettleUp.setLast_modified_by(sender);

        SettleUp updatedSettleUp = settleupPersistentService.saveSettleUp(existingSettleUp);
        activityLoggingServiceImpl.logSettleUpActivity(sender, room, updatedSettleUp, Activity.ActivityType.SETTLE_UP_EDIT, sender.getFirstName() + " paid to(edited) '" +  receiver.getFirstName()+ "'");

        return updatedSettleUp;
    }

    private void deleteSettleUpDetails(User sender, Room room, SettleUp existingSettleUp) {
        existingSettleUp.setStatus(false);
        existingSettleUp.setLast_modified_on(LocalDateTime.now());
        existingSettleUp.setLast_modified_by(sender);

        settleupPersistentService.saveSettleUp(existingSettleUp);
        activityLoggingServiceImpl.logSettleUpActivity(sender, room, existingSettleUp, Activity.ActivityType.SETTLE_UP_DELETE, sender.getFirstName() + " paid to(deleted) '" +  existingSettleUp.getPaid_to().getFirstName()+ "'");
    }

    private SettleUp findSettleUpById(int id) {
        return settleupPersistentService.getSettleUp(id);
    }

    private List<SettleUp> findSettleUpsByType(Room room, String type) {
        RetrievalStrategy<SettleUp> strategy = strategyFactory.getStrategy(type, SettleUp.class);
        return strategy.retrieveItems(room);
    }

    private List<SettleUpDTOResponse> convertToDTOResponse(List<SettleUp> settleUps) {
        List<SettleUpDTOResponse> response = new ArrayList<>();
        for (SettleUp settleUp : settleUps) {
            response.add(settleUpMapper.toSettleUpDTOResponse(settleUp));
        }
        return response;
    }

    @Override
    public List<SettleUpAmountDTOResponse> calculateSettleUps(Room room, User user) {
        List<ExpenseDTOResponse> expenseDTOResponseList = expenseService.listExpenses(room, "active");
        List<SettleUpAmountDTOProjection> settleUpList = settleupPersistentService.findSettleUpBySum(room.getId());
        return settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, settleUpList);
    }

}