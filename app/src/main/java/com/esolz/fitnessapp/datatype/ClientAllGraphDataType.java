package com.esolz.fitnessapp.datatype;

import java.util.ArrayList;

/**
 * Created by ltp on 05/08/15.
 */
public class ClientAllGraphDataType {

    String graphId, graph_type, graph_for, measure_unit, goal, deadline;
    ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList;

    public ClientAllGraphDataType(String graphId, String graph_type, String graph_for, String measure_unit, String goal, String deadline,
                                  ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList) {
        this.graphId = graphId;
        this.graph_type = graph_type;
        this.graph_for = graph_for;
        this.measure_unit = measure_unit;
        this.goal = goal;
        this.deadline = deadline;
        this.clientAllGraphPointDataTypeArrayList = clientAllGraphPointDataTypeArrayList;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getGraph_type() {
        return graph_type;
    }

    public void setGraph_type(String graph_type) {
        this.graph_type = graph_type;
    }

    public String getGraph_for() {
        return graph_for;
    }

    public void setGraph_for(String graph_for) {
        this.graph_for = graph_for;
    }

    public String getMeasure_unit() {
        return measure_unit;
    }

    public void setMeasure_unit(String measure_unit) {
        this.measure_unit = measure_unit;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public ArrayList<ClientAllGraphPointDataType> getClientAllGraphPointDataTypeArrayList() {
        return clientAllGraphPointDataTypeArrayList;
    }

    public void setClientAllGraphPointDataTypeArrayList(ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList) {
        this.clientAllGraphPointDataTypeArrayList = clientAllGraphPointDataTypeArrayList;
    }
}