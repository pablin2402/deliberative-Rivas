package deliberative.template.rivas;

import logist.plan.Action;

import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.*;

public class State {
    private City currentCity;
    //actions
    private List<Action> currentActions;
    //aceptadas
    private TaskSet availableTask;
    //a ser entregadas
    private TaskSet deliverTo;
    private Vehicle vehicle;
    public State (){}

    public State(City currentCity, List<Action> currentActions, TaskSet currentTask, TaskSet deliverTo) {
        this.currentCity = currentCity;
        this.currentActions = currentActions;
        this.availableTask = currentTask;
        this.deliverTo = deliverTo;
    }

    //TODO: need to implement builder pattern
    public State(Vehicle vehicle, TaskSet taskSet) {
        this.currentCity = vehicle.getCurrentCity();
        this.currentActions = Collections.<Action>emptyList();
        this.availableTask = TaskSet.copyOf(taskSet);
        this.deliverTo = TaskSet.copyOf(taskSet);
        this.vehicle = vehicle;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public List<Action> getCurrentActions() {
        return currentActions;
    }

    public void setCurrentActions(List<Action> currentActions) {
        this.currentActions = currentActions;
    }

    public TaskSet getCurrentTask() {
        return availableTask;
    }

    public void setCurrentTask(TaskSet currentTask) {
        this.availableTask = currentTask;
    }

    public TaskSet getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(TaskSet deliverTo) {
        this.deliverTo = deliverTo;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return
                Objects.equals(currentCity, state.currentCity) &&
                        Objects.equals(currentActions, state.currentActions) &&
                        Objects.equals(availableTask, state.availableTask) &&
                        Objects.equals(deliverTo, state.deliverTo) &&
                        Objects.equals(vehicle, state.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCity, currentActions, availableTask, deliverTo, vehicle);
    }

    private int remainingCapacity() {
        int weight = 0;
        for (Task currentTask : deliverTo) {
            weight += currentTask.weight;
        }
        return weight;
    }

    protected List<State> generateSuccesors(State currentState, int remainingCapacity) {
        List<State> generateStates = new LinkedList<>();
        int capacity;
        //pick up
        boolean pickUp=false;
        for (Task task : availableTask) {
            capacity = remainingCapacity - currentState.remainingCapacity();
            if (capacity >= task.weight) {
                pickUp =true;
                currentState = currentState.pickup(task);

            }
        }
        City currentCity = currentState.getCurrentCity();
        //delivery
        for (Task task : deliverTo) {
            if(task.deliveryCity.equals(currentCity)){
                currentState = currentState.deliver(task);
            }
        }
        generateStates.add(currentState);
        return generateStates;

    }

    //lista de los paquetes a recoger
    private List<Task> pickUpPackages(City currentCity) {
        List<Task> pickUp = new LinkedList<>();
        for (Task t : availableTask) {
            if (t.pickupCity == currentCity) {
                pickUp.add(t);
            }
        }
        return pickUp;
    }

    protected boolean goalState() { return availableTask.isEmpty() && deliverTo.isEmpty(); }
    public State move(City c) {
        List<Action> act = new ArrayList<>();
        act.add(new Action.Move(c));
        return new State(c, act,this.availableTask, this.deliverTo);
    }

    public State deliver(Task t) {
        List<Task> deliverTo = new ArrayList<>();
        deliverTo.remove(t);
        List<Action> act = new ArrayList<>();
        act.add(new Action.Delivery(t));
        return new State(this.currentCity, act, this.availableTask,this.deliverTo);
    }

    public State pickup(Task t) {
        List<Task> newTasks = new ArrayList<>();
        List<Task> deliverTo = new ArrayList<>();
        List<Action> act = new ArrayList<>();
        act.add(new Action.Pickup(t));
        newTasks.remove(t);
        deliverTo.add(t);
        return new State(this.currentCity,act,this.availableTask,this.deliverTo);
    }

}