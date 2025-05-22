package it.unibo.javadyno.model.dyno.simulated.api;

public interface Vehicle{
    
    /**
     * @return Engine
     */
    Engine getEngine();

    /**
     * @return Transmission
     */
    Transmission getTransmission();

    /**
     * @return Ecu
     */
    Ecu getEcu();

    /**
     * 
     * @param dt step of simulation of duration dt(s) 
     */
    default void update(double dt){
        //TODO: update of single components
        //TODO: signal ecu for warnings
        
    }

}
