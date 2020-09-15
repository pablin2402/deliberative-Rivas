package deliberative.template.rivas;

import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.*;

public class BFS {
    protected State initState(Vehicle vehicle, TaskSet taskSet) {
        City city = vehicle.getCurrentCity();
        List<Task> tasksWaitingToBePickUp = new ArrayList<>(taskSet);
        List<Task> tasksWaitingToBeDeliver = new ArrayList<>(vehicle.getCurrentTasks());
        return new State(city,0.0 ,Collections.emptyList(), tasksWaitingToBePickUp, tasksWaitingToBeDeliver);
    }
    protected State getBreathFirstSearch(Vehicle vehicle, TaskSet taskSet) {
        State firstNode = initState(vehicle, taskSet);

        List<State> q = new LinkedList<>();
        List<State> cost = new LinkedList<>();
        q.add(firstNode);
        do {
            State firstElement = q.get(0);
            q.remove(0);
            if (firstElement.goalState()) {
                return firstElement;
            }else {
                boolean currentAction = false;
                for (State currentState : cost) {
                    if (currentState.equals(firstElement)) {
                        currentAction = true;
                        break;
                    }
                }
                if (!currentAction) {
                    cost.add(firstElement);
                    q.addAll(firstElement.generateSuccesors(firstElement, vehicle.capacity()));
                }
            }
        } while (!q.isEmpty());
        return null;
    }


}
