package com.example.application.views.main;


import com.example.application.network.VisJsEdge;
import com.example.application.network.VisJsNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.dom.Element;

import java.util.ArrayList;
import java.util.List;

@JsModule("./visjs-test.ts")
@NpmPackage(value = "vis", version = "0.110.0")
@Tag("custom-tag")
public class VisJs extends Component {

    List<VisJsNode> nodes = new ArrayList<>();
    List<VisJsEdge> edges = new ArrayList<>();

    public VisJs(List<VisJsEdge> edges, List<VisJsNode> nodes) throws JsonProcessingException {
        ObjectWriter owForEdges = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonEdges = owForEdges.writeValueAsString(edges);
        String jsonNodes = owForNodes.writeValueAsString(nodes);

        setId("customId");

        getElement().executeJs("window.initThree($0, $1, $2)", this, jsonEdges, jsonNodes);
    }
    public VisJs(List<VisJsEdge> edges, List<VisJsNode> nodes, Boolean bool){

        this.edges = edges;
        this.nodes = nodes;

    }

    public void initConnection() throws JsonProcessingException {

        ObjectWriter owForEdges = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonEdges = owForEdges.writeValueAsString(this.edges);
        String jsonNodes = owForNodes.writeValueAsString(this.nodes);

        setId("customId");

        getElement().executeJs("window.initThree($0, $1, $2)", this, jsonEdges, jsonNodes);
    }

    public void addNode(VisJsNode node, VisJsEdge connectToNodeId) throws JsonProcessingException {
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ObjectWriter owForEdges = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonNode = owForNodes.writeValueAsString(node);
        String jsonEdge = owForEdges.writeValueAsString(connectToNodeId);
        getElement().executeJs("window.addEdge($0, $1, $2)", this, jsonNode, jsonEdge);
    }

    public void refresh(List<VisJsEdge> edges, List<VisJsNode> nodes) throws JsonProcessingException {
        ObjectWriter owForEdges = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonEdges = owForEdges.writeValueAsString(edges);
        String jsonNodes = owForNodes.writeValueAsString(nodes);


        getElement().executeJs("window.redraw($0, $1, $2)", this, jsonEdges, jsonNodes);



    }

    public void getNodesCoordinates(){
        getElement().executeJs("window.getCoordinates($0)", this);
    }

    public void updateNode(VisJsNode node) throws JsonProcessingException {
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonNode = owForNodes.writeValueAsString(node);
        getElement().executeJs("window.updateNode($0, $1)", this, jsonNode);
    }

    public void deleteNode(VisJsNode node) throws JsonProcessingException {
        ObjectWriter owForNodes = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String jsonNode = owForNodes.writeValueAsString(node);
        getElement().executeJs("window.updateNode($0, $1)", this, jsonNode);
    }

}
