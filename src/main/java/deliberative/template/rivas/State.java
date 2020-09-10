package deliberative.template.rivas;

import logist.plan.Plan;
import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class State {
    private City currentCity;
    //actions
    private Plan currentPlan;
    //aceptadas
    private TaskSet availableTask;
    //a ser entregadas
    private TaskSet deliverTo;
    private Vehicle vehicle;
    private int remainingCapacity;

    public State(City currentCity, Plan currentPlan, TaskSet currentTask, TaskSet deliverTo, Vehicle vehicle, int remainingCapacity) {
        this.currentCity = currentCity;
        this.currentPlan = currentPlan;
        this.availableTask = currentTask;
        this.deliverTo = deliverTo;
        this.vehicle = vehicle;
        this.remainingCapacity = remainingCapacity;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
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

    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(int remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return remainingCapacity == state.remainingCapacity &&
                Objects.equals(currentCity, state.currentCity) &&
                Objects.equals(currentPlan, state.currentPlan) &&
                Objects.equals(availableTask, state.availableTask) &&
                Objects.equals(deliverTo, state.deliverTo) &&
                Objects.equals(vehicle, state.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCity, currentPlan, availableTask, deliverTo, vehicle, remainingCapacity);
    }

   protected  List <State> generateSuccesors(){
        List <State> generateStates = new LinkedList<>();
        List <Task>  generateTasks = new LinkedList<>();
        //pick up
        for(Task task : availableTask){

            for(City currentCity : currentCity.pathTo(task.pickupCity)){
                currentPlan.appendMove(currentCity);
            }
            currentCity = task.pickupCity;
            currentPlan.appendPickup(task);
            availableTask.remove(task);
            deliverTo.add(task);
        }

        //delivery
        for(Task task : deliverTo){
            for(City currentCity : currentCity.pathTo(task.deliveryCity)){
                currentPlan.appendMove(currentCity);

            }
        }
        return generateStates;

   }
   //lista de los paquetes a recoger
   private List<Task> pickUpPackages(City currentCity){
       List<Task> pickUp = new LinkedList<>();
       for (Task t : availableTask) {
           if (t.pickupCity == currentCity) {
               pickUp.add(t);
           }
       }
       return pickUp;
   }
   protected boolean goalState(){
        return availableTask.isEmpty() && deliverTo.isEmpty();
   }



}
