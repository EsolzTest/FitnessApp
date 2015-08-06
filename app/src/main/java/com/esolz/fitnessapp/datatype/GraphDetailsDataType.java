package com.esolz.fitnessapp.datatype;

import java.util.ArrayList;

/**
 * Created by ltp on 17/07/15.
 */
public class GraphDetailsDataType {

    String client_name, graph_type, graph_for, measure_unit, goal, deadline;
    ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypes;

    public GraphDetailsDataType(String client_name, String graph_type, String graph_for, String measure_unit, String goal, String deadline,
                                ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypes) {
        this.client_name = client_name;
        this.graph_type = graph_type;
        this.graph_for = graph_for;
        this.measure_unit = measure_unit;
        this.goal = goal;
        this.deadline = deadline;
        this.graphDetailsPointDataTypes = graphDetailsPointDataTypes;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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

    public ArrayList<GraphDetailsPointDataType> getGraphDetailsPointDataTypes() {
        return graphDetailsPointDataTypes;
    }

    public void setGraphDetailsPointDataTypes(ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypes) {
        this.graphDetailsPointDataTypes = graphDetailsPointDataTypes;
    }
}