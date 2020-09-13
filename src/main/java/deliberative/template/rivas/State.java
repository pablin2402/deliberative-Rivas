package deliberative.template.rivas;

import logist.plan.Action;

import logist.task.Task;
import logist.topology.Topology.City;

import java.util.*;

public class State {
    private City currentCity;
    // currentActions
    private List<Action> currentActions;
    // aceptadas
    private List<Task> availableTask;
    // a ser entregadas
    private List<Task> deliverTo;


    public State(City currentCity, List<Action> currentActions, List<Task> availableTask, List<Task> deliverTo) {
        this.currentCity = currentCity;
        this.currentActions = currentActions;
        this.availableTask = availableTask;
        this.deliverTo = deliverTo;
    }

    public State(State s) {
        this.currentCity = s.currentCity;
        this.availableTask = s.availableTask;
        this.deliverTo = s.deliverTo;
        this.currentActions = s.currentActions;

    }
    public City getCurrentCity() {
        return currentCity;
    }

    public List<Action> getCurrentActions() {
        return currentActions;
    }
    public List<Task> getAvailableTask() {
        return availableTask;
    }
    public List<Task> getDeliverTo() {
        return deliverTo;
    }
    public boolean equals(State s) {
        return this.currentCity.equals(s.getCurrentCity()) && this.availableTask.equals(s.getAvailableTask())
                && this.deliverTo.equals(s.getDeliverTo());
    }
    private List<Task> pickUpAvailableTasks() {
        List<Task> availableTasksToBeCollected = new ArrayList<>();
        this.availableTask.stream()
                .filter(taskWaitingToBePickUp -> taskWaitingToBePickUp.pickupCity.equals(this.currentCity))
                .forEach(availableTasksToBeCollected::add);
        return availableTasksToBeCollected;
    }

    private int remainingCapacity() {
        return this.deliverTo.stream().map(task -> task.weight).reduce(0, Integer::sum);
    }

    protected List<State> generateSuccesors(State currentState, int carLoadCapacity) {
        List<State> generateStates = new ArrayList<>();
        City currentCityForGetNeighbors = currentState.getCurrentCity();
        currentState = tasksToBeDeliver(currentState, currentCityForGetNeighbors);
        generateStates.add(currentState);
        State state = new State(currentState);
        int freeCharge;
        boolean pick = false;
        for (Task taskToBePickUP : currentState.pickUpAvailableTasks()) {
            freeCharge = carLoadCapacity - currentState.remainingCapacity();
            if (freeCharge >= taskToBePickUP.weight) {
                pick = true;
                currentState = currentState.pickupTask(taskToBePickUP);
            }
        }
        State finalCurrentState = currentState;
        currentCityForGetNeighbors.neighbors().stream()
                .map(finalCurrentState::moveAction)
                .forEach(generateStates::add);
        if (pick) {
            currentCityForGetNeighbors.neighbors().stream()
                    .map(state::moveAction)
                    .forEach(generateStates::add);

        }

        return generateStates;
    }

    private State tasksToBeDeliver(State currentState, City currentCityForGetNeighbors) {
        for (Task taskToBeDeliver : currentState.getDeliverTo()) {
            if (taskToBeDeliver.deliveryCity.equals(currentCityForGetNeighbors)) {
                currentState = currentState.deliverTo(taskToBeDeliver);
            }
        }
        return currentState;
    }

    public State moveAction(City currentCityNeighbors2) {
        List<Action> listOfActions = new ArrayList<>(this.currentActions);
        listOfActions.add(new Action.Move(currentCityNeighbors2));
        return new State(currentCityNeighbors2, listOfActions, this.availableTask, this.deliverTo);
    }

    public State deliverTo(Task taskToDeliver) {
        List<Task> listOfDeliverTasks = new ArrayList<>(this.deliverTo);
        listOfDeliverTasks.remove(taskToDeliver);
        List<Action> listOfActions = new ArrayList<>(this.currentActions);
        listOfActions.add(new Action.Delivery(taskToDeliver));
        return new State(this.currentCity,listOfActions ,this.availableTask, listOfDeliverTasks);
    }

    public State pickupTask(Task taskToPickUp) {
        List<Task> listOfNewTasks = new ArrayList<>(this.availableTask);
        List<Task> listOfDeliverTasks = new ArrayList<>(this.deliverTo);
        List<Action> listOfActions = new ArrayList<>(this.currentActions);
        listOfActions.add(new Action.Pickup(taskToPickUp));
        listOfNewTasks.remove(taskToPickUp);
        listOfDeliverTasks.add(taskToPickUp);
        return new State(this.currentCity, listOfActions,listOfNewTasks, listOfDeliverTasks);
    }
    protected boolean goalState() {
        return availableTask.isEmpty() && deliverTo.isEmpty();
    }


}