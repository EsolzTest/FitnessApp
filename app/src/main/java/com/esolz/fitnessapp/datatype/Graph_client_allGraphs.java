package com.esolz.fitnessapp.datatype;

/**
 * Created by su on 18/6/15.
 */
public class Graph_client_allGraphs {
    String graph_id;
    String graph_type;
    String graph_for;
    String measure_unit;
    String x_axis_point;
    String y_axis_point;

    public Graph_client_allGraphs(String graph_id, String y_axis_point, String x_axis_point, String measure_unit, String graph_for, String graph_type) {
        this.graph_id = graph_id;
        this.y_axis_point = y_axis_point;
        this.x_axis_point = x_axis_point;
        this.measure_unit = measure_unit;
        this.graph_for = graph_for;
        this.graph_type = graph_type;
    }

    public String getGraph_id() {
        return graph_id;
    }

    public void setGraph_id(String graph_id) {
        this.graph_id = graph_id;
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

    public String getX_axis_point() {
        return x_axis_point;
    }

    public void setX_axis_point(String x_axis_point) {
        this.x_axis_point = x_axis_point;
    }

    public String getY_axis_point() {
        return y_axis_point;
    }

    public void setY_axis_point(String y_axis_point) {
        this.y_axis_point = y_axis_point;
    }
}
