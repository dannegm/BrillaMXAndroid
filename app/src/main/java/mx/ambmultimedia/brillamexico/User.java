package mx.ambmultimedia.brillamexico;

import java.util.ArrayList;

/**
 * Created by ambmultimedia on 03/03/15.
 */
public class User {
    private Integer _fbID;
    private String _name;
    private String _bio;

    private Integer _campoDeAccion;
    private ArrayList<Integer> _compromisos = new ArrayList<Integer>();

    private Integer _puntos;

    // Constructor
    public User () {
        // Constructor requiere un método vacío
    }

    // Setea usuario
    public void setUser(Integer nFbID, String nName, String nBio, Integer nCampoDeAccion) {
        this._fbID = nFbID; this._name = nName; this._bio = nBio; this._campoDeAccion = nCampoDeAccion;
    }

    // Devuelve datos básicos
    public String Name () {
        return this._name;
    }
    public String Bio () {
        return this._bio;
    }
    public Integer CampoDeAccion () {
        return this._campoDeAccion;
    }

    // Añade compromiso
    public void addCompromiso (Integer nCompromiso) {
        int index = this._compromisos.indexOf(nCompromiso);
        if (index < 0) {
            this._compromisos.add(nCompromiso);
        }
    }
    // Regresa compromisos
    public ArrayList<Integer> Compromisos () {
        return this._compromisos;
    }

    // Añade puntos
    public void addPuntos (Integer nPuntos) {
        this._puntos += nPuntos;
    }
    // Regresa puntos
    public Integer Puntos() {
        return this._puntos;
    }
}
