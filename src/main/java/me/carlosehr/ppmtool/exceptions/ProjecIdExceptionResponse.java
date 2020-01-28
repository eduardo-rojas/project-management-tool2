package me.carlosehr.ppmtool.exceptions;



public class ProjecIdExceptionResponse {

    private String projectIdentifier;

    public ProjecIdExceptionResponse(String projectIdentifier){
        this.projectIdentifier = projectIdentifier;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
