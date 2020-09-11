package deliberative.template.rivas;

import logist.simulation.Vehicle;
import logist.task.TaskSet;

import java.util.LinkedList;
import java.util.List;

public class BFS {
    protected State getBreathFirstSearch(Vehicle vehicle, TaskSet taskSet){
        List<State> q = new LinkedList<>();
        List <State> cost = new LinkedList<>();
        State firstNode = new State(vehicle,taskSet);
        q.add(firstNode);
        do{
            State firstElement =q.get(0);
            q.remove(0);
            if(firstElement.goalState()){
                return firstElement;
            }
            boolean cycle = false;
            for(State s: cost) {
                if(s.equals(firstElement)) {
                    cycle = true;
                    break;
                }
            }
            State state =new State();
            if(! cycle) {
                cost.add(firstElement);
                q.addAll(state.generateSuccesors(firstElement, vehicle.capacity()));
            }

        }while (!q.isEmpty());
        //TODO: need to change for Optional.isEMPTY
        return null;
    }
}
