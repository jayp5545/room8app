package com.ASDC.backend.service.implementation;

import com.ASDC.backend.Models.SettleUpAmountDTOProjection;
import com.ASDC.backend.dto.ResponseDTO.ExpenseDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.ParticipantDTOResponse;
import com.ASDC.backend.dto.ResponseDTO.SettleUpAmountDTOResponse;
import com.ASDC.backend.entity.Room;
import com.ASDC.backend.entity.User;
import com.ASDC.backend.service.Interface.SettleupCalculation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettleupCalculationImpl implements SettleupCalculation {

    @Override
    public List<SettleUpAmountDTOResponse> calculate(Room room, User user, List<ExpenseDTOResponse> expenseDTOResponseList, List<SettleUpAmountDTOProjection> settleUpList) {
        List<SettleUpAmountDTOResponse> initialSettleUps = createInitialSettleUpAmount(room, user, expenseDTOResponseList);
        List<SettleUpAmountDTOResponse> aggregatedSettleUps = aggregateSettleUpAmounts(initialSettleUps);
        List<SettleUpAmountDTOResponse> finalResponse = finalizeSettleUpAmounts(aggregatedSettleUps);
        return adjustFinalAmounts(finalResponse, room, settleUpList);
    }


    private List<SettleUpAmountDTOResponse> createInitialSettleUpAmount(Room room, User user, List<ExpenseDTOResponse> expenseDTOResponseList){
        List<SettleUpAmountDTOResponse> settleupAmounts = new ArrayList<>();

        for (ExpenseDTOResponse expenseDTOResponse : expenseDTOResponseList){
            for (ParticipantDTOResponse currParticipant : expenseDTOResponse.getParticipants()){
                SettleUpAmountDTOResponse settleUpAmountDTOResponse = new SettleUpAmountDTOResponse();
                settleUpAmountDTOResponse.setTo(expenseDTOResponse.getPaidBy());
                settleUpAmountDTOResponse.setFrom(currParticipant.getUser());
                settleUpAmountDTOResponse.setAmount(currParticipant.getAmount());
                settleUpAmountDTOResponse.setStatus(true);
                settleupAmounts.add(settleUpAmountDTOResponse);
            }
        }

        return settleupAmounts;
    }


    private List<SettleUpAmountDTOResponse> aggregateSettleUpAmounts(List<SettleUpAmountDTOResponse> settleUpAmountList){
        List<SettleUpAmountDTOResponse> createAggregateSettleUp = new ArrayList<>();

        List<Integer> index = new ArrayList<>();

        for (int i=0;i<settleUpAmountList.size();i++){
            if (index.contains(i)){
                continue;
            }
            SettleUpAmountDTOResponse settleUpAmountDTOResponse = settleUpAmountList.get(i);

            for (int j=i+1;j<settleUpAmountList.size();j++){
                if (settleUpAmountDTOResponse.getFrom().getId().equals(settleUpAmountList.get(j).getFrom().getId()) && settleUpAmountDTOResponse.getTo().getId().equals(settleUpAmountList.get(j).getTo().getId())){
                    index.add(j);
                    SettleUpAmountDTOResponse currSettleUp = settleUpAmountList.get(j);
                    double amount = currSettleUp.getAmount() + settleUpAmountDTOResponse.getAmount();
                    settleUpAmountDTOResponse.setAmount(amount);
                }
            }
            System.out.println(settleUpAmountDTOResponse);
            createAggregateSettleUp.add(settleUpAmountDTOResponse);
        }

        return createAggregateSettleUp;
    }



    private List<SettleUpAmountDTOResponse> finalizeSettleUpAmounts(List<SettleUpAmountDTOResponse> aggregatedAmountList){
        List<SettleUpAmountDTOResponse> finalResponse = new ArrayList<>();

        List<Integer> index = new ArrayList<>();
        for (int i=0;i<aggregatedAmountList.size();i++){
            if (index.contains(i)){
                continue;
            }
            SettleUpAmountDTOResponse settleUpAmountDTOResponse = aggregatedAmountList.get(i);

            boolean isMatchFound = false;
            for (int j=i+1;j<aggregatedAmountList.size();j++){
                if (settleUpAmountDTOResponse.getFrom().getId().equals(aggregatedAmountList.get(j).getTo().getId()) && settleUpAmountDTOResponse.getTo().getId().equals(aggregatedAmountList.get(j).getFrom().getId())){
                    isMatchFound = true;

                    index.add(j);

                    SettleUpAmountDTOResponse currSettleUp = aggregatedAmountList.get(j);
                    double amount = settleUpAmountDTOResponse.getAmount() - currSettleUp.getAmount();

                    if (amount > 0){
                        settleUpAmountDTOResponse.setAmount(amount);
                        finalResponse.add(settleUpAmountDTOResponse);
                    } else if (amount < 0) {
                        currSettleUp.setAmount(Math.abs(amount));
                        finalResponse.add(currSettleUp);
                    }
                }
            }
            if (!isMatchFound){
                finalResponse.add(settleUpAmountDTOResponse);
            }
        }
        return finalResponse;
    }


    private List<SettleUpAmountDTOResponse> adjustFinalAmounts(List<SettleUpAmountDTOResponse> finalResponse, Room room, List<SettleUpAmountDTOProjection> settleUpList){
        List<SettleUpAmountDTOResponse> listRemoveSettleUP = new ArrayList<>();

        for (SettleUpAmountDTOResponse settleUpDTOResponse : finalResponse){
            for (SettleUpAmountDTOProjection existingSettleUp : settleUpList){

                if (Integer.parseInt(String.valueOf(settleUpDTOResponse.getFrom().getId())) == existingSettleUp.getPaidBy() && Integer.parseInt(String.valueOf(settleUpDTOResponse.getTo().getId()))  == existingSettleUp.getPaidTo()){

                    double amount = settleUpDTOResponse.getAmount() - existingSettleUp.getAmount();

                    if (amount == 0){
                        listRemoveSettleUP.add(settleUpDTOResponse);
                    }

                    if(amount < 0 && settleUpDTOResponse.isStatus()){
                        settleUpDTOResponse.setAmount(Math.abs(amount));
                        settleUpDTOResponse.setStatus(false);
                    }

                    if(amount > 0){
                        settleUpDTOResponse.setAmount(amount);
                    }
                    break;
                }
            }
        }

        for (SettleUpAmountDTOResponse currRemoveSettleUp : listRemoveSettleUP){
            finalResponse.remove(currRemoveSettleUp);
        }

        return finalResponse;
    }

}