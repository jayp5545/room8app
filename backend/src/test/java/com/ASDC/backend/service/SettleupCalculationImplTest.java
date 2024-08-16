package com.ASDC.backend.service;

import com.ASDC.backend.DummyData.DummyData;
import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ParticipantDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.UserDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.service.implementation.SettleupCalculationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SettleupCalculationImplTest {

    @InjectMocks
    private SettleupCalculationImpl settleupCalculationImpl;
    private DummyData dummyData;
    private Room room;
    private User user;


    @BeforeEach
    void setUp() {
        dummyData = new DummyData();
        setDummyData();
    }

    private void setDummyData() {
        user = dummyData.createUser();
        room = dummyData.createRoom();
    }

    /*@Test
    public void testGetAllSettleUpAmount() {

        List<ParticipantDTOResponse> participants = new ArrayList<>();
        ParticipantDTOResponse participant_1 = dummyData.createParticipantDTOResponse();
        ParticipantDTOResponse participant_2 = dummyData.createParticipantDTOResponse();
        participants.add(participant_1);
        participants.add(participant_2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expenseDTOResponse_1 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_1.setStatus(true);
        expenseDTOResponse_1.setParticipants(participants);
        ExpenseDTOResponse expenseDTOResponse_2 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_2.setStatus(true);
        expenseDTOResponse_2.setParticipants(participants);
        expenseDTOResponseList.add(expenseDTOResponse_1);
        expenseDTOResponseList.add(expenseDTOResponse_2);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, new ArrayList<>());

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(100, result.get(0).getAmount());
        assertEquals(expenseDTOResponseList.get(0).getPaidBy().getFirstName(), result.get(0).getFrom().getFirstName());
    }*/

    @Test
    public void GetAllSettleUpAmount_EquallyCancelOut() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        sender.setFirstName("Sender");
        sender.setEmail("sender@gmail.com");

        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);
        receiver.setFirstName("Receiver");
        receiver.setEmail("receiver@gmail.com");

        List<ParticipantDTOResponse> participantsList_1 = new ArrayList<>();
        ParticipantDTOResponse participant_1 = dummyData.createParticipantDTOResponse();
        participant_1.setId(1);
        participant_1.setAmount(100);
        participant_1.setUser(receiver);
        participantsList_1.add(participant_1);

        List<ParticipantDTOResponse> participantsList_2 = new ArrayList<>();
        ParticipantDTOResponse participant_2 = dummyData.createParticipantDTOResponse();
        participant_2.setAmount(100);
        participant_2.setUser(sender);
        participantsList_2.add(participant_2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expenseDTOResponse_1 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_1.setStatus(true);
        expenseDTOResponse_1.setParticipants(participantsList_1);
        expenseDTOResponse_1.setPaidBy(sender);
        ExpenseDTOResponse expenseDTOResponse_2 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_2.setStatus(true);
        expenseDTOResponse_2.setParticipants(participantsList_2);
        expenseDTOResponse_2.setPaidBy(receiver);
        expenseDTOResponseList.add(expenseDTOResponse_1);
        expenseDTOResponseList.add(expenseDTOResponse_2);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, new ArrayList<>());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllSettleUpAmount_Positive() {
        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);

        List<ParticipantDTOResponse> participantsList1 = new ArrayList<>();
        ParticipantDTOResponse participant1 = dummyData.createParticipantDTOResponse();
        participant1.setId(1);
        participant1.setAmount(1000);
        participant1.setUser(receiver);
        participantsList1.add(participant1);

        List<ParticipantDTOResponse> participantsList2 = new ArrayList<>();
        ParticipantDTOResponse participant2 = dummyData.createParticipantDTOResponse();
        participant2.setAmount(100);
        participant2.setUser(sender);
        participantsList2.add(participant2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expense1 = dummyData.createExpenseDTOResponse();
        expense1.setStatus(true);
        expense1.setParticipants(participantsList1);
        expense1.setPaidBy(sender);
        ExpenseDTOResponse expense2 = dummyData.createExpenseDTOResponse();
        expense2.setStatus(true);
        expense2.setParticipants(participantsList2);
        expense2.setPaidBy(receiver);
        expenseDTOResponseList.add(expense1);
        expenseDTOResponseList.add(expense2);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, new ArrayList<>());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(900, result.get(0).getAmount());
        assertEquals(expense1.getPaidBy(), result.get(0).getTo());
    }

    @Test
    public void testGetAllSettleUpAmount_Negative() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);

        List<ParticipantDTOResponse> participantsList1 = new ArrayList<>();
        ParticipantDTOResponse participant1 = dummyData.createParticipantDTOResponse();
        participant1.setId(1);
        participant1.setAmount(100);
        participant1.setUser(receiver);
        participantsList1.add(participant1);

        List<ParticipantDTOResponse> participantsList2 = new ArrayList<>();
        ParticipantDTOResponse participant2 = dummyData.createParticipantDTOResponse();
        participant2.setAmount(1000);
        participant2.setUser(sender);
        participantsList2.add(participant2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expense1 = dummyData.createExpenseDTOResponse();
        expense1.setStatus(true);
        expense1.setParticipants(participantsList1);
        expense1.setPaidBy(sender);
        ExpenseDTOResponse expense2 = dummyData.createExpenseDTOResponse();
        expense2.setStatus(true);
        expense2.setParticipants(participantsList2);
        expense2.setPaidBy(receiver);
        expenseDTOResponseList.add(expense1);
        expenseDTOResponseList.add(expense2);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, new ArrayList<>());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(900, result.get(0).getAmount());
        assertEquals(expense1.getPaidBy(), result.get(0).getFrom());
    }

    @Test
    public void testGetAllSettleUpAmount_AlreadySettleUpExist_Positive() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);

        List<ParticipantDTOResponse> participantsList1 = new ArrayList<>();
        ParticipantDTOResponse participant1 = dummyData.createParticipantDTOResponse();
        participant1.setId(1);
        participant1.setAmount(100);
        participant1.setUser(receiver);
        participantsList1.add(participant1);

        List<ParticipantDTOResponse> participantsList2 = new ArrayList<>();
        ParticipantDTOResponse participant2 = dummyData.createParticipantDTOResponse();
        participant2.setAmount(1000);
        participant2.setUser(sender);
        participantsList2.add(participant2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expense1 = dummyData.createExpenseDTOResponse();
        expense1.setStatus(true);
        expense1.setParticipants(participantsList1);
        expense1.setPaidBy(sender);
        ExpenseDTOResponse expense2 = dummyData.createExpenseDTOResponse();
        expense2.setStatus(true);
        expense2.setParticipants(participantsList2);
        expense2.setPaidBy(receiver);
        expenseDTOResponseList.add(expense1);
        expenseDTOResponseList.add(expense2);

        List<SettleUpAmountDTOProjection> settleUpAmountDTOProjectionList = new ArrayList<>();
        SettleUpAmountDTOProjection settleUpAmountDTOProjection = new SettleUpAmountDTOProjection() {
            @Override
            public Integer getPaidBy() {
                return Integer.parseInt(String.valueOf(sender.getId()));
            }

            @Override
            public Integer getPaidTo() {
                return Integer.parseInt(String.valueOf(receiver.getId()));
            }

            @Override
            public Double getAmount() {
                return 500.0;
            }

            @Override
            public Boolean getStatus() {
                return true;
            }
        };
        settleUpAmountDTOProjectionList.add(settleUpAmountDTOProjection);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList,settleUpAmountDTOProjectionList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(400, result.get(0).getAmount());
    }

    @Test
    public void testGetAllSettleUpAmount_AlreadySettleUpExist_Negative() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);

        List<ParticipantDTOResponse> participantsList1 = new ArrayList<>();
        ParticipantDTOResponse participant1 = dummyData.createParticipantDTOResponse();
        participant1.setId(1);
        participant1.setAmount(1000);
        participant1.setUser(receiver);
        participantsList1.add(participant1);

        List<ParticipantDTOResponse> participantsList2 = new ArrayList<>();
        ParticipantDTOResponse participant2 = dummyData.createParticipantDTOResponse();
        participant2.setAmount(100);
        participant2.setUser(sender);
        participantsList2.add(participant2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expense1 = dummyData.createExpenseDTOResponse();
        expense1.setStatus(true);
        expense1.setParticipants(participantsList1);
        expense1.setPaidBy(sender);
        ExpenseDTOResponse expense2 = dummyData.createExpenseDTOResponse();
        expense2.setStatus(true);
        expense2.setParticipants(participantsList2);
        expense2.setPaidBy(receiver);
        expenseDTOResponseList.add(expense1);
        expenseDTOResponseList.add(expense2);

        List<SettleUpAmountDTOProjection> settleUpAmountDTOProjectionList = new ArrayList<>();
        SettleUpAmountDTOProjection settleUpAmountDTOProjection = new SettleUpAmountDTOProjection() {
            @Override
            public Integer getPaidBy() {
                return Integer.parseInt(String.valueOf(receiver.getId()));
            }

            @Override
            public Integer getPaidTo() {
                return Integer.parseInt(String.valueOf(sender.getId()));
            }

            @Override
            public Double getAmount() {
                return 1500.0;
            }

            @Override
            public Boolean getStatus() {
                return true;
            }
        };
        settleUpAmountDTOProjectionList.add(settleUpAmountDTOProjection);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, settleUpAmountDTOProjectionList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(600, result.get(0).getAmount());
    }

    @Test
    public void GetAllSettleUpAmount_AlreadySettleUpExist_Equalize() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        sender.setFirstName("Sender");
        sender.setEmail("sender@gmail.com");

        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);
        receiver.setFirstName("Receiver");
        receiver.setEmail("receiver@gmail.com");

        List<ParticipantDTOResponse> participantsList_1 = new ArrayList<>();
        ParticipantDTOResponse participant_1 = dummyData.createParticipantDTOResponse();
        participant_1.setId(1);
        participant_1.setAmount(100);
        participant_1.setUser(receiver);
        participantsList_1.add(participant_1);

        List<ParticipantDTOResponse> participantsList_2 = new ArrayList<>();
        ParticipantDTOResponse participant_2 = dummyData.createParticipantDTOResponse();
        participant_2.setAmount(1000);
        participant_2.setUser(sender);
        participantsList_2.add(participant_2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expenseDTOResponse_1 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_1.setStatus(true);
        expenseDTOResponse_1.setParticipants(participantsList_1);
        expenseDTOResponse_1.setPaidBy(sender);
        ExpenseDTOResponse expenseDTOResponse_2 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_2.setStatus(true);
        expenseDTOResponse_2.setParticipants(participantsList_2);
        expenseDTOResponse_2.setPaidBy(receiver);
        expenseDTOResponseList.add(expenseDTOResponse_1);
        expenseDTOResponseList.add(expenseDTOResponse_2);

        List<SettleUpAmountDTOProjection> settleUpAmountDTOProjectionList = new ArrayList<>();

        SettleUpAmountDTOProjection settleUpAmountDTOProjection = new SettleUpAmountDTOProjection() {
            @Override
            public Integer getPaidBy() {
                return Integer.parseInt(String.valueOf(sender.getId()));
            }

            @Override
            public Integer getPaidTo() {
                return Integer.parseInt(String.valueOf(receiver.getId()));
            }

            @Override
            public Double getAmount() {
                return 900.0;
            }

            @Override
            public Boolean getStatus() {
                return true;
            }
        };

        settleUpAmountDTOProjectionList.add(settleUpAmountDTOProjection);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, settleUpAmountDTOProjectionList);

        assertNotNull(result);
        assertEquals(0, result.size());
    }


    @Test
    public void GetAllSettleUpAmount_AlreadySettleUpExist_NotMatchWithAnyOne() {

        UserDTOResponse sender = dummyData.createUserDTOResponse();
        sender.setId(1L);
        sender.setFirstName("Sender");
        sender.setEmail("sender@gmail.com");

        UserDTOResponse receiver = dummyData.createUserDTOResponse();
        receiver.setId(2L);
        receiver.setFirstName("Receiver");
        receiver.setEmail("receiver@gmail.com");

        List<ParticipantDTOResponse> participantsList_1 = new ArrayList<>();
        ParticipantDTOResponse participant_1 = dummyData.createParticipantDTOResponse();
        participant_1.setId(1);
        participant_1.setAmount(100);
        participant_1.setUser(receiver);
        participantsList_1.add(participant_1);

        List<ParticipantDTOResponse> participantsList_2 = new ArrayList<>();
        ParticipantDTOResponse participant_2 = dummyData.createParticipantDTOResponse();
        participant_2.setAmount(1000);
        participant_2.setUser(sender);
        participantsList_2.add(participant_2);

        List<ExpenseDTOResponse> expenseDTOResponseList = new ArrayList<>();
        ExpenseDTOResponse expenseDTOResponse_1 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_1.setStatus(true);
        expenseDTOResponse_1.setParticipants(participantsList_1);
        expenseDTOResponse_1.setPaidBy(sender);
        ExpenseDTOResponse expenseDTOResponse_2 = dummyData.createExpenseDTOResponse();
        expenseDTOResponse_2.setStatus(true);
        expenseDTOResponse_2.setParticipants(participantsList_2);
        expenseDTOResponse_2.setPaidBy(receiver);
        expenseDTOResponseList.add(expenseDTOResponse_1);
        expenseDTOResponseList.add(expenseDTOResponse_2);

        List<SettleUpAmountDTOProjection> settleUpAmountDTOProjectionList = new ArrayList<>();

        user.setId(5L);
        SettleUpAmountDTOProjection settleUpAmountDTOProjection = new SettleUpAmountDTOProjection() {
            @Override
            public Integer getPaidBy() {
                return Integer.parseInt(String.valueOf(user.getId()));
            }

            @Override
            public Integer getPaidTo() {
                return Integer.parseInt(String.valueOf(receiver.getId()));
            }

            @Override
            public Double getAmount() {
                return 2000.0;
            }

            @Override
            public Boolean getStatus() {
                return true;
            }
        };

        settleUpAmountDTOProjectionList.add(settleUpAmountDTOProjection);

        List<SettleUpAmountDTOResponse> result = settleupCalculationImpl.calculate(room, user, expenseDTOResponseList, settleUpAmountDTOProjectionList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(900, result.get(0).getAmount());
        assertEquals(true, result.get(0).isStatus());
    }
}