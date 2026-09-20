package br.edu.ucsal.pokesal.model.enums;

public enum Status {
    NORMAL(0.0, 0.0),
    QUEIMADO(0.0625, 0.5),    
    ENVENENADO(0.125, 0.0),   
    PARALISADO(0.0, 0.5);     

    private final double danoPorTurno;
    private final double redutorAtributo;

    Status(double danoPorTurno, double redutorAtributo) {
        this.danoPorTurno = danoPorTurno;
        this.redutorAtributo = redutorAtributo;
    }

    public double getDanoPorTurno() {
        return danoPorTurno;
    }

    public double getRedutorAtributo() {
        return redutorAtributo;
    }
}