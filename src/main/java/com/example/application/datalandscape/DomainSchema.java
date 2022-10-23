package com.example.application.datalandscape;

import com.example.application.network.VisJsEdge;
import com.example.application.network.VisJsNode;
import com.example.application.schema.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DomainSchema {
    public Schema getSchema() {
        return schema;
    }
    Schema schema;

    List<VisJsNode> anchorList = new ArrayList<>();
    List<VisJsNode> attributeList = new ArrayList<>();
    List<VisJsNode> knotList = new ArrayList<>();
    List<VisJsNode> tieList = new ArrayList<>();

    List<VisJsEdge> edgeList = new ArrayList<>();

    public DomainSchema(InputStream schemaXML) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Schema> e =  (JAXBElement<Schema> )unmarshaller.unmarshal(schemaXML);
            schema = e.getValue();
            getNodes();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void getNodes(){
        getAnchorAndAttribute();
        getKnot();
        getTie();
    }

    public void getAnchorAndAttribute(){
        for(Anchor anchor : schema.getAnchor()){
            anchorList.add(new VisJsNode(anchor));
            for(Attribute attribute : anchor.getAttribute()){
                attributeList.add(new VisJsNode(anchor, attribute));
                edgeList.add(new VisJsEdge(anchor, attribute));
                if(attribute.getKnotRange() != null){
                    edgeList.add(new VisJsEdge(attribute, anchor));
                }
            }
        }
    }
    public void getKnot(){
        for(Knot knot : schema.getKnot()){
            knotList.add(new VisJsNode(knot));
        }
    }
    public void getTie(){
        for(Tie tie :schema.getTie()){
            VisJsNode tieNode = new VisJsNode(tie);
            tieList.add(tieNode);
            for(AnchorRole anchorRole : tie.getAnchorRole()){
                edgeList.add(new VisJsEdge(tieNode.getId(), anchorRole.getType()));
            }
            if(tie.getKnotRole() != null){
                edgeList.add(new VisJsEdge(tieNode.getId(), tie.getKnotRole().getType()));
            }
        }
    }
    public String getAnchors() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.anchorList);
    }
    public String getAttributes() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.attributeList);
    }
    public String getKnots() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.knotList);
    }
    public String getTies() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.tieList);
    }
    public String getEdges() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.edgeList);
    }
    public List<VisJsNode> getAnchorList(){
        return this.anchorList;
    }
    public List<VisJsNode> getAttributeList(){
        return this.attributeList;
    }
    public List<VisJsNode> getKnotList(){
        return this.knotList;
    }
    public List<VisJsNode> getTieList(){
        return this.tieList;
    }
    public List<VisJsEdge> getEdgeList(){
        return this.edgeList;
    }


//    public String getAnchors () throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(schema.getAnchor());
//    }
}
