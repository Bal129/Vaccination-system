package JavaDir;

import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;

public class VcSimulationHall {
    // private int dose;
    private final int SENIOR_AGE = 60;
    private Queue<Recipient> queueSenior = new LinkedList<>();
    private Queue<Recipient> queueNormal = new LinkedList<>();
    private Stack<Vaccine> stackVaccines = new Stack<>();
    private Queue<Recipient> queueVaccinated = new LinkedList<>();

    ////////////// constructor //////////////

    VcSimulationHall() {}

    VcSimulationHall(ArrayList<Recipient> recipientList, LinkedList<Vaccine> vaccines) {
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).getAge() >= SENIOR_AGE)
                queueSenior.offer(recipientList.get(i));
            else 
                queueNormal.offer(recipientList.get(i));
            Vaccine checkVaccine = vaccines.get(i);
            if (checkVaccine != null) {
                stackVaccines.add(checkVaccine);    
            }
        }
    }

    ////////////// getter //////////////

    public Queue<Recipient> getQueueNormal() {
        return queueNormal;
    }

    public Queue<Recipient> getQueueSenior() {
        return queueSenior;
    }

    public Stack<Vaccine> getStackVaccines() {
        return stackVaccines;
    }

    public Queue<Recipient> getQueueVaccinated() {
        return queueVaccinated;
    }

    ////////////// setter //////////////

    public void setQueueNormal(Queue<Recipient> queueNormal) {
        this.queueNormal = queueNormal;
    }

    public void setQueueSenior(Queue<Recipient> queueSenior) {
        this.queueSenior = queueSenior;
    }

    public void setStackVaccines(Stack<Vaccine> stackVaccines) {
        this.stackVaccines = stackVaccines;
    }

    ////////////// function //////////////

    public void next(int vcId, int targetDose) throws Exception {
        next(queueSenior, vcId, targetDose);
        next(queueNormal, vcId, targetDose);
    };

    private void next(Queue<Recipient> recipientQueue, int vcId, int targetDose) throws Exception {
        if (!recipientQueue.isEmpty()) {
            Recipient curRecipient = recipientQueue.poll();
            Vaccine vaccine = stackVaccines.pop();
            vaccine.setRecipientId(curRecipient.getId());
            queueVaccinated.add(curRecipient);
            curRecipient.getStatus().setVaccine(vaccine);
            Finder.getVc(vcId).updateStatus(curRecipient.getId(), targetDose);
        }
    }
}

